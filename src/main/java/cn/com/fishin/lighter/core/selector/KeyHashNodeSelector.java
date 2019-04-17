package cn.com.fishin.lighter.core.selector;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.HashHelper;
import cn.com.fishin.lighter.core.LighterNodeManager;

/**
 * 根据 key 值哈希选择的节点选择器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 12:56:03</p>
 */
public class KeyHashNodeSelector implements NodeSelector {

    @Override
    public int[] select(Task task) {
        return new int[] {
                // 根据任务中的 key 值进行一次哈希计算得到选择的节点
                HashHelper.stringSimpleHash(task.getKey(), LighterNodeManager.NUMBER_OF_NODES)
        };
    }
}
