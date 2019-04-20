package cn.com.fishin.lighter.protocol.http;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.GracefulHelper;
import cn.com.fishin.lighter.common.helper.HttpRequestHelper;
import cn.com.fishin.lighter.core.LighterArgument;
import cn.com.fishin.lighter.core.executor.TaskAction;
import cn.com.fishin.lighter.protocol.RequestHandler;
import cn.com.fishin.lighter.protocol.RequestParser;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

/**
 * HTTP 协议解析器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/20 19:27:04</p>
 */
public class HttpRequestParser implements RequestParser<FullHttpRequest> {

    @Override
    public Task parse(FullHttpRequest request) {
        // 根据请求方法来匹配不同的请求处理器
        // 将请求转换成大写来进行匹配
        return HttpMethodMapping.valueOf(action(request)).handler.handle(request);
    }

    // 任务动作枚举类
    // 这里使用的是 HTTP 的 Restful 风格指令动作
    private enum HttpMethodMapping {

        // 主要用来获取数据
        get(request -> Task.make(TaskAction.GET, key(request))),

        // 主要用来新增数据
        set(request -> filledWithParameter(
                request,
                Task.make(
                        TaskAction.SET,
                        key(request),
                        value(request)
                ))
        ),

        // 如果不存在这个 key 值，就插入，否则直接返回
        setAbsent(request -> filledWithParameter(
                request,
                Task.make(
                        TaskAction.SET_ABSENT,
                        key(request),
                        value(request)
                ))
        ),

        // 主要用来删除数据
        remove(request -> Task.make(
                TaskAction.REMOVE,
                key(request))
        );

        // 请求处理器
        private RequestHandler<FullHttpRequest> handler = null;

        HttpMethodMapping(RequestHandler<FullHttpRequest> handler) {
            this.handler = handler;
        }
    }

    // 获取动作
    private static String action(FullHttpRequest request) {
        return HttpRequestHelper.uris(request)[0];
    }

    // 将 uri 前面的 / 截掉
    private static String key(FullHttpRequest request) {
        return HttpRequestHelper.uris(request)[1];
    }

    // 获取 value 值
    private static String value(FullHttpRequest request) {
        return trim(request.content().toString(CharsetUtil.UTF_8));
    }

    // 将一些符号去掉
    private static String trim(String str) {

        // 如果为 null 就直接返回
        if (str == null) {
            return str;
        }
        return str.replaceAll("\r", "").replaceAll("\n", "");
    }

    // 获取存活时间
    private static String expiredTime(FullHttpRequest request) {
        return request.headers().get(LighterArgument.EXPIRE_TIME_ARGUMENT);
    }

    // 添加参数
    private static Task filledWithParameter(FullHttpRequest request, Task task) {
        // 设置存活时间
        if (GracefulHelper.isNotNull(expiredTime(request))) {
            task.addArgument(
                    LighterArgument.EXPIRE_TIME_ARGUMENT,
                    expiredTime(request)
            );
        }

        // 返回任务
        return task;
    }
}
