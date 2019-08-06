# 过程重构

以典型的电商系统为例，其往往某个操作会包含复杂的逻辑过程：

![](https://i.postimg.cc/przvyfSZ/image.png)

其中某个上架的链路，可能包含以下的流程：

![](https://i.postimg.cc/G3ydMRpy/image.png)

面对这种复杂的逻辑，我们首先想到的就是分治理念，譬如引入流程引擎等专门的 Pipeline 处理流程。不过在实践中，如 KISS（Keep It Simple and Stupid）原则所示，最好是什么工具都不要用，次之是用一个极简的 Pipeline 模式，最差是使用像流程引擎这样的重方法。除非你的应用有极强的流程可视化和编排的诉求，否则非常不推荐使用流程引擎等工具。第一，它会引入额外的复杂度，特别是那些需要持久化状态的流程引擎；第二，它会割裂代码，导致阅读代码的不顺畅。

# 分解与抽象

回到商品上架的问题，这里问题核心是工具吗？是设计模式带来的代码灵活性吗？显然不是，问题的核心应该是如何分解问题和抽象问题，知道金字塔原理的应该知道，此处，我们可以使用结构化分解将问题解构成一个有层级的金字塔结构：

![](https://i.postimg.cc/52nVHNsZ/image.png)

以商品上架为例，程序的入口是一个上架命令（OnSaleCommand）, 它由三个阶段（Phase）组成。

```java
@Command
public class OnSaleNormalItemCmdExe {

    @Resource
    private OnSaleContextInitPhase onSaleContextInitPhase;
    @Resource
    private OnSaleDataCheckPhase onSaleDataCheckPhase;
    @Resource
    private OnSaleProcessPhase onSaleProcessPhase;

    @Override
    public Response execute(OnSaleNormalItemCmd cmd) {

        OnSaleContext onSaleContext = init(cmd);

        checkData(onSaleContext);

        process(onSaleContext);

        return Response.buildSuccess();
    }

    private OnSaleContext init(OnSaleNormalItemCmd cmd) {
        return onSaleContextInitPhase.init(cmd);
    }

    private void checkData(OnSaleContext onSaleContext) {
        onSaleDataCheckPhase.check(onSaleContext);
    }

    private void process(OnSaleContext onSaleContext) {
        onSaleProcessPhase.process(onSaleContext);
    }
}
```

每个 Phase 又可以拆解成多个步骤（Step），以 OnSaleProcessPhase 为例，它是由一系列 Step 组成的：

```java
@Phase
public class OnSaleProcessPhase {

    @Resource
    private PublishOfferStep publishOfferStep;
    @Resource
    private BackOfferBindStep backOfferBindStep;
    //省略其它step

    public void process(OnSaleContext onSaleContext){
        SupplierItem supplierItem = onSaleContext.getSupplierItem();

        // 生成OfferGroupNo
        generateOfferGroupNo(supplierItem);

       // 发布商品
        publishOffer(supplierItem);

        // 前后端库存绑定 backoffer域
        bindBackOfferStock(supplierItem);

        // 同步库存路由 backoffer域
        syncStockRoute(supplierItem);

        // 设置虚拟商品拓展字段
        setVirtualProductExtension(supplierItem);

        // 发货保障打标 offer域
        markSendProtection(supplierItem);

        // 记录变更内容ChangeDetail
        recordChangeDetail(supplierItem);

        // 同步供货价到BackOffer
        syncSupplyPriceToBackOffer(supplierItem);

        // 如果是组合商品打标，写扩展信息
        setCombineProductExtension(supplierItem);

        // 去售罄标
        removeSellOutTag(offerId);

        // 发送领域事件
        fireDomainEvent(supplierItem);

        // 关闭关联的待办事项
        closeIssues(supplierItem);
    }
}
```

因此，在做过程分解的时候，我建议工程师不要把太多精力放在工具上，放在设计模式带来的灵活性上。而是应该多花时间在对问题分析，结构化分解，最后通过合理的抽象，形成合适的阶段（Phase）和步骤（Step）上。

![](https://i.postimg.cc/YSPB3DKR/image.png)

# 对象模型

使用过程分解之后的代码，已经比以前的代码更清晰、更容易维护了。不过，还有两个问题值得我们去关注一下。首先是领域知识被割裂肢解：过程化拆解，导致没有一个聚合领域知识的地方。每个 Use Case 的代码只关心自己的处理流程，知识没有沉淀。相同的业务逻辑会在多个 Use Case 中被重复实现，导致代码重复度高，即使有复用，最多也就是抽取一个 util，代码对业务语义的表达能力很弱，从而影响代码的可读性和可理解性。

其次是代码的业务表达能力缺失，举个例子，在上架过程中，有一个校验是检查库存的，其中对于组合品（CombineBackOffer）其库存的处理会和普通品不一样。原来的代码是这么写的：

```java
boolean isCombineProduct = supplierItem.getSign().isCombProductQuote();

// supplier.usc warehouse needn't check
if (WarehouseTypeEnum.isAliWarehouse(supplierItem.getWarehouseType())) {
    // quote warehosue check
    if (CollectionUtil.isEmpty(supplierItem.getWarehouseIdList()) && !isCombineProduct) {
        throw ExceptionFactory.makeFault(ServiceExceptionCode.SYSTEM_ERROR, "亲，不能发布Offer，请联系仓配运营人员，建立品仓关系！");
    }
    // inventory amount check
    Long sellableAmount = 0L;
    if (!isCombineProduct) {
        sellableAmount = normalBiz.acquireSellableAmount(supplierItem.getBackOfferId(), supplierItem.getWarehouseIdList());
    } else {
        //组套商品
        OfferModel backOffer = backOfferQueryService.getBackOffer(supplierItem.getBackOfferId());
        if (backOffer != null) {
            sellableAmount = backOffer.getOffer().getTradeModel().getTradeCondition().getAmountOnSale();
        }
    }
    if (sellableAmount < 1) {
        throw ExceptionFactory.makeFault(ServiceExceptionCode.SYSTEM_ERROR, "亲，实仓库存必须大于0才能发布，请确认已补货.\r[id:" + supplierItem.getId() + "]");
    }
}
```

然而，如果我们在系统中引入领域模型之后，其代码会简化为如下：

```java
if(backOffer.isCloudWarehouse()){
    return;
}

if (backOffer.isNonInWarehouse()){
    throw new BizException("亲，不能发布Offer，请联系仓配运营人员，建立品仓关系！");
}

if (backOffer.getStockAmount() < 1){
    throw new BizException("亲，实仓库存必须大于0才能发布，请确认已补货.\r[id:" + backOffer.getSupplierItem().getCspuCode() + "]");
}
```

使用模型的表达要清晰易懂很多，而且也不需要做关于组合品的判断了，因为我们在系统中引入了更加贴近现实的对象模型（CombineBackOffer 继承 BackOffer），通过对象的多态可以消除我们代码中的大部分的 if-else。

通过上面的案例，我们可以看到有过程分解要好于没有分解，过程分解+对象模型要好于仅仅是过程分解。对于商品上架这个 case，如果采用过程分解+对象模型的方式，最终我们会得到一个如下的系统结构：

![](https://i.postimg.cc/TYfFrv5G/image.png)
