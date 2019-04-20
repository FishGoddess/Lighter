package cn.com.fishin.lighter.core.node;

import cn.com.fishin.lighter.common.entity.Task;

/**
 * 节点接口
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 11:01:41</p>
 */
public interface Node {

    // 获取数据
    Object get(Task task);

    // 保存数据
    Object set(Task task);

    // 不存在才保存
    Object setAbsent(Task task);

    // 移除数据
    Object remove(Task task);

    // 返回当前节点 key 个数
    int numberOfKeys();
}
