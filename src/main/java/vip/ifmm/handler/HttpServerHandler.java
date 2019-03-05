package vip.ifmm.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import vip.ifmm.core.App;
import vip.ifmm.helper.HttpProtocolHelper;
import vip.ifmm.helper.NodeDataHelper;
import vip.ifmm.protocol.Command;
import vip.ifmm.protocol.ProtocolParser;

import javax.annotation.Resource;


/**
 * HTTP 服务器处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 21:35:30
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 协议解析器
    private ProtocolParser protocolParser = null;

    // 应用程序
    private App app = null;

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
        if (method == null || !method.name().toUpperCase().equals(HttpProtocolHelper.SERVER_SUPPORT_METHOD)) {
            ctx.writeAndFlush(HttpProtocolHelper.responseHTML(HttpProtocolHelper.NOT_SUPPORT_METHOD))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // 支持的请求方法
        // 检查请求是否合法
        if (!HttpProtocolHelper.checkContent(msg.content())) {
            ctx.writeAndFlush(HttpProtocolHelper.responseHTML(HttpProtocolHelper.CONTENT_IS_EMPTY))
                    .addListener(ChannelFutureListener.CLOSE);
        }

        // 解析请求体，得到对应的指令对象
        try {
            Command command = protocolParser.parse(msg.content().toString());
            app.publishEvent(NodeDataHelper.getEventFromCommand(command));
        } catch (Exception e) {
            ctx.writeAndFlush(HttpProtocolHelper.responseHTML(HttpProtocolHelper.CONTENT_IS_EMPTY))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(HttpProtocolHelper.PROTOCOL_PARSE_ERROR)
                .addListener(ChannelFutureListener.CLOSE);
        //ctx.close();
    }
}
