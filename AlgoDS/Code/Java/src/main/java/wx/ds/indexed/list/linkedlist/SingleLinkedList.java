package wx.ds.indexed.list.linkedlist;

/**
 * Created by apple on 16/5/11.
 */

import java.util.ArrayList;

/**
 * @function 演示单链表, 只用到节点的next指针
 */
public class SingleLinkedList {

    //链表头部指针
    Node first = null;

    //链表长度
    Integer length;

    /**
     * @function 默认构造函数
     */
    public SingleLinkedList() {

        this.length = 0;
    }

    /**
     * 默认从头部插入节点
     */
    public void insert(Item item) {
        Node oldFirst = first;
        first = new Node(item);
        first.next = oldFirst;
        length++;
    }

    /**
     * @function 反转链表
     */
    public void reverse() {

        //指向当前正在操作的节点
        Node current = first;

        //指向当前节点的前一个节点
        Node prevHead = null;

        while (current != null) {

            //获取当前节点指向的下一个节点
            Node next = current.next;

            //此时才将当前节点的后置节点设置为前一个节点完成翻转
            current.next = prevHead;

            //如果下一个节点为null,即原来的尾节点,现在的头节点
            if (next == null) {
                this.first = current;
            }

            //将后置节点设置为当前节点
            prevHead = current;

            //使指针移动到下一个节点
            current = next;
        }
    }

    /**
     * @return
     * @function 将链表的值按照数组形式输出
     */
    public Integer[] getNodeValAsArray() {

        ArrayList<Integer> result = new ArrayList<>();

        Node node = first;

        while (node != null) {

            result.add((Integer) node.data.val);

            node = node.next;

        }


        return result.toArray(new Integer[this.length]);

    }

    public Node getFirst() {
        return first;
    }

    public Integer getLength() {
        return length;
    }
}
