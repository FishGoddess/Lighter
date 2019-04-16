package cn.com.fishin.lighter.protocol.json;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.enums.TaskAction;
import cn.com.fishin.lighter.common.helper.GracefulHelper;
import cn.com.fishin.lighter.common.helper.HttpRequestHelper;
import cn.com.fishin.lighter.core.LighterArgument;
import cn.com.fishin.lighter.protocol.RequestHandler;
import cn.com.fishin.lighter.protocol.RequestParser;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

/**
 * HTTP 中使用的 JSON 协议解析器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 22:27:04</p>
 */
public class JsonHttpRequestParser implements RequestParser<FullHttpRequest> {

    // 服务器存活周期时间，从请求头中获取时用到的名字
    private static final String EXPIRE_TIME = "Lighter-Expire-Time";

    @Override
    public Task parse(FullHttpRequest request) {
        // 根据请求方法来匹配不同的请求处理器
        // 将请求转换成大写来进行匹配
        return HttpMethodMapping.valueOf(HttpRequestHelper.method(request))
                .handler.handle(request);
    }

    // 任务动作枚举类
    // 这里使用的是 HTTP 的 Restful 风格指令动作
    private enum HttpMethodMapping {

        // GET 请求
        // 主要用来获取数据
        GET(request -> Task.make(TaskAction.FETCH, request.uri())),

        // POST 请求
        // 主要用来新增数据
        POST(request -> Task.make(
                TaskAction.SAVE,
                request.uri(),
                request.content().toString(CharsetUtil.UTF_8))
                .addArgument(
                        LighterArgument.EXPIRE_TIME_ARGUMENT,
                        GracefulHelper.ifNull(request.headers().get(EXPIRE_TIME), "0")
                )
        ),

        // DELETE 请求
        // 主要用来删除数据
        DELETE(request -> Task.make(
                TaskAction.REMOVE,
                request.uri())
        ),

        // PUT 请求
        // 主要用来修改数据
        PUT(request -> Task.make(
                TaskAction.UPDATE,
                request.uri(),
                request.content().toString(CharsetUtil.UTF_8))
                .addArgument(
                        LighterArgument.EXPIRE_TIME_ARGUMENT,
                        GracefulHelper.ifNull(request.headers().get(EXPIRE_TIME), "0")
                )
        );

        // 请求处理器
        private RequestHandler<FullHttpRequest> handler = null;

        HttpMethodMapping(RequestHandler<FullHttpRequest> handler) {
            this.handler = handler;
        }
    }
}