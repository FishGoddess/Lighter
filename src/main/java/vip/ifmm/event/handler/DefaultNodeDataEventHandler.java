package vip.ifmm.event.handler;

import vip.ifmm.core.support.Node;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.core.net.protocol.Command;

import java.util.List;

/**
 * 节点数据事件处理器
 * 这个处理器将用来处理节点收到数据处理的事件
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 15:29:57
 */
public class DefaultNodeDataEventHandler implements NodeDataEventHandler {

    /**
     * 处理节点数据事件
     *
     * @param event 发生的要被处理的事件
     * @return true 处理成功，false 处理失败
     */
    @Override
    public boolean handle(NodeDataEvent event) {
        return handlerInternal(event);
    }

    // 内部真正处理事件的方法
    private boolean handlerInternal(NodeDataEvent event) {
        Command command = event.getCommand();
        List<Node<String, String>> nodes = event.getNodes();

        if ("save".equals(command.getInstruction())) {
            nodes.get(0).save(command.getKey(), command.getValue());
        } else {
            System.out.println(nodes.get(0).fetch(command.getKey()));
        }

        return true;
    }
}
