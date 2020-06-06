package cn.com.fishin.lighter.common.exception;

/**
 * 请求异常
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/16 12:29:14</p>
 */
public class RequestException extends RuntimeException {

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
