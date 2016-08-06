package wx.algorithm.op.dp;

/**
 * Created by apple on 16/8/1.
 */

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

    public static void main(String args[]) {
        try {
            String s = LCSAlgorithm("adacsfsar3d", "ffadcdacewqe");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}