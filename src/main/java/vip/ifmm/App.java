package vip.ifmm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.core.net.protocol.Command;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");

        Command command = new Command();
        command.setInstruction("save");
        command.setKey("testKey");
        command.setValue("testValue");
        NodeDataEvent event = new NodeDataEvent("NodeDataEvent");
        event.setCommand(command);
        context.publishEvent(event);

        command.setInstruction("fetch");
        context.publishEvent(event);
    }
}
