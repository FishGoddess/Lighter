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
    
4. remove 动作，删除一个对象:
    + 例子：http://127.0.0.1:9669/remove/testKey
    + testKey 为对象的 key 值
    + **建议使用 `DELETE 请求`**
    + 返回删除的这个对象
    
5. exists 动作，判断一个对象 key 是否存在:
    + 例子：http://127.0.0.1:9669/exists/testKey
    + testKey 为对象的 key 值
    + **建议使用 `GET 请求`**
    + 返回 true 如果这个 key 存在
    
6. lighter 动作，获取 Lighter 服务运行的信息
    + 例子：http://127.0.0.1:9669/lighter/info
    + 目前支持 keys、values、numberOfKeys、info 等动作
    + info 为系统运行所有信息，包含上面几个系统动作的功能
    + **建议使用 `GET 请求`**
    + 返回系统运行信息
    ```json
    {
      "code": 0,
      "data": [
        {
          "keys": {
            "node_15": [],
            "node_14": [],
            "node_0": [],
            "node_1": [],
            "node_2": [
              "tehjhs"
            ],
            "node_3": [
              "bsdf"
            ],
            "node_4": [],
            "node_5": [],
            "node_13": [
              "gdsgas"
            ],
            "node_6": [
              "%E5%B9%BF%E6%B3%9B%E7%9A%84%E5%99%B6"
            ],
            "node_12": [
              "tyret"
            ],
            "node_7": [],
            "node_11": [
              "gdsa"
            ],
            "node_8": [],
            "node_10": [],
            "node_9": [
              "ghfdhfd"
            ]
          },
          "values": {
            "node_15": [],
            "node_14": [],
            "node_0": [],
            "node_1": [],
            "node_2": [
              "{  \"key1\": \"value1\",  \"key2\": \"value2\"}"
            ],
            "node_3": [
              "{  \"key1\": \"value1\",  \"key2\": \"value2\"}"
            ],
            "node_4": [],
            "node_5": [],
            "node_13": [
              "{  \"gsda\": \"gsdfga\",  \"风格的yrtruhjm放大\": \"gdfgad灌水广\"}"
            ],
            "node_6": [
              "{  \"key1\": \"value1\",  \"key2\": \"value2\"}"
            ],
            "node_12": [
              "{  \"key1\": \"value1\",  \"key2\": \"value2\"}"
            ],
            "node_7": [],
            "node_11": [
              "{  \"key1\": \"value1\",  \"风格的放大\": \"gdfgad灌水广\"}"
            ],
            "node_8": [],
            "node_10": [],
            "node_9": [
              "{  \"key1\": \"value1\",  \"key2\": \"value2\"}"
            ]
          },
          "numberOfKeys": {
            "total": 7,
            "details": {
              "node_15": 0,
              "node_14": 0,
              "node_0": 0,
              "node_1": 0,
              "node_2": 1,
              "node_3": 1,
              "node_4": 0,
              "node_5": 0,
              "node_13": 1,
              "node_6": 1,
              "node_12": 1,
              "node_7": 0,
              "node_11": 1,
              "node_8": 0,
              "node_10": 0,
              "node_9": 1
            }
          },
          "numberOfNodes": 16
        }
      ],
      "msg": "Done",
      "success": true
    }
    ```


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
    