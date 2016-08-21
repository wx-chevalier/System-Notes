package wx.algorithm.op.backtracking;

/**
 * Created by apple on 16/8/16.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @function 24点算法 给出4个1-10的数字，通过加减乘除，得到数字为24就算胜利
 * @OJ http://www.nowcoder.com/practice/fbc417f314f745b1978fc751a54ac8cb?tpId=37&tqId=21290&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class Game24 {

    /**
     * @param permutation
     * @function 求出所有可能的排列
     */
    public static void arrange(int maxNum, ArrayList<Integer> visited, ArrayList<Integer[]> permutation) {


        //判断是否到头了
        if (visited.size() == maxNum) {

            //如果全部遍历了,添加可行解
            Integer[] tempPermutation = new Integer[4];

            permutation.add(visited.toArray(tempPermutation));
        }

        //从未遍历的当中选择某个数
        for (int i = 0; i < maxNum; i++) {
            if (!visited.contains(i)) {

                visited.add(i);

                //如果visited尚未包含,设置为当前值,进入下一轮遍历
                arrange(maxNum, visited, permutation);

                //本轮搜索结束,回溯
                visited.remove(visited.indexOf(i));
            }
        }

    }

    /**
     * @return
     * @function 判断是否为24点
     */
    public static boolean dpsSolved(int[] numArray, int index, int result) {

        if (index == 3) {
            //如果已经遍历结束
            if (result == 24) {
                return true;
            } else {
                return false;
            }
        }

        //如果尚未遍历结束,进行遍历
        //加
        if (dpsSolved(numArray, index + 1, result + numArray[index])) {
            return true;
        }

        //减
        if (dpsSolved(numArray, index + 1, result - numArray[index])) {
            return true;
        }

        //乘
        if (dpsSolved(numArray, index + 1, result * numArray[index])) {
            return true;
        }

        //除
        if (dpsSolved(numArray, index + 1, result / numArray[index])) {
            return true;
        }

        return false;

    }


    public static void main(String args[]) {

//        int[] numbers = new int[]{1, 1, 1, 1};

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            int[] numbers = new int[4];

            for (int i = 0; i < 4; i++) {
                numbers[i] = scanner.nextInt();
            }

            ArrayList<Integer> visited = new ArrayList<>();

            ArrayList<Integer[]> permutation = new ArrayList<>();

            arrange(4, visited, permutation);

            boolean flag = false;

            //已经找出了全部的组合
            //下面根据每个组合计算所有操作
            for (int i = 0; i < permutation.size(); i++) {

                Integer[] per = permutation.get(i);

                int[] numArray = new int[4];

                for (int j = 0; j < numbers.length; j++) {
                    numArray[j] = numbers[per[j]];
                }

                if (dpsSolved(numArray, 0, numArray[0])) {
                    flag = true;
                    break;
                }


            }

            System.out.println(flag);


        }
    }

}


