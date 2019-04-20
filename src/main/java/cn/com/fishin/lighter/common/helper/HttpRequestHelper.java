package cn.com.fishin.lighter.common.helper;

import io.netty.handler.codec.http.HttpRequest;

/**
 * HTTP 请求帮助类
 *
 * @author Fish
 * <p>Email: fishinlove@163.com</p>
 * <p>created by 2019/04/16 12:41:27</p>
 */
public class HttpRequestHelper {

    // 判断是否是 GET 请求
    public static boolean isGet(HttpRequest request) {
        return "GET".equals(method(request));
    }

    // 判断是否是 POST 请求
    public static boolean isPost(HttpRequest request) {
        return "POST".equals(method(request));
    }

    // 判断是否是 DELETE 请求
    public static boolean isDelete(HttpRequest request) {
        return "DELETE".equals(method(request));
    }

    // 判断是否是 PUT 请求
    public static boolean isPut(HttpRequest request) {
        return "PUT".equals(method(request));
    }

    // 判断是否是 OPTIONS 请求
    public static boolean isOptions(HttpRequest request) {
        return "OPTIONS".equals(method(request));
    }

    // 获得 request 的请求方法
    public static String method(HttpRequest request) {
        return request.method().name().toUpperCase();
    }

    // 获得请求 uri 数组
    // 比如，/fetch/key  ===> [fetch, key]
    public static String[] uris(HttpRequest request) {
        // 先去掉开头的 / ，也就是 /fetch/key ===> fetch/key，然后使用 / 分割
        return request.uri().substring(1).split("/");
    }
}
