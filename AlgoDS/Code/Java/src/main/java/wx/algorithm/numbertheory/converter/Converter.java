package wx.algorithm.numbertheory.converter;

/**
 * Created by apple on 16/8/13.
 */

import java.util.Scanner;

/**
 * @function 存放一系列进制转换相关的代码
 */
public class Converter {

    /**
     * @param hex 十六进制数
     * @function 十六进制向十进制转换
     * @OJ http://www.nowcoder.com/practice/8f3df50d2b9043208c5eed283d1d4da6?tpId=37&tqId=21228&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public static long hex2Decimal(String hex) {

        long sum = 0;

        for (int i = hex.length() - 1; i > 1; i--) {

            char number = hex.charAt(i);

            int numberInteger;

            if (number >= '0' & number <= '9') {
                numberInteger = number - '0';
            } else {
                numberInteger = number - 'A' + 10;
            }

            sum += Math.pow(16, hex.length() - i - 1) * numberInteger;

        }

        return sum;

    }

    public static void main(String args[]) {

        System.out.println(hex2Decimal("0xA1"));

    }

}


