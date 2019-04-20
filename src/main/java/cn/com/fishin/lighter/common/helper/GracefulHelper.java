package cn.com.fishin.lighter.common.helper;

/**
 * 让我更加优雅地写代码
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 12:21:23</p>
 */
public class GracefulHelper {

    // 如果 str 为空，就返回 ifNull
    public static Object ifNull(Object object, Object notNullObject) {
        return isNull(object) ? notNullObject : object;
    }

    // 判断对象是否为 null
    public static boolean isNull(Object object) {
        return object == null;
    }

    // 判断对象是否不为 null
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }
}
