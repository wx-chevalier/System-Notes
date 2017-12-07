
# Test Double

本部分主要介绍所谓的Test Double的概念，并且对其中容易被混用的Mocks与Stubs的概念进行一个阐述。在初期接触到的时候，很多人会把Mock对象与另一个单元测试中经常用到的Stub对象搞混掉。为了方便更好地理解，这里把所有的所谓的Test Double的概念进行一个说明。我们先来看一个常用的单元测试的用例：

```
public class OrderEasyTester extends TestCase {
  private static String TALISKER = "Talisker";
  
  private MockControl warehouseControl;
  private Warehouse warehouseMock;
  
  public void setUp() {
    warehouseControl = MockControl.createControl(Warehouse.class);
    warehouseMock = (Warehouse) warehouseControl.getMock();    
  }

  public void testFillingRemovesInventoryIfInStock() {
    //setup - data
    Order order = new Order(TALISKER, 50);
    
    //setup - expectations
    warehouseMock.hasInventory(TALISKER, 50);
    warehouseControl.setReturnValue(true);
    warehouseMock.remove(TALISKER, 50);
    warehouseControl.replay();

    //exercise
    order.fill(warehouseMock);
    
    //verify
    warehouseControl.verify();
    assertTrue(order.isFilled());
  }

  public void testFillingDoesNotRemoveIfNotEnoughInStock() {
    Order order = new Order(TALISKER, 51);    

    warehouseMock.hasInventory(TALISKER, 51);
    warehouseControl.setReturnValue(false);
    warehouseControl.replay();

    order.fill((Warehouse) warehouseMock);

    assertFalse(order.isFilled());
    warehouseControl.verify();
  }
}
```
当我们进行单元测试的时候，我们会专注于软件中的一个小点，不过问题就是虽然我们只想进行一个单一模块的测试，但是不得不依赖于其他模块，就好像上面例子中的warehouse。而在我提供的两种不同的测试用例的编写方案中，第一个是使用了真实的warehouse对象，而第二个使用了所谓的mock的warehouse对象，也意味着并不是一个真正的warehouse。使用Mock对象也是一种常用的在测试中避免依赖真正的对象的方法，不过像这种在测试中不使用真正对象的方法也有很多。
我们经常看到的类似的关联的名词会有：stub、mock、fake、dummy。本文中我是打算借鉴Gerard Meszaros的论述，可能并不是所有人都怎么描述，不过我觉得Gerard Meszaros说的不错。Gerard Meszaros是用`Test Double`这个术语来称呼这一类用于替换真实对象的模拟对象。Gerard Meszaros具体定义了以下几类double:
- Dummy : 用于传递给调用者但是永远不会被真实使用的对象，通常它们只是用来填满参数列表。
- Fake : Fake对象常常与类的实现一起起作用，但是只是为了让其他程序能够正常运行，譬如内存数据库就是一个很好的例子。
- Stubs : Stubs通常用于在测试中提供封装好的响应，譬如有时候编程设定的并不会对所有的调用都进行响应。Stubs也会记录下调用的记录，譬如一个email gateway就是一个很好的例子，它可以用来记录所有发送的信息或者它发送的信息的数目。简而言之，Stubs一般是对一个真实对象的封装。
- Mocks : Mocks也就是Fowler这篇文章讨论的重点，即是针对设定好的调用方法与需要响应的参数封装出合适的对象。

在上述这几种doubles中，只有mocks强调行为验证，其他的一般都是强调状态验证。为了更好地描述这种区别，我们会对上面的例子进行一些扩展。一般在真实对象不太好交互或者代码还没有写好的时候，我们会选择使用一个测试的Double。譬如我们需要测试一个发送邮件的程序是不是能够在发送邮件的时候设定正确的顺序，而我们肯定不希望真的发邮件出去，这样会被打死的。因此我们会为我们的email系统来创建一个test double。这里也是用例子来展示mocks与stubs区别的地方：
```
public interface MailService {
  public void send (Message msg);
}
public class MailServiceStub implements MailService {
  private List<Message> messages = new ArrayList<Message>();
  public void send (Message msg) {
    messages.add(msg);
  }
  public int numberSent() {
    return messages.size();
  }
}                                 
```
然后就可以进行状态验证了：
```
class OrderStateTester...
  public void testOrderSendsMailIfUnfilled() {
    Order order = new Order(TALISKER, 51);
    MailServiceStub mailer = new MailServiceStub();
    order.setMailer(mailer);
    order.fill(warehouse);
    assertEquals(1, mailer.numberSent());
  }
```
当然这是一个非常简单的测试，我们并没有测试它是否发给了正确的人或者发出了正确的内容。而如果使用Mock的话写法就很不一样了：
```
class OrderInteractionTester...
  public void testOrderSendsMailIfUnfilled() {
    Order order = new Order(TALISKER, 51);
    Mock warehouse = mock(Warehouse.class);
    Mock mailer = mock(MailService.class);
    order.setMailer((MailService) mailer.proxy());

    mailer.expects(once()).method("send");
    warehouse.expects(once()).method("hasInventory")
      .withAnyArguments()
      .will(returnValue(false));

    order.fill((Warehouse) warehouse.proxy());
  }
}

```
在两个例子中我们都是用了test double来替代真正的mail服务，不同的在于stub是用的状态验证而mock使用的是行为验证。如果要基于stub编写状态验证的方法，需要写一些额外的代码来进行验证。而Mock对象用的是行为验证，并不需要写太多的额外代码。




