# Lighter 服务器 HTTP 协议

这个协议目前实现的是 HTTP 请求，建议使用 Restful 的风格发送请求：
1. get 动作，获取一个对象:
    + 例子：http://127.0.0.1:9669/get/testKey
    + testKey 为对象的 key 值
    + **建议使用 `GET 请求`**
    + 注意：返回值是一个 json 对象数组
    
2. set 动作，保存一个对象:
    + 例子：http://127.0.0.1:9669/set/testKey
    + testKey 为对象的 key 值
    + **建议使用 `POST 请求`**
    + **建议使用 Content-Type: application/json;charset=utf-8**
    + 注意：返回值是一个 json 对象数组
    
    请求体：
    ```json
    {
        "key1": "value1",
        "key2": "value2",
        "key3": "value3",
        "key4": "value4"
    }
    ```
    或者是：
    ```text
    任意字符串
    ```

3. setAbsent 动作，如果对象键值不存在才保存:
    + 例子：http://127.0.0.1:9669/setAbsent/testKey
    + testKey 为对象的 key 值
    + 如果 key 值不存在才保存
    + **建议使用 `POST 请求`**
    + **建议使用 Content-Type: application/json;charset=utf-8**
    + 注意：返回值是一个 json 对象数组
        
    请求体：
    ```json
    {
       "key1": "value1",
       "key2": "value2",
       "key3": "value3",
       "key4": "value4"
    }
    ```
    或者是：
    ```text
    任意字符串
    ```
    
4. DELETE 请求，删除一个对象:
    + 例子：http://127.0.0.1:9669/remove/testKey
    + testKey 为对象的 key 值
    + **建议使用 `DELETE 请求`**
    + 返回删除的这个对象


### 例子：
```http request
### 使用 idea 的 rest_client_editor 发送请求
### 测试关闭服务器
GET http://{{closeHost}}

### 获取数据
GET http://{{host}}/get/testKey

### 保存数据
POST http://{{host}}/set/testKey
Lighter-Expire-Time: 3600
Content-Type: application/json;charset=utf-8

{
  "key1": "value1",
  "key2": "value2",
  "key3": "value3",
  "key4": "value4"
}

### 如果不存在才插入
POST http://{{host}}/setAbsent/testKey
Lighter-Expire-Time: 24
Content-Type: application/json;charset=utf-8

{
  "key1": "value1",
  "key2": "value2"
}

### 测试 DELETE 删除数据
DELETE http://{{host}}/remove/testKey

### 测试 POST 存储表单数据
POST http://{{host}}/set/testKey2
Lighter-Expire-Time: 3600
Content-Type: application/x-www-form-urlencoded

key1=value1&
key2=哈哈哈

### 测试 POST 存储二进制数据
POST http://{{host}}/set/testKey3
Lighter-Expire-Time: 16
Content-Type: multipart/form-data;boundary=LighterBoundary

--LighterBoundary
Content-Disposition: form-data; name="field-name"

key-value
--LighterBoundary
Content-Disposition: form-data; name="field-name"; file-name="无标题.png"

< Z:/无标题.png
--LighterBoundary--

###
```
    