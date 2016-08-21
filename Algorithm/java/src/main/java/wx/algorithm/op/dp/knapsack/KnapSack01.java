package wx.algorithm.op.dp.knapsack;

/**
 * Created by apple on 16/8/15.
 */

import java.util.Scanner;

/**
 * @function 01背包问题的解
 */
public class KnapSack01 {


    /**
     * @param w
     * @param items_weight
     * @param items_value
     * @return
     * @function 使用递归法求解所有可行解
     */
    public static int knapsack01_crs(int w, int[] items_weight, int[] items_value) {


        KnapSack01 knapSack01 = new KnapSack01();

        knapSack01.knapsack01_iteration(w, items_weight, items_value, 0, 0, 0, new int[items_value.length]);

        return knapSack01.maxValue;

    }

    //存放全局的最大值
    public int maxValue;

    //存放最大值对应的方案
    public int[] maxAdded;

    /**
     * @param w              背包总重量
     * @param items_weight   物体重量向量
     * @param items_value    物体价值向量
     * @param visited        当前需要被访问的物体的下标
     * @param current_weight 当前总重量
     * @param current_value  当前总价值
     * @param added          记录每个元素是否被添加到背包中
     * @return
     * @function 迭代函数
     */
    public void knapsack01_iteration(int w, int[] items_weight, int[] items_value, int visited, int current_weight, int current_value, int added[]) {

        //判断是否已经全部遍历结束
        if (visited == items_value.length) {

            //判断当前值是否大于已知的最大值
            if (current_value > maxValue) {

                //设置最大值
                maxValue = current_value;

                //设置已添加入背包的数组
                maxAdded = added.clone();
            }

            return;
        }

        //否则判断当前元素能否加入背包
        if (items_weight[visited] < (w - current_weight)) {

            //将元素加入当前背包
            added[visited] = 1;

            //如果可以加入背包
            knapsack01_iteration(w, items_weight, items_value, visited + 1, current_weight + items_weight[visited], current_value + items_value[visited], added);
        }

        added[visited] = 0;

        //否则默认一条路径是当前元素不加入背包判断
        knapsack01_iteration(w, items_weight, items_value, visited + 1, current_weight, current_value, added);

    }

    /**
     * @param w            背包总重量
     * @param items_weight 每个物体的重量
     * @param items_value  每个物体的价值
     * @function 使用动态规划计算01背包
     */
    public static int knapsack01_dp(int w, int[] items_weight, int[] items_value) {

        //构建一个dp数组
        int[][] dp = new int[items_weight.length + 1][w + 1];

        //按行遍历,选择前i个物品放置到重量为j的背包中
        for (int i = 1; i < items_weight.length + 1; i++) {
            for (int j = 1; j < w + 1; j++) {
                //判断当前物品是否可以放到该背包中
                if (items_weight[i - 1] > j) {
                    //如果放不进去,则等于dp[i-1][j]
                    dp[i][j] = dp[i - 1][j];
                } else {
                    //如果可以放进去
                    //分别选择不放该物品与放置该物品的大小
                    int with = dp[i - 1][j - items_weight[i - 1]] + items_value[i - 1];

                    int withOut = dp[i - 1][j];

                    dp[i][j] = Math.max(with, withOut);

                }
            }
        }

        return dp[items_weight.length][w];

    }

    /**
     * @param args
     * @function 经典的01背包问题
     * @OJ http://www.practice.geeksforgeeks.org/problem-page.php?pid=909
     */
    public static void main(String args[]) {

        //测试数据
//        int w = 4;
//        int[] items_weight = new int[]{4, 5, 1};
//        int[] items_value = new int[]{1, 2, 3};
//
//        System.out.println(knapsack01_crs(w, items_weight, items_value));

        System.out.println(Integer.toBinaryString(2));

        Scanner scanner = new Scanner(System.in);

        int T = scanner.nextInt();

        for (int i = 0; i < T; i++) {

            int N = scanner.nextInt();

            int[] items_weight = new int[N];

            int[] items_value = new int[N];

            int W = scanner.nextInt();

            for (int j = 0; j < N; j++) {
                items_value[j] = scanner.nextInt();
            }

            for (int j = 0; j < N; j++) {
                items_weight[j] = scanner.nextInt();
            }

            System.out.println(knapsack01_dp(W, items_weight, items_value));
        }

    }
}
