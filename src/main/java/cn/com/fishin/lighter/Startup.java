package cn.com.fishin.lighter;

import cn.com.fishin.lighter.common.helper.TuzHelper;
import cn.com.fishin.lighter.core.LighterExecutor;
import cn.com.fishin.lighter.core.LighterNodeManager;
import cn.com.fishin.lighter.core.LighterParser;
import cn.com.fishin.lighter.net.NioServer;
import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.plugin.DiPlugin;
import io.netty.channel.ChannelInitializer;


/**
 * 启动类
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 23:01:25</p>
 */
public class Startup {

    // 打印启动标志
    private static void printSymbol() {
        System.out.println();
        System.out.println("         **");
        System.out.println("        *  *");
        System.out.println("       *    *               |          ..");
        System.out.println("      *      *              |__        ||");
        System.out.println("      ********");
        System.out.println("       ******                __");
        System.out.println("        ****                | _       |__|");
        System.out.println("     ___________            |__|      |  |");
        System.out.println("    |           |");
        System.out.println("    |           |                ____");
        System.out.println("    |    ^_^    |                 ||");
        System.out.println("    |           |");
        System.out.println("    |           |          www.fishin.com.cn");
        System.out.println("    |    ^_^    |");
        System.out.println("    |           |        Startup successfully...");
        System.out.println("    |___________|            Enjoy yourself!");
        System.out.println();
    }

    public static void main(String[] args) throws Exception {

        // 初始化 Tuz 容器
        TuzHelper.initTuz(args);

        // 初始化服务器
        // 同时会监听两个端口，一个用于正常服务通信，一个用于关闭服务器
        NioServer server = new NioServer();
        server.open(
                Integer.valueOf(Tuz.use("server.port")),
                Integer.valueOf(Tuz.use("server.closePort")),
                DiPlugin.useInstance(ChannelInitializer.class),
                () -> {
                    LighterNodeManager.init(); // 初始化节点管理器
                    LighterExecutor.init(); // 初始化执行器
                    LighterParser.init(); // 初始化解析器
                    printSymbol(); // 打印图标
                },
                () -> {
                    LighterExecutor.shutdown(); // 关闭执行器
                }
        );
    }
}
