package wx.algorithm.op.dp;

/**
 * Created by apple on 16/8/12.
 */

import org.junit.Test;

/**
 * @function 最长公共子串问题
 */
public class LongestSubString {

    //记录子串,避免重复创建新的子串
    private String firstStr;

    private String secondStr;

    //构建一个备忘录,记录两个(i,j)下的最长公共子串长度
    private int[][] dp;

    public LongestSubString(String firstStr, String secondStr) {

        this.firstStr = firstStr;

        this.secondStr = secondStr;

        this.dp = new int[firstStr.length()][secondStr.length()];

    }

    public int LCS() {
        int i, j;

        //首先初始化DP的第一行和第一列
        for (i = 0; i < firstStr.length(); i++) {

            if (firstStr.charAt(i) == secondStr.charAt(0)) {

                dp[i][0] = 1;

            } else {
                dp[i][0] = 0;
            }

        }

        for (j = 0; j < secondStr.length(); j++) {

            if (secondStr.charAt(j) == firstStr.charAt(0)) {

                dp[0][j] = 1;

            } else {
                dp[0][j] = 0;
            }

        }


        //遍历剩下的全部项目
        for (i = 1; i < firstStr.length(); i++) {
            for (j = 1; j < secondStr.length(); j++) {
                //注意,在最长公共子串中,只考虑最上角,即两个子串的前一个字符也要相等
                if (firstStr.charAt(i) == secondStr.charAt(j)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        //遍历dp求取最大值
        int max = 0;

        for (i = 0; i < firstStr.length(); i++) {
            for (j = 0; j < secondStr.length(); j++) {
                if (dp[i][j] > max) {
                    max = dp[i][j];
                }
            }
        }

        return max;
    }


    /**
     * @function 计算最长公共子串
     * @OJ http://www.nowcoder.com/practice/98dc82c094e043ccb7e0570e5342dd1b?tpId=37&tqId=21298&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public static void main(String args[]) {

        LongestSubString longestSubString = new LongestSubString("bacefaebcdfabfaadebdaacabbdabcfffbdcebaabecefddfaceeebaeabebbad", "dedcecfbbbecaffedcedbadadbbfaafcafdd");

        System.out.println(longestSubString.LCS());


    }

}
