package wx.algorithm.op.dp.stock;

/**
 * Created by apple on 16/8/21.
 */

/**
 * @function 能够进行无穷多次买卖, 求取最大值
 * @OJ https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
public class Stock2 {

    public int maxProfit(int[] prices) {
        int total = 0;

        //遍历所有交易日
        for (int i = 0; i < prices.length - 1; i++) {
            //只要是后一天比前一天贵,就卖出
            if (prices[i + 1] > prices[i]) total += prices[i + 1] - prices[i];
        }

        return total;
    }
}
