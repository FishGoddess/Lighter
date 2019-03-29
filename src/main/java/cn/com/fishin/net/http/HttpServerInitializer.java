package cn.com.fishin.net.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import cn.com.fishin.net.NioServerInitializer;

/**
 * HTTP 服务器初始化器
 * 这个实现的不好，由于使用了观察者模式，所以没办法在发布事件时立刻返回数据
 * 而 HTTP 不能由服务器发送数据，所以这里遇到了一个问题
 *
 * TODO 等找到解决办法再完成这个处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 21:28:30
 */
@Deprecated
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
        implements NioServerInitializer {

    // 通道处理器
    private ChannelHandler channelHandler = null;

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new HttpServerCodec()) // http 请求和响应，必须放在第一个
                .addLast(new ChunkedWriteHandler()) // 解决大码流的问题
                .addLast(new HttpObjectAggregator(16 * 1024 * 1024)) // 聚合 http 请求
                .addLast(new HttpContentCompressor()) // 启用 http 压缩
                .addLast(channelHandler);
    }
}
