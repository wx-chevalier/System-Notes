![default](https://user-images.githubusercontent.com/5803001/44412872-85055300-a59c-11e8-9025-b74c9a47b42b.png)

# Software Engineering Series | 软件工程基础

软件开发就是把一个复杂的问题分解为一系列简单的问题，再把一系列简单的解决方案组合成一个复杂的解决方案。在笔者的系列文章中，分门别类地讨论了 Web、服务端、基础架构等某些具体的领域，而本系列关注形而上的软件工程相关的思考方式、通用能力与设计原则。

# Nav | 导航

接下来，您可以直接浏览相关的篇章：

| [数据结构与算法](./数据结构与算法) | [algorithm-snippets](https://github.com/wx-chevalier/algorithm-snippets) | [面向对象的设计模式](./面向对象的设计模式) | [design-pattern-snippets](https://github.com/wx-chevalier/design-pattern-snippets) | [整洁与重构](./整洁与重构) | [测试与质量保障](./测试与质量保障) | [软件架构设计](./软件架构设计) | [研发方式与工具](./研发方式与工具) |
| ---------------------------------- | ------------------------------------------------------------------------ | ------------------------------------------ | ---------------------------------------------------------------------------------- | -------------------------- | ---------------------------------- | ------------------------------ | ---------------------------------- |


最后，您还可以前往 [xCompass](https://wx-chevalier.github.io/home/#/search) 或 [alfred-sg](https://github.com/wx-chevalier/Soogle/tree/master/alfred-sg) 交互式地检索、查找需要的文章/链接/书籍/课程。

# Preface | 前言

Generally speaking, there are 3. The Performant System Problem, the Embedded System Problem, and the Complex Domain Problem.

## The Performant System Problem

Let's talk about Twitter.

Twitter is actually a really simple concept.

You sign up, you make tweets, you like other people's tweets and that's pretty much it.

If Twitter is that simple, why couldn't someone else do it?

It's apparent that the real challenge for Twitter is not actually so much as "what it does", but it's "how it's able to do what it does".

Twitter has the unique challenge of serving requests from approximately 500 million users every single day.

The hard problem that Twitter solves is actually a perfomance problem.

When the challenge is performance, whether we use a strictly typed language or not is less important.

## The Embedded System Problem

An embedded system is a combination of computer hardware and software, with the purpose of enabling control over the mechanical or electrical aspects of a system.

Most systems we use today are built on a very complex layer of code that, if not initially written in, compiles down to C or C++ usually.

Coding in these languages is not for the faint of heart.

In C, there is no such thing as objects; and we as humans like objects because we can easily understand them. C is procedural and this makes the code that we have to write in this language more challenging to keep clean. These problems also require knowledge of the lower-level details.

C++ does make life a whole lot better because it has object orientation, but the challenge is still fundementally interacting with lower-level hardware details.

Because we don't really have that much of a choice on the languages we use for these problems, it's irrelevant to discuss TypeScript here.

## The Complex Domain Problem

For some problems, that challenge is less about scaling in terms of handling more requests, but scaling in terms of the codebase's size.

Enterprise companies have complex real-life problems to be solved. In these companies, the biggest engineering challenges are usually:

- Being able to logically (via domains) separate parts of that monolith into smaller apps. And then, physically (via microservices for bounded contexts) split them up so that teams can be assigned to maintain them

- Handling integration and synchronization between these apps

- Modeling the domain concepts and actually solving the problems of the domain

- Creating a ubiquitous (all encompassing) language to be shared by developers and domain experts

- Not getting lost in the mass amounts of code written and slowing down to the point where it becomes impossible to add new features without breaking existing ones

I've essentially described the types of problems that Domain Driven Design solves. For these types of projects, you wouldn't even think about not using a strictly-typed language like TypeScript.

## 强/弱类型语言

For Complex Domain problems, if you don't choose TypeScript and instead, choose JavaScript, it will require some extra effort to be successful. Not only will you have to be extra comfortable with your object modeling abilities in vanilla JavaScript, but you'll also have to know how to utilize the 4 principles of object-oriented programming (encapsulation, abstraction, inheritance, and polymorphism).

This can be hard to do. JavaScript doesn't naturally come with concepts of interfaces and abstract classes.

"Interface Segregation" from the SOLID design principles isn't easily achievable with vanilla JavaScript

Using JavaScript alone would also require a certain level of discipline as a developer in order to keep the code clean, and this is vital once the codebase is sufficiently large. You're also left to ensure that your team shares the same discipline, experience and knowledge level on how to implement common design patterns in JavaScript. If not, you'll need to guide them.

In Domain-Driven projects like this, the strong benefit from using a strictly typed language is less about expressing what can be done, but more about using encapsulation and information hiding to reduce the surface area of bugs by limiting what domain objects are actually allowed to do.

Code size usually ties back to the Complex Domain Problem, where a large codebase means a complex domain, but that's not always the case.

When the amount of code a project has gets to a certain size, it becomes harder to keep track of everything that exists, and becomes easier to end up re-implementing something already coded.

Duplication is the enemy to well-designed and stable software.

This is especially heightened when new developers start coding on an already large codebase.

Visual Studio Code's autocompletion and Intellisense helps to navigate through huge projects. It works really well with TypeScript, but it's somewhat limited with JavaScript.

For projects that I know will stay simple and small, or if I know that it will be thrown away eventually, I would be less pressed to recommend TypeScript as a necessity.

## Production software vs. pet projects

Production software is code that you care about, or code that you'll get in trouble for if it doesn't work. It's also code that you've written tests for. The general rule of thumb is that if you care about the code, you need to have unit tests for it.

If you don't care, don't have tests.

Pet projects are self-explanatory. Do whatever you like. You have no professional commitment to uphold any standards of craftsmanship whatsoever.

Go on and make things! Make small things, make big things.

# About

![default](https://i.postimg.cc/y1QXgJ6f/image.png)

## 版权

![License: CC BY-NC-SA 4.0](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg) ![](https://parg.co/bDm)

笔者所有文章遵循 [知识共享 署名-非商业性使用-禁止演绎 4.0 国际许可协议](https://creativecommons.org/licenses/by-nc-nd/4.0/deed.zh)，欢迎转载，尊重版权。如果觉得本系列对你有所帮助，欢迎给我家布丁买点狗粮(支付宝扫码)~

![](https://github.com/wx-chevalier/OSS/blob/master/2017/8/1/Buding.jpg?raw=true)
