<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Introduction](#introduction)
  - [Terminology:名词解释](#terminology%E5%90%8D%E8%AF%8D%E8%A7%A3%E9%87%8A)
    - [Passive Module & Reactive Module](#passive-module-&-reactive-module)
    - [Declarative vs. Imperative:命令式编程与声明式编程](#declarative-vs-imperative%E5%91%BD%E4%BB%A4%E5%BC%8F%E7%BC%96%E7%A8%8B%E4%B8%8E%E5%A3%B0%E6%98%8E%E5%BC%8F%E7%BC%96%E7%A8%8B)
  - [谈到架构，我们关心哪些方面？](#%E8%B0%88%E5%88%B0%E6%9E%B6%E6%9E%84%EF%BC%8C%E6%88%91%E4%BB%AC%E5%85%B3%E5%BF%83%E5%93%AA%E4%BA%9B%E6%96%B9%E9%9D%A2%EF%BC%9F)
    - [分久必合，合久必分](#%E5%88%86%E4%B9%85%E5%BF%85%E5%90%88%EF%BC%8C%E5%90%88%E4%B9%85%E5%BF%85%E5%88%86)
    - [功能的模块化](#%E5%8A%9F%E8%83%BD%E7%9A%84%E6%A8%A1%E5%9D%97%E5%8C%96)
    - [界面的组件化](#%E7%95%8C%E9%9D%A2%E7%9A%84%E7%BB%84%E4%BB%B6%E5%8C%96)
      - [无状态的组件](#%E6%97%A0%E7%8A%B6%E6%80%81%E7%9A%84%E7%BB%84%E4%BB%B6)
    - [状态管理](#%E7%8A%B6%E6%80%81%E7%AE%A1%E7%90%86)
  - [Features of Good Architectural Pattern:何为好的架构模式](#features-of-good-architectural-pattern%E4%BD%95%E4%B8%BA%E5%A5%BD%E7%9A%84%E6%9E%B6%E6%9E%84%E6%A8%A1%E5%BC%8F)
    - [Balanced Distribution of Responsibilities:合理的职责划分](#balanced-distribution-of-responsibilities%E5%90%88%E7%90%86%E7%9A%84%E8%81%8C%E8%B4%A3%E5%88%92%E5%88%86)
    - [Testability:可测试性](#testability%E5%8F%AF%E6%B5%8B%E8%AF%95%E6%80%A7)
    - [Ease of Use:易用性](#ease-of-use%E6%98%93%E7%94%A8%E6%80%A7)
    - [Fractal:碎片化，易于封装与分发](#fractal%E7%A2%8E%E7%89%87%E5%8C%96%EF%BC%8C%E6%98%93%E4%BA%8E%E5%B0%81%E8%A3%85%E4%B8%8E%E5%88%86%E5%8F%91)
  - [Reference](#reference)
    - [Overview](#overview)
    - [MV*](#mv)
      - [MVC](#mvc)
      - [MVP](#mvp)
      - [MVVM](#mvvm)
    - [Unidirectional Architecture](#unidirectional-architecture)
    - [Viper/Clean Architecture](#viperclean-architecture)
- [MV*:Fragmentary State 碎片化的状态与双向数据流](#mvfragmentary-state-%E7%A2%8E%E7%89%87%E5%8C%96%E7%9A%84%E7%8A%B6%E6%80%81%E4%B8%8E%E5%8F%8C%E5%90%91%E6%95%B0%E6%8D%AE%E6%B5%81)
  - [MVC:Monolithic Controller](#mvcmonolithic-controller)
    - [Observer Pattern:自带观察者模式的MVC](#observer-pattern%E8%87%AA%E5%B8%A6%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F%E7%9A%84mvc)
  - [MVP:Decoupling View and Model 将视图与模型解耦, View<->Presenter](#mvpdecoupling-view-and-model-%E5%B0%86%E8%A7%86%E5%9B%BE%E4%B8%8E%E6%A8%A1%E5%9E%8B%E8%A7%A3%E8%80%A6-view-presenter)
    - [Supervising Controller MVP](#supervising-controller-mvp)
  - [MVVM:Data Binding & Stateless View 数据绑定与无状态的View，View<->ViewModels](#mvvmdata-binding-&-stateless-view-%E6%95%B0%E6%8D%AE%E7%BB%91%E5%AE%9A%E4%B8%8E%E6%97%A0%E7%8A%B6%E6%80%81%E7%9A%84view%EF%BC%8Cview-viewmodels)
  - [MV* in iOS](#mv-in-ios)
    - [MVC](#mvc-1)
    - [MVP](#mvp-1)
    - [MVVM](#mvvm-1)
  - [MV* in Android](#mv-in-android)
    - [MVC](#mvc-2)
    - [MVP](#mvp-2)
    - [MVVM](#mvvm-2)
- [Unidirectional User Interface Architecture:单向数据流](#unidirectional-user-interface-architecture%E5%8D%95%E5%90%91%E6%95%B0%E6%8D%AE%E6%B5%81)
  - [Why not Bidirectional(Two-way DataBinding)?](#why-not-bidirectionaltwo-way-databinding)
  - [Flux:数据流驱动的页面](#flux%E6%95%B0%E6%8D%AE%E6%B5%81%E9%A9%B1%E5%8A%A8%E7%9A%84%E9%A1%B5%E9%9D%A2)
  - [Redux:集中式的状态管理](#redux%E9%9B%86%E4%B8%AD%E5%BC%8F%E7%9A%84%E7%8A%B6%E6%80%81%E7%AE%A1%E7%90%86)
  - [Model-View-Update](#model-view-update)
  - [Model-View-Intent](#model-view-intent)
- [Clean Architecture:尚文完成，请勿转载](#clean-architecture%E5%B0%9A%E6%96%87%E5%AE%8C%E6%88%90%EF%BC%8C%E8%AF%B7%E5%8B%BF%E8%BD%AC%E8%BD%BD)
  - [iOS Viper Architecture](#ios-viper-architecture)
  - [Android Clean Architecture](#android-clean-architecture)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->



> 十年前，Martin Fowler撰写了[GUI Architectures](http://martinfowler.com/eaaDev/uiArchs.html)一文，至今被奉为经典。本文所谈的所谓架构二字，核心即是对于对于富客户端的**代码组织/职责划分**。纵览这十年内的架构模式变迁，大概可以分为MV\*与Unidirectional两大类，而Clean Architecture则是以严格的层次划分独辟蹊径。从笔者的认知来看，从MVC到MVP的变迁完成了对于View与Model的解耦合，改进了职责分配与可测试性。而从MVP到MVVM，添加了View与ViewModel之间的数据绑定，使得View完全的无状态化。最后，整个从MV\*到Unidirectional的变迁即是采用了消息队列式的数据流驱动的架构，并且以Redux为代表的方案将原本MV\*中碎片化的状态管理变为了统一的状态管理，保证了状态的有序性与可回溯性。

> 笔者在撰写本文的时候也不可避免的带了很多自己的观点，在漫长的GUI架构模式变迁过程中，很多概念其实是交错复杂，典型的譬如MVP与MVVM的区别，笔者按照自己的理解强行定义了二者的区分边界，不可避免的带着自己的主观想法。另外，鉴于笔者目前主要进行的是Web方面的开发，因此在整体倾向上是支持Unidirectional Architecture并且认为集中式的状态管理是正确的方向。但是必须要强调，GUI架构本身是无法脱离其所依托的平台，下文笔者也会浅述由于Android与iOS本身SDK API的特殊性，生搬硬套其他平台的架构模式也是邯郸学步，沐猴而冠。不过总结而言，它山之石，可以攻玉，本身我们所处的开发环境一直在不断变化，对于过去的精华自当应该保留，并且与新的环境相互印证，触类旁通。



# Introduction

> **Make everything as simple as possible, but not simpler — Albert Einstein**



Graphical User Interfaces一直是软件开发领域的重要组成部分，从当年的MFC，到WinForm/Java Swing，再到WebAPP/Android/iOS引领的智能设备潮流，以及未来可能的AR/VR，GUI应用开发中所面临的问题一直在不断演变，但是从各种具体问题中抽象而出的可以复用的模式恒久存在。而这些模式也就是所谓应用架构的核心与基础。对于所谓应用架构，空谈误事，不谈误己，笔者相信不仅仅只有自己想把那一团糟的代码给彻底抛弃。往往对于架构的认知需要一定的大局观与格局眼光，每个有一定经验的客户端程序开发者，无论是Web、iOS还是Android，都会有自己熟悉的开发流程习惯，但是笔者认为架构认知更多的是道，而非术。当你能够以一种指导思想在不同的平台上能够进行高效地开发时，你才能真正理解架构。这个有点像张三丰学武，心中无招，方才达成。笔者这么说只是为了强调，尽量地可以不拘泥于某个平台的具体实现去审视GUI应用程序架构模式，会让你有不一样的体验。譬如下面这个组装Android机器人的图：

![](http://luboganev.github.io/images/2015-07-21-clean-architecture-pt1/android_anatomy.jpg)

怎么去焊接两个组件，属于具体的术实现，而应该焊接哪两个组件就是术，作为合格的架构师总不能把脚和头直接焊接在一起，而忽略中间的连接模块。对于软件开发中任何一个方面，我们都希望能够寻找到一个抽象程度适中，能够在接下来的4，5年内正常运行与方便维护扩展的开发模式。引申下笔者在[我的编程之路](https://segmentfault.com/a/1190000004292245)中的论述，目前在GUI架构模式中，无论是Android、iOS还是Web，都在经历着从命令式编程到声明式/响应式编程，从Passive Components到Reactive Components，从以元素操作为核心到以数据流驱动为核心的变迁(关于这几句话的解释可以参阅下文的Declarative vs. Imperative这一小节)。





## Terminology:名词解释

正文之前，我们先对一些概念进行阐述：

- User Events/用户事件:即是来自于可输入设备上的用户操作产生的数据，譬如鼠标点击、滚动、键盘输入、触摸等等。

- User Interface Rendering/用户界面渲染:View这个名词在前后端开发中都被广泛使用，为了明晰该词的含义，我们在这里使用用户渲染这个概念，来描述View，即是以HTML或者JSX或者XAML等等方式在屏幕上产生的图形化输出内容。

- UI Application:允许接收用户输入，并且将输出渲染到屏幕上的应用程序，该程序能够长期运行而不只是渲染一次即结束



### Passive Module & Reactive Module

箭头表示的归属权实际上也是Passive Programming与Reactive Programming的区别，譬如我们的系统中有Foo与Bar两个模块，可以把它们当做OOP中的两个类。如果我们在Foo与Bar之间建立一个箭头，也就意味着Foo能够影响Bar中的状态:

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/5F14C7A2-9E84-40F4-BC6B-50FDAE97CB25.png)

譬如Foo在进行一次网络请求之后将Bar内部的计数器加一操作：

```

// This is inside the Foo module

function onNetworkRequest() {
  // ...
  Bar.incrementCounter();
  // ...
}
```

在这里将这种逻辑关系可以描述为Foo拥有着`网络请求完成之后将Bar内的计数器加一`这个关系的控制权，也就是Foo占有主导性，而Bar相对而言是Passive被动的：

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/0790C8B2-0869-4468-9EF6-AA5DF9B2852E.png)

Bar是Passive的，它允许其他模块改变其内部状态。而Foo是主动地，它需要保证能够正确地更新Bar的内部状态，Passive模块并不知道谁会更新到它。而另一种方案就是类似于控制反转，由Bar完成对于自己内部状态的更新：

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/A17B8365-2A31-4F74-8B0A-8A3207F116AC.png)

在这种模式下，Bar监听来自于Foo中的事件，并且在某些事件发生之后进行内部状态更新:

```

// This is inside the Bar module

Foo.addOnNetworkRequestListener(() => {

  self.incrementCounter(); // self is Bar

});

```

此时Bar就变成了Reactive Module，它负责自己的内部的状态更新以响应外部的事件，而Foo并不知道它发出的事件会被谁监听。



### Declarative vs. Imperative:命令式编程与声明式编程

> [three-ds-of-web-development](http://developer.telerik.com/featured/three-ds-of-web-development-1-declarative-vs-imperative/)

> [前端攻略-从路人甲到英雄无敌二：JavaScript 与不断演化的框架](https://segmentfault.com/a/1190000005353213#articleHeader9)



形象地来描述命令式编程与声明式编程的区别，就好像C#/JavaScript与类似于XML或者HTML这样的标记语言之间的区别。命令式编程关注于`how to do what you want done`，即事必躬亲，需要安排好每个要做的细节。而声明式编程关注于`what you want done without worrying about how`，即只需要声明要做的事情而不用将具体的过程再耦合进来。对于开发者而言，声明式编程将很多底层的实现细节向开发者隐藏，而使得开发者可以专注于具体的业务逻辑，同时也保证了代码的解耦与单一职责。譬如在Web开发中，如果你要基于jQuery将数据填充到页面上，那么大概按照命令式编程的模式你需要这么做：

```

var options = $("#options");
$.each(result, function() {
    options.append($("<option />").val(this.id).text(this.name));
});
```

而以Angular 1声明式的方式进行编写，那么是如下的标记模样：

```

<div ng-repeat="item in items" ng-click="select(item)">{{item.name}}
</div>
```

而在iOS和Android开发中，近年来函数响应式编程(Functional Reactive Programming)也非常流行，参阅笔者关于响应式编程的介绍可以了解，响应式编程本身是基于流的方式对于异步操作的一种编程优化，其在整个应用架构的角度看更多的是细节点的优化。以[RxSwift](https://github.com/ReactiveX/RxSwift)为例，通过响应式编程可以编写出非常优雅的用户交互代码：

```

let searchResults = searchBar.rx_text
    .throttle(0.3, scheduler: MainScheduler.instance)
    .distinctUntilChanged()
    .flatMapLatest { query -> Observable<[Repository]> in
        if query.isEmpty {
            return Observable.just([])
        }

        return searchGitHub(query)
            .catchErrorJustReturn([])
    }
    .observeOn(MainScheduler.instance)
searchResults
    .bindTo(tableView.rx_itemsWithCellIdentifier("Cell")) {
        (index, repository: Repository, cell) in
        cell.textLabel?.text = repository.name
        cell.detailTextLabel?.text = repository.url
    }
    .addDisposableTo(disposeBag)
```

其直观的效果大概如下图所示：

![](https://raw.githubusercontent.com/kzaher/rxswiftcontent/master/GithubSearch.gif)

到这里可以看出，无论是从命令式编程与声明式编程的对比还是响应式编程的使用，我们开发时的关注点都慢慢转向了所谓的数据流。便如MVVM，虽然它还是双向数据流，但是其使用的Data-Binding也意味着开发人员不需要再去以命令地方式寻找元素，而更多地关注于应该给绑定的对象赋予何值，这也是数据流驱动的一个重要体现。而Unidirectional Architecture采用了类似于Event Source的方式，更是彻底地将组件之间、组件与功能模块之间的关联交于数据流操控。



## 谈到架构，我们关心哪些方面？

当我们谈论所谓客户端开发的时候，我们首先会想到怎么保证向后兼容、怎么使用本地存储、怎么调用远程接口、如何有效地利用内存/带宽/CPU等资源，不过最核心的还是怎么绘制界面并且与用户进行交互，关于这部分详细的知识点纲要推荐参考笔者的[我的编程之路——知识管理与知识体系](https://segmentfault.com/a/1190000004612590#articleHeader3)这篇文章或者[这张知识点列表思维脑图](https://github.com/wxyyxc1992/Coder-Knowledge-Graph/blob/master/client/client.all.png)。

![](https://github.com/wxyyxc1992/Coder-Knowledge-Graph/blob/master/client/client.all.png?raw=true)





而当我们提纲挈领、高屋建瓴地以一个较高的抽象的视角来审视总结这个知识点的时候会发现，我们希望的好的架构，便如在引言中所说，即是有好的代码组织方式/合理的职责划分粒度。笔者脑中会出现如下这样的一个层次结构，可以看出，最核心的即为View与ViewLogic这两部分：

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/1/27259D5F-DF43-455C-8BBB-D4BC81E1C865.png)





实际上，对于富客户端的**代码组织/职责划分**，从具体的代码分割的角度，即是**功能的模块化**、**界面的组件化**、**状态管理**这三个方面。最终呈献给用户的界面，笔者认为可以抽象为如下等式：```View = f(State,Template)```。而ViewLogic中对于类/模块之间的依赖关系，即属于代码组织，譬如MVC中的View与Controller之间的从属关系。而对于动态数据，即所谓应用数据的管理，属于状态管理这一部分，譬如APP从后来获取了一系列的数据，如何将这些数据渲染到用户界面上使得用户可见，这样的不同部分之间的协同关系、整个数据流的流动，即属于状态管理。



### 分久必合，合久必分

实际上从MVC、MVP到MVVM，一直围绕的核心问题就是如何分割ViewLogic与View，即如何将负责界面展示的代码与负责业务逻辑的代码进行分割。所谓分久必合，合久必分，从笔者自我审视的角度，发现很有趣的一点。Android与iOS中都是从早期的用代码进行组件添加与布局到专门的XML/Nib/StoryBoard文件进行布局，Android中的Annotation/DataBinding、iOS中的IBOutlet更加地保证了View与ViewLogic的分割(这一点也是从元素操作到以数据流驱动的变迁，我们不需要再去编写大量的`findViewById`)。而Web的趋势正好有点相反，无论是WebComponent还是ReactiveComponent都是将ViewLogic与View置于一起，特别是JSX的语法将JavaScript与HTML混搭，很像当年的PHP/JSP与HTML混搭。这一点也是由笔者在上文提及的Android/iOS本身封装程度较高的、规范的API决定的。对于Android/iOS与Web之间开发体验的差异，笔者感觉很类似于静态类型语言与动态类型语言之间的差异。





### 功能的模块化

老实说在AMD/CMD规范之前，或者说在ES6的模块引入与Webpack的模块打包出来之前，功能的模块化依赖一直也是个很头疼的问题。

SOLID中的接口隔离原则，大量的IOC或者DI工具可以帮我们完成这一点，就好像Spring中的@Autowire或者Angular 1中的@Injection，都给笔者很好地代码体验。

在这里笔者首先要强调下，从代码组织的角度来看，项目的构建工具与依赖管理工具会深刻地影响到代码组织，这一点在功能的模块化中尤其显著。譬如笔者对于Android/Java构建工具的使用变迁经历了从Eclipse到Maven再到Gradle，笔者会将不同功能逻辑的代码封装到不同的相对独立的子项目中，这样就保证了子项目与主项目之间的一定隔离，方便了测试与代码维护。同样的，在Web开发中从AMD/CMD规范到标准的ES6模块与Webpack编译打包，也使得代码能够按照功能尽可能地解耦分割与避免冗余编码。而另一方面，依赖管理工具也极大地方便我们使用第三方的代码与发布自定义的依赖项，譬如Web中的NPM与Bower，iOS中的CocoaPods都是十分优秀的依赖发布与管理工具，使我们不需要去关心第三方依赖的具体实现细节即能够透明地引入使用。因此选择合适的项目构建工具与依赖管理工具也是好的GUI架构模式的重要因素之一。不过从应用程序架构的角度看，无论我们使用怎样的构建工具，都可以实现或者遵循某种架构模式，笔者认为二者之间也并没有必然的因果关系。

### 界面的组件化

> A component is a small piece of the user interface of our application, a view, that can be composed with other components to make more advanced components.



何谓组件？一个组件即是应用中用户交互界面的部分组成，组件可以通过组合封装成更高级的组件。组件可以被放入层次化的结构中，即可以是其他组件的父组件也可以是其他组件的子组件。根据上述的组件定义，笔者认为像Activity或者UIViewController都不能算是组件，而像ListView或者UITableView可以看做典型的组件。

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/1/1-k33db-Hx1gRRSxR_LMprMQ.png)

我们强调的是界面组件的Composable&Reusable，即可组合性与可重用性。当我们一开始接触到Android或者iOS时，因为本身SDK的完善度与规范度较高，我们能够很多使用封装程度较高的组件。譬如ListView，无论是Android中的RecycleView还是iOS中的UITableView或者UICollectionView，都为我们提供了。凡事都有双面性，这种较高程度的封装与规范统一的API方便了我们的开发，但是也限制了我们自定义的能力。同样的，因为SDK的限制，真正意义上可复用/组合的组件也是不多，譬如你不能将两个ListView再组合成一个新的ListView。在React中有所谓的controller-view的概念，即意味着某个React组件同时担负起MVC中Controller与View的责任，也就是JSX这种将负责ViewLogic的JavaScript代码与负责模板的HTML混编的方式。

界面的组件化还包括一个重要的点就是路由，譬如Android中的[AndRouter](https://github.com/campusappcn/AndRouter)、iOS中的[JLRoutes](https://github.com/joeldev/JLRoutes)都是集中式路由的解决方案，不过集中式路由在Android或者iOS中并没有大规模推广。iOS中的StoryBoard倒是类似于一种集中式路由的方案，不过更偏向于以UI设计为核心。笔者认为这一点可能是因为Android或者iOS本身所有的代码都是存放于客户端本身，而Web中较传统的多页应用方式还需要用户跳转页面重新加载，而后在单页流行之后即不存在页面级别的跳转，因此在Web单页应用中集中式路由较为流行而Android、iOS中反而不流行。



#### 无状态的组件

无状态的组件的构建函数是纯函数(pure function)并且引用透明的(refferentially transparent)，在相同输入的情况下一定会产生相同的组件输出，即符合```View = f(State,Template)```公式。笔者觉得Android中的ListView/RecycleView，或者iOS中的UITableView，也是无状态组件的典型。譬如在Android中，可以通过动态设置Adapter实例来为RecycleView进行源数据的设置，而作为View层以IoC的方式与具体的数据逻辑解耦。

组件的可组合性与可重用性往往最大的阻碍就是状态，一般来说，我们希望能够重用或者组合的组件都是

Generalization，而状态往往是Specification，即领域特定的。同时，状态也会使得代码的可读性与可测试性降低，在有状态的组件中，我们并不能通过简单地阅读代码就知道其功能。如果借用函数式编程的概念，就是因为副作用的引入使得函数每次回产生不同的结果。函数式编程中存在着所谓Pure Function，即纯函数的概念，函数的返回值永远只受到输入参数的影响。譬如`(x)=>x*2`这个函数，输入的x值永远不会被改变，并且返回值只是依赖于输入的参数。而Web开发中我们也经常会处于带有状态与副作用的环境，典型的就是Browser中的DOM，之前在jQuery时代我们会经常将一些数据信息缓存在DOM树上，也是典型的将状态与模板混合的用法。这就导致了我们并不能控制到底应该何时去进行重新渲染以及哪些状态变更的操作才是必须的，

```

var Header = component(function (data) {
  // First argument is h1 metadata
  return h1(null, data.text);
});

// Render the component to our DOM
render(Header({text: 'Hello'}), document.body);

// Some time later, we change it, by calling the
// component once more.
setTimeout(function () {
  render(Header({text: 'Changed'}), document.body);
}, 1000);
```

```

var hello = Header({ text: 'Hello' }); var bye   = Header({ text: 'Good Bye' });

```



### 状态管理

> 可变的与不可预测的状态是软件开发中的万恶之源

> - [Web开发中所谓状态浅析:Domain State&UI State](https://github.com/wxyyxc1992/web-frontend-practice-handbook/blob/master/advanced/engineering/state/domain-state-vs-ui-state.md)



上文提及，我们尽可能地希望组件的无状态性，那么整个应用中的状态管理应该尽量地放置在所谓High-Order Component或者Smart Component中。在React以及Flux的概念流行之后，Stateless Component的概念深入人心，不过其实对于MVVM中的View，也是无状态的View。通过双向数据绑定将界面上的某个元素与ViewModel中的变量相关联，笔者认为很类似于HOC模式中的Container与Component之间的关联。随着应用的界面与功能的扩展，状态管理会变得愈发混乱。这一点，无论前后端都有异曲同工之难，笔者在[基于Redux思想与RxJava的SpringMVC中Controller的代码风格实践](https://segmentfault.com/a/1190000005039478)一文中对于服务端应用程序开发中的状态管理有过些许讨论。







## Features of Good Architectural Pattern:何为好的架构模式

### Balanced Distribution of Responsibilities:合理的职责划分

合理的职责划分即是保证系统中的不同组件能够被分配合理的职责，也就是在复杂度之间达成一个平衡，职责划分最权威的原则就是所谓Single Responsibility Principle，单一职责原则。



### Testability:可测试性

可测试性是保证软件工程质量的重要手段之一，也是保证产品可用性的重要途径。在传统的GUI程序开发中，特别是对于界面的测试常常设置于状态或者运行环境，并且很多与用户交互相关的测试很难进行场景重现，或者需要大量的人工操作去模拟真实环境。



### Ease of Use:易用性

代码的易用性保证了程序架构的简洁与可维护性，所谓最好的代码就是永远不需要重写的代码，而程序开发中尽量避免的代码复用方法就是复制粘贴。



### Fractal:碎片化，易于封装与分发

> In fractal architectures, the whole can be naively packaged as a component to be used in some larger application.In non-fractal architectures, the non-repeatable parts are said to be orchestrators over the parts that have hierarchical composition.

> - By André Staltz



所谓的Fractal Architectures，即你的应用整体都可以像单个组件一样可以方便地进行打包然后应用到其他项目中。而在Non-Fractal Architectures中，不可以被重复使用的部分被称为层次化组合中的Orchestrators。譬如你在Web中编写了一个登录表单，其中的布局、样式等部分可以被直接复用，而提交表单这个操作，因为具有应用特定性，因此需要在不同的应用中具有不同的实现。譬如下面有一个简单的表单：

```

<form action="form_action.asp" method="get">
  <p>First name: <input type="text" name="fname" /></p>
  <p>Last name: <input type="text" name="lname" /></p>
  <input type="submit" value="Submit" />
</form>
```

因为不同的应用中，form的提交地址可能不一致，那么整个form组件是不可直接重用的，即Non-Fractal Architectures。而form中的`input`组件是可以进行直接复用的，如果将`input`看做一个单独的GUI架构，即是所谓的Fractal Architectures，form就是所谓的Orchestrators，将可重用的组件编排组合，并且设置应用特定的一些信息。



## Reference

### Overview

- [Martin Fowler-GUI Architectures](http://martinfowler.com/eaaDev/uiArchs.html)

- [Comparison-of-Architecture-presentation-patterns](http://www.codeproject.com/Articles/66585/Comparison-of-Architecture-presentation-patterns-M)

### MV*



- [THE EVOLUTION OF ANDROID ARCHITECTURE](http://zserge.com/blog/android-mvp-mvvm-redux-history.html)

- [the-evolution-of-android-architecture](https://medium.com/@trikita/the-evolution-of-android-architecture-6c6f04fc1927#.uuk4iuh9e)

- [android-architecture](https://medium.com/android-news/android-architecture-2f12e1c7d4db#.vzmxahsi0)

- [ios-architecture-patterns](https://medium.com/ios-os-x-development/ios-architecture-patterns-ecba4c38de52#.iy9umjlqa)

- [Albert Zuurbier:MVC VS. MVP VS. MVVM](http://www.albertzuurbier.com/index.php/programming/84-mvc-vs-mvp-vs-mvvm)

#### MVC

- [Model-View-Controller (MVC) in iOS: A Modern Approach](https://www.raywenderlich.com/132662/mvc-in-ios-a-modern-approach)

- [为什么我不再使用MVC框架](http://www.infoq.com/cn/articles/no-more-mvc-frameworks?utm_source=infoq_en&utm_medium=link_on_en_item&utm_campaign=item_in_other_langs)

- [difference-between-mvc-mvp-mvvm-swapneel-salunkhe](https://www.linkedin.com/pulse/difference-between-mvc-mvp-mvvm-swapneel-salunkhe)

#### MVP

- [presentation-model-and-passive-view-in-mvp-the-android-way](https://medium.com/@andrzejchm/presentation-model-and-passive-view-in-mvp-the-android-way-fdba56a35b1e#.tgz1fwb6y)

- [Repository that showcases 3 Android app architectures](https://github.com/ivacf/archi)

#### MVVM

- [approaching-android-with-mvvm](https://labs.ribot.co.uk/approaching-android-with-mvvm-8ceec02d5442#.lmbtfveih)



### Unidirectional Architecture

- [unidirectional-user-interface-architectures](http://staltz.com/unidirectional-user-interface-architectures.html)

- [Facebook: MVC Does Not Scale, Use Flux Instead [Updated]](https://www.infoq.com/news/2014/05/facebook-mvc-flux)

- [mvvm-mvc-is-dead-is-unidirectional-a-mvvm-mvc-killer](http://www.michaelridland.com/xamarin/mvvm-mvc-is-dead-is-unidirectional-a-mvvm-mvc-killer/)

- [flux-vs-mvc-design-patterns](https://medium.com/hacking-and-gonzo/flux-vs-mvc-design-patterns-57b28c0f71b7#.p0h9ih5zj)

- [jedux](https://github.com/trikita/jedux):Redux architecture for Android

- [writing-a-todo-app-with-redux-on-android](https://medium.com/@trikita/writing-a-todo-app-with-redux-on-android-5de31cfbdb4f#.tku4k1n0o)

- [state-streams-and-react](https://medium.com/@markusctz/state-streams-and-react-7921e3c376a4#.dtrx0ep4j)

### Viper/Clean Architecture

- [Uncle Bob:the-clean-architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)

- [Android Clean Architecture](http://luboganev.github.io/blog/clean-architecture-pt1/)

- [A sample iOS app built using the Clean Swift architecture](https://github.com/Clean-Swift/CleanStore)

- [Introduction to VIPER](http://mutualmobile.github.io/blog/2013/12/04/viper-introduction/)



# MV*:Fragmentary State 碎片化的状态与双向数据流

MVC模式将有关于渲染、控制与数据存储的概念有机分割，是GUI应用架构模式的一个巨大成就。但是，MVC模式在构建能够长期运行、维护、有效扩展的应用程序时遇到了极大的问题。MVC模式在一些小型项目或者简单的界面上仍旧有极大的可用性，但是在现代富客户端开发中导致职责分割不明确、功能模块重用性、View的组合性较差。作为继任者MVP模式分割了View与Model之间的直接关联，MVP模式中也将更多的ViewLogic转移到Presenter中进行实现，从而保证了View的可测试性。而最年轻的MVVM将ViewLogic与View剥离开来，保证了View的无状态性、可重用性、可组合性以及可测试性。总结而言，MV*模型都包含了以下几个方面:

- Models:负责存储领域/业务逻辑相关的数据与构建数据访问层，典型的就是譬如`Person`、`PersonDataProvider`。

- Views:负责将数据渲染展示给用户，并且响应用户输入

- Controller/Presenter/ViewModel:往往作为Model与View之间的中间人出现，接收View传来的用户事件并且传递给Model，同时利用从Model传来的最新模型控制更新View



## MVC:Monolithic Controller

相信每一个程序猿都会宣称自己掌握MVC，这个概念浅显易懂，并且贯穿了从GUI应用到服务端应用程序。MVC的概念源自Gamma, Helm, Johnson 以及Vlissidis这四人帮在讨论设计模式中的Observer模式时的想法，不过在那本经典的设计模式中并没有显式地提出这个概念。我们通常认为的MVC名词的正式提出是在1979年5月Trygve Reenskaug发表的Thing-Model-View-Editor这篇论文，这篇论文虽然并没有提及Controller，但是Editor已经是一个很接近的概念。大概7个月之后，Trygve Reenskaug在他的文章Models-Views-Controllers中正式提出了MVC这个三元组。上面两篇论文中对于Model的定义都非常清晰，Model代表着`an abstraction in the form of data in a computing system.`，即为计算系统中数据的抽象表述，而View代表着`capable of showing one or more pictorial representations of the Model on screen and on hardcopy.`，即能够将模型中的数据以某种方式表现在屏幕上的组件。而Editor被定义为某个用户与多个View之间的交互接口，在后一篇文章中Controller则被定义为了`a special controller ... that permits the user to modify the information that is presented by the view.`，即主要负责对模型进行修改并且最终呈现在界面上。从我的个人理解来看，Controller负责控制整个界面，而Editor只负责界面中的某个部分。Controller协调菜单、面板以及像鼠标点击、移动、手势等等很多的不同功能的模块，而Editor更多的只是负责某个特定的任务。后来，Martin Fowler在2003开始编写的著作Patterns of Enterprise Application Architecture中重申了MVC的意义：`Model View Controller (MVC) is one of the most quoted (and most misquoted) patterns around.`，将Controller的功能正式定义为：响应用户操作，控制模型进行相应更新，并且操作页面进行合适的重渲染。这是非常经典、狭义的MVC定义，后来在iOS以及其他很多领域实际上运用的MVC都已经被扩展或者赋予了新的功能，不过笔者为了区分架构演化之间的区别，在本文中仅会以这种最朴素的定义方式来描述MVC。

根据上述定义，我们可以看到MVC模式中典型的用户场景为：

- 用户交互输入了某些内容

- Controller将用户输入转化为Model所需要进行的更改

- Model中的更改结束之后，Controller通知View进行更新以表现出当前Model的状态



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/B24AC2FE-EC59-48AA-B94F-417AC73D9886.png)



根据上述流程，我们可知经典的MVC模式的特性为：

- View、Controller、Model中皆有ViewLogic的部分实现

- Controller负责控制View与Model，需要了解View与Model的细节。

- View需要了解Controller与Model的细节，需要在侦测用户行为之后调用Controller，并且在收到通知后调用Model以获取最新数据

- Model并不需要了解Controller与View的细节，相对独立的模块

### Observer Pattern:自带观察者模式的MVC

上文中也已提及，MVC滥觞于Observer模式，经典的MVC模式也可以与Observer模式相结合，其典型的用户流程为：

- 用户交互输入了某些内容

- Controller将用户输入转化为Model所需要进行的更改

- View作为Observer会监听Model中的任意更新，一旦有更新事件发出，View会自动触发更新以展示最新的Model状态



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/565313A4-6639-468A-9E27-FE9E126CEFA2.png)

可知其与经典的MVC模式区别在于不需要Controller通知View进行更新，而是由Model主动调用View进行更新。这种改变提升了整体效率，简化了Controller的功能，不过也导致了View与Model之间的紧耦合。



## MVP:Decoupling View and Model 将视图与模型解耦, View<->Presenter

维基百科将[MVP](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)称为MVC的一个推导扩展，观其渊源而知其所以然。对于MVP概念的定义，Microsoft较为明晰，而Martin Fowler的定义最为广泛接受。MVP模式在WinForm系列以Visual-XXX命名的编程语言与Java Swing等系列应用中最早流传开来，不过后来ASP.NET以及JFaces也广泛地使用了该模式。在MVP中用户不再与Presenter进行直接交互，而是由View完全接管了用户交互，譬如窗口上的每个控件都知道如何响应用户输入并且合适地渲染来自于Model的数据。而所有的事件会被传输给Presenter，Presenter在这里就是View与Model之间的中间人，负责控制Model进行修改以及将最新的Model状态传递给View。这里描述的就是典型的所谓Passive View版本的MVP，其典型的用户场景为：

- 用户交互输入了某些内容

- View将用户输入转化为发送给Presenter

- Presenter控制Model接收需要改变的点

- Model将更新之后的值返回给Presenter

- Presenter将更新之后的模型返回给View



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/28983226-1BC6-4AD2-900B-E7D254266D4F.png)

根据上述流程，我们可知Passive View版本的MVP模式的特性为：

- View、Presenter、Model中皆有ViewLogic的部分实现

- Presenter负责连接View与Model，需要了解View与Model的细节。

- View需要了解Presenter的细节，将用户输入转化为事件传递给Presenter

- Model需要了解Presenter的细节，在完成更新之后将最新的模型传递给Presenter

- View与Model之间相互解耦合



### Supervising Controller MVP

简化Presenter的部分功能，使得Presenter只起到需要复杂控制或者调解的操作，而简单的Model展示转化直接由View与Model进行交互：

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/EB81D8B6-227A-4E94-8107-C6DCC7920574.png)



## MVVM:Data Binding & Stateless View 数据绑定与无状态的View，View<->ViewModels

Model View View-Model模型是MV*家族中最年轻的一位，也是由Microsoft提出，并经由Martin Fowler布道传播。MVVM源于Martin Fowler的Presentation Model，Presentation Model的核心在于接管了View所有的行为响应，View的所有响应与状态都定义在了Presentation Model中。也就是说，View不会包含任意的状态。举个典型的使用场景，当用户点击某个按钮之后，状态信息是从Presentation Model传递给Model，而不是从View传递给Presentation Model。任何控制组件间的逻辑操作，即上文所述的ViewLogic，都应该放置在Presentation Model中进行处理，而不是在View层，这一点也是MVP模式与Presentation Model最大的区别。

MVVM模式进一步深化了Presentation Model的思想，利用Data Binding等技术保证了View中不会存储任何的状态或者逻辑操作。在WPF中，UI主要是利用XAML或者XML创建，而这些标记类型的语言是无法存储任何状态的，就像HTML一样（因此JSX语法其实是将View又有状态化了），只是允许UI与某个ViewModel中的类建立映射关系。渲染引擎根据XAML中的声明以及来自于ViewModel的数据最终生成呈现的页面。因为数据绑定的特性，有时候MVVM也会被称作MVB:Model View Binder。总结一下，MVVM利用数据绑定彻底完成了从命令式编程到声明式编程的转化，使得View逐步无状态化。一个典型的MVVM的使用场景为：

- 用户交互输入

- View将数据直接传送给ViewModel，ViewModel保存这些状态数据

- 在有需要的情况下，ViewModel会将数据传送给Model

- Model在更新完成之后通知ViewModel

- ViewModel从Model中获取最新的模型，并且更新自己的数据状态

- View根据最新的ViewModel的数据进行重新渲染



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/BB708F10-1F39-4FFE-A66C-319293AAC71F.png)


根据上述流程，我们可知MVVM模式的特性为：

- ViewModel、Model中存在ViewLogic实现，View则不保存任何状态信息

- View不需要了解ViewModel的实现细节，但是会声明自己所需要的数据类型，并且能够知道如何重新渲染

- ViewModel不需要了解View的实现细节(非命令式编程)，但是需要根据View声明的数据类型传入对应的数据。ViewModel需要了解Model的实现细节。

-  Model不需要了解View的实现细节，需要了解ViewModel的实现细节






## MV* in iOS

### MVC

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-PkWjDU0jqGJOB972cMsrnA.png)

Cocoa MVC中往往会将大量的逻辑代码放入ViewController中，这就导致了所谓的Massive ViewController，而且很多的逻辑操作都嵌入到了View的生命周期中，很难剥离开来。或许你可以将一些业务逻辑或者数据转换之类的事情放到Model中完成，不过对于View而言绝大部分时间仅起到发送Action给Controller的作用。ViewController逐渐变成了几乎所有其他组件的Delegate与DataSource，还经常会负责派发或者取消网络请求等等职责。你的代码大概是这样的:

```

var userCell = tableView.dequeueReusableCellWithIdentifier("identifier") as UserCell

userCell.configureWithUser(user)

```

上面这种写法直接将View于Model关联起来，其实算是打破了Cocoa MVC的规范的，不过这样也是能够减少些Controller中的中转代码呢。这样一个架构模式在进行单元测试的时候就显得麻烦了，因为你的ViewController与View紧密关联，使得其很难去进行测试，因为你必须为每一个View创建Mock对象并且管理其生命周期。另外因为整个代码都混杂在一起，即破坏了职责分离原则，导致了系统的可变性与可维护性也很差。经典的MVC的示例程序如下:

```

import UIKit



struct Person { // Model

    let firstName: String

    let lastName: String

}



class GreetingViewController : UIViewController { // View + Controller

    var person: Person!

    let showGreetingButton = UIButton()

    let greetingLabel = UILabel()

    

    override func viewDidLoad() {

        super.viewDidLoad()

        self.showGreetingButton.addTarget(self, action: "didTapButton:", forControlEvents: .TouchUpInside)

    }

    

    func didTapButton(button: UIButton) {

        let greeting = "Hello" + " " + self.person.firstName + " " + self.person.lastName

        self.greetingLabel.text = greeting

        

    }

    // layout code goes here

}

// Assembling of MVC

let model = Person(firstName: "David", lastName: "Blaine")

let view = GreetingViewController()

view.person = model;

```

上面这种代码一看就很难测试，我们可以将生成greeting的代码移到GreetingModel这个单独的类中，从而进行单独的测试。不过我们还是很难去在GreetingViewController中测试显示逻辑而不调用UIView相关的譬如`viewDidLoad`、`didTapButton`等等较为费时的操作。再按照我们上文提及的优秀的架构的几个方面来看:

- Distribution:View与Model是分割开来了，不过View与Controller是紧耦合的

- Testability:因为较差的职责分割导致貌似只有Model部分方便测试

- 易用性:因为程序比较直观，可能容易理解。



### MVP

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-hKUCPEHg6TDz6gtOlnFYwQ.png)

Cocoa中MVP模式是将ViewController当做纯粹的View进行处理，而将很多的ViewLogic与模型操作移动到Presenter中进行，代码如下:

```

import UIKit



struct Person { // Model

    let firstName: String

    let lastName: String

}



protocol GreetingView: class {

    func setGreeting(greeting: String)

}



protocol GreetingViewPresenter {

    init(view: GreetingView, person: Person)

    func showGreeting()

}



class GreetingPresenter : GreetingViewPresenter {

    unowned let view: GreetingView

    let person: Person

    required init(view: GreetingView, person: Person) {

        self.view = view

        self.person = person

    }

    func showGreeting() {

        let greeting = "Hello" + " " + self.person.firstName + " " + self.person.lastName

        self.view.setGreeting(greeting)

    }

}



class GreetingViewController : UIViewController, GreetingView {

    var presenter: GreetingViewPresenter!

    let showGreetingButton = UIButton()

    let greetingLabel = UILabel()

    

    override func viewDidLoad() {

        super.viewDidLoad()

        self.showGreetingButton.addTarget(self, action: "didTapButton:", forControlEvents: .TouchUpInside)

    }

    

    func didTapButton(button: UIButton) {

        self.presenter.showGreeting()

    }

    

    func setGreeting(greeting: String) {

        self.greetingLabel.text = greeting

    }

    

    // layout code goes here

}

// Assembling of MVP

let model = Person(firstName: "David", lastName: "Blaine")

let view = GreetingViewController()

let presenter = GreetingPresenter(view: view, person: model)

view.presenter = presenter

```

- Distribution:主要的业务逻辑分割在了Presenter与Model中，View相对呆板一点

- Testability:较为方便地测试

- 易用性:代码职责分割的更为明显，不过不像MVC那样直观易懂了



### MVVM

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-uhPpTHYzTmHGrAZy8hiM7w.png)

```

import UIKit



struct Person { // Model

    let firstName: String

    let lastName: String

}



protocol GreetingViewModelProtocol: class {

    var greeting: String? { get }

    var greetingDidChange: ((GreetingViewModelProtocol) -> ())? { get set } // function to call when greeting did change

    init(person: Person)

    func showGreeting()

}



class GreetingViewModel : GreetingViewModelProtocol {

    let person: Person

    var greeting: String? {

        didSet {

            self.greetingDidChange?(self)

        }

    }

    var greetingDidChange: ((GreetingViewModelProtocol) -> ())?

    required init(person: Person) {

        self.person = person

    }

    func showGreeting() {

        self.greeting = "Hello" + " " + self.person.firstName + " " + self.person.lastName

    }

}



class GreetingViewController : UIViewController {

    var viewModel: GreetingViewModelProtocol! {

        didSet {

            self.viewModel.greetingDidChange = { [unowned self] viewModel in

                self.greetingLabel.text = viewModel.greeting

            }

        }

    }

    let showGreetingButton = UIButton()

    let greetingLabel = UILabel()

    

    override func viewDidLoad() {

        super.viewDidLoad()

        self.showGreetingButton.addTarget(self.viewModel, action: "showGreeting", forControlEvents: .TouchUpInside)

    }

    // layout code goes here

}

// Assembling of MVVM

let model = Person(firstName: "David", lastName: "Blaine")

let viewModel = GreetingViewModel(person: model)

let view = GreetingViewController()

view.viewModel = viewModel

```

- Distribution:在Cocoa MVVM中，View相对于MVP中的View担负了更多的功能，譬如需要构建数据绑定等等

- Testability:ViewModel拥有View中的所有数据结构，因此很容易就可以进行测试

- 易用性:相对而言有很多的冗余代码



## MV* in Android

此部分完整代码在[这里](https://github.com/ivacf/archi)，笔者在这里节选出部分代码方便对照演示。Android中的Activity的功能很类似于iOS中的UIViewController，都可以看做MVC中的Controller。在2010年左右经典的Android程序大概是这样的:

```

TextView mCounterText;

Button mCounterIncrementButton;



int mClicks = 0;



public void onCreate(Bundle b) {

  super.onCreate(b);



  mCounterText = (TextView) findViewById(R.id.tv_clicks);

  mCounterIncrementButton = (Button) findViewById(R.id.btn_increment);



  mCounterIncrementButton.setOnClickListener(new View.OnClickListener() {

    public void onClick(View v) {

      mClicks++;

      mCounterText.setText(""+mClicks);

    }

  });

}

```

后来2013年左右出现了[ButterKnife](https://github.com/JakeWharton/butterknife)这样的基于注解的控件绑定框架，此时的代码看上去是这样的:

```


@Bind(R.id.tv_clicks) mCounterText;

@OnClick(R.id.btn_increment)

public void onSubmitClicked(View v) {

	mClicks++;

	mCounterText.setText("" + mClicks);

}

```

后来Google官方也推出了数据绑定的框架，从此MVVM模式在Android中也愈发流行:

```

<layout xmlns:android="http://schemas.android.com/apk/res/android">

   <data>

       <variable name="counter" type="com.example.Counter"/>

       <variable name="counter" type="com.example.ClickHandler"/>

   </data>

   <LinearLayout

       android:orientation="vertical"

       android:layout_width="match_parent"

       android:layout_height="match_parent">

       <TextView android:layout_width="wrap_content"

           android:layout_height="wrap_content"

           android:text="@{counter.value}"/>

       <Buttonandroid:layout_width="wrap_content"

           android:layout_height="wrap_content"

           android:text="@{handlers.clickHandle}"/>

   </LinearLayout>

</layout>

```

后来[Anvil](https://github.com/zserge/anvil)这样的受React启发的组件式框架以及Jedux这样借鉴了Redux全局状态管理的框架也将Unidirectional 架构引入了Android开发的世界。



### MVC

- 声明View中的组件对象或者Model对象

```

    private Subscription subscription;

    private RecyclerView reposRecycleView;

    private Toolbar toolbar;

    private EditText editTextUsername;

    private ProgressBar progressBar;

    private TextView infoTextView;

    private ImageButton searchButton;

```

- 将组件与Activity中对象绑定，并且声明用户响应处理函数

```



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        infoTextView = (TextView) findViewById(R.id.text_info);

        //Set up ToolBar

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //Set up RecyclerView

        reposRecycleView = (RecyclerView) findViewById(R.id.repos_recycler_view);

        setupRecyclerView(reposRecycleView);

        // Set up search button

        searchButton = (ImageButton) findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                loadGithubRepos(editTextUsername.getText().toString());

            }

        });

        //Set up username EditText

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);

        editTextUsername.addTextChangedListener(mHideShowButtonTextWatcher);

        editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String username = editTextUsername.getText().toString();

                    if (username.length() > 0) loadGithubRepos(username);

                    return true;

                }

                return false;

            }

});

```

- 用户输入之后的更新流程

```



        progressBar.setVisibility(View.VISIBLE);

        reposRecycleView.setVisibility(View.GONE);

        infoTextView.setVisibility(View.GONE);

        ArchiApplication application = ArchiApplication.get(this);

        GithubService githubService = application.getGithubService();

        subscription = githubService.publicRepositories(username)

                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(application.defaultSubscribeScheduler())

                .subscribe(new Subscriber<List<Repository>>() {

                    @Override

                    public void onCompleted() {

                        progressBar.setVisibility(View.GONE);

                        if (reposRecycleView.getAdapter().getItemCount() > 0) {

                            reposRecycleView.requestFocus();

                            hideSoftKeyboard();

                            reposRecycleView.setVisibility(View.VISIBLE);

                        } else {

                            infoTextView.setText(R.string.text_empty_repos);

                            infoTextView.setVisibility(View.VISIBLE);

                        }

                    }



                    @Override

                    public void onError(Throwable error) {

                        Log.e(TAG, "Error loading GitHub repos ", error);

                        progressBar.setVisibility(View.GONE);

                        if (error instanceof HttpException

                                && ((HttpException) error).code() == 404) {

                            infoTextView.setText(R.string.error_username_not_found);

                        } else {

                            infoTextView.setText(R.string.error_loading_repos);

                        }

                        infoTextView.setVisibility(View.VISIBLE);

                    }



                    @Override

                    public void onNext(List<Repository> repositories) {

                        Log.i(TAG, "Repos loaded " + repositories);

                        RepositoryAdapter adapter =

                                (RepositoryAdapter) reposRecycleView.getAdapter();

                        adapter.setRepositories(repositories);

                        adapter.notifyDataSetChanged();

                    }

});

```



### MVP

- 将Presenter与View绑定，并且将用户响应事件绑定到Presenter中

```

        //Set up presenter

        presenter = new MainPresenter();

        presenter.attachView(this);

        ...

        

        // Set up search button

        searchButton = (ImageButton) findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                presenter.loadRepositories(editTextUsername.getText().toString());

            }

        });

```

- Presenter中调用Model更新数据，并且调用View中进行重新渲染

```

    public void loadRepositories(String usernameEntered) {

        String username = usernameEntered.trim();

        if (username.isEmpty()) return;



        mainMvpView.showProgressIndicator();

        if (subscription != null) subscription.unsubscribe();

        ArchiApplication application = ArchiApplication.get(mainMvpView.getContext());

        GithubService githubService = application.getGithubService();

        subscription = githubService.publicRepositories(username)

                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(application.defaultSubscribeScheduler())

                .subscribe(new Subscriber<List<Repository>>() {

                    @Override

                    public void onCompleted() {

                        Log.i(TAG, "Repos loaded " + repositories);

                        if (!repositories.isEmpty()) {

                            mainMvpView.showRepositories(repositories);

                        } else {

                            mainMvpView.showMessage(R.string.text_empty_repos);

                        }

                    }



                    @Override

                    public void onError(Throwable error) {

                        Log.e(TAG, "Error loading GitHub repos ", error);

                        if (isHttp404(error)) {

                            mainMvpView.showMessage(R.string.error_username_not_found);

                        } else {

                            mainMvpView.showMessage(R.string.error_loading_repos);

                        }

                    }



                    @Override

                    public void onNext(List<Repository> repositories) {

                        MainPresenter.this.repositories = repositories;

                    }

                });

        }



```

### MVVM

- XML中声明数据绑定

```

<data>

        <variable

            name="viewModel"

            type="uk.ivanc.archimvvm.viewmodel.MainViewModel"/>

</data>

...

            <EditText

                android:id="@+id/edit_text_username"

                android:layout_width="match_parent"

                android:layout_height="wrap_content"

                android:layout_toLeftOf="@id/button_search"

                android:hint="@string/hit_username"

                android:imeOptions="actionSearch"

                android:inputType="text"

                android:onEditorAction="@{viewModel.onSearchAction}"

                android:textColor="@color/white"

                android:theme="@style/LightEditText"

                app:addTextChangedListener="@{viewModel.usernameEditTextWatcher}"/>




```

- View中绑定ViewModel

```

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mainViewModel = new MainViewModel(this, this);

        binding.setViewModel(mainViewModel);

        setSupportActionBar(binding.toolbar);

        setupRecyclerView(binding.reposRecyclerView);

```

- ViewModel中进行数据操作

```

public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            String username = view.getText().toString();

            if (username.length() > 0) loadGithubRepos(username);

            return true;

        }

        return false;

    }



    public void onClickSearch(View view) {

        loadGithubRepos(editTextUsernameValue);

    }



    public TextWatcher getUsernameEditTextWatcher() {

        return new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {



            }



            @Override

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                editTextUsernameValue = charSequence.toString();

                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);

            }



            @Override

            public void afterTextChanged(Editable editable) {



            }

        };

}

```

# Unidirectional User Interface Architecture:单向数据流

Unidirectional User Interface Architecture架构的概念源于后端常见的CROS/Event Sourcing模式，其核心思想即是将应用状态被统一存放在一个或多个的Store中，并且所有的数据更新都是通过可观测的Actions触发，而所有的View都是基于Store中的状态渲染而来。该架构的最大优势在于整个应用中的数据流以单向流动的方式从而使得有用更好地可预测性与可控性，这样可以保证你的应用各个模块之间的松耦合性。与MVVM模式相比，其解决了以下两个问题：

- 避免了数据在多个ViewModel中的冗余与不一致问题

- 分割了ViewModel的职责，使得ViewModel变得更加Clean



## Why not Bidirectional(Two-way DataBinding)?

This means that one change (a user input or API response) can affect the state of an application in many places in the code — for example, two-way data binding. That can be hard to maintain and debug.



> - [easier-reasoning-with-unidirectional-dataflow-and-immutable-data](https://open.bekk.no/easier-reasoning-with-unidirectional-dataflow-and-immutable-data)





Facebook强调，双向数据绑定极不利于代码的扩展与维护。

从具体的代码实现角度来看，双向数据绑定会导致更改的不可预期性(UnPredictable)，就好像Angular利用Dirty Checking来进行是否需要重新渲染的检测，这导致了应用的缓慢，简直就是来砸场子的。而在采用了单向数据流之后，整个应用状态会变得可预测(Predictable)，也能很好地了解当状态发生变化时到底会有多少的组件发生变化。另一方面，相对集中地状态管理，也有助于你不同的组件之间进行信息交互或者状态共享，特别是像Redux这种强调Single Store与SIngle State Tree的状态管理模式，能够保证以统一的方式对于应用的状态进行修改，并且Immutable的概念引入使得状态变得可回溯。

譬如Facebook在[Flux Overview](https://facebook.github.io/flux/docs/overview.html)中举的例子，当我们希望在一个界面上同时展示未读信息列表与未读信息的总数目的时候，对于MV*就有点恶心了，特别是当这两个组件不在同一个ViewModel/Controller中的时候。一旦我们将某个未读信息标识为已读，会引起控制已读信息、未读信息、未读信息总数目等等一系列模型的更新。特别是很多时候为了方便我们可能在每个ViewModel/Controller都会设置一个数据副本，这会导致依赖连锁更新，最终导致不可预测的结果与性能损耗。而在Flux中这种依赖是反转的，Store接收到更新的Action请求之后对数据进行统一的更新并且通知各个View，而不是依赖于各个独立的ViewModel/Controller所谓的一致性更新。从职责划分的角度来看，除了Store之外的任何模块其实都不知道应该如何处理数据，这就保证了合理的职责分割。这种模式下，当我们创建新项目时，项目复杂度的增长瓶颈也就会更高，不同于传统的View与ViewLogic之间的绑定，控制流被独立处理，当我们添加新的特性，新的数据，新的界面，新的逻辑处理模块时，并不会导致原有模块的复杂度增加，从而使得整个逻辑更加清晰可控。



这里还需要提及一下，很多人应该是从React开始认知到单向数据流这种架构模式的，而当时Angular 1的缓慢与性能之差令人发指，但是譬如Vue与Angular 2的性能就非常优秀。借用Vue.js官方的说法，

The virtual-DOM approach provides a functional way to describe your view at any point of time, which is really nice. Because it doesn’t use observables and re-renders the entire app on every update, the view is by definition guaranteed to be in sync with the data. It also opens up possibilities to isomorphic JavaScript applications.

Instead of a Virtual DOM, Vue.js uses the actual DOM as the template and keeps references to actual nodes for data bindings. This limits Vue.js to environments where DOM is present. However, contrary to the common misconception that Virtual-DOM makes React faster than anything else, Vue.js actually out-performs React when it comes to hot updates, and requires almost no hand-tuned optimization. With React, you need to implementshouldComponentUpdate everywhere and use immutable data structures to achieve fully optimized re-renders.


总而言之，笔者认为双向数据流与单向数据流相比，性能上孰优孰劣尚无定论，最大的区别在于单向数据流与双向数据流相比有更好地可控性，这一点在上文提及的函数响应式编程中也有体现。若论快速开发，笔者感觉双向数据绑定略胜一筹，毕竟这种View与ViewModel/ViewLogic之间的直接绑定直观便捷。而如果是注重于全局的状态管理，希望维护耦合程度较低、可测试性/可扩展性较高的代码，那么还是单向数据流，即Unidirectional Architecture较为合适。一家之言，欢迎讨论。



## Flux:数据流驱动的页面

Flux不能算是绝对的先行者，但是在Unidirectional Architecture中却是最富盛名的一个，也是很多人接触到的第一个Unidirectional Architecture。Flux主要由以下几个部分构成：

- Stores:存放业务数据和应用状态，一个Flux中可能存在多个Stores

- View:层次化组合的React组件

- Actions:用户输入之后触发View发出的事件

- Dispatcher:负责分发Actions



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/923C026D-DCF6-49F1-932E-88A632578068.png)

根据上述流程，我们可知Flux模式的特性为：

- Dispatcher:Event Bus中设置有一个单例的Dispatcher，很多Flux的变种都移除了Dispatcher依赖。

- 只有View使用可组合的组件:在Flux中只有React的组件可以进行层次化组合，而Stores与Actions都不可以进行层次化组合。React组件与Flux一般是松耦合的，因此Flux并不是Fractal，Dispatcher与Stores可以被看做Orchestrator。

- 用户事件响应在渲染时声明:在React的`render()`函数中，即负责响应用户交互，也负责注册用户事件的处理器



下面我们来看一个具体的代码对比，首先是以经典的Cocoa风格编写一个简单的计数器按钮:

```

class ModelCounter



    constructor: (@value=1) ->



    increaseValue: (delta) =>

        @value += delta



class ControllerCounter



    constructor: (opts) ->

        @model_counter = opts.model_counter

        @observers = []



    getValue: => @model_counter.value



    increaseValue: (delta) =>

        @model_counter.increaseValue(delta)

        @notifyObservers()



    notifyObservers: =>

        obj.notify(this) for obj in @observers



    registerObserver: (observer) =>

        @observers.push(observer)



class ViewCounterButton



    constructor: (opts) ->

        @controller_counter = opts.controller_counter

        @button_class = opts.button_class or 'button_counter'

        @controller_counter.registerObserver(this)



    render: =>

        elm = $("<button class=\"#{@button_class}\">

                #{@controller_counter.getValue()}</button>")

        elm.click =>

            @controller_counter.increaseValue(1)

        return elm



    notify: =>

        $("button.#{@button_class}").replaceWith(=> @render())

```

上述代码逻辑用上文提及的MVC模式图演示就是:

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-wjSds7V7Q2jqC7AqkTK8hg.gif)

而如果用Flux模式实现，会是下面这个样子:

```

# Store

class CounterStore extends EventEmitter



    constructor: ->

        @count = 0

        @dispatchToken = @registerToDispatcher()



    increaseValue: (delta) ->

        @count += 1



    getCount: ->

        return @count



    registerToDispatcher: ->

        CounterDispatcher.register((payload) =>

            switch payload.type

                when ActionTypes.INCREASE_COUNT

                    @increaseValue(payload.delta)

        )



# Action

class CounterActions



    @increaseCount: (delta) ->

        CounterDispatcher.handleViewAction({

            'type': ActionTypes.INCREASE_COUNT

            'delta': delta

        })



# View

CounterButton = React.createClass(



    getInitialState: ->

        return {'count': 0}



    _onChange: ->

        @setState({

            count: CounterStore.getCount()

        })



    componentDidMount: ->

        CounterStore.addListener('CHANGE', @_onChange)



    componentWillUnmount: ->

        CounterStore.removeListener('CHANGE', @_onChange)



    render: ->

        return React.DOM.button({'className': @prop.class}, @state.value)



)

```

其数据流图为:

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-C1WAATMd5gagQXy73bPEzw.gif)





## Redux:集中式的状态管理

Redux是Flux的所有变种中最为出色的一个，并且也是当前Web领域主流的状态管理工具，其独创的理念与功能深刻影响了GUI应用程序架构中的状态管理的思想。Redux将Flux中单例的Dispatcher替换为了单例的Store，即也是其最大的特性，集中式的状态管理。并且Store的定义也不是从零开始单独定义，而是基于多个Reducer的组合，可以把Reducer看做Store Factory。Redux的重要组成部分包括:

- Singleton Store:管理应用中的状态，并且提供了一个`dispatch(action)`函数。

- Provider:用于监听Store的变化并且连接像React、Angular这样的UI框架

- Actions:基于用户输入创建的分发给Reducer的事件

- Reducers:用于响应Actions并且更新全局状态树的纯函数



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/EE52C4A6-0755-47D9-9A4B-70080E177869.png)

根据上述流程，我们可知Redux模式的特性为：

- 以工厂模式组装Stores:Redux允许我以`createStore()`函数加上一系列组合好的Reducer函数来创建Store实例，还有另一个`applyMiddleware()`函数可以允许在`dispatch()`函数执行前后链式调用一系列中间件。

- Providers:Redux并不特定地需要何种UI框架，可以与Angular、React等等很多UI框架协同工作。Redux并不是Fractal，一般来说Store被视作Orchestrator。

- User Event处理器即可以选择在渲染函数中声明，也可以在其他地方进行声明。







## Model-View-Update

又被称作[Elm Architecture](https://github.com/evancz/elm-architecture-tutorial/)，上面所讲的Redux就是受到Elm的启发演化而来，因此MVU与Redux之间有很多的相通之处。MVU使用函数式编程语言Elm作为其底层开发语言，因此该架构可以被看做更纯粹的函数式架构。MVU中的基本组成部分有:

- Model:定义状态数据结构的类型

- View:纯函数，将状态渲染为界面

- Actions:以Mailbox的方式传递用户事件的载体

- Update:用于更新状态的纯函数



![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/mvu-unidir-ui-arch.jpg)

根据上述流程，我们可知Elm模式的特性为：

- 到处可见的层次化组合:Redux只是在View层允许将组件进行层次化组合，而MVU中在Model与Update函数中也允许进行层次化组合，甚至Actions都可以包含内嵌的子Action

- Elm属于Fractal架构:因为Elm中所有的模块组件都支持层次化组合，即都可以被单独地导出使用



## Model-View-Intent

MVI是一个基于[RxJS](https://github.com/Reactive-Extensions/RxJS)的响应式单向数据流架构。MVI也是[Cycle.js](http://cycle.js.org/)的首选架构，主要由Observable事件流对象与处理函数组成。其主要的组成部分包括:

- Intent:Observable提供的将用户事件转化为Action的函数

- Model:Observable提供的将Action转化为可观测的State的函数

- View:将状态渲染为用户界面的函数

- Custom Element:类似于React Component那样的界面组件



![](http://staltz.com/img/mvi-unidir-ui-arch.jpg)

根据上述流程，我们可知MVI模式的特性为：

- 重度依赖于Observables:架构中的每个部分都会被转化为Observable事件流

- Intent:不同于Flux或者Redux，MVI中的Actions并没有直接传送给Dispatcher或者Store，而是交于正在监听的Model

- 彻底的响应式，并且只要所有的组件都遵循MVI模式就能保证整体架构的fractal特性



# Clean Architecture:尚文完成，请勿转载

![](http://luboganev.github.io/images/2015-07-23-clean-architecture-pt2/CleanArchitecture.jpg)

Uncle Bob提出Clean Architecture最早并不是专门面向于GUI应用程序，而是描述了一种用于

## iOS Viper Architecture

Viper架构中职责分割地更为细致，大概分为了五层:

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/2/1-0pN3BNTXfwKbf08lhwutag.png)

- Interactor:包含了与数据以及网络相关的业务逻辑，譬如从服务端获取数据并构造出实体对象。很多时候我们会使用所谓的Services或者Managers来负责此方面的工作

- 包含UI相关的一些业务逻辑，调用Interactor中的方法

- Entities:单纯的数据对象而不是数据访问层

- Router:在VIPER模块间完成路由



一般来说，一个VIPER模块可以是单独的某个页面或者整个应用程序，经常会按照权限来划分。

```

import UIKit



struct Person { // Entity (usually more complex e.g. NSManagedObject)

    let firstName: String

    let lastName: String

}



struct GreetingData { // Transport data structure (not Entity)

    let greeting: String

    let subject: String

}



protocol GreetingProvider {

    func provideGreetingData()

}



protocol GreetingOutput: class {

    func receiveGreetingData(greetingData: GreetingData)

}



class GreetingInteractor : GreetingProvider {

    weak var output: GreetingOutput!

    

    func provideGreetingData() {

        let person = Person(firstName: "David", lastName: "Blaine") // usually comes from data access layer

        let subject = person.firstName + " " + person.lastName

        let greeting = GreetingData(greeting: "Hello", subject: subject)

        self.output.receiveGreetingData(greeting)

    }

}



protocol GreetingViewEventHandler {

    func didTapShowGreetingButton()

}



protocol GreetingView: class {

    func setGreeting(greeting: String)

}



class GreetingPresenter : GreetingOutput, GreetingViewEventHandler {

    weak var view: GreetingView!

    var greetingProvider: GreetingProvider!

    

    func didTapShowGreetingButton() {

        self.greetingProvider.provideGreetingData()

    }

    

    func receiveGreetingData(greetingData: GreetingData) {

        let greeting = greetingData.greeting + " " + greetingData.subject

        self.view.setGreeting(greeting)

    }

}



class GreetingViewController : UIViewController, GreetingView {

    var eventHandler: GreetingViewEventHandler!

    let showGreetingButton = UIButton()

    let greetingLabel = UILabel()



    override func viewDidLoad() {

        super.viewDidLoad()

        self.showGreetingButton.addTarget(self, action: "didTapButton:", forControlEvents: .TouchUpInside)

    }

    

    func didTapButton(button: UIButton) {

        self.eventHandler.didTapShowGreetingButton()

    }

    

    func setGreeting(greeting: String) {

        self.greetingLabel.text = greeting

    }

    

    // layout code goes here

}

// Assembling of VIPER module, without Router

let view = GreetingViewController()

let presenter = GreetingPresenter()

let interactor = GreetingInteractor()

view.eventHandler = presenter

presenter.view = view

presenter.greetingProvider = interactor

interactor.output = presenter

```

- Distribution:毫无疑问，VIPER中职责分割的最为细致

- Testability:测试性肯定也是最好的

- 易用性:代码最多

## Android Clean Architecture

