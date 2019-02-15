# Why RARF?

> 本文仅代表个人思考，若有谬误请付之一笑。若能指教一二则感激不尽。另外本文所写是笔者个人的思考，暂未发现有类似的工作。不过估计按照笔者的智商可能世界上已经有很多大神早就解决了笔者考虑的问题，若有先行者也不吝赐教。

> Make everything as simple as possible, but not simpler — Albert Einstein 把每件事，做简单到极致，但又不过于简单 - 阿尔伯特 · 爱因斯坦

在文章之初，笔者想交代下自己的背景，毕竟下文的很多考虑和思想都是来源于笔者所处的一个环境而产生的，笔者处于一个创业初期产品需求业务需求急速变更的小型技术团队。一般而言软件工程的基本步骤都会是先定义好需求，定义好数据存储，定义好接口规范与用户界面原型，但是笔者所在的环境下往往变更的速度已经超过了程序猿码字的速度 N 倍。另外，笔者本身也是一名前端开发者(待过创业公司的都知道，啥都得上)，笔者很多用于后端开发的思想也受到了前端很大的影响，譬如后端一直遵循的 MVC 架构模式，而前端已经有了 MVC、MVP 、 MVVM、Flux 、 Redux 等等这一些变迁。前端迎来了组件化的时代，而后端也是微服务的思想大行其道。本文主要受以下几个方法论的影响：

* RESTful
* MicroService
* Reactive Programming
* Redux

在进行具体的表述之前，笔者想先把一些总结的内容说下，首先是几个前提吧：

* **不要相信产品经理说的需求永远不变了这种话，too young ， too naive**

* **在快速迭代的情况下，现有的后台接口僵化、代码混乱以及整体可变性与可维护性变差，不能仅仅依赖于提高程序猿的代码水平、编写大量复杂的接口说明文档或者指望在项目开始之初即能定下所有接口与逻辑。目前的后端开发中流行的 MVC 架构也负有一定的责任，我们的目标是希望能够寻找出在最差的人员条件下也能有最好结果的方式。或者描述为无论程序猿水平如何，只要遵循几条基本原则，即可构造高可用逻辑架构，并且这些规则具有高度抽象性而与具体的业务逻辑无任何关系。**

* **任何一个复杂的业务逻辑都可以表示为对一或多种抽象资源的四种操作 (GET、POST 、 PUT、DELETE) 的组合。**

## Motivation

RARF 的全称是 Reactive Abstract Resource Flow，字面意思是响应式抽象资源流，笔者把它称为基于抽象资源流的响应式处理的架构风格。一般来说，Server-Side 开发包含接口定义、具体业务逻辑处理方法、数据存储交互 (SQL+NoSQL) 这三个层次，RARF 也是面向这三个方面，提出了一些自己的设想。这边的 Reactive 表示 RFRF 同时关注了并发与异步这一方面的问题，并选择了 Reactive Programming 或者更明确一点的，FPR 来作为方法论。

### 面向用户的接口可读性与可用性 ( 可组合性 ) 的提升

为啥笔者一直在强调自己是个前端工程师，就是在思考 RARF 的时候，我也从一个前端工程师的角度，来考虑到底前端需要怎样的一种接口方案。

#### 接口的可读性

REST 风格的一个特征就是资源化的接口描述，譬如：

```
[GET] http://api.com/books
[GET] http://api.com/book/1
[POST] http://api.com/book
```

这种资源化的接口可读性还是比较好的，通过动词与资源名的搭配很容易就能知道该接口描述的业务逻辑是啥。不过，在笔者浅薄的认知里，包括 REST 原版论文和后面看的各式各样的描述，都只针对单个资源进行了描述，即简单逻辑进行了表述。如果现在我们要添加一个购买书籍的接口，在该接口内包括了创建订单、扣费等一系列复杂逻辑操作，相信程序猿会倾向于使用：

```
http://api.com/doBookBuy
```

对于单个接口而言，可读性貌似上去了，但是，这最终又会导致接口整体命名的混乱性。确实可以通过统一的规划定义来解决，但是笔者个人认为这并没有从接口风格本身来解决这个问题，还是依赖于项目经理或者程序猿的能力。笔者在 RARF 中定义了一个概念，叫 Uniform Resource Flow Path( 统一资源流动路径 ) 来增加接口的描述性，同时 URFP 也是整个 RARF 的驱动基础。

#### 前端定制的接口 (Frontend-Defined APIs And URFP-Driven)

无论是 WebAPP 还是 iOS 还是 Android，都需要与业务服务器进行交互，我们一般称之为消费接口，也就是所谓 Consume API 的感觉。笔者一直有个形象的感觉，现在前后端交互上，就好像去饭店吃饭，厨师把菜单拿过来，消费者按照菜单定好的来点菜。而笔者希望能在 RARF 达成的效果，就是把点菜变成自助餐，也就是所谓的前端可以根据后端提供的基本的资源的四种操作来完成自己复杂的业务逻辑。笔者在这里先描述两个常见的蛋疼场景：( 1)前后之争，该迁就谁？作为一个前端开发者，往往会以界面为最小单元来进行逻辑分割 ( 不考虑组件化这种代码分割的影响 )，毕竟每次请求都会引起延迟与网络消耗，所以前端开发者往往希望我在一个界面上进行一次逻辑请求即可获取所有的待展示参数。但是对于后端开发者而言，是以接口为最小单元来进行逻辑分割，有点类似于那种 SRP( 单一职责原则 )，每个接口做好自己的事情就好了。譬如如果 UI 为 Android 端设计了一个界面，该界面上展示了一个电商活动的信息，譬如: ![](http://7u2q25.com1.z0.glb.clouddn.com/906389A5-57BE-430F-A798-BA3B5C3C4E38.png) 然后呢，UI 为 iOS 端设计了一个界面，展示某个电商活动的所有商品，譬如: ![](http://7u2q25.com1.z0.glb.clouddn.com/5E86BDA2-89A9-4900-977F-12B0FF21082C.png) 然后，UI 为 WebAPP 设计了一个界面，同时包含了活动介绍和商品列表，大概是这样: ![](http://7u2q25.com1.z0.glb.clouddn.com/8CE16080-63AD-45FE-9FF7-662DB7EF9E9B.png)

那么现在后端要提供接口了，如果按照逻辑分割的要求，最好是提供三个接口：

```
/activities ->获取所有的活动列表
/activity/1 ->根据某个活动ID获取活动详情，包含了该活动的商品列表
/goods/2 ->根据商品编号获取商品详情
```

这样三个原子化的接口是如此的清晰好用，然后后端就会被 iOS、Android 、 WebAPP 三端混合打死。那再换个思路呢，按照最大交集，就是 WebAPP 的需求，我把全部数据一股脑返回了，然后后端就会被产品经理打死吧，浪费了用户这么多流量，顺带着延迟啊服务器负载啊也都上来了。另外呢，譬如商品表中有个描述属性，是个很大的文本，那么我们在返回简略列表的时候，肯定不能返回这个大文本啊，这样的话，是不是又要多提供一个接口了呢？假设业务逻辑中有 $M$ 个不同的资源，相互之间有交集，而每个资源有 $N$ 个属性，那么夸张来说，如果按照界面来提供接口，大概需要: $$ \sum*{i=1}^M C_M^i \* ( \sum*{j=1}^{i} (C_N^j) ) $$ 种搭配，当然了，这绝逼是个夸张的描述。( 2)界面变了 / 接口变了 / 参数变了假设勤恳的后端程序猿按照 iOS、Android 、 WebAPP 不同的要求写了不同的接口，完美的解决了这个问题，然后 UI 大手一挥，界面要改，之前做的菜是不是全都得倒了呢？同理，如果接口变了或者接口参数变了，无论前后端估计都会想杀人的吧？

### 逻辑处理函数中的状态管理

在目前的前端开发中，状态管理这个概念很流行，笔者本身在前端的实践中也是深感其的必要性。在前端中，根据用户不同的交互会有不同的响应，而对于这些响应的管理即是一个很大的问题。笔者在这里是借鉴的 Redux，它提出了构造一颗全局的状态树，并且利用大量的*纯函数*来对这颗状态树进行修正。同时，这颗状态树是可追踪的，换言之，通过对于状态树构造过程的跟踪可以完全反推出所有的用户操作。那么这个状态的概念，移植到后端的开发中，应该咋理解呢？首先，后端是对于用户的某个请求的响应，那么就不存在前端那种对于用户复杂响应的问题。直观来看，后端的每个 Controller 中的逻辑操作都是顺序面条式的 ( 即使是异步响应它在逻辑上也是顺序的 )，可以用伪代码描述如下：

```
RequestFilter();//对于请求过滤与权限审核
a = ServiceA();//根据逻辑需要调用服务A，并且获取数据a
if(a=1){
b = ServiceB(a);//根据上一步得到的结果，调用某个特定的Service代码，注意，数据存取操作被封装在了Service中
}else{
b = ServiceC(a);//否则就调用
}
c = ServiceD(b)
put c
```

#### 状态混乱

目前很多的 Controller 里是没有一个全局状态的，我们习惯了调用不同的 Service 然后进行判断处理在最后整合成一个结果然后返回给用户。这就导致了存在着大量的临时变量，譬如上面伪代码中的`a,b,c`。笔者认为的状态管理的结果，就是在一个逻辑处理的流程中，不需要看前 N 步代码即可以判断出当前的变量状态或者可能的已知值，特别是在存在嵌套了 N 层的条件判断之下。笔者认为的状态混乱的第一个表现即是缺失这样一种全局状态，顺便插一句，如果有学过 Redux 的朋友肯定也能感受到，在有全局状态树的情况下可以更好地追踪代码执行过程并且重现错误。另一个可能引起状态混乱的原因，即是 Service 的不可控性。借用函数式编程中的概念，我们是希望每个被调用的 Service 函数都是纯函数 ( 假设把数据库输入也作为一个抽象的参数 )，不会存在 Side Effect。但是不可否认，现行的很多代码中 Service 函数会以不可预知的方式修改变量，虽然有时候是为了节约内存空间的考虑，譬如：

```
ArrayList<Integer> array = new ArrayList();
Service(array);//在Service中向array里添加了数据
```

而这样一种 Side Effect 的表现之一就是我们在调试或者修改代码的时候，需要递归 N 层才能找到某变量到底是在哪边被修改了，这对于代码的可维护性、可变性都造成了很大的负担。

#### 抽象混乱

**因为每一个 Controller 都是面向一条完整的业务逻辑进行抽象，所以在朴素的认知下并不能很好地进行代码分割。**在符合自然认知规律的开发模式中，我们习惯先定义出接口，然后在具体的接口需要的功能时进行抽象，譬如在项目之初，我们需要一个获取所有商品列表的接口，我们可以定义为：

```
[GET]:/goodss -> 映射到getGoodsController
```

在编写这个 getGoodsController 方法的过程中，我们发现需要去 goods 表中查询商品信息，那我们会提出一个`selectGoodsAllService`的服务方法，来帮助我们进行数据查询。然后，又多了一个需求，查询所有价格小于 10 块的商品：

```
[GET]:/goods?query={"price":"lt 10"} -> 映射到getGoodsController
```

这时候我们就需要在 Controller 加个判断了，判断有没有附带查询条件，如果附带了，就要调用专门的 Service。或者也可以为这个查询写一个专门的 Controller：

```
[GET]:/getGoodsByPriceLess?price=10 -> getGoodsByPriceLessController
```

同样需要编写一个根据价格查询的 Service。然后，随着业务的发展，我们需要根据商家、剩余货物量等等进行查询，然后 Service 越来越多，慢慢的，就陷入了抽象漏洞的困境，就是我们虽然抽象了，但是根本不敢去用下面的代码，因为就怕那是个大坑，比较不知道它的抽象到底是遵循了什么的规则。

#### 依赖混乱

![](http://7xkt0f.com1.z0.glb.clouddn.com/5AB7141F-CDAA-4045-87CF-466CD3AEE73B.png)

上图描述了一个请求处理过程中的依赖传递问题，直观的感受是，当我们的业务逻辑系统变得日渐复杂之后，不依赖 Code Review 或者测试，特别是对于小团队而言，不敢去随便乱改一个现有的服务，宁可去为自己要处理的逻辑写一个新的服务，因为压根不知道现有的服务到底被多少个东西依赖着。牵一发而动全身啊。这就导致了随着时间的增长，系统中的函数方法越来越多，并且很多是同构的。举例而言，有时候业务需要根据不同的年龄来获取不同的用户，但是程序猿在初期接到的业务需求是查找出所有未成年的人，它就写了个方法`getMinorsService()`，这个服务不用传入任何参数，直接调用就好。然后某天，有个新的需求，查找所有的没到 20 岁的人，于是又有了一个服务`getPeopleYoungerThan20()`。这样系统中就多出了大量的功能交叉的服务，而这些服务之间也可能互相依赖，形成一个复杂的依赖网络。如果有一天，你要把数据库的表结构动一下 ( 譬如要把部分数据移到 NoSQL 中 )，就好像要踩 N 颗地雷一样的感觉吧。

说到依赖，相信很多人首先想到的就是 Spring 的 DI/IoC 的概念。不过笔者认为依赖注入和本文笔者纠结的依赖混乱的问题，还是有所区别的。依赖注入的原理和使用确实非常方便，但是它还是没有解决依赖划分混乱的问题，或者需要大量的劳动力去在代码之初就把所有的依赖确定好，那么这一点和 RARF 文初假设的场景也是不一致的。

最后，笔者在 RARF 中，不会解决依赖传递或者多重依赖的问题，只是说通过划分逻辑资源的方式，把所有的依赖项目明晰化了。譬如上面提到的 N 个具有交叉功能的 GET 函数，都会统一抽象成对于某个资源的抽象 GET 操作。换言之，以 URFP 的抽象原则进行统一调用。

### 挣脱数据库表结构 / 文档结构的束缚

在富客户端发展的大潮之下，服务端越来越像一个提供前端进行 CRUD 的工具。任何一个学过数据库，学过 SQL 的人，都知道，联表查询比分成几个单独的查询效率要来的好得多，这也是毋庸置疑的。但是，在本文所描述的情况下，联表查询会破坏原本资源之间的逻辑分割。这边先说一句废话：**在 MVC 中，当数据表设计和接口设计定了之后，中间的代码实现也就定了。** 举例来说，我们存在两个业务需求：获取我收藏的商品列表和我购买过的商品列表，这边涉及到三个表：goods(goods_id) ， goods_favorite(goods_id,user_id)，goods_order(goods_id,user_id) 。在 MVC 中，我们会倾向于写上三个接口，配上三个 Service，这就是典型的提高了查询效率，并且符合人们正常开发顺序的方式。不过，如果单纯从资源的逻辑分割的角度考虑的话，对于商品查询而言，应该只有一个`selectGoodsByGoodsId`这个 Service，换言之，把原来的单次查询拆分为了：先在其他表中查询出 goods_id，然后再用 goods_id 在 goods 表中查询商品详情。这样的逻辑分割是不是明晰了一点？是啊，这肯定会影响效率的，笔者也在想着能不能通过某种方法达成一个平衡。

> 如果你看到这还有兴趣的话，可以看看下面笔者的思考。

## Solution( 解决方案 )

### 关于抽象资源

> **万物皆抽象资源**

RESTful 中也有`all is resource`的概念，但是 RESTful 强调的是像超文本啊、某个音视频啊，这些都可以通过 URI 访问到，也就是可以当做一个资源然后被前端获取。这一点已经获得了广泛地认可，而在 RARF 中强调的抽象资源，就好像在电商系统中，我们都知道 goods 与 user 是两种资源，那么在描述用户收藏夹的时候，即对于 user_goods 这个表，算不算资源呢？当然算啊！换言之，描述两个资源之间关系的，无论是一对一，还是一对多，只要具备唯一标识的，就是独立的抽象资源。不过，如果哪一天逻辑设计上，把用户收藏的商品，变成了 JSON 字段然后存储在 user 表中，即成了 user 资源的一个属性，那么此时这种映射关系就不是一个资源了。因为它没有一个唯一标识。

#### 资源的定义

在讲资源的定义之前，首先看看关系型数据库中经典的设计范式：

>第一范式 ( 确保每列保持原子性 )
>第二范式 (2NF) 属性完全依赖于主键 ( 消除部分子函数依赖 )
>第三范式 (3NF) 属性不依赖于其它非主属性[消除传递依赖]

对于从具体的业务逻辑抽象出相互分割并且关联的资源是 RARF 的基础，在笔者构思 RARF 的基本原则时，一开始是想走强制严格化道路，即严格命名，具体而言：

* 万物皆资源，资源皆平等。
* 每个资源具有唯一的不可重复的命名。
* 任何资源具有一个唯一的标识，即 {resource_name_id} 在所有表中必须保持一致。譬如我们定义了一个资源叫 user，那么它的标识就是 user_id，不可以叫 uid、userId 等等其他名称。
* 任何资源的属性由 {resource_name_attribute_name} 构成，且遵循第二与第三范式。

这一套命名规则，有点像乌托邦吧，毕竟作为一个不成熟的想法，RARF 还是要去切合已经存在的各种各样的数据库设计风格或者方案，不可能让人家把表推倒全部重建，所以呢，最后资源定义的规范就一句话：**资源名不可重复且资源属性具有唯一所有性**。不过想想，如果按照严格命名方案的话，会自动化很多。

#### 资源的操作 :GET、POST 、 PUT、DELETE + ResourceHandler

RARF 的基石即是抽象资源 (Abstract Resource) 与对于资源的操作 (Handling)，笔者这里暂时借用函数式编程与 Redux 的概念，ResourceHandler 我们可以把它当做 " 纯函数对待 "。这里的 ResourceHandler，我们可以将用户输入与数据库输入当做两个输入，在输入相同的情况下 ResourceHandler 的执行结果是一致的，换言之，在 RARF 中，ResourceHandler 本身是无状态的、无副作用的。而每个 ResourceHandler 向外提供的资源的操作，就是 GET、POST 、 PUT 与 DELETE。这里不进行赘述了。

到这里为止，我们其实是针对 RESTful 原始的譬如：

```
/book/1
```

这种逻辑的处理进行了分析，还是对于单个资源的，在实际的业务场景中，我们往往是对一或多个资源的组合操作。

### 业务逻辑与资源流动 : 用 URFP 来描述业务逻辑，去 Controller

学过数学的都知道，两点确定一条直线，而在后端逻辑开发中，当你的 Controller(Controller 指响应某个固定业务请求的接口处理函数 ) 与数据表设计定了之后，你中间的代码该怎么写也就定了。换言之，Controller 与数据表是主食材，中间代码是调味料。这也就是典型的点菜模式，而 RARF 中一直强调的资源操作的组合的概念，是希望前端能够自己用资源和资源的操作来给自己做一盘菜。而 URFP 正是提供这种灵活性的核心机制。RESTful 是推荐使用四个动词来分割一个业务，在非 RESTful 风格下，如果用户要买个东西，接口应该这么定义：

```
/doGoodsOrderCreate?goods_id=1
```

如果是 RESTful 风格的接口呢：

```
POST:/goods_order?goods_id=1&...
```

于是我自己想，按照 RARF 的资源流动的这个感觉，那是不是应该：

```
POST:/goods/{goods_id}/goods_order?requestData={requestData}
```

这一套 URL 的规则笔者称之为 Uniform Resource Flow Path( 统一资源流动路径 )，换言之，将与业务逻辑相关的必须性资源放在 url 上。这样写有啥好处呢，我自己觉得啊，一个是可读性更好了，二个呢在代码层次区分的也会更开了。就能很好地把各个独立地 ResourceHandler 给串联起来啦。

#### 资源转化

**原则：所有 URFP 的邻接资源之间必存在且只存在 ResourceID 依赖关系** 资源转化是 URFP 的最核心的思想，感性理解的话，譬如对于下面这个 URL，是用来获取某个用户的收藏夹的：

```
/user/1/goods_favorite/all/goods
```

当 Application 接收到这个请求时，会创建一个 ResourceBag，就好像一个空的购物篮。然后 ResourceBag 被传送到 UserHandler，根据 user_id 里面拿到了一个 User 资源的实例，然后下一步又被传送到了 goodsFavoriteHandler，这个 Handler 看下篮子里已经有了 User 资源，然后就根据 user_id 查询出新的 GoodsFavorite 资源，然后这个篮子再被传递到 GoodsHandler，同理，根据篮子里现有的资源来换取获得 Goods 资源的实例。这种用已用的资源去获取新资源的方式就是所谓的资源转化，注意，邻接资源之间务必存在主键依赖关系，譬如从 goods_favorite 转化到 goods 的时候，在 goods_favorite 表中就有一列是表示的是 goods_id。

#### 资源注入

资源注入的应用场景可以描述如下: UI 设计了一个界面，同时展示了商品列表和商品的供货方的信息。这就等于要把两种资源合并返回了，在上文前后之争中笔者就已经讨论过，最符合逻辑分割的思想就是让前端先请求一波商品列表，然后根据返回的 goods_provider_id 来获取供货方的信息，不过估计要是真的这么做了，会被打死的吧。笔者这边呢，引入了一个`resource_integration`关键字，譬如，我们的这个请求可以这么写：

```
/goods?requestData={"resource_integration":["goods_provider"]}
```

那么在 ResourceHandler 接收到这个请求的时候，会自动根据需要注入的资源，进行资源注入。这边还有个遗留问题，类似于数据库查询中的左联接和右联接，如果需要注入的资源不存在，咋办呢？我还没碰到过这个业务场景，如果有朋友遇到过，请和我联系。

#### 隐性业务逻辑的处理

实际上 URFP 并不能完美显示所有的业务逻辑，譬如在购买商品时候，我们是把它看做了对于 goods_order 资源的一个 POST 操作，但是实际业务中，购买了商品之后还要对 goods 进行操作，即把商品数目减一，还要对 credit 进行操作，即把用户积分加减，或者创建支付订单等等等等。以上这些隐性业务逻辑是与返回结果强相关的，直接写在 ResourceHandler 当中即可，那还有一种是非强相关的，最典型的例子就是日志功能。在正常的业务逻辑处理时，不可能因为你日志记错了就不让用户去购买东西的吧？关于这部分隐性业务逻辑的处理，笔者其实不太喜欢 AOP 这种方式，感觉不太可控，所以还是想借鉴 Middleware( 二者区别在哪呢？) 或者 Reducer 这种方式。

看到这里，如果还有兴趣的话可以看看笔者[Java 版本的实现，正在剧烈变动中，不过代码也不多就是了](https://github.com/wxyyxc1992/RARF-Java)，如果笔者的思想真的可能有些意义的话笔者打算在 NodeJs、PHP 以及 Swift 这几个语言中都实现一下，也欢迎大神的加入。

### 关于数据库设计

RARF 有一个核心理念就是资源的独立性，但是在现有的后端程序开发中，特别是基于关系型数据库的开发时，我们不可避免的会引入联表查询。譬如系统中使用 goods_user 这个表来描述用户的商品收藏夹，如果我们要获取某个用户的收藏的商品列表，最好的方式肯定是用 goods_user 与 goods 这两个表进行联合查询，但是这样的话势必又无法达成资源分割的目标，那么在 RARF 中，最极端的方法是：

```
/goods_user/all/goods
```

根据上文对于 URFP 的描述，这个路径首先会传给 goods_user 的 Handler，在该 Handler 进行一次查询，获取所有的 goods_id。然后根据 goods_id 查询获取所有的商品信息并以列表方式返回。这是一种最符合资源分割原则的方法，但是毫无疑问这样会大大增加数据库交互次数，不符合优化的规则。那该咋办呢，根据 URFP 的原则一，邻接资源之间存在且只存在 ResourceID 依赖关系，那我们引入 DeferredSQL 的概念，即在 goods_user 中，我知道我要查询出什么，但是因为我不是 URFP 的最后一环，我只是传递一个 DeferredSQL 下去，而不真正的进行数据库查询操作。另一种情况，可能存在需要联合查询的就是 URFP 中描述的资源注入的情况，即存在 resource_integration 的情况下。实例而言，我们在获取商品列表的时候同时也要获取商品的提供方的信息，一般情况下需要把 goods 表与 goods_provider 表联合查询，那么在 RARF 中同样也是基于 DeferredSQL，如下所示：

```
DeferredSQLExecutor(DeferredSQLForQueryGoods,DeferredSQLForQueryGoodsProvider)
```

具体到方法论上，以 Java 为例，目前流行的数据库辅助框架有 Hibernate 与 MyBatis。Hibernate 是全自动的 ORM 框架，不过它的实体类中强调 One-to-One、One-to-Many 这样的依赖关系，即通过关联查询把其他资源视作某资源的一个属性，这样无形又根据逻辑把资源混杂了。另一个是 MyBatis，半自动的框架，但是其 SQL 语句无法自动组装，即无法自动帮你进行联合查询，还是得自己写基本的 SQL 模板然后它帮你自动生成，也是无法完全符合 RARF 的需要。

## Pursuit( 愿景 )

缩了那么多，最后，我还是陈述下我在设计 RARF 一些莫名其妙的东西时候的愿景吧，其实看到现在机智的同学，应该能够感觉到，这个 RARF 和 MicroService 在很多设计理念上还是很类似的，这里先盗用下 MicroService 的 Benefits：

>Microservices do not require teams to rewrite the whole application if they want to add new features.
>Smaller codebases make maintenance easier and faster. This saves a lot of development effort and time, therefore increases overall productivity.
>The parts of an application can be scaled separately and are easier to deploy.

那么改造一下，RARF 的愿景就是：

* RARF 希望能够在修改或者增删某些功能时不需要把全部代码过一遍
* 基于 Resource 分割的代码库会更小并且更好管理，这会大大节省开发周期，提供产品能力
* 整个应用程序能够独立扩展、易于部署。就像 RARF 中，如果发现哪个 ResourceHandler 需求比较大，可以无缝扩展出去。

估计这篇文章也没啥人愿意看吧，不过如果哪位大神也有同样类似的思考的欢迎加 QQ384924552，可以一起讨论讨论。

# Introduction

> **AARF 是适用于现代复杂多变的业务模型，同时支持异步编程与分布式扩展的架构风格，Inspired By REST,Flux And Functional Reactive Programming 。**

AARF 的全称为 Asynchronous Abstract Resource Flow，是属于对于 REST 架构风格的扩充与完善。AARF 提出了完全面向资源的请求处理、逻辑分层 (Layer System) 以及全局统一的命名规范。AARF 的核心理念来源于笔者多年的前后端综合实践，不过笔者也不能确定这一套想法是否具有真的实践意义与价值，还是仅仅只是沉渣泛起抑或是简单的脑残。

AARF 不仅仅是一套具体实现方式，更重要的是一种架构风格的设计理念，它可以灵活的运用于包括 Java、PHP 、 NodeJS、Go 乃至于 Swift、Rust 这样的新兴服务端编程语言上。笔者也会不断地完善更新，也欢迎大家提出自己的宝贵意见。

AARF 在设计理念上很类似于现在流行的微服务模式 (MicroService Pattern)，笔者会在一个单独的章节进行对比。不过概而论之，微服务是从功能的角度，以原子服务组合的方式构建大型复杂长期运行的应用程序。而 AARF 是从业务逻辑的角度，以抽象资源的原子处理进行组合的方式构建灵活可变稳定的应用程序。

MVC 是 。 笔者没有经历过大量的大型项目，大部分经历的是中小型需要快速迭代乃至于整个需求全部推翻重来的项目。在实战过程中，深刻感受到目前 MVC 粗粒度下

。很多的看上去很傻逼的错误，我们可能寄希望于程序员开发时候的标准的规范。简单来说，我们可能在 MySQL 中存放一条 JSON 字符串，在返回给前端时需要先进行 JSON 解码转化为 JSONArray 再和其他属性统一编码。这个问题看上去只需要规定程序猿在每个需要返回该属性的接口处进行编码即可，不过事实证明很多莫名奇妙的错误就是来自于这里。当然，通过数据模型或者统一的返回校验，譬如 JSON Schema 校验，我们肯定可以避免这个问题。上文提及的两个小例子，说明我们需要一个灵活但是有一定规范约束，这个规范约束应该像 REST 一样简单易懂并且容易实行。

MVC 架构的 Layer System 是在 Controller、Model 、 Service 这样进行分层，而 AARF 是纵向切分。在 Controller 层，或者是请求映射层，AARF 并不是将每个请求映射到一个专门的处理函数。而在 Model 层，AARF 也不建议按照每一个具体的业务逻辑编写 SQL 语句并向上提供，这样确实可以提供查询的效率，但是一旦业务需求发生变化之后，会出现大量的冗余的 SQL 语句以及对应的冗余代码。你会觉得自己的代码一团糟，就好像埋着无数的地雷。

为了方便理解，我们也可以借用函数柯里化与反柯里化的概念。我们提供一系列适用范围广，但是适用性较低的接口来代替原先适用性较高，但是适用范围极低的接口。而柯里化的这一步即交由前端来完成，也就是下文中会提及的 URFP 的概念。

AARF 中文文档地址：[AARF-CH](https://wxyyxc1992.gitbooks.io/aarf-ch/content/)

AARF 英文文档地址：[AARF-EN](https://wxyyxc1992.gitbooks.io/aarf-en/content/)

AARF 的 Java 实例地址：[AARF-Java](https://github.com/wxyyxc1992/AARF-Java)

笔者的联系方式：QQ(384924552) ， Mail(384924552@qq.com)

## Why AARF?( 为什么是 AARF)

> AARF 代表着在后端开发中从面向逻辑到面向资源，从以业务 / 页面为驱动到以状态 / 数据为驱动的设计理念的变化。

目前流行的复合 REST 分层的系统的原则的架构即是 MVC，在传统的 J2EE 中一般分为 Controller-Service-Model，也是典型的面向逻辑的编程。首先一个逻辑会映射到一个 Controller，再由 Controller 中根据 URI 来组合搭配不同的 Service，而某个 Service 往往也是为了完成某个业务逻辑而特别存在的。不过 MVC 完全是一套非常粗粒度的规范，在实际的编程中，后台就是一个庞杂的数据提供方，连接前端与数据库。而后台的具体代码分割风格在 Controller 层次受业务请求约束，在 Model 层则受到 SQL 语句的约束。确实，特别是在 GET 类型请求的处理中，如果需要返回多个资源的综合结果，用连接查询或者子查询毫无疑问会比多次查询节省很多消耗。但是这种混杂的 SQL 又会将我们希望达到的资源分割的目的给毁掉，因此，AARF 中希望能够立足于规范的不同资源间的 SQL 的自动智能组合来解决这个问题。

在 AARF 中，不同的资源处理器之间的逻辑代码重合度会在 80% 以上，换言之，为了不同业务逻辑而产生的硬编码的需求被尽可能地缩小，从而尽可能地避免需求变更带来的代码变化。毕竟在大型系统中一行代码的改变可能形成的多米诺骨牌效应是不可想象的。

另一方面，以 Spring 为代表的框架强调的是以 DI，也就是 IOC 的方式来解决这种依赖的可变性问题。但是它们在一个宏观的视角上还是面向逻辑编程。AARF 提供了不仅仅是一套类似于 Spring 的编程 Library，也提供了一套从前后到后的基于约定的命名规划与代码风格。需要注意的是，因为业务逻辑的复杂多变性，AARF 只是尽可能地在抽象与具体之间寻求一种平衡，具体的业务逻辑设计与代码架构还是需要根据实际的应用场景而言。而这种抽象毫无疑问在设计初期会影响到性能，譬如可能需要冗余的数据库操作等，这种性能的补偿会在后面提及。

在对大量复杂的业务后台的分析之后，发现如果我们深入到某个具体的业务流设计中，会发现不同的 API 之间具有相当复杂的且唯一的关系。而如果我们提取这些业务逻辑之间的共性，即会发现对于一些抽象资源的组合和处理方式是如此的类似。

AARF 继承并个性化的解释了 REST 的六大原则，同时自身的原则为：

> 这些所谓的原则还处于修订中，不建议用于生产环境
>
> PS：上面这句话只是单纯的为了装逼

* 约定优于配置，配置优于硬编码，这一条算是 AARF 的核心理念，一方面表现在前端可见的资源组合搭配上，另一方面表现在对于隐性

- 请求与响应的一致性。就好像 Redux 宣称的 Predictable State 一样，之前客户端是在请求之后对于返回数据进行
- 尽可能地资源隔离。不强求资源的完全隔离，也不支持把对于资源的处理混杂在一起。至少在 ResourceHandler 这一层，不建议有对于其他资源的操作。在 Model 层，可以对于确定性的关联操作。在 Relation 层，可以随便混杂资源的操作。

举个具体的例子，在我们的电商模型中，我们最常用的接口是返回商品 (goods) 信息，譬如：

```
api.com/goods
```

这会根据我们的需要自动地返回一些商品列表，如果我们需要根据不同品类、属性等进行查询，则只需要加一个查询参数等，譬如：

```
api.com/goods?requestData={category:1}&sort=rating
```

随着业务需求的进一步完善，我们现在需要返回用户的订单商品信息，这里我们假设有一个专门的订单表 (goods_order)，如果是按照传统的朴素的方式，可以为：

```
api.com/getGoodsInOrderByUserId?user_id=123
```

而如果按照 rest 的风格，可以定义为：

```
api.com/goods_order?requestData={user_id:123}
```

注意，无论是哪种 URL 定义方式都会映射到某个处理函数，但是很明显的 REST 风格的接口 (URI) 可读性会更好。但是虽然我们请求的是订单，但实际上返回的却是一个商品列表 ( 如果你认为商品列表与商品是属于同一种资源，那么请忽略这一部分 )。请求者最终想要的是一个包含商品详情的列表，而不是单纯的订单列表。因此，在对应的处理函数中，我们需要根据 user_id 查询到商品订单列表，然后根据商品订单列表查询到商品详情。这三者之间是一对多 ( 用户 - 商品订单 ) 以及一对一的关系 ( 商品订单 - 商品 )。

当然，我们可以在该处理函数中用一个简单的 SQL 连接查询即可获得想要的数据，一切看上去是如此的简单。接下来我们的业务逻辑更加复杂，添加了购物车、收藏夹等等功能，同样的，我们需要获得购物车中的商品列表、收藏夹中的商品列表。我们会选择定义不同的 URI，譬如：

```
api.com/getGoodsInCartByUserId?user_id=123
api.com/getGoodsInFavoriteByUserId?user_id=123
```

当然，也可以用等效的 REST 风格的接口，不过它们都是同样映射到某个处理函数，然后很简单的用一个 SQL 语句解决。一切还是很简单很正常，但是墨菲定理就来了。有一天如果整个系统需要重构，商品表发生了翻天覆地的变化，或者某个原有的需求发生了些许的改变。好一点的情况是还是当时那个开发人员，他清晰的知道每一条代码，并且能精准的进行每一行的改动，最终通过回归测试。

不过真实情况却是，即使是自己也不乐意去看那一大堆好久不碰的代码，或者换了个新人，压根不愿意去看前辈的代码 ( 这在小公司里非常常见，毕竟大家的代码风格都不一样 )。可能一个函数的命名方式都会让你纠结半天，毕竟命名时是如此的随意。最终，导致整个系统中充满了冗余代码，就好像炸弹一样，谁也不知道这段代码是干啥的，但是也不敢删除。整个依赖变得非常混乱。直到有一天受不了了，花费大量的时间进行业务逻辑重构。当然，老板肯定会在你重构完成之前告诉你，需求又改了。你还能咬他咋的？

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

### MicroServices( 微服务 )

近年来微服务概念的兴起也是为了进行这样的解耦合，只是微服务和 AARF 的类比可以有以下几点：

* 微服务和 AARF 在某种意义上都是推崇 SRP 原则，不过微服务是从传统的 SOA 架构衍变而来，而 AARF 是借鉴了以状态 / 数据为驱动的这样一种开发方式。
* 微服务更多的是在功能上的，面向于整体后台架构的解耦合。而 AARF 关注的是偏向于业务逻辑的组织方式。
* 微服务往往强调的是不同功能间的物理隔离，这是其与传统的巨石 (Monolith) 应用程序的一个很大的区别。而 AARF 依赖的是逻辑间的隔离，相对于功能领域会是一个更加抽象的概念。
* 微服务是去除了 ESB 的 SOA，即一种去中心化的分布式软件架构。而 AARF 面向每个具体业务而言的去 Controller 化的基于抽象资源流的架构风格。

AARF 与 MicroServices 的最终目标都是为了避免巨石应用程序的出现，最终形成一种分布式地灵活可拆卸的应用程序。某些方面来说，AARF 是实现 MicroServices 的一个手段。

实际上，现在也有很多微服务的设计模式上，在保证隔离性的基础上，以 Event-Driven 的方式来组合不同的服务并且管理这些分布式数据。

> [event-driven-data-management-microservices](https://www.nginx.com/blog/event-driven-data-management-microservices/)

![Each service in a microservice architecture maintains a private database table](https://www.nginx.com/wp-content/uploads/2015/12/Richardson-microservices-part5-separate-tables-e1449727641793.png)

![](https://www.nginx.com/wp-content/uploads/2015/12/Richardson-microservices-part5-credit-check-1-e1449727610972.png)

## REST & Software Architecture Style

在理想的情况下，每个开发者都能具有高度一致的编码风格与能力，并且对整个项目了如指掌，这样才能够快速地无冗余地根据业务需求来修改代码。并且辅助以大量的回归测试来保证代码的可用性，毕竟

Request per thread is an old beast and should be avoided as much as possible. It not only affects an application's scalability but can also lead to cascading failures.

> [Non-Blocking REST Services With Spring](https://dzone.com/articles/non-blocking-rest-services-with-spring)

## Flux & Redux

## Reactive Programming & Actor

在笔者的基于 Spring 框架的 AARF 的实践中，选用了 RxJava 作为 FRP 的实现库。不过实际上每个 ResourceHandler 非常类似于 Actor

Reactive programming is an emerging discipline which combines concurrency and event-based and asynchronous systems.Reactive programming can be seen as a natural extension of higher-order functional programming to concurrent systems that deal with distributed state by coordinating and orchestrating asynchronous data streams exchanged by actors.

Reactive Programming Principles (via manifesto)

* Responsive: The application should be quick to reacts to users, even under load and in the presence of failures
* Resilient and Scalable: The application should be resilient, in order to stay responsive under various conditions. They also should react to changes in the input rate by increasing or decreasing the resources allocated to service these inputs. Today’s applications have more integration complexity, as they are composed of multiple applications.
* Message Driven: A *message-driven*architecture is the foundation of scalable, resilient, and ultimately responsive systems.

**参考文献**

* [reactive-programming-using-rxjava-and-akka](https://www.linkedin.com/pulse/20141208023559-947775-reactive-programming-using-rxjava-and-akka)

## What's AARF?( 什么是 AARF)

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

  可变性包括对内的可变性与对外的可变性。

  #### extensibility

  #### [Reusability](undefined)

  Reusability is a property of an application architecture if its components, connectors, or data elements can be reused, without modification, in other applications. The primary mechanisms for inducing reusability within architectural styles is reduction of coupling (knowledge of identity) between components and constraining the generality of component interfaces. The uniform pipe-and-filter style exemplifies these types of constraints.

  ​

  * #### [Configurability](undefined)

    Configurability is related to both extensibility and reusability in that it refers to post-deployment modification of components, or configurations of components, such that they are capable of using a new service or data element type. The pipe-and-filter and code-on-demand styles are two examples that induce configurability of configurations and components, respectively.

  * ### [Visibility](undefined)

    Styles can also influence the visibility of interactions within a network-based application by restricting interfaces via generality or providing access to monitoring. Visibility in this case refers to the ability of a component to monitor or mediate the interaction between two other components. Visibility can enable improved performance via shared caching of interactions, scalability through layered services, reliability through reflective monitoring, and security by allowing the interactions to be inspected by mediators (e.g., network firewalls). The mobile agent style is an example where the lack of visibility may lead to security concerns.

* Scalability: 架构的可扩展性

  Scalability refers to the ability of the architecture to support large numbers of components, or interactions among components, within an active configuration. Scalability can be improved by simplifying components, by distributing services across many components (decentralizing the interactions), and by controlling interactions and configurations as a result of monitoring. Styles influence these factors by determining the location of application state, the extent of distribution, and the coupling between components.

  Scalability is also impacted by the frequency of interactions, whether the load on a component is distributed evenly over time or occurs in peaks, whether an interaction requires guaranteed delivery or a best-effort, whether a request involves synchronous or asynchronous handling, and whether the environment is controlled or anarchic (i.e., can you trust the other components?).

  ​

## Case Model( 案例模型 )

* User
* Book
* Comment

Relation

​ FriendShip

# Resource( 资源 )

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

## Attribute( 资源属性 )

* 资源唯一标识，譬如对于用户资源的唯一标识就是 user_id。
* 外键依赖，外键依赖是表征资源之间显性关系的特征。注意，任何一个资源的标识名具有全局唯一性，譬如 use_id，那么所有的资源中都应该叫 user_id，而不应该使用 uid、id 等等缩写或者别名。另一方面，可能某个资源中的两个外键依赖都指向 user_id，但是表示两个不同的含义。譬如如果我们需要表征用户之间的互相关注的行为，一个表中可能有两个 user_id，第一个表示关注者，第二个表示被关注者。那么在命名时务必保证前缀不变，即皆为 user_id，可以通过 by 关键字添加后缀的方式，即 user_id_by_following、user_id_by_followed 。
* 值属性

## Entity

不是很建议资源的嵌套，即将某整个其他的资源打包成某个资源的一个属性。

建议以使用基本类型加上 JSONObject、JSONArray 为主，笔者常使用的类型有：

String

Integer

JSONObject

JSONArray

Instant( 映射 TimeStamp 类型 )

LocalDateTime( 映射时间类型 )

## Model

在 Model 层的设计时，要注意做到逻辑无关性，而属性相关性。

#### Select

* 利用 foreach 构造多查询的 in 条件查询语句时候，注意容错

# Request & Response

>

## Uniform Resource Flow Path( 统一资源流动路径 )

### Resource Quantifier( 资源量词 )

量词分为 单个值、多个值 (JSONArray)、空值以及全部值 (all)。

preHandle 根据上一个资源，获取到本次所需要处理的资源编号列表。

all ，不一定就代表着全部。譬如

/user/{user_id}/book/all/comment

这边的 book 的 all，就应该被替换为多个值，即为隐性的多值。

换言之，所有与当前业务逻辑相关的处理，都放在 preHandle 的起始完成。这种多量词的一般出现在 GET 请求中，也是最复杂的请求之一。

## ResourceBag( 资源包 )

> 资源包是唯一可以跨资源流动的对象

### Immutable Object( 不可变对象 )

笔者觉得，没必要为每个 ResourceHandler 创建一个新的 ResourceBag，目前是暂时规定某个 ResourceHandler 尽可以操作 ResourceBag 中包含其对应的资源，而不可以操作其他资源。

## Advanced Request

### Resource Filter

包括条件查找、聚合操作、筛选操作

如果是聚合操作：aggreation

### Resource Integration( 资源注入 )

### MapReduce For Multiple Request Once( 单次多请求处理 )

## Non-Resourceful Response( 非资源化响应 )

```json
{
  "code": 404, //Required
  "desc": "Description Message", //Optional
  "subCode": "Sub Code to specific error" //Optional
}
```

### Code And HTTP Status( 响应码与 HTTP 状态 )

虽然 HTTP 协议为我们提供了大量的状态码，但是在实际的 API 开发中不建议用很多，毕竟前后端看着都很崩溃。一般来说，我们会建议使用以下五种状态码来表述不同的问题：

### Empty Resource Is Error( 空资源即为错误 )

# ResourceHandler( 资源处理 )

## Dispatcher

为了保证多线程下的安全性，要保证每个 ResourceHandler 的无状态性。

为了避免因为复杂的异步数据流而导致逻辑的混乱，做以下约定：

( 1)在 route 函数中的主逻辑步骤只允许同步执行，譬如常常要进行推送。如果推送会影响到本次资源流的结果，则同步执行。否则异步执行。

( 2)在 route 方法中，不可以调用 onNext、onError 以及 onComplete 方法

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

# Relation( 混合关联层 )

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

## Authority Control( 权限控制 )

### JWT Based User Authentic( 用户认证 )

笔者在最初设计 AARF 的风格时犯了一个很大的错误，也是为了兼容旧的系统。将请求认证，即发起该请求的用户认证与用户资源的获取混为一谈。譬如对于以下的请求：

```
/user/{user_token}
```

这样一种直接把用户令牌与用户资源的唯一标识混为一谈的方式就是典型的错误。笔者比较推出基于 JWT 的用户认证。

基于 SQL 级别的控制

错误还是空？譬如用户请求一个不存在的商品，这是一个错误吗？请求一个不存在的资源，特别是在发生资源串联时，因此，在 Get 中，如果请求到的资源为空，则报错。

## Data Verification( 数据验证 )

数据验证一个是数据格式的验证，包括

请求数据格式的验证，放在 Model 中

返回数据格式的 Json 化

# Deferred SQL

笔者在文章开头就提及 AARF 可能面临的为了保证资源之间的隔离性而导致的 SQL 的冗余查询问题。Deferred SQL 的理念来自于异步编程中的 Promise 或者 Java 中的 Future 的概念。

# Optimization( 性能优化 )
