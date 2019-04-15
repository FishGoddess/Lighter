package cn.com.fishin.lighter.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * NIO 服务器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/1/8 13:28:27
 */
public class NioServer {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(NioServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 打开服务器，占用端口
     *
     * @param port         占用的端口
     * @param initializer 具体实现服务器的初始化处理器
     * @throws InterruptedException 中断异常
     */
    public void open(int port, int closeListenPort, ChannelInitializer initializer, ReadyHook hook) throws Exception {

        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(initializer);
        ChannelFuture f = server.bind(port).sync();

        prepareCloseListener(closeListenPort);

        // 记录日志
        log.info("NioServer run on port: " + port);

        hook.hook();
        f.channel().closeFuture().sync();
    }

    // 监听关闭端口
    private void prepareCloseListener(int port) {

        // 添加关闭服务器的钩子函数
        //Runtime.getRuntime().addShutdownHook(new Thread(this::closeGracefully));

        // 添加关闭服务器的监听器
        new Thread(() -> {

            // 监听这个端口，如果有人连接上来，就关闭服务器
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
                server.accept();

                // 关闭服务器
                closeGracefully();
            } catch (IOException e) {
                // 关闭端口监听失败
                log.error("Server listen close port failed! port: " + port, e);
            } finally {
                try {
                    if (server != null) {
                        server.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }).start();
    }

    // 关闭服务器释放资源
    public void closeGracefully() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        // 信息记录
        log.info("Server closed! Have a good day :)");
    }

    // 准备好之后会被调用的钩子函数
    public interface ReadyHook {
        void hook();
    }
}
