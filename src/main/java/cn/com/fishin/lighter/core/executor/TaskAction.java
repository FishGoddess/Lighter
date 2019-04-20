package cn.com.fishin.lighter.core.executor;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.common.exception.ActionException;
import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.core.node.Node;
import cn.com.fishin.tuz.entity.InterceptedMethod;
import cn.com.fishin.tuz.factory.ProxyFactory;
import cn.com.fishin.tuz.interceptor.DefaultInterceptor;
import cn.com.fishin.tuz.interceptor.Interceptor;

/**
 * 任务指令枚举类
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 10:38:57</p>
 */
public enum TaskAction {

    // 获取数据动作
    GET(Node::get),

    // 保存数据动作
    SET(Node::set),

    // 如果不存在这个 key 值，就插入，否则直接返回
    SET_ABSENT(Node::setAbsent),

    // 移除数据动作
    REMOVE(Node::remove);

    // 动作执行器
    private TaskExecutor executor = null;

    // 任务处理器
    // 这里进行了一层拦截，就是对任务执行来一层次要业务的处理
    TaskAction(TaskExecutor executor) {
        this.executor = (TaskExecutor) ProxyFactory.wrap(
                executor,
                new Interceptor[] {
                        new TaskExecutorInterceptor()
                }
        );
        this.executor = executor;
    }

    // 执行动作
    public static Object action(Node node, Task task) {
        return task.getAction().executor.execute(node, task);
    }

    // 任务执行器拦截器
    // 这里需要对执行的任务持久化，以防止服务突然崩溃，导致的任务丢失
    // 在任务执行之后就需要持久化了，如果任务执行失败，就不会持久化了
    private static class TaskExecutorInterceptor extends DefaultInterceptor {

        @Override
        public boolean before(InterceptedMethod method) {
            // 调试记录
            LogHelper.debug("Action " + method.getThisMethod().getName() + " is executing!");
            return true;
        }

        @Override
        public boolean after(InterceptedMethod method) {
            // TODO 持久化操作
            return true;
        }

        @Override
        public boolean afterThrowing(InterceptedMethod method) {

            // 记录异常
            method.setException(new ActionException(
                    "Action " + method.getThisMethod().getName() + " executed failed!",
                    method.getException())
            );

            // 错误记录
            LogHelper.error(method.getException().getMessage(), method.getException());
            return true;
        }
    }
}
