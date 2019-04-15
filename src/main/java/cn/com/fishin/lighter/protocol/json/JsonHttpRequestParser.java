package cn.com.fishin.lighter.protocol.json;

import cn.com.fishin.lighter.common.entity.Task;
import cn.com.fishin.lighter.protocol.RequestParser;
import io.netty.handler.codec.http.HttpRequest;

/**
 * HTTP 中使用的 JSON 协议解析器
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/15 22:27:04</p>
 */
public class JsonHttpRequestParser implements RequestParser<HttpRequest> {

    @Override
    public Task parse(HttpRequest request) {

        String uri = request.uri();
        System.out.println(uri);
        return null;
    }
}
