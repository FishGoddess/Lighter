package vip.ifmm.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import vip.ifmm.core.App;
import vip.ifmm.helper.NodeDataHelper;
import vip.ifmm.protocol.Command;
import vip.ifmm.protocol.ProtocolParser;
import vip.ifmm.protocol.ProtocolParserKeeper;

import javax.annotation.Resource;


/**
 * WebSocket 服务器处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:29:27
 */
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
        implements ProtocolParserKeeper {

    // 将所有已连接上来的通道都保存起来
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 协议解析器
    private ProtocolParser protocolParser = null;

    @Override
    public void setProtocolParser(ProtocolParser protocolParser) {
        this.protocolParser = protocolParser;
    }

    // 应用程序实例
    private App app = null;

    @Resource
    public void setApp(App app) {
        this.app = app;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        // 解析协议内容
        Command command = protocolParser.parse(msg.text());

        // 发布收到新指令事件
        app.publishEvent(NodeDataHelper.getEventFromCommand(command, new Object[] {
                ctx.channel() // 将这个通道传入事件内部当作额外参数，以方便在结果处理器中返回数据
        }));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(cause);
    }
}
