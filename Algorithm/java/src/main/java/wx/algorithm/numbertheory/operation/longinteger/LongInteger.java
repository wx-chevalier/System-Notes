package wx.algorithm.numbertheory.operation.longinteger;

/**
 * Created by apple on 16/8/16.
 */
public class LongInteger {

    /**
     * @param addend 加数
     * @param augend 被加数
     * @return 加法结果
     * @function 请设计一个算法完成两个超长正整数的加法。
     * @OJ http://www.nowcoder.com/practice/5821836e0ec140c1aa29510fd05f45fc?tpId=37&tqId=21301&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public String AddLongInteger(String addend, String augend) {


        int addendLength = addend.length();

        int augendLength = augend.length();

        int resultLength = Math.max(addendLength, addendLength) + 1;

        //构建空字符串

        for (int i = 0; i < resultLength; i++) {

        }

    }

}
