# 领域驱动设计

DDD 领域驱动设计，起源于 2004 年著名建模专家 Eric Evans 发表的他最具影响力的著名书籍：《Domain-Driven Design – Tackling Complexity in the Heart of Software》，Eric Evans 在该书中只是提供了一套原始理论，并没有提供一套方法论，因此多年来对于 DDD 也是见仁见智。更早些时候 MartinFowler 曾经提出贫血模型与充血模型的概念，他认为我们大多数系统以 POJO 作为模型，只有普通的 getter、setter 方法，没有真正的行为，好像缺少血液的人，在 Evans 看来，DDD 中模型都是以充血形式存在，也就是说在 DDD 中，我们设计的模型不仅包含描述业务属性，还要包含能够描述动作的方法，不同的是，领域中一些概念不能用在模型对象，如仓储、工厂、服务等，如强加于模型中，将破坏模型的定义。

软件开发过程很复杂，当我们遇到问题时，我们通常会尝试通过将其变为更易理解和易于管理的部分来解决复杂问题。领域驱动设计即是一种用于处理复杂的软件项目的开发方法，将教您如何在应用程序中建模现实世界，并使用 OOP 封装组织的业务逻辑，以提供满足组织目标的最终产品。事实上，领域驱动设计促进了项目将重点放在不断发展的核心模型上。

DDD 的实践并非一蹴而就，往往都会先是套概念，在代码中使用 Aggregation Root，Bonded Context，Repository 等等这些概念；也会使用一定的分层策略，然而这种做法一般对复杂度的治理并没有多大作用。在经过大量的实践之后，就可以融会贯通，理解 DDD 的本质是统一语言、边界划分和面向对象分析的方法。

![](https://i.postimg.cc/hvm36Vvk/image.png)

# 设计理念

领域驱动设计的战略核心即是将问题域与应用架构相剥离，将业务语义显现化，把原先晦涩难懂的业务算法逻辑，通过领域对象（Domain Object），统一语言（Ubiquitous Language）转化为领域概念清晰的显性化表达出来。

- 统一语言，软件的开发人员/使用人员都使用同一套语言，即对某个概念，名词的认知是统一的，建立清晰的业务模型，形成统一的业务语义。将模型作为语言的支柱。确保团队在内部的所有交流中，代码中，画图，写东西，特别是讲话的时候都要使用这种语言。例如账号，转账，透支策略，这些都是非常重要的领域概念，如果这些命名都和我们日常讨论以及 PRD 中的描述保持一致，将会极大提升代码的可读性，减少认知成本。。比如不再会有人在会议中对“工单”、“审核单”、“表单”而反复确认含义了，DDD 的模型建立不会被 DB 所绑架。

- 面向领域，业务语义显性化，以领域去思考问题，而不是模块。将隐式的业务逻辑从一推 if-else 里面抽取出来，用通用语言去命名、去写代码、去扩展，让其变成显示概念；很多重要的业务概念，按照事务脚本的写法，其含义完全淹没在代码逻辑中没有突显出来。

- 职责划分，根据实际业务合理划分模型，模型之间依赖结构和边界更加清晰，避免了混乱的依赖关系，进而增加可读性、可维护性；单一职责，模型只关注自身的本职工作，避免“越权”而导致混乱的调用关系。通过建模，更好的表达现实世界中的复杂业务，随着时间的发展，不断增加系统对实际业务的沉淀，也将更好的通过清晰的代码描述业务逻辑，模型的内聚增加了系统的高度模块化，提升代码的可重用性，对比传统三层模式中，很有可能大量重复的功能散落在各个 Service 内部。

接触到需求第一步就是考虑领域模型，而不是将其切割成数据和行为，然后数据用数据库实现，行为使用服务实现，最后造成需求的首肢分离。以银行账号 Account 为案例，Account 有“存款”，“计算利息”和“取款”等业务行为，但是传统经典的方式是将“存款”，“计算利息”和“取款”行为放在账号的服务 AccountService 中，而不是放在 Account 对象本身之中。DDD 让你首先考虑的是业务语言，而不是数据。DDD 强调业务抽象和面向对象编程，而不是过程式业务逻辑实现。通过领域模型和 DDD 的分层思想，屏蔽外部变化对领域逻辑的影响，确保交付的软件产品是边界清晰的微服务，而不是内部边界依然混乱的小单体。在需求和设计变化时，可以轻松的完成微服务的开发、拆分和组合，确保微服务不易受外部变化的影响，并稳定运行。

DDD 的落地实现中，我们首先可以将领域设计模式分解到问题空间（Problem Space）与解空间（Solution Space）两大类中，问题空间的划分更多着眼于业务上值得注意的、重要的点，而解空间则更关注与应用的组织架构，保证应用本身更易于管理。这里领域专家会起到关键的作用，他应该通晓研发的这个产品需要解决哪些问题，专业术语，关联关系。

![](https://i.postimg.cc/J0SgsLHy/image.png)

![](https://i.postimg.cc/QNzv4pYX/image.png)

## 设计原则

- Focusing on the Core Domain: DDD 强调需要将最多精力集中在核心子域上，核心子域是产品成败的关键，它的是产品的独特卖点，是它被建造而不是购买的原因。核心领域，将为您提供竞争优势，并为您的产品创造真正的商业价值，所有团队都了解核心领域是至关重要的。

- Learning through Collaboration: DDD stresses the importance of collaboration between the development teams and business experts to produce useful models to solve problems. Without this collaboration and commitment from the business experts, much of the knowledge sharing will not be able to take place, and development teams will not gain deeper insights into the problem domain. It is also true that, through collaboration and knowledge crunching, the business has the opportunity to learn much more about its domain.

- Creating Models through Exploration and Experimentation: DDD treats the analysis and code models as one. This means that the technical code model is bound to the analysis model through the shared UL. A breakthrough in the analysis model results in a change to the code model. A refactoring in the code model that reveals deeper insight is again reflected in the analysis model and mental models of the business. Breakthroughs only occur when teams are given time to explore a model and experiment with its design. Spending time prototyping and experimenting can go a long way in helping you shape a better design. It can also reveal what a poor design looks like. Eric Evans suggests that for every good design there must be at least three bad ones, this will prevent teams stopping at the first useful model.

- Communication: The ability to effectively describe a model built to represent a problem domain is the foundation of DDD. This is why, without a doubt, the single most important facet of DDD is the creation of the UL. Without a shared language, collaboration between the business and development teams to solve problems would not be effective. Analysis and mental models produced in knowledge‐crunching sessions between the teams need a shared language to bind them to a technical implementation. Without an effective way to communicate ideas and solutions within a problem domain, design breakthroughs cannot occur.

- Understanding the Applicability of a Model: Each model that is built is understood within the context of its subdomain and described using the UL. However, in many large models, there can be ambiguity within the UL, with different parts of an organization having different understandings of a common term or concept. DDD addresses this by ensuring that each model has its own UL that is valid only in a certain context. Each context defines a linguistic boundary; ensuring models are understood in a specific context to avoid ambiguity in language. Therefore a model with overlapping terms is divided into two models, each clearly defined within its own context. On the implementation side, strategic patterns can enforce these linguistic boundaries to enable models to evolve in isolation. These strategic patterns result in organized code that is able to support change and rewriting.

- Constantly Evolving the Model: Any developer working on a complex system can write good code and maintain it for a short while. However, without synergy between the source code and the problem domain, continued development will likely end up in a codebase that is hard to modify, resulting in a BBoM. DDD helps with this issue by placing emphasis on the team to continually look at how useful the model is for the current problem. It challenges the team to evolve and simplify complex models of domains as and when it gains domain insights. DDD is still no silver bullet and requires dedication and constant knowledge crunching to produce software that is maintainable for years and not just months. New business cases may break a previously useful model, or may necessitate changes to make new or existing concepts more explicit.

## 概念澄清

- DDD 不是一个框架：

- DDD 并非银弹：我们实践 DDD 的根本目的也是为了更快、更好地满足业务不断迭代的需求，并且能够保证团队的延续性与表述一致性，譬如对于问题域中相同变量、指标的定义是一致的。DDD 不适合多数为 CRUD 单表操作的简单业务场景，在该场景下往往导致增加系统复杂度。同样不适合基于老系统代码之上进行的优化重构，因为这样从传统分层模式到基于聚合的设计是完全颠覆性的，论改造成本不如重新开发一套新应用。
