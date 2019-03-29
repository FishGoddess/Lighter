package cn.com.fishin.lighter.handler;

/**
 * 默认的结果处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/04 14:42:13
 */
@Deprecated // This is only for test !
public class DefaultResultHandler implements ResultHandler<String> {

    @Override
    public boolean handle(String result) {

        // TODO 目前只是简单的打印
        System.out.println(result);
        return true;
    }
}
