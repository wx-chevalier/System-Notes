package wx.algorithm.op.backtracking;

/**
 * Created by apple on 16/8/14.
 */

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @function 数独
 * @description 数独是一个我们都非常熟悉的经典游戏，运用计算机我们可以很快地解开数独难题，现在有一些简单的数独题目，请编写一个程序求解。
 * @OJ http://www.nowcoder.com/practice/2b8fa028f136425a94e1a733e92f60dd?tpId=49&tqId=29298&rp=3&ru=/ta/2016test&qru=/ta/2016test/question-ranking
 */
public class Sudoku {

    /**
     * @param index  当前要处理的下标
     * @param matrix 数据矩阵
     * @param row    存放某一行中包含的所有数据
     * @param column 存放某一列中包含的所有数据
     * @return 如果已经搜索到最后一个, 则成功, 否则失败
     * @function 每次迭代所需要处理的点
     */
    public static boolean bps(int index, int[][] matrix, ArrayList<HashSet<Integer>> row, ArrayList<HashSet<Integer>> column, ArrayList<HashSet<Integer>> squ) {

        if (index == 81) {

            //已经搜索到
            //打印矩阵
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if(j == 8){
                        System.out.print(matrix[i][j] + "");

                    }else {
                        System.out.print(matrix[i][j] + " ");

                    }
                }
                System.out.println();
            }

            return true;
        }

        //获取当前处理的点的坐标
        int x = index / 9;
        int y = index % 9;
        int z = x / 3 * 3 + y / 3;

        //判断当前点是否不为0
        //如果不为0,则直接搜索下一层
        if (matrix[x][y] != 0) {

            if (bps(index + 1, matrix, row, column, squ)) {
                return true;
            }
            return false;

        } else {

            //获取所有当前点的取值
            ArrayList<Integer> arrayList = new ArrayList<>();

            for (int i = 1; i < 10; i++) {
                //如果某个值还没有被包含
                if (!row.get(x).contains(i) && !column.get(y).contains(i) && !squ.get(z).contains(i)) {
                    arrayList.add(i);
                }
            }

            for (int i = 0; i < arrayList.size(); i++) {

                //将当前处理点设置状态
                matrix[x][y] = arrayList.get(i);

                //将当前值加入到所在行列
                row.get(x).add(arrayList.get(i));

                column.get(y).add(arrayList.get(i));

                squ.get(z).add(arrayList.get(i));


                //搜索下一层
                if (bps(index + 1, matrix, row, column,squ)) {
                    return true;
                }

                //进行回溯

                //将当前处理点设置状态
                matrix[x][y] = 0;

                //将当前值加入到所在行列
                row.get(x).remove(arrayList.get(i));

                column.get(y).remove(arrayList.get(i));

                squ.get(z).remove(arrayList.get(i));
            }

            return false;
        }


    }

    /**
     * 测试用例:
     * 7 2 6 9 0 4 0 5 1
     * 0 8 0 6 0 7 4 3 2
     * 3 4 1 0 8 5 0 0 9
     * 0 5 2 4 6 8 0 0 7
     * 0 3 7 0 0 0 6 8 0
     * 0 9 0 0 0 3 0 1 0
     * 0 0 0 0 0 0 0 0 0
     * 9 0 0 0 2 1 5 0 0
     * 8 0 0 3 0 0 0 0 0
     * <p>
     * 对应输出应该为:
     * <p>
     * 7 2 6 9 3 4 8 5 1
     * 5 8 9 6 1 7 4 3 2
     * 3 4 1 2 8 5 7 6 9
     * 1 5 2 4 6 8 3 9 7
     * 4 3 7 1 9 2 6 8 5
     * 6 9 8 5 7 3 2 1 4
     * 2 1 5 8 4 6 9 7 3
     * 9 6 3 7 2 1 5 4 8
     * 8 7 4 3 5 9 1 2 6
     * 测试用例:
     * 0 9 5 0 2 0 0 6 0
     * 0 0 7 1 0 3 9 0 2
     * 6 0 0 0 0 5 3 0 4
     * 0 4 0 0 1 0 6 0 7
     * 5 0 0 2 0 7 0 0 9
     * 7 0 3 0 9 0 0 2 0
     * 0 0 9 8 0 0 0 0 6
     * 8 0 6 3 0 2 1 0 5
     * 0 5 0 0 7 0 2 8 3
     * <p>
     * 对应输出应该为:
     * <p>
     * 3 9 5 7 2 4 8 6 1
     * 4 8 7 1 6 3 9 5 2
     * 6 2 1 9 8 5 3 7 4
     * 9 4 2 5 1 8 6 3 7
     * 5 6 8 2 3 7 4 1 9
     * 7 1 3 4 9 6 5 2 8
     * 2 3 9 8 5 1 7 4 6
     * 8 7 6 3 4 2 1 9 5
     * 1 5 4 6 7 9 2 8 3
     *
     * @param args
     */
    public static void main(String args[]) {

        Scanner scanner = new Scanner(System.in);

        //存放每一行的数据
        ArrayList<HashSet<Integer>> row = new ArrayList<HashSet<Integer>>();

        //存放每一列的数据
        ArrayList<HashSet<Integer>> column = new ArrayList<HashSet<Integer>>();

        //存放对角线的数据
        ArrayList<HashSet<Integer>> squ = new ArrayList<HashSet<Integer>>();


        while (scanner.hasNext()) {

            int[][] matrix = new int[9][9];

            for (int i = 0; i < 9; i++) {
                row.add(new HashSet<Integer>());
                column.add(new HashSet<Integer>());
                squ.add(new HashSet<Integer>());
            }

            for (int i = 0; i < 9; i++) {


                for (int j = 0; j < 9; j++) {

                    int number = scanner.nextInt();

                    matrix[i][j] = number;

                    row.get(i).add(number);

                    column.get(j).add(number);

                    squ.get(i / 3 * 3 + j / 3).add(number);
                }

            }

            bps(0, matrix, row, column, squ);
        }

    }


}
