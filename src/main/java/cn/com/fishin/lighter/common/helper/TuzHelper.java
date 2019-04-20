package cn.com.fishin.lighter.common.helper;

import cn.com.fishin.tuz.core.Tuz;
import cn.com.fishin.tuz.loader.ClasspathPropertiesLoader;
import cn.com.fishin.tuz.loader.FileSystemPropertiesLoader;

import java.io.IOException;

/**
 * Tuz 容器帮助类
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/17 21:34:49</p>
 */
public class TuzHelper {

    // 初始化 Tuz 容器
    public static void initTuz(String[] args) throws IOException {
        // 初始化 Tuz 容器
        if (args.length != 0) {
            // 从文件路径加载配置文件
            Tuz.load(new FileSystemPropertiesLoader(args[0]));
        } else {
            // 从类路径加载配置文件
            Tuz.load(new ClasspathPropertiesLoader("conf/config.properties"));
        }
    }
}
