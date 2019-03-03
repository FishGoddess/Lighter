package vip.ifmm.selector;

/**
 * 节点选择器
 * 用于选择一个节点来存储
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 22:08:47
 */
public interface NodeSelector {

    /**
     * 根据情况获取节点索引
     * 如果不止一个，就返回多个索引
     *
     * @param instruction 协议传过来的指令
     * @param key 数据的 key 值
     * @param value 数据的 value 值
     * @param args 额外的参数
     * @return 返回选择的节点索引数组
     */
    int[] getNodeIndexs(String instruction, String key, String value, String... args);


}
