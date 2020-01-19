![](https://i.postimg.cc/L8T6Sz3G/image.png)

# 软件架构设计

什么是架构？ISO/IEC 42010:20072 中对架构的定义如下：The fundamental organization of a system, embodied in its components, their relationships to each other and the enviroment, and the principles governing its design and evolution. 维基百科中的定义是：软件体系结构是指软件系统的基本结构，创建此类结构的规则以及这些结构的文档。需要这些结构来推断软件系统。每个结构包括软件元素，它们之间的关系，元素和关系的属性，以及每个元素的引入和配置的基本原理。这里定义了架构的三要素：职责明确的模块或者组件、组件间明确的关联关系、约束和指导原则。

![架构三要素](https://s2.ax1x.com/2019/10/11/uHQKtU.png)

软件系统的体系结构是一种隐喻，类似于建筑物的体系结构，是一种**整体与局部关系的抽象描述**，架构是软件系统内部设计中最重要而又模糊的方面。对于架构，见仁见智，很难有一个明确或标准的定义；但架构并非镜花水月或阳春白雪，有系统的地方就需要架构，大到航空飞机，小到一个电商系统里面的一个功能组件，都需要设计和架构。抽象而言，架构就是对系统中的实体以及实体之间的关系所进行的抽象描述，是对物/信息的功能与形式元素之间的对应情况所做的分配，是对元素之间的关系以及元素同周边环境之间的关系所做的定义。架构能将目标系统按某个原则进行切分，切分的原则，是要便于不同的角色进行并行工作，结构良好的创造活动要优于毫无结构的创造活动。

**软件架构的核心价值，即是控制系统的复杂性，将核心业务逻辑和技术细节的分离与解耦**。软件架构是系统的草图，它描述的对象是直接构成系统的抽象组件；各个组件之间的连接则明确和相对细致地描述组件之间的通信。在实现阶段，这些抽象组件被细化为实际的组件，比如具体某个类或者对象。在面向对象领域中，组件之间的连接通常用接口来实现。架构师的职责是努力训练自己的思维，用它去理解复杂的系统，通过合理的分解和抽象，理解并解析需求，创建有用的模型，确认、细化并扩展模型，管理架构；能够进行系统分解形成整体架构，能够正确的技术选型，能够制定技术规格说明并有效推动实施落地。

## 架构模式与架构风格

软件架构设计的一个核心问题是能否使用重复的架构模式，即能否达到架构级的软件重用。也就是说，能否在不同的软件系统中，使用同一架构。当我们讨论软件架构时，常常会提及软件架构模式（Architectural Pattern）与软件架构风格（Architectural Style）。

软件架构模式往往会用于具体地解决某个具体的重复的架构问题，而架构风格则是对于某个具体的架构设计方案的命名。软件架构风格是描述某一特定应用领域中系统组织方式的惯用模式；架构风格反映了领域中众多系统所共有的结构和语义特性，并指导如何将各个模块和子系统有效组织成一个完整的系统。

在笔者的系列文章中，CRUD、分层架构、六边形架构、洋葱架构、REST 以及 DDD，都算是架构风格；而 CQRS、EDA、UDLA、微服务等则被划分到架构模式中。

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

# About



## Copyright | 版权

![License: CC BY-NC-SA 4.0](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg) ![](https://parg.co/bDm)

笔者所有文章遵循 [知识共享 署名-非商业性使用-禁止演绎 4.0 国际许可协议](https://creativecommons.org/licenses/by-nc-nd/4.0/deed.zh)，欢迎转载，尊重版权。如果觉得本系列对你有所帮助，欢迎给我家布丁买点狗粮(支付宝扫码)~

## Copyright & More | 延伸阅读

![技术视野](https://s2.ax1x.com/2019/12/03/QQJLvt.png)

您还可以前往 [NGTE Books](https://ng-tech.icu/books/) 主页浏览包含知识体系、编程语言、软件工程、模式与架构、Web 与大前端、服务端开发实践与工程架构、分布式基础架构、人工智能与深度学习、产品运营与创业等多类目的书籍列表：

![NGTE Books](https://s2.ax1x.com/2020/01/18/19uXtI.png)