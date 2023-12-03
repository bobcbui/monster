
Monster 聊天协议


### 账户
```
@bobcbui:ooqn.com -> ws://ooqn.com/bobcbui
@bobcbui:user:ooqn.com -> ws://ooqn.com/user/bobcbui
@bobcbui:api:ooqn.com -> ws://ooqn.com/api/bobcbui
@bobcbui:api:232.222.123:8080 -> ws://232.222.123:8080/api/bobcbui
```

### 获取信息
#### request:
```json
{
    messageId:"121111",
    code:"1000"
}
```
#### response：
```json
{
    messageId:"121111",
    nickname:"bobcbui",
    picurl:"http://ooqn.com/user",
    accountType:"1",
    statusCode:"100",
    info:[
        {
            title:"mail",
            value:"bobcbui@qq.com"
        },
        {
            title:"language",
            value:"chinese"
        },
        {
            title:"region"
            value:"beijing"
        },
        {
            title:"synopsis",
            value:"i am a human"
        }
    ]
}
```
#### 参数介绍：
accountType 1好友，2群，3频道
statusCode: 100，200，300，400，500
info：作者对你公开的信息


### 申请添加（好友/群/频道等）
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    pass:"9994454"
    code:"1001"
}
```
#### response:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    statusCode:"100",
    type:"1"
}
```
#### 参数介绍：
// type 1：通过，2：等待验证，3：验证码不正确

### 通知对方删除关系
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    code:"1004"
}
```
#### response:
```json
{
    messageId:"",
    statusCode:"200"
}
```
#### 参数介绍：
// code 1004 通知对方删除关系

### 发送消息
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    code:"2001",
    text: base64(context)
}
```
#### response:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    statusCode:"200",
}
```
### 撤回消息
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    code:"2004",
    recallMessageId: "12312312"
}
```
#### response:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    statusCode:"200"
}
```

### 发起语音通话消息
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    code:"2002",
    text: base64(context)
}
```
#### response:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    statusCode:"200",
}
```
### 发起视频通话消息
#### request:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    code:"2003",
    text: base64(context)
}
```
#### response:
```json
{
    messageId:"aksjdfkajkjdkfjkdjf",
    statusCode:"200",
}
```
