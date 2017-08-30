package wx.algorithm.op.dp.stock;

/**
 * Created by apple on 16/8/21.
 */

/**
 * @function 最多两次买卖
 * @OJ https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 */
public class Stock3 {

    public static int maxProfit(int[] prices) {

        //判断是否为有效交易天数
        if (prices.length == 0) return 0;

        //存放左半部分最大收益
        int[] left = new int[prices.length];

        //存放右半部分最大收益
        int[] right = new int[prices.length];

        //初始化为0
        int leftMin = prices[0];

        int rightMax = prices[prices.length - 1];

        //总收益
        int sum = 0;

        //计算左半段最大收益
        for (int i = 1; i < prices.length; i++) {

            //获取左半部分的最低价
            leftMin = Math.min(prices[i], leftMin);

            //获取左半部分最大收益
            left[i] = Math.max(prices[i] - leftMin, left[i - 1]);
        }

        //计算右半段最大收益
        for (int i = prices.length - 2; i >= 0; i--) {

            //获取右半部分最低价
            rightMax = Math.max(prices[i], rightMax);

            //获取右半部分最大收益
            right[i] = Math.max(rightMax - prices[i], right[i + 1]);
        }
        //找出两次交易最大收益组合
        for (int i = 0; i < prices.length; i++) {
            if ((left[i] + right[i]) > sum) sum = left[i] + right[i];
        }
        return sum;
    }

    public static void main(String args[]) {
        int[] prices = new int[]{1, 7, 15, 6, 57, 32, 76};

        System.out.print("maxProfit is" + maxProfit(prices));
    }
}
