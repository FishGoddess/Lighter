package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 执行任务的执行器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 20:56:55</p>
 */
public interface TaskExecutor {

    // 执行任务的方法
    Object execute(Task task);
}
