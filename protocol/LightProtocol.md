# Lighter 服务器 Light 协议

### 注意：下面的所有指令仅仅针对于默认的节点实现类！！
### 例子：
#### 1. save 指令：
保存数据，类似于 java.util.Map 中的 put。

需要两个参数，一个是 key 值，一个是 value 值
```text
save\r\n
test\r\n
value\r\n
```

#### 2. fetch 指令：
获取数据，类似于 java.util.Map 中的 get

需要一个参数，key 值
```text
fetch\r\n
ndfbndf5435\r\n
```

#### 3. keys 指令：
获取当前服务器上所有的 key 键值，类似于 java.util.Map 中的 keys

不需要参数
```text
keys\r\n
\r\n
```

#### 4. size 指令：
获取当前服务器上的 key 个数，类似于 java.util.Map 中的 size

不需要参数
```text
size\r\n
\r\n
```

#### 5. exists 指令：
是否存在某个 key 键值，类似于 java.util.Map 中的 exists

需要一个参数，key 值
```text
exists\r\n
test\r\n
```

#### 6. remove 指令：
移除某个数据，类似于 java.util.Map 中的 remove

需要一个参数，key 值
```text
remove\r\n
test\r\n
```

#### 7. removeAll 指令：
移除所有数据，类似于 java.util.Map 中的 removeAll

不需要参数
```text
removeAll\r\n
\r\n
```
