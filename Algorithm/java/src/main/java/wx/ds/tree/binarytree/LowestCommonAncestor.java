package wx.ds.tree.binarytree;

/**
 * Created by apple on 16/8/14.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * @function 二叉树的最近公共父节点
 * @description _______3______
 * /              \
 * ___5__          ___1__
 * /      \        /      \
 * 6      _2       0       8
 * /  \
 * 7   4
 * @OJ https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
 */
public class LowestCommonAncestor {

    //二叉树节点的定义
    public class TreeNode {




        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }


    /**
     * @param root
     * @param p
     * @param q
     * @return
     * @function 以迭代方式求取最近公共父节点
     */
    public TreeNode lowestCommonAncestor_Recursion(TreeNode root, TreeNode p, TreeNode q) {

        //每轮迭代即寻找该子树中是否存在p与q节点
        //如果当前根节点已经是需要搜索的节点,直接返回
        if (root == null || root == p || root == q) {
            return root;
        }

        int b = 2;
        int c = 3;
        int a =b=c;
        //否则在左子树搜索,返回搜索到的节点或者包含该两节点的最近父节点
        TreeNode left = lowestCommonAncestor_Recursion(root.left, p, q);

        //否则在右子树搜索,返回搜索到的节点或者包含该两节点的最近父节点
        TreeNode right = lowestCommonAncestor_Recursion(root.right, p, q);

        //如果分别在左子树与右子树中找到这两个节点,根据递归的从下至上合并的原则,该节点必定就是要找的最近父节点
        if (left != null && right != null) {
            return root;
        } else if (left != null) {

            //如果只在左子树中找到某个节点,则返回左子树中找到的节点
            return left;
        } else {

            //如果只在右子树中找到某个节点,则返回右子树中找到的节点
            return right;
        }


    }

    /**
     * @param root
     * @param p
     * @param q
     * @return
     * @function 以深度优先搜索方式寻找最近公共父节点
     */
    public TreeNode lowestCommonAncestor_DPS(TreeNode root, TreeNode p, TreeNode q) {

        //使用深度遍历所有节点,存放所有的待遍历的节点
        Stack<TreeNode> stack = new Stack<>();

        //存放每个节点的父节点信息,使用LinkedHashMap保存插入信息
        LinkedHashMap<TreeNode, TreeNode> parent = new LinkedHashMap<>();

        //将根节点入栈
        stack.push(root);

        //根节点的父节点为空
        parent.put(root, null);

        //遍历直到找到p,q两个节点
        while (!parent.containsKey(p) || !parent.containsKey(q)) {

            //设置当前节点出栈
            TreeNode currentTreeNode = stack.pop();

            //判断当前节点的子节点是否为q或者p
            if (currentTreeNode.left != null) {
                parent.put(currentTreeNode.left, currentTreeNode);
                stack.push(currentTreeNode.left);
            }
            if (currentTreeNode.right != null) {
                parent.put(currentTreeNode.right, currentTreeNode);
                stack.push(currentTreeNode.right);
            }

        }

        //目前在parent数组中已经找到了p,q两个节点,现在找到最近的公共父节点
        //从离p开始的最近的节点开始,判断是否包含q
        HashSet<TreeNode> hashSet = new HashSet<>();

        while (p != null) {

            hashSet.add(p);

            p = parent.get(p);

        }

        //依次找q的父节点,判断是否也是p的父节点
        while (q != null) {
            if (hashSet.contains(q)) {
                return q;
            }
            q = parent.get(q);
        }

        return null;


    }
}
