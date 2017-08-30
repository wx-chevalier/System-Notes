package wx;

import java.util.*;

/**
 * Created by apple on 16/8/12.
 */
public class Main {

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            //读入数据行数
            int n = in.nextInt();

            int[][] pairs = new int[n][2];

            //初始化一个TreeMap
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();

            //读入n行数据
            for (int i = 0; i < n; i++) {

                pairs[i][0] = in.nextInt();

                pairs[i][1] = in.nextInt();

                //判断是否在Map中存在
                if (treeMap.containsKey(pairs[i][0])) {

                    treeMap.put(pairs[i][0], pairs[i][1] + treeMap.get(pairs[i][0]));

                } else {
                    treeMap.put(pairs[i][0], pairs[i][1]);
                }


            }
            for (Map.Entry entry : treeMap.entrySet()) {
                System.out.print(entry.getKey() + " ");
                System.out.println(entry.getValue());
            }
        }

    }

    /**
     * @function 读取外部输入数据
     */
    public void readInfinite() {

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {//注意while处理多个case

            break;

        }

    }

    /**
     * @function 读取指定行数的数据
     */
    public void readFixed() {

        Scanner in = new Scanner(System.in);

        //获取行数
        int n = in.nextInt();

        //临时循环变量
        int i, j;

        //获取所有变量,注意,只能按行获取
        for (i = 0; i < n; i++) {

            String str = in.next();
        }
    }
}