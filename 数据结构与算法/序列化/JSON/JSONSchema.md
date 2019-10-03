# JSONSchema

JSON Schema 用以标注和验证 JSON 文档的元数据的文档，可以类比于 XML Schema。相对于 JSON Schema，一个 JSON 文档就是 JSON Schema 的一个 instance。

![JSON Schema 概念](https://s2.ax1x.com/2019/09/02/nP1GSs.png)

如下是简单的 JSON Schema 的定义：

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "http://example.com/product.schema.json",
  "title": "Product",
  "description": "A product in the catalog",
  "type": "object"
}
```

# 链接

- http://json-schema.org/learn/getting-started-step-by-step.html
