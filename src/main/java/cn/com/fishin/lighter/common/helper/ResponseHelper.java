package cn.com.fishin.lighter.common.helper;

import cn.com.fishin.lighter.common.entity.ServerResponse;
import cn.com.fishin.lighter.common.enums.ServerState;
import cn.com.fishin.tuz.core.Tuz;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 服务器响应帮助类
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 20:27:16</p>
 */
public class ResponseHelper {

    // 返回响应数据
    public static FullHttpResponse wrap(ServerState state, Object result) {
        return wrap(ServerResponse.response(state, result));
    }

    // 返回响应数据
    public static FullHttpResponse wrap(ServerState state) {
        return wrap(state, null);
    }

    // 返回响应数据
    private static FullHttpResponse wrap(ServerResponse response) {
        return wrapHeaders(
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        status(response),
                        content(response)
                )
        );
    }

    // 设置响应头
    private static FullHttpResponse wrapHeaders(FullHttpResponse response) {
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        // 跨域需要访问这两个请求头
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, Tuz.useGracefully("AccessControlAllowOrigin", "*"));
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "*");
        return response;
    }

    // 获取响应内容
    private static ByteBuf content(ServerResponse response) {
        return Unpooled.copiedBuffer(JSON.toJSONString(response), CharsetUtil.UTF_8);
    }

    // 获取响应状态
    private static HttpResponseStatus status(ServerResponse response) {
        return HttpResponseStatus.OK;
        /*return response.isSuccess() ?
                HttpResponseStatus.OK :
                HttpResponseStatus.BAD_REQUEST;*/
    }
}
