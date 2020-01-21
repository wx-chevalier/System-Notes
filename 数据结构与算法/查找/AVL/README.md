# AVL: 完全平衡的二叉查找树

二叉查找树可以表示动态的数据集合，对于给定的数据集合，在建立一颗二叉查找树时，二叉查找树的结构形态与关键字的插入顺序有关。如果全部或者部分地按照关键字的递增或者递减顺序插入二叉查找树的结点，则所建立的二叉查找树全部或者在局部形成退化的单分支结构。在最坏的情况下，二叉查找树可能完全偏斜，高度为`n`，其平均与最坏的情况下查找时间都是`O(n)；`而最好的情况下，二叉查找树的结点尽可能靠近根结点，其平均与最好情况的查找时间都是`O(logn)`。因此，我们希望最理想的状态下是使二叉查找树始终处于良好的结构形态。![](http://www.tutorialspoint.com/data_structures_algorithms/images/unbalanced_bst.jpg)

1962 年，Adelson-Velsikii 和 Landis 提出了一种结点在高度上相对平衡的二叉查找树，又称为 AVL 树。其平均和最坏情况下的查找时间都是`O(logn)`。同时，插入和删除的时间复杂性也会保持`O(logn)`，且在插入和删除之后，在高度上仍然保持平衡。AVL 树又称为平衡二叉树，即 Balanced Binary Tree 或者 Height-Balanced Tree，它或者是一棵空二叉树，或者是具有如下性质的二叉查找树：其左子树和右子树都是高度平衡的二叉树，且左子树和右子树的高度之差的绝对值不超过 1。如果将二叉树上结点的平衡因子 BF(Balanced Factor )定义为该结点的左子树与右子树的高度之差，根据 AVL 树的定义，AVL 树中的任意结点的平衡因子只可能是 -1(右子树高于左子树)、 0 或者 1(左子树高于右子树)，在某些图中也会表示为绝对高度差，即 0，1 ， 2 这种形式，请注意理解。

```
BalanceFactor = height(left-sutree) − height(right-sutree)
```

![](http://www.tutorialspoint.com/data_structures_algorithms/images/unbalanced_avl_trees.jpg) AVL 树中的结点的数据结构可以表示为 :

```
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
```

# Rebalance: 平衡调整

AVL 树的调整过程很类似于数学归纳法，每次在插入新节点之后都会找到离新插入节点最近的非平衡叶节点，然后对其进行旋转操作以使得树中的每个节点都处于平衡状态。

## Left Rotation: 左旋 , 右子树右子节点

当新插入的结点为右子树的右子结点时，我们需要进行左旋操作来保证此部分子树继续处于平衡状态。![](http://www.tutorialspoint.com/data_structures_algorithms/images/avl_left_rotation.jpg) 我们应该找到离新插入的结点最近的一个非平衡结点，来以其为轴进行旋转，下面看一个比较复杂的情况: ![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/4/F8CD6D7F-825E-44BB-B295-3B5AD1F11930.png)

```
/**
 * @param a
 * @return
 * @function 左旋操作
 */
private AVLNode rotateLeft(AVLNode a) {

    //指向当前节点的右孩子
    AVLNode b = a.right;

    //将当前节点的右孩子挂载到当前节点的父节点
    b.parent = a.parent;

    //将原本节点的右孩子挂载到新节点的左孩子
    a.right = b.left;

    if (a.right != null)
        a.right.parent = a;

    //将原本节点挂载到新节点的左孩子上
    b.left = a;

    //将原本节点的父节点设置为新节点
    a.parent = b;

    //如果当前节点的父节点不为空
    if (b.parent != null) {
        if (b.parent.right == a) {
            b.parent.right = b;
        } else {
            b.parent.left = b;
        }
    }

    //重新计算每个节点的平衡度
    setBalance(a, b);

    return b;
}
```

## Right Rotation: 右旋 , 左子树左子节点

当新插入的结点为左子树的左子结点时，我们需要进行右旋操作来保证此部分子树继续处于平衡状态。![](http://www.tutorialspoint.com/data_structures_algorithms/images/avl_right_rotation.jpg) 下面看一个比较复杂的情况 : ![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/4/23B249C2-F7A3-44D5-ACCA-B34CC6F41089.png)

```
private AVLNode rotateRight(AVLNode a) {

    AVLNode b = a.left;
    b.parent = a.parent;

    a.left = b.right;

    if (a.left != null)
        a.left.parent = a;

    b.right = a;
    a.parent = b;

    if (b.parent != null) {
        if (b.parent.right == a) {
            b.parent.right = b;
        } else {
            b.parent.left = b;
        }
    }

    setBalance(a, b);

    return b;
}
```

## Left-Right Rotation: 先左旋再右旋 , 左子树右子节点

在某些情况下我们需要进行两次旋转操作，譬如在如下的情况下，某个结点被插入到了左子树的右子结点: ![](http://www.tutorialspoint.com/data_structures_algorithms/images/right_subtree_of_left_subtree.jpg) 我们首先要以 A 为轴进行左旋操作 : ![](http://www.tutorialspoint.com/data_structures_algorithms/images/subtree_left_rotation.jpg) 然后需要以 C 为轴进行右旋操作 : ![](http://www.tutorialspoint.com/data_structures_algorithms/images/left_unbalanced_tree.jpg) ![](http://www.tutorialspoint.com/data_structures_algorithms/images/right_rotation.jpg) 最终得到的又是一棵平衡树 : ![](http://www.tutorialspoint.com/data_structures_algorithms/images/balanced_avl_tree.jpg) ![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/4/6CE95997-6D45-4483-8962-2BEEA8CF0DDF.png)

```
private AVLNode rotateLeftThenRight(AVLNode n) {
    n.left = rotateLeft(n.left);
    return rotateRight(n);
}
```

## Right-Left Rotation: 先右旋再左旋 , 右子树左子节点

![](http://www.tutorialspoint.com/data_structures_algorithms/images/left_subtree_of_right_subtree.jpg) ![](http://www.tutorialspoint.com/data_structures_algorithms/images/subtree_right_rotation.jpg) ![](http://www.tutorialspoint.com/data_structures_algorithms/images/right_unbalanced_tree.jpg) ![](http://www.tutorialspoint.com/data_structures_algorithms/images/left_rotation.jpg) ![](http://www.tutorialspoint.com/data_structures_algorithms/images/balanced_avl_tree.jpg) ![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2016/7/4/CEB6A7C5-4A5A-4491-9A80-8DDC859F8B7B.png)

```
private AVLNode rotateRightThenLeft(AVLNode n) {
    n.right = rotateRight(n.right);
    return rotateLeft(n);
}
```

# Java Implementation

Java 实现的核心代码地址为 :[AVLTree](https://github.com/wx-chevalier/just-coder-handbook/blob/master/Algorithm/java/src/main/java/wx/algorithm/search/avl/AVLTree.java)

```
package wx.algorithm.search.avl;

/**
 * Created by apple on 16/7/30.
 */
public class AVLTree {

    //指向当前AVL树的根节点
    private AVLNode root;

    /**
     * @param key
     * @return
     * @function 插入函数
     */
    public boolean insert(int key) {

        //如果当前根节点为空,则直接创建新节点
        if (root == null)
            root = new AVLNode(key, null);
        else {

            //设置新的临时节点
            AVLNode n = root;

            //指向当前的父节点
            AVLNode parent;

            //循环直至找到合适的插入位置
            while (true) {

                //如果查找到了相同值的节点
                if (n.key == key)

                    //则直接报错
                    return false;

                //将当前父节点指向当前节点
                parent = n;

                //判断是移动到左节点还是右节点
                boolean goLeft = n.key > key;
                n = goLeft ? n.left : n.right;

                //如果左孩子或者右孩子为空
                if (n == null) {
                    if (goLeft) {
                        //将节点挂载到左孩子上
                        parent.left = new AVLNode(key, parent);
                    } else {
                        //否则挂载到右孩子上
                        parent.right = new AVLNode(key, parent);
                    }

                    //重平衡该树
                    rebalance(parent);
                    break;
                }

                //如果不为空,则以n为当前节点进行查找
            }
        }
        return true;
    }

    /**
     * @param delKey
     * @function 根据关键值删除某个元素, 需要对树进行再平衡
     */
    public void delete(int delKey) {
        if (root == null)
            return;
        AVLNode n = root;
        AVLNode parent = root;
        AVLNode delAVLNode = null;
        AVLNode child = root;

        while (child != null) {
            parent = n;
            n = child;
            child = delKey >= n.key ? n.right : n.left;
            if (delKey == n.key)
                delAVLNode = n;
        }

        if (delAVLNode != null) {
            delAVLNode.key = n.key;

            child = n.left != null ? n.left : n.right;

            if (root.key == delKey) {
                root = child;
            } else {
                if (parent.left == n) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                rebalance(parent);
            }
        }
    }

    /**
     * @function 打印节点的平衡度
     */
    public void printBalance() {
        printBalance(root);
    }

    /**
     * @param n
     * @function 重平衡该树
     */
    private void rebalance(AVLNode n) {

        //为每个节点设置相对高度
        setBalance(n);

        //如果左子树高于右子树
        if (n.balance == -2) {

            //如果挂载的是左子树的左孩子
            if (height(n.left.left) >= height(n.left.right))

                //进行右旋操作
                n = rotateRight(n);
            else

                //如果挂载的是左子树的右孩子,则先左旋后右旋
                n = rotateLeftThenRight(n);

        }
        //如果左子树高于右子树
        else if (n.balance == 2) {

            //如果挂载的是右子树的右孩子
            if (height(n.right.right) >= height(n.right.left))

                //进行左旋操作
                n = rotateLeft(n);
            else

                //否则进行先右旋后左旋
                n = rotateRightThenLeft(n);
        }

        if (n.parent != null) {

            //如果当前节点的父节点不为空,则平衡其父节点
            rebalance(n.parent);
        } else {
            root = n;
        }
    }

    /**
     * @param a
     * @return
     * @function 左旋操作
     */
    private AVLNode rotateLeft(AVLNode a) {

        //指向当前节点的右孩子
        AVLNode b = a.right;

        //将当前节点的右孩子挂载到当前节点的父节点
        b.parent = a.parent;

        //将原本节点的右孩子挂载到新节点的左孩子
        a.right = b.left;

        if (a.right != null)
            a.right.parent = a;

        //将原本节点挂载到新节点的左孩子上
        b.left = a;

        //将原本节点的父节点设置为新节点
        a.parent = b;

        //如果当前节点的父节点不为空
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        //重新计算每个节点的平衡度
        setBalance(a, b);

        return b;
    }

    private AVLNode rotateRight(AVLNode a) {

        AVLNode b = a.left;
        b.parent = a.parent;

        a.left = b.right;

        if (a.left != null)
            a.left.parent = a;

        b.right = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private AVLNode rotateLeftThenRight(AVLNode n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private AVLNode rotateRightThenLeft(AVLNode n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    /**
     * @param n
     * @return
     * @function 计算某个节点的高度
     */
    private int height(AVLNode n) {
        if (n == null)
            return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * @param AVLNodes
     * @function 重设置每个节点的平衡度
     */
    private void setBalance(AVLNode... AVLNodes) {
        for (AVLNode n : AVLNodes)
            n.balance = height(n.right) - height(n.left);
    }

    private void printBalance(AVLNode n) {
        if (n != null) {
            printBalance(n.left);
            System.out.printf("%s ", n.balance);
            printBalance(n.right);
        }
    }
}
```

# 链接

- https://blog.csdn.net/xiaojin21cen/article/details/84060807
