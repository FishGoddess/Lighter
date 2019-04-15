package cn.com.fishin.lighter.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
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
    private static final Log log = LogFactory.getLog(NioServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    // 使用读写锁，因为读的次数比写的次数多得多，所以读的时候可以多个线程同时
    private ReadWriteLock readyReadWriteLock = new ReentrantReadWriteLock();
    private Lock readyReadLock = readyReadWriteLock.readLock();
    private Lock readyWriteLock = readyReadWriteLock.writeLock();

    // 是否准备好了
    private volatile boolean ready = false;

    /**
     * 服务器是否已经准备好了
     *
     * @return true 准备好了，false 没有准备好
     */
    public boolean isReady() {

        // 读操作上锁
        readyReadLock.lock();
        try {
            return ready;
        } finally {
            // 必须保证读锁一定被释放
            readyReadLock.unlock();
        }
    }

    /**
     * 打开服务器，占用端口
     *
     * @param port         占用的端口
     * @param initializer 具体实现服务器的初始化处理器
     * @throws InterruptedException 中断异常
     */
    public void open(int port, int closeListenPort, ChannelInitializer initializer) throws Exception {

        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(initializer);
        ChannelFuture f = server.bind(port).sync();

        // 准备阶段
        waitForReady(port);
        prepareCloseListener(closeListenPort);

        f.channel().closeFuture().sync();
    }

    // 监听关闭端口
    private void prepareCloseListener(int port) {
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

    // 服务器准备阶段
    private void waitForReady(int port) {

        // 服务器准备好了
        readyWriteLock.lock();
        try {
            ready = true;
        } finally {
            // 必须保证写锁一定被释放
            readyWriteLock.unlock();
        }

        // 记录日志
        log.info("NioServer run on port: " + port);
    }

    // 关闭服务器释放资源
    public void closeGracefully() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        // 信息记录
        log.info("Server closed! Have a good day :)");
    }
}
