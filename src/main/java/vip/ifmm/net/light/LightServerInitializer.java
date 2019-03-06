package vip.ifmm.net.light;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import vip.ifmm.handler.WebSocketServerHandler;
import vip.ifmm.net.NioServerInitializer;

/**
 * Light 服务器初始化器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:30:27
 */
public class LightServerInitializer extends ChannelInitializer<SocketChannel>
        implements NioServerInitializer {

    private ChannelHandler channelHandler = null;

    @Override
    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler()) // 解决大码流的问题
                .addLast(new HttpObjectAggregator(16 * 1024 * 1024)) // 聚合 http 请求
                .addLast(new WebSocketServerProtocolHandler("/EAD_ws"))
                .addLast(channelHandler);
    }
}
