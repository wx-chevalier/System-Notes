package wx.ds.linkedlist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by apple on 16/5/11.
 */
public class SingleLinkedListTest {

    SingleLinkedList singleLinkedList;

    @Before
    public void setUp() {
        this.singleLinkedList = new SingleLinkedList();
    }

    /**
     * @function 测试插入
     */
    @Test
    public void testInsert() {

        Item<Integer> item = new Item<>(1);

        this.singleLinkedList.insert(item);

        Assert.assertEquals(this.singleLinkedList.getLength(), (Integer) 1);

        Assert.assertArrayEquals(this.singleLinkedList.getNodeValAsArray(), new Integer[]{1});

    }

    @Test
    public void testReverse() {

        Item<Integer> item_1 = new Item<>(1);

        Item<Integer> item_2 = new Item<>(2);

        this.singleLinkedList.insert(item_2);

        this.singleLinkedList.insert(item_1);

        this.singleLinkedList.reverse();

        Assert.assertArrayEquals(this.singleLinkedList.getNodeValAsArray(), new Integer[]{2,1});

    }

}
