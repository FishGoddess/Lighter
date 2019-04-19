# Lighter 轻量级对象缓存服务中间件 [![License](./license.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
(目前主分支是 rebuild，如果在代码设计和风格统一上存在问题，请在评论区留言)

### 1. 使用用途：
主要用作于对象缓存，带有默认的缓存实现，并且允许多个节点的缓存，这也实现了负载均衡和分布式缓存。

使用 Tuz 做容器托管，并利用它的 IOC 技术和拦截器技术做业务解耦。

在网络通信方面使用的是 Netty，并且实现了 HTTP 下的 JSON 格式传输协议。

整个项目的所有组件都允许自定义，包括缓存实现、节点选择、协议解析和网络通信协议，你只需要非常简单的实现一个接口即可。

+ #### 留言网站：[https://www.fishin.com.cn](https://www.fishin.com.cn)

+ #### 联系方式：fishinlove@163.com

+ #### 开源协议：[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

### 2. 使用步骤：
服务中带有一个启动脚本：cn.com.fishin.lighter.Startup，可以加一个启动参数，就是配置文件的路径，

如果不指定路径，默认加载目录下的配置文件 config/config.properties，

启动 cn.com.fishin.lighter.Startup 即可启动项目，默认占用 9669 和 9999 两个端口，

如果需要关闭服务，启动 cn.com.fishin.lighter.Shutdown 脚本即可。

### 例子：
HTTP 协议请求，符合 Restful 风格，返回 json 对象，前端可以直接使用
```json
{
  "development": {
    "host": "localhost:9669",
    "closeHost": "localhost:9999"
  }
}
```
```http request
### 测试关闭服务器
GET http://{{closeHost}}

### 测试 GET 获取数据
GET http://{{host}}/testKey

### 测试 POST 存储数据
POST http://{{host}}/testKey
Lighter-Expire-Time: 3600
Content-Type: application/json;charset=utf-8

{
  "key1": "value1",
  "key2": "value2",
  "key3": "value3",
  "key4": "value4"
}

### 测试 DELETE 删除数据
DELETE http://{{host}}/testKey

### 测试 PUT 修改数据
PUT http://{{host}}/testKey
Lighter-Expire-Time: 24
Content-Type: application/json;charset=utf-8

{
  "key1": "value1",
  "key2": "value2"
}
```

这个协议目前实现的是 HTTP 请求，符合 Restful 的风格：
1. GET 请求，获取一个对象:
    + 例子：http://127.0.0.1:9669/testKey
    + testKey 为对象的 key 值
    + 注意：返回值是一个 json 对象数组
    
2. POST 请求，保存一个对象:
    + 例子：http://127.0.0.1:9669/testKey
    + testKey 为对象的 key 值
    + 如果 key 值已经存在则直接返回
    + 建议使用 Content-Type: application/json;charset=utf-8
    ```json
    {
        "key1": "value1",
        "key2": "value2",
        "key3": "value3",
        "key4": "value4"
    }
    ```

3. PUT 请求，修改一个对象:
    + 例子：http://127.0.0.1:9669/testKey
    + testKey 为对象的 key 值
    + 如果 key 值不存在则直接插入
    + 建议使用 Content-Type: application/json;charset=utf-8
    ```json
    {
        "key1": "value1",
        "key2": "value2",
        "key3": "value3"
    }
    ```
    
4. DELETE 请求，删除一个对象:
    + 例子：http://127.0.0.1:9669/testKey
    + testKey 为对象的 key 值
    + 返回删除的这个对象


### 3. 使用到的依赖：
```xml
<properties>
    <tuz.version>0.6.6-FINAL</tuz.version>
    <netty.version>4.1.33.Final</netty.version>
    <fastjson.version>1.2.56</fastjson.version>
    <logback.version>1.2.3</logback.version>
</properties>
```
#### i. Tuz 作为整个项目的容器，使用到了它的 IOC 技术和拦截器技术 
#### ii. Netty 作为整个项目的网络通信模块，使用到了它的 NIO 网络通信服务
#### iii. fastJson 作为协议解析器的一部分，是 Json 协议的解析库
#### iiii. logBack 作为项目的日志模块，使用 SLF4J 接口对接

### 4. 项目架构：
+ 性能测试
    + 计算机硬件：
    
    ![firstTest.png](./images/pc.png)
    + 单次操作，2000 个线程，10 万个请求，吞吐量：平均 11000 次/秒
    
    ![firstTest.png](./images/SecondTest.png)
        
    ![firstTest.png](./images/ThirdTest_60s.png)
            
    + 混合操作，四种基本操作同时进行，4000 个线程，10 万个请求，平均 8400 次/秒
    
    ![firstTest.png](./images/FourthTest_60s_mixed_request.png)
            
具体测试文件在 images 下，为 Lighter.jmx，是一个 JMeter 的测试计划

+ 具体配置文件详见 resources/config.properties
```properties
#############################################################
# Lighter 服务配置文件 v1.2.2
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
# 同时，由于线程之间切换会有开销，所以如果使用的是内存型的节点实现，请保持线程数在 CPU 线程数的两倍以内
# 如果节点实现是 I/O 或者网络型的，可以适当调高比例，比如调节成 CPU 线程数的四倍甚至更高
corePoolSize=16
maximumPoolSize=64
# 线程存活时间的设置和一个任务执行的时间应该差不多，由于这个任务一般是短时间任务
# 所以这个值不建议设置的太大，应该要设置为任务执行时间的 2 倍左右，这样可以复用大量线程
keepAliveTime=10
# 等待队列大小和队列拒绝策略一般会有关联，当队列满了就会开始新增线程，当最大线程数达到了
# 这个线程池就会执行拒绝策略，由于 Lighter 是一个对象缓存服务，因此是允许缓存不命中的情况的
# 但是这个值不应该设置得太小，以免造成缓存雪崩和缓存穿透，需要结合系统性能来决定大小
waitQueueSize=512
RejectedExecutionHandler=cn.com.fishin.lighter.core.DefaultRejectedExecutionHandler

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
# 由于线程数肯定是根据业务量来定的，所以这里取线程数的两倍作为初始值，在代码中可以看到这一行：
# private static final int INITIAL_CAPACITY = (Integer.valueOf(Tuz.use("corePoolSize")) << 2);
Node=cn.com.fishin.lighter.core.node.DefaultNode

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

(1) cn.com.fishin.lighter.core.node.Node 节点接口类

(2) cn.com.fishin.lighter.core.selector.NodeSelector 节点选择器

(3) cn.com.fishin.lighter.core.executor.TaskExecutor 任务执行器

(4) cn.com.fishin.lighter.protocol.RequestParser 协议解析器

(5) cn.com.fishin.lighter.protocol.RequestHandler 辅助协议解析器

### 分为解析和执行两步

#### 解析：
1. 客户端通过网络通信传输协议内容
2. 协议解析器将协议解析为一个任务对象，交给任务执行器执行

#### 执行：
1. 任务执行器将任务交给线程池执行，这个任务将被封装成一个 FutureTask 执行
2. 节点管理器将任务分配到具体的一个节点或者一些节点执行，得到结果返回

