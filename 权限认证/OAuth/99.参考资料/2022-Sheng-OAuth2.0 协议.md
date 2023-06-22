# OAuth2.0 协议

OAuth 2.0 定义了四种授权方式:

- 授权码模式（authorization code）
- 简化模式（implicit）
- 密码模式（resource owner password credentials）
- 客户端模式（client credentials）

## [#](https://thinking.renzhansheng.cn/pages/7459f3/#_1-授权码模式)1. 授权码模式

授权码模式（authorization code）是功能最完整、流程最严密的授权模式。它的特点就是通过客户端的后台服务器，与"服务提供商"的认证服务器进行互动。

![oauth2_code_flow](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/2021/10/202110101141573.png)

![image-20210608075752483](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/202110101055393.png)

它的步骤如下：

```text
（A）用户访问客户端，后者将前者导向认证服务器，并指定"重定向URI"（redirection URI）redirect_uri。

（B）用户选择是否给予客户端授权。

（C）假设用户给予授权，认证服务器将用户导向客户端事先指定的redirect_uri，同时附上一个授权码Code。

（D）客户端收到授权码，向认证服务器申请令牌。这一步是在客户端的后台的服务器上完成的，对用户不可见。

（E）认证服务器核对了授权码，确认无误后，向客户端发送访问令牌（access token）和更新令牌（refresh token）。
```

A 步骤中，客户端申请认证的 URI，包含以下参数：

- response_type：表示授权类型，必选项，此处的值固定为"code"
- client_id：表示客户端的 ID，必选项
- redirect_uri：表示重定向 URI，可选项
- scope：表示申请的权限范围，可选项
- state：表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。

```http
GET /oauth.sample.com/authorize
	?response_type=code
	&client_id=s6BhdRkqt3
    &redirect_uri=http%3A%2F%2Fclient.sample.com%2Fsignin-oauth
    &scope=sns_login token
    &state=xyz
```

C 步骤中，服务器回应客户端的 URI，包含以下参数：

- code：表示授权码，必选项。该码的有效期应该很短，通常设为 10 分钟，客户端只能使用该码一次，否则会被授权服务器拒绝。该码与客户端 ID 和重定向 URI，是一一对应关系。
- state：如果客户端的请求中包含这个参数，认证服务器的回应也必须一模一样包含这个参数。

```http
HTTP/1.1 302 Found
Location: https://client.example.com/signin-oauth
	?code=SplxlOBeZQQYbYS6WxSbIA
    &state=xyz
```

D 步骤中，客户端向认证服务器申请令牌的 HTTP 请求，包含以下参数：

- grant_type：表示使用的授权模式，必选项，此处的值固定为"authorization_code"。
- code：表示上一步获得的授权码，必选项。
- redirect_uri：表示重定向 URI，必选项，且必须与 A 步骤中的该参数值保持一致。
- client_id：表示客户端 ID，必选项。

```http
POST /token HTTP/1.1
Host: oauth.example.com
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=SplxlOBeZQQYbYS6WxSbIA
&redirect_uri=http%3A%2F%2Fclient.sample.com%2Fsignin-oauth
&client_id=s6BhdRkqt3
```

E 步骤中，认证服务器发送的 HTTP 回复，包含以下参数：

- access_token：表示访问令牌，必选项。
- token_type：表示令牌类型，该值大小写不敏感，必选项，可以是 bearer 类型或 mac 类型。
- expires_in：表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。
- refresh_token：表示更新令牌，用来获取下一次的访问令牌，可选项。
- scope：表示权限范围，如果与客户端申请的范围一致，此项可省略。

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
	"access_token":"2YotnFZFEjr1zCsicMWpAA",
    "token_type":"bearer",
    "expires_in":3600,
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA"
}
```

## [#](https://thinking.renzhansheng.cn/pages/7459f3/#_2-简化模式)2. 简化模式

简化模式（implicit grant type）不通过第三方应用程序的服务器，直接在浏览器中向认证服务器申请令牌，跳过了"授权码"这个步骤，因此得名。所有步骤在浏览器中完成，令牌对访问者是可见的，且客户端不需要认证。

![image-20210608100435439](https://thinking-oss.oss-cn-beijing.aliyuncs.com/img/2021/10/202110101100302.png)

```text
（A）客户端将用户导向认证服务器。

（B）用户决定是否给于客户端授权。

（C）假设用户给予授权，认证服务器向浏览器回应重定向，并在URI的Hash部分包含了访问令牌，将客户端导向“重定向URI”。

（D）浏览器向资源服务器的“重定向URI”地址发出请求，其中不包括上一步收到的Hash值。
```

A 步骤中，客户端发出的 HTTP 请求，包含以下参数：

- response_type：表示授权类型，此处的值固定为"token"，必选项。
- client_id：表示客户端的 ID，必选项。
- redirect_uri：表示重定向的 URI，可选项。
- scope：表示权限范围，可选项。
- state：表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。

```http
GET /oauth.sample.com/authorize
	?response_type=token
	&client_id=s6BhdRkqt3
    &redirect_uri=http%3A%2F%2Fclient.sample.com%2Fcallback
    &scope=token
    &state=xyz
```

C 步骤中，服务器回应客户端的 URI，包含以下参数：

- access_token：表示访问令牌，必选项。
- token_type：表示令牌类型，该值大小写不敏感，必选项。
- expires_in：表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。
- scope：表示权限范围，如果与客户端申请的范围一致，此项可省略。
- state：如果客户端的请求中包含这个参数，认证服务器的回应也必须一模一样包含这个参数。

```http
HTTP/1.1 302 Found
Location: http://client.sample.com/callback#access_token=2YotnFZFEjr1zCsicMWpAA
               &token_type=bearer
               &expires_in=3600
               &scope=token
               &state=xyz
```

在这个网址的 Hash 部分包含了令牌。

根据上面的 D 步骤，下一步浏览器会访问 Location 指定的网址，但是 Hash 部分不会发送。

## [#](https://thinking.renzhansheng.cn/pages/7459f3/#_3-密码模式)3. 密码模式

密码模式（Resource Owner Password Credentials Grant）中，用户向客户端提供自己的用户名和密码。客户端使用这些信息，向"服务商提供商"索要授权。

在这种模式中，用户必须把自己的密码给客户端，但是客户端不得储存密码。这通常用在用户对客户端高度信任的情况下，比如客户端是操作系统的一部分。而认证服务器只有在其他授权模式无法执行的情况下，才能考虑使用这种模式。

它的步骤如下：

```text
（A）用户向客户端提供用户名和密码。

（B）客户端将用户名和密码发给认证服务器，向后者请求令牌。

（C）认证服务器确认无误后，向客户端提供访问令牌。
```

B 步骤中，客户端发出的 HTTP 请求，包含以下参数：

- grant_type：表示授权类型，此处的值固定为"password"，必选项。
- username：表示用户名，必选项。
- password：表示用户的密码，必选项。
- scope：表示权限范围，可选项。

下面是一个例子。

```http
POST /token HTTP/1.1
Host: oauth.example.com
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=johndoe&password=A3ddj3w
```

## [#](https://thinking.renzhansheng.cn/pages/7459f3/#_4-客户端模式)4. 客户端模式

客户端模式（Client Credentials Grant）指客户端以自己的名义，而不是以用户的名义，向"服务提供商"进行认证。严格地说，客户端模式并不属于 OAuth 框架所要解决的问题。在这种模式中，用户直接向客户端注册，客户端以自己的名义要求"服务提供商"提供服务，其实不存在授权问题。

```text
（A）客户端向认证服务器进行身份认证，并要求一个访问令牌。

（B）认证服务器确认无误后，向客户端提供访问令牌。
```

A 步骤中，客户端发出的 HTTP 请求，包含以下参数：

- grant*type：表示授权类型，此处的值固定为"client*credentials"，必选项。
- scope：表示权限范围，可选项。

```http
POST /token HTTP/1.1
Host: server.example.com
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```

## [#](https://thinking.renzhansheng.cn/pages/7459f3/#_5-更新令牌)5. 更新令牌

如果用户访问的时候，客户端的"访问令牌"已经过期，则需要使用"更新令牌"申请一个新的访问令牌。

客户端发出更新令牌的 HTTP 请求，包含以下参数：

- grant*type：表示使用的授权模式，此处的值固定为"refresh*token"，必选项。
- refresh_token：表示早前收到的更新令牌，必选项。
- scope：表示申请的授权范围，不可以超出上一次申请的范围，如果省略该参数，则表示与上一次一致。

```http
POST /token HTTP/1.1
Host: oauth.example.com
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&refresh_token=tGzv3JOkF0XG5Qx2TlKWIA
```
