package cn.com.fishin.lighter.handler;

import cn.com.fishin.lighter.core.Node;
import cn.com.fishin.lighter.core.Result;
import cn.com.fishin.lighter.event.NodeDataEvent;
import cn.com.fishin.lighter.exception.ArgumentException;
import cn.com.fishin.lighter.protocol.Command;
import cn.com.fishin.lighter.helper.NodeDataHelper;
import cn.com.fishin.lighter.selector.NodeSelector;

import java.lang.reflect.Method;
import java.util.Arrays;
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
    private NodeSelector<String, String> nodeSelector = null;

    // 结果处理器
    private ResultHandler<Result<String>> resultHandler = null;

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
        invokeCommand(event.getCommand(), event.getArgs());
        return true;
    }

    // 内部会获取所有需要执行指令的节点，然后执行
    private void invokeCommand(Command command, Object[] args) {

        // 选择出一些节点
        List<Node<String, String>> selectedNodes = getSelectedNodes(command);

        Method method = null;
        String result = null;
        for (Node<String, String> node : selectedNodes) {

            // 找到这个指令映射的方法
            method = mappingHandler.mappingTo(command.getInstruction());

            // 调用方法
            try {
                Object resultObj = NodeDataHelper.invoke(node, method, command);

                // 数组的话输出方式不一样
                if (resultObj.getClass().isArray()) {
                    result = Arrays.toString((Object[]) resultObj);
                } else {
                    result = String.valueOf(resultObj);
                }
                resultHandler.handle(new Result<>(result, args));
            }catch (NullPointerException e) {

                // 当获取的结果为 null 时，会爆出这个异常
                resultHandler.handle(new Result<>(result, args));
            } catch (Exception e) {
                e = new ArgumentException(command.getInstruction() +
                        "指令参数不合法！具体信息：" + e.getMessage(), e);
                resultHandler.handle(new Result<>(e.getMessage(), args));
            }
        }
    }

    // 由节点选择器来选择出一些节点
    private List<Node<String, String>> getSelectedNodes(Command command) {
        return nodeSelector.getSelectedNodes(command.getInstruction(), command.getAllArgs());
    }
}
