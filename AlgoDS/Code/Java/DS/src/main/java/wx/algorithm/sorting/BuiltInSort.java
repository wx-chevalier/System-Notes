package wx.algorithm.sorting;

/**
 * Created by apple on 16/8/13.
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @function 测试Java内建的排序
 */
public class BuiltInSort {

    //测试的字符串
    String[] stringArray = new String[]{
            "ss",
            "sdsadsa",
            "1",
            "219dsai"
    };

    /**
     * @function 对字符串数组进行排序
     */
    @Test
    public void stringSortInArray() {

        Arrays.sort(stringArray);

        //按照字典顺序输出
        Arrays.asList(stringArray).forEach(s -> {
            System.out.println(s);
        });

        Arrays.sort(stringArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() > o2.length() ? 1 : -1;
            }
        });

        //按照大小输出
        Arrays.asList(stringArray).forEach(s -> {
            System.out.println(s);
        });

    }

    /**
     * @function 对于数组中的整数去重排序并且输出
     * @OJ http://www.nowcoder.com/practice/3245215fffb84b7b81285493eae92ff0?tpId=37&tqId=21226&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public void integerSortInArray(int[] integers) {

        TreeSet<Integer> treeSet = new TreeSet<>();

        for (int i = 0; i < integers.length; i++) {
            treeSet.add(integers[i]);
        }

        treeSet.forEach(integer -> {
            System.out.println(integer);
        });

    }

    public static void main(String args[]) {

        BuiltInSort builtInSort = new BuiltInSort();

        builtInSort.integerSortInArray(new int[]{
                10,
                20,
                40,
                32,
                67,
                40,
                20,
                89,
                300,
                400,
                15
        });
    }
}
