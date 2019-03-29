package cn.com.fishin.net.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import cn.com.fishin.net.NioServerInitializer;

/**
 * WebSocket 服务器初始化器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:30:27
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel>
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
                .addLast(new WebSocketServerProtocolHandler("/lighter"))
                .addLast(channelHandler);
    }
}
