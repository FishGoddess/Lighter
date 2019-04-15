package net;

import cn.com.fishin.lighter.net.HttpServerInitializer;
import cn.com.fishin.lighter.net.NioServer;
import org.junit.Test;

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
        NioServer server = new NioServer();
        try {
            server.open(9669, 6969, new HttpServerInitializer());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.closeGracefully();
        }
    }
}
