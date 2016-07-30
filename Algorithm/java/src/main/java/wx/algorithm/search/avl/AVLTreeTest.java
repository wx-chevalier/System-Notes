package wx.algorithm.search.avl;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by apple on 16/7/30.
 */
public class AVLTreeTest {

    AVLTree tree;

    @Before
    public void setUp() {
        tree = new AVLTree();
    }

    @Test
    public void testInsertAndBalance() {
        System.out.println("Inserting values 1 to 10");
        for (int i = 1; i < 10; i++)
            tree.insert(i);

        System.out.print("Printing balance: ");
        tree.printBalance();
    }

}
