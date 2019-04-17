package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.executor.TaskExecutor;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.plugin.DiPlugin;

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
    private static final TaskExecutor executor = DiPlugin.useInstance(TaskExecutor.class);

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
}
