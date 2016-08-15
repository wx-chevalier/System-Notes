package wx.ds.string.longestpalindrome;

/**
 * Created by apple on 16/8/13.
 */

import java.util.Scanner;

/**
 * @function 利用Manacher法求取最长回文字串
 */
public class Manacher {

    /**
     * @param str 原字符串
     * @return 最长回文字串的长度
     * @function 核心求解算法
     */
    public static int manacher(String str) {

        int strLength = str.length();

        //构造新字符串 长度为 2 * strLength + 1

        StringBuilder stringBuilder = new StringBuilder();

        //第一个字符串使用特殊字符
        stringBuilder.append("$");

        stringBuilder.append("#");

        for (int i = 0; i < strLength; i = i + 1) {

            stringBuilder.append(str.charAt(i));
            stringBuilder.append("#");
        }

        stringBuilder.append("%");

        //填充之后的字符串
        String filledStr = stringBuilder.toString();

        //初始化P数组,用于指向以每个字符为圆心的回文字符串的半径
        int[] P = new int[filledStr.length()];

        int id = 0; //指向正在求取回文半径的第i个字符之前的最长回文字串的轴心点的下标

        int mx = 0; //指向正在求取回文半径的第i个字符之前的最长回文字串的半径

        //开始从下标为2开始遍历新的字符串
        for (int i = 2; i < filledStr.length(); i++) {

            //判断当前点在之前字串中的对称点
            if ((mx - i) > 0 && 2 * id - i > 2) {

                //如果之前存在有效的回文字串,则取二者的最小值
                P[i] = Math.min(mx - i, P[2 * id - i]);

            } else {

                //否则初始化为1
                P[i] = 1;
            }

            //以当前点为中心计算半径
            while ((i + P[i]) < filledStr.length()
                    && (i - P[i]) > 0
                    && filledStr.charAt(i + P[i]) == filledStr.charAt(i - P[i])) {
                P[i]++;
            }

            //判断当前点的半径是否已经大于了原来的最大值
            if (P[i] > mx) {
                mx = P[i];

                id = i;
            }
        }


        return mx - 1;

    }

    /**
     * @function Catcher是MCA国的情报员，他工作时发现敌国会用一些对称的密码进行通信，比如像这些ABBA，ABA，A，123321，但是他们有时会在开始或结束时加入一些无关的字符以防止别国破解。比如进行下列变化 ABBA->12ABBA,ABA->ABAKK,123321->51233214　。因为截获的串太长了，而且存在多种可能的情况（abaaab可看作是aba,或baaab的加密形式），Cathcer的工作量实在是太大了，他只能向电脑高手求助，你能帮Catcher找出最长的有效密码串吗？
     * @OJ http://www.nowcoder.com/practice/3cd4621963e8454594f00199f4536bb1?tpId=37&tqId=21255&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public static void Catcher_OJ() {

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            System.out.println(manacher(str));

        }

    }

    public static void main(String args[]) {

        System.out.println(manacher("ab32ba"));

        Catcher_OJ();

    }
}
