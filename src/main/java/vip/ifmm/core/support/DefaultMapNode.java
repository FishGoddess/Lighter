package vip.ifmm.core.support;

import vip.ifmm.annotation.MethodMapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的 KV 键值对节点
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 14:20:27
 */
public class DefaultMapNode<K, V> implements Node<K, V> {

    // 内部真正使用的存储数据的节点
    private Map<K, V> node = new ConcurrentHashMap<>(32);

    @Override
    @MethodMapping("save")
    public V save(K key, V value) {
        return save(key, value, -1);
    }

    @Override
    @MethodMapping("save")
    public V save(K key, V value, int expire) {
        return node.put(key, value);
    }

    @Override
    @MethodMapping("fetch")
    public V fetch(K key) {
        return node.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    @MethodMapping("keys")
    public K[] keys() {
        return (K[]) node.keySet().toArray();
    }

    @Override
    @MethodMapping("size")
    public int size() {
        return node.size();
    }

    @Override
    @MethodMapping("exists")
    public boolean exists(K key) {
        return node.containsKey(key);
    }

    @Override
    @MethodMapping("remove")
    public V remove(K key) {
        return node.remove(key);
    }

    @Override
    @MethodMapping("removeAll")
    public void removeAll() {
        node.clear();
    }
}
