package cn.com.fishin.lighter;

import cn.com.fishin.lighter.common.helper.LogHelper;
import cn.com.fishin.lighter.common.helper.TuzHelper;
import cn.com.fishin.tuz.core.Tuz;
import java.net.Socket;

/**
 * 关闭服务脚本
 *
 * @author Fish
 * <p>Email: fishgoddess@qq.com</p>
 * <p>created by 2019/04/17 21:27:32</p>
 */
public class Shutdown {

    public static void main(String[] args) {

        try {
            // 初始化 Tuz 容器
            TuzHelper.initTuz(args);

            // 发送关闭服务器请求
            Socket closeSocket = new Socket("127.0.0.1", Integer.valueOf(Tuz.use("server.closePort")));
            LogHelper.info("Server closed successfully!");
        } catch (Exception e) {
            LogHelper.error("Server closed failed! cause: " + e.getMessage(), e);
        }
    }
}
