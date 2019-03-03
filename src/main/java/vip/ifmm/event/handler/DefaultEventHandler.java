package vip.ifmm.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import vip.ifmm.core.support.Node;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.core.net.protocol.Command;
import vip.ifmm.helper.MappingHelper;
import vip.ifmm.helper.NodeDataHelper;
import vip.ifmm.selector.NodeSelector;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 节点数据事件处理器
 * 这个处理器将用来处理节点收到数据处理的事件
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 15:29:57
 */
public class DefaultEventHandler implements EventHandler<NodeDataEvent> {

    // 映射器
    private MappingHelper mappingHelper = null;

    @Autowired
    public void setMappingHelper(MappingHelper mappingHelper) {
        this.mappingHelper = mappingHelper;
    }

    // 节点选择器
    private NodeSelector<String, String, String> nodeSelector = null;

    public NodeSelector getNodeSelector() {
        return nodeSelector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setNodeSelector(NodeSelector nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

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

        // 检查节点选择器是否为空
        if (nodeSelector == null) {
            return false;
        }

        // 设置选择器的节点数据库
        nodeSelector.setNodes(event.getNodes());

        Command command = event.getCommand();
        String instruction = command.getInstruction();
        String key = command.getKey();
        String value = command.getValue();

        // 由节点选择器来选择出一些节点
        List<Node<String, String>> selectedNodes = nodeSelector.getSelectedNodes(instruction, key, value);

        // 每个节点都执行这个指令
        Method method = null;
        String result = null;
        for (Node<String, String> node : selectedNodes) {

            // 找到这个指令映射的方法
            method = mappingHelper.mappingTo(instruction);

            // 调用方法
            result = (String) NodeDataHelper.invoke(node, method, command);
            System.out.println(result); // TODO 结果处理器
        }

        return true;
    }
}
