package cn.com.fishin.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * 专门用来判断参数的工具类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 00:38:09
 */
public class ArgsHelper {

    // Spring 的 XML 配置文件
    public static final String XML_LOCATION = "-xml";
    private static final String DEFAULT_XML_LOCATION = "classpath:application-context.xml";

    // 参数映射表
    // 这里的 String 是所有启动参数，Integer 是这个参数所在下标
    private static Map<String, Integer> argsTable = null;

    // 所有启动参数
    private static String[] args = null;

    /**
     * 初始化
     *
     * @param args 所有启动参数
     */
    public static void init(String[] args) {
        ArgsHelper.args = args;

        // 构建参数映射表
        buildArgsTable();
    }

    // 构建参数映射表
    private static void buildArgsTable() {
        argsTable = new HashMap<>(args.length * 2);

        // 构建参数映射表
        int length = args.length;
        for (int i = 0; i < length; i++) {
            argsTable.put(args[i].toLowerCase(), i);
        }
    }

    /**
     * 判断是否含有某个指令
     *
     * @param argument 要被判断的指令
     * @return true 含有，false 不含有
     */
    public static boolean hasArgument(String argument) {
        return argsTable.get(argument) != null;
    }

    /**
     * 获得 XmlLocation
     *
     * @return 返回 XmlLocation
     */
    public static String getXmlLocation() {
        // 如果参数没有直接返回默认位置
        if (!hasArgument(XML_LOCATION)) {
            return DEFAULT_XML_LOCATION;
        }

        // 判断是不是只有 -xml 参数，但是没有参数内容
        int index = argsTable.get(XML_LOCATION) + 1;
        if (index >= args.length) {
            return DEFAULT_XML_LOCATION;
        }

        // 返回 XmlLocation
        return args[index];
    }
}
