package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.exception.ExecuteException;
import cn.com.fishin.lighter.common.helper.LogHelper;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认的队列拒绝策略处理器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 21:45:37</p>
 */
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 此时可以直接抛出异常，当作缓存没有命中
        RuntimeException exception = new ExecuteException("Thread pool is full! Task " + r + " is threw away!");

        // 记录错误
        LogHelper.error(exception.getMessage(), exception);
        throw exception;
    }
}
