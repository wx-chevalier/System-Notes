# OAuth

简单说，OAuth 就是一种授权机制。数据的所有者告诉系统，同意授权第三方应用进入系统，获取这些数据。系统从而产生一个短期的进入令牌（token），用来代替密码，供第三方应用使用。

令牌（token）与密码（password）的作用是一样的，都可以进入系统，但是有三点差异。

- 令牌是短期的，到期会自动失效，用户自己无法修改。密码一般长期有效，用户不修改，就不会发生变化。
- 令牌可以被数据所有者撤销，会立即失效。以上例而言，屋主可以随时取消快递员的令牌。密码一般不允许被他人撤销。
- 令牌有权限范围（scope），比如只能进小区的二号门。对于网络服务来说，只读令牌就比读写令牌更安全。密码一般是完整权限。

# 授权机制

OAuth 2.0 规定了四种获得令牌的流程。你可以选择最适合自己的那一种，向第三方应用颁发令牌。

## 授权码

## 隐藏式

有些 Web 应用是纯前端应用，没有后端。这时就不能用上面的方式了，必须将令牌储存在前端。RFC 6749 就规定了第二种方式，允许直接向前端颁发令牌。这种方式没有授权码这个中间步骤，所以称为（授权码）"隐藏式"（implicit）。第一步，A 网站提供一个链接，要求用户跳转到 B 网站，授权用户数据给 A 网站使用。

```url
https://b.com/oauth/authorize?response_type=token&
client_id=CLIENT_ID&
redirect_uri=CALLBACK_URL&
scope=read
```

上面 URL 中，response_type 参数为 token，表示要求直接返回令牌。第二步，用户跳转到 B 网站，登录后同意给予 A 网站授权。这时，B 网站就会跳回 redirect_uri 参数指定的跳转网址，并且把令牌作为 URL 参数，传给 A 网站。

```url
https://a.com/callback#token=ACCESS_TOKEN
```

上面 URL 中，token 参数就是令牌，A 网站因此直接在前端拿到令牌。注意，令牌的位置是 URL 锚点（fragment），而不是查询字符串（querystring），这是因为 OAuth 2.0 允许跳转网址是 HTTP 协议，因此存在"中间人攻击"的风险，而浏览器跳转时，锚点不会发到服务器，就减少了泄漏令牌的风险。

## 密码式

如果你高度信任某个应用，RFC 6749 也允许用户把用户名和密码，直接告诉该应用。该应用就使用你的密码，申请令牌，这种方式称为"密码式"（password）。

第一步，A 网站要求用户提供 B 网站的用户名和密码。拿到以后，A 就直接向 B 请求令牌。

```sh
https://oauth.b.com/token?
  grant_type=password&
  username=USERNAME&
  password=PASSWORD&
  client_id=CLIENT_ID
```

上面 URL 中，grant_type 参数是授权方式，这里的 password 表示"密码式"，username 和 password 是 B 的用户名和密码。

第二步，B 网站验证身份通过后，直接给出令牌。注意，这时不需要跳转，而是把令牌放在 JSON 数据里面，作为 HTTP 回应，A 因此拿到令牌。这种方式需要用户给出自己的用户名/密码，显然风险很大，因此只适用于其他授权方式都无法采用的情况，而且必须是用户高度信任的应用。

## 凭证式

凭证式（client credentials），适用于没有前端的命令行应用，即在命令行下请求令牌。第一步，A 应用在命令行向 B 发出请求。

```sh
https://oauth.b.com/token?
  grant_type=client_credentials&
  client_id=CLIENT_ID&
  client_secret=CLIENT_SECRET
```

上面 URL 中，grant_type 参数等于 client_credentials 表示采用凭证式，client_id 和 client_secret 用来让 B 确认 A 的身份。第二步，B 网站验证通过以后，直接返回令牌。这种方式给出的令牌，是针对第三方应用的，而不是针对用户的，即有可能多个用户共享同一个令牌。

# Links

- http://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html
- https://mp.weixin.qq.com/s/5iryI8e6bwVWDWU57TbYLg
