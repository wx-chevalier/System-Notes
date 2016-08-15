package wx.algorithm.patternmatch;

/**
 * Created by apple on 16/8/12.
 */

import org.junit.Test;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @function 判断某个字符是否存在的问题
 */
public class CharExist {

    /**
     * @function 判断短字符串中的所有字符是否在长字符串中全部出现
     * @OJ http://www.nowcoder.com/practice/22fdeb9610ef426f9505e3ab60164c93?tpId=37&tqId=21304&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    @Test
    public void boolIsAllCharExist_OJ() {


        //测试默认数据集
        System.out.println(this.boolIsAllCharExist("bdc", "abc"));


        //测试输入数据
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String pShortString = scanner.nextLine();

            String pLongString = scanner.nextLine();

            System.out.println(this.boolIsAllCharExist(pShortString, pLongString));
        }

    }

    /**
     * @param pShortString 短字符串
     * @param pLongString  长字符串
     * @return
     * @function 判断短字符串中的所有字符是否在长字符串中全部出现
     */
    private boolean boolIsAllCharExist(String pShortString, String pLongString) {

        //遍历短字符串,找出所有的不重复字符
        Set<Character> charsInShortString = new HashSet<>();

        //获取短字符串中的所有字符
        int length = pShortString.length();

        for (int i = 0; i < length; i++) {
            charsInShortString.add(pShortString.charAt(i));
        }

        //遍历长字符串
        for (Character c : charsInShortString) {

            if (!pLongString.contains(c.toString())) {
                return false;
            }

        }

        return true;

    }

}
