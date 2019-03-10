# Tree Based Search Algorithms: 基于树的搜索算法

- AVL: 最早的平衡二叉树之一 , 早期有应用在 linux 内核上，后来被 RBtree 代替了。两者都保持 log(n) 的插入与查询，是平衡的 BST，不会出现 (n2) 的糟糕情况。但是 AVL 是一种高度平衡的二叉树，所以通常的结果是，维护这种高度平衡所付出的代价比从中获得的效率收益还大，故而实际的应用不多。如果场景中对插入删除不频繁，只是对查找特别有要求，AVL 还是优于红黑的。

- 红黑树 : 广泛用在 C++ 的 STL 中 ,map 和 set 都是用红黑树实现的。其他的著名的 linux 进程调度 Completely Fair Scheduler, 用红黑树管理进程控制块、epoll 在内核中的实现，用红黑树管理事件块、nginx 中，用红黑树管理 timer 等、Java 的 TreeMap 实现。

- B/B+: 运用在 file system database 这类持续存储结构，同样能保持 lon(n) 的插入与查询，也需要额外的平衡调节。像 mysql 的数据库定义是可以指定 B+ 索引还是 hash 索引。

- Tire: 大都用在 word 的匹配，但单纯的 trie 内存消耗很大，建 trie 树也需要些时间，通常用在带词典的机械分词，jieba 分词就是建立在 trie 上匹配的，trie 有其他变体可以压缩空间，像 double array trie 这类比较老且经典的压缩方法，也有其他比较新的压缩方式，看论文时有看过，没自己实现过所以不断言了，其实面对多模匹配 trie 没有其变体 aho-corasick 来得理想，另外 aho-corasick 也是可以用巧妙的方法来进行压缩空间，这里不再展开，毕竟手机码字，同时想基数树与其也类似，在 nginx 上有应用，说到 aho-corasick 其实早期的入侵检测工具 snort 也有应用实现，但如今改成 wu-menber 了，具体记不清了，其实 trie 还是挺有用的，Tengine 也用 trie 实现了了匹配模块。但要是用在大量单词的匹配上确实吃内存。
