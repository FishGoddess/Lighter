package cn.com.fishin.lighter.core.selector;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 节点选择器接口
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 10:30:48</p>
 */
public interface NodeSelector {

    // 根据任务选择节点
    // 可以同时选择多个，以此实现负载均衡和分布式缓存方案
    int[] select(Task task);
}
