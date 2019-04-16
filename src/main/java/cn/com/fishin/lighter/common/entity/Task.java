package cn.com.fishin.lighter.common.entity;

import cn.com.fishin.lighter.common.enums.TaskAction;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务实体类
 * 包含一次请求的任务信息
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 22:25:37</p>
 */
public class Task {

    private TaskAction action = null;
    private String key = null;
    private Object value = null;
    private Map<String, Object> args = new ConcurrentHashMap<>(8);

    public Task() {}

    // 不打算给别人使用这个有参构造器
    // 如果需要有参构造，请使用 make 方法
    private Task(TaskAction action, String key) {
        this.action = action;
        this.key = key;
    }

    // 构造一个任务对象
    public static Task make(TaskAction action, String key) {
        return make(action, key, null);
    }

    // 构造一个任务对象
    public static Task make(TaskAction action, String key, Object value) {
        return new Task(action, key).setValue(value);
    }

    public TaskAction getAction() {
        return action;
    }

    public Task setAction(TaskAction action) {
        this.action = action;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Task setKey(String key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Task setValue(Object value) {
        this.value = value;
        return this;
    }

    public Task addArgument(String key, Object argument) {
        args.put(key, argument);
        return this;
    }

    public Object getArgument(String key) {
        return args.get(key);
    }

    @Override
    public String toString() {
        return "Task{" +
                "action='" + action + '\'' +
                ", key='" + key + '\'' +
                ", value=" + value +
                ", args=" + args +
                '}';
    }
}
