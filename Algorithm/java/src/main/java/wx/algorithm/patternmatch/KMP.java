package wx.algorithm.patternmatch;

/**
 * Created by apple on 16/8/12.
 */
public class KMP {

    public static int[] getNext(String findStr) {

        //模式字符串的长度
        int len = findStr.length();

        int j = 0;

        //next表示长度为i的字符串前缀和后缀的最长公共部分，从1开始
        //next[1]表示长度为1的字符串前缀和后缀的最长公共部分,也就是在字符串下标1处匹配失败所需要回退到的位置
        int next[] = new int[len + 1];

        //初始化前两个数值为0
        next[0] = next[1] = 0;

        //从字符串下标1开始匹配
        //ababcd
        for (int i = 1; i < len; i++) {
            //在第 i 次循环中,我们计算的是 next[i + 1]的值,因为在考虑 i + 1位置时,需要将第 i 个字符进行考虑
            //j在每次循环开始都表示next[i]的值，同时也表示需要比较的下一个位置
            while (j > 0 &&
                    //直到找到离起点最近的一个与i字符相等的下标
                    findStr.charAt(i) != findStr.charAt(j)
                    ) {
                //不断分割子字符串
                //以abcdcabcd为例,next[7] = 2,这样就找到了离起点最近的那个c
                j = next[j];
            }

            //如果第 i 个字符与第 j 个字符相等,则把长度加1
            //这个if是判断第一个字符串与第i个字符串是否相等,相等的话还是要加1的
            //在j>0的情况下,这个是必然相等的
            if (findStr.charAt(i) == findStr.charAt(j)) j++;

            //将当前选定的j赋值给next[i+1]
            next[i + 1] = j;
        }

        return next;
    }

    public static void KMP(String originStr, String findStr) {

        if (originStr.length() == 0 || findStr.length() == 0) {
            //如果长度都为0,则直接跳过
            return;
        }

        //获取findStr的next数组
        int[] next = getNext(findStr);

        //开始匹配
        //遍历匹配字符串的下标
        int i = 0;

        //遍历模式字符串的下标
        int j = 0;

        while (i < originStr.length()) {

            //从当前位置i,j开始匹配
            //这里跳出的是i,j并不匹配的一位
            //如果i,j中任何一个到头了,也是 i = originStr.length() 或者 j = findStr.length()
            while (i < originStr.length() && j < findStr.length() && originStr.charAt(i) == findStr.charAt(j)) {
                i++;
                j++;
            }

            //此时匹配失败
            if (j == 0) {
                //直接第一个字符处失败,则直接i++
                i++;
                continue;
            }

            //判断是否提取出某个字符处
            if (j == findStr.length()) {

                System.out.println(originStr.subSequence(i - j, i));
                System.out.println(i - j);
            }

            //如果不是第一个字符处,则从j = next[j]处进行
            j = next[j];

        }

    }

    public static void main(String args[]) {


        KMP("sadddd2sadddd3asdascedddd3ds", "dddd");

        KMP("bbb", "bb");


    }

}
