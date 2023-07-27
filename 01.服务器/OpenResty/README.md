# OpenResty: 基于 Nginx 的全功能 Web 应用服务器

OpenResty (也称为 ngx_openresty)是一个全功能的 Web 应用服务器。它打包了标准的 [Nginx](undefined) 核心，很多的常用的[第三方模块](http://wiki.nginx.org/3rdPartyModules)，以及它们的大多数依赖项。通过众多进行良好设计的 Nginx 模块，OpenResty 有效地把 Nginx 服务器转变为一个强大的 Web 应用服务器，基于它开发人员可以使用 Lua 编程语言对 Nginx 核心以及现有的各种 Nginx C 模块进行脚本编程，构建出可以处理一万以上并发请求的极端高性能的 Web 应用。OpenResty 致力于将你的服务器端应用完全运行于 Nginx 服务器中，充分利用 Nginx 的事件模型来进行非阻塞 IO 通信。不仅仅是和 HTTP 客户端间的网络通信是非阻塞的，与 MySQL、PostgreSQL、Memcached、以及 Redis 等众多远方后端之间的网络通信也是非阻塞的。因为 OpenResty 软件包的维护者也是其中打包的许多 Nginx 模块的作者，所以 OpenResty 可以确保所包含的所有组件可以可靠地协同工作。

# 安装与部署

# HttpClient

## [lua-resty-http](https://github.com/pintsized/lua-resty-http)

- HTTP 1.0 and 1.1
- Streaming interface to reading bodies using coroutines, for predictable memory usage in Lua land.
- Alternative simple interface for singleshot requests without manual connection step.
- Headers treated case insensitively.
- Chunked transfer encoding.
- Keepalive.
- Pipelining.
- Trailers.

```lua
lua_package_path "/path/to/lua-resty-http/lib/?.lua;;";

server {


  location /simpleinterface {
    resolver 8.8.8.8;  # use Google's open DNS server for an example

    content_by_lua '

      -- For simple singleshot requests, use the URI interface.
      local http = require "resty.http"
      local httpc = http.new()
      local res, err = httpc:request_uri("http://example.com/helloworld", {
        method = "POST",
        body = "a=1&b=2",
        headers = {
          ["Content-Type"] = "application/x-www-form-urlencoded",
        }
      })

      if not res then
        ngx.say("failed to request: ", err)
        return
      end

      -- In this simple form, there is no manual connection step, so the body is read
      -- all in one go, including any trailers, and the connection closed or keptalive
      -- for you.

      ngx.status = res.status

      for k,v in pairs(res.headers) do
          --
      end

      ngx.say(res.body)
    ';
  }


  location /genericinterface {
    content_by_lua '

      local http = require "resty.http"
      local httpc = http.new()

      -- The generic form gives us more control. We must connect manually.
      httpc:set_timeout(500)
      httpc:connect("127.0.0.1", 80)

      -- And request using a path, rather than a full URI.
      local res, err = httpc:request{
          path = "/helloworld",
          headers = {
              ["Host"] = "example.com",
          },
      }

      if not res then
        ngx.say("failed to request: ", err)
        return
      end

      -- Now we can use the body_reader iterator, to stream the body according to our desired chunk size.
      local reader = res.body_reader

      repeat
        local chunk, err = reader(8192)
        if err then
          ngx.log(ngx.ERR, err)
          break
        end

        if chunk then
          -- process
        end
      until not chunk

      local ok, err = httpc:set_keepalive()
      if not ok then
        ngx.say("failed to set keepalive: ", err)
        return
      end
    ';
  }
}
```

# 存储

## [Redis](https://github.com/openresty/lua-resty-redis)

```conf
# you do not need the following line if you are using
    # the OpenResty bundle:
    lua_package_path "/path/to/lua-resty-redis/lib/?.lua;;";

    server {
        location /test {
            content_by_lua '
                local redis = require "resty.redis"
                local red = redis:new()

                red:set_timeout(1000) -- 1 sec

                -- or connect to a unix domain socket file listened
                -- by a redis server:
                --     local ok, err = red:connect("unix:/path/to/redis.sock")

                local ok, err = red:connect("127.0.0.1", 6379)
                if not ok then
                    ngx.say("failed to connect: ", err)
                    return
                end

                ok, err = red:set("dog", "an animal")
                if not ok then
                    ngx.say("failed to set dog: ", err)
                    return
                end

                ngx.say("set result: ", ok)

                local res, err = red:get("dog")
                if not res then
                    ngx.say("failed to get dog: ", err)
                    return
                end

                if res == ngx.null then
                    ngx.say("dog not found.")
                    return
                end

                ngx.say("dog: ", res)

                red:init_pipeline()
                red:set("cat", "Marry")
                red:set("horse", "Bob")
                red:get("cat")
                red:get("horse")
                local results, err = red:commit_pipeline()
                if not results then
                    ngx.say("failed to commit the pipelined requests: ", err)
                    return
                end

                for i, res in ipairs(results) do
                    if type(res) == "table" then
                        if res[1] == false then
                            ngx.say("failed to run command ", i, ": ", res[2])
                        else
                            -- process the table value
                        end
                    else
                        -- process the scalar value
                    end
                end

                -- put it into the connection pool of size 100,
                -- with 10 seconds max idle time
                local ok, err = red:set_keepalive(10000, 100)
                if not ok then
                    ngx.say("failed to set keepalive: ", err)
                    return
                end

                -- or just close the connection right away:
                -- local ok, err = red:close()
                -- if not ok then
                --     ngx.say("failed to close: ", err)
                --     return
                -- end
            ';
        }
    }
```
