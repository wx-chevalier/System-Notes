![](https://cdn-images-1.medium.com/max/2000/1*Vv0HNvRhU0ihKVaBIpDUww.jpeg)

# 编程范式与设计模式

![mindmap](https://i.postimg.cc/Jz4vvTrm/image.png)

本篇的所有参考代码归纳在 [design-pattern-snippets](https://github.com/wx-chevalier/design-pattern-snippets) 仓库中。

# 编程范式

托马斯.库尔提出“科学的革命”的范式论后，Robert Floyd 在 1979 年图灵奖的颁奖演说中使用了编程范式一词。编程范式一般包括三个方面，以 OOP 为例：

- 学科的逻辑体系——规则范式：如 类/对象、继承、动态绑定、方法改写、对象替换等等机制。

- 心理认知因素——心理范式：按照面向对象编程之父 Alan Kay 的观点，“计算就是模拟”。OO 范式极其重视隐喻(metaphor)的价值，通过拟人化，按照自然的方式模拟自然。

- 自然观/世界观——观念范式：强调程序的组织技术，视程序为松散耦合的对象/类的组合，以继承机制将类组织成一个层次结构，把程序运行视为相互服务的对象之间的对话。

简单来说，编程范式是程序员看待程序应该具有的观点，代表了程序设计者认为程序应该如何被构建和执行的看法。编程范式是编程语言的一种分类方式，它并不针对某种编程语言。就编程语言而言，一种语言可以适用多种编程范式。

常见的编程范式有：命令式、过程式、说明式、面向对象、函数式、泛型编程等。事实上，凡是非命令式的编程都可归为声明式编程。因此，命令式、函数式和逻辑式是最核心的三种范式。为清楚起见，我们用一幅图来表示它们之间的关系。

![](http://www.nowamagic.net/librarys/images/201306/2013_06_25_01.jpg)

与命令式编程相对的声明式编程(declarative programming)。顾名思义，声明式编程由若干规范(specification)的声明组成的，即一系列陈述句：‘已知这，求解那’，强调‘做什么’而非‘怎么做’。声明式编程是人脑思维方式的抽象，即利用数理逻辑或既定规范对已知条件进行推理或运算。

一些编程语言是专门为某种特定范式设计的，例如 C 语言是过程式编程语言；Smalltalk 和 Java 是较纯粹的面向对象编程语言；Haskell 是纯粹的函数式编程语言。另外一些编程语言和编程范式的关系并不一一对应，如 Python，Scala，Groovy 都支持面向对象和一定程度上的函数式编程。C++是多范式编程语言成功的典范。C++ 支持和 C 语言一样的过程式编程范式，同时也支持面向对象编程范式，STL（Standard Template Library）使 C++具有了泛型编程能力。支持多种范式可能是 C++直到现在仍然具有强大的生命力的原因之一。Swift 是一门典型的多范式编程语言，即支持面向对象编程范式，也支持函数式编程范式，同时还支持泛型编程。Swift 支持多种编程范式是由其创造目标决定的。

# 设计模式

设计模式（Design Patterns）是一套被反复使用、多数人知晓的、经过分类编目的、代码设计经验的总结。使用设计模式是为了可重用代码、让代码更容易被他人理解、保证代码可靠性。

> Descriptions of communicating objects and classes that are customized to solve a general design problem in a particular context.

毫无疑问，设计模式于己于他人于系统都是多赢的，设计模式使代码编制真正工程化，设计模式是软件工程的基石，如同大厦的一块块砖石一样。项目中合理的运用设计模式可以完美的解决很多问题，每种模式在现在中都有相应的原理来与之对应，每一个模式描述了一个在我们周围不断重复发生的问题，以及该问题的核心解决方案，这也是它能被广泛应用的原因。

## Gang of Four

在 1994 年，由 Erich Gamma、Richard Helm、Ralph Johnson 和 John Vlissides 四人合著出版了一本名为 Design Patterns - Elements of Reusable Object-Oriented Software（中文译名：设计模式 - 可复用的面向对象软件元素） 的书，该书首次提到了软件开发中设计模式的概念。

四位作者合称 GOF（四人帮，全拼 Gang of Four）。他们所提出的设计模式主要是基于以下的面向对象设计原则：

- 对接口编程而不是对实现编程。
- 优先使用对象组合而不是继承。

![](https://i.postimg.cc/W1tRts0Y/image.png)

# Motivation & Credits

本篇参考了如下教程：

- [Source Making](https://sourcemaking.com/design_patterns/)

- [Refactoring Guru](https://refactoringguru.cn/design-patterns/factory-method)
