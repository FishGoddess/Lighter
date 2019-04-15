package cn.com.fishin.lighter.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>日志记录帮助类</p>
 * <p>Log Helper</p>
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/03/28 16:29:20</p>
 */
public class LogHelper {

    // 实际上这个代码应该每个类都有的，但是这里为了方便，就直接使用这个类
    // 为了方便切换日志模块
    private static final Logger logger = LoggerFactory.getLogger(LogHelper.class);

    /**
     * <p>输出调试信息</p>
     * <p>Output debug message</p>
     *
     * @param msg <p>调试信息</p>
     *            <p>Debug message</p>
     */
    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    /**
     * <p>输出正常信息</p>
     * <p>Output info message</p>
     *
     * @param msg <p>正常信息</p>
     *            <p>info message</p>
     */
    public static void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    /**
     * <p>输出警告信息</p>
     * <p>Output warn message</p>
     *
     * @param msg <p>警告信息</p>
     *            <p>Warn message</p>
     */
    public static void warn(String msg) {
        warn(msg, null);
    }

    /**
     * <p>输出警告信息</p>
     * <p>Output warn message</p>
     *
     * @param msg <p>警告信息</p>
     *            <p>Warn message</p>
     * @param e <p>抛出的异常</p>
     *          <p>Throw the exception</p>
     */
    public static void warn(String msg, Throwable e) {
        logger.warn(msg, e);
    }

    /**
     * <p>输出错误信息</p>
     * <p>Output error message</p>
     *
     * @param msg <p>错误信息</p>
     *            <p>error message</p>
     * @param t   <p>抛出的异常</p>
     *            <p>Throw the exception</p>
     */
    public static void error(String msg, Throwable t) {
        logger.error(msg, t);
    }
}
