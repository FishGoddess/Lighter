package vip.ifmm.selector;

import vip.ifmm.core.Node;
import vip.ifmm.helper.HashHelper;

import java.util.List;

/**
 * 根据 key 值散列的节点选择器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:55:25
 */
public class KeyHashNodeSelector implements NodeSelector<String, String, String> {

    // 所有节点
    private List<Node<String, String>> nodes = null;

    @Override
    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int[] getSelectedNodeIndexes(String instruction, String key, String value, String... args) {
        return new int[] {
                HashHelper.stringSimpleHash(key, nodes.size())
        };
    }

    @Override
    public List<Node<String, String>> getSelectedNodes(String instruction, String key, String value, String... args) {
        int selectedIndex = getSelectedNodeIndexes(instruction, key, value, args)[0];
        return nodes.subList(selectedIndex, selectedIndex + 1);
    }
}
