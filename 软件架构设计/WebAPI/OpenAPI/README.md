[![返回目录](https://i.postimg.cc/WzXsh0MX/image.png)](https://parg.co/UdT)

# OpenAPI

OpenAPI 规范（以前称为 Swagger 规范）是 REST API 的 API 描述格式。OpenAPI 文件允许您描述整个 API，包括：每个端点上的可用端点（/users）和操作（GET /users，POST /users）、操作参数每个操作的输入和输出、认证方法、联系信息，许可证，使用条款和其他信息、API 规范可以用 YAML 或 JSON 编写。

```yaml
openapi: 3.0.0

info:
  title: A simplified version of fromAtoB’s backend API
  version: '1.0'

paths:
  /lookup:
    get:
      description: Look up the coordinates for a city by name.
      parameters:
        - in: query
          name: name
          schema:
            type: string
          description: City name.
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Coordinate'
        '404':
          description: Not Found
          content:
            text/plain:
              schema:
                type: string

components:
  schemas:
    Coordinate:
      type: object
      description: A Coordinate identifies a location on Earth by latitude and longitude.
      properties:
        latitude:
          type: number
          description: Latitude is the degrees latitude of the location, in the range [-90, 90].
        longitude:
          type: number
          description: Longitude is the degrees longitude of the location, in the range [-180, 180].
```

# 链接

- https://www.breakyizhan.com/swagger/2806.html
