package wx.algorithm.op.recursion.fibonacci;

import java.util.*;

/**
 * @function 无限兔子繁殖问题
 * @description 有一只兔子，从出生后第3个月起每个月都生一只兔子，小兔子长到第三个月后每个月又生一只兔子，假如兔子都不死，问每个月的兔子总数为多少？
 * @OJ http://www.nowcoder.com/practice/1221ec77125d4370833fd3ad5ba72395?tpId=37&tqId=21260&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class Rabbit {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int monthCount = in.nextInt();
            System.out.println(getTotalCount(monthCount));
        }
        in.close();
    }

    public static int getTotalCount(int monthCount) {

        if (monthCount == 1 || monthCount == 2) {
            return 1;
        }

        return getTotalCount(monthCount - 1) + getTotalCount(monthCount - 2);

    }
}