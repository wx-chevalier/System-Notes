# B-Tree

为了描述 B-Tree，首先定义一条数据记录为一个二元组[key, data]，key 为记录的键值，对于不同数据记录，key 是互不相同的；data 为数据记录除 key 外的数据。

那么 B-Tree 是满足下列条件的数据结构：

- 一个节点中的 key 从左到右非递减排列。所有节点组成树结构。每个指针要么为 null，要么指向另外一个节点。

- 每个叶节点最少包含一个 key 和两个指针，最多包含 2d-1 个 key 和 2d 个指针。key 和指针互相间隔，节点两端是指针。

- 叶子节点的指针均为 null，所有子叶节点具有相同的深度，等于树高 h。

B-Tree 还中的父子节点还满足以下排序规则：

- 如果某个指针在节点 node 最左边且不为 null，则其指向节点的所有 key 小于 v(key1)，其中 v(key1)为 node 的第一个 key 的值。

- 如果某个指针在节点 node 最右边且不为 null，则其指向节点的所有 key 大于 v(keym)，其中 v(keym)为 node 的最后一个 key 的值。

- 如果某个指针在节点 node 的左右相邻 key 分别是 keyi 和 keyi+1 且不为 null，则其指向节点的所有 key 小于 v(keyi+1)且大于 v(keyi)。

![](https://ww1.sinaimg.cn/large/007rAy9hly1g162uiayzzj30cq037jrf.jpg)

由于 B-Tree 的特性，在 B-Tree 中按 key 检索数据的算法非常直观：首先从根节点进行二分查找，如果找到则返回对应节点的 data，否则对相应区间的指针指向的节点递归进行查找，直到找到节点或找到 null 指针，前者查找成功，后者查找失败。B-Tree 上查找算法的伪代码如下：

```c
BTree_Search(node,key) {
    if(node == null) returnnull;
    foreach(node.key){
        if(node.key[i]== key) return node.data[i];
        if(node.key[i]> key) return BTree_Search(point[i]->node, key);
    }
    return BTree_Search(point[i+1]->node, key);
}
data =BTree_Search(root, my_key);
```

关于 B-Tree 有一系列有趣的性质，例如一个度为 d 的 B-Tree，设其索引 N 个 key，则其树高 h 的上限为 logd((N+1)/2)，检索一个 key，其查找节点个数的渐进复杂度为 O(logdN)。从这点可以看出，B-Tree 是一个非常有效率的索引数据结构。

# B+Tree

B-Tree 有许多变种，其中最常见的是 B+Tree，例如 MySQL 就普遍使用 B+Tree 实现其索引结构。与 B-Tree 相比，B+Tree 有以下不同点：

- 每个节点的指针上限为 2d 而不是 2d+1。

- 内节点不存储 data，只存储 key；叶子节点不存储指针。

下图是一个简单的 B+Tree 示意。

![](https://ww1.sinaimg.cn/large/007rAy9hly1g162uiayzzj30cq037jrf.jpg)

由于并不是所有节点都具有相同的域，因此 B+Tree 中叶节点和内节点一般大小不同。这点与 B-Tree 不同，虽然 B-Tree 中不同节点存放的 key 和指针可能数量不一致，但是每个节点的域和上限是一致的，所以在实现中 B-Tree 往往对每个节点申请同等大小的空间。

一般来说，B+Tree 比 B-Tree 更适合实现外存储索引结构。
