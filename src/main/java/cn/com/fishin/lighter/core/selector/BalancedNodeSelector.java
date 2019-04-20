package cn.com.fishin.lighter.core.selector;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.core.LighterNodeManager;
import cn.com.fishin.lighter.core.executor.TaskAction;

import java.util.Arrays;

/**
 * 负载均衡选择的节点选择器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 12:59:47</p>
 */
public class BalancedNodeSelector implements NodeSelector {

    @Override
    public int[] select(Task task) {

        // 系统功能只选择第一个
        if (TaskAction.LIGHTER.equals(task.getAction())) {
            return new int[] {0};
        }

        // 其他功能正常执行
        int[] selectNodeIndexes = new int[LighterNodeManager.NUMBER_OF_NODES];
        for (int i = 0; i < LighterNodeManager.NUMBER_OF_NODES; i++) {
            selectNodeIndexes[i] = i;
        }

        // 选择所有节点
        return selectNodeIndexes;
    }
}
