package vip.ifmm;

import java.io.IOException;
import java.net.Socket;

/**
 * 关闭服务器的脚本
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 01:18:02
 */
public class Shutdown {

    public static void main(String[] args) {

        // 关闭服务器，连接到关闭服务器的端口即可
        try {
            System.out.println(Startup.application);
            //Socket socket = new Socket("127.0.0.1", Startup.application.getCloseNioServerPort());
            Socket socket = new Socket("127.0.0.1", 9999);
        } catch (NullPointerException | IOException e) {
            // 当没有启动服务的时候就会发生这个异常
            throw new RuntimeException("服务未启动！", e);
        }
    }
}
