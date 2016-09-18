package wx.algorithm.numbertheory.operation.matrix;

import java.util.Arrays;

/**
 * Created by apple on 16/9/1.
 */
public class SnakeMatrix {


    /**
     * @param matrix 存放全部的矩阵
     * @param i      当前要处理的行号
     * @param j      当前要处理的列号
     * @test 123
     * 894
     * 765
     * @function
     */
    public static void snakeMatrix(int[][] matrix, int i, int j, boolean rowOrColumn) {

        int value = matrix[i][j];

        if (rowOrColumn) {
            //如果是某一行

            //判断是否向右
            if (j < matrix[0].length - 1 && matrix[i][j + 1] == 0) {

                while (j < matrix[0].length - 1 && matrix[i][j + 1] == 0) {
                    matrix[i][j + 1] = matrix[i][j] + 1;
                    j++;
                }

                //判断当前向下是否有路
                if (i < matrix.length - 1 && matrix[i + 1][j] == 0) {

                    snakeMatrix(matrix, i, j, false);
                }

            }

            //判断是否向左
            if (j > 0 && matrix[i][j - 1] == 0) {

                while (j > 0 && matrix[i][j - 1] == 0) {
                    matrix[i][j - 1] = matrix[i][j] + 1;
                    j--;
                }

                //判断当前向上是否有路
                if (i > 0 && matrix[i - 1][j] == 0) {

                    snakeMatrix(matrix, i, j, false);
                }

            }


        } else {
            //判断向上还是向下

            //判断是否向下
            if (i < matrix.length - 1 && matrix[i + 1][j] == 0) {

                while (i < matrix.length - 1 && matrix[i + 1][j] == 0) {
                    matrix[i + 1][j] = matrix[i][j] + 1;
                    i++;
                }

                //判断当前向左是否有路
                if (j > 0 && matrix[i][j - 1] == 0) {

                    snakeMatrix(matrix, i, j, true);
                }

            }

            //判断是否向上
            if (i > 0 && matrix[i - 1][j] == 0) {

                while (i > 0 && matrix[i - 1][j] == 0) {
                    matrix[i - 1][j] = matrix[i][j] + 1;
                    i--;
                }

                //判断当前向右是否有路
                if (j < matrix[0].length - 1 && matrix[i][j + 1] == 0) {

                    snakeMatrix(matrix, i, j, true);
                }

            }
        }

    }

    public static void main(String args[]) {


        int N = 5;

        int[][] matrix = new int[N][N];

        matrix[0][0] = 1;

        snakeMatrix(matrix, 0, 0, true);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + " ");
            }

            System.out.println();
        }

    }

}
