package cn.com.fishin.lighter.protocol;

/**
 * 协议解析器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 23:07:07
 */
public interface ProtocolParser {

    /**
     * 根据协议解析内容
     *
     * @param content 内容
     * @return 返回解析得到的指令对象
     */
    Command parse(String content);
}
