# Java 依赖倒置

在Java 中抽象指的是接口或者抽象类，两者皆不能实例化。而细节就是实现类，也就是实现了接口或者继承了抽象类的类。他是可以被实例化的。高层模块指的是调用端，底层模块是具体的实现类。在Java中，依赖倒置原则是指模块间的依赖是通过抽象来发生的，实现类之间不发生直接的依赖关系，其依赖关系是通过接口是来实现的。这就是俗称的面向接口编程。

我们下面有一个例子来讲述这个问题。这个例子是工人用锤子来修理东西。我们的代码如下：

public
 
class
 
Hammer
 
{
    
public
 
String
 function
(){
        
return
 
"用锤子修理东西"
;
    
}
}

public
 
class
 
Worker
 
{
    
public
 
void
 fix
(
Hammer
 hammer
){
        
System
.
out
.
println
(
"工人"
 
+
 hammer
.
function
());
    
}


    
public
 
static
 
void
 main
(
String
[]
 args
)
 
{
        
new
 
Worker
().
fix
(
new
 
Hammer
());
    
}
}
这个是一个很简单的例子，但是如果我们要新增加一个功能，工人用 螺丝刀来修理东西，在这个类，我们发现是很难做的。因为我们Worker类依赖于一个具体的实现类Hammer。所以我们用到面向接口编程的思想，改成如下的代码：

public
 
interface
 
Tools
 
{
    
public
 
String
 function
();
}
然后我们的Worker是通过这个接口来于其他细节类进行依赖。代码如下：

public
 
class
 
Worker
 
{
    
public
 
void
 fix
(
Tools
 tool
){
        
System
.
out
.
println
(
"工人"
 
+
 tool
.
function
());
    
}


    
public
 
static
 
void
 main
(
String
[]
 args
)
 
{
        
new
 
Worker
().
fix
(
new
 
Hammer
());
        
new
 
Worker
().
fix
(
new
 
Screwdriver
());

    
}
}
我们的Hammer类与Screwdriver类实现这个接口

public
 
class
 
Hammer
 
implements
 
Tools
{
    
public
 
String
 function
(){
        
return
 
"用锤子修理东西"
;
    
}
}

public
 
class
 
Screwdriver
 
implements
 
Tools
{
    
@Override
    
public
 
String
 function
()
 
{
        
return
 
"用螺丝刀修理东西"
;
    
}

}
这样，通过面向接口编程，我们的代码就有了很高的扩展性，降低了代码之间的耦合度，提高了系统的稳定性。

