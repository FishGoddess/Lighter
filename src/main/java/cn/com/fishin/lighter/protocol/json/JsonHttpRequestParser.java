package cn.com.fishin.lighter.protocol.json;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.enums.TaskAction;
import cn.com.fishin.lighter.core.LighterArgument;
import cn.com.fishin.lighter.protocol.RequestHandler;
import cn.com.fishin.lighter.protocol.RequestParser;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

/**
 * HTTP 中使用的 JSON 协议解析器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 22:27:04</p>
 */
public class JsonHttpRequestParser implements RequestParser<HttpRequest> {

    //
    private static final String EXPIRE_TIME = "Lighter-expire-time";

    @Override
    public Task parse(HttpRequest request) {
        // 根据请求方法来匹配不同的请求处理器
        // 将请求转换成大写来进行匹配
        return HttpMethodMapping.valueOf(request.method().name().toUpperCase())
                .handler.handle(request);
    }

    // 从请求中获取任务参数
    private static Task filledArgsWith(HttpRequest request, Task task) {

        // 从请求头中获取参数
        return task.addArgument(
                LighterArgument.EXPIRE_TIME_ARGUMENT,
                request.headers().get(EXPIRE_TIME)
        );
    }

    // 任务动作枚举类
    // 这里使用的是 HTTP 的 Restful 风格指令动作
    private enum HttpMethodMapping {

        // GET 请求
        // 主要用来获取数据
        GET(request -> {
            return filledArgsWith(request,
                    Task.make(
                            TaskAction.FETCH,
                            request.uri()
                    )
            );
        }),

        // POST 请求
        // 主要用来新增数据
        POST(request -> {
            return filledArgsWith(request,
                    Task.make(
                            TaskAction.SAVE,
                            request.uri()
                    )
            );
        }),

        // DELETE 请求
        // 主要用来删除数据
        DELETE(request -> {
            return filledArgsWith(request,
                    Task.make(
                            TaskAction.REMOVE,
                            request.uri()
                    )
            );
        }),

        // PUT 请求
        // 主要用来修改数据
        PUT(request -> {
            return filledArgsWith(request,
                    Task.make(
                            TaskAction.UPDATE,
                            request.uri()
                    )
            );
        });

        // 请求处理器
        private RequestHandler<HttpRequest> handler = null;

        HttpMethodMapping(RequestHandler<HttpRequest> handler) {
            this.handler = handler;
        }
    }
}
