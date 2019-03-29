package cn.com.fishin.lighter.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.com.fishin.lighter.event.NodeDataEvent;
import cn.com.fishin.lighter.protocol.Command;
import cn.com.fishin.lighter.core.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * NodeDataEvent 事件的帮助工具类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 19:40:01
 */
public class NodeDataHelper {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(NodeDataHelper.class);

    /**
     * 调用一个节点相应的方法
     *
     * @param node    节点
     * @param method  相应的方法
     * @param command 指令
     */
    public static Object invoke(Node node, Method method, Command command) throws InvocationTargetException, IllegalAccessException {
        // 调用方法
        return method.invoke(node, (Object[]) command.getAllArgs());
    }

    /**
     * 从指令对象中获取节点数据事件
     *
     * @param command 指令对象
     * @return 返回节点数据事件
     */
    public static NodeDataEvent getEventFromCommand(Command command) {
        return getEventFromCommand(command, null);
    }

    /**
     * 从指令对象中获取节点数据事件
     *
     * @param command 指令对象
     * @param args 额外的事件参数
     * @return 返回节点数据事件
     */
    public static NodeDataEvent getEventFromCommand(Command command, Object[] args) {
        NodeDataEvent event = new NodeDataEvent("NodeDataEvent");
        event.setCommand(command);
        event.setArgs(args);

        return event;
    }
}
