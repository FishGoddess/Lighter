package cn.com.fishin.lighter.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import cn.com.fishin.lighter.core.App;
import cn.com.fishin.lighter.helper.ProtocolHelper;
import cn.com.fishin.lighter.helper.NodeDataHelper;
import cn.com.fishin.lighter.protocol.Command;
import cn.com.fishin.lighter.protocol.ProtocolParser;
import cn.com.fishin.lighter.protocol.ProtocolParserKeeper;

import javax.annotation.Resource;


/**
 * HTTP 服务器处理器
 * 这个实现的不好，由于使用了观察者模式，所以没办法在发布事件时立刻返回数据
 * 而 HTTP 不能由服务器发送数据，所以这里遇到了一个问题
 *
 * TODO 等找到解决办法再完成这个处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 21:35:30
 */
@Deprecated
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
        implements ProtocolParserKeeper {

    // 协议解析器
    private ProtocolParser protocolParser = null;

    // 应用程序
    private App app = null;

    @Override
    public void setProtocolParser(ProtocolParser protocolParser) {
        this.protocolParser = protocolParser;
    }

    @Resource(name = "app")
    public void setApp(App app) {
        this.app = app;
    }

    // 接收到任何数据，并根据请求判断具体指令
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        HttpMethod method = msg.method();
        // 不支持的请求方法就直接返回
        if (method == null || !method.name().toUpperCase().equals(ProtocolHelper.SERVER_SUPPORT_METHOD)) {
            ctx.writeAndFlush(ProtocolHelper.responseHTML(ProtocolHelper.NOT_SUPPORT_METHOD))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // 支持的请求方法
        // 检查请求是否合法
        if (!ProtocolHelper.checkContent(msg.content())) {
            ctx.writeAndFlush(ProtocolHelper.responseHTML(ProtocolHelper.CONTENT_IS_EMPTY))
                    .addListener(ChannelFutureListener.CLOSE);
        }

        // 解析请求体，得到对应的指令对象
        try {
            Command command = protocolParser.parse(msg.content().toString());
            app.publishEvent(NodeDataHelper.getEventFromCommand(command));

            ctx.writeAndFlush(ProtocolHelper.responseHTML(ProtocolHelper.SERVER_SUPPORT_METHOD))
                    .addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            ctx.writeAndFlush(ProtocolHelper.responseHTML(ProtocolHelper.CONTENT_IS_EMPTY))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(ProtocolHelper.PROTOCOL_PARSE_ERROR)
                .addListener(ChannelFutureListener.CLOSE);
        //ctx.close();
    }
}
