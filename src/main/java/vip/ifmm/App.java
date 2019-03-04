package vip.ifmm;

import io.netty.channel.ChannelHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.protocol.Command;

/**
 * Hello world!
 */
public class App {

    // 通道处理器，具体服务器实现
    private ChannelHandler channelHandler = null;

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");

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
}
