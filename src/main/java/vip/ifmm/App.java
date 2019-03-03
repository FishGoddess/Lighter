package vip.ifmm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.protocol.Commad;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");

        Commad commad = new Commad();
        commad.setInstruction("save");
        commad.setKey("testKey");
        commad.setValue("testValue");
        NodeDataEvent event = new NodeDataEvent("NodeDataEvent");
        event.setCommad(commad);
        context.publishEvent(event);

        commad.setInstruction("fetch");
        context.publishEvent(event);
    }
}
