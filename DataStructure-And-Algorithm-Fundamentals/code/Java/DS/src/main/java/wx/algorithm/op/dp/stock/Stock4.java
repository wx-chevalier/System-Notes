package wx.algorithm.op.dp.stock;

/**
 * Created by apple on 16/8/21.
 */
public class Stock4 {

    /**
     * @param k      可交易的次数
     * @param prices 价格向量
     * @return
     * @function 计算最多K次情况下能获得的最大理论
     */
    public int maxProfit(int k, int[] prices) {
        if (k == 0 || prices.length < 2)
            return 0;
        if (k > prices.length / 2)
            return noLimit(prices);

        // hold[i][j]: 对于0~i天中最多进行j次交易并且第i天仍然持有股票的收益
        // unhold[i][j]: 对于0~i天中最多进行j次交易并且第i天不持有股票的收益
        // 第i天持有股票的收益为 之前买了股票但还没有卖出 或者 今天才选择买入股票 二者中较大值
        // hold[i][j] = Math.max(unhold[i-1][j]-prices[i],hold[i-1][j]);
        // 第i天不持有股票的收益为 选择今天卖出 或者 今天不买入时 最大的收益
        // unhold[i][j] = Math.max(hold[i-1][j-1]+prices[i],unhold[i-1][j]);

        int[][] hold = new int[k + 1][prices.length];
        int[][] unhold = new int[k + 1][prices.length];
        for (int i = 1; i <= k; i++) {

            //初始化持有状态下的初始值
            hold[i][0] = -prices[0];

            //初始化不持有状态下的初始值 为0
            unhold[i][0] = 0;
            for (int j = 1; j < prices.length; j++) {
                hold[i][j] = Math.max(-prices[j] + unhold[i - 1][j], hold[i][j - 1]); // Buy or not buy
                unhold[i][j] = Math.max(prices[j] + hold[i][j - 1], unhold[i][j - 1]); // Sell or not sell
            }
        }

        //真实情况下最后一天了还是要卖出的
        return unhold[k][prices.length - 1];
    }

    private int noLimit(int[] prices) { // Solution from Best Time to Buy and Sell Stock II
        int max = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            if (prices[i + 1] > prices[i])
                max += prices[i + 1] - prices[i];
        }
        return max;
    }

}
