package cn.com.fishin.lighter.common.exception;

/**
 * 执行异常
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/16 21:48:33</p>
 */
public class ExecuteException extends RuntimeException{

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
