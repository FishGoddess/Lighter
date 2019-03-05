package vip.ifmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import vip.ifmm.core.App;
import vip.ifmm.exception.CloseServerException;
import vip.ifmm.helper.ArgsHelper;
import vip.ifmm.helper.SpringInitHelper;

import java.io.IOException;
import java.net.Socket;

/**
 * 关闭服务器的脚本
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 01:18:02
 */
public class Shutdown {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(Shutdown.class);

    public static void main(String[] args) {

        // 由于启动两个 main 方法属于两个 JVM 进程
        // 所以没有办法直接共享数据，这里加载配置文件获取

        // 初始化参数帮助类
        ArgsHelper.init(args);

        // 获取配置文件位置
        String xmlLocation = ArgsHelper.getXmlLocation();

        // 加载 Spring 配置文件
        ApplicationContext context = SpringInitHelper.initSpring(xmlLocation);

        // 关闭服务器，连接到关闭服务器的端口即可
        try {
            // 启动应用程序
            App application = context.getBean(App.class);
            application.setContext(context);

            new Socket("127.0.0.1", application.getCloseNioServerPort());
            log.info("Lighter 服务器已经关闭！");
        } catch (Exception e) {
            // 当没有启动服务的时候就会发生这个异常
            e = new CloseServerException("服务未启动！", e);
            log.error("服务器关闭出错！原因可能是：{}", e.getMessage(), e);
        }
    }
}
