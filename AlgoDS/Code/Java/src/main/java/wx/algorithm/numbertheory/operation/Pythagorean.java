package wx.algorithm.numbertheory.operation;

/**
 * Created by apple on 16/8/25.
 */

/**
 * @function 利用Java实现的勾股定理
 */
public class Pythagorean {


    public static boolean isPythagorean(int a, int b, int c) {

        //求取a,b,c中最大值
        int max = Math.max(a, b);

        max = Math.max(max, c);


        if (max == a) {

            return a * a == b * b + c * c;

        } else if (max == b) {

            return b * b == a * a + c * c;

        } else {
            return c * c == a * a + b * b;
        }

    }

    public static void main(String args[]) {

        System.out.println(isPythagorean(1, 2, 3));

        System.out.println(isPythagorean(1, 3, 2));

        System.out.println(isPythagorean(4, 3, 5));
    }
}
