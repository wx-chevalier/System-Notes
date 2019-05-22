# Go 中一致性哈希算法的实现与应用

```go
func (m *Map) Add(nodes ...string) {
    for _, n := range nodes {
        for i := 0; i < m.replicas; i++ {
            hash := int(m.hash([]byte(strconv.Itoa(i) + " " + n)))
            m.nodes = append(m.nodes, hash)
            m.hashMap[hash] = n
        }
    }
    sort.Ints(m.nodes)
}
```

```go
func (m *Map) Get(key string) string {
    hash := int(m.hash([]byte(key)))
    idx := sort.Search(len(m.keys),
        func(i int) bool { return m.keys[i] >= hash }
    )
    if idx == len(m.keys) {
        idx = 0
    }
    return m.hashMap[m.keys[idx]]
}
```
