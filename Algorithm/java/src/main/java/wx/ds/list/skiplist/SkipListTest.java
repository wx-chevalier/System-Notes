package wx.ds.list.skiplist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by apple on 16/7/30.
 */
public class SkipListTest {

    SkipList<Integer, String> sl;

    @Before
    public void setUp() {

        sl = new SkipList<Integer, String>();
        sl.insert(3, "H");
        sl.insert(6, "E");
        sl.insert(2, "L");
        sl.insert(5, "L");
        sl.insert(1, "O");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertDuplicateElement() {

        sl.insert(1, "ett");

    }

    @Test
    public void testFind() {

        Assert.assertEquals(sl.find(3), "H");

        Assert.assertEquals(sl.find(6), "E");

        Assert.assertEquals(sl.find(2), "L");

        Assert.assertEquals(sl.find(5), "L");

        Assert.assertEquals(sl.find(1), "O");


    }

    @Test(expected = NoSuchElementException.class)
    public void testDelete() {

        sl.delete(6);
        System.out.println(sl);
        System.out.println("-------");

        sl.delete(3);
        System.out.println(sl);
        System.out.println("-------");

        sl.find(3);

    }
}
