package wx.algorithm.numbertheory.operation.bit;

/**
 * Created by apple on 16/8/13.
 */

import org.junit.Test;

import java.util.Scanner;

/**
 * @function 其他类型转化为Bit类型的计算
 */
public class BitConverter {

    /**
     * @function 输入一个int型数据，计算出该int型数据在内存中存储时1的个数。
     * @OJ http://www.nowcoder.com/practice/440f16e490a0404786865e99c6ad91c9?tpId=37&tqId=21238&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    @Test
    public void integerBits() {

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            int number = scanner.nextInt();

            //计算Bit中为1的数目
            int count = 0;
            
            while (number > 0) {
                if ((number & 1) > 0) {
                    count++;
                }

                number = number >> 1;
            }

        }

    }

}
