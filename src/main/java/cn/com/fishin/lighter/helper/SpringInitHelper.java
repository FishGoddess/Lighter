package cn.com.fishin.lighter.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 初始化的帮助类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 11:38:38
 */
public class SpringInitHelper {

    /**
     * 初始化 Spring 容器
     *
     * @param xmlLocation   Spring 的 XML 配置文件位置
     */
    public static ApplicationContext initSpring(String xmlLocation) {

        // 是否从类路径加载配置文件
        if (xmlLocation.startsWith("classpath:")) {
            // 从类路径加载配置文件
            return new ClassPathXmlApplicationContext(xmlLocation);
        } else {
            // 从文件路径加载配置文件
            return new FileSystemXmlApplicationContext(xmlLocation);
        }
    }
}
