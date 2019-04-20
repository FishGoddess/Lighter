package cn.com.fishin.lighter.net.http;

import cn.com.fishin.lighter.common.enums.ServerState;
import cn.com.fishin.lighter.common.enums.state.NetServerState;
import cn.com.fishin.lighter.common.exception.RequestException;
import cn.com.fishin.lighter.common.helper.HttpRequestHelper;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.common.helper.ResponseHelper;
import cn.com.fishin.lighter.core.LighterExecutor;
import cn.com.fishin.lighter.core.LighterParser;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * 服务器处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/4/15 21:35:30
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        // 调试记录
        LogHelper.debug("One client connected! ===> " + request.uri());

        // 如果是 OPTIONS 请求，说明是跨域访问请求
        if (HttpRequestHelper.isOptions(request)) {
            ctx.writeAndFlush(ResponseHelper.wrap(
                    NetServerState.SUCCESS,
                    null
            )).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // 检查请求是否合法
        checkRequest(request);

        // 提交任务执行，并返回数据
        ctx.writeAndFlush(ResponseHelper.wrap(
                NetServerState.SUCCESS,
                LighterExecutor.submit(request)
        )).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // 错误记录
        LogHelper.error(cause.getMessage(), cause);

        // 返回错误信息
        ctx.writeAndFlush(ResponseHelper.wrap(new ServerState() {
            @Override
            public int getCode() {
                return -1;
            }

            @Override
            public String getMsg() {
                return cause.getMessage();
            }
        }));

        ctx.close();
    }

    // 检查请求是否合法
    private static void checkRequest(FullHttpRequest request) {
        checkRequestUri(request);
        checkRequestBody(request);
    }

    // 检查 uri 是否为空
    private static void checkRequestUri(FullHttpRequest request) {
        if ("".equals(request.uri()) || "/".equals(request.uri())) {
            throw new RequestException("The uri of request must be not empty!");
        }
    }

    // 检查请求体是否为空
    private static void checkRequestBody(FullHttpRequest request) {
        boolean needChecking = HttpRequestHelper.isPost(request) || HttpRequestHelper.isPut(request);
        if (needChecking && request.content().readableBytes() <= 0) {
            throw new RequestException("When the method is 'POST' or 'PUT', the request body require not empty!");
        }
    }
}
