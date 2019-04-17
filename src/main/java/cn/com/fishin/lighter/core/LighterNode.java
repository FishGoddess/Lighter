package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 节点接口
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 11:01:41</p>
 */
public interface LighterNode {

    // 获取数据
    Object fetch(Task task);

    // 保存数据
    Object save(Task task);

    // 移除数据
    Object remove(Task task);

    // 更新数据
    Object update(Task task);
}
