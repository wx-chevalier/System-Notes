<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [URI Components](#uri-components)
  - [Version:版本控制](#version%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6)
  - [Namespaces:命名空间](#namespaces%E5%91%BD%E5%90%8D%E7%A9%BA%E9%97%B4)
  - [Resource References:资源关联](#resource-references%E8%B5%84%E6%BA%90%E5%85%B3%E8%81%94)
- [Collection Resources](#collection-resources)
  - [Collection Resource:获取资源集合/列表](#collection-resource%E8%8E%B7%E5%8F%96%E8%B5%84%E6%BA%90%E9%9B%86%E5%90%88%E5%88%97%E8%A1%A8)
    - [Filtering:过滤](#filtering%E8%BF%87%E6%BB%A4)
      - [Paging:分页](#paging%E5%88%86%E9%A1%B5)
        - [Hypermedia links:超链接](#hypermedia-links%E8%B6%85%E9%93%BE%E6%8E%A5)
      - [Time selection](#time-selection)
      - [Sorting:排序](#sorting%E6%8E%92%E5%BA%8F)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

在构建API时，我们不可避免的会采用现有的跨平台的HTTP的交互方式与资源模型，因此如果你发现你目前的模式与我们的标准南辕北辙，那么请咨询你们专业的API设计师以获得进一步的建议。

# URI Components

## Version:版本控制

URI应当包含`vN`，其中`N`指明版本号。基于URL的版本控制相较于其他复杂的请求头的方法会显得简单易用很多。


- URI Template
```	
/v{version}/
```
- Example
```
/v1/
```

## Namespaces:命名空间
如果在URI中你需要考虑命名空间这个概念，那么应当选择紧邻在`version`之后的第一个字段。命名空间折射出消费者对于API功能的观点，而不一定是公司本身业务逻辑层级的划分。

- URI Template
```
/{version}/{namespace}/
```
- Example
```
/v1/vault/
```

## Resource References:资源关联

URI与资源之间的关联应当保证一致性，避免出现容易引起混淆的子命名空间或者子目录的命名，这样有助于使用者能够很明晰地构造这些请求的URI。

- URI Template
	
```
/{version}/{namespace}/{resource}/{resource-id}/{sub-resource}/{sub-resource-id}
```

# Collection Resources
支持CRUD操作的资源被称为是Collection Resources，往往这些资源会与POST/GET/PUT/PATCH/DELETE这些HTTP动词紧密关联。Collection Resources命名时应当使用复数名词，譬如`/users`，这样可以和下面提及的Singletons进行区分。

| Verb | Usage | Idempotent:幂等性 | Notes |
| ---  | ---   | ---        |	---	 |
| GET   | Read  | X          |	|
| POST  | Create|            | 仅当使用 [PayPal-Request-Id](https://developer.paypal.com/webapps/developer/docs/api/#authentication--headers) 请求头时具有幂等性 |
| PUT   | Create|            | 仅当在客户端提供了资源标识符时具有创建功能  |
| PUT   | Update| X           | 仅用于某个资源的全部属性的更新，不可用于局部 |
| PATCH | Update|            | 使用[JSON Patch](https://tools.ietf.org/html/rfc6902) 消息格式 |
| DELETE| Delete|            | 应当在多次请求下具有相同的响应 |

## Collection Resource:获取资源集合/列表

包含任何相关联的元信息的给定资源的列表，所有的资源应当包含在`items`域中，而类似于`total_items` 以及 `total_pages`的域指明整个关于数据整体的信息。这种命名的一致性有助于客户端开发者构建面向不同的资源集合的通用的处理函数。如果使用`GET`动作进行访问，那么注意不应该影响到整个系统，并且保证除非数据发送变化否则响应消息也应该保持一致性。另外还需要注意的是，譬如日志输出这种动作不会被认为是对系统的修改。
在API客户端权限合规的情况下允许对于资源列表进行过滤操作，即并不是本次都要把全部资源进行返回。另外，我们需要提供一个简短的摘要性质的资源表述来减少带宽的消耗，一般来说单个资源都包含较多的属性。

### Filtering:过滤
#### Paging:分页
关于分页的操作应该来源于请求时的`page`与`page_size`参数，其中`page_size`指明了每次请求的结果数目，`page`指明了请求的是第几页。另外，响应时应当保证包含`total_items`与`total_pages`这两个参数，其中`total_items`指示请求的集合中总的数目，`total_pages`指向总的页数(`total_items`/`page_size`)。

##### Hypermedia links:超链接

Hypermedia links用于在分页的集合资源中指明请求其他页资源的便捷地址，一般来说会包含在`next`, `previous`, `first`, `last`等等类似的命名下。

#### Time selection
如果需要根据时间进行选择，那么需要添加`start_time` 或者`{property_name}_after`, `end_time` 或者 `{property_name}_before` 这些查询参数。

#### Sorting:排序
`sort_by` 以及 `sort_order` 参数可以用来指明需要被排序的资源集合。一般来说`sort_by`需要包含某个独立资源名，而`sort_order`应该是`asc`或者`desc`值。

- URI Template
```
GET /{version}/{namespace}/{resource}

```
- Example Request
```
GET /v1/vault/credit-cards
```
- Example Resopnse
```
~~~
{
    "total_items": 1,
    "total_pages": 1,
    "items": [
        {
            "id": "CARD-1SV265177X389440GKLJZIYY",
            "state": "ok",
            "payer_id": "user12345",
            "type": "visa",
            "number": "xxxxxxxxxxxx0331",
            "expire_month": "11",
            "expire_year": "2018",
            "first_name": "Joe",
            "last_name": "Shopper",
            "valid_until": "2017-01-12T00:00:00Z",
            "create_time": "2014-01-13T07:23:15Z",
            "update_time": "2014-01-13T07:23:15Z",
            "links": [
                {
                    "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/CARD-1SV265177X389440GKLJZIYY",
                    "rel": "self",
                    "method": "GET"
                },
                {
                    "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/CARD-1SV265177X389440GKLJZIYY",
                    "rel": "delete",
                    "method": "DELETE"
                },
                {
                    "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/CARD-1SV265177X389440GKLJZIYY",
                    "rel": "patch",
                    "method": "PATCH"
                }
            ]
        }
    ],
    "links": [
        {
            "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/?page_size=10&sort_by=create_time&sort_order=asc",
            "rel": "first",
            "method": "GET"
        }
    ]
}
~~~
```

## HTTP Status
如果返回的资源集合为空，即没有任何的资源项，此时也不应该返回`404 Not Found`，而应该将`items`项设置为空，并且提供一些集合的元信息，譬如`total_count`设置为0。而如果是错误的请求参数应当返回`404 Bad Request`。否则应该返回`200 OK`来表示成功的返回值。


## Read Single Resource

单个资源一般比资源集合中的对应项更详细，同时需要注意GET请求不应该影响到系统。对于敏感数据的资源标识不应该是连续的或者数值类型的，另外，如果待读取的数据是其他数据的子类，那么应该使用不可变的字符串标识符，这样可读性与可调试性都会更好。


- URI Template
```
GET /{version}/{namespace}/{resource}/{resource-id}
```
- Example Request
```
GET /v1/vault/customers/CUSTOMER-66W27667YB813414MKQ4AKDY
```
- Example Response
```	
{
	"merchant_customer_id": "merchant-1",
        "merchant_id": "target",
        "create_time": "2014-10-10T16:10:55Z",
        "update_time": "2014-10-10T16:10:55Z",
        "first_name": "Kartik",
        "last_name": "Hattangadi"
}
```
- HTTP Status

如果指定的资源并不存在，那么应该返回`404 Not Found`状态，否则应该返回`200 OK`状态码


## Update Single Resource
注意，使用PUT动作更新单个资源的时候需要除了需要修正的值否则保证PUT请求的值与GET响应的值保持一致性，另外对于像`create_time`这样系统自动计算的值也可以忽略。

- URI Template
```
PUT /{version}/{namespace}/{resource}/{resource-id}
```
- Example Request
```
PUT /v1/vault/customers/CUSTOMER-66W27667YB813414MKQ4AKDY
{
    "merchant_customer_id": "merchant-1",
    "merchant_id": "target",
    "create_time": "2014-10-10T16:10:55Z",
    "update_time": "2014-10-10T16:10:55Z",
    "first_name": "Kartik",
    "last_name": "Hattangadi"
}
```
- HTTP Status
任何处理失败的请求都应该返回`400 Bad Request`,特别是如果客户端想要更改某个只读的字段，也应该返回`400 Bad Request`。如果具体的业务逻辑上存在校验规则，譬如对于数据的类型、长度等等，那么应该提供具体的操作码说明。如果部分场景下需要客户单与其他API进行交互或者在本次请求之外发出额外的请求，那么应该返回`422`状态码，详情可以参考[PPaaS Blog on this topic](http://ppaas/news/2014/05/01/added-standards-for-422-http-status/)这篇文章。
对于其他成功的更新请求，应该返回`204 No Content`状态码，即没有任何的返回体。


## Update Partial Single Resoure

不同于每次PUT请求中都需要更新资源的全部属性，PACTH可以根据指定的域更新对应的属性值，并且不会影响到其他属性。[JSON Patch](https://tools.ietf.org/html/rfc6902) 是一个推荐的信息格式，在PayPal的几乎所有关于PATCH的操作中都有所应用。除非客户端的特别需要，否则每次PATCH操作的返回状态都应该是`204 No Content`,这样从带宽的角度，特别是在移动设备中能够更好地节约流量。


- URI Template
```
PATCH /{version}/{namespace}/{resource}/{resource-id}
```
- Example Request
```
PATCH /v1/notifications/webhooks/52Y53119KP6130839
[
    {
        "op": "replace",
        "path": "/url",
        "value": "https://www.yeowza.com/paypal_webhook_new_url"
    }
```
- Example Response
```
204 No Content
``` 
- HTTP Status
和PUT请求一致。

## Delete Single Resource
在删除一个资源的时候，为了保证客户端的可重试性，应当将DELETE操作当做幂等操作对待。因此每次删除操作都应该返回`204 No Content`状态码，否则如果你返回的是`404 Not Found`可能会让客户端误认为该资源是并不存在，而不是被删除了。应该使用GET请求来验证某个资源是否被成功删除，而不应该通过DELETE请求进行验证。


- URI Template
```
DELETE /{version}/{namespace}/{resource}/{resource-id}
```

- Example Request
```
DELETE /v1/vault/customers/CUSTOMER-66W27667YB813414MKQ4AKDY
204 No Content
```

## Create New Resource

一般来说，创建某个资源的请求体与GET/PUT不太一致，大部分情况下API Server都会为该资源创建一个全局的资源描述符，即使用[Create New Resource - Consumer ID]()。一旦POST请求被成功执行，也就意味着资源创建成功，那么该资源的描述符也会被添加到资源集合的URI中。Hypermedia links提供了一种较为便捷的方式访问新近创建的资源，可以使用`rel`: `self`。

- URI Template
```
POST /{version}/{namespace}/{resource}
```
- Example Request

```
~~~
POST /v1/vault/credit-cards
{
    "payer_id": "user12345",
    "type": "visa",
    "number": "4417119669820331",
    "expire_month": "11",
    "expire_year": "2018",
    "first_name": "Betsy",
    "last_name": "Buyer",
    "billing_address": {
        "line1": "111 First Street",
        "city": "Saratoga",
        "country_code": "US",
        "state": "CA",
        "postal_code": "95070"
    }
}
~~~
```
- Example Response
```
~~~
201 Created
{
    "id": "CARD-1MD19612EW4364010KGFNJQI",
    "valid_until": "2016-05-07T00:00:00Z",
    "state": "ok",
    "payer_id": "user12345",
    "type": "visa",
    "number": "xxxxxxxxxxxx0331",
    "expire_month": "11",
    "expire_year": "2018",
    "first_name": "Betsy",
    "last_name": "Buyer",
    "links": [
        {
            "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/CARD-1MD19612EW4364010KGFNJQI",
            "rel": "self",
            "method": "GET"
        },
        {
            "href": "https://api.sandbox.paypal.com/v1/vault/credit-cards/CARD-1MD19612EW4364010KGFNJQI",
            "rel": "delete",
            "method": "DELETE"
        }
    ]
}
~~~
```
## Create New Resource - Consumer Supplied Identifier
当某个API Consumer自定义了Resource Identifier，那么应该使用PUT动作来创建资源，这样也能保证幂等性。

# Sub-Resource Collection

在某些情况下，我们可能需要多个标识符来定位到某个资源，这一类资源往往是其他资源的子类。

## Cautions
* 多层的资源标识符本身对于Consumer而言也是一种负担。
  * 尽可能地将具有唯一标识符的资源或者没必要指明父资源的资源作为First-Level Resource。
* 要注意使用多个资源标识符的时候务必不能产生歧义，譬如`/{version}/{namespace}/{resource}/{resource-id}/{sub-resource-id}`这种直接将子资源标识符放在父资源标识符之后的做法就是不合适的，会让Consumer迷糊。 
* 实践中这种资源的层叠嵌套不要超过两层。
  * 要保证API客户端的可用性，如果在某个URI中维持大量的层级资源标识符会大大增加复杂度。
  * 服务端开发者需要校验每一层级的标识符来判断是否具有访问权限，如果层级过深极易导致复杂度的陡升。


- URI Templates
```
POST /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}
GET /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}
GET /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}/{sub-resource-id}
PUT /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}/{sub-resource-id}
DELETE /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}/{sub-resource-id}
```
- Examples
```
GET /v1/notifications/webhooks/{webhook-id}/event-types
POST /v1/factory/widgets/PART-4312/sub-assemblies
GET /v1/factory/widgets/PART-4312/sub-assemblies/INNER_COG
PUT /v1/factory/widgets/PART-4312/sub-assemblies/INNER_COG
DELETE /v1/factory/widgets/PART-4312/sub-assemblies/INNER_COG
```

# Sub-Resource Singleton
当父子资源之间实际上是一一映射的关系时，可以使用单数形式的资源名来表明多个资源标识符的作用。这种情况下子资源往往也是父资源的一部分，即所谓的被父资源所有。否则子资源应当被放置于独立的资源集合中，并且以其他方式表明父子资源的关联。如果需要创建这种所谓的Singleton子资源，应该使用PUT动作，因为PUT是幂等性的。可以使用PATCH来进行部分更新，不过千万要注意不能使用PATCH进行创建操作。

- URI Template

```
GET/PUT /{version}/{namespace}/{resource}/{resource-id}/{sub-resource}
```


- Examples
```
GET /v1/customers/devices/DEV-FDU233FDSE213f)/vendor-information
```

# Complex Operation

- URI Template
```
~~~
POST /{namespace}/{action-resource}
~~~
```

所谓复杂的操作有时候也被称为`controller`或者`actions`，务必要审慎地使用，只有在仔细考虑过上文提及的[Resource Collection](#resource-collection)设计并不能满足需要的时候再进行使用。可以参考[section 2.6 of the RESTful Web Services Cookbook](http://www.amazon.com/RESTful-Web-Services-Cookbook-Scalability/dp/0596801688)这一章节来了解更多的关于`controller`的概念。复杂操作往往是与POST协同使用，并且大部分需要在URI中显式地指明动作，譬如'activate', 'cancel', 'validate', 'accept', 以及 'deny'都是常见的操作。实际上，这种所谓Action-Oriented架构如果直接作用在跟URI中，即直接跟在命名空间之后，就是典型的反模式，通常这种模式较为适用于跟随在子资源之后。当某个场景是专注于动作，而不是资源的时候，应该建议适用Action-Oriented模式，并且此时应该适用POST动作，然后用某个单一的动词指明action。


## Risks
* 架构设计的可扩展性
	* 一旦这种模式被滥用了，URI的数量会急剧增长，特别是根级别的Action可以随着时间疯狂增长。同样的这也会导致路由或者对外提供服务的配置复杂度急速增长。
	* URI无法再被扩展，即不能再使用子资源。 
* 可测试性: 因为缺乏丰富的GET等读取类操作而使得与 [Resource Collection](#resource-collection)-oriented 模式相比有较大缺陷
* 历史: 所有对于Action的调用应该存在某种资源中，譬如`/action-resource-history`。

## Benefits
* 避免因为短暂性数据而导致资源集合模型的损害。
* 可用性的提升：这种Action-Oriented模式能够大大简化客户端交互内容，不过客户端并不能获益于资源本身的可读性


## Resource-Oriented Alternative

与上文提及的这种单纯的Action-Oriented RFC风格URL相比，更好地方法就是与[Resource Collection](#resource-collection)相结合，并且使用`GET /{actions}`来获取历史记录。这也允许未来基于资源模型的扩展。除此之外，这种模式也能较好地与[event sourcing](http://martinfowler.com/eaaDev/EventSourcing.html)概念相结合。


- URI Template
```
POST /{version}/{namespace}/{action}
```
- Example Request
```
~~~
POST /v1/risk/payment-decisions
{
	"code": "h43j5k6iop"
}
~~~
```
- Example Response
```
~~~
201 Created
{
    "code": "h43j5k6iop",
    "status": "APPROVED",
    "links": [
        {
            "href": "https://api.sandbox.paypal.com/v1/risk/payment-decisions/ID-FEF8EWR8E9FW)",
            "rel": "self",
            "method": "GET"
        }
    ]
}
~~~
```
## Complex Operation - Sub-Resource

很多时候我们需要对于资源进行些特定的操作或者状态修正，而这些操作是无法准确的用PUT或者PATCH进行表示。这些URI看上去有点像其他的Sub-Resources不过隐含着操作名。这个模式典型的使用场景就是当改变了某个资源的状态之后会添加些额外的副作用。另外，往往需要将资源标识符包含在URL中。而且并不是每个Action都会修改资源的状态。

一般来说，Action的响应状态都是`200 OK`以及资源本身，如果没有任何的资源状态的修正，那么应该返回`204 No Content`，并且不应该附上任何的响应体。


- URI Template
```
POST /{version}/{namespace}/{resource}/{resource-id}/{complex-operation}
```
- Example Request
```
~~~
POST /v1/payments/billing-agreements/I-0LN988D3JACS/suspend
{
    "note": "Suspending the agreement."
}
~~~
```

- Example Response
```
~~~
204 No Content
~~~  
```

不过需要注意的是，虽然这种模式可以改变状态，也并不意味着所有的关于资源的状态改变都要使用所谓的复杂操作模式。简单的状态的改变仍然可以使用PUT/PATCH，也就是意味着混合使用[Resource Collection](#resource-collection)与Complex Operation从而减少操作的数目。


- Example Request (for mixed use of PUT)

```
~~~
PATCH /v1/payments/billing-agreements/I-0LN988D3JACS
[
    {
        "op": "replace",
        "path": "/",
        "value": {
            "description": "New Description",
            "shipping_address": {
                "line1": "2065 Hamilton Ave",
                "city": "San Jose",
                "state": "CA",
                "postal_code": "95125",
                "country_code": "US"
            }
        }
    }
]
~~~

```


## Complex Operation - Composite

该系列的操作往往可以在一次请求中处理多个creates/updates/deletes操作。这一点往往从性能与可用性方面综合考虑，这也会在影响多个资源的请求中更好地维持原子性。参考下面这个例子，capture和payment都会同时被refund操作影响。对于capture资源的PUT或者PATCH操作会隐性地影响payment资源。


- URI Template
```
POST /{version}/{namespace}/{action}
```
- Example Request
```
~~~
POST /v1/payments/captures/{capture-id}/refund
~~~
```
- Example Response
```
~~~
{
    "id": "0P209507D6694645N",
    "create_time": "2013-05-06T22:11:51Z",
    "update_time": "2013-05-06T22:11:51Z",
    "state": "completed",
    "amount": {
        "total": "110.54",
        "currency": "USD"
    },
    "capture_id": "8F148933LY9388354",
    "parent_payment": "PAY-8PT597110X687430LKGECATA",
    "links": [
        {
            "href": "https://api.sandbox.paypal.com/v1/payments/refund/0P209507D6694645N",
            "rel": "self",
            "method": "GET"
        },
        {
            "href": "https://api.sandbox.paypal.com/v1/payments/payment/PAY-8PT597110X687430LKGECATA",
            "rel": "parent_payment",
            "method": "GET"
        },
        {
            "href": "https://api.sandbox.paypal.com/v1/payments/capture/8F148933LY9388354",
            "rel": "capture",
            "method": "GET"
        }
    ]
}
~~~
```

## Complex Operation - Transient

这一个类型的复杂操作并不会保留客户端的状态或者创建新的资源。往往就是一个简单的RPC调用，然后直接获取返回值。该操作一般不会用在子资源中，因为子资源操作一般会影响父资源的状态。此时返回是建议使用`200 OK`这个状态码。

- URI Template
```
POST /{version}/{namespace}/{action}
```
- Example Request
```
POST /v1/risk/evaluate-payment
{
	"code": "h43j5k6iop"
}
```
- Example Response
```
200 OK
{
	"status": "VALID"
}
```

## Complex Operation - Search
在使用[Resource Collections](#resource-collection)的时候，最好是使用查询参数来进行集合内容的过滤。不过有时候我们会需要一些更为复杂的查询语法，单纯的查询参数的方式会导致使用的问题或者受限于查询参数的长度。这时候，应该选择使用POST请求来指定查询参数。


### Paging

如果响应体比较大的情况下应当使用分页方式，需要注意的是，Consumer应该在每次子请求的时候都使用POST方式。这样也就意味着，在POST请求体中需要维护一些查询参数。分页查询参数同样可以参考[Resource Collections](#paging)这一部分。同样可以适用于提供 `next`, `previous`, `first`, `last` 来进行其他页的快速读取。

- URI Template
```
POST /{version}/{namespace}/{search-resource}
```

- Example Request
```
	POST /v1/factory/widgets-search
	{
		"created_before":"1975-05-13",
		"status": "ACTIVE",
		"vendor": "Parts Inc."
	}
```

- Example Response
```
	200 OK
	{
		"items": [
			<<lots of part objects here>>
		]
		"links": [
                {
                    "href": "https://api.sandbox.factory.io/v1/factory/widgets-search?page=2&page_size=10",
                    "rel": "next",
                    "method": "POST"
                },
				{
                    "href": "https://api.sandbox.factory.io/v1/factory/widgets-search?page=124&page_size=10",
                    "rel": "last",
                    "method": "POST"
                },
		]
	}
```
# Read-only resources

在部分需要进行计算或者静态引用的场景下，GET会比POST请求更为合适，因为POST在HTTP层面是不会有缓存的。GET请求本身是幂等的，即不会改变资源的状态，而POST请求可以用于[Complex Operations](#complex-operation)。

- URI Template
```
GET /{version}/{namespace}/{read-only-resource}
```
- Example Request
```
GET /v1/location/geocode?address=77+N.+Washington+Street%2C+Boston%2C+MA%2C+02114
```
