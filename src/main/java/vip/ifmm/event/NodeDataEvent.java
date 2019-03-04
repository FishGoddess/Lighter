package vip.ifmm.event;

import org.springframework.context.ApplicationEvent;
import vip.ifmm.protocol.Command;
import vip.ifmm.core.Node;

import java.util.List;

/**
 * 节点数据事件
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 15:31:39
 */
public class NodeDataEvent extends ApplicationEvent {

    // 节点数据库
    private List<Node<String, String>> nodes = null;

    // 当前事件要执行的指令
    private Command command = null;

    public NodeDataEvent(Object source) {
        super(source);
    }

    public List<Node<String, String>> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "NodeDataEvent{" +
                "nodes=" + nodes +
                '}';
    }
}
