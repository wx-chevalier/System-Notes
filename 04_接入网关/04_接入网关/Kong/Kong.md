# 基于 Kong 的微服务网关架构

Kong = OpenResty + Nginx，我们可以直接在 Docker 上启动 Kong 实例：

```sh
# 使用 Postgres 作为数据存储
$ docker run -d --name kong-database \
              -p 5432:5432 \
              -e "POSTGRES_USER=kong" \
              -e "POSTGRES_DB=kong" \
              postgres:9.4

# 进行数据库迁移
$ docker run --rm \
    --link kong-database:kong-database \
    -e "KONG_DATABASE=postgres" \
    -e "KONG_PG_HOST=kong-database" \
    -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
    kong:latest kong migrations up

# 启动 Kong
$ docker run -d --name kong \
    --link kong-database:kong-database \
    -e "KONG_DATABASE=postgres" \
    -e "KONG_PG_HOST=kong-database" \
    -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
    -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
    -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
    -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_LISTEN=0.0.0.0:8001" \
    -e "KONG_ADMIN_LISTEN_SSL=0.0.0.0:8444" \
    -p 8000:8000 \
    -p 8443:8443 \
    -p 8001:8001 \
    -p 8444:8444 \
    kong:latest

# 判断 Kong 是否启动成功
$ curl -i http://localhost:8001/


# 使用 Konga 作为界面化管理
# Admin login: admin | password: adminadminadmin
$ docker run -p 1337:1337 \
             --link kong:kong \
             --name konga \
             -e "NODE_ENV=production" \
             pantsel/konga
```

Kong 使用的各个端口的作用依次为：
:8000 on which Kong listens for incoming HTTP traffic from your clients, and forwards it to your upstream services.
:8443 on which Kong listens for incoming HTTPS traffic. This port has a similar behavior as the :8000 port, except that it expects HTTPS traffic only. This port can be disabled via the configuration file.
:8001 on which the Admin API used to configure Kong listens.
:8444 on which the Admin API listens for HTTPS traffic.
Kong 启动完毕后，可以在 Konga 或者直接以 API 请求的方式注册与消费 API：

```sh
# 注册新的 API
$ curl -i -X POST \
  --url http://localhost:8001/apis/ \
  --data 'name=example-api' \
  --data 'hosts=example.com' \
  --data 'upstream_url=http://mockbin.org'

# 添加插件
$ curl -i -X POST \
  --url http://localhost:8001/apis/example-api/plugins/ \
  --data 'name=rate-limiting' \
  --data 'config.minute=100'

# 尝试进行访问
$ curl -i -X GET \
--url http://localhost:8000/ \
--header 'Host: example.com'
```
