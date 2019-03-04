package vip.ifmm.handler;

/**
 * 处理指令执行结果的处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 14:37:07
 */
public interface ResultHandler<T> {

    /**
     * 处理结果
     *
     * @param result 要被处理的结果
     * @return true 处理成功，false 处理失败
     */
    boolean handle(T result);
}
