package cn.com.fishin.core;

import java.util.Arrays;

/**
 * 结果封装类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/06 18:00:49
 */
public class Result<T> {

    // 结果对象
    private T result = null;

    // 额外参数
    private Object[] args = null;

    public Result() {}

    public Result(T result) {
        this.result = result;
    }

    public Result(T result, Object[] args) {
        this.result = result;
        this.args = args;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
