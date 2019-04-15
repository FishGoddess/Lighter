package cn.com.fishin.lighter.common.enums.state;

import cn.com.fishin.lighter.common.enums.ServerState;

/**
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 20:22:14</p>
 */
public enum NetServerState implements ServerState {

    SUCCESS(ServerState.SUCCESS_CODE, "Done"),
    NET_ERROR(-1000, "Error");

    private int code = ServerState.SUCCESS_CODE;
    private String msg = null;

    NetServerState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
