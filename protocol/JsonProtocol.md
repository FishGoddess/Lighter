# Lighter 服务器 Json 协议

### 例子：
#### 1. save 指令：
需要两个参数，一个是 key 值，一个是 value 值
```json
{
  "instruction":"save",
  "allArgs":[
    "test", 
    "value"
  ]
}
```

#### 2. fetch 指令：
需要一个参数，key 值
```json
{"instruction":"fetch","allArgs":["ndfbndf5435"]}
```

#### 3. keys 指令
不需要参数
```json
{"instruction":"keys","allArgs":[]}
```

#### 4. size 指令
不需要参数
```json
{"instruction":"size","allArgs":[]}
```

#### 5. exists 指令
需要一个参数，key 值
```json
{"instruction":"exists","allArgs":["test"]}
```

#### 6. remove 指令
需要一个参数，key 值
```json
{"instruction":"remove","allArgs":["test"]}
```

#### 7. removeAll 指令
不需要参数
```json
{"instruction":"removeAll","allArgs":[]}
```
