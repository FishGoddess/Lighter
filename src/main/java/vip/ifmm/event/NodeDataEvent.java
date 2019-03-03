package vip.ifmm.event;

import org.springframework.context.ApplicationEvent;
import vip.ifmm.core.support.Node;
import vip.ifmm.protocol.Commad;

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
    private Commad commad = null;

    public NodeDataEvent(Object source) {
        super(source);
    }

    public List<Node<String, String>> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    public Commad getCommad() {
        return commad;
    }

    public void setCommad(Commad commad) {
        this.commad = commad;
    }

    @Override
    public String toString() {
        return "NodeDataEvent{" +
                "nodes=" + nodes +
                '}';
    }
}
