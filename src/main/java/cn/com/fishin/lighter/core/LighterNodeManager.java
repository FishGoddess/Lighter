package cn.com.fishin.lighter.core;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.executor.TaskAction;
import cn.com.fishin.lighter.core.node.Node;
import cn.com.fishin.lighter.core.selector.NodeSelector;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.plugin.DiPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        return invokeAll(selector.select(task), task);
    }

    // 在所有节点上执行
    private static Object invokeAll(int[] nodeIndexes, Task task) {
        // 所有选择的节点都执行任务
        Object[] results = new Object[nodeIndexes.length];
        for (int i = 0; i < nodeIndexes.length; i++) {
            results[i] = TaskAction.action(NODES[nodeIndexes[i]], task);
        }
        return results;
    }

    // 返回当前系统存储的 key 的个数
    // 这个方法并不保证强一致性！！也就是说如果在执行时，个数被修改，它不会有所感知
    // 这么做是因为防止次要的系统功能占用过多 CPU 时间，导致业务不可用
    public static Map<String, Object> numberOfKeys() {

        Map<String, Object> result = new HashMap<>(4);
        Map<String, Object> details = new HashMap<>(NUMBER_OF_NODES << 2);
        int sum = 0;
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            int number = NODES[i].numberOfKeys();

            // 统计总和
            sum = sum + number;

            // 每一个节点的个数
            details.put("node_" + i, number);
        }

        result.put("total", sum);
        result.put("details", details);
        return result;
    }

    // 返回系统信息
    public static Map<String, Object> systemInfo() {

        // 组装系统信息
        Map<String, Object> result = new HashMap<>();
        result.put(LighterArgument.NUMBER_OF_KEYS, numberOfKeys());
        result.put(LighterArgument.KEYS, keys());
        result.put(LighterArgument.VALUES, values());
        result.put("numberOfNodes", NUMBER_OF_NODES);
        return result;
    }

    // 返回所有节点上的所有的 key
    public static Map<String, Object> keys() {
        Map<String, Object> result = new HashMap<>(NUMBER_OF_NODES << 2);
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result.put("node_" + i, NODES[i].keys());
        }

        // 返回结果
        return result;
    }

    // 返回所有节点上的所有的 value
    public static Map<String, Object> values() {
        Map<String, Object> result = new HashMap<>(NUMBER_OF_NODES << 2);
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            result.put("node_" + i, NODES[i].values());
        }

        // 返回结果
        return result;
    }
}
