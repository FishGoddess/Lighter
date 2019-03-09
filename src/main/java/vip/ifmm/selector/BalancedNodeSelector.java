package vip.ifmm.selector;

import vip.ifmm.core.Node;

import java.util.List;

/**
 * 负载均衡节点选择器
 * 如果是伪节点集群，将耗费大量内存！
 * 如果配置这个节点选择器，请重写节点实现类，转而将每个节点的数据都分别存储在不同的服务器上
 * 这时候，这一台服务器将变成一台专用的节点选择器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:20:02
 */
public class BalancedNodeSelector implements NodeSelector<String, String> {

    // 所有节点
    private List<Node<String, String>> nodes = null;

    @Override
    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int[] getSelectedNodeIndexes(String instruction, Object[] args) {

        // 所有节点都要写入数据，以达到负载均衡的效果
        int numberOfNodes = nodes.size();
        int[] nodeIndexes = new int[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            nodeIndexes[i] = i;
        }

        return nodeIndexes;
    }

    @Override
    public List<Node<String, String>> getSelectedNodes(String instruction, Object[] args) {
        return nodes;
    }
}
