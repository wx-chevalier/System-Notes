[![javascript](https://user-images.githubusercontent.com/5803001/44412874-859de980-a59c-11e8-8845-eebe9d13d832.jpg)](https://github.com/wx-chevalier/SoftwareEngineering-Series)

# 软件架构设计

关于架构这个概念很难给出一个明确或标准的定义，更多是结构和愿景，也可以理解为对系统中的实体以及实体之间的关系所进行的抽象描述。有系统的地方就需要架构，架构并非镜花水月或阳春白雪，大到航空飞机，小到一个电商系统里面的一个功能组件都需要设计和架构。系统架构是概念的体现，是对物/信息的功能与形式元素之间的对应情况所做的分配，是对元素之间的关系以及元素同周边环境之间的关系所做的定义。发展就需要分工协作，将目标系统按某个原则进行切分，切分的原则，是要便于不同的角色进行并行工作，结构良好的创造活动要优于毫无结构的创造活动。

软件架构是一个系统的草图。软件架构描述的对象是直接构成系统的抽象组件。各个组件之间的连接则明确和相对细致地描述组件之间的通信。在实现阶段，这些抽象组件被细化为实际的组件，比如具体某个类或者对象。在面向对象领域中，组件之间的连接通常用接口来实现。软件架构为软件系统提供了一个结构、行为和属性的高级抽象，由构件的描述、构件的相互作用、指导构件集成的模式以及这些模式的约束组成。软件架构不仅显示了软件需求和软件结构之间的对应关系，而且指定了整个软件系统的组织和拓扑结构，提供了一些设计决策的基本原理。

**软件架构的核心价值，即是控制系统的复杂性，将核心业务逻辑和技术细节的分离与解耦**。架构师的职责是努力训练自己的思维，用它去理解复杂的系统，通过合理的分解和抽象，使哪些系统不再那么难懂；在工作中了解并关注实际上关系重大但未变得过载的一些关键细节和界面。架构师的角色有：理解并解析需求，创建有用的模型，确认、细化并扩展模型，管理架构。

![](https://i.postimg.cc/mZHM03TM/image.png)

# 软件架构分类

在笔者的知识体系中，实际上将架构分为业务应用架构、服务架构、云基础架构这几大类，业务架构主要着眼于控制业务的复杂性，基础架构着眼于解决分布式系统中存在的一系列问题。无论何种架构，都希望能实现系统的可用，保障业务的高可用。

- 可预测性：

- 可干预性：

- 可伸缩性：

## 职责划分

根据企业中职责的划分，我们往往可以将软件架构，及关联的架构师划分为以下几类：

- 业务/解决方案架构：了解客户/业务方的痛点，项目定义，现有环境；梳理高阶需求和非功能性需求；沟通，方案建议，多次迭代，交付总体架构。

- 应用架构：专注于结合企业需求，定制化 IT 解决方案；大部分需要交付的工作包括总体架构，应用架构，数据架构，甚至部署架构。根据业务场景的需要，设计应用的层次结构，制定应用规范、定义接口和数据交互协议等。并尽量将应用的复杂度控制在一个可以接受的水平，从而在快速的支撑业务发展的同时，在保证系统的可用性和可维护性的同时，确保应用满足非功能属性要求（性能、安全、稳定性等）。

- 数据架构：专注于构建数据中台，统一数据定义规范，标准化数据表达，形成有效易维护的数据资产。打造统一的大数据处理平台，包括数据可视化运营平台、数据共享平台、数据权限管理平台等。

- 中间件架构：专注于中间件系统的构建，需要解决服务器负载，分布式服务的注册和发现，消息系统，缓存系统，分布式数据库等问题，同时架构师要在 CAP 之间进行权衡。

- 运维架构：负责运维系统的规划、选型、部署上线，建立规范化的运维体系。

- 物理架构：物理架构关注软件元件是如何放到硬件上的，专注于基础设施，某种软硬件体系，甚至云平台，包括机房搭建、网络拓扑结构，网络分流器、代理服务器、Web 服务器、应用服务器、报表服务器、整合服务器、存储服务器和主机等。

## 架构模式与架构风格

软件架构设计的一个核心问题是能否使用重复的架构模式，即能否达到架构级的软件重用。也就是说，能否在不同的软件系统中，使用同一架构。当我们讨论软件架构时，常常会提及软件架构模式（Architectural Pattern）与软件架构风格（Architectural Style）。

软件架构模式往往会用于具体地解决某个具体的重复的架构问题，而架构风格则是对于某个具体的架构设计方案的命名。软件架构风格是描述某一特定应用领域中系统组织方式的惯用模式；架构风格反映了领域中众多系统所共有的结构和语义特性，并指导如何将各个模块和子系统有效组织成一个完整的系统。

在笔者的系列文章中，CRUD、分层架构、六边形架构、洋葱架构、REST 以及 DDD，都算是架构风格；而 CQRS、EDA、RARF、微服务等则被划分到架构模式中。

![](https://i.postimg.cc/kXcKDsx2/image.png)

# 软件架构编年史

- 20 世纪 50 年代
  - **非结构化编程**
  - ~1951 – **汇编**
- 20 世纪 60 年代
  - **结构化编程**
  - **分层**: 用户界面、业务逻辑数据存储都在**一层**。
  - ~1958 – Algol
- 20 世纪 70 年代
  - **过程式/函数式编程**
  - ~1970 – Pascal
  - ~1972 – C
  - [1979](http://heim.ifi.uio.no/~trygver/1979/mvc-2/1979-12-MVC.pdf) – **MVC 模式(Model-View-Controller)**
- 20 世纪 80 年代
  - **面向对象编程** (但其思想在 [20 世纪 60 年代](http://userpage.fu-berlin.de/~ram/pub/pub_jf47ht81Ht/doc_kay_oop_en)晚期已经第一次提出)
  - **分层**: **两层**，第一层是用户界面，第二层是业务逻辑和数据存储
  - ~1980 – C++
  - **CORBA** – 通用物件请求代理架构(尽管[1991 年](https://en.wikipedia.org/wiki/Common_Object_Request_Broker_Architecture%23Versions_history)才推出第一个稳定版，但最早使用可以追溯到 [20 世纪 80 年代](https://en.wikipedia.org/wiki/TIBCO_Software))
  - ~1986 – Erlang
  - ~1987 – Perl
  - [1987](https://www.lri.fr/~mbl/ENS/FONDIHM/2013/papers/Coutaz-Interact87.pdf) – PAC 即 **HMVC 模式(Hierarchical Model-View-Controller)**
  - [1988](https://drive.google.com/file/d/0BwhCYaYDn8EgNzAzZjA5ZmItNjU3NS00MzQ5LTkwYjMtMDJhNDU5ZTM0MTlh/view) – **LSP(里氏替换原则)** (~SO**L**ID)
- 20 世纪 90 年代
  - **分层**: **三层**，第一层是用户界面，第二层是业务逻辑(以及浏览器作为客户端时的用户界面展现逻辑)，第三层是数据存储
  - ~1991 – **消息总线**
  - ~1991 – Python
  - [1992](https://www.amazon.com/Object-Oriented-Software-Engineering-Driven-Approach/dp/0201403471) – **EBI 架构**(Entity-Boundary-Interactor) 即 EBC 或 EIC
  - ~1993 – Ruby
  - ~1995 – Delphi, Java, Javascript, PHP
  - [1996](http://www.wildcrest.com/Potel/Portfolio/mvp.pdf) – **MVP 模式(Model-View-Presenter)**
  - [1996](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod) – **OCP**, **ISP**, **DIP** (~S**O**L**ID**), REP, CRP, CCP, ADP
  - [1997](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod) – SDP, SAP
  - ~[1997](http://www.cs.ubc.ca/~gregor/papers/kiczales-ECOOP1997-AOP.pdf) – **面向方面编程**
  - ~1997 – Web 服务
  - ~[1997](http://shop.oreilly.com/product/9780596006754.do) – **ESB** – 企业服务总线 (尽管创造该术语的书籍 2004 年才出版，但这个概念早已被使用)
- 21 世纪 00 年代
  - [2002](http://a.co/7S3sJ2J) – **SRP** (~**S**OLID)
  - [2003](https://www.amazon.com/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215) – **领域驱动设计**
  - [2005](https://blogs.msdn.microsoft.com/johngossman/2005/10/08/introduction-to-modelviewviewmodel-pattern-for-building-wpf-apps/) – **MVVM 模式(Model-View-ViewModel)**
  - [2005](http://alistair.cockburn.us/Hexagonal%2Barchitecture) – **端口和适配器架构**即六边形架构
  - [2006](https://youtu.be/JHGkaShoyNs%3Ft%3D1m17s)? – **CQRS 与 ES** (命令查询职责分离与事件溯源)
  - [2008](http://jeffreypalermo.com/blog/the-onion-architecture-part-1/) – **洋葱架构**
  - [2009](https://medium.com/s-c-a-l-e/talking-microservices-with-the-man-who-made-netflix-s-cloud-famous-1032689afed3) – **微服务**(Netflix)
- 21 世纪 10 年代
  - [2010](https://www.amazon.co.uk/Lean-Architecture-Agile-Software-Development/dp/0470684208) – **DCI 架构**(Data-Context-Interaction)
  - [2012](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) – **整洁架构**
  - [2014](http://www.codingthearchitecture.com/2014/08/24/c4_model_poster.html) – C4 模型

# 架构师

按 TOGAF 的定义，架构师的职责是了解并关注实际上关系重大但未变得过载的一些关键细节和界面，架构师的角色有：理解并解析需求，创建有用的模型，确认、细化并扩展模型，管理架构。架构师是一个既能掌控整体又能洞悉局部瓶颈并依据具体的业务场景给出解决方案的团队领导型人物。

架构师不是一个人，他需要建立高效卓越的体系，带领团队去攻城略地，在规定的时间内完成项目。架构师需要能够识别定义确认需求，能够进行系统分解形成整体架构，能够正确的技术选型，能够制定技术规格说明并有效推动实施落地。
