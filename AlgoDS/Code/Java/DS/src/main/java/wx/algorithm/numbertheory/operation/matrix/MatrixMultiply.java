package wx.algorithm.numbertheory.operation.matrix;

/**
 * Created by apple on 16/8/17.
 */

import java.util.Scanner;

/**
 * @function 矩阵乘法
 * @description 如果A是个x行y列的矩阵，B是个y行z列的矩阵，把A和B相乘，其结果将是另一个x行z列的矩阵C。
 * @OJ http://www.nowcoder.com/practice/ebe941260f8c4210aa8c17e99cbc663b?tpId=37&tqId=21292&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class MatrixMultiply {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int row = sc.nextInt();
            int column_row = sc.nextInt();
            int column = sc.nextInt();
            int[][] x = new int[row][column_row];
            int[][] y = new int[column_row][column];
            for (int i = 0; i < row; i++)
                for (int j = 0; j < column_row; j++)
                    x[i][j] = sc.nextInt();
            for (int i = 0; i < column_row; i++)
                for (int j = 0; j < column; j++)
                    y[i][j] = sc.nextInt();
            matrix_multiply(row, column_row, column, x, y);
        }
        sc.close();
    }

    /**
     * @function 执行矩阵乘法
     * @param row 第一个矩阵的行数
     * @param column_row 第一个矩阵的列数和第二个矩阵的行数
     * @param column 第二个矩阵的列数
     * @param x 第一个矩阵的值
     * @param y 第二个矩阵的值
     */
    public static void matrix_multiply(int row, int column_row, int column, int[][] x, int[][] y) {
        int[][] z = new int[row][column];
        for (int k = 0; k < row; k++) {
            for (int i = 0; i < column; i++) {
                for (int j = 0; j < column_row; j++) {
                    z[k][i] += x[k][j] * y[j][i];
                }
                //行末最后一个元素要换行
                if (i != column - 1)
                    System.out.print(z[k][i] + " ");
                else
                    System.out.println(z[k][i]);
            }
        }
    }
}
