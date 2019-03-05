package vip.ifmm.exception;

/**
 * 参数不合法异常
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 11:58:54
 */
public class ArgumentException extends RuntimeException {

    // 重写这个构造器即可
    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
