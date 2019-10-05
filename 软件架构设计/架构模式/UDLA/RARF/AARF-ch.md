[TOC]

# Introduction

> **AARF 是适用于现代复杂多变的业务模型，同时支持异步编程与分布式扩展的架构风格，Inspired By REST,Flux And Functional Reactive Programming。**

AARF 的全称为 Asynchronous Abstract Resource Flow，是属于对于 REST 架构风格的扩充与完善。AARF 提出了完全面向资源的请求处理、逻辑分层(Layer System)以及全局统一的命名规范。AARF 的核心理念来源于笔者多年的前后端综合实践，不过笔者也不能确定这一套想法是否具有真的实践意义与价值，还是仅仅只是沉渣泛起抑或是简单的脑残。

AARF 不仅仅是一套具体实现方式，更重要的是一种架构风格的设计理念，它可以灵活的运用于包括 Java、PHP、NodeJS、Go 乃至于 Swift、Rust 这样的新兴服务端编程语言上。笔者也会不断地完善更新，也欢迎大家提出自己的宝贵意见。

AARF 在设计理念上很类似于现在流行的微服务模式(MicroService Pattern)，笔者会在一个单独的章节进行对比。不过概而论之，微服务是从功能的角度，以原子服务组合的方式构建大型复杂长期运行的应用程序。而 AARF 是从业务逻辑的角度，以抽象资源的原子处理进行组合的方式构建灵活可变稳定的应用程序。

MVC 是 。 笔者没有经历过大量的大型项目，大部分经历的是中小型需要快速迭代乃至于整个需求全部推翻重来的项目。在实战过程中，深刻感受到目前 MVC 粗粒度下

。很多的看上去很傻逼的错误，我们可能寄希望于程序员开发时候的标准的规范。简单来说，我们可能在 MySQL 中存放一条 JSON 字符串，在返回给前端时需要先进行 JSON 解码转化为 JSONArray 再和其他属性统一编码。这个问题看上去只需要规定程序猿在每个需要返回该属性的接口处进行编码即可，不过事实证明很多莫名奇妙的错误就是来自于这里。当然，通过数据模型或者统一的返回校验，譬如 JSON Schema 校验，我们肯定可以避免这个问题。上文提及的两个小例子，说明我们需要一个灵活但是有一定规范约束，这个规范约束应该像 REST 一样简单易懂并且容易实行。

MVC 架构的 Layer System 是在 Controller、Model、Service 这样进行分层，而 AARF 是纵向切分。在 Controller 层，或者是请求映射层，AARF 并不是将每个请求映射到一个专门的处理函数。而在 Model 层，AARF 也不建议按照每一个具体的业务逻辑编写 SQL 语句并向上提供，这样确实可以提供查询的效率，但是一旦业务需求发生变化之后，会出现大量的冗余的 SQL 语句以及对应的冗余代码。你会觉得自己的代码一团糟，就好像埋着无数的地雷。

为了方便理解，我们也可以借用函数柯里化与反柯里化的概念。我们提供一系列适用范围广，但是适用性较低的接口来代替原先适用性较高，但是适用范围极低的接口。而柯里化的这一步即交由前端来完成，也就是下文中会提及的 URFP 的概念。

AARF 中文文档地址：[AARF-CH](https://wx-chevalier.gitbooks.io/aarf-ch/content/)

AARF 英文文档地址：[AARF-EN](https://wx-chevalier.gitbooks.io/aarf-en/content/)

AARF 的 Java 实例地址：[AARF-Java](https://github.com/wx-chevalier/AARF-Java)

笔者的联系方式：QQ(384924552)，Mail(384924552@qq.com)

## Why AARF?(为什么是 AARF)

> AARF 代表着在后端开发中从面向逻辑到面向资源，从以业务/页面为驱动到以状态/数据为驱动的设计理念的变化。

目前流行的复合 REST 分层的系统的原则的架构即是 MVC，在传统的 J2EE 中一般分为 Controller-Service-Model，也是典型的面向逻辑的编程。首先一个逻辑会映射到一个 Controller，再由 Controller 中根据 URI 来组合搭配不同的 Service，而某个 Service 往往也是为了完成某个业务逻辑而特别存在的。不过 MVC 完全是一套非常粗粒度的规范，在实际的编程中，后台就是一个庞杂的数据提供方，连接前端与数据库。而后台的具体代码分割风格在 Controller 层次受业务请求约束，在 Model 层则受到 SQL 语句的约束。确实，特别是在 GET 类型请求的处理中，如果需要返回多个资源的综合结果，用连接查询或者子查询毫无疑问会比多次查询节省很多消耗。但是这种混杂的 SQL 又会将我们希望达到的资源分割的目的给毁掉，因此，AARF 中希望能够立足于规范的不同资源间的 SQL 的自动智能组合来解决这个问题。

在 AARF 中，不同的资源处理器之间的逻辑代码重合度会在 80%以上，换言之，为了不同业务逻辑而产生的硬编码的需求被尽可能地缩小，从而尽可能地避免需求变更带来的代码变化。毕竟在大型系统中一行代码的改变可能形成的多米诺骨牌效应是不可想象的。

另一方面，以 Spring 为代表的框架强调的是以 DI，也就是 IOC 的方式来解决这种依赖的可变性问题。但是它们在一个宏观的视角上还是面向逻辑编程。AARF 提供了不仅仅是一套类似于 Spring 的编程 Library，也提供了一套从前后到后的基于约定的命名规划与代码风格。需要注意的是，因为业务逻辑的复杂多变性，AARF 只是尽可能地在抽象与具体之间寻求一种平衡，具体的业务逻辑设计与代码架构还是需要根据实际的应用场景而言。而这种抽象毫无疑问在设计初期会影响到性能，譬如可能需要冗余的数据库操作等，这种性能的补偿会在后面提及。

在对大量复杂的业务后台的分析之后，发现如果我们深入到某个具体的业务流设计中，会发现不同的 API 之间具有相当复杂的且唯一的关系。而如果我们提取这些业务逻辑之间的共性，即会发现对于一些抽象资源的组合和处理方式是如此的类似。

AARF 继承并个性化的解释了 REST 的六大原则，同时自身的原则为：

> 这些所谓的原则还处于修订中，不建议用于生产环境
>
> PS：上面这句话只是单纯的为了装逼

* 约定优于配置，配置优于硬编码，这一条算是 AARF 的核心理念，一方面表现在前端可见的资源组合搭配上，另一方面表现在对于隐性

- 请求与响应的一致性。就好像 Redux 宣称的 Predictable State 一样，之前客户端是在请求之后对于返回数据进行
- 尽可能地资源隔离。不强求资源的完全隔离，也不支持把对于资源的处理混杂在一起。至少在 ResourceHandler 这一层，不建议有对于其他资源的操作。在 Model 层，可以对于确定性的关联操作。在 Relation 层，可以随便混杂资源的操作。

举个具体的例子，在我们的电商模型中，我们最常用的接口是返回商品(goods)信息，譬如：

```
api.com/goods
```

这会根据我们的需要自动地返回一些商品列表，如果我们需要根据不同品类、属性等进行查询，则只需要加一个查询参数等，譬如：

```
api.com/goods?requestData={category:1}&sort=rating
```

随着业务需求的进一步完善，我们现在需要返回用户的订单商品信息，这里我们假设有一个专门的订单表(goods_order)，如果是按照传统的朴素的方式，可以为：

```
api.com/getGoodsInOrderByUserId?user_id=123
```

而如果按照 rest 的风格，可以定义为：

```
api.com/goods_order?requestData={user_id:123}
```

注意，无论是哪种 URL 定义方式都会映射到某个处理函数，但是很明显的 REST 风格的接口(URI)可读性会更好。但是虽然我们请求的是订单，但实际上返回的却是一个商品列表(如果你认为商品列表与商品是属于同一种资源，那么请忽略这一部分)。请求者最终想要的是一个包含商品详情的列表，而不是单纯的订单列表。因此，在对应的处理函数中，我们需要根据 user_id 查询到商品订单列表，然后根据商品订单列表查询到商品详情。这三者之间是一对多(用户-商品订单)以及一对一的关系(商品订单-商品)。

当然，我们可以在该处理函数中用一个简单的 SQL 连接查询即可获得想要的数据，一切看上去是如此的简单。接下来我们的业务逻辑更加复杂，添加了购物车、收藏夹等等功能，同样的，我们需要获得购物车中的商品列表、收藏夹中的商品列表。我们会选择定义不同的 URI，譬如：

```
api.com/getGoodsInCartByUserId?user_id=123
api.com/getGoodsInFavoriteByUserId?user_id=123
```

当然，也可以用等效的 REST 风格的接口，不过它们都是同样映射到某个处理函数，然后很简单的用一个 SQL 语句解决。一切还是很简单很正常，但是墨菲定理就来了。有一天如果整个系统需要重构，商品表发生了翻天覆地的变化，或者某个原有的需求发生了些许的改变。好一点的情况是还是当时那个开发人员，他清晰的知道每一条代码，并且能精准的进行每一行的改动，最终通过回归测试。

不过真实情况却是，即使是自己也不乐意去看那一大堆好久不碰的代码，或者换了个新人，压根不愿意去看前辈的代码(这在小公司里非常常见，毕竟大家的代码风格都不一样)。可能一个函数的命名方式都会让你纠结半天，毕竟命名时是如此的随意。最终，导致整个系统中充满了冗余代码，就好像炸弹一样，谁也不知道这段代码是干啥的，但是也不敢删除。整个依赖变得非常混乱。直到有一天受不了了，花费大量的时间进行业务逻辑重构。当然，老板肯定会在你重构完成之前告诉你，需求又改了。你还能咬他咋的？

以上的方式就是面向业务逻辑的接口，我们有一个固定的 URL 或者 URI 代指某个业务逻辑的处理方式，并在该处理函数中调用所需的 Service 层或者 Model 层函数，完成整个业务逻辑处理过程。同时作为一个前端开发者，我们往往需要的是面向页面的接口。具体而言，前端开发者希望的最理想的情况是每个页面只需要发起一次请求即可以获得所需要的数据。譬如我们在电商系统中添加了所谓的主题活动，一个活动可能关联到多个商品。那么对于如下的这个界面，我们可能有两种构造方式：

```
api.com/getGoodsThemeAndGoodsList?goods_theme_id=1
```

当前端请求该接口时，就会返回如下格式的数据：

```
{
  "goods_theme":{goods_theme_detail}
  "goodss":[{goods_detail}]
}
```

绝大部分人都会把主题活动和商品当做两个存在一定关联的不同资源吧，不会认为这两货是同一个东西的吧？如果按照第一种方式，一旦设计同学修正了前端页面，譬如不展示商品列表详情了，或者后台的商品表发生变化，那么整个接口就要改或者添加新的接口。与接口相关联的 Service 或者 Model 层也会变动。就像上面所讲的，一大堆地雷正在靠近。

如果我们按照纯粹的资源的方式考虑呢，即完全的 REST 方式，首先我们获取主题活动的详情：

```
api.com/goods_theme/1
```

获得一系列的商品列表 ID，然后通过商品列表去获取详情，譬如：

```
api.com/goods/[1,2,3,4,5]
```

这种划分方式，可以达成逻辑上的明晰，不过前端估计会疯掉。并且用户体验会非常不好，同时也会耗费大量无谓的资源。

综上所述，我们需要是是一种抽象的，即独立于具体业务逻辑的资源划分与处理方式，这种方式能够灵活组合并且适应快速变化的前端界面与业务需求，最终在前端的请求次数与逻辑的划分之间达成较好的平衡。

MVC 架构功能之间分层比较明晰，但是逻辑间分层比较混乱，并且不同的功能点之间是所谓的双向数据流，如下图所示：

而 AARF 中明确划分了资源的边界，并且能够根据业务需求动态映射到不同的数据流现。

AARF 的适用领域是那种需要快速迭代的具有一定逻辑复杂度的服务后台搭建。对于大型项目，如果在项目起草时期就能够将需要的业务逻辑以及操作全部规划完善，那么也没必要使用 AARF 这种自找麻烦的架构风格。

个人感觉，AARF 会在代码自动生成领域有一定的领先优势。

### MicroServices(微服务)

近年来微服务概念的兴起也是为了进行这样的解耦合，只是微服务和 AARF 的类比可以有以下几点：

* 微服务和 AARF 在某种意义上都是推崇 SRP 原则，不过微服务是从传统的 SOA 架构衍变而来，而 AARF 是借鉴了以状态/数据为驱动的这样一种开发方式。
* 微服务更多的是在功能上的，面向于整体后台架构的解耦合。而 AARF 关注的是偏向于业务逻辑的组织方式。
* 微服务往往强调的是不同功能间的物理隔离，这是其与传统的巨石(Monolith)应用程序的一个很大的区别。而 AARF 依赖的是逻辑间的隔离，相对于功能领域会是一个更加抽象的概念。
* 微服务是去除了 ESB 的 SOA，即一种去中心化的分布式软件架构。而 AARF 面向每个具体业务而言的去 Controller 化的基于抽象资源流的架构风格。

AARF 与 MicroServices 的最终目标都是为了避免巨石应用程序的出现，最终形成一种分布式地灵活可拆卸的应用程序。某些方面来说，AARF 是实现 MicroServices 的一个手段。

实际上，现在也有很多微服务的设计模式上，在保证隔离性的基础上，以 Event-Driven 的方式来组合不同的服务并且管理这些分布式数据。

> [event-driven-data-management-microservices](https://www.nginx.com/blog/event-driven-data-management-microservices/)

![Each service in a microservice architecture maintains a private database table](https://www.nginx.com/wp-content/uploads/2015/12/Richardson-microservices-part5-separate-tables-e1449727641793.png)

![](https://www.nginx.com/wp-content/uploads/2015/12/Richardson-microservices-part5-credit-check-1-e1449727610972.png)

## REST & Software Architecture Style

在理想的情况下，每个开发者都能具有高度一致的编码风格与能力，并且对整个项目了如指掌，这样才能够快速地无冗余地根据业务需求来修改代码。并且辅助以大量的回归测试来保证代码的可用性，毕竟

Request per thread is an old beast and should be avoided as much as possible. It not only affects an application's scalability but can also lead to cascading failures.

> [Non-Blocking REST Services With Spring](https://dzone.com/articles/non-blocking-rest-services-with-spring)

## Flux & Redux

## Reactive Programming & Actor

在笔者的基于 Spring 框架的 AARF 的实践中，选用了 RxJava 作为 FRP 的实现库。不过实际上每个 ResourceHandler 非常类似于 Actor

Reactive programming is an emerging discipline which combines concurrency and event-based and asynchronous systems.Reactive programming can be seen as a natural extension of higher-order functional programming to concurrent systems that deal with distributed state by coordinating and orchestrating asynchronous data streams exchanged by actors.

Reactive Programming Principles (via manifesto)

* Responsive: The application should be quick to reacts to users, even under load and in the presence of failures
* Resilient and Scalable: The application should be resilient, in order to stay responsive under various conditions. They also should react to changes in the input rate by increasing or decreasing the resources allocated to service these inputs. Today’s applications have more integration complexity, as they are composed of multiple applications.
* Message Driven: A *message-driven*architecture is the foundation of scalable, resilient, and ultimately responsive systems.

**参考文献**

- [reactive-programming-using-rxjava-and-akka](https://www.linkedin.com/pulse/20141208023559-947775-reactive-programming-using-rxjava-and-akka)

## What's AARF?(什么是 AARF)

> AARF is an architectural style, it's not only specific set of technologies, but a design concept throughout the develop stack.
>
> 笔者将 AARF 称为一种架构风格，它不仅仅是一系列具体的技术的集合，更重要的是一种贯穿开发全栈的设计理念。AARF 继承并个性化地实现了 REST 的基本思想，借鉴了 Flux 以及 Redux 的单向数据流的思想，最终使用 Reactive Programming 作为实现手段。

最直观的感受上，传统的 MVC 模型是厨师点菜，不同的业务逻辑就是不同的菜，根据前端的需求做出不同的菜。而 AARF 是自助餐，即使在业务逻辑不明确的情况下，也能提供基本的食材或者半成品，前端可以自由的将这些食材组合搭配，最终形成自己所需要的美食。

> 后端开发不应该再根据前端的某个需求编写相应的 URI 与处理函数，而应该从自身资源分割的视角，为前端提供一系列原子操作，并且在保证权限控制与数据验证的基础上允许前端自由组合

对于 Model 层而言，。实际上，为了强调资源之间的隔离性与操作的原子性，直接的表现就是将原先复杂的 SQL 语句拆分成不同的小块然后交由不同的资源处理器或者 Model 进行处理。这样势必会增加 SQL 查询的次数，不过笔者在下文中会提及如何在保证 AARF 的资源独立的前提的基础上通过 Deferred SQL 的方式来减少无谓的 SQL 查询次数。另一方面，随着 NoSQL 数据库的发展，笔者也是建议可以通过缓存等多种存储方式来减少直接查询的 SQL 的次数。

### Twelve-Factors & Cloud Native Principles

### AARF Principles

* Front-end Friendly

  * flexibility
  * readability
  * Consistency
    * Response comply with request

* High Availability

  * Responsive & Fault-Tolerant

* Develop Simplicity

  ### [Modifiability](undefined)

  #### [Reusability](undefined)

  Reusability is a property of an application architecture if its components, connectors, or data elements can be reused, without modification, in other applications. The primary mechanisms for inducing reusability within architectural styles is reduction of coupling (knowledge of identity) between components and constraining the generality of component interfaces. The uniform pipe-and-filter style exemplifies these types of constraints.

  ​

  * #### [Configurability](undefined)

    Configurability is related to both extensibility and reusability in that it refers to post-deployment modification of components, or configurations of components, such that they are capable of using a new service or data element type. The pipe-and-filter and code-on-demand styles are two examples that induce configurability of configurations and components, respectively.

  * ### [Visibility](undefined)

    Styles can also influence the visibility of interactions within a network-based application by restricting interfaces via generality or providing access to monitoring. Visibility in this case refers to the ability of a component to monitor or mediate the interaction between two other components. Visibility can enable improved performance via shared caching of interactions, scalability through layered services, reliability through reflective monitoring, and security by allowing the interactions to be inspected by mediators (e.g., network firewalls). The mobile agent style is an example where the lack of visibility may lead to security concerns.

* Scalability:架构的可扩展性

  Scalability refers to the ability of the architecture to support large numbers of components, or interactions among components, within an active configuration. Scalability can be improved by simplifying components, by distributing services across many components (decentralizing the interactions), and by controlling interactions and configurations as a result of monitoring. Styles influence these factors by determining the location of application state, the extent of distribution, and the coupling between components.

  Scalability is also impacted by the frequency of interactions, whether the load on a component is distributed evenly over time or occurs in peaks, whether an interaction requires guaranteed delivery or a best-effort, whether a request involves synchronous or asynchronous handling, and whether the environment is controlled or anarchic (i.e., can you trust the other components?).

  ​

## Case Model(案例模型)

* User
* Book
* Comment

Relation

​ FriendShip

# Resource(资源)

在传统的 RESTful 风格的 MVC 框架中，将某个请求按照其请求路径映射到控制器中，并在该控制器中完成了

任何一個 ResourceHandler，可以以相同方式处理来自于外部或者内部的请求。

譬如 DELETE book_comment 可以即直接来自于 URLMapping

也可以来自于内部的 DELETE book。

资源的规范性，包括分割以及命名的规范性是整个 AARF 体系的基石。

## Abstract Resource

> **Everything is Abstract Resource**

AARF 主要是以数据流的方式解决复杂逻辑后来的构建问题，其中对于资源的定义与传统的 REST 约定中的资源有交集也有差异，REST 中强调的是 URI 的概念，即唯一资源定位，往往将超媒体、文档或者某个逻辑对象当做资源。而 AARF 中主要面向的是逻辑对象以及对于逻辑对象的操作。

> **具有独立的属性或者属性组合的逻辑对象即为抽象资源**

最常见的，我们可以将商品的所有属性组合为商品资源。

而对于发邮件这个动作而言，它的属性可能有收件人、邮件正文等等，那么这些属性的组合也就可以构成邮件资源。

实际上，对于资源的定义与划分具有很大的随机性，并且不同的划分方案会极大的影响后期的组合操作。对于如何合理划分资源来保证整个系统的最优性，还需要在后面详细讨论。

## Attribute(资源属性)

* 资源唯一标识，譬如对于用户资源的唯一标识就是 user_id。
* 外键依赖，外键依赖是表征资源之间显性关系的特征。注意，任何一个资源的标识名具有全局唯一性，譬如 use_id，那么所有的资源中都应该叫 user_id，而不应该使用 uid、id 等等缩写或者别名。另一方面，可能某个资源中的两个外键依赖都指向 user_id，但是表示两个不同的含义。譬如如果我们需要表征用户之间的互相关注的行为，一个表中可能有两个 user_id，第一个表示关注者，第二个表示被关注者。那么在命名时务必保证前缀不变，即皆为 user_id，可以通过 by 关键字添加后缀的方式，即 user_id_by_following、user_id_by_followed。
* 值属性

## Entity

不是很建议资源的嵌套，即将某整个其他的资源打包成某个资源的一个属性。

建议以使用基本类型加上 JSONObject、JSONArray 为主，笔者常使用的类型有：

String

Integer

JSONObject

JSONArray

Instant(映射 TimeStamp 类型)

LocalDateTime(映射时间类型)

## Model

在 Model 层的设计时，要注意做到逻辑无关性，而属性相关性。

#### Select

* 利用 foreach 构造多查询的 in 条件查询语句时候，注意容错

# Request & Response

>

## Uniform Resource Flow Path(统一资源流动路径)

### Resource Quantifier(资源量词)

量词分为 单个值、多个值(JSONArray)、空值以及全部值(all)。

preHandle 根据上一个资源，获取到本次所需要处理的资源编号列表。

all，不一定就代表着全部。譬如

/user/{user_id}/book/all/comment

这边的 book 的 all，就应该被替换为多个值，即为隐性的多值。

换言之，所有与当前业务逻辑相关的处理，都放在 preHandle 的起始完成。这种多量词的一般出现在 GET 请求中，也是最复杂的请求之一。

## ResourceBag(资源包)

> 资源包是唯一可以跨资源流动的对象

### Immutable Object(不可变对象)

笔者觉得，没必要为每个 ResourceHandler 创建一个新的 ResourceBag，目前是暂时规定某个 ResourceHandler 尽可以操作 ResourceBag 中包含其对应的资源，而不可以操作其他资源。

## Advanced Request

### Resource Filter

包括条件查找、聚合操作、筛选操作

如果是聚合操作：aggreation

### Resource Integration(资源注入)

### MapReduce For Multiple Request Once(单次多请求处理)

## Non-Resourceful Response(非资源化响应)

```json
{
  "code": 404, //Required
  "desc": "Description Message", //Optional
  "subCode": "Sub Code to specific error" //Optional
}
```

### Code And HTTP Status(响应码与 HTTP 状态)

虽然 HTTP 协议为我们提供了大量的状态码，但是在实际的 API 开发中不建议用很多，毕竟前后端看着都很崩溃。一般来说，我们会建议使用以下五种状态码来表述不同的问题：

### Empty Resource Is Error(空资源即为错误)

# ResourceHandler(资源处理)

## Dispatcher

为了保证多线程下的安全性，要保证每个 ResourceHandler 的无状态性。

为了避免因为复杂的异步数据流而导致逻辑的混乱，做以下约定：

(1)在 route 函数中的主逻辑步骤只允许同步执行，譬如常常要进行推送。如果推送会影响到本次资源流的结果，则同步执行。否则异步执行。

(2)在 route 方法中，不可以调用 onNext、onError 以及 onComplete 方法

## Get

### SetResult

本步骤主要在根据 deferredResult 进行返回，注意，所有的资源，为了保证解析的一致性与有效性，全部以列表方式返回。

## Post

## Update

## Delete

获取用户评价过的书

/user/{user_id}/comment/all/book

获取用户好友评价过的书

/user/[{user_id}]/comment/all/book

/user/{user_id}/comment/all/book

/user/{user_id}/book

/user/{user_id}/comment/["1","2"]/book

get getAll getSingle getMultiple

post postSingle postMultiple

update updateSingle updateMultiple

delete deleteSingle deleteMultiple deleteAll

# Relation(混合关联层)

资源之间的关联一般分为两种，显性关联与隐性关联。譬如主键依赖这样的，往往称为是显性关联，一般也都会在 URFP 中明晰的表现出来。而另一种关联就是所谓的隐性关联，譬如在我们的电商模型中，用户在购买某件商品，即下了某个订单之后

对于隐性关联，一般也会分为必然关联与可变关联。譬如我们往往有一个推送模块，而用户在执行某个操作之后是否发送通知，或者发送什么样的通知，这个在业务初期是不可预知的。因此在 AARF 中的建议中，Model 层中可以写这样的必然关联。而可选关联应该放在单独的 Relation 中进行处理。

对于某个资源的某个操作不可避免地会引起其他资源的操作，而在定义 AARF 的基本规则的时候也提及，某个资源的域中应当只处理与该资源相关联的事务，这样来尽可能地将所有的逻辑操作划分为原子操作，从而能够更灵活地组合封装。譬如在我们的电商模型中，用户在订购某个商品之后会由系统发邮件通知该用户。那么发送邮件这个动作与对订单资源的操作就是产生了一种隐性的依赖关系，这种依赖关系式可变的，譬如可能由发邮件切换到了发短信。在实际的应用中，我们并不可能预知未来的切换方式，如果依靠 DI 的概念，同样会造成一定的硬编码问题。

特别是在协同项目或者后期的迭代开发中，如果以面向逻辑的方式，那么往往会选择根据新的逻辑需求添加新的功能函数，而尽可能地避免修改之前的函数。但是这种盲目地堆叠方式只会导致代码的可维护性更差，最终积重难返。这样一个问题，看起来可以通过严格的

一方面，我们希望能保证代码的简洁性，另一方面，我们又希望能保证代码的灵活性与可变性。笔者一再强调的资源的单一职责问题，并不排斥在资源的 Model 中会有相关其他的操作，只不过建议将存在较大不确定性的操作放置于专门的 Relation 模块。

```java
    public boolean insertSingleGoodsOrder(GoodsOrderResource.Entity goodsOrderEntity) {

        return this.transactionWrapper(() -> {

            //首先插入订单
            goodsOrderMapper.insertSingleGoodsOrder(goodsOrderEntity);

            //将资源封装到ResourceBag中
            final ResourceBag resourceBag = new ResourceBag.Builder(ResourceBag.Action.GET, new ArrayList()).build();

            GoodsOrderResource goodsOrderResource = new GoodsOrderResource(goodsOrderEntity);

            resourceBag.getResources().put("goods_order", goodsOrderResource);

            if (!this.relationProxy.proxy("goods_order", "insertSingleGoodsOrder", resourceBag).toBlocking().first()) {
                //如果操作失败，则回滚
                throw new Exception("附加操作失败");
            }


        });
    }
```

笔者的建议是，对于必然的操作，直接放置在该功能函数中，对于可选操作，放置在 Relation 中进行。

# Filter/Interceptor

## Authority Control(权限控制)

### JWT Based User Authentic(用户认证)

笔者在最初设计 AARF 的风格时犯了一个很大的错误，也是为了兼容旧的系统。将请求认证，即发起该请求的用户认证与用户资源的获取混为一谈。譬如对于以下的请求：

```
/user/{user_token}
```

这样一种直接把用户令牌与用户资源的唯一标识混为一谈的方式就是典型的错误。笔者比较推出基于 JWT 的用户认证。

基于 SQL 级别的控制

错误还是空？譬如用户请求一个不存在的商品，这是一个错误吗？请求一个不存在的资源，特别是在发生资源串联时，因此，在 Get 中，如果请求到的资源为空，则报错。

## Data Verification(数据验证)

数据验证一个是数据格式的验证，包括

请求数据格式的验证，放在 Model 中

返回数据格式的 Json 化

# Deferred SQL

笔者在文章开头就提及 AARF 可能面临的为了保证资源之间的隔离性而导致的 SQL 的冗余查询问题。Deferred SQL 的理念来自于异步编程中的 Promise 或者 Java 中的 Future 的概念。

# Optimization(性能优化)
