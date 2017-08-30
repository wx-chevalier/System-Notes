package wx.algorithm;

import java.util.Scanner;

/**
 * Created by apple on 16/9/11.
 */
public class Miane {


    public static int number(int leftMoney) {

        if (leftMoney < 5) {
            //小于5块 只有一种
            return 1;
        }

        if (leftMoney >= 5 && leftMoney < 10) {
            return number(leftMoney - 1) + number(leftMoney - 5);
        }

        if (leftMoney >= 10 && leftMoney < 20) {
            return number(leftMoney - 1) + number(leftMoney - 5) + number(leftMoney - 10);
        }

        if (leftMoney >= 20 && leftMoney < 50) {
            return number(leftMoney - 1) + number(leftMoney - 5) + number(leftMoney - 10) + number(leftMoney - 20);
        }

        if (leftMoney >= 50 && leftMoney < 100) {
            return number(leftMoney - 1) + number(leftMoney - 5) + number(leftMoney - 10) + number(leftMoney - 20) + number(leftMoney - 50);
        }

        if (leftMoney >= 100) {
            return number(leftMoney - 1) + number(leftMoney - 5) + number(leftMoney - 10) + number(leftMoney - 20) + number(leftMoney - 50) + number(leftMoney - 100);
        }

        return 0;
    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {

            int money = in.nextInt();

            if (money <= 0) {
                System.out.println(0);
            } else {
                System.out.println(number(money));
            }

        }
    }
}
