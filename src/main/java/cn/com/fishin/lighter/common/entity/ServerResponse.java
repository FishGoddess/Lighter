package cn.com.fishin.lighter.common.entity;

import cn.com.fishin.lighter.common.enums.ServerState;

/**
 * 通用服务器响应 DTO 类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2018/11/12 21:54:23
 */
public class ServerResponse<T> {

    private int code;
    private String msg = null;
    private T data = null;

    private ServerResponse(ServerState state, T data) {
        this.code = state.getCode();
        this.msg = state.getMsg();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 返回服务器响应
    public static <T> ServerResponse<T> response(ServerState statable) {
        return ServerResponse.response(statable, null);
    }

    // 返回服务器响应
    public static <T> ServerResponse<T> response(ServerState statable, T data) {
        return new ServerResponse<T>(statable, data);
    }

    // 判断是否操作成功
    public boolean isSuccess() {
        return (code == ServerState.SUCCESS_CODE);
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
