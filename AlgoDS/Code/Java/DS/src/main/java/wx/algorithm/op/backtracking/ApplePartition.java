package wx.algorithm.op.backtracking;

/**
 * Created by apple on 16/8/13.
 */

import org.junit.Test;

import java.sql.Array;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @function 这是分苹果问题
 * @description 把M个同样的苹果放在N个同样的盘子里，允许有的盘子空着不放，问共有多少种不同的分法？（用K表示）5，1，1和1，5，1 是同一种分法。
 * @OJ http://www.nowcoder.com/practice/bfd8234bb5e84be0b493656e390bdebf?tpId=37&tqId=21284&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class ApplePartition {

    //记录所有可行分法的总数
    public int count = 0;


    /**
     * @param m            总的苹果数目
     * @param currentPanID 当前需要装盘的盘子编号
     * @param pans         目前的盘子的分配情况
     * @param leftApples   剩余苹果数
     * @function 为每个盘子分配合适的苹果数目
     */
    public void assignSinglePan(int m, int currentPanID, int[] pans, int leftApples) {

        //判断当前盘子是否是最后一个,且是否能够装下合适的苹果
        if (currentPanID == pans.length - 1) {

            //如果只有一个盘子,则直接OK
            if (pans.length == 1) {
                this.count++;
                pans[0] = leftApples;
                this.display(pans);
                return;
            }

            //已经分配到了最后一个
            //判断是否有效分配苹果数
            if (leftApples >= pans[currentPanID - 1]) {
                //如果剩余苹果数还够装一盘
                pans[currentPanID] = leftApples;
                this.display(pans);

                this.count++;
            }
            return;
        }

        //判断当前应该装的苹果数,当前可以装的苹果数目为 pans[currentPanID - 1] || 1 ~ leftApples / pans.length() - currentPanID
        //当前可以装的最少苹果数为1或者上一个盘子的数目
        int minApples;

        if (currentPanID > 0) {
            minApples = pans[currentPanID - 1];
        } else {
            minApples = 1;
        }

        for (int i = minApples; i < leftApples / (pans.length - currentPanID) + 1; i++) {

            //为当前盘子分配苹果
            pans[currentPanID] = i;

            //开始尝试分配下一个盘子的苹果
            assignSinglePan(m, currentPanID + 1, pans, leftApples - i);

            //将当前盘子的苹果重置为1
            pans[currentPanID] = 1;

        }

    }

    public void display(int[] array) {

        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }

        System.out.println();
    }

    /**
     * @param m 苹果数目
     * @param n 盘子数目
     * @function 进行分割
     */
    public void partition(int m, int n) {

        //分别选择使用1 ~ n个盘子,保证,选中的每个盘子至少放1个,否则放0个
        for (int i = 1; i < n + 1; i++) {

            int[] pans = new int[i];

            this.assignSinglePan(m, 0, pans, m);
        }

    }

    /**
     * @function 单纯地求解分割的数目
     */
    public int partitionNumber(int m, int n) {

         /*
            放苹果分为两种情况，一种是有盘子为空，一种是每个盘子上都有苹果。
            令(m,n)表示将m个苹果放入n个盘子中的摆放方法总数。
            1.假设有一个盘子为空，则(m,n)问题转化为将m个苹果放在n-1个盘子上，即求得(m,n-1)即可
            2.假设所有盘子都装有苹果，则每个盘子上至少有一个苹果，即最多剩下m-n个苹果，问题转化为将m-n个苹果放到n个盘子上
            即求(m-n，n)

            综上所述：
            (m，n)=(m,n-1)+(m-n,n);
        */
        if (m < 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        } else {
            return partitionNumber(m, n - 1) + partitionNumber(m - n, n);
        }

    }

    @Test
    public void ApplePartition_OJ() {


        this.partition(4, 1);

        System.out.println(this.count);

        System.out.println(this.partitionNumber(7, 3));

    }

    public static void main(String args[]) {


        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            ApplePartition applePartition = new ApplePartition();

            int m = scanner.nextInt();
            int n = scanner.nextInt();

            applePartition.partition(m, n);
            System.out.print(applePartition.count);
        }

    }

}
