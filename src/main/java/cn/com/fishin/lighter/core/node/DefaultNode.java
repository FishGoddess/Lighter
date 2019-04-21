package cn.com.fishin.lighter.core.node;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.GracefulHelper;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.LighterArgument;
import cn.com.fishin.tuz.core.Tuz;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认的节点实现类
 * 使用 java.util.HashMap 来做基层数据容器
 * 目前实现了懒存活时间，也就是一个 key 只有在获取时才会被消除
 * 这样做的好处是不用占用而外的通知线程去清除过期的键值
 * 但是这样也会导致这个过期时间是一种假过期，内存依旧被占用着，造成浪费
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 11:06:56</p>
 */
public class DefaultNode implements Node {

    // 存储数据的容器
    // 注意这里针对初始值进行了调整，由于线程数肯定是根据业务量来定的，所以这里取线程数的两倍作为初始值
    // 另外，这里原本是使用 juc 包中的 ConcurrentHashMap 的，但是由于这里有可能有多步操作合成一步原子操作
    // 所以锁是无法避免的了，既然如此，还是使用更轻量级的 HashMap 吧，由我自己来控制读写锁的问题
    // 经过多次测试，改成读写锁的情况下，单次读取速度几乎没有损失，多次读写混合操作下损失 4% 左右的性能
    private static final int INITIAL_CAPACITY = (Integer.valueOf(Tuz.use("corePoolSize")) << 2);
    private Map<String, Value> map = new HashMap<>(INITIAL_CAPACITY);

    // 根据死亡时间升序排序的优先队列
    // 每一次删除一些已经过期的数据，根据 GC_PER_SECOND 的时间间隔来清理
    private static final int MAX_GC_COUNT = 32;
    private static final int GC_PER_SECOND = Integer.valueOf(Tuz.useGracefully("GcPerSecond", "60"));
    private PriorityQueue<Value> deadlineQueue = new PriorityQueue<>(Comparator.comparingLong(o -> o.deadline));

    // 使用读写锁来保证读取更高效
    // 由于缓存的特性就是大量读取，所以要尽可能地保证读取地效率，而写入效率可以适当降低
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    // 初始化清理哨兵线程
    public DefaultNode() {
        // 设置为守护线程，等服务停止，这个定时任务就停止了
        Timer timer = new Timer("Deadline_Timer", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LogHelper.info("Begins to cleanup the dead key! Current size of deadline queue ===> " + deadlineQueue.size());
                cleanupDeadlineQueue();
                LogHelper.info("Finished one task! Current size of deadline queue ===> " + deadlineQueue.size());
            }
        }, new Date(), GC_PER_SECOND * 1000);
    }

    @Override
    public Object get(Task task) {
        return new LockTemplate<Object>() {
            @Override
            public Object doInLockBlock() {
                return getByKey(task.getKey());
            }
        }.executeSafely(readLock);
    }

    @Override
    public Object set(Task task) {
        return new LockTemplate<Object>() {
            @Override
            public Object doInLockBlock() {
                Value value = Value.from(task);
                map.put(task.getKey(), value);
                addToDeadlineQueue(value);
                return value.value;
            }
        }.executeSafely(writeLock);
    }

    @Override
    public Object setAbsent(Task task) {
        return new LockTemplate<Object>() {
            @Override
            public Object doInLockBlock() {
                Value value = Value.from(task);
                if (map.putIfAbsent(task.getKey(), value) == null) {
                    // 如果返回为 null，说明是第一次插入这个值，添加进死亡队列
                    addToDeadlineQueue(value);
                }

                return value.value;
            }
        }.executeSafely(writeLock);
    }

    @Override
    public Object remove(Task task) {
        return new LockTemplate<Object>() {
            @Override
            public Object doInLockBlock() {
                Value value = map.remove(task.getKey());
                return value == null ? null : value.value;
            }
        }.executeSafely(writeLock);
    }

    @Override
    public boolean exists(Task task) {
        return new LockTemplate<Boolean>() {
            @Override
            public Boolean doInLockBlock() {
                return getByKey(task.getKey()) != null;
            }
        }.executeSafely(readLock);
    }

    @Override
    public int numberOfKeys() {
        return new LockTemplate<Integer>() {
            @Override
            public Integer doInLockBlock() {
                return map.size();
            }
        }.executeSafely(readLock);
    }

    @Override
    public String[] keys() {
        return new LockTemplate<String[]>() {
            @Override
            public String[] doInLockBlock() {
                List<String> keys = new LinkedList<>();
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();

                    // 这里主要是判断这个 key 是否已经失效过期
                    if (map.get(key).hasDead()) {
                        iterator.remove();
                        continue;
                    }
                    keys.add(key);
                }
                map.keySet().forEach(key -> {

                });
                return keys.toArray(new String[0]);
            }
        }.executeSafely(readLock);
    }

    @Override
    public Object[] values() {
        return new LockTemplate<Object[]>() {
            @Override
            public Object[] doInLockBlock() {
                List<Object> values = new LinkedList<>();
                Iterator<Value> iterator = map.values().iterator();
                while (iterator.hasNext()) {
                    Value value = iterator.next();
                    // 这里主要是判断这个 value 是否已经失效过期
                    if (value.hasDead()) {
                        iterator.remove();
                        continue;
                    }

                    values.add(value.value);
                }
                return values.toArray(new Object[0]);
            }
        }.executeSafely(readLock);
    }

    // 添加进死亡队列
    private void addToDeadlineQueue(Value value) {
        if (value.willBeDead()) {
            // 只有这个值有可能会死亡才放进清除队列
            deadlineQueue.add(value);
        }
    }

    // 清理过期键值数据
    private void cleanupDeadlineQueue() {
        for (int i = 0; i < MAX_GC_COUNT && !deadlineQueue.isEmpty(); i++) {
            Value value = deadlineQueue.peek();

            // 如果第一个值都没有死亡，说明后面的也都没有死亡，就没必要继续清理了
            if (!value.hasDead()) {
                break;
            }

            // 清理这个过期数据
            map.remove(value.key);
            deadlineQueue.poll();
        }
    }

    // 锁代码模板
    private static abstract class LockTemplate<Result> {

        // 线程安全
        Result executeSafely(Lock lock) {
            lock.lock();
            try {
                return doInLockBlock();
            } finally {
                lock.unlock();
            }
        }

        // 在安全的锁区域里执行
        public abstract Result doInLockBlock();
    }

    // 根据 key 值来获取数据值
    private Object getByKey(String key) {

        Value result = map.get(key);

        // 先判断是否存在
        if (result == null) {
            return null;
        }

        // 存在时还要判断是否存活
        if (result.hasDead()) {
            // 已经死亡就直接移除掉
            // 注意这个是线程不安全的！但在这里是可以接受的
            map.remove(key);
            return null;
        }

        return result.value;
    }

    // 要被保存的数据，包括数据值和过期时间
    private static class Value {

        String key = null;
        Object value = null;
        long deadline = -1; // 永不过期

        Value(String key, Object value, long deadline) {
            this.key = key;
            this.value = value;
            this.deadline = deadline;
        }

        // 从一个任务上获取数据值
        static Value from(Task task) {
            return new Value(task.getKey(), task.getValue(), deadline(task));
        }

        // 死亡时间
        private static long deadline(Task task) {

            // 如果有设置存活时间，就要计算这个值的死亡时间
            if (GracefulHelper.isNotNull(expiredTime(task))) {
                // 注意这个单位是秒。。。
                return System.currentTimeMillis() + (Long.valueOf(expiredTime(task)) * 1000);
            }

            // 永不过期
            return -1;
        }

        // 获取存活时间
        private static String expiredTime(Task task) {
            return (String) task.getArgument(LighterArgument.EXPIRE_TIME_ARGUMENT);
        }

        // 判断这个值是否已经过期
        boolean hasDead() {
            return willBeDead() && deadline <= System.currentTimeMillis();
        }

        // 判断这个值是否会死亡
        boolean willBeDead() {
            return deadline != -1;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value=" + value +
                    ", deadline=" + deadline +
                    '}';
        }
    }
}
