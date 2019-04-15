package cn.com.fishin.lighter.net.http;

import cn.com.fishin.lighter.common.enums.state.NetServerState;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.common.helper.ResponseHelper;
import cn.com.fishin.lighter.protocol.RequestParser;
import cn.com.fishin.tuz.plugin.DiPlugin;
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
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @SuppressWarnings("unchecked")
    private static final RequestParser<HttpRequest> requestParser = DiPlugin.useInstance(RequestParser.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {

        // 调试记录
        LogHelper.debug("One client connected! ===> " + request.uri());

        ctx.writeAndFlush(ResponseHelper.wrap(
                NetServerState.SUCCESS,
                requestParser.parse(request)
        )).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // 错误记录
        LogHelper.error(cause.getMessage(), cause);

        // 返回错误信息
        ctx.writeAndFlush(ResponseHelper.wrap(NetServerState.NET_ERROR));
        ctx.close();
    }
}
