![](https://i.postimg.cc/RF4nt0Xz/image.png)

# SOLID

- 单一职责原则（Single Responsibility Principle, SRP）：一个类只允许有一个职责，即只有一个导致该类变更的原因。

- 开放封闭原则（Open Closed Principle, OCP）：一个软件实体如类、模块和函数应该对扩展开放，对修改关闭。

- 里式替换原则（Liskov Substitution Principle, LSP）：所有引用基类的地方必须能透明地使用其子类的对象，也就是说子类对象可以替换其父类对象，而程序执行效果不变。

- 最少知识原则(Least Knowledge Principle, LKP)：又称迪米特法则（Law of Demeter），一个对象应该对尽可能少的对象有接触，也就是只接触那些真正需要接触的对象。

- 接口分离原则（Interface Segregation Principle， ISP）：多个特定的客户端接口要好于一个通用性的总接口。

- 依赖倒置原则（Dependency Inversion Principle, DIP）：依赖抽象，而不是依赖实现；抽象不应该依赖细节；细节应该依赖抽象；高层模块不能依赖低层模块，二者都应该依赖抽象。

将以上六大原则的英文首字母拼在一起就是 SOLID(稳定的)，所以也称之为 SOLID 原则。

![单一职责原则之间的关系](https://i.postimg.cc/tJmnnQrd/image.png)

单一职责是所有设计原则的基础，开闭原则是设计的终极目标。里氏替换原则强调的是子类替换父类后程序运行时的正确性，它用来帮助实现开闭原则。而接口隔离原则用来帮助实现里氏替换原则，同时它也体现了单一职责。依赖倒置原则是过程式编程与 OO 编程的分水岭，同时它也被用来指导接口隔离原则。

# 链接

- https://www.baeldung.com/solid-principles
