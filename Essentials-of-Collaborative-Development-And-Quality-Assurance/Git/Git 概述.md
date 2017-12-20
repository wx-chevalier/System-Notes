[TOC]

# Git

Git是来源于Linux内核开发中的的一个分布式版本管理工具。基于Git的工作流程也是有很多工作流。新人刚使用 git 的时候，就像去到一个既不识当地文字也不会说当地语言的陌生的国家。只要你知道你在什么地方、要去哪里，一切都 OK，而一旦你迷路，麻烦就来了。

The name "git" was given by Linus Torvalds when he wrote the very
first version. He described the tool as "the stupid content tracker"
and the name as (depending on your way):

- random three-letter combination that is pronounceable, and not
actually used by any common UNIX command. The fact that it is a
mispronunciation of "get" may or may not be relevant.
- stupid. contemptible and despicable. simple. Take your pick from the
dictionary of slang.
- "global information tracker": you're in a good mood, and it actually
works for you. Angels sing, and a light suddenly fills the room.
- "g*dd*mn idiotic truckload of sh*t": when it breaks

## Git VS SVN

任性来说，我对Git的第一印象来源于Github提供的非常Nice的客户端，使得我感觉SVN就像乌龟一样缓慢，而Git简单易用。也有人是这么形容Git与SVN的差异的：

![enter description here][4]

> 参考资料
> 
> - [Why Git is Better than SVN][5]

## Reference

### Articles

### Books


### Tutorials & Docs

- [Git Step by Step][3]
- [githug](https://github.com/Gazler/githug):一个集齐了54个管卡的Git小游戏，很不错，过关说明文档看[这里](https://codingstyle.cn/topics/51)

### Practices & Resources

- [探索 .git 目录，让你真正了理解git](http://blog.jobbole.com/98634/?f=tt&hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)
- [dropbox-as-a-true-git-server](http://www.anishathalye.com/2016/04/25/dropbox-as-a-true-git-server/?utm_source=tuicool&utm_medium=referral)
- [Git tips](https://github.com/git-tips/tips):常用的Git小技巧
### Books & Tools

- [My-Git:Github上的Git学习资料的整合][2]
- [Pro Git v2 中文版](https://git-scm.com/book/zh/v2)

# Quick Start

## Installation

### .gitignore

> [A collection of useful .gitignore templates](https://github.com/github/gitignore)

注意忽略只对未跟踪文件有效，对于已加入版本库的文件无效。

**一、三级忽略文件**

1. 版本库共享式忽略文件

版本库中目录下的.gitignore文件作用于整个目录及子目录，会随着该版本库同其他人共享。

2. 本地的针对具体版本库的独享式忽略文件

即在版本库.git目录下的文件info/exclude中设置文件忽略

3. 本地的全局的独享式忽略文件

通过Git的配置变量core.excludesfile指定的一个忽略文件（指定文件名），其设置的忽略对所有本地版本库均有效。设置方法如下（文件名可以任意设置）：

git config --global core.excludesfile ~/.gitignore

**二、关于Git的忽略文件的语法规则**

1. 忽略文件中的空行或以井号（#）开始的行将会被忽略。
2. 可以使用Linux通配符。例如：星号（*）代表任意多个字符，问号（？）代表一个字符，方括号（[abc]）代表可选字符范围，大括号（{string1,string2,...}）代表可选的字符串等。
3. 如果名称的最前面有一个感叹号（!），表示例外规则，将不被忽略。
4. 如果名称的最前面是一个路径分隔符（/），表示要忽略的文件在此目录下，而子目录中的文件不忽略。
5. 如果名称的最后面是一个路径分隔符（/），表示要忽略的是此目录下该名称的子目录，而非文件（默认文件或目录都忽略）。

示例：
s