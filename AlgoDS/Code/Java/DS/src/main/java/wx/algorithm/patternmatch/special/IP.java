package wx.algorithm.patternmatch.special;

import org.junit.Test;

/**
 * Created by apple on 16/8/13.
 */
public class IP {

    int A_count = 0;//A类地址统计

    int B_count = 0;//B类地址统计

    int C_count = 0;//C类地址统计

    int D_count = 0;//D类地址统计

    int E_count = 0;//E类地址统计

    int private_count = 0;//私有地址统计

    int error_count = 0;//错误地址统计


    /**
     * @param ipAddr
     * @return 返回IP地址是否合法
     * @function 单独检测IP地址是否合法
     * @OJ http://www.nowcoder.com/practice/995b8a548827494699dc38c3e2a54ee9?tpId=37&tqId=21313&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public boolean checkIP(String[] ipAddr) {

        //判断数组长度是否为4
        if (ipAddr.length != 4) {
            return false;
        }

        //检测每一位的IP
        for (int i = 0; i < ipAddr.length; i++) {
            if (ipAddr[i].length() == 0) {
                return false;
            }

            try {
                Integer integer = Integer.valueOf(ipAddr[i]);
                if (integer < 0 || integer > 255) {
                    return false;
                }
            } catch (NumberFormatException exception) {
                return false;
            }


        }

        return true;

    }

    @Test
    public void test_checkIP() {

        String ip = "10.138.15.1";

        System.out.println(checkIP(ip.split("\\.")));

        ip = "10.138..1";

        System.out.println(checkIP(ip.split("\\.")));

        ip = "10.e.15.-2";

        System.out.println(checkIP(ip.split("\\.")));

    }

    public boolean checkMask(String[] maskAddr) {

        return false;
    }

    /**
     * @function 请解析IP地址和对应的掩码，进行分类识别。要求按照A/B/C/D/E类地址归类，不合法的地址和掩码单独归类。
     * @OJ http://www.nowcoder.com/practice/de538edd6f7e4bc3a5689723a7435682?tpId=37&tqId=21241&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
     */
    public static void IPStatistic_OJ() {

    }

    public static void main(String args[]) {

    }

}
