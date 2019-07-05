# 领域驱动设计

DDD 领域驱动设计，起源于 2004 年著名建模专家 Eric Evans 发表的他最具影响力的著名书籍：Domain-Driven Design – Tackling Complexity in the Heart of Software，Eric Evans 在该书中只是提供了一套原始理论，并没有提供一套方法论，因此多年来对于 DDD 也是见仁见智。

Domain-Driven Design will teach you how to model the real world in your application and use OOP to encapsulate the business logic of the organization.

The process of software development is complicated. When we face problems, we usually try to tackle the complexity by turning it into more understandable and manageable pieces.

Domain-Driven Design is a software development methodology for tackling complex software projects to deliver an end-product that meets the goals of the organization. In fact, Domain-Driven Design promotes focusing the project on an evolving core model.

It will teach you how to effectively model the real world in your application and use OOP to encapsulate the business logic of the organization.

通过领域模型和 DDD 的分层思想，屏蔽外部变化对领域逻辑的影响，确保交付的软件产品是边界清晰的微服务，而不是内部边界依然混乱的小单体。在需求和设计变化时，可以轻松的完成微服务的开发、拆分和组合，确保微服务不易受外部变化的影响，并稳定运行。

# 设计理念

在域驱动设计中，语言是最重要的因素。您希望拥有的是在代码中明确表达的问题域。从本质上讲，作为开发人员，您希望使用与您的业务相同的语言。DDD 如同微服务一样，理论与价值观的引导优于具体的落地框架，所谓领域建模的设计核心包含了两个：

- 统一语言，软件的开发人员/使用人员都使用同一套语言，即对某个概念，名词的认知是统一的。
- 面向领域，以领域去思考问题，而不是模块。

为了实现这两个核心，需要一个关键的角色，领域专家。他负责问题域，和问题解决域，他应该通晓研发的这个产品需要解决哪些问题，专业术语，关联关系。而在目前敏捷开发的结构下，流水线上的各个角色往往会专注于自己负责的环节，精细化的分工也限制了每个角色的全局视角；虽然我们经常提倡所谓的主人翁意识，但是在落地时又很难去推进。

如果微服务一样，DDD 也并非银弹，我们实践 DDD 的根本目的也是为了更快、更好地满足业务不断迭代的需求，并且能够保证团队的延续性与表述一致性，譬如对于问题域中相同变量、指标的定义是一致的。

# 分层架构

- 展现层  

展现层负责向用户显示信息和解释用户指令。


