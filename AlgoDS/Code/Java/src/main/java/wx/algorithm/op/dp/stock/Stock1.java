package wx.algorithm.op.dp.stock;

/**
 * Created by apple on 16/8/21.
 */

/**
 * @function 仅允许买卖一次
 * @description Say you have an array for which the ith element is the price of a given stock on day i.If you were only permitted to complete at most one transaction (ie, buy one and sell one share of the stock), design an algorithm to find the maximum profit.
 * @OJ https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */
public class Stock1 {

    public int maxProfit(int[] prices) {

        //存储最大值的变量,初始化为0
        int ans = 0;

        //判断是否有效价格序列
        if (prices.length == 0) {
            return ans;
        }

        //判断应该在哪一日买入,即最小值
        int bought = prices[0];

        //遍历所有交易日价格
        for (int i = 1; i < prices.length; i++) {

            //判断本日是否能够卖出
            if (prices[i] > bought) {

                //判断如果本日卖出收益是否最大
                if (ans < (prices[i] - bought)) {
                    ans = prices[i] - bought;
                }
            } else {

                //判断本日是否为最低价格
                bought = prices[i];
            }
        }
        return ans;

    }
}

