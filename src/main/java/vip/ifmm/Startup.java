package vip.ifmm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import vip.ifmm.core.App;
import vip.ifmm.helper.ArgsHelper;

import java.net.Socket;

/**
 * 程序运行的主类
 * 个人认为这个类和 vip.ifmm.core.App 类写的不好。。。
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 21:57:09
 */
public class Startup {

    // Spring 框架上下文
    private static ApplicationContext context = null;

    // 应用程序
    public static App application = null;

    public static void main(String[] args) throws Exception {

        // 初始化参数帮助类
        ArgsHelper.init(args);

        // 获取配置文件位置
        String xmlLocation = ArgsHelper.getXmlLocation();

        // 默认从类路径加载 Spring 配置文件
        initSpring(xmlLocation, xmlLocation.startsWith("classpath:"));

        // 启动应用程序
        application = context.getBean(App.class);
        application.startApplication(context, args);
    }

    /**
     * 初始化 Spring 容器
     *
     * @param xmlLocation   Spring 的 XML 配置文件位置
     * @param fromClassPath 是否从类路径加载配置文件
     */
    private static void initSpring(String xmlLocation, boolean fromClassPath) {
        if (fromClassPath) {
            // 从类路径加载配置文件
            context = new ClassPathXmlApplicationContext(xmlLocation);
        } else {
            context = new FileSystemXmlApplicationContext(xmlLocation);
        }
    }
}
