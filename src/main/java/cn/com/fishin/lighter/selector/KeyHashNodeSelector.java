package cn.com.fishin.lighter.selector;

import cn.com.fishin.lighter.core.Node;
import cn.com.fishin.lighter.helper.HashHelper;

import java.util.List;

/**
 * 根据 key 值散列的节点选择器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:55:25
 */
public class KeyHashNodeSelector implements NodeSelector<String, String> {

    // 所有节点
    private List<Node<String, String>> nodes = null;

    @Override
    public void setNodes(List<Node<String, String>> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int[] getSelectedNodeIndexes(String instruction, Object[] args) {

        // 如果指令不含 key 值，就选择所有节点
        if (args.length == 0) {
            int[] indexes = new int[nodes.size()];
            for (int i = 0; i< indexes.length; i++) {
                indexes[i] = i;
            }

            return indexes;
        }

        // 不会返回空数组
        return new int[] {
                HashHelper.stringSimpleHash((String) args[0], nodes.size())
        };
    }

    @Override
    public List<Node<String, String>> getSelectedNodes(String instruction, Object[] args) {

        // 如果指令不含 key 值，就选择所有节点
        if (args.length == 0) {
            return nodes;
        }

        // 选择第一个下标元素作为选择的节点号数
        int selectedIndex = getSelectedNodeIndexes(instruction, args)[0];
        return nodes.subList(selectedIndex, selectedIndex + 1);
    }
}
