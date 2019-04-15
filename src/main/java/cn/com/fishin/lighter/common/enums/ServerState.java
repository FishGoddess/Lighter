package cn.com.fishin.lighter.common.enums;

/**
 * 状态接口类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2018/11/12 21:43:56
 */
public interface ServerState {

    // 成功状态码，默认是 0
    int SUCCESS_CODE = 0;

    int getCode();
    String getMsg();
}
