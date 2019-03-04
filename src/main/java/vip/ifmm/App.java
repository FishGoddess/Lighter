package vip.ifmm;

import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.net.NioServer;
import vip.ifmm.protocol.Command;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 应用程序入口
 */
public class App {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(App.class);

    // 通道处理器，具体服务器实现
    private ChannelHandler channelHandler = null;

    @Value("${nioServerPort}")
    private int nioServerPort = 6666;

    @Value("${closeNioServerPort}")
    private int closeNioServerPort = 9999;

    // Spring 框架上下文
    private ApplicationContext context = null;

    // 服务器
    private NioServer server = null;

    // 关闭软件的参数指令
    private static final String CLOSE_APPLICATION = "-stop";

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    /**
     * 启动应用的入口方法
     *
     * @param args 应用程序启动时传过来的参数
     */
    public void startApplication(ApplicationContext context, String[] args) {

        // 初始化 Spring 容器
        this.context = context;

        log.info("Spring 容器初始化完成！");

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

        // TODO 暂时是测试代码
        Command command = new Command();
        command.setInstruction("save");
        command.setKey("testKey");
        command.setValue("testValue");
        command.setAllArgs(new String[]{
                command.getKey(),
                command.getValue()
        });

        NodeDataEvent event = new NodeDataEvent("NodeDataEvent");
        event.setCommand(command);
        context.publishEvent(event);

        command.setInstruction("fetch");
        command.setAllArgs(new String[]{
                command.getKey()
        });
        context.publishEvent(event);
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

                log.info("关闭服务器监听器，监听端口 {} 已经开启！", closeNioServerPort);

                // 这个方法会被阻塞，当有客户端连接到这个端口，就会放行，然后执行关闭服务器操作
                closeServer.accept();
                server.closeGracefully();
            } catch (IOException e) {
                log.error("关闭服务器监听端口 {} 已被占用！", closeNioServerPort, e);
            }
        }).start();
    }
}
