package cn.com.fishin.lighter.core;

import cn.com.fishin.tuz.core.Tuz;

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
    private static final int NUMBER_OF_NODES = Integer.valueOf(Tuz.use("numberOfNodes", "16"));

    static {
        // 这边需要将 Tuz 的类锁定，防止有任何别人在操作，导致这边初始化出现问题
        synchronized (Tuz.class) {

            // 创建节点时需要使用多例模式
            boolean isSingleton = Tuz.getConfig().isSingleton();
            Tuz.getConfig().setSingleton(false);

            // TODO 初始化节点


            // 设置回原来的实例生成方式
            Tuz.getConfig().setSingleton(isSingleton);
        }
    }
}
