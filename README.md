### 账户
```
@bobcbui:ooqn.com -> ws://ooqn.com/bobcbui
@bobcbui:user:ooqn.com -> ws://ooqn.com/user/bobcbui
@bobcbui:api:ooqn.com -> ws://ooqn.com/api/bobcbui
```

### 获取信息
#### request:
```
{
    messageId:'121111',
    code:"1000"
}
```
#### response：
```
{
    messageId:'121111',
    nickname:"bobcbui",
    picurl:"http://ooqn.com/user",
    info:[
        {
            title:'mail',
            value:'bobcbui@qq.com'
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
### 申请添加（好友/群/频道等）
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    pass:"test"
    code:'1001'
}
```
#### response:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    resultCode:'1'
}
```
#### 参数介绍：
// resultCode 1：通过，2：等待验证，3：验证码不正确

### 通知对方删除关系
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    code:'1005'
}
```
#### response:
```
{
    messageId:'',
    code:'1005'
}
```

### 发送消息
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    code:'2001',
    text: base64(context)
}
```
#### response:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
}
```
### 撤回消息
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    code:'2001',
    text: base64(context)
}
```
#### response:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
}
```

### 发起语音通话消息
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    code:'2001',
    text: base64(context)
}
```
#### response:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
}
```
### 发起视频通话消息
#### request:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
    code:'2001',
    text: base64(context)
}
```
#### response:
```
{
    messageId:'aksjdfkajkjdkfjkdjf',
}
```
