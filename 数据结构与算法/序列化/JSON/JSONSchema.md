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

- [`$schema`](http://json-schema.org/latest/json-schema-core.html#rfc.section.7) 关键字指出此模式是根据标准的特定草案编写的，出于各种原因（主要是版本控制）使用。

- [`$id`](http://json-schema.org/latest/json-schema-core.html#rfc.section.8.2) 关键字定义架构的 URI，以及解析架构中其他 URI 引用所依据的基本 URI。

- [`title`](http://json-schema.org/latest/json-schema-validation.html#rfc.section.10.1) and [`description`](http://json-schema.org/latest/json-schema-validation.html#rfc.section.10.1) 关键字仅是描述性的。 它们不会对要验证的数据增加约束，用这两个关键字说明了 Schema 的意图。

- [`type`](http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.1.1) 关键字定义了对我们的 JSON 数据的第一个约束，在这种情况下，它必须是 JSON 对象。

# 链接

- http://json-schema.org/learn/getting-started-step-by-step.html
