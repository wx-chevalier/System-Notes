# 清新脱俗的 Web 服务器 Caddy

作为新兴 Web 服务器，Caddy 提供了很多简单易用的功能而没有历史的包袱，其默认支持并且能帮你自动配置 HTTP/2、HTTPS，对于 IPV6、WebSockets 都有很好的支持。基于 Go 编写的 Caddy 天生对于多核具有很好的支持，并且其丰富的插件系统提供了文件管理、文件上传、基于 MarkDown 的博客系统等等开箱即用的扩展功能。我们可以在[官方下载界面](https://caddyserver.com/download)选择你需要的插件功能定制个性化二进制文件，下载完毕之后即可以使用`caddy`命令直接运行。其默认监听 2015 端口，在浏览器中打开 [http://localhost:2015](http://localhost:2015) 即可以查看其运行情况。我们也可以通过`-conf`参数指定配置文件：

```
$ caddy -conf="/path/to/Caddyfile"
```

下文我们会详细介绍 Caddyfile 的配置语法，Caddy 的一大特性在于其使用所谓指令(Directives)来描述功能进行配置，相较于 Nginx 或者 Apache 其配置会简化很多。如果我们希望支持多配置文件，可以使用`import`指令：

```
import config/common.conf
```

或者引入整个文件夹：

```
import ../vhosts/*
```

# 站点配置

典型的 Caddyfile 配置文件如下所示：

```
localhost


gzip
browse
websocket /echo cat
ext.html
log/var/log/access.log
proxy/api 127.0.0.1:7005
header /api Access-Control-Allow-Origin *
```

每个 Caddyfile 的第一行必须描述其服务的地址：

```
localhost:2020
```

之后的每一行都是官方提供的指令，譬如我们需要为服务器添加 gzip 压缩支持，只需要直接添加一个指令：

```
localhost:2020
gzip
```

我们可以使用 `bind` 指令来指定当前服务器绑定的地址：

```
bind host

bind 127.0.0.1
```

## 虚拟主机

如果我们需要配置独立的虚拟主机，需要将配置信息移动到站点名之后的大括号内：

```
mysite.com {
  root /www/mysite.com
}


sub.mysite.com {
  root /www/sub.mysite.com
  gzip
  log ../access.log
}
```

注意，左括号必须与站点名位于同一行，而右括号则是必须单起一行。对于共享相同配置的站点，我们可以用逗号来声明多个站点：

```
localhost:2020, https://site.com, http://mysite.com {
  ...
}
```

当 Caddy 检测到站点名符合下列条件时会自动使用 Let's Encrypt 脚本来为站点添加 HTTPS 支持，并且自动监听 80 与 443 端口：

- 主机名不可为空并且没有 localhost 与 IP 地址
- 端口号未明确指定为 80
- Scheme 未明确指定为 http
- TLS 未被关闭
- 未指明证书

## 缓存设置

我们可以通过 `expires` 指令来设置相较于请求时间的过期头，其基本语法为：

```yml
expires {
match regex duration
}
```

regex 是用于匹配请求文件的正则表达式，而 duration 则是 0y0m0d0h0i0s 格式的描述时长的表达式，常用的匹配语法为：

```
expires {
  match some/path/.*.css$ 1y # expires
  css files in some/path after one year
  match .js$ 1m # expires
  js files after 30 days
  match .png$ 1d # expires
  png files after one day
  match .jpg$ 1h # expires
  jpg files after one hour
  match .pdf$ 1i # expires
  pdf file after one minute
  match .txt$ 1s # expires
  txt files after one second
  match .html$ 5i30s # expires
  html files after 5 minutes 30 seconds
}
```

## 反向代理

`proxy` 指令提供了基本的反向代理功能，其支持 Health Checks 以及 Failovers，并且支持对于 WebSocket 的反向代理。其基本语法为：

```
proxy from to
```

from 即是请求匹配的基本路径，to 则是请求转发到的端点地址。我们也可以使用更复杂的配置：

```
proxy from to... {
  policy random | least_conn | round_robin | ip_hash
  fail_timeout duration
  max_fails integer
  try_duration duration
  try_interval duration
  health_check path
  health_check_interval interval_duration
  health_check_timeout timeout_duration
  header_upstream name value
  header_downstream name value
  keepalive number
  without prefix
  except ignored_paths...
  upstream to
  insecure_skip_verify
  preset
}
```

将所有发往 /api 的请求转发到后端系统：

```
# 这里会将完整的路径转发到后端，如果需要过滤掉前缀，则是以 without 指令
proxy /api localhost:9005
```

使用随机策略将所有请求负载均衡到三个后端服务器：

```
proxy / web1.local:80 web2.local:90 web3.local:100
```

使用循环机制：

```
proxy / web1.local:80 web2.local:90 web3.local:100 {
  policy round_robin
}
```

添加健康检查并且透明转发主机名、地址与上游：

```
proxy / web1.local:80 web2.local:90 web3.local:100 {
    policy round_robin
    health_check /health
    transparent
}
```

转发 WebSocket 请求：

```
proxy /stream localhost:8080 {
  websocket
}
```

避免对于部分静态请求的转发：

```
proxy / backend:1234 {
    # 这里取消 /static /robots.txt 方向的请求
  except /static /robots.txt
}
```

或者首先进行静态资源解析，然后进行二次跳转：

```
0.0.0.0
root /srv/build/
log / stdout
rewrite {path} /proxy/{path}
proxy /proxy backend {
    without /proxy
}
```

## WebSocket

Caddy 内建支持 WebSocket 连接，其允许客户端发起 WebSocket 连接的时候客户端执行某个简单的指令，其基本语法如下：

```
websocket [path] command
```

我们可以在客户端内构建简单的 WebSocket 客户端请求：

```js
if (window.WebSocket != undefined) {
  var connection = new WebSocket("ws://localhost:2015/echo");
  connection.onmessage = wsMessage;

  connection.onopen = wsOpen;

  function wsOpen(event) {
    connection.send("Hello World");
  }
  function wsMessage(event) {
    console.log(event.data);
  }
}
function wsMessage(event) {
  console.log(event.data);
}
```

然后在服务端接收该请求并且将客户端输入的内容返回：

```js
var readline = require("readline");
var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
  terminal: false,
});

rl.on("line", function (line) {
  console.log(line);
});
```

最后 Caddy 文件配置如下：

```
websocket /echo "node tmp.js"
```

## 文件上传

我们可以使用 Caddy 提供的扩展指令 upload 来搭建简单的文件上传服务器：

```
upload path {
  to"directory"
  yes_without_tls
  filenames_formnone|NFC|NFD
  filenames_inu0000–uff00 [u0000–uff00| …]
  hmac_keys_inkeyID_0=base64(binary) [keyID_n=base64(binary) | …]
  timestamp_tolerance 0..32
  silent_auth_errors
}
```

直接添加如下配置：

```
upload /web/path {
  to "/var/tmp"
}
```

然后使用 curl 上传文件：

```
# HTTP PUT
curl \
  -T /etc/os-release \
  https://127.0.0.1/web/path/from-release
```

或者同时上传多个文件：

```
# HTTP POST
curl \
  -F gitconfig=@.gitconfig \
  -F id_ed25519.pub=@.ssh/id_ed25519.pub \
  https://127.0.0.1/web/path/
```

我们也可以使用指令来移动或者删除这些文件：

```
# MOVE is 'mv'
curl -X MOVE \
  -H "Destination: /web/path/to-release" \
  https://127.0.0.1/web/path/from-release


# DELETE is 'rm -r'
curl -X DELETE \
  https://127.0.0.1/web/path/to-release
```

# 访问控制

## 权限认证

### Basic Auth

Caddy 内建支持 HTTP Basic Authentication，能够强制用户使用指定的用户名与密码访问某些目录或者文件。其基本配置语法如下：

```
basicauth username password {
  resources
}
```

如果我们希望为 /secret 目录下所有文件添加权限认证：

```
basicauth /secret Bob hiccup
```

也可以指明某些文件：

```
basicauth "Mary Lou" milkshakes {
  /notes-for-mary-lou.txt
  /marylou-files
  /another-file.txt
}
```

### JWT

jwt 指令是 Caddy 的扩展功能，我们需要在官网上选择添加该功能并且获取编译后的版本，其基本语法为：

```
jwt path
// 或者
jwt {
  pathresource
  allow claim value
  denyclaim value
}
```

譬如我们预设了两个令牌：`user: someone` 与 `role: member`，我们的配置项如下：

```
jwt {
  path/protected
  denyrole member
  allow user someone
}
```

该中间件会拒绝所有 `role: member` 的访问，除了用户名为 `someone` 的用户。而另一个 `role: admin` 或者 `role: foo` 的用户则可以正常访问。我们可以通过三种方式来提交令牌：

| Method               | Format                        |
| -------------------- | ----------------------------- |
| Authorization Header | Authorization: Bearer _token_ |
| Cookie               | "jwt*token": \_token*         |
| URL Query Parameter  | /protected?token=_token_      |

## 跨域请求

我们可以使用 cors 指令来为服务器添加跨域请求的能力：

```
cors / {
  originhttp://allowedSite.com
  originhttp://anotherSite.org https://anotherSite.org
  methods POST,PUT
  allow_credentials false
  max_age 3600
  allowed_headers X-Custom-Header,X-Foobar
  exposed_headers X-Something-Special,SomethingElse
}
```

我们也可以添加 JSONP 的支持：

```
jsonp /api/status
```

譬如某个端点返回类似于`{"status":"ok"}`这样的 JSON 响应，请求格式如下：

```
$ wget 'http://example.com/api/status?callback=func3022933'
```

其会返回如下格式的响应：

```
func3022933({"status":"ok"});
```

## 地址过滤

我们可以使用 ipfilter 指令来基于用户的 IP 来允许或者限制用户访问，其基本语法为：

```
ipfilter paths... {
  rule block | allow
  ip list or/and range of IPs...
  countrycountries ISO codes...
  database db_path
  blockpageblock_page
  strict
}
```

仅允许某个 IP 访问：

```
ipfilter / {
  rule allow
  ip 93.168.247.245
}
```

禁止两段 IP 地址与某个具体的 IP 访问，并且向他们返回默认界面：

```
ipfilter / {
  rule block
  ip 192.168.0.0/16 2E80::20:F8FF:FE31:77CF/16 5.23.4.24
  blockpage/local/data/default.html
}
```

仅允许来自法国的视野固定 IP 地址的客户端访问：

```
ipfilter / {
  ruleallow
  country FR
  database/local/data/GeoLite2-Country.mmdb
  ip99.23.4.24 2E80::20::FEF1:91C4
}
```

仅支持来自美国与日本的客户端访问：

```
ipfilter / {
  ruleallow
  country US JP
  database/local/data/GeoLite2-Country.mmdb
}
```

禁止来自美国与日本的客户端对于/notglobal 与 /secret 的访问，直接返回默认地址：

```
ipfilter /notglobal /secret {
  rule block
  countryUS JP
  database /local/data/GeoLite2-Country.mmdb
  blockpage/local/data/default.html
}
```

## 请求限流

我们可以使用 ratelimit 这个扩展指令来为资源添加请求限流的功能，对于单资源可以使用如下指令：

```
ratelimit path rate burst unit

// 限制客户端每秒最多对于 /r 资源发起两个请求，突发上限最多为 3 个
ratelimit /r 2 3 second
```

对于多资源可以使用如下指令：

```
ratelimit rate burst unit {
  resources
}
// 限制对于资源文件的访问时长为 2 分钟
ratelimit 2 2 minute {
  /foo.html
  /dir
}
```
