package cn.com.fishin.lighter.protocol;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 请求处理器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 10:03:11</p>
 */
public interface RequestHandler<Request> {

    // 处理请求，解析为任务
    Task handle(Request request);
}
