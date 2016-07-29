package wx.algorithm.search.bst;

/**
 * Created by apple on 16/7/29.
 */

/**
 * @function 二叉搜索树中的节点
 */
public class Node {

    //存放节点数据
    int data;

    //指向左子节点
    Node left;

    //指向右子节点
    Node right;

    /**
     * @function 默认构造函数
     * @param data 节点数据
     */
    public Node(int data) {
        this.data = data;
        left = null;
        right = null;
    }
}
