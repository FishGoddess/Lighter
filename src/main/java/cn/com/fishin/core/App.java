package cn.com.fishin.core;

import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import cn.com.fishin.net.NioServer;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 应用程序
 * 个人认为这个类和 cn.com.fishin.Startup 类写的不好。。。
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 21:57:09
 */
public class App {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(App.class);

    // 通道处理器，具体服务器实现
    private ChannelHandler channelHandler = null;

    // Nio 服务器端口
    private int nioServerPort = 6666;

    // 关闭服务器的端口
    private int closeNioServerPort = 9999;

    // Spring 框架上下文
    private ApplicationContext context = null;

    // 服务器
    private NioServer server = null;

    // 初始化 Spring 框架上下文
    public void setContext(ApplicationContext context) {
        this.context = context;
        log.info("Spring 容器初始化完成！");
    }

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public void setNioServerPort(int nioServerPort) {
        this.nioServerPort = nioServerPort;
    }

    public void setCloseNioServerPort(int closeNioServerPort) {
        this.closeNioServerPort = closeNioServerPort;
    }

    public int getNioServerPort() {
        return nioServerPort;
    }

    public int getCloseNioServerPort() {
        return closeNioServerPort;
    }

    /**
     * 启动应用的入口方法
     *
     * @param args 应用程序启动时传过来的参数
     */
    public void startApplication(String[] args) {

        // 在初始化服务器之前执行一些操作
        beforeInitServer();

        // 启动服务器
        try {
            log.info("Lighter 服务器现正在启动！服务端口：{}，服务类型：{}", nioServerPort, channelHandler);
            initServer();
        } catch (InterruptedException e) {
            log.error("Lighter 服务器现已启动失败！{}", e.getMessage());
        }
    }

    // 在初始化服务器之前执行
    private void beforeInitServer() {

        // 启动监听线程
        startCloseServerListener();
    }

    // 初始化服务器
    private void initServer() throws InterruptedException {
        server = new NioServer();
        server.open(nioServerPort, channelHandler);
    }

    // 启动关闭服务器监听线程
    private void startCloseServerListener() {
        new Thread(()->{
            try {
                ServerSocket closeServer = new ServerSocket(closeNioServerPort);

                log.info("服务器关闭监听器正在监听端口 {} ！", closeNioServerPort);

                // 这个方法会被阻塞，当有客户端连接到这个端口，就会放行，然后执行关闭服务器操作
                closeServer.accept();
                server.closeGracefully();

                log.info("Lighter 服务器已经关闭！");
            } catch (IOException e) {
                log.error("关闭服务器监听端口 {} 已被占用！", closeNioServerPort, e);
            }
        }).start();
    }

    /**
     * 发布事件
     *
     * @param event 要被发布的事件
     */
    public void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }
}
