package vip.ifmm.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * NIO 服务器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:28:27
 */
public class NioServer {

    // 记录日志
    private static final Log log = LogFactory.getLog(NioServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 打开服务器，占用端口
     *
     * @param port 占用的端口
     * @param childHandler 具体实现服务器的初始化处理器
     * @throws InterruptedException 中断异常
     */
    public void open(int port, ChannelHandler childHandler) throws InterruptedException {

        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(childHandler);

        ChannelFuture f = server.bind(port).sync();

        // 记录日志
        log.info("NioServer run on port: " + port);

        f.channel().closeFuture().sync();
    }

    // 关闭服务器释放资源
    public void closeGracefully() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
