package vip.ifmm.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ifmm.core.net.protocol.Command;
import vip.ifmm.core.support.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
     * @param node 节点
     * @param method 相应的方法
     * @param command 指令
     */
    public static Object invoke(Node node, Method method, Command command) {
        try {
            // 调用方法
            return method.invoke(node, (Object[]) command.getAllArgs());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("{} 方法调用失败！参数是 {}", method, Arrays.toString(command.getAllArgs()), e);
        }

        return null;
    }
}
