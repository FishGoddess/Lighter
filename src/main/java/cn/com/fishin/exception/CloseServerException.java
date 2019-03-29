package cn.com.fishin.exception;

/**
 * 服务器关闭异常
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 11:58:54
 */
public class CloseServerException extends RuntimeException {

    // 重写这个构造器即可
    public CloseServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
