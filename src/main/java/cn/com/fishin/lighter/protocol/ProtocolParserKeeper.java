package cn.com.fishin.lighter.protocol;

/**
 * 协议解析器拥有者
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/06 17:27:59
 */
public interface ProtocolParserKeeper {

    /**
     * 协议解析器
     *
     * @param protocolParser 协议解析器
     */
    void setProtocolParser(ProtocolParser protocolParser);
}
