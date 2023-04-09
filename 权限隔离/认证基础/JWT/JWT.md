![JWT](https://s2.ax1x.com/2020/01/06/lykdTf.png)

# JWT 机制详解

JWT 是 Auth0 提出的通过对 JSON 进行加密签名来实现授权验证的方案。与前文介绍的基于 Session 的机制相比，使用 JWT 可以省去服务端读取 Session 的步骤，因此 JWT 是用来取代服务端的 Session 而非客户端 Cookie 的方案，也更符合 RESTful 的规范。这样但是对于客户端（或 App 端）来说，为了保存用户授权信息，仍然需要通过 Cookie 或 localStorage/sessionStorage 等机制进行本地保存。JWT 也可以被广泛用于微服务场景下各个服务的认证，免去重复中心化认证的繁琐。

# 背景分析

Session 模式的问题在于，扩展性（scaling）不好。单机当然没有问题，如果是服务器集群，或者是跨域的服务导向架构，就要求 session 数据共享，每台服务器都能够读取 session。

举例来说，A 网站和 B 网站是同一家公司的关联服务。现在要求，用户只要在其中一个网站登录，再访问另一个网站就会自动登录，可能有以下的实现方式：

- 一种解决方案是 session 数据持久化，写入数据库或别的持久层。各种服务收到请求后，都向持久层请求数据。这种方案的优点是架构清晰，缺点是工程量比较大。另外，持久层万一挂了，就会单点失败。

- 另一种方案是服务器索性不保存 session 数据了，所有数据都保存在客户端，每次请求都发回服务器。JWT 就是这种方案的一个代表。

# 组成结构

JWT 的三个部分依次如下：Header（头部）、Payload（负载）、Signature（签名），编码之后的 JWT 就是简单的字符串，包含又 `.` 分割的三部分：

![JWT Token](https://s2.ax1x.com/2020/01/06/lyk3fe.png)

```js
// 1. Header, 包括类别（typ）、加密算法（alg）；
{
  "alg": "HS256",
  "typ": "JWT"
}

// 2. Payload, 包括需要传递的用户信息；
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}

// 3. Signature, 根据 alg 算法与私有秘钥进行加密得到的签名字串；这一段是最重要的敏感信息，只能在服务端解密；
HMACSHA256(
    base64UrlEncode(header) + "." +
    base64UrlEncode(payload),
    SECREATE_KEY
)
```

Payload 部分也是一个 JSON 对象，用来存放实际需要传递的数据。JWT 规定了 7 个官方字段，供选用。

- iss (issuer)：签发人
- exp (expiration time)：过期时间
- sub (subject)：主题
- aud (audience)：受众
- nbf (Not Before)：生效时间
- iat (Issued At)：签发时间
- jti (JWT ID)：编号

需要注意的是，JWT 默认是不加密，并且本身包含了认证信息，一旦泄露，任何人都可以获得该令牌的所有权限。为了减少盗用，JWT 的有效期应该设置得比较短。对于一些比较重要的权限，使用时应该再次对用户进行认证。并且由于服务器不保存 session 状态，因此无法在使用过程中废止某个 token，或者更改 token 的权限。也就是说，一旦 JWT 签发了，在到期之前就会始终有效，除非服务器部署额外的逻辑。

# 处理流程

在使用过程中，服务端通过用户登录验证之后，将 Header+Claim 信息加密后得到第三段签名，然后将签名返回给客户端，在后续请求中，服务端只需要对用户请求中包含的 JWT 进行解码，即可验证是否可以授权用户获取相应信息，其原理如下图所示：

![JWT S2S 认证](https://s2.ax1x.com/2020/01/06/lykKw6.png)

以 [node/jsonwebtoken](https://github.com/auth0/node-jsonwebtoken) 为例，我们可以使用字符串密钥，或者加载 cert 文件:

```js
const jwt = require("jsonwebtoken");
const token = jwt.sign({ foo: "bar" }, "shhhhh");

// sign with RSA SHA256
const cert = fs.readFileSync("private.key");
const token = jwt.sign({ foo: "bar" }, cert, { algorithm: "RS256" });

// 设置过期时间
jwt.sign(
  {
    exp: Math.floor(Date.now() / 1000) + 60 * 60,
    data: "foobar",
  },
  "secret"
);
```

在进行内容读取时，我们可以直接解码 Payload 部分获取用户信息，也可以对签名内容进行验证：

```js
// 直接获取到 Payload 而忽略签名
const decoded = jwt.decode(token);

// 获取完整的 Payload 与 Header
const decoded = jwt.decode(token, { complete: true });
console.log(decoded.header);
console.log(decoded.payload);

// 同步校验 Token 是否有效
const decoded = jwt.verify(token, "shhhhh");
console.log(decoded.foo); // bar

// 异步校验是否有效
jwt.verify(token, "shhhhh", function (err, decoded) {
  console.log(decoded.foo); // bar
});
```

# Links

- https://mp.weixin.qq.com/s/j-Ap_30PO8bSFUY3Q_RHcg
