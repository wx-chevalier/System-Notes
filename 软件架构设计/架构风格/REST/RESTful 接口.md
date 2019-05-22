[![返回目录](https://parg.co/Udx)](https://parg.co/UdT)

# RESTful 接口

# HTTP Methods

## 幂等性

# State Management | 状态管理

![](http://img1.tuicool.com/jAnqmmj.jpg!web)

状态管理即维持一个页面状态或者控制的状态。HTTP 协议本身是一个无状态的协议，因此状态管理就是用来维持一系列的来自客户端的请求状态的机制。REST 服务本身提供了两个基础的状态类型：

- Active : 当服务被调用或者执行的时候即进入 Active 状态
- Passive : 当服务没有被使用是即进入 Passive 状态

## Active

## Stateful

这种状态下，服务处于激活状态，并且能够处理连续的任务。如果服务处于 Stateful 状态下，它可以处理三个类型的数据：Session 、 Context 与 Business。

### Session

Session data represents information associated with retaining a connection made between a program and its client program.Sessions are identified by a unique identifier that can be read by using the SessionID property.A session is considered active as long as requests continue to be made with the same SessionID value. If the time between requests for a particular session exceeds the specified time-out value in minutes, the session is considered expired. Requests made with an expired SessionID value result in a new session.

### Context

When the service is active,stateful and executing the main task of the service.If the logic is tied with workflow, then it is further divided to context data and context rules. Context rules are the rules for executing workflow.

### Business

When the service is active and stateful,the service can execute a business task by executing multiple services.

## Stateless

这种状态下，服务处于激活状态，但是没有在处理一个连贯的任务。

[1]: http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm
[2]: http://www.restapitutorial.com/lessons/whatisrest.html#
