# Lighter 轻量级对象缓存服务中间件
(目前正在重构！！如果需要使用，请切换到 master 分支！！)

(而且目前的代码还有很多可以重构以及改善的，尤其是目前的架构，功能加多之后，有些跑偏了。。。)

### 1. 使用用途：
主要用作于缓存中间件，带有默认的缓存实现，并且允许多个节点的缓存，这也实现了负载均衡和分布式缓存。

使用 Spring 做容器托管，并利用它的 IOC 技术和监听者模式。

在网络通信方面使用的是 Netty，并且实现了两个网络协议，一个是 JSON 格式传输，一个是自定义的协议。

整个项目的所有组件都允许自定义，包括缓存实现、节点选择、协议解析和网络通信协议。

### 2. 使用步骤：
源码中分别带有两个脚本：Startup 和 Shutdown

启动 Startup 即可启动项目，默认占用 8888 和 9999 两个端口

如果需要关闭，执行 Shutdown 即可

### 3. 使用到的依赖：
```xml
<properties>
    <netty.version>4.1.33.Final</netty.version>
    <fastjson.version>1.2.56</fastjson.version>
    <logback.version>1.2.3</logback.version>
    <tuz.version>0.6.4-BETA</tuz.version>
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
# Lighter 服务配置文件 v1.0.9
# 下面的配置仅仅是为了定制化 Lighter 服务，如没有这个需求，请不要随便改动
#                                          2019-4-15   水不要鱼
#############################################################

# 服务器监听端口
# 这个端口是内置服务器使用的通信端口，默认是 Netty 实现的 Nio 服务器
server.port=9669

# 服务器监听的关闭端口
# 由于服务器是运行且阻塞等待的，所以需要使用网络通信来关闭服务器
# 默认情况下会开启一个监听线程，只要监听到这个端口有客户端连接就关闭服务器
server.closePort=9999

# 服务器初始化器
# 这个服务器具体使用什么网络模式来进行和客户端的通信也是可以自定义的
# 你可以增加一个你自己的服务器初始化器以实现你想要的通信方式，只需实现一些方法即可
# 由于整个服务器使用的是 Netty 来通信的，所以如果你准备改写这个服务器初始化器
# 你最好具备有 Netty 的编程经验，否则可能导致服务不可用的后果
# 同时，由于默认使用的是 HTTP 协议，所以在请求时可能会产生大量的冗余信息
# 这在高并发访问下会浪费大量的网络资源，我也注意到了这一点
# 只是单从使用的角度上来说的话，HTTP 协议的确更加方便，同时可以跨平台跨语言以及高兼容性
# 所以就选择了 HTTP 协议来作为默认的应用协议
ChannelInitializer=cn.com.fishin.lighter.net.http.HttpServerInitializer

# 协议解析器
# 当接收到用户的请求时，需要解析成一个任务对象提供给任务执行器去执行
# 你可以自定义协议解析器，以此来实现你的特殊业务解析或者实现你自己的协议
# 你只需实现 cn.com.fishin.lighter.protocol.RequestParser 接口
# 然后重写 parse 方法，将用户请求解析成一个任务对象返回即可
RequestParser=cn.com.fishin.lighter.protocol.json.JsonHttpRequestParser

# 线程池属性
# 属性包含：核心线程数 & 最大线程数 & 线程存活时间 & 等待队列大小 & 队列拒绝策略
# 注意：以下属性需要对 JDK 中的线程池有一定的了解才可以更改，否则请保持默认设置
# 具体可以参考 java.util.concurrent.ThreadPoolExecutor 线程池类
# 底层执行器使用线程池来执行任务，你可以设置这个线程池的核心线程数以及最大线程数
# 这两个值需要根据系统性能以及具体业务并发量而定，另外，一个系统也会对线程数量有限制
# 在 JVM 中可以设置线程栈大小，这个大小对可以使用的线程数量有影响，详情参考 JVM 指令
corePoolSize=512
maximumPoolSize=1024
# 线程存活时间的设置和一个任务执行的时间应该差不多，由于这个任务一般是短时间任务
# 所以这个值不建议设置的太大，应该要设置为任务执行时间的 2 倍左右，这样可以复用大量线程
keepAliveTime=10
# 等待队列大小和队列拒绝策略一般会有关联，当队列满了就会开始新增线程，当最大线程数达到了
# 这个线程池就会执行拒绝策略，由于 Lighter 是一个对象缓存服务，因此是允许缓存不命中的情况的
# 但是这个值不应该设置得太小，以免造成缓存雪崩和缓存穿透，需要结合系统性能来决定大小
waitQueueSize=2048
RejectedExecutionHandler=cn.com.fishin.lighter.core.DefaultRejectedExecutionHandler

# 任务执行器
# 这个执行器用于执行任务，通过解析任务对象来获得一次任务执行的指令和数据
# 你可以自己定义任务处理器，来达到自己的解析业务，甚至是将任务再进行一次加工操作
# 比如，对特定的任务进行特定的处理，存进数据库或者缓存系统等等
TaskExecutor=cn.com.fishin.lighter.core.executor.DefaultTaskExecutor

# 节点个数
# Lighter 服务器将支持多节点管理，每个节点可以是一个独立的服务器，甚至是集群
# 这个节点数量不能设置的太大，应该要根据业务来定，如果业务需要较多节点，你可以设置大数量
# 另外，Lighter 只提供默认的节点实现，这个节点实现很难满足高并发下的所有需求
# 所以，我推荐自己实现一个节点，结合业务进行定制，让 Lighter 单纯作为缓存中间件也是可以的
numberOfNodes=16

# 节点实现类
# 这是默认实现节点，一般情况下可以使用，但如果是高并发的场景下，还是建议再重写一个节点实现
# 尽可能地和业务场景相契合，毕竟通用的实现无法满足所有情况，尤其是在性能上
# 默认使用 java.util.concurrent.ConcurrentHashMap 来存储数据，保证了并发的安全
# 此外，为了防止大数据量时 Map 的扩容导致性能损失，这里针对初始值进行了调整，
# 由于线程数肯定是根据业务量来定的，所以这里取线程数的一半作为初始值，在代码中可以看到这一行：
# private static final int INITIAL_CAPACITY = (Integer.valueOf(Tuz.use("corePoolSize")) >> 2);
LighterNode=cn.com.fishin.lighter.core.node.DefaultNode

# 节点选择器
# 当接收到一个任务时，选择在哪个节点上执行就需要靠这个节点选择器来选择了
# 默认内置两个实现，一个是负载均衡选择器，一个是 key 哈希选择器
# 负载均衡选择器会在所有节点中执行，另外，由于缓存数据不需要强一致性，
# 所以这里的负载均衡没有使用高扩展性，也就是说其中一台机器崩溃了，其他机器不会有所感知
# 即使没有执行成功，也不会再去执行，也就有可能导致负载均衡的所有机器上数据并不一定完全一致
# key 哈希选择器使用哈希算法，将任务分散到不同的节点上执行，这是初步的分布式缓存实现
# 这个选择器同样是弱一致性的，也就是说其中一台机器崩溃，导致的任务丢失是允许的
# 在默认节点实现下，这个崩溃的可能性几乎不存在，但如果是自定义的节点实现，比如 redis 节点实现
# 就有可能因为机器故障而导致节点写入失败等操作问题，此时将失去这个哈希的范围，也就是缓存命中失败
NodeSelector=cn.com.fishin.lighter.core.selector.KeyHashNodeSelector
```

### 主要接口如下：

(1) cn.com.fishin.lighter.core.Node 节点接口类

(2) cn.com.fishin.lighter.core.NodeManageable 节点管理接口类

(3) cn.com.fishin.lighter.handler.EventHandler 事件处理器接口

(4) cn.com.fishin.lighter.handler.MappingHandler 映射处理器

(5) cn.com.fishin.lighter.handler.ResultHandler 执行结果处理器

(6) cn.com.fishin.lighter.net.NioServerInitializer 服务器初始器

(7) cn.com.fishin.lighter.protocol.RequestParser 协议解析器

(8) cn.com.fishin.lighter.protocol.ProtocolParserKeeper 协议解析器拥有者

(9) cn.com.fishin.lighter.core.selector.NodeSelector 节点选择器

### 分为解析和执行两步

#### 解析：
1. 客户端通过网络通信传输协议内容
2. 协议解析器拥有者通过协议解析器解析出当前指令，通过发布事件来结束解析阶段

#### 执行：
1. 事件处理器接收到事件的发生，通过节点选择器选择要执行指令的节点，通过映射器选择要执行的方法出来
2. 将具体要执行的指令和参数交给节点处理

