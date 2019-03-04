package vip.ifmm.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;


/**
 * 服务器处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 21:35:30
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 类加载时就缓存 HTML 页面
    private static final String HTML_OK = "Hello World!";

    // 接收到任何数据都返回首页
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(HTML_OK, CharsetUtil.UTF_8)
        );

        // 设置响应头
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.writeAndFlush(cause);
        ctx.close();
    }
}
