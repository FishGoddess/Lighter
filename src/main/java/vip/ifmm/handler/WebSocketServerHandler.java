package vip.ifmm.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import vip.ifmm.protocol.ProtocolParser;
import vip.ifmm.protocol.ProtocolParserKeeper;


/**
 * WebSocket 服务器处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:29:27
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
        implements ProtocolParserKeeper {

    // 协议解析器
    private ProtocolParser protocolParser = null;

    @Override
    public void setProtocolParser(ProtocolParser protocolParser) {
        this.protocolParser = protocolParser;
    }

    // 将所有已连接上来的通道都保存起来
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.writeAndFlush(cause);
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        System.out.println("added ==> " + channels.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel()); // netty 会自动处理离开的通道，所以这里其实是可以省略的
        System.out.println("removed ==> " + channels.size());
    }

    // 向所有连接上来的用户发送消息
    private static void sendToAll(String line) {
        channels.writeAndFlush(new TextWebSocketFrame(line));
    }
}
