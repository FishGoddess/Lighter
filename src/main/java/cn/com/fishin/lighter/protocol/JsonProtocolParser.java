package cn.com.fishin.lighter.protocol;

import com.alibaba.fastjson.JSON;

/**
 * HTTP 协议的解析器
 * 简单使用 JSON 作为请求体
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 23:11:56
 */
public class JsonProtocolParser implements ProtocolParser {

    @Override
    public Command parse(String content) {
        return JSON.parseObject(content, Command.class);
    }
}
