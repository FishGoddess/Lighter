package cn.com.fishin.lighter.core;

/**
 * Lighter 参数
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/16 11:13:28</p>
 */
public interface LighterArgument {

    // 存活周期时间，默认为 0，即永不过期
    String EXPIRE_TIME_ARGUMENT = "Lighter-Expire-Time";

    // 查看系统信息时使用到的常量
    String SYSTEM_INFO = "info";

    // 查看所有节点上 key 的总数量以及详细数量
    String NUMBER_OF_KEYS = "numberOfKeys";

    // 查看所有 key 时使用到的常量
    String KEYS = "keys";

    // 查看所有 value 时使用到的常量
    String VALUES = "values";
}
