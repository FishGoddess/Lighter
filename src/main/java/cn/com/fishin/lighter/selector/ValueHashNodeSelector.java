package cn.com.fishin.lighter.selector;

import cn.com.fishin.lighter.core.Node;
import cn.com.fishin.lighter.helper.HashHelper;

import java.util.List;

/**
 * 根据 value 值散列的节点选择器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:55:25
 */
@Deprecated
public class ValueHashNodeSelector implements NodeSelector<String, String> {

    // 所有节点
    private List<Node<String, String>> nodes = null;

    @Override
    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int[] getSelectedNodeIndexes(String instruction, Object[] args) {
        return new int[] {
                HashHelper.stringSimpleHash((String) args[1], nodes.size())
        };
    }

    @Override
    public List<Node<String, String>> getSelectedNodes(String instruction, Object[] args) {
        int selectedIndex = getSelectedNodeIndexes(instruction, args)[0];
        return nodes.subList(selectedIndex, selectedIndex + 1);
    }
}
