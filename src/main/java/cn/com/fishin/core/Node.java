package cn.com.fishin.core;

/**
 * 自己定义的一个节点的接口
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 14:19:41
 */
public interface Node<K, V> {

    /**
     * 向节点中保存一个数据
     *
     * @param key 数据的 key
     * @param value 数据的 value
     * @return 返回 value
     */
    V save(K key, V value);

    /**
     * 向节点中保存一个数据
     *
     * @param key   数据的 key
     * @param value 数据的 value
     * @param expire 这个数据的存活时间，单位秒
     * @return 返回 value
     */
    V save(K key, V value, int expire);

    /**
     * 获取一个数据
     *
     * @param key 数据的 key
     * @return 返回获取到的数据
     */
    V fetch(K key);

    /**
     * 查找所有的 key
     *
     * @return 返回 key 的数组
     */
    K[] keys();

    /**
     * 获取这个节点的数据条数
     *
     * @return 返回这个节点的数据条数
     */
    int size();

    /**
     * 查看一个 key 是否存在于这个节点中
     *
     * @param key 要查询的 key
     * @return true 存在，false 不存在
     */
    boolean exists(K key);

    /**
     * 从一个节点中删除一个数据
     *
     * @param key 要被删除的数据的 key
     * @return 返回删除的数据
     */
    V remove(K key);

    /**
     * 删除一个节点的所有数据
     */
    void removeAll();
}
