package cn.com.fishin.lighter.common.exception;

/**
 * 动作执行异常
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 20:16:21</p>
 */
public class ActionException extends RuntimeException {

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
