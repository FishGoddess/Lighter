package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.executor.TaskExecutor;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.entity.InterceptedMethod;
import cn.com.fishin.tuz.interceptor.DefaultInterceptor;
import cn.com.fishin.tuz.interceptor.Interceptor;
import cn.com.fishin.tuz.plugin.DiPlugin;
import cn.com.fishin.tuz.plugin.ProxyPlugin;

import java.util.concurrent.*;

/**
 * 任务执行器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 21:09:28</p>
 */
public class LighterExecutor {

    // 任务处理器
    // 这里进行了一层拦截，就是对任务执行来一层次要业务的处理
    private static final TaskExecutor executor = ProxyPlugin.useInstance(
            TaskExecutor.class,
            new Interceptor[]{
                    new TaskExecutorInterceptor()
            });

    // 内置线程池，用来执行任务
    private static ThreadPoolExecutor threadPool;

    // 执行任务
    public static Object submit(Task task) throws Exception {
        return threadPool.submit(() -> executor.execute(task)).get();
    }

    // 初始化执行器
    public static void init() {
        // 初始化线程池
        threadPool = new ThreadPoolExecutor(
                Integer.valueOf(Tuz.use("corePoolSize")),
                Integer.valueOf(Tuz.use("maximumPoolSize")),
                Long.valueOf(Tuz.use("keepAliveTime")),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Integer.valueOf(Tuz.use("waitQueueSize"))),
                DiPlugin.useInstance(RejectedExecutionHandler.class)
        );

        // 输出日志信息
        LogHelper.info("Thread pool inits successfully!");
    }

    // 关闭执行器
    public static void shutdown() {
        // 关闭线程池
        threadPool.shutdown();
        LogHelper.info("Thread pool shutdown " + (threadPool.isShutdown() ? "successfully!" : "failed!"));
    }

    // 任务执行器拦截器
    // 这里需要对执行的任务持久化，以防止服务突然崩溃，导致的任务丢失
    // 在任务执行之后就需要持久化了，如果任务执行失败，就不会持久化了
    private static class TaskExecutorInterceptor extends DefaultInterceptor {
        @Override
        public boolean after(InterceptedMethod method) {

            // TODO 进行任务持久化和日志记录
            return true;
        }
    }
}
