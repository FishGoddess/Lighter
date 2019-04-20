package net;

import cn.com.fishin.lighter.net.http.HttpServerInitializer;
import cn.com.fishin.lighter.net.NioServer;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.loader.ClasspathPropertiesLoader;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 测试网络模块
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 20:50:27</p>
 */
public class NioServerTest {

    @Test
    public void testNioServer() {

        try {
            Tuz.load(new ClasspathPropertiesLoader("conf/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        NioServer server = new NioServer();
        try {
            server.open(9669, 6969, new HttpServerInitializer(), () -> {
                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(6);
                        Socket socket = new Socket("127.0.0.1", 6969);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }, () -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
