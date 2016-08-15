package wx.algorithm.op.dp;

/**
 * Created by apple on 16/8/1.
 */

import org.junit.Test;

import java.util.Scanner;

/**
 * This is an implementation, in Java, of the Longest Common Subsequence algorithm.
 * That is, given two strings A and B, this program will find the longest sequence
 * of letters that are common and ordered in A and B.
 * <p>
 * There are only two reasons you are reading this:
 * - you don't care what the algorithm is but you need a piece of code to do it
 * - you're trying to understand the algorithm, and a piece of code might help
 * In either case, you should either read an entire chapter of an algorithms textbook
 * on the subject of dynamic programming, or you should consult a webpage that describes
 * this particular algorithm.   It is important, for example, that we use arrays of size
 * |A|+1 x |B|+1.
 * <p>
 * This code is provided AS-IS.
 * You may use this code in any way you see fit, EXCEPT as the answer to a homework
 * problem or as part of a term project in which you were expected to arrive at this
 * code yourself.
 * <p>
 * Copyright (C) 2005 Neil Jones.
 */
public class LCS {
    // These are "constants" which indicate a direction in the backtracking array.
    private static final int NEITHER = 0;
    private static final int UP = 1;
    private static final int LEFT = 2;
    private static final int UP_AND_LEFT = 3;

    public static String LCSAlgorithm(String a, String b) {
        int n = a.length();
        int m = b.length();
        int S[][] = new int[n + 1][m + 1];
        int R[][] = new int[n + 1][m + 1];
        int ii, jj;

        // It is important to use <=, not <.  The next two for-loops are initialization
        for (ii = 0; ii <= n; ++ii) {
            S[ii][0] = 0;
            R[ii][0] = UP;
        }
        for (jj = 0; jj <= m; ++jj) {
            S[0][jj] = 0;
            R[0][jj] = LEFT;
        }

        // This is the main dynamic programming loop that computes the score and
        // backtracking arrays.
        for (ii = 1; ii <= n; ++ii) {
            for (jj = 1; jj <= m; ++jj) {

                if (a.charAt(ii - 1) == b.charAt(jj - 1)) {
                    S[ii][jj] = S[ii - 1][jj - 1] + 1;
                    R[ii][jj] = UP_AND_LEFT;
                } else {
                    S[ii][jj] = S[ii - 1][jj - 1] + 0;
                    R[ii][jj] = NEITHER;
                }

                if (S[ii - 1][jj] >= S[ii][jj]) {
                    S[ii][jj] = S[ii - 1][jj];
                    R[ii][jj] = UP;
                }

                if (S[ii][jj - 1] >= S[ii][jj]) {
                    S[ii][jj] = S[ii][jj - 1];
                    R[ii][jj] = LEFT;
                }
            }
        }

        // The length of the longest substring is S[n][m]
        ii = n;
        jj = m;
        int pos = S[ii][jj] - 1;
        char lcs[] = new char[pos + 1];

        // Trace the backtracking matrix.
        while (ii > 0 || jj > 0) {
            if (R[ii][jj] == UP_AND_LEFT) {
                ii--;
                jj--;
                lcs[pos--] = a.charAt(ii);
            } else if (R[ii][jj] == UP) {
                ii--;
            } else if (R[ii][jj] == LEFT) {
                jj--;
            }
        }

        return new String(lcs);
    }

    /**
     * @function 计算两个字符串的最大公共字串的长度，字符不区分大小写
     * @OJ http://www.nowcoder.com/practice/98dc82c094e043ccb7e0570e5342dd1b?tpId=37&tqId=21298&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    @Test
    public void LCS_OJ() {

        System.out.println(this.LCS("bacefaebcdfabfaadebdaacabbdabcfffbdcebaabecefddfaceeebaeabebbad", "dedcecfbbbecaffedcedbadadbbfaafcafdd"));

        //测试输入内容
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            String str = scanner.nextLine();

            System.out.println(this.LCS(str.split(" ")[0], str.split(" ")[1]));

        }


    }

    /**
     * @param firstStr
     * @param secondStr
     * @return
     * @function 计算最长公共子序列
     */
    public int LCS(String firstStr, String secondStr) {

        //采用自底向上的计算方法
        //当前公共字串长度 =
        //若当前比较字符串相同 = LCS(firstStr - 1, secondStr - 1) + 1
        //若当前比较字符串不同 = Max(LCS(firstStr , secondStr - 1),LCS(firstStr - 1, secondStr));

        int firstStrLength = firstStr.length();

        int secondStrLength = secondStr.length();

        if (firstStrLength == 0 || secondStrLength == 0) {
            //如果两个字符串中某个长度为0,则返回0
            return 0;
        }

        char firstStrLastChar = firstStr.charAt(firstStrLength - 1);

        char secondStrLastChar = secondStr.charAt(secondStrLength - 1);

        if (firstStrLastChar == secondStrLastChar) {

            return 1 + LCS(firstStr.substring(0, firstStrLength - 1), secondStr.substring(0, secondStrLength - 1));

        } else {

            int LCS_1 = LCS(firstStr.substring(0, firstStrLength - 1), secondStr.substring(0, secondStrLength));

            int LCS_2 = LCS(firstStr.substring(0, firstStrLength), secondStr.substring(0, secondStrLength - 1));

            if (LCS_1 > LCS_2) {
                return LCS_1;
            } else {
                return LCS_2;
            }

        }

    }

    public static void main(String args[]) {
        try {
            String s = LCSAlgorithm("bacefaebcdfabfaadebdaacabbdabcfffbdcebaabecefddfaceeebaeabebbad", "dedcecfbbbecaffedcedbadadbbfaafcafdd");

            System.out.println(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}