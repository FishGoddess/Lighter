package cn.com.fishin.lighter.protocol;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 协议解析器接口
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/15 22:21:30</p>
 */
public interface RequestParser<Request> {

    // 将一次请求解析为一个任务对象
    Task parse(Request request);
}
