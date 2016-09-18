package wx.ds.string.palindrome;

/**
 * Created by apple on 16/9/2.
 */

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @function 回文子串划分问题
 */
public class PalindromePartition {


    /**
     * @param str
     * @param partitionedStr
     * @param result
     * @param pos
     * @function 使用回溯法进行所有的回文子串切分
     * @OJ https://leetcode.com/problems/palindrome-partitioning/
     */
    public void partitionBacktracking(String str, List<String> partitionedStr, List<List<String>> result, int pos) {

        //判断当前字符串是否到头了
        if (pos == str.length()) {

            //将本次的结果添加到result中
            result.add(new ArrayList<String>(partitionedStr));

            return;
        }

        //否则开始递归求取字符串
        for (int i = pos + 1; i < str.length() + 1; i++) {

            String subStr = str.substring(pos, i);

            //判断是否为回文字符串
            if (isPal(subStr)) {

                partitionedStr.add(subStr);

                //递归调用
                partitionBacktracking(str, partitionedStr, result, i);

                //回溯
                partitionedStr.remove(partitionedStr.size() - 1);

            } else {
                //否则直接跳过
                continue;
            }

        }

    }

    @Test
    public void test_partitionBacktracking() {

        List<String> partitionedStr = new ArrayList<>();

        List<List<String>> result = new ArrayList<>();

        partitionBacktracking("afdafabbacddadscadsfdsaab", partitionedStr, result, 0);

        System.out.println(result);

    }


    /**
     * @function 搜索最小切分数
     * @OJ https://leetcode.com/problems/palindrome-partitioning-ii/
     */
    public int partitionMincut(String str) {

        int n = str.length();

        //记录对于前K个字符最小的切分数
        int cut[] = new int[n + 1];

        char[] s = str.toCharArray();

        //设置初始值
        for (int i = 0; i <= n; i++) cut[i] = i - 1;

        for (int i = 0; i < n; i++) {

            //判断从i开始的长度为j的回文子串,这里是寻找奇数长度的串
            for (int j = 0; i - j >= 0 && i + j < n && s[i - j] == s[i + j]; j++) // odd length palindrome
                //找到某个字串,则更新该字串下个点的切分数
                cut[i + j + 1] = Math.min(cut[i + j + 1], 1 + cut[i - j]);

            //这里是寻找偶数长度的串
            for (int j = 1; i - j + 1 >= 0 && i + j < n && s[i - j + 1] == s[i + j]; j++) // even length palindrome
                cut[i + j + 1] = Math.min(cut[i + j + 1], 1 + cut[i - j + 1]);
        }
        return cut[n];

    }

    /**
     * @param s
     * @return
     * @function 判断字符串s是否为回文字串
     */
    private boolean isPal(String s) {

        int i = 0;

        while (i < s.length() / 2) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {

                return false;

            }

            i++;
        }
        return true;

    }

    @Test
    public void test_isPal() {

        Assert.assertEquals(isPal("a"), true);

        Assert.assertEquals(isPal("ab"), false);

        Assert.assertEquals(isPal("abba"), true);

        Assert.assertEquals(isPal("abdba"), true);

        Assert.assertEquals(isPal("abdbad"), false);

    }


}
