package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.protocol.RequestParser;
import cn.com.fishin.tuz.plugin.DiPlugin;

/**
 * Lighter 专用解析器
 * 包括协议解析等解析器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 12:16:03</p>
 */
public class LighterParser {

    // 请求解析器
    private static final RequestParser requestParser = DiPlugin.useInstance(RequestParser.class);

    // 解析请求为任务
    @SuppressWarnings("unchecked")
    public static Task parseRequest(Object request) {
        return requestParser.parse(request);
    }
}
