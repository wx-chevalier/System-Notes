>[原文地址 :Microsoft API Guidelines](https://github.com/Microsoft/api-guidelines/blob/master/Guidelines.md#63-silent-fail-rule)
>[笔者有关于 REST 系列文章](https://github.com/wx-chevalier/just-coder-handbook/#restful) 笔者之前翻译过一篇[来自于 PayPal 的 RESTful API 标准](https://segmentfault.com/a/1190000005924733)，其是 PayPal 内部遵循的 RESTful 的接口规范，本文则是微软提出的 API 风格指南，笔者认为二者各有优劣。微软的准则相对更加详细严谨，但是对于复杂资源请求，却没有 PayPal 提及的多。本文目前仅包含第一部分的翻译，笔者日后为添加上第二部分。

微软云平台为开发者提供了基于 HTTP 的 REST 风格的 API 接口，虽然不同的服务都是基于特定语言的一些请求框架的实现，但是基于 HTTP 的 REST 的操作却是遵循一致的规则。本指南的目的即是提供可以使得任何的 HTTP 客户端能够以相同的行为模式与规范来使用的接口。而保障开发者的一致性体验的关键，即是保证 REST API 遵循统一的设计指南，从而简单易用，符合人们的直觉反应。本指南的目标在于 :

* 为 Microsoft 提供的所有 REST 风格的 Endpoints 都遵循一直的实践模式
* 尽可能与目前产业界中大规模使用的 REST/HTTP 的最佳实践相符合
* 保证所有使用 Microsoft 服务的应用开发者能够方便快捷地基于 REST 接口进行访问
* 允许服务开发者能够基于之前的工作快速搭建新的接口
* 允许所有的非 Microsoft 合作者也能使用这些规范

在正式介绍本指南之前，推荐阅读如下一些参考资料

* [RFC 2616](http://www.ietf.org/rfc/rfc2616.txt) -- HTTP/1.1 的细节定义
* [REST in Practice](http://www.amazon.com/REST-Practice-Hypermedia-Systems-Architecture/dp/0596805829/) -- 基于 REST 的实践指南
* [REST on Wikipedia](http://en.wikipedia.org/wiki/Representational_state_transfer) -- 维基百科上关于 REST 的核心概念与思想的介绍

# Taxonomy

作为 Microsoft REST API 指南的一部分，服务需要遵从如下的分类。

## Errors: 错误

Errors，或者更准确地说，Service Errors ，被定义为因为客户端将错误的数据传递给了服务端而导致服务端拒绝该请求。典型的譬如无效的认证状态、错误的参数、未知的版本 ID 等等。一般来说，服务端会以`4xx`系列的 HTTP 错误码来进行响应，注意，Errors 并不会影响整体的 API 可用性。

## Faults: 故障

Faults，或者说是 Service Faults，被定义为因为服务端内部错误而导致的无法准确对客户端进行响应，一般来说，服务端会以`5xx`系列的 HTTP 错误码进行响应。不同于 Errors，Faults 一定会影响整体的 API 可用性。需要注意的是，因为限流导致的拒绝服务不可以被归类到 Faults。

## Latency: 延迟

延迟，即是指某个 API 从请求到响应完成所需要的时间，一般来说我们会在客户端测试该值。对于同步请求与异步请求的测试方法应该保持一致，对于异步响应的请求应该计算从初始请求发出的时间点到最终响应完成的时间点。

## Time To Complete

提供慢操作的服务必须要追踪`Time To Complete`这个测量标准。

## Long Running API Faults

对于长期运行的 API( 这里的 Long Running API 即指那些异步响应的 API)，极有可能在初始化请求与获取结果的请求时都收到 200 的状态码，但是实际上内部的操作却触发了错误。这些错误需要被纳入对于整体可用性的测量。

# Client Guidence: 客户端指南

为了保证客户端能够较好地接入 REST 服务，同样我们建议客户端也需要遵循一些最佳实践。

## Ignore Rule: 忽略原则

对于松耦合的客户单而言，在真实地获取到数据之前是不知道数据的确切结构的，如果服务端返回了一些并不是客户端所需要的数据，那么客户端应该安全地忽略这些意外的数据。另外在服务端迭代的过程中，可能有部分服务端会添加些额外的域，对于老的客户端而言可能并不需要这些域，从客户端的角度应该选择性地忽略这些域，而服务端必须要在文档中进行注明。

## Variable Order Rule: 变量排序原则

客户端在处理服务端的返回数据时，一定不能依赖于返回的 JSON 数据中的键的顺序，换言之，即使服务端以随机顺序发送回了 JSON 对象，客户端也要能够成功处理。而对于一些特殊的服务端支持的以`$orderBy`关键字来对返回的 JSON 数组中的元素进行排序的时候，服务端必须在接口文档中显式地说明规范。

## Slient Fail Rule

如果客户端是使用服务端提供的一些可选功能，譬如可能发送一些自定义的头部请求项，那么客户端必须要能够在服务器可能无法提供这些可选功能的情况下依然正常工作。

# REST Consistency Fundamentals

## URL Structure

URL 必须保证友好的可读性与可构造性，这有助于对于那些尚未较好支持地客户端提供探索与尽快采用的便捷性。一个典型的较好的 URL 结构如下所示：

```
https://api.contoso.com/v1.0/people/jdoe@contoso.com/inbox
```

而与之相反的，一个可读性较差的 URL 为 :

```
https://api.contoso.com/EWS/OData/Users('jdoe@microsoft.com')/Folders('AAMkADdiYzI1MjUzLTk4MjQtNDQ1Yy05YjJkLWNlMzMzYmIzNTY0MwAuAAAAAACzMsPHYH6HQoSwfdpDx-2bAQCXhUk6PC1dS7AERFluCgBfAAABo58UAAA=')
```

另一个常用的模式就是将某个 URL 作为参数值传递过去，譬如 :

```
https://api.contoso.com/v1.0/items?url=https://resources.contoso.com/shoes/fancy
```

## URL 长度

HTTP 1.1 协议，在 RFC 2616 的[3.2.1](http://tools.ietf.org/html/rfc2616#section-3.2.1)章节中规定了 URL 长度的限制 :

> HTTP 协议本身不会对 URL 长度有任何限制，服务端必须要能够处理任何有效的 URL，并且对于基于 GET 的过长请求也要能够有效处理。当 URL 过长导致服务端无法处理时，服务端应该返回 414 号错误码。

注意，Internet Explorer 浏览器对于生成的 URL 长度会有限制，请服务端注意。

## Canonical Identifier: 标准的标识符

除了需要提供友好的 URL 之外，能够被移动或者重命名的资源必须要包含唯一且稳定的标识符。注意，系统中并不会强制要求以 GUID 作为标识符，典型的包含统一标识符的 URL 如下 :

```
https://api.contoso.com/v1.0/people/7011042402/inbox
```

## Supported Verbs: 支持的动词

客户端必须要以合适的 HTTP 动词来执行某些操作，并且必须要考虑是否支持动词的幂等性。下表是在 Microsoft REST 服务中支持的动词，并不一定是所有的资源都支持全部的动词，不过任何动词都需要满足以下约定 : | Verb | 描述 | 是否幂等 | | ------- | ---------------------------------------- | ------------- | | GET | 返回某个对象的当前值 | True | | PUT | 用于替换某个对象或者创建某个命名对象 | True | | DELETE | 删除某个对象 | True | | POST | 基于提供的数据创建对象，或者提交某个命令操作 | False | | HEAD | 返回某个对象的元信息 l | True | | PATCH | 更新对象的部分属性值 | False | | OPTIONS | 获取某个请求的部分信息 | False |

### POST

POST 操作必须返回创建好的对象的访问链接，譬如我们有个服务是允许用户创建自建的云服务器，请求方式可能如下 :

```
POST http://api.contoso.com/account1/servers
```

响应值应该是如下这样 :

```
201 Created
Location: http://api.contoso.com/account1/servers/server321
```

### 利用 PATCG 创建资源

允许客户端在创建资源的时候只指定部分键值数据的必须支持 UPSET 语法，并且必须支持以 PATCH 动词来创建资源。鉴于 PUT 一般用于对于整个资源的内容的替换，直接允许客户端使用 PUT 进行更新会非常危险。一旦客户端不小心忽略了某些值，那么可能在更新资源时这些原值会被丢失。因此，所有支持 PUT 动词的操作都必须使用用户提交的属性来替换原有属性，而对于用户在请求中未提交的属性则要直接置空，即保证对服务端保留的原有属性务必全部删除。在 UPSET 语法下，对于不存在的资源的 PATCH 操作会被视作创建，而对于已存在资源的操作会被视作更新。为了避免服务端误解客户端的真实意图，客户端必须要在请求头中以一些额外参数注明。如果请求头中包含了 If-Match，那么该 PATCH 操作绝不会当做创建操作。如果请求头中包含了 If-None-Match，那么 PATCH 操作绝不能视作更新操作。如果某个服务端不支持 UPSET，那么对于不存在资源的 PATCH 操作应该返回`409 Conflict`.

### OPTIONS and Link Headers

OPTIONS 操作允许客户端查询某个对象的元信息，至少需要返回该资源支持的动词类别。除以之外，建议是在返回时也包含上一个辅助文档的地址，譬如 :

```
Link: {help}; rel="help"
```

## Standard Request Headers: 标准请求头

所有遵循 Microsoft REST API 指南的服务都需要支持如下的请求头 : | Header | Type | Description | | --------------------------------- | ------------------------------------- | ---------------------------------------- | | Authorization | String | 请求的认证头 | | Date | Date | [RFC 3339](https://tools.ietf.org/html/rfc3339) 格式的时间戳 | | Accept | Content type | 请求的资源类型 : application/xml text/xml application/json text/javascript (for JSONP) | | Accept-Encoding | Gzip, deflate | REST 端必须支持 GZIP 与 DEFLATE 编码，对于大型资源的请求，服务端可以忽略未压缩的请求。 | | Accept-Language | "en", "es", etc. | 指定响应的偏好语言，对于那些支持国际化的服务端必须要处理该请求头。 | | Accept-Charset | Charset type like "UTF-8" | 默认是 UTF-8, 但是服务端也要能处理 ISO-8859-1。 | | Content-Type | Content type | 请求体的 MIME 类型 (PUT/POST/PATCH) | | Prefer | return=minimal, return=representation | 如果指定了 return=minimal，服务端对于插入或者修改操作应该返回一个空的响应体。如果指定了 return=representation ，服务端应该在响应时返回创建好的或者更新后的资源。当部分客户端希望能够较好的带宽利用与响应时间时，服务端应该支持该请求头。 | | If-Match, If-None-Match, If-Range | String | 支持对于资源的优化的并发更新控制的必须要支持该请求头，也可以使用 ETags 等其他的头信息来进行缓存操作。 |

## Standard Response Headers

| Response Header    | Required                  | Description                                                                   |
| ------------------ | ------------------------- | ----------------------------------------------------------------------------- |
| Date               | All responses             | 请求的处理完成的时间，以 [RFC 3339](https://tools.ietf.org/html/rfc3339) 格式 |
| Content-Type       | All responses             | The content type                                                              |
| Content-Encoding   | All responses             | GZIP 或者 DEFLATE                                                             |
| Preference-Applied | When specified in request | Whether a preference indicated in the Prefer request header was applied       |
| ETag               | 当请求的资源包含资源体时  | 包含当前资源的版本信息，与 If-Match 等协同进行并发控制                        |

## Custom Headers: 自定义请求头

对于某些给定 API 的基本操作不应该支持自定义的请求头。对于一些需要额外数据的服务应该支持自定义的请求头。不过自定义的非标准的请求头应该遵循如下的规范 :

* RFC 3864 中暂定为临时的请求头格式
* 请求头应该尽可能地符合使用场景

## Specifying Headers as Query Parameters: 以查询参数方式提交自定义请求头

很多时候在进行 Ajax 请求，特别是跨域请求时部分自定义的请求头可能无法被支持。在这种情况下，我们应该允许将部分请求头作为查询参数传递给服务端，并且要保证查询参数中的名称与请求头中的名称保持一致。不过该规则有个特例就是 Accept 字段，一般在请求参数中会使用缩写的方式来表明请求的是 XML 还是 JSON 或者其他类型。

## PII Parameters: 个人认证信息

鉴于通行的安全与隐私性保护原则，所有关于个人认证的信息不应该放置在 URL 中，即以路径或者查询参数的方式进行传递。因为该信息非常容易在客户端或者网络乃至于服务器的日志文件中被窃取。我们推荐的是所有 PII 类型的信息都应该放在请求头中进行传递，不过，在实践中我们也发现很多时候这些参数无法被放置在请求头中，因此服务端也应该保证能够在查询参数中去获取这些 PII 信息。而对于服务端获取到的 PII 信息，开发者应该严格遵循公司的隐私保护原则，并且建议客户端以加密的方式等等来避免信息泄露。

## Response Formats: 响应格式

成功的平台总是相似的，它们往往会提供可读性较好并且一致的响应结果，从而使开发者能够尽可能地复用代码来进行响应处理。一般在基于 Web 的通信环境下，特别是移动端或者其他低带宽的环境下，我们推荐使用 JSON 作为传输格式，这一点也特别适用于那些基于 JavaScript 的客户端。注意，JSON 中的属性名应该是 camelCased，并且服务端应该默认使用 JSON 作为编码格式。

### Client Specified Response Format

在 HTTP 中，响应的请求格式应该由客户端使用 Accept 属性来决定。如果客户单发送了多种格式请求，那么服务端应当选择其中的一个作为标准。默认的响应格式应该是`application/json`，并且所有的服务端都应该支持`application/json`: | Accept Header | Response type | Notes | | ---------------- | ---------------------------------- | ---------------------------------------- | | application/json | Payload SHOULD be returned as JSON | Also accept text/javascript for JSONP cases |

```
GET https://api.contoso.com/v1.0/products/user
Accept: application/json
```

### Error Condition Response

对于非成功的场景，开发者应该能够以相同的代码库来处理不同的 REST API 返回的错误信息。这样有助于在正常的处理流程之外构建独立的稳定的架构来处理错误情形，下面这个例子基于 OData v4 JSON 标准。我们推荐任何服务端都应该遵循该标准。错误响应必须是单个 JSON 对象，该对象含有唯一的键名`error`，而值也是一个 JSON 对象。该对象应该包含键值对，包含`code`、`message`，并且建议包含譬如`target`、`details`、`innererror`这些信息。code 属性应该是一个与语言无关的字符串，是一个由服务端定义的可读的说明信息。与 HTTP 状态码相比，code 应该指向的更加具体。服务端应该保持有较少的这种`code`定义，推荐不要超过 20 种。并且客户端要保证能够有效处理这些错误信息。`message`中的值应该是可读的错误的具体描述，主要是为了辅助开发者进行理解，注意，不可以将 error 信息直接展示给终端用户。展示给用户的信息应该使用[annotation](http://docs.oasis-open.org/odata/odata-json-format/v4.0/os/odata-json-format-v4.0-os.html#_Instance_Annotations)或者一些自定义的友好的描述。服务端不会负责为`message`提供本地化服务，因为这样对于开发者变得非常不友好并且难以处理。`target`中的值应该指向错误的具体的目标，譬如错误中的属性名。`details`中的值必须是一个包含多个 JSON 对象的 JSON 数组，每个 JSON 对象会包含一个`code`与`message`信息，并且可能包含`target`属性。`details`中的信息会用于描述请求过程中发生的一些其他错误。`innererror`键值对必须包含一个 JSON 对象，该对象的内容则是由服务端指定。每个嵌套的新的错误描述应该是比父层级更加精确具体的描述，这种方式有助于在迭代的过程中随着业务逻辑的编号逐步地添加新的错误码。

```
{
  "error": {
    "code": "BadArgument",
    "message": "Previous passwords may not be reused",
    "target": "password",
    "innererror": {
      "code": "PasswordError",
      "innererror": {
        "code": "PasswordDoesNotMeetPolicy",
        "minLength": "6",
        "maxLength": "64",
        "characterTypes": ["lowerCase","upperCase","number","symbol"],
        "minDistinctCharacterTypes": "2",
        "innererror": {
          "code": "PasswordReuseNotAllowed"
        }
      }
    }
  }
}
```

上面例子中，基本的错误码为 "BadArgument,"，但是服务端在`innererror`中提供了更多详细的说明。"PasswordReuseNotAllowed" 错误码可能是在迭代过程中被添加进去以给客户端更好地提示，这种增量型的添加方式并不会破坏老的客户端的处理过程，而又可以给开发者一些更详细的信息。

```
{
  "error": {
    "code": "BadArgument",
    "message": "Multiple errors in ContactInfo data",
    "target": "ContactInfo",
    "details": [
      {
        "code": "NullValue",
        "target": "PhoneNumber",
        "message": "Phone number must not be null"
      },
      {
        "code": "NullValue",
        "target": "LastName",
        "message": "Last name must not be null"
      },
      {
        "code": "MalformedValue",
        "target": "Address",
        "message": "Address is not valid"
      }
    ]
  }
}
```

上例中一次请求里发生了多个错误，这些错误以及信息都包含在了 "details." 中。我们还推荐服务端在发生错误时添加一个 Retry-After HTTP 响应头，这样可以让客户端知道在合适的时间之后进行重试请求。

## HTTP Status Codes:HTTP 状态码

我们应当使用标准的 HTTP 状态码作为响应的状态码。

## Client Library Optional

开发者应该能够使用多种语言或者平台来进行客户端开发，譬如 Windows、MacOS 、 Linux、C# 、 Python、Node.js 以及 Ruby。服务应该允许利用 curl 等工具进行快速访问。
