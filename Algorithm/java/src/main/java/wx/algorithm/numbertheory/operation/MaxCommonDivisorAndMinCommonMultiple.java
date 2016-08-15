package wx.algorithm.numbertheory.operation;

import java.util.Scanner;

/**
 * Created by apple on 16/8/12.
 */

/**
 * @function 求取最大公约数和最小公倍数
 */
public class MaxCommonDivisorAndMinCommonMultiple {

    // 递归法求最大公约数
    public int maxCommonDivisor(int m, int n) {
        if (m < n) {// 保证m>n,若m<n,则进行数据交换
            int temp = m;
            m = n;
            n = temp;
        }
        if (m % n == 0) {// 若余数为0,返回最大公约数
            return n;
        } else { // 否则,进行递归,把n赋给m,把余数赋给n
            return maxCommonDivisor(n, m % n);
        }
    }

    // 循环法求最大公约数
    public int maxCommonDivisor2(int m, int n) {

        if (m < n) {// 保证m>n,若m<n,则进行数据交换
            int temp = m;
            m = n;
            n = temp;
        }
        while (m % n != 0) {// 在余数不能为0时,进行循环
            int temp = m % n;
            m = n;
            n = temp;
        }
        return n;// 返回最大公约数
    }

    // 求最小公倍数
    public int minCommonMultiple(int m, int n) {
        return m * n / maxCommonDivisor(m, n);
    }

    /**
     * @function 求取最小公倍数正整数A和正整数B 的最小公倍数是指 能被A和B整除的最小的正整数值，设计一个算法，求输入A和B的最小公倍数。
     * @OJ http://www.nowcoder.com/practice/22948c2cad484e0291350abad86136c3?tpId=37&tqId=21331&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public void minCommonMultipleOJ() {

        Scanner scanner = new Scanner(System.in);


        int A = scanner.nextInt();

        int B = scanner.nextInt();

        if (A == 0 || B == 0) {

            System.out.println(0);

            return;
        }

        //首先求取最大公约数

        //交换保证A > B
        if (A < B) {

            int temp = A;

            A = B;

            B = temp;
        }

        int m = A;

        int n = B;

        //使用辗转相除法
        while (m % n > 0 && n > 1) {

            int temp = m;

            m = n;

            n = temp % n;

        }

        System.out.println((A * B) / n);

    }

    /**
     * @function
     */
    public static void main(String args[]) {

        MaxCommonDivisorAndMinCommonMultiple maxCommonDivisorAndMinCommonMultiple = new MaxCommonDivisorAndMinCommonMultiple();

        maxCommonDivisorAndMinCommonMultiple.minCommonMultipleOJ();

    }
}
