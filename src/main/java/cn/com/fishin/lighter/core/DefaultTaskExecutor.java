package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 默认任务处理器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 22:51:51</p>
 */
public class DefaultTaskExecutor implements TaskExecutor {

    @Override
    public Object execute(Task task) {
        return task;
    }
}
