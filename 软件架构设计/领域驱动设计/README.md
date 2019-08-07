# 领域驱动设计

DDD 领域驱动设计，起源于 2004 年著名建模专家 Eric Evans 发表的他最具影响力的著名书籍：《Domain-Driven Design – Tackling Complexity in the Heart of Software》，Eric Evans 在该书中只是提供了一套原始理论，并没有提供一套方法论，因此多年来对于 DDD 也是见仁见智。更早些时候 MartinFowler 曾经提出贫血模型与充血模型的概念，他认为我们大多数系统以 POJO 作为模型，只有普通的 getter、setter 方法，没有真正的行为，好像缺少血液的人，在 Evans 看来，DDD 中模型都是以充血形式存在，也就是说在 DDD 中，我们设计的模型不仅包含描述业务属性，还要包含能够描述动作的方法，不同的是，领域中一些概念不能用在模型对象，如仓储、工厂、服务等，如强加于模型中，将破坏模型的定义。

领域驱动设计将教您如何在应用程序中建模现实世界，并使用 OOP 封装组织的业务逻辑。软件开发过程很复杂。当我们遇到问题时，我们通常会尝试通过将其变为更易理解和易于管理的部分来解决复杂问题。领域驱动设计是一种软件开发方法，用于处理复杂的软件项目，以提供满足组织目标的最终产品。事实上，领域驱动设计促进了项目将重点放在不断发展的核心模型上。它将教您如何在应用程序中有效地建模现实世界，并使用 OOP 封装组织的业务逻辑。

DDD 的实践并非一蹴而就，往往都会先是套概念，在代码中使用 Aggregation Root，Bonded Context，Repository 等等这些概念；也会使用一定的分层策略，然而这种做法一般对复杂度的治理并没有多大作用。在经过大量的实践之后，就可以融会贯通，理解 DDD 的本质是统一语言、边界划分和面向对象分析的方法。

![](https://i.postimg.cc/hvm36Vvk/image.png)

# 设计理念

领域驱动设计最大的好处是将业务语义显现化，把原先晦涩难懂的业务算法逻辑，通过领域对象（Domain Object），统一语言（Ubiquitous Language）将领域概念清晰的显性化表达出来。

- 统一语言，软件的开发人员/使用人员都使用同一套语言，即对某个概念，名词的认知是统一的。将模型作为语言的支柱。确保团队在内部的所有交流中，代码中，画图，写东西，特别是讲话的时候都要使用这种语言。例如账号，转账，透支策略，这些都是非常重要的领域概念，如果这些命名都和我们日常讨论以及 PRD 中的描述保持一致，将会极大提升代码的可读性，减少认知成本。
- 面向领域，业务语义显性化，以领域去思考问题，而不是模块。将隐式的业务逻辑从一推 if-else 里面抽取出来，用通用语言去命名、去写代码、去扩展，让其变成显示概念，比如“透支策略”这个重要的业务概念，按照事务脚本的写法，其含义完全淹没在代码逻辑中没有突显出来。

接触到需求第一步就是考虑领域模型，而不是将其切割成数据和行为，然后数据用数据库实现，行为使用服务实现，最后造成需求的首肢分离。以银行账号 Account 为案例，Account 有“存款”，“计算利息”和“取款”等业务行为，但是传统经典的方式是将“存款”，“计算利息”和“取款”行为放在账号的服务 AccountService 中，而不是放在 Account 对象本身之中。

DDD 让你首先考虑的是业务语言，而不是数据。DDD 强调业务抽象和面向对象编程，而不是过程式业务逻辑实现。通过领域模型和 DDD 的分层思想，屏蔽外部变化对领域逻辑的影响，确保交付的软件产品是边界清晰的微服务，而不是内部边界依然混乱的小单体。在需求和设计变化时，可以轻松的完成微服务的开发、拆分和组合，确保微服务不易受外部变化的影响，并稳定运行。

为了实现这两个核心，需要一个关键的角色，领域专家。他负责问题域，和问题解决域，他应该通晓研发的这个产品需要解决哪些问题，专业术语，关联关系。而在目前敏捷开发的结构下，流水线上的各个角色往往会专注于自己负责的环节，精细化的分工也限制了每个角色的全局视角；虽然我们经常提倡所谓的主人翁意识，但是在落地时又很难去推进。

## 优势

![](https://i.postimg.cc/QNKPFZmf/image.png)

- 根据实际业务合理划分模型，模型之间依赖结构和边界更加清晰，避免了混乱的依赖关系，进而增加可读性、可维护性；

- 单一职责，模型只关注自身的本职工作，避免“越权”而导致混乱的调用关系。

- 通过建模，更好的表达现实世界中的复杂业务，随着时间的发展，不断增加系统对实际业务的沉淀，也将更好的通过清晰的代码描述业务逻辑。

- 模型的内聚增加了系统的高度模块化，提升代码的可重用性，对比传统三层模式中，很有可能大量重复的功能散落在各个 Service 内部。

- 通过建立清晰的业务模型，形成统一的业务语义，便于不同角色在合作过程中有效沟通。比如不在会有人在会议中对“工单”、“审核单”、“表单”而反复确认含义了。比如某系统将这三种类型工单在底层使用同一张表存储，而实际业务中审核单=表单 ，与工单差别较大， 由于使用“数据驱动开发”方式因底层表一致而采用了相同的模型-Worksheet，这样大家提起 Worksheet 并不能有效表达其中的真正含义，DDD 的模型建立不会被 DB 所绑架，如建模后提取模型 Worksheet、AuditForm，沟通中无论是 PD、开发甚至是业务基于模型进行沟通会节约很多沟通理解成本。

## 不适用的场景

如果微服务一样，DDD 也并非银弹，我们实践 DDD 的根本目的也是为了更快、更好地满足业务不断迭代的需求，并且能够保证团队的延续性与表述一致性，譬如对于问题域中相同变量、指标的定义是一致的。

DDD 不适合多数为 CRUD 单表操作的简单业务场景，在该场景下往往导致增加系统复杂度。同样不适合基于老系统代码之上进行的优化重构，因为这样从传统分层模式到基于聚合的设计是完全颠覆性的，论改造成本不如重新开发一套新应用。

# 应用案例

## 事务脚本

这里以银行转账事务脚本实现为例，在事务脚本的实现中，关于在两个账号之间转账的领域业务逻辑都被写在了 MoneyTransferService 的实现里面了，而 Account 仅仅是 getters 和 setters 的数据结构，也就是我们说的贫血模型：

```java
public class MoneyTransferServiceTransactionScriptImpl
      implements MoneyTransferService {
  private AccountDao accountDao;
  private BankingTransactionRepository bankingTransactionRepository;
  . . .
  @Override
  public BankingTransaction transfer(
      String fromAccountId, String toAccountId, double amount) {
    Account fromAccount = accountDao.findById(fromAccountId);
    Account toAccount = accountDao.findById(toAccountId);
    // . . .
    double newBalance = fromAccount.getBalance() - amount;
    switch (fromAccount.getOverdraftPolicy()) {
    case NEVER:
      if (newBalance < 0) {
        throw new DebitException("Insufficient funds");
      }
      break;
    case ALLOWED:
      if (newBalance < -limit) {
        throw new DebitException(
            "Overdraft limit (of " + limit + ") exceeded: " + newBalance);
      }
      break;
    }
    fromAccount.setBalance(newBalance);
    toAccount.setBalance(toAccount.getBalance() + amount);
    BankingTransaction moneyTransferTransaction =
        new MoneyTranferTransaction(fromAccountId, toAccountId, amount);
    bankingTransactionRepository.addTransaction(moneyTransferTransaction);
    return moneyTransferTransaction;
  }
}
```

这完全是面向过程的代码风格。

## DDD

如果用 DDD 的方式实现，Account 实体除了账号属性之外，还包含了行为和业务逻辑，比如 debit( ) 和 credit( ) 方法。

```java
// @Entity
public class Account {
  // @Id
  private String id;
  private double balance;
  private OverdraftPolicy overdraftPolicy;
  // . . .
  public double balance() { return balance; }
  public void debit(double amount) {
    this.overdraftPolicy.preDebit(this, amount);
    this.balance = this.balance - amount;
    this.overdraftPolicy.postDebit(this, amount);
  }
  public void credit(double amount) {
    this.balance = this.balance + amount;
  }
}
```

而且透支策略 OverdraftPolicy 也不仅仅是一个 Enum 了，而是被抽象成包含了业务规则并采用了策略模式的对象。

```java
public interface OverdraftPolicy {
  void preDebit(Account account, double amount);
  void postDebit(Account account, double amount);
}
public class NoOverdraftAllowed implements OverdraftPolicy {
  public void preDebit(Account account, double amount) {
    double newBalance = account.balance() - amount;
    if (newBalance < 0) {
      throw new DebitException("Insufficient funds");
    }
  }
  public void postDebit(Account account, double amount) {
  }
}
public class LimitedOverdraft implements OverdraftPolicy {
  private double limit;
  // . . .
  public void preDebit(Account account, double amount) {
    double newBalance = account.balance() - amount;
    if (newBalance < -limit) {
      throw new DebitException(
          "Overdraft limit (of " + limit + ") exceeded: " + newBalance);
    }
  }
  public void postDebit(Account account, double amount) {
  }
}
```

而 Domain Service 只需要调用 Domain Entity 对象完成业务逻辑即可。

```java
public class MoneyTransferServiceDomainModelImpl
      implements MoneyTransferService {
  private AccountRepository accountRepository;
  private BankingTransactionRepository bankingTransactionRepository;
  . . .
  @Override
  public BankingTransaction transfer(
      String fromAccountId, String toAccountId, double amount) {
    Account fromAccount = accountRepository.findById(fromAccountId);
    Account toAccount = accountRepository.findById(toAccountId);
    // . . .
    fromAccount.debit(amount);
    toAccount.credit(amount);
    BankingTransaction moneyTransferTransaction =
        new MoneyTranferTransaction(fromAccountId, toAccountId, amount);
    bankingTransactionRepository.addTransaction(moneyTransferTransaction);
    return moneyTransferTransaction;
  }
}
```
