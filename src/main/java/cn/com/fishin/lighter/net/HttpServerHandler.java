package cn.com.fishin.lighter.net;

import cn.com.fishin.lighter.common.enums.state.NetServerState;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.common.helper.ResponseHelper;
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
        LogHelper.debug("One client connected! ===> " + request);

        ctx.writeAndFlush(ResponseHelper.wrap(NetServerState.SUCCESS)).addListener(ChannelFutureListener.CLOSE);
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
