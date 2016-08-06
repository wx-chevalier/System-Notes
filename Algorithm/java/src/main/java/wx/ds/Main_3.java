package wx.ds;

import java.util.Scanner;

/**
 * Created by apple on 16/8/2.
 */
public class Main_3 {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);

        //获取总行数
        int n = in.nextInt();

        //获取总列数
        int m = in.nextInt();

        //存放土地价值
        int[][] area = new int[n][m];

        int totalValue = 0; //田地的总价值

        //临时循环变量
        int i, j;

        //获取所有变量,注意,只能按行获取
        for (i = 0; i < n; i++) {

            String str = "";

            while (in.hasNext()) {

                //直接获取一行数据
                str = in.next();

                break;
            }

            //将一行分割为数字
            for (j = 0; j < m; j++) {
                area[i][j] = new Integer(str.charAt(j));

                totalValue += area[i][j]; //计算出田地的总价值
            }
        }

        //计算出平均值
        //牛牛分得的最大土地肯定不会超过该价值
        //我们要做的就是要逼近该价值
        int overheadValue = totalValue / 16;



        System.out.println(overheadValue);

    }
}
