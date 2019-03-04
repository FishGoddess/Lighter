package vip.ifmm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 程序运行的主类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 21:57:09
 */
@Component
public class Main {

    // Spring 框架上下文
    private static ApplicationContext context = null;

    public static void main(String[] args) throws InterruptedException {
        String xmlLocation = "classpath:application-context.xml";

        // 默认从类路径加载 Spring 配置文件
        initSpring(xmlLocation, true);

        // 启动应用程序
        App application = context.getBean(App.class);
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
