# cleanApp
 use for try to apply <a href="https://five.agency/android-architecture-part-3-applying-clean-architecture-android/">clean  architecture </a>
 
## 项目介绍
整体采用Clean架构，实现的简易聊天室demo，可使用c socket/java socket 作为载体 来实现同一个聊天室内用户 发送文件聊天消息功能。 ui采用mvvm，数据库使用Room,内存中的键值对存储采用datastore.

## 模块介绍
各个模块的接口实现通过 在ApiService中进行依赖注入
整体分为 4个大模块，分别是 app主模块，domain模块，datalib模块，platformrelated模块。
各个模块个功能如下：
app 主模块：负责ui
domain 模块:负责业务逻辑 以及 其他模块的接口定义
datalib 模块：负责 数据库/网络 的基础功能
platformrelated 模块: 负责 除了 数据库/网络外 的与 Android平台相关的 功能定义


## 模块详情介绍
- app模块；
  CleanApplication 实现了IGlobalContextProvider 接口，用于向外提供全局的的一个Context, 依赖注入也在 CleanApplication onCreate中进行。
  其他的类就是 正常的 MVVM的ui以及数据传递
- platformrelated 模块，ToastHelper 实现 IToast接口 以展示toast， RealExecutor 实现 Executor接口 作为线程切换的工具类
- datalib 模块 
  - database包 使用Room orm那一套实现
  - memostore包 DataStore的使用
  - socket包  tcp链接
- domain模块
  - base/common 定义了 通用工具/接口
  - db/device/memostore/socket 定义了其他层的接口
    - 其中 socket包下的 ILogicAction 是对 socket的业务逻辑的抽象， ISocket是对 tcp链接的操作的抽象
  - logic 业务逻辑模块
    - chat 聊天室相关的 逻辑
  - ApiService /Api  用于依赖注入和管理

## tcp链接的业务逻辑
1. 在tcp链接建立成功后 ，会发送bindUser 用于绑定 uid和对应的 tcp链接，也会定时发送 心跳包
2. tcp发送的数据结构是json string
3. 发送的数据结构 由 RawDataStruct 定义
4. 收到消息后 会由 MainRouter 根据 消息类型（opType) 分发到不同 ISocketMsgDealer。
