package vip.ifmm.core.support;

import org.springframework.context.ApplicationListener;
import vip.ifmm.event.NodeDataEvent;
import vip.ifmm.event.NodeDataEventHandler;

/**
 * 可以管理节点的接口
 * 实现该接口即可拥有管理节点的能力
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 19:23:31
 */
public interface NodeManageable extends ApplicationListener<NodeDataEvent> {

    // 初始化数据
    void init();

    // 初始化节点的实现类
    String getNodeClassName();

    // 初始化节点的实现类
    void setNodeClassName(String nodeClassName);

    // 节点个数
    int getNumberOfNodes();

    // 节点个数
    void setNumberOfNodes(int numberOfNodes);

    // 节点数据事件处理器
    NodeDataEventHandler getHandler();

    // 节点数据事件处理器
    void setHandler(NodeDataEventHandler handler);

    // 初始化节点的实现类
    Class getNodeClass();

    // 初始化节点的实现类
    void setNodeClass(Class nodeClass);
}
