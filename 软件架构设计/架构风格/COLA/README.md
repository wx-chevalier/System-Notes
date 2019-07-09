# COLA

COLA 是由阿里开源的业务架构风格，COLA 是 Clean Object-Oriented and Layered Architecture 的缩写，代表“整洁面向对象分层架构”，也叫“可乐”架构。在架构思想上，COLA 主张像六边形架构那样，使用端口-适配器去解耦技术细节；主张像洋葱圈架构那样，以领域为核心，并通过依赖倒置反转领域层的依赖方向。最终形成如下图所示的组件关系。

![](https://i.postimg.cc/9QPNbzm9/image.png)

换一个视角，从 COLA 应用处理响应一个请求的过程来看。COLA 使用了 CQRS 来分离命令和查询的职责，使用扩展点和元数据来提升应用的扩展性。整个处理流程如下图所示：
