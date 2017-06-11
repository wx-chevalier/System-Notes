package wx.algorithm.search.bt;

/**
 * Created by apple on 16/7/30.
 */
public class BTree {


    static int order; // 树的单节点关键值数目上限

    BNode root;  // 指向树的根节点


    /**
     * @param order
     * @function 默认构造函数
     */
    public BTree(int order) {

        this.order = order;

        root = new BNode(order, null);

    }


    /**
     * @param x
     * @function 打印搜索出的结果
     */
    public void SearchPrintNode(int x) {

        //存放搜索到的节点
        BNode temp = new BNode(order, null);

        temp = search(root, x);

        if (temp == null) {
            System.out.println("该关键值不存在于B树中");
        } else {
            print(temp);
        }


    }

    /**
     * @param root 需要查询的根节点
     * @param key  关键值
     * @return
     * @function 在BTree中搜索指定关键值的节点
     */
    public BNode search(BNode root, int key) {

        //默认从第零个节点开始搜索
        int i = 0;

        //遍历搜索到该值可能在的区域
        while (i < root.count && key > root.key[i]) {
            i++;
        }

        //如果待搜索的关键值正好在当前节点中
        if (i <= root.count && key == root.key[i]) {
            return root;
        }

        //如果该节点没有任何子节点,则直接返回null
        if (root.leaf) {

            return null;

        } else {
            //否则迭代开始搜索对应的子节点
            return search(root.getChild(i), key);

        }
    }

    /**
     * @param t
     * @param key
     * @function 向某个BTree中插入某个关键值, 依次遍历寻找到最合适的插入点
     */
    public void insert(BTree t, int key) {

        //首先考虑插入到根节点中
        BNode r = t.root;

        //如果根节点已经满了
        if (r.count == 2 * order - 1) {

            //构建一个新节点,作为新的父节点
            BNode s = new BNode(order, null);

            t.root = s;

            s.leaf = false;

            s.count = 0;

            s.child[0] = r;

            //分割原来的父节点
            split(s, 0, r);

            //此时调用非满状态下插入
            nonfullInsert(s, key);

        } else {

            //如果当前节点未满则直接插入
            nonfullInsert(r, key);

        }
    }

    /**
     * @param x 新构建的父节点
     * @param i 关键值
     * @param y 原本的节点
     * @function 在节点满了之后执行分割操作
     */
    private void split(BNode x, int i, BNode y) {

        //设置一个临时节点
        BNode z = new BNode(order, null);

        //将临时节点的叶子属性设置为原节点
        z.leaf = y.leaf;

        //更新后的大小
        z.count = order - 1;

        for (int j = 0; j < order - 1; j++) {
            z.key[j] = y.key[j + order]; //copy end of y into front of z

        }

        //如果不是叶节点则需要重新设置子节点
        if (!y.leaf) {
            for (int k = 0; k < order; k++) {
                z.child[k] = y.child[k + order]; //reassing child of y
            }
        }

        y.count = order - 1; //new size of y

        for (int j = x.count; j > i; j--)//if we push key into x we have
        {                 //to rearrange child nodes

            x.child[j + 1] = x.child[j]; //shift children of x

        }
        x.child[i + 1] = z; //reassign i+1 child of x

        for (int j = x.count; j > i; j--) {
            x.key[j + 1] = x.key[j]; // shift keys
        }
        x.key[i] = y.key[order - 1];//finally push value up into root.

        y.key[order - 1] = 0; //erase value where we pushed from

        for (int j = 0; j < order - 1; j++) {
            y.key[j + order] = 0; //'delete' old values
        }

        //更新新构建中的父节点的大小
        x.count++;
    }

// ----------------------------------------------------------
// this will be insert method when node is not full.        |
// ----------------------------------------------------------

    private void nonfullInsert(BNode x, int key) {
        int i = x.count; //i is number of keys in node x

        if (x.leaf) {
            while (i >= 1 && key < x.key[i - 1])//here find spot to put key.
            {
                x.key[i] = x.key[i - 1];//shift values to make room

                i--;//decrement
            }

            x.key[i] = key;//finally assign value to node
            x.count++; //increment count of keys in this node now.

        } else {
            int j = 0;
            while (j < x.count && key > x.key[j])//find spot to recurse
            {                         //on correct child
                j++;
            }

            //	i++;

            if (x.child[j].count == order * 2 - 1) {
                split(x, j, x.child[j]);//call split on node x's ith child

                if (key > x.key[j]) {
                    j++;
                }
            }

            nonfullInsert(x.child[j], key);//recurse
        }
    }


    /**
     * @param n
     * @function 打印某个节点的所有关键值, 如果其存在子节点则递归打印出所有的子节点
     */
    public void print(BNode n) {

        for (int i = 0; i < n.count; i++) {
            //打印出当前节点的关键值
            System.out.print(n.getValue(i) + " ");
        }

        //当该节点并不是叶节点
        if (!n.leaf) {
            //依次遍历所有的子节点
            for (int j = 0; j <= n.count; j++) {
                //按照中根顺序
                if (n.getChild(j) != null) {
                    System.out.println();
                    print(n.getChild(j));
                }
            }
        }
    }


    /**
     * @param t
     * @param key
     * @function 从BTree中删除某个关键值
     */
    public void deleteKey(BTree t, int key) {

        BNode temp = new BNode(order, null);//temp Bnode

        temp = search(t.root, key);//call of search method on tree for key

        if (temp.leaf && temp.count > order - 1) {
            int i = 0;

            while (key > temp.getValue(i)) {
                i++;
            }
            for (int j = i; j < 2 * order - 2; j++) {
                temp.key[j] = temp.getValue(j + 1);
            }
            temp.count--;

        } else {
            System.out.println("This node is either not a leaf or has less than order - 1 keys.");
        }
    }

}
