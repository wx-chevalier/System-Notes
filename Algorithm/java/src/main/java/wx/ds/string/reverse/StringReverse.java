package wx.ds.string.reverse;

import org.junit.Test;

import java.util.*;

/**
 * Created by apple on 16/8/13.
 */
public class StringReverse {

    /**
     * @param str
     * @param split
     * @function 返回按照分隔符分隔之后的单词的倒序
     */
    public String WordReverse(String str, String split) {

        //分割为字符串
        String[] stringArray = replaceNonEnglishLetterWithBlank(str).split(split);

        //倒序构建
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = stringArray.length - 1; i > -1; i--) {

            //判断字符串是否为空
            String tempStr = stringArray[i].trim();

            if (tempStr.length() > 0) {
                stringBuilder.append(tempStr + " ");
            }
        }

        //返回字符串
        return stringBuilder.toString().trim();

    }

    /**
     * @param str
     * @return
     * @function 将无效字符替换为空格
     */
    public String replaceNonEnglishLetterWithBlank(String str) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) >= 'a' && str.charAt(i) <= 'z') || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
                stringBuilder.append(str.charAt(i));
            }
            else {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();

    }

    /**
     * @function 对字符串中的所有单词进行倒排。
     * @OJ http://www.nowcoder.com/practice/81544a4989df4109b33c2d65037c5836?tpId=37&tqId=21254&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    @Test
    public void WordReverse_OJ() {


        System.out.println(this.WordReverse("$bo*y gi!r#l #", " "));

//        Scanner scanner = new Scanner(System.in);
//
//        while (scanner.hasNext()) {
//
//            String str = scanner.nextLine();
//
//            System.out.println(this.WordReverse(str, " "));
//
//        }
    }


}
