/**
 * Created by apple on 16/7/30.
 */

package wx.ds.list.skiplist;

import java.util.NoSuchElementException;

/**
 * @param <K>
 * @param <V>
 * @function SkipList中的节点
 */
public class QuadNode<K extends Comparable<K>, V> {
    private QuadNode<K, V>
            //指向上一层节点
            up,
    //指向下一层节点
    down,
    //指向本层前一个节点
    next,
    //指向本层前一个节点
    previous;
    //当前节点的Key值
    private K key;
    //当前节点的Value
    private V value;
    //当前节点所处于的层级
    private int level;

    public QuadNode(K key, V value, int level) {
        this.key = key;
        this.value = value;
        this.level = level;
    }

    /**
     * @param key
     * @param value
     * @param level
     * @param parent
     * @function 插入一个节点
     */
    public void insert(K key, V value, int level, QuadNode<K, V> parent) {

        if (this.level <= level && (next == null || next.getKey().compareTo(key) > 0)) {

            QuadNode<K, V> newNode = new QuadNode<K, V>(key, value, this.level);

            if (next != null) {
                next.setPrevious(newNode);
                newNode.setNext(next);
            }

            next = newNode;
            newNode.setPrevious(this);

            if (parent != null) {
                newNode.setUp(parent);
                parent.setDown(newNode);
            }

            if (down != null) {
                down.insert(key, value, level, newNode);
            }
        }
        //如果当前节点并不是最后一个节点,并且当前下一个值大于当前值,则插入下一个值
        else if (next != null && next.getKey().compareTo(key) < 0) {
            next.insert(key, value, level, parent);
        }
        //如果与当前值相等,则抛出异常
        else if (next != null && next.getKey().compareTo(key) == 0) {
            throw new IllegalArgumentException("Duplicate key is not allowed!");
        } else if (down != null) {
            down.insert(key, value, level, parent);
        }
    }

    /**
     * @param key
     * @return
     * @function 判断当前节点的本层下一个节点是否与要查找的值相等
     */
    public QuadNode<K, V> find(K key) {

        //如果下一个节点不为空
        if (next != null) {

            //比较结果
            int compareResult = next.getKey().compareTo(key);

            //如果值相等
            if (compareResult == 0) {
                //则直接返回
                return next;
            }
            //如果大于下一个节点
            else if (compareResult < 0) {

                //则在本层下一个节点之后查找
                return next.find(key);
            } else if (down != null) {

                //否则到下一层节点开始查找
                return down.find(key);
            } else {

                //否则抛出节点不存在异常
                throw new NoSuchElementException();
            }
        } else if (down != null) {
            return down.find(key);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        QuadNode<K, V> bottom = this;

        while (bottom.getDown() != null) {
            bottom = bottom.getDown();
        }

        for (QuadNode<K, V> node = bottom; node != null; node = node.getUp()) {
            sb.append('[').append((node.getKey() == null) ? "H" : node.getKey().toString()).append(']');
        }

        if (bottom.next != null) {
            sb.append('\n').append(bottom.next.toString());
        }

        return sb.toString();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setPrevious(QuadNode qn) {
        previous = qn;
    }

    public void setNext(QuadNode qn) {
        next = qn;
    }

    public void setDown(QuadNode qn) {
        down = qn;
    }

    public void setUp(QuadNode qn) {
        up = qn;
    }

    public QuadNode<K, V> getUp() {
        return up;
    }

    public QuadNode<K, V> getDown() {
        return down;
    }

    public QuadNode<K, V> getPrevious() {
        return previous;
    }

    public QuadNode<K, V> getNext() {
        return next;
    }

    public int getLevel() {
        return level;
    }
}