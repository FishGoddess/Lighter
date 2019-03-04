package vip.ifmm.net.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import vip.ifmm.handler.HttpServerHandler;

/**
 * 服务器初始化器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 21:28:30
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new HttpServerCodec()) // http 请求和响应，必须放在第一个
                .addLast(new ChunkedWriteHandler()) // 解决大码流的问题
                .addLast(new HttpObjectAggregator(16 * 1024 * 1024)) // 聚合 http 请求
                .addLast(new HttpContentCompressor()) // 启用 http 压缩
                .addLast(new HttpServerHandler());
    }
}
