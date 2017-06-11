package wx.algorithm.numbertheory.operation.root;

/**
 * Created by apple on 16/7/29.
 */

/**
 * @function 平方根计算
 */
public class SquareRoot {


    /**
     * @param value 待求取平方根的值
     * @function 带参数的构造函数
     */
    SquareRoot(double value) {

        this.value = value;

    }

    /**
     * @return
     * @function 采用牛顿法计算平方根
     */
    public double Babylonian() {

        double g = this.value;

        while (isApproximate(g)) {
            g = (g + this.value / g) / 2;
        }

        return g;

    }

    /**
     * @return
     * @function 使用级数逼近的方法计算
     */
    public double TSqrt() {

        //设置修正系数
        double correction = 1;

        //因为要对原值进行缩小,因此设置临时值
        double tempValue = value;

        while (tempValue >= 2) {
            tempValue = tempValue / 4;
            correction *= 2;
        }

        return this.TSqrtIteration(tempValue) * correction;
    }

    private double TSqrtIteration(double value) {

        double sum = 0, coffe = 1, factorial = 1, xpower = 1, term = 1;

        int i = 0;

        while (Math.abs(term) > 0.000001) {

            sum += term;

            coffe *= (0.5 - i);

            factorial *= (i + 1);

            xpower *= (value - 1);

            term = coffe * xpower / factorial;

            i++;
        }

        return sum;

    }

    /**
     * @return
     * @function 平方根倒数速算法
     */
    public double FastInverseSquareRoot() {

        double tempValue = value;

        double xhalf = 0.5d * tempValue;

        long i = Double.doubleToLongBits(tempValue);

        i = 0x5fe6ec85e7de30daL - (i >> 1);

        tempValue = Double.longBitsToDouble(i);

        tempValue = tempValue * (1.5d - xhalf * tempValue * tempValue);

        tempValue = this.value * tempValue;

        return tempValue;

    }

    //存放待求平方根的值
    private double value;

    /**
     * @return
     * @function 判断当前计算出来的数与原值是否逼近
     */
    private boolean isApproximate(double v) {

        return Math.abs(v * v - value) > 0.000001;

    }

}
