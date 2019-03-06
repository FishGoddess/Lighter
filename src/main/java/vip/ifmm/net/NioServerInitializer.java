package vip.ifmm.net;

import io.netty.channel.ChannelHandler;

/**
 * NIO 服务器必须实现支持的接口
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/06 17:15:56
 */
public interface NioServerInitializer {

    /**
     * 通道处理器
     *
     * @param channelHandler 要使用的通道处理器
     */
    void setChannelHandler(ChannelHandler channelHandler);
}
