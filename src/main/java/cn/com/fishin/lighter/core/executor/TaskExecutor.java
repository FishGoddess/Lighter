package cn.com.fishin.lighter.core.executor;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.core.node.Node;

/**
 * 执行任务的执行器
 * 这个执行器用于执行任务，通过解析任务对象来获得一次任务执行的指令和数据
 * 你可以自己定义任务处理器，来达到自己的解析业务，甚至是将任务再进行一次加工操作
 * 比如，对特定的任务进行特定的处理，存进数据库或者缓存系统等等
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/16 20:56:55</p>
 */
public interface TaskExecutor {

    // 执行任务的方法
    Object execute(Node node, Task task);
}
