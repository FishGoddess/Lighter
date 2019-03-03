package vip.ifmm.event.handler;

import vip.ifmm.event.NodeDataEvent;

/**
 * 节点数据事件处理器
 * 这个处理器将用来处理节点收到数据处理的事件
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 15:29:57
 */
public interface NodeDataEventHandler {

    /**
     * 处理节点数据事件
     *
     * @param event 发生的要被处理的事件
     * @return true 处理成功，false 处理失败
     */
    boolean handle(NodeDataEvent event);
}
