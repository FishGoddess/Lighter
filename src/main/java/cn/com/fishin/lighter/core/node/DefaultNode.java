package cn.com.fishin.lighter.core.node;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.tuz.core.Tuz;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的节点实现类
 * 使用 java.util.ConcurrentHashMap
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 11:06:56</p>
 */
public class DefaultNode implements Node {

    // 存储数据的容器
    // 注意这里针对初始值进行了调整，由于线程数肯定是根据业务量来定的，所以这里取线程数的两倍作为初始值
    private static final int INITIAL_CAPACITY = (Integer.valueOf(Tuz.use("corePoolSize")) << 2);
    private Map<String, Object> map = new ConcurrentHashMap<>(INITIAL_CAPACITY);

    @Override
    public Object get(Task task) {
        return map.get(task.getKey());
    }

    @Override
    public Object set(Task task) {
        return map.put(task.getKey(), task.getValue());
    }

    @Override
    public Object setAbsent(Task task) {
        return map.putIfAbsent(task.getKey(), task.getValue());
    }

    @Override
    public Object remove(Task task) {
        return map.remove(task.getKey());
    }
}
