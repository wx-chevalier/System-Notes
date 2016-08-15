package wx.ds.string.length;

/**
 * Created by apple on 16/8/12.
 */

import org.junit.Test;

import java.util.*;


public class StringLength {

    /**
     * @function 计算字符串最后一个单词的长度，单词以空格隔开。
     * @OJ http://www.nowcoder.com/practice/8c949ea5f36f422594b306a2300315da?tpId=37&tqId=21224&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public void computeLastWordLength() {

        Scanner scanner = new Scanner(System.in);

        //获取输入的字符串
        String str = scanner.nextLine();

        //从字符串最后一个进行遍历
        int length = str.length();

        //单词长度
        int wordLength = 0;

        for (int i = length - 1; i > -1; i--) {
            if (str.charAt(i) != ' ') {
                wordLength++;
                continue;
            }

            break;
        }

        System.out.println(wordLength);

    }

    /**
     * @function 写出一个程序，接受一个有字母和数字以及空格组成的字符串，和一个字符，然后输出输入字符串中含有该字符的个数。不区分大小写。
     * @OJ http://www.nowcoder.com/practice/a35ce98431874e3a820dbe4b2d0508b1?tpId=37&tqId=21225&rp=&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
     */
    public void computeWordCountInString() {

        Scanner scanner = new Scanner(System.in);

        //获取输入的字符串
        String str = scanner.nextLine();

        String c = scanner.nextLine();

        //从字符串最后一个进行遍历
        int length = str.length();

        //单词长度
        int wordCount = 0;

        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == c.charAt(0) || str.charAt(i) == (c.charAt(0) - 32) || str.charAt(i) == (c.charAt(0) + 32)) {
                wordCount++;
            }
        }


        System.out.println(wordCount);
    }

    /**
     * @param str
     * @function 输入一个字符串，求出该字符串包含的字符集合
     * @OJ http://www.nowcoder.com/practice/784efd40ed8e465a84821c8f3970b7b5?tpId=49&tqId=29297&rp=3&ru=/ta/2016test&qru=/ta/2016test/question-ranking
     */
    public void containedLetters(String str) {

        //使用Set记录是否存在,List记录插入的顺序
        List<Character> letters = new ArrayList<Character>();

        Set<Character> letterSet = new HashSet<>();

        for (int i = 0; i < str.length(); i++) {
            if (!letterSet.contains(str.charAt(i))) {
                letters.add(str.charAt(i));

                letterSet.add(str.charAt(i));
            }
        }

        for (int i = 0; i < letters.size(); i++) {
            System.out.print(letters.get(i));
        }

        System.out.println();

    }

    @Test
    public void containedLetters_OJ() {

//        containedLetters("abcqweracb");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String str = scanner.nextLine();

            containedLetters(str);

        }

    }


    public static void main(String args[]) {

        System.out.print((char) ('c' - 32));

    }

}
