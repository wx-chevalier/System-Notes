package wx.ds.indexed.list.skiplist;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by apple on 16/7/30.
 */
public class SkipList<K extends Comparable<K>, V> {

    //指向当前跳跃表头部
    private QuadNode<K, V> head = new QuadNode<K, V>(null, null, 0);

    //随机数生成器
    final static Random random = new Random();

    /**
     * @param key   键
     * @param value 值
     * @function 插入某个节点
     */
    public void insert(K key, V value) {

        //从第零层开始
        int level = 0;

        //随机选择某一层
        while (random.nextBoolean()) {
            level++;
        }

        //依次创建N个头元素,直至达到要插入的层级
        while (head.getLevel() < level) {

            //为当前层级的下一层构造新的头节点
            QuadNode<K, V> newHead = new QuadNode<K, V>(null, null, head.getLevel() + 1);

            //将当前节点的上一层节点指向新节点
            head.setUp(newHead);

            //将新节点的下一层节点指向当前节点
            newHead.setDown(head);

            //将当前节点指向原本的新节点
            head = newHead;
        }

        //在最后一层插入新值
        head.insert(key, value, level, null);
    }

    /**
     * @param key
     * @return
     * @function 在跳跃表中查找某个元素
     */
    public V find(K key) {
        return head.find(key).getValue();
    }

    /**
     * @param key
     * @function 在跳跃表中删除某个元素
     */
    public void delete(K key) {

        //依次找到所有与该值相等的节点
        for (QuadNode<K, V> node = head.find(key); node != null; node = node.getDown()) {

            //将当前节点的本层上一个节点直接指向下一个节点
            node.getPrevious().setNext(node.getNext());
            if (node.getNext() != null) {
                node.getNext().setPrevious(node.getPrevious());
            }
        }

        //如果是头节点本删除,则在不同层上进行合并
        while (head.getNext() == null) {
            head = head.getDown();
            head.setUp(null);
        }
    }

    @Override
    public String toString() {
        return head.toString();
    }

    public static void main(String[] args) {
        SkipList<Integer, String> sl = new SkipList<Integer, String>();

        try {
            throw new IllegalStateException("不允许重复插入元素");
        } catch (IllegalArgumentException e) {
            System.out.println("不允许重复插入元素");
        }

        System.out.println(sl);
        System.out.println("-------");
        System.out.println(sl.find(3).equals("H"));
        System.out.println(sl.find(6).equals("E"));
        System.out.println(sl.find(2).equals("L"));
        System.out.println(sl.find(5).equals("L"));
        System.out.println(sl.find(1).equals("O"));

        sl.delete(6);
        System.out.println(sl);
        System.out.println("-------");

        sl.delete(3);
        System.out.println(sl);
        System.out.println("-------");

        try {
            sl.find(3);
            throw new IllegalStateException("Nooo!");
        } catch (NoSuchElementException e) {
            System.out.println("不应该找到节点3");
        }
    }
}
