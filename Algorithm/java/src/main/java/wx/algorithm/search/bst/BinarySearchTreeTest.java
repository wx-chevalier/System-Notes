package wx.algorithm.search.bst;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by apple on 16/7/30.
 */
public class BinarySearchTreeTest {

    BinarySearchTree binarySearchTree;

    @Before
    public void setUp() {
        binarySearchTree = new BinarySearchTree();
        binarySearchTree.insert(3);
        binarySearchTree.insert(8);
        binarySearchTree.insert(1);
        binarySearchTree.insert(4);
        binarySearchTree.insert(6);
        binarySearchTree.insert(2);
        binarySearchTree.insert(10);
        binarySearchTree.insert(9);
        binarySearchTree.insert(20);
        binarySearchTree.insert(25);
        binarySearchTree.insert(15);
        binarySearchTree.insert(16);
        System.out.println("原始的树 : ");
        binarySearchTree.display();
        System.out.println("");

    }

    @Test
    public void testFind() {

        System.out.println("判断4是否存在树中 : " + binarySearchTree.find(4));

    }

    @Test
    public void testInsert() {

    }

    @Test
    public void testDelete() {

        System.out.println("删除值为2的节点 : " + binarySearchTree.delete(2));
        binarySearchTree.display();

        System.out.println("\n 删除有一个子节点值为4的节点 : " + binarySearchTree.delete(4));
        binarySearchTree.display();

        System.out.println("\n 删除有两个子节点的值为10的节点 : " + binarySearchTree.delete(10));
        binarySearchTree.display();

    }

}
