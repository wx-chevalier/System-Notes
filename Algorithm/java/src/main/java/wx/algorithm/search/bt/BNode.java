package wx.algorithm.search.bt;

/**
 * Created by apple on 16/7/30.
 */

/**
 * @function B树的节点
 */
public class BNode {

    static int t;  //用于决定树的单节点关键值数目上限

    int count; // 该节点中关键值的数量

    int key[];  // 关键值列表

    BNode child[]; // 子节点引用

    boolean leaf; // 判断是否为叶节点

    BNode parent;  // 当前节点的父节点

    /**
     * @function 默认构造函数
     */
    public BNode() {
    }

    /**
     * @param t
     * @param parent
     * @function 设置默认值
     */
    public BNode(int t, BNode parent) {
        this.t = t;  //设置大小

        this.parent = parent; //设置该节点的父节点

        key = new int[2 * t - 1];  // 真实数组的大小

        child = new BNode[2 * t]; // 子节点的大小

        leaf = true; // 默认设置为叶节点

        count = 0; // 默认当前关键值的个数为零
    }

    /**
     * @param index
     * @return
     * @function 返回指定下标的关键值
     */
    public int getValue(int index) {
        return key[index];
    }

    /**
     * @param index
     * @return
     * @function 返回指定下标的子元素
     */
    public BNode getChild(int index) {
        return child[index];
    }
}
