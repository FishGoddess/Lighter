package vip.ifmm.handler;

import io.netty.channel.ChannelOutboundInvoker;
import vip.ifmm.core.Result;

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
            ChannelOutboundInvoker channel = (ChannelOutboundInvoker)args[0];
            channel.writeAndFlush(result.getResult());
            return true;
        }

        return false;
    }
}
