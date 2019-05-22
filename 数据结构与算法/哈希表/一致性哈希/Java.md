# Java 中一致性哈希算法的实现

我们可以选择用某个有序数组或者 TreeMap 来模拟一致性哈希中的环：

- 初始化一个长度为 N 的数组。
- 将服务节点通过 hash 算法得到的正整数，同时将节点自身的数据（hashcode、ip、端口等）存放在这里。
- 完成节点存放后将整个数组进行排序（排序算法有多种）。
- 客户端获取路由节点时，将自身进行 hash 也得到一个正整数；
- 遍历这个数组直到找到一个数据大于等于当前客户端的 hash 值，就将当前节点作为该客户端所路由的节点。
- 如果没有发现比客户端大的数据就返回第一个节点（满足环的特性）。

只使用了 TreeMap 的一些 API：

- 写入数据候， TreeMap 可以保证 key 的自然排序。
- tailMap 可以获取比当前 key 大的部分数据。
- 当这个方法有数据返回时取第一个就是顺时针中的第一个节点了。
- 如果没有返回那就直接取整个 Map 的第一个节点，同样也实现了环形结构。

# 链接

- https://mp.weixin.qq.com/s?__biz=MzIyMzgyODkxMQ==&mid=2247484108&idx=1&sn=87df95335ca97aa5d44475fc2c8d1e4b&scene=21#wechat_redirect
- http://blog.carlosgaldino.com/consistent-hashing.html
