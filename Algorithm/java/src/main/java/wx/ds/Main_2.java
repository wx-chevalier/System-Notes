package wx.ds;

import java.util.Scanner;

/**
 * Created by apple on 16/8/2.
 */
public class Main_2 {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);

        //获取地牢长度
        int n = in.nextInt();

        //获取地牢宽度
        int m = in.nextInt();

        //存放地牢
        int[][] area = new int[n][m];

        //临时循环变量
        int i, j;

        //获取所有变量,注意,只能按行获取
        for (i = 0; i < n; i++) {

            String str = "";

            //遍历获取所有的
            while (in.hasNext()) {

                //直接获取一行数据
                str = in.next();

                break;
            }

            //将一行分割为数字
            for (j = 0; j < m; j++) {
                if (str.charAt(j) == '.') {
                    area[i][j] = 0; //0表示可以通过
                } else {
                    area[i][j] = 1; //1表示不可以通过
                }
            }
        }

        //获取起始位置
        int x0 = in.nextInt();
        int y0 = in.nextInt();

        //获取合法步长数
        int k = in.nextInt();

        int[][] move = new int[k][2];

        //获取所有的移动方向
        for (i = 0; i < k; i++) {

            move[i][0] = in.nextInt();

            move[i][1] = in.nextInt();

        }

        System.out.println(-1);


    }
}
