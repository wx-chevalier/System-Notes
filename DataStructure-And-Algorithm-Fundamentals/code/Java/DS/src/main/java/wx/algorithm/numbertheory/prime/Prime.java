package wx.algorithm.numbertheory.prime;

/**
 * Created by apple on 16/8/12.
 */

import org.junit.Test;

import java.util.*;

/**
 * @function 存放一系列质数相关的算法
 */
public class Prime {

    /**
     * @param ulDataInput
     * @return
     * @function 输入一个正整数，按照从小到大的顺序输出它的所有质数的因子（如180的质数因子为2 2 3 3 5 ）
     * @OJ http://www.nowcoder.com/practice/196534628ca6490ebce2e336b47b3607?tpId=37&tqId=21229&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public static List<Integer> primeFactor(long ulDataInput) {

        List<Integer> treeSet = new ArrayList<>();

        while (ulDataInput > 2) {

            for (int i = 2; i < ulDataInput + 1; i++) {

                if (ulDataInput % i == 0) {
                    treeSet.add(i);
                    ulDataInput = ulDataInput / i;
                    break;
                }

            }

        }

        Collections.sort(treeSet);

        return treeSet;

    }

    @Test
    public void test_primeFactor() {
        System.out.println(primeFactor(64577));
    }


}
