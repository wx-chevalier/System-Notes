# Nginx 初窥与部署

# 环境部署

```s
# CentOS
yum install nginx;
# Ubuntu
sudo apt-get install nginx;
# Mac
brew install nginx;
```

## Management

```
# 启动
nginx -s start;
# 重新启动，热启动，修改配置重启不影响线上
nginx -s reload;
# 关闭
nginx -s stop;
# 修改配置后，可以通过下面的命令测试是否有语法错误
nginx -t;
```

### Start

```
cd usr/local/nginx/sbin
./nginx
```

### Reload

```
更改配置重启nginx
kill -HUP 主进程号或进程号文件路径
或者使用
cd /usr/local/nginx/sbin
./nginx -s reload
    判断配置文件是否正确
nginx -t -c /usr/local/nginx/conf/nginx.conf
或者
cd  /usr/local/nginx/sbin
./nginx -t
```

### Stop

查询 nginx 主进程号　　 ps -ef | grep nginx
　　从容停止 kill -QUIT 主进程号　　快速停止 kill -TERM 主进程号　　强制停止 kill -9 nginx
　　若 nginx.conf 配置了 pid 文件路径，如果没有，则在 logs 目录下　　 kill -信号类型 '/usr/local/nginx/logs/nginx.pid'

## Configuration

```
events {
    # 需要保留这一个段落，可以为空
}
http {
    server {
        listen 127.0.0.1:8888;
        location / {
            root /home/barret/test/;
        }
    }
}
```

# Basic Configuration(基本配置)

## 运行配置

`#定义 Nginx 运行的用户和用户组

user www www; `

```
#nginx进程数，建议设置为等于CPU总核心数。

worker_processes 8;
#全局错误日志定义类型，[ debug | info | notice | warn | error | crit ]

error_log /var/log/nginx/error.log info;
#进程文件

pid /var/run/nginx.pid;
#一个nginx进程打开的最多文件描述符数目，理论值应该是最多打开文件数(系统的值ulimit -n)与nginx进程数相除，但是nginx分配请求并不均匀，所以建议与ulimit -n的值保持一致。

worker_rlimit_nofile 65535;
#工作模式与连接数上限

events

{

#参考事件模型，use [ kqueue | rtsig | epoll | /dev/poll | select | poll ]; epoll模型是Linux 2.6以上版本内核中的高性能网络IO模型，如果跑在FreeBSD上面，就用kqueue模型。

use epoll;

#单个进程最大连接数(最大连接数=连接数*进程数)

worker_connections 65535;

}
```

## HTTP 服务器配置

```
设定http服务器
http
{
include mime.types; #文件扩展名与文件类型映射表
default_type application/octet-stream; #默认文件类型
#charset utf-8; #默认编码
server_names_hash_bucket_size 128; #服务器名字的hash表大小
client_header_buffer_size 32k; #上传文件大小限制
large_client_header_buffers 4 64k; #设定请求缓
client_max_body_size 8m; #设定请求缓
sendfile on; #开启高效文件传输模式，sendfile指令指定nginx是否调用sendfile函数来输出文件，对于普通应用设为 on，如果用来进行下载等应用磁盘IO重负载应用，可设置为off，以平衡磁盘与网络IO处理速度，降低系统的负载。注意：如果图片显示不正常把这个改成off。
autoindex on; #开启目录列表访问，合适下载服务器，默认关闭。
tcp_nopush on; #防止网络阻塞
tcp_nodelay on; #防止网络阻塞
keepalive_timeout 120; #长连接超时时间，单位是秒

#FastCGI相关参数是为了改善网站的性能：减少资源占用，提高访问速度。下面参数看字面意思都能理解。
fastcgi_connect_timeout 300;
fastcgi_send_timeout 300;
fastcgi_read_timeout 300;
fastcgi_buffer_size 64k;
fastcgi_buffers 4 64k;
fastcgi_busy_buffers_size 128k;
fastcgi_temp_file_write_size 128k;

#gzip模块设置
gzip on; #开启gzip压缩输出
gzip_min_length 1k; #最小压缩文件大小
gzip_buffers 4 16k; #压缩缓冲区
gzip_http_version 1.0; #压缩版本(默认1.1，前端如果是squid2.5请使用1.0)
gzip_comp_level 2; #压缩等级
gzip_types text/plain application/x-javascript text/css application/xml;
#压缩类型，默认就已经包含text/html，所以下面就不用再写了，写上去也不会有问题，但是会有一个warn。
gzip_vary on;
#limit_zone crawler $binary_remote_addr 10m; #开启限制IP连接数的时候需要使用
}
```

## 内置全局变量

名称 版本 说明(变量列表来源于文件 ngx_http_variables )

\$args 1.0.8 请求中的参数;

\$binary_remote_addr 1.0.8 远程地址的二进制表示

\$body_bytes_sent 1.0.8 已发送的消息体字节数

\$content_length 1.0.8 HTTP 请求信息里的"Content-Length";

\$content_type 1.0.8 请求信息里的"Content-Type";

\$document_root 1.0.8 针对当前请求的根路径设置值;

$document_uri 1.0.8 与$uri 相同; 比如 /test1/test2/test.php

\$host 1.0.8 请求信息中的"Host"，如果请求中没有 Host 行，则等于设置的服务器名;

\$hostname 1.0.8

\$http_cookie 1.0.8 cookie 信息

\$http_post 1.0.8

\$http_referer 1.0.8 引用地址

\$http_user_agent 1.0.8 客户端代理信息

\$http_via 1.0.8 最后一个访问服务器的 Ip 地址。http://www.cnblogs.com/deng02/archive/2009/02/11/1387911.html

\$http_x_forwarded_for 1.0.8 相当于网络访问路径。http://www.cnblogs.com/craig/archive/2008/11/18/1335809.html

\$is_args 1.0.8

\$limit_rate 1.0.8 对连接速率的限制;

\$nginx_version 1.0.8

\$pid 1.0.8

$query_string 1.0.8 与$args 相同;

\$realpath_root 1.0.8

\$remote_addr 1.0.8 客户端地址;

\$remote_port 1.0.8 客户端端口号;

\$remote_user 1.0.8 客户端用户名，认证用;

\$request 1.0.8 用户请求

\$request_body 1.0.8

\$request_body_file 1.0.8 发往后端的本地文件名称

\$request_completion 1.0.8

$request_filename 1.0.8 当前请求的文件路径名，比如$request_filename：D:\nginx/html/test1/test2/test.php

\$request_method 1.0.8 请求的方法，比如"GET"、"POST"等;

\$request_uri 1.0.8 请求的 URI，带参数; 比如http://localhost:88/test1/test2/test.php

$scheme 1.0.8 所用的协议，比如 http 或者是 https，比如 rewrite^(.+)$$scheme://example.com$1redirect;

\$sent_http_cache_control 1.0.8

\$sent_http_connection 1.0.8

\$sent_http_content_length 1.0.8

\$sent_http_content_type 1.0.8

\$sent_http_keep_alive 1.0.8

\$sent_http_last_modified 1.0.8

\$sent_http_location 1.0.8

\$sent_http_transfer_encoding 1.0.8

\$server_addr 1.0.8 服务器地址，如果没有用 listen 指明服务器地址，使用这个变量将发起一次系统调用以取得地址(造成资源浪费);

\$server_name 1.0.8 请求到达的服务器名;

\$server_port 1.0.8 请求到达的服务器端口号;

\$server_protocol 1.0.8 请求的协议版本，"HTTP/1.0"或"HTTP/1.1";

\$uri 1.0.8 请求的 URI，可能和最初的值有不同，比如经过重定向之类的。

# 虚拟主机与负载均衡

```
upstream blog.ha97.com {
#upstream的负载均衡，weight是权重，可以根据机器配置定义权重。weigth参数表示权值，权值越高被分配到的几率越大。
server 192.168.80.121:80 weight=3;
server 192.168.80.122:80 weight=2;
server 192.168.80.123:80 weight=3;
}

# 反向代理
    server {
    listen 80;
    server_name www.001.com;
      location / {
      proxy_pass http://192.168.84.129; //后端ip地址
      proxy_redirect off; //关闭后端返回的header修改
      proxy_set_header Host $host; //修改发送到后端的header的host
      proxy_set_header X-Real-IP $remote_addr; //设置真实ip
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      }
    }

    server {
    listen 80;
    server_name www.002.com;
      location / {
      proxy_pass http://192.168.84.128; //后端ip地址
      proxy_redirect off; //关闭后端返回的header修改
      proxy_set_header Host $host; //修改发送到后端的header的host
      proxy_set_header X-Real-IP $remote_addr; //设置真实ip
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      }
    }

多个域名可以绑定到一个反向代理中。

#虚拟主机的配置
server
{
#监听端口
listen 80;
#域名可以有多个，用空格隔开
server_name www.ha97.com ha97.com;
index index.html index.htm index.php;
root /data/www/ha97;
location ~ .*\.(php|php5)?$
{
fastcgi_pass 127.0.0.1:9000;
fastcgi_index index.php;
include fastcgi.conf;
}
#图片缓存时间设置
location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$
{
expires 10d;
}
#JS和CSS缓存时间设置
location ~ .*\.(js|css)?$
{
expires 1h;
}
#日志格式设定
log_format access '$remote_addr - $remote_user [$time_local] "$request" '
'$status $body_bytes_sent "$http_referer" '
'"$http_user_agent" $http_x_forwarded_for';
#定义本虚拟主机的访问日志
access_log /var/log/nginx/ha97access.log access;

#对 "/" 启用反向代理
location / {
proxy_pass http://127.0.0.1:88;
proxy_redirect off;
proxy_set_header X-Real-IP $remote_addr;
#后端的Web服务器可以通过X-Forwarded-For获取用户真实IP
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
#以下是一些反向代理的配置，可选。
proxy_set_header Host $host;
client_max_body_size 10m; #允许客户端请求的最大单文件字节数
client_body_buffer_size 128k; #缓冲区代理缓冲用户端请求的最大字节数，
proxy_connect_timeout 90; #nginx跟后端服务器连接超时时间(代理连接超时)
proxy_send_timeout 90; #后端服务器数据回传时间(代理发送超时)
proxy_read_timeout 90; #连接成功后，后端服务器响应时间(代理接收超时)
proxy_buffer_size 4k; #设置代理服务器(nginx)保存用户头信息的缓冲区大小
proxy_buffers 4 32k; #proxy_buffers缓冲区，网页平均在32k以下的设置
proxy_busy_buffers_size 64k; #高负荷下缓冲大小(proxy_buffers*2)
proxy_temp_file_write_size 64k;
#设定缓存文件夹大小，大于这个值，将从upstream服务器传
}

#设定查看Nginx状态的地址
location /NginxStatus {
stub_status on;
access_log on;
auth_basic "NginxStatus";
auth_basic_user_file conf/htpasswd;
#htpasswd文件的内容可以用apache提供的htpasswd工具来产生。
}

#本地动静分离反向代理配置
#所有jsp的页面均交由tomcat或resin处理
location ~ .(jsp|jspx|do)?$ {
proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_pass http://127.0.0.1:8080;
}
#所有静态文件由nginx直接读取不经过tomcat或resin
location ~ .*.(htm|html|gif|jpg|jpeg|png|bmp|swf|ioc|rar|zip|txt|flv|mid|doc|ppt|pdf|xls|mp3|wma)$
{ expires 15d; }
location ~ .*.(js|css)?$
{ expires 1h; }
}
```

# 代理与重定向

## Rewrite(重定向)

**nginx rewrite 正则表达式匹配**

\*\*]大小写匹配\*\*\*

~ 为区分大小写匹配

~\* 为不区分大小写匹配

!~和!~\*分别为区分大小写不匹配及不区分大小写不匹配

\*\*]文件及目录匹配\*\*\*

-f 和!-f 用来判断是否存在文件

-d 和!-d 用来判断是否存在目录

-e 和!-e 用来判断是否存在文件或目录

-x 和!-x 用来判断文件是否可执行

\*\*]flag 标记\*\*\*

last 相当于 Apache 里的[L]标记，表示完成 rewrite

break 终止匹配, 不再匹配后面的规则。

redirect 返回 302 临时重定向 地址栏会显示跳转后的地址。

permanent 返回 301 永久重定向 地址栏会显示跳转后的地址。

logcation 的几个使用实例：

1)location / { }：匹配任何查询，因为所有请求都以 / 开头。但是正则表达式规则将被优先和查询匹配。

2)location =/ {}：仅仅匹配/

3)location ~\\\\\\\\\\\* \.(gif|jpg|jpeg)\$

     ｛

        rewrite \.(gif|jpg)$ /logo.png;

     ｝：location不区分大小写，匹配任何以gif，jpg，jpeg结尾的文件。

几个实例：

**多目录转成参数 **

要求：abc.domian.com/sort/2 => abc.domian.com/index.php?act=sort&name=abc&id=2

规则配置：

if (\$host ~_ (._)\.domain\.com) {

    set $sub_name $1;

    rewrite ^/sort\/(\d+)\/?$ /index.php?act=sort&cid=$sub_name&id=$1 last;

}

**目录对换**

要求：/123456/xxxx -> /xxxx?id=123456

规则配置：

rewrite ^/(\d+)/(.+)/ /$2?id=$1 last;

再来一个针对浏览器优化的自动 rewrite，这里 rewrite 后的目录可以是存在的；

例如设定 nginx 在用户使用 ie 的使用重定向到/nginx-ie 目录

规则如下：

if (\$http_user_agent ~ MSIE) {

     rewrite ^(.*)$ /nginx-ie/$1 break;

}

**目录自动加“/”，这个功能一般浏览器自动完成**

if (-d \$request_filename){

rewrite ^/(.\*)([^/])$ http://$host/$1$2/ permanent;

}

以下这些可能就跟广义的 rewrite 重写无关了

**禁止 htaccess**

location ~/\.ht {

    deny all;

}

**禁止多个目录 **

location ~ ^/(cron|templates)/ {

    deny all; break;

}

**禁止以/data 开头的文件，可以禁止/data/下多级目录下.log.txt 等请求**

location ~ ^/data {

    deny all;

}

禁止单个文件

location ~ /data/sql/data.sql {

    deny all;

}

给 favicon.ico 和 robots.txt 设置过期时间; 这里为 favicon.ico 为 99 天,robots.txt 为 7 天并不记录 404 错误日志

location ~(favicon.ico) {

    log_not_found off;

    expires 99d;

    break;

}

location ~(robots.txt) {

    log_not_found off;

    expires 7d;

    break;

}

设定某个文件的浏览器缓存过期时间;这里为 600 秒，并不记录访问日志

location ^~ /html/scripts/loadhead_1.js {

    access_log off;

    expires 600;

    break;

}

Nginx 还可以自定义某一类型的文件的保质期时间，具体写法看下文的代码：

location ~\* \.(js|css|jpg|jpeg|gif|png|swf)\$ {

if (-f \$request_filename) {

expires 1h;

break;

}

}

//上段代码就将 js|css|jpg|jpeg|gif|png|swf 这类文件的保质期设置为一小时。

**防盗链的设置：**

防盗链：如果你的网站是个下载网站，下载步骤应该是先经过你的主页找到下载地址，才能下载，为了防止某些网友直接访问下载地址完全不通过主页下载，我们就可以使用防盗链的方式，具体代码如下：

location ~\* \.(gif|jpg|swf)\$ {

valid_referers none blocked start.igrow.cn sta.igrow.cn;

if (\$invalid_referer) {

rewrite ^/ http://$host/logo.png;

}

}

**文件反盗链并设置过期时间--<盗链多次请求也会打开你的站点的图片啊，所以设置下缓存时间，不会每次盗链都请求并下载这张图片>**

location ~\* ^.+\.(jpg|jpeg|gif|png|swf|rar|zip|css|js)\$ {

    valid_referers none blocked *.jjonline.cn *.jjonline.com.cn *.lanwei.org *.jjonline.org localhost  42.121.107.189;

    if ($invalid_referer) {

        rewrite ^/ http://img.jjonline.cn/forbid.gif;

        return 417;

        break;

    }

    access_log off;

    break;

}

说明：

这里的 return 417 为自定义的 http 状态码，默认为 403，方便通过 nginx 的 log 文件找出正确的盗链的请求地址

“rewrite ^/ http://img.jjonline.cn/forbid.gif;”显示一张防盗链图片

“access_log off;”不记录访问日志，减轻压力

“expires 3d”所有文件 3 天的浏览器缓存

**只充许固定 ip 访问网站，并加上密码；这个对有权限认证的应用比较在行**

location \ {

    allow 22.27.164.25; #允许的ipd

    deny all;

    auth_basic “KEY”; #认证的一些设置

    auth_basic_user_file htpasswd;

}

说明：location 的应用也有各种变化，这里的写法就针对了根目录了。

**文件和目录不存在的时重定向**

if (!-e \$request_filename) {

    #proxy_pass http://127.0.0.1; #这里是跳转到代理ip，这个代理ip上有一个监听的web服务器

    rewrite ^/ http://www.jjonline.cn/none.html;  #跳转到这个网页去

    #return 404; #直接返回404码，然后会寻找root指定的404.html文件

}

**域名跳转 **

server {

    listen 80;

    server_name jump.jjonline.cn #需要跳转的多级域名

    index index.html index.htm index.php; #入口索引文件的名字

    root /var/www/public_html/; #这个站点的根目录

    rewrite ^/ http://www.jjonline.cn/;

    #rewrite到这个地址，功能表现：在浏览器上输入jump.jjonline.cn并回车，不会有任何提示直接变成www.jjonline.cn

    access_log off;

}

**多域名转向**

server {

    listen 80;



    server_name www.jjonline.cn www.jjonline.org;

    index index.html index.htm index.php;

    root /var/www/public_html/;

    if ($host ~ “jjonline\.org”) {

        rewrite ^(.*) http://www.jjonline.cn$1 permanent;

    }

}

**三级域名跳转 **

if ($http_host ~_ “^(._)\.i\.jjonline\.cn$”) {

    rewrite ^(.*) http://demo.jjonline.cn$1;

    break;

}

**域名镜向**

server {

    listen 80;

    server_name mirror.jjonline.cn;

    index index.html index.htm index.php;

    root /var/www/public_html;

    rewrite ^/(.*) http://www.jjonline.cn/$1 last;

    access_log off;

}

**某个子目录作镜向,这里的示例是 demo 子目录**

location ^~ /demo {

    rewrite ^.+ http://demo.jjonline.cn/ last;

    break;

}

以下在附带本博客的 rewrite 写法，emlog 系统的 rewrite

location ~ {

    if (!-e $request_filename) {

           rewrite ^/(.+)$ /index.php last;

    }

}
