# Lighter v1.0 Beta 轻量级键值对缓存服务
(目前正在开发，还未经过完整的测试！！)

(而且目前的代码还有很多可以重构以及改善的，尤其是目前的架构，功能加多之后，有些跑偏了。。。)

### 1. 使用用途：
你可以使用它来作为缓存服务器，或者是简易的分布式锁、消息队列消息去重，甚至是 redis 负载均衡等

### 2. 使用步骤：
源码中分别带有两个脚本：Startup 和 Shutdown

启动 Startup 即可启动项目，默认占用 8888 和 9999 两个端口

如果需要关闭，执行 Shutdown 即可

### 3. 使用到的依赖：
```xml
<properties>
    <spring.version>5.1.5.RELEASE</spring.version>
    <netty.version>4.1.33.Final</netty.version>
    <fastjson.version>1.2.56</fastjson.version>
    <logback.version>1.2.3</logback.version>
</properties>
```
#### i. Spring 作为整个项目的容器，使用到了它的 IOC 技术和监听器模式 
#### ii. Netty 作为整个项目的网络通信模块，使用到了它的 NIO 网络通信服务
#### iii. fastJson 作为协议解析器的一部分，是 Json 协议的解析库
#### iiii. logBack 作为项目的日志模块，使用 SLF4J 接口对接

### 4. 项目架构：
具体配置详见 resources/properties/config.properties
```properties
#############################################################
# Lighter 服务配置文件 v1.0
# 下面的配置仅仅是为了定制化 Lighter 服务，如没有这个需求，请不要随便改动
#                                          2019-3-3   水不要鱼
#############################################################

# 节点使用的接口实现类，该类必须实现 vip.ifmm.core.Node 接口
# 默认内部存储使用 java.util.concurrent.ConcurrentHashMap
# 你还可以自己定制一个真实存储在 redis 服务器上的 Map 实现类，从而实现 redis 负载均衡
# 或者是让一台机器专门做分发器，将多个分发器分出多个 Lighter 服务器
nodeClassName=vip.ifmm.core.DefaultMapNode

# 节点管理器，该管理器必须实现 vip.ifmm.core.NodeManageable 接口
# 它实现了 Spring 的 ApplicationListener 接口，因此它可以接收到事件发生
# 可以参考 vip.ifmm.core.DefaultNodeManager 的默认实现
# 具体配置在 classpath:/application-context.xml 中
nodeManager=vip.ifmm.core.DefaultNodeManager

# 节点数据事件处理器，专门用来处理节点数据事件的处理器
# 实现 vip.ifmm.handler.EventHandler 即可注册为事件处理器
# 当发生事件时，这个处理器中的处理方法将会被调用
nodeDataEventHandler=vip.ifmm.handler.DefaultNodeDataEventHandler

# 节点选择器，这个处理器决定了如何根据指令来选择一个节点甚至是一些节点
# 你可以自己实现 vip.ifmm.selector.NodeSelector 接口，然后配置在这里，
# 定制节点选择的规则，比如可以配置一个 Lighter 或者是 redis 的负载均衡集群
# 注意：如果 nodeSelector 配置为 vip.ifmm.selector.BalancedNodeSelector，
# 那每一个节点都将收到相应的指令，也就意味着数据可能会重复存储多份，也实现了负载均衡的效果，
# 但是，如果此时节点实现类仍然使用内置的 vip.ifmm.core.DefaultMapNode 节点实现类，
# 就会导致一台服务器上的内存被重复浪费，并且这个节点的集群并没有任何意义，因此，
# 在 nodeSelector 配置为 vip.ifmm.selector.BalancedNodeSelector 的情况下，
# 强烈建议你去重写一个节点的实现类！只需要实现 vip.ifmm.core.Node 接口即可
nodeSelector=vip.ifmm.selector.KeyHashNodeSelector

# 指令调用结果处理器
# 你可以自己实现 vip.ifmm.handler.ResultHandler 接口注册为结果处理器
# 最重要的一步就是在 application-context.xml 中将这个处理器注入到你的节点数据事件处理器中
resultHandler=vip.ifmm.handler.WebSocketResultHandler

# 协议指令和方法映射处理器
# 默认的映射处理器是根据节点实现类上的 @vip.ifmm.annotation.MethodMapping 注解
# 中的 instruction 属性值来匹配相应的方法名，使用反射技术去执行对应的方法
# 你可以自己实现 vip.ifmm.handler.MappingHandler 接口，然后重写自己的映射规则
# 你甚至可以结合数据库或者是网络来定制一个动态变化的映射处理器，以此来达到更复杂的业务需求
mappingHandler=vip.ifmm.handler.DefaultMappingHandler

# 初始化 Node 节点的个数，默认是 16 个
# 注意：如果 nodeSelector 配置为 vip.ifmm.selector.BalancedNodeSelector，
# 那每一个节点都将收到相应的指令，也就意味着数据可能会重复存储多份，也实现了负载均衡的效果，
# 但是，如果此时节点实现类仍然使用内置的 vip.ifmm.core.DefaultMapNode 节点实现类，
# 就会导致一台服务器上的内存被重复浪费，并且这个节点的集群并没有任何意义，因此，
# 在 nodeSelector 配置为 vip.ifmm.selector.BalancedNodeSelector 的情况下，
# 强烈建议你去重写一个节点的实现类！只需要实现 vip.ifmm.core.Node 接口即可
numberOfNodes=16

# Nio 服务器实现类，内部使用 Netty 实现
# 目前实现了 HTTP (目前暂时报废) / WebSocket / Light 三种对外公开 API 接口，
# 其中，Light 是自己实现的协议接口，没有 HTTP 协议的冗余信息，
# 直接使用 Netty 来做 NIO 的网络通信，类似于 RPC 远程调用，
# 实现客户端只需简单的网络编程即可，比 WebSocket 效率更高
nioServerInitializer=vip.ifmm.net.websocket.WebSocketServerInitializer
nioServerHandler=vip.ifmm.handler.WebSocketServerHandler

# 上面那个 Nio 服务器占用的端口
nioServerPort=8888

# 关闭服务器所占用的端口
# 当启动服务器之前，会开启一个监听线程，监听下面这个端口，
# 当有客户端连接到这个端口时，即认为需要关闭服务器，就会执行关闭服务器的操作
closeNioServerPort=9999

# 协议解析器，目前存在两种，分别是 Json 协议和 Light 协议，
# 你可以改写这个协议解析器，从而实现自己的协议，
# 推荐使用 Light 网络协议，更节省流量，而且更方便编写客户端
protocolParser=vip.ifmm.protocol.JsonProtocolParser
```

### 主要接口如下：

(1) vip.ifmm.core.Node 节点接口类

(2) vip.ifmm.core.NodeManageable 节点管理接口类

(3) vip.ifmm.handler.EventHandler 事件处理器接口

(4) vip.ifmm.handler.MappingHandler 指令和方法映射处理器

(5) vip.ifmm.handler.ResultHandler 指令执行结果处理器

(6) vip.ifmm.net.NioServerInitializer 服务器初始化接口类

(7) vip.ifmm.protocol.ProtocolParser 协议解析器

(8) vip.ifmm.protocol.ProtocolParserKeeper 协议解析器拥有者

(9) vip.ifmm.selector.NodeSelector 节点选择器

### 服务器运行分为解析和执行两步

#### 解析：
1. 客户端通过网络通信传输协议内容
2. 协议解析器拥有者通过协议解析器解析出当前指令，通过发布事件来结束解析阶段

#### 执行：
1. 事件处理器接收到事件的发生，通过节点选择器选择要执行指令的节点，通过映射器选择要执行的方法出来
2. 将具体要执行的指令和参数交给节点处理

