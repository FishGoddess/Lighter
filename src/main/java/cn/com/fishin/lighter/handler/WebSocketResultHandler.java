package cn.com.fishin.lighter.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import cn.com.fishin.lighter.core.Result;

/**
 * WebSocket 结果处理器
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/06 18:23:20
 */
public class WebSocketResultHandler implements ResultHandler<Result<String>> {

    @Override
    public boolean handle(Result<String> result) {

        // 解析出这次传输的通道，第一个参数即为通道
        Object[] args = result.getArgs();
        if (args != null && args.length > 0 && args[0] != null) {
            Channel ch = (Channel) args[0];

            String resp = result.getResult();
            if (resp == null) {
                resp = "result is null";
            }

            // 返回数据
            // 注意这边的 TextWebSocketFrame，必须发送这个信息，否则会报异常！！
            // 第一次写这个类的时候漏了写这个，消息一直发送不回去，
            // 然后调试了两天，最后发现居然是这里爆出了不支持信息类型的异常
            ch.writeAndFlush(new TextWebSocketFrame(resp));
            return true;
        }

        return false;
    }
}
