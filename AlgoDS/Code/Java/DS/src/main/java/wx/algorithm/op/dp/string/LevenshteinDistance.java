package wx.algorithm.op.dp.string;


import java.util.Scanner;

/**
 * @function Levenshtein 距离，又称编辑距离，指的是两个字符串之间，由一个转换成另一个所需的最少编辑操作次数。许可的编辑操作包括将一个字符替换成另一个字符，插入一个字符，删除一个字符。编辑距离的算法是首先由俄国科学家Levenshtein提出的，故又叫Levenshtein Distance。
 * @OJ http://www.nowcoder.com/practice/3959837097c7413a961a135d7104c314?tpId=37&tqId=21275&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class LevenshteinDistance {

    public static int calStringDistance(String firstStr, String secondStr) {

        int firstStrLength = firstStr.length();

        int secondStrLength = secondStr.length();

        //初始化记录矩阵,注意,这里不同于LCS,需要设置为length+1的尺寸
        //这里的 0 0表示啥都没有,而不是字符串中的第一个字符
        int[][] dp = new int[firstStrLength + 1][secondStrLength + 1];

        //初始化记录矩阵
        int i, j;

        for (i = 1; i < firstStrLength + 1; i++) {
            dp[i][0] = i;
        }

        for (j = 1; j < secondStrLength + 1; j++) {
            dp[0][j] = j;
        }

        for (i = 1; i < firstStrLength + 1; i++) {
            for (j = 1; j < secondStrLength + 1; j++) {
                //判断当前字符是否相等
                if (firstStr.charAt(i - 1) == secondStr.charAt(j - 1)) {
                    //如果相等,则不需要修改
                    dp[i][j] = dp[i - 1][j - 1];


                } else {
                    //不相等
                    dp[i][j] = dp[i - 1][j - 1] + 1;

                }

                //设置其他可能的情况
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + 1);

                dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + 1);
            }
        }

        return dp[firstStrLength][secondStrLength];
    }

    public static void main(String[] args) {

//        System.out.println(calStringDistance("abcdefg","abcdef"));

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            String firstStr = scanner.nextLine();

            String secondStr = scanner.nextLine();

            System.out.println(calStringDistance(firstStr, secondStr));
        }

    }
}
