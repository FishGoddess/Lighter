package cn.com.fishin.lighter.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import cn.com.fishin.lighter.core.App;
import cn.com.fishin.lighter.helper.ProtocolHelper;
import cn.com.fishin.lighter.helper.NodeDataHelper;
import cn.com.fishin.lighter.protocol.Command;
import cn.com.fishin.lighter.protocol.ProtocolParser;
import cn.com.fishin.lighter.protocol.ProtocolParserKeeper;

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

        // 解析协议内容，得到对应的指令对象
        try {
            Command command = protocolParser.parse(msg.text());

            // 检查请求是否合法
            if (!ProtocolHelper.checkContent(command)) {
                ctx.writeAndFlush(new TextWebSocketFrame(ProtocolHelper.PROTOCOL_PARSE_ERROR));
                return;
            }

            // 发布收到新指令事件
            app.publishEvent(NodeDataHelper.getEventFromCommand(command, new Object[]{
                    ctx.channel(), // 将这个通道传入事件内部当作额外参数，以方便在结果处理器中返回数据
            }));
        } catch (Exception e) {
            ctx.writeAndFlush(new TextWebSocketFrame(ProtocolHelper.INVOKE_ERROR));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(cause);
    }
}
