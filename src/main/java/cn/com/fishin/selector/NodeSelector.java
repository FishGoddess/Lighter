package cn.com.fishin.selector;

import cn.com.fishin.core.Node;

import java.util.List;

/**
 * 节点选择器
 * 用于选择一个节点来存储
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:08:47
 */
public interface NodeSelector<K, V> {

    /**
     * 注入节点数据库
     * <b>实现类应该使用一个成员变量来保存</b>
     *
     * @param nodes 所有的节点
     */
    void setNodes(List<Node<K, V>> nodes);

    /**
     * 根据情况获取节点索引
     * 如果不止一个，就返回多个索引
     *
     * @param instruction 协议传过来的指令
     * @param args 额外的参数
     * @return 返回选择的节点索引数组
     */
    int[] getSelectedNodeIndexes(String instruction, Object[] args);

    /**
     * 根据情况获取节点
     * 如果不止一个，就返回多个索引
     *
     * @param instruction 协议传过来的指令
     * @param args        额外的参数
     * @return 返回选择的节点数组
     */
    List<Node<K, V>> getSelectedNodes(String instruction, Object[] args);
}
