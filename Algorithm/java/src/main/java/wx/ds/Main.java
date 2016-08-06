package wx.ds;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        //计算料理的总的数目
        int number = 0;

        //存放所有的材料名称
        int[] index = new int[2500];

        int[] index_3670 = new int[3670];


        for (int i = 0; i < 2500; i++) {
            index[i] = 0;
        }

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {//注意while处理多个case

            String str = in.next();
            
            int strIndex = str.hashCode() % 2500;

            int strIndex_3670 = str.hashCode() % 3670;

            if (index[strIndex] == 0 && index_3670[strIndex_3670] == 0) {
                index[strIndex] = 1;
                index_3670[strIndex_3670] = 1;
            }


        }

        //计算总的料理数目
        for (int i = 0; i < 2500; i++) {
            if (index[i] == 1) {
                number++;
            }
        }

        System.out.println(number);
    }
}