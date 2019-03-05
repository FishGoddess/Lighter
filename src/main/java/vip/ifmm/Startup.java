package vip.ifmm;

import org.springframework.context.ApplicationContext;
import vip.ifmm.core.App;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.helper.ArgsHelper;
import vip.ifmm.helper.SpringInitHelper;
import vip.ifmm.protocol.Command;

/**
 * 程序运行的主类
 * 个人认为这个类和 vip.ifmm.core.App 类写的不好。。。
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 21:57:09
 */
public class Startup {

    public static void main(String[] args) throws Exception {

        // 初始化参数帮助类
        ArgsHelper.init(args);

        // 获取配置文件位置
        String xmlLocation = ArgsHelper.getXmlLocation();

        // 加载 Spring 配置文件
        ApplicationContext context = SpringInitHelper.initSpring(xmlLocation);

        // 启动应用程序
        App application = context.getBean(App.class);
        application.setContext(context);

        test(application); // TODO 暂时是测试代码
        application.startApplication(args);
    }

    // TODO 暂时是测试代码
    private static void test(App app) {

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
        app.publishEvent(event);

        command.setInstruction("fetch");
        command.setAllArgs(new String[]{
                command.getKey()
        });
        app.publishEvent(event);
    }
}
