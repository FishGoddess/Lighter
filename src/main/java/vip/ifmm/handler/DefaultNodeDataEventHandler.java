package vip.ifmm.handler;

import vip.ifmm.core.Node;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.exception.ArgumentException;
import vip.ifmm.protocol.Command;
import vip.ifmm.helper.NodeDataHelper;
import vip.ifmm.selector.NodeSelector;

import java.lang.reflect.InvocationTargetException;
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
public class DefaultNodeDataEventHandler implements EventHandler<NodeDataEvent> {

    // 映射器
    private MappingHandler mappingHandler = null;

    // 节点选择器
    private NodeSelector<String, String, String> nodeSelector = null;

    // 结果处理器
    private ResultHandler<String> resultHandler = null;

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }

    public NodeSelector getNodeSelector() {
        return nodeSelector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setNodeSelector(NodeSelector nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    @SuppressWarnings("unchecked")
    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
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

        // 执行这个指令
        invokeCommand(event.getCommand());
        return true;
    }

    // 内部会获取所有需要执行指令的节点，然后执行
    private void invokeCommand(Command command) {

        // 选择出一些节点
        List<Node<String, String>> selectedNodes = getSelectedNodes(command);

        Method method = null;
        String result = null;
        for (Node<String, String> node : selectedNodes) {

            // 找到这个指令映射的方法
            method = mappingHandler.mappingTo(command.getInstruction());

            // 调用方法
            try {
                result = (String) NodeDataHelper.invoke(node, method, command);
                resultHandler.handle(result);
            } catch (Exception e) {
                e = new ArgumentException(command.getInstruction() +
                        "指令参数不合法！具体信息：" + e.getMessage(), e);
                resultHandler.handle(e.getMessage());
            }
        }
    }

    // 由节点选择器来选择出一些节点
    private List<Node<String, String>> getSelectedNodes(Command command) {
        return nodeSelector.getSelectedNodes(command.getInstruction(), command.getKey(), command.getValue());
    }
}
