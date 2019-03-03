package vip.ifmm.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.event.handler.NodeDataEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点管理器
 * 所有节点都将注册在这个节点管理器上
 * 数据也将先经过这个管理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 15:21:15
 */
public class DefaultNodeManager implements NodeManageable {

    // 记录日志
    private static final Logger log = LoggerFactory.getLogger(DefaultNodeManager.class);

    // 节点个数
    private int numberOfNodes = 16;

    // 所有的节点，这里多占用两个空间，防止 ArrayList 进行扩容导致空间浪费
    private List<Node<String, String>> nodes = new ArrayList<>(numberOfNodes + 2);

    // 节点数据事件处理器
    private NodeDataEventHandler handler = null;

    // 初始化节点的实现类
    private String nodeClassName = "vip.ifmm.core.support.DefaultMapNode";
    private Class nodeClass = null;

    public DefaultNodeManager() {}

    // 初始化数据
    @SuppressWarnings("unchecked")
    public void init() {
        try {
            // 加载节点实现类
            nodeClass = Class.forName(nodeClassName);
            log.info("节点实现类 {} 加载成功！", nodeClassName);
        } catch (ClassNotFoundException e) {
            log.error("节点实现类 {} 不存在！{}", nodeClassName, e.getMessage());
            throw new RuntimeException("节点实现类" + nodeClassName + "不存在！", e);
        }

        // 初始化节点数据库
        try {
            for (int i = 0; i < numberOfNodes; i++) {
                nodes.add((Node<String, String>) nodeClass.newInstance());
            }
            log.info("节点数据库初始化成功！");
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("节点数据库初始化失败！{}", e.getMessage());
            throw new RuntimeException("节点数据库初始化失败！", e);
        }
    }

    public String getNodeClassName() {
        return nodeClassName;
    }

    public void setNodeClassName(String nodeClassName) {
        this.nodeClassName = nodeClassName;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public NodeDataEventHandler getHandler() {
        return handler;
    }

    public void setHandler(NodeDataEventHandler handler) {
        this.handler = handler;
    }

    public Class getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class nodeClass) {
        this.nodeClass = nodeClass;
    }

    @Override
    public void onApplicationEvent(NodeDataEvent event) {
        completeEvent(event);

        // 交给处理器处理
        if (!handler.handle(event)) {
            log.error("事件 {} 处理失败！", event);
        }
    }

    // 将事件的属性注入的更完善
    private void completeEvent(NodeDataEvent event) {
        event.setNodes(nodes); // 将节点数据库引用传过去
    }
}
