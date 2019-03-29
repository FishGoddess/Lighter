package cn.com.fishin.lighter.handler;

import cn.com.fishin.lighter.core.NodeManageable;

import java.lang.reflect.Method;

/**
 * 协议指令和方法执行的映射处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 14:59:37
 */
public interface MappingHandler {

    /**
     * 初始化
     */
    void init();

    /**
     * 注入节点管理器
     *
     * @param nodeManager 节点管理器
     */
    void setNodeManager(NodeManageable nodeManager);

    /**
     * 根据指令返回映射的方法
     *
     * @param instruction 指令
     * @return 返回映射的方法
     */
    Method mappingTo(String instruction);
}
