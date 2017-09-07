package wx.algorithm.numbertheory.display;

/**
 * Created by apple on 16/8/13.
 */

/**
 * @function 杨辉三角
 * @OJ http://www.nowcoder.com/practice/8ef655edf42d4e08b44be4d777edbf43?tpId=37&tqId=21276&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class YHTriangle {

    /**
     * @function 展示杨辉三角形
     */
    public static void display(int n) {

        //存放前1000行
        int[][] yhTriangle = new int[n][2 * n + 1];

        yhTriangle[0][n] = 1;

        for (int i = 1; i < n; i++) {

            for (int j = n - i; j < n + i + 1; j++) {

                //下一行为上一行的左上角到右上角
                yhTriangle[i][j] = yhTriangle[i - 1][j - 1] + yhTriangle[i - 1][j] + yhTriangle[i - 1][j + 1];

            }

        }

        for (int i = 0; i < yhTriangle.length; i++) {
            for (int j = 0; j < yhTriangle[i].length; j++) {
                System.out.print(yhTriangle[i][j] + " ");
            }
            System.out.println();
        }


    }

    public static void main(String[] args) {

        display(10);

    }

}
