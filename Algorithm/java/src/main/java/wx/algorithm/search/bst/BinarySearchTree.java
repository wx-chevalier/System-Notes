package wx.algorithm.search.bst;

/**
 * Created by apple on 16/7/29.
 */

/**
 * @function 二叉搜索树的示范代码
 */
public class BinarySearchTree {

    //指向二叉搜索树的根节点
    private Node root;

    //默认构造函数
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * @param id 待查找的值
     * @return
     * @function 默认搜索函数
     */
    public boolean find(int id) {

        //从根节点开始查询
        Node current = root;

        //当节点不为空
        while (current != null) {

            //是否已经查询到
            if (current.data == id) {
                return true;
            } else if (current.data > id) {
                //查询左子树
                current = current.left;
            } else {
                //查询右子树
                current = current.right;
            }
        }
        return false;
    }


    /**
     * @param id
     * @function 插入某个节点
     */
    public void insert(int id) {

        //创建一个新的节点
        Node newNode = new Node(id);

        //判断根节点是否为空
        if (root == null) {
            root = newNode;
            return;
        }

        //设置current指针指向当前根节点
        Node current = root;

        //设置父节点为空
        Node parent = null;

        //遍历直到找到第一个插入点
        while (true) {

            //先将父节点设置为当前节点
            parent = current;

            //如果小于当前节点的值
            if (id < current.data) {

                //移向左节点
                current = current.left;

                //如果当前节点不为空,则继续向下一层搜索
                if (current == null) {
                    parent.left = newNode;
                    return;
                }
            } else {

                //否则移向右节点
                current = current.right;

                //如果当前节点不为空,则继续向下一层搜索
                if (current == null) {
                    parent.right = newNode;
                    return;
                }
            }
        }
    }

    /**
     * @param id
     * @return
     * @function 删除树中的某个元素
     */
    public boolean delete(int id) {


        Node parent = root;
        Node current = root;

        //记录被找到的节点是父节点的左子节点还是右子节点
        boolean isLeftChild = false;

        //循环直到找到目标节点的位置,否则报错
        while (current.data != id) {
            parent = current;
            if (current.data > id) {
                isLeftChild = true;
                current = current.left;
            } else {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null) {
                return false;
            }
        }

        //如果待删除的节点没有任何子节点
        //直接将该节点的原本指向该节点的指针设置为null
        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            }
            if (isLeftChild == true) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }

        //如果待删除的节点有一个子节点,且其为左子节点
        else if (current.right == null) {

            //判断当前节点是否为根节点
            if (current == root) {
                root = current.left;
            } else if (isLeftChild) {

                //挂载到父节点的左子树
                parent.left = current.left;
            } else {

                //挂载到父节点的右子树
                parent.right = current.left;
            }
        } else if (current.left == null) {
            if (current == root) {
                root = current.right;
            } else if (isLeftChild) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        }

        //如果待删除的节点有两个子节点
        else if (current.left != null && current.right != null) {

            //寻找右子树中的最小值
            Node successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.left = successor;
            } else {
                parent.right = successor;
            }
            successor.left = current.left;
        }
        return true;
    }

    /**
     * @param deleleNode
     * @return
     * @function 在树种查找最合适的节点
     */
    private Node getSuccessor(Node deleleNode) {
        Node successsor = null;
        Node successsorParent = null;
        Node current = deleleNode.right;
        while (current != null) {
            successsorParent = successsor;
            successsor = current;
            current = current.left;
        }
        if (successsor != deleleNode.right) {
            successsorParent.left = successsor.right;
            successsor.right = deleleNode.right;
        }
        return successsor;
    }

    /**
     * @function 以中根顺序遍历树
     */
    public void display() {
        display(root);
    }

    private void display(Node node) {

        //判断当前节点是否为空
        if (node != null) {

            //首先展示左子树
            display(node.left);

            //然后展示当前根节点的值
            System.out.print(" " + node.data);

            //最后展示右子树的值
            display(node.right);
        }
    }

}
