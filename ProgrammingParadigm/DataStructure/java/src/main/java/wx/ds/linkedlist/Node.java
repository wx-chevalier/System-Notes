package wx.ds.linkedlist;

/**
 * Created by apple on 16/5/11.
 */

/**
 * @function 节点类
 */
public class Node {

    //存放当前节点数据
    public Item data;

    //存放指向下一个节点的指针
    public Node next = null;

    public Node(Item data) {
        this.data = data;
    }
}
