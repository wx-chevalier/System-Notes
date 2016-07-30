package wx.algorithm.search.avl;

/**
 * Created by apple on 16/7/30.
 */
public class AVLNode {

    //节点的值
    public int key;

    //节点的平衡度
    public int balance;

    //分别指向节点的左孩子、右孩子与父节点
    public AVLNode left, right, parent;

    /**
     * @function 默认构造函数
     * @param k
     * @param p
     */
    AVLNode(int k, AVLNode p) {
        key = k;
        parent = p;
    }
}
