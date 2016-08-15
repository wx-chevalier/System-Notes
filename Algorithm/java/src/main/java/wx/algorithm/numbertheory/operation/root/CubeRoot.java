package wx.algorithm.numbertheory.operation.root;

/**
 * Created by apple on 16/8/13.
 */

/**
 * @function 计算立方根
 */
public class CubeRoot {

    public static double Babylonian(double number) {

        //使用暴力逼近法,设置一个max、一个mid和一个min

        double max = number;

        double min = 0;

        double mid;

        while ((max - min) > 0.0001) {

            mid = (max + min) / 2;

            //判断mid是落在左边还是右边
            if (mid * mid * mid > number) {

                max = mid;

            } else if (mid * mid * mid < number) {
                min = mid;

            } else {

                return mid;

            }


        }

        return max;

    }

    public static void main(String args[]) {


        System.out.println(Babylonian(216.0));
    }

}
