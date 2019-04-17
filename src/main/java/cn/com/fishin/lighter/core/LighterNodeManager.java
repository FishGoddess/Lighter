package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.executor.TaskAction;
import cn.com.fishin.lighter.core.node.Node;
import cn.com.fishin.lighter.core.selector.NodeSelector;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.plugin.DiPlugin;

import java.util.Arrays;

/**
 * 节点管理者
 * 这也是 Lighter 服务的核心功能提供者
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 10:31:50</p>
 */
public class LighterNodeManager {

    // 节点数量
    public static final int NUMBER_OF_NODES = Integer.valueOf(Tuz.useGracefully("numberOfNodes", "16"));

    // 节点容器
    private static final Node[] NODES = new Node[NUMBER_OF_NODES];

    // 节点选择器
    private static final NodeSelector selector = DiPlugin.useInstance(NodeSelector.class);

    // 初始化
    public static void init() {
        initNodes();
    }

    // 初始化节点
    private static void initNodes() {
        // 这边需要将 Tuz 的类锁定，防止有任何别人在操作，导致这边初始化出现问题
        synchronized (Tuz.class) {

            // 创建节点时需要使用多例模式
            boolean isSingleton = Tuz.getConfig().isSingleton();
            Tuz.getConfig().setSingleton(false);

            // 初始化节点
            for (int i = 0; i < NUMBER_OF_NODES; i++) {
                NODES[i] = DiPlugin.useInstance(Node.class);
            }

            // 设置回原来的实例生成方式
            Tuz.getConfig().setSingleton(isSingleton);
        }

        // 日志记录
        LogHelper.info("Nodes are ready!");
        LogHelper.debug("Nodes are " + Arrays.toString(NODES));
    }

    // 提交任务
    static Object submit(Task task) {
        // 选择要处理的节点
        return invokeAll(selector.select(task), task);
    }

    // 在所有节点上执行
    private static Object invokeAll(int[] nodeIndexes, Task task) {
        // 所有选择的节点都执行任务
        Object[] results = new Object[nodeIndexes.length];
        for (int i = 0; i < nodeIndexes.length; i++) {
            results[i] = TaskAction.action(NODES[i], task);
        }
        return results;
    }
}
