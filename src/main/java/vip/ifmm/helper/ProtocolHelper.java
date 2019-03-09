package vip.ifmm.helper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import vip.ifmm.protocol.Command;

/**
 * HTTP 响应帮助类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/05 20:40:39
 */
public class ProtocolHelper {

    // 支持的请求类型
    public static final String SERVER_SUPPORT_METHOD = "POST";

    // 不支持请求方法的返回信息
    public static final String NOT_SUPPORT_METHOD = "{'response':'Only supports " + SERVER_SUPPORT_METHOD + " method!'";

    // 请求体为空的返回信息
    public static final String CONTENT_IS_EMPTY = "{'response':'You must write a content!'";

    public static final String PROTOCOL_PARSE_ERROR = "{'response':'Protocol parses error!'}";

    /**
     * 返回响应信息给前台
     *
     * @param responseMessage 要返回给前台的响应信息
     * @param contentType 响应内容类型
     * @return 返回响应对象
     */
    public static HttpResponse response(String responseMessage, String contentType) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(responseMessage, CharsetUtil.UTF_8)
        );

        // 设置响应头
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        return response;
    }

    /**
     * 返回响应信息给前台
     * 响应信息内容为 HTML
     *
     * @param responseMessage 要返回给前台的响应信息
     * @return 返回响应对象
     */
    public static HttpResponse responseHTML(String responseMessage) {
        return response(responseMessage, "text/html;charset=UTF-8");
    }

    /**
     * 返回响应信息给前台
     * 响应信息内容为 JSON
     *
     * @param responseMessage 要返回给前台的响应信息
     * @return 返回响应对象
     */
    public static HttpResponse responseJSON(String responseMessage) {
        return response(responseMessage, "application/json;charset=UTF-8");
    }

    /**
     * 检查请求体
     *
     * @param content 请求体
     * @return true 请求体合法，false 请求体不合法，注意这里的合法并不是说符合协议
     */
    public static boolean checkContent(ByteBuf content) {

        try {
            // 如果请求体为空，返回错误信息
            String contentString = content.toString();
            if (contentString == null || "".equals(contentString.trim())) {
                throw new RuntimeException(ProtocolHelper.CONTENT_IS_EMPTY);
            }
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

    /**
     * 检查请求指令
     *
     * @param command 请求指令
     * @return true 请求指令符合协议规范，false 反之不符合
     */
    public static boolean checkContent(Command command) {
        if (command == null) {
            return false;
        }

        // 判断里面具体的值是否合法
        // 这里判断的 value 只要不为 null 即可
        // 而 key 则不可以为 null 或 "" 或 "   "
        boolean isFieldBlank = isBlank(command.getInstruction());
        if (isFieldBlank) {
            return false;
        }

        return true;
    }

    /**
     * 判断字符串是否为空
     * null "" "   " 都视为空
     *
     * @param str 要被判断的字符串
     * @return true 为空字符串，false 否则不为空
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }
}
