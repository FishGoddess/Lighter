package vip.ifmm.helper;

/**
 * 散列帮助工具类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 23:12:52
 */
public class HashHelper {

    public static int stringSimpleHash(String str, int range) {

        int hashCode = str.hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }

        return hashCode % range;
    }
}
