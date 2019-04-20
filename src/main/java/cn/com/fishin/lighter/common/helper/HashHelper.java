package cn.com.fishin.lighter.common.helper;

/**
 * 散列帮助工具类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 23:12:52
 */
public class HashHelper {

    /**
     * 简单的字符串散列算法
     *
     * @param str 要被计算散列值的方法
     * @param range 散列的范围
     * @return 返回散列值
     */
    public static int stringSimpleHash(String str, int range) {

        // 如果是 null 则返回 0
        if (GracefulHelper.isNull(str)) {
            return 0;
        }

        // 不是 null 就直接返回 hashCode()
        int hashCode = str.hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }

        return hashCode % range;
    }
}
