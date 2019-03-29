package cn.com.fishin.lighter.event;

import org.springframework.context.ApplicationEvent;
import cn.com.fishin.lighter.protocol.Command;
import cn.com.fishin.lighter.core.Node;

import java.util.Arrays;
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

    // 额外参数
    private Object[] args = null;

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

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "NodeDataEvent{" +
                "nodes=" + nodes +
                ", command=" + command +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
