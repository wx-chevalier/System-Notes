# OIDC 协议

OIDC 是 OpenID Connect 的简称，OIDC=(Identity, Authentication) + OAuth 2.0。**它在 OAuth2 上构建了一个身份层，是一个基于 OAuth2 协议的身份认证标准协议。**我们都知道 OAuth2 是一个授权协议，它无法提供完善的身份认证功能，OIDC 使用 OAuth2 的授权服务器来为第三方客户端提供用户的身份认证，并把对应的身份认证信息传递给客户端，且可以适用于各种类型的客户端（比如服务端应用，移动 APP，JS 应用），且完全兼容 OAuth2，也就是说你搭建了一个 OIDC 的服务后，也可以当作一个 OAuth2 的服务来用。应用场景如图：

![img](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/2021/10/202110101111139.png)

## [#](https://thinking.renzhansheng.cn/pages/1518f6/#_1-oidc协议族)1. OIDC 协议族

OIDC 本身是有多个规范构成，其中包含一个核心的规范，多个可选支持的规范来提供扩展支持.

1. [Core (opens new window)](http://openid.net/specs/openid-connect-core-1_0.html)：必选。定义 OIDC 的核心功能，在 OAuth 2.0 之上构建身份认证，以及如何使用 Claims 来传递用户的信息。
2. [Discovery (opens new window)](http://openid.net/specs/openid-connect-discovery-1_0.html)：可选。发现服务，使客户端可以动态的获取 OIDC 服务相关的元数据描述信息（比如支持那些规范，接口地址是什么等等）。
3. [Dynamic Registration (opens new window)](http://openid.net/specs/openid-connect-registration-1_0.html)：可选。动态注册服务，使客户端可以动态的注册到 OIDC 的 OP（这个缩写后面会解释）。
4. [OAuth 2.0 Multiple Response Types (opens new window)](http://openid.net/specs/oauth-v2-multiple-response-types-1_0.html)：可选。针对 OAuth2 的扩展，提供几个新的 response_type。
5. [OAuth 2.0 Form Post Response Mode (opens new window)](http://openid.net/specs/oauth-v2-form-post-response-mode-1_0.html)：可选。针对 OAuth2 的扩展，OAuth2 回传信息给客户端是通过 URL 的 queryString 和 fragment 这两种方式，这个扩展标准提供了一基于 form 表单的形式把数据 post 给客户端的机制。
6. [Session Management (opens new window)](http://openid.net/specs/openid-connect-session-1_0.html)：可选。Session 管理，用于规范 OIDC 服务如何管理 Session 信息。
7. [Front-Channel Logout (opens new window)](http://openid.net/specs/openid-connect-frontchannel-1_0.html)：可选。基于前端的注销机制，使得 RP（这个缩写后面会解释）可以不使用 OP 的 iframe 来退出。
8. [Back-Channel Logout (opens new window)](http://openid.net/specs/openid-connect-backchannel-1_0.html)：可选。基于后端的注销机制，定义了 RP 和 OP 直接如何通信来完成注销。

![img](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/2021/10/202110101111003.png)

上图是官方给出的一个 OIDC 组成结构图，我们暂时只关注 Core 的部分，其他的部分了解是什么东西就可以了，当作黑盒来用。OIDC 也不是新技术，它主要是借鉴 OpenID 的身份标识，OAuth2 的授权和 JWT 包装数据的方式，把这些技术融合在一起就是 OIDC。

## [#](https://thinking.renzhansheng.cn/pages/1518f6/#_2-核心概念)2. 核心概念

- OAuth2 提供了**Access Token**来解决授权第三方客户端访问受保护资源的问题；
- OIDC 在这个基础上提供了**ID Token**来解决第三方客户端标识用户身份认证的问题。

OIDC 的核心在于在 OAuth2 的授权流程中，一并提供用户的身份认证信息（ID Token）给到第三方客户端，ID Token 使用 JWT 格式来包装，得益于 JWT 的自包含性，紧凑性以及防篡改机制，使得 ID Token 可以安全的传递给第三方客户端程序并且容易被验证。此外还提供了**UserInfo**的接口，用户获取用户的更完整的信息。

### [#](https://thinking.renzhansheng.cn/pages/1518f6/#_2-1-主要术语)2.1 主要术语

完整数据参见[Terminology(opens new window)](https://openid.net/specs/openid-connect-core-1_0.html#Terminology)

1. **EU**: End User：一个人类用户。
2. **RP**: Relying Party ,用来代指 OAuth2 中的受信任的客户端，身份认证和授权信息的消费方；
3. **OP**: OpenID Provider，有能力提供 EU 认证的服务（比如 OAuth2 中的授权服务），用来为 RP 提供 EU 的身份认证信息；
4. **ID Token**: JWT 格式的数据，包含 EU 身份认证的信息。
5. **UserInfo Endpoint**：用户信息接口（受 OAuth2 保护），当 RP 使用 Access Token 访问时，返回授权用户的信息，此接口必须使用 HTTPS。

### [#](https://thinking.renzhansheng.cn/pages/1518f6/#_2-2-工作流程)2.2 工作流程

从抽象的角度来看，OIDC 的流程由以下 5 个步骤构成：

1. RP 发送一个**认证**请求给 OP；
2. OP 对 EU 进行身份认证，然后提供授权；
3. OP 把 ID Token 和 Access Token（需要的话）返回给 RP；
4. RP 使用 Access Token 发送一个请求 UserInfo EndPoint；
5. UserInfo EndPoint 返回 EU 的 Claims。

![img](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/2021/10/202110101111497.jpg)

AuthN=Authentication，表示认证；AuthZ=Authorization，代表授权。

### [#](https://thinking.renzhansheng.cn/pages/1518f6/#_2-3-id-token)2.3 ID Token

上面提到过**OIDC 对 OAuth2 最主要的扩展就是提供了 ID Token**。ID Token 是一个安全令牌，是一个授权服务器提供的包含**用户信息**的 JWT 格式的数据结构。ID Token 的主要构成部分如下（使用 OAuth2 流程的 OIDC）。

1. **iss** = Issuer Identifier：必须。提供认证信息者的唯一标识。一般是一个 https 的 url（不包含 querystring 和 fragment 部分）。
2. **sub** = Subject Identifier：必须。iss 提供的 EU 的标识，在 iss 范围内唯一。它会被 RP 用来标识唯一的用户。最长为 255 个 ASCII 个字符。
3. **aud** = Audience(s)：必须。标识 ID Token 的受众。必须包含 OAuth2 的 client_id。
4. **exp** = Expiration time：必须。过期时间，超过此时间的 ID Token 会作废不再被验证通过。
5. **iat** = Issued At Time：必须。JWT 的构建的时间。
6. **auth_time** = AuthenticationTime：EU 完成认证的时间。如果 RP 发送 AuthN 请求的时候携带 max_age 的参数，则此 Claim 是必须的。
7. **nonce**：RP 发送请求的时候提供的随机字符串，用来减缓重放攻击，也可以来关联 ID Token 和 RP 本身的 Session 信息。
8. **acr** = Authentication Context Class Reference：可选。表示一个认证上下文引用值，可以用来标识认证上下文类。
9. **amr** = Authentication Methods References：可选。表示一组认证方法。
10. **azp** = Authorized party：可选。结合 aud 使用。只有在被认证的一方和受众（aud）不一致时才使用此值，一般情况下很少使用。

请参考 OIDC 提供的[标准 Claims(opens new window)](https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims)

> **Access Token** 和 **ID Token** 的区别

- Access Token 是授权第三方客户端**访问受保护资源**的令牌
- ID Token 是第三方客户端**标识用户身份认证**的问令牌

### [#](https://thinking.renzhansheng.cn/pages/1518f6/#_2-4-认证流程)2.4 认证流程

1. **Authorization Code Flow**：使用 OAuth2 的**授权码**来换取 Id Token 和 Access Token。
2. **Implicit Flow**：使用 OAuth2 的**Implicit**流程获取 Id Token 和 Access Token。
3. **Hybrid Flow**：混合 Authorization Code Flow+Implicit Flow。
