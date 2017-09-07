package wx.algorithm.op.dp.stock;

/**
 * Created by apple on 16/8/21.
 */
public class StockWithCoolDown {

    public int maxProfit(int[] prices) {

        //判断日期长度是否大于1
        if (prices.length <= 1) {
            return 0;
        }

        //构建三个状态数组
        int[] unhold = new int[prices.length];

        int[] hold = new int[prices.length];

        int[] cooldown = new int[prices.length];

        unhold[0] = 0;

        hold[0] = -prices[0];

        cooldown[0] = Integer.MIN_VALUE;

        for (int i = 1; i < prices.length; i++) {
            unhold[i] = Math.max(unhold[i - 1], cooldown[i - 1]);
            hold[i] = Math.max(hold[i - 1], unhold[i - 1] - prices[i]);
            cooldown[i] = hold[i - 1] + prices[i];
        }

        return Math.max(unhold[prices.length - 1],cooldown[prices.length - 1]);

    }
}
