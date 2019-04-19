# Lighter 服务器 Json 协议

### 例子：
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

### 测试 POST 存储表单数据
POST http://{{host}}/testKey
Lighter-Expire-Time: 3600
Content-Type: application/x-www-form-urlencoded

key1=value1&
key2=哈哈哈

###
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
    