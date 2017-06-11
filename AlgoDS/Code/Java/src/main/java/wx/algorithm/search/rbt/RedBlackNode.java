package wx.algorithm.search.rbt;

/**
 * Created by apple on 16/7/30.
 */

/**
 * @param <T>
 * @function 红黑树中的节点数据结构
 */
class RedBlackNode<T extends Comparable<T>> {

    //黑色标识值
    public static final int BLACK = 0;

    //红色标识值
    public static final int RED = 1;

    // 每个节点的关键值
    public T key;

    /**
     * 当前节点的父节点
     */
    RedBlackNode<T> parent;


    /**
     * 左孩子
     */
    RedBlackNode<T> left;
    /**
     * 右孩子
     */
    RedBlackNode<T> right;

    // 左侧子树的节点数目
    public int numLeft = 0;

    // 右侧子树的节点数目
    public int numRight = 0;

    // 当前节点的颜色值
    public int color;

    RedBlackNode() {
        color = BLACK;
        numLeft = 0;
        numRight = 0;
        parent = null;
        left = null;
        right = null;
    }

    // Constructor which sets key to the argument.
    RedBlackNode(T key) {
        this();
        this.key = key;
    }
}// end class RedBlackNode