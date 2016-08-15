package wx.ds.graph.search.dfs.maze;

/**
 * Created by apple on 16/8/13.
 */

import java.util.Scanner;

/**
 * @function 请编写一个函数（允许增加子函数），计算n x m的棋盘格子（n为横向的格子数，m为竖向的格子数）沿着各自边缘线从左上角走到右下角，总共有多少种走法，要求不能走回头路，即：只能往右和往下走，不能往左和往上走。
 * @OJ http://www.nowcoder.com/practice/e2a22f0305eb4f2f9846e7d644dba09b?tpId=37&tqId=21314&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class ChessboardWithAllSolution {

    /**
     * @function 描述棋盘上的每个点
     */
    static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //计算器,没找到一个可行解加一
    private int count = 0;

    //棋盘行个数
    private int n;

    //棋盘列个数
    private int m;


    public ChessboardWithAllSolution(int n, int m) {
        this.n = n;
        this.m = m;
    }

    public void go(Point point) {


        //判断是否走到了右下角
        if (point.x == (n - 1) && point.y == (m - 1)) {
            this.count++;
            return;
        }

        //否则选择走右边
        if (point.y + 1 < m) {
            go(new Point(point.x, point.y + 1));
        }

        //或者走下边
        if (point.x + 1 < n) {
            go(new Point(point.x + 1, point.y));
        }

    }

    public static void main(String args[]) {

//        ChessboardWithAllSolution chessboardWithAllSolution = new ChessboardWithAllSolution(3, 3);

        ChessboardWithAllSolution chessboardWithAllSolution;

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            int n = scanner.nextInt();

            int m = scanner.nextInt();

            chessboardWithAllSolution = new ChessboardWithAllSolution(n + 1, m + 1);

            chessboardWithAllSolution.go(new Point(0, 0));

            System.out.println(chessboardWithAllSolution.count);

        }


    }

}
