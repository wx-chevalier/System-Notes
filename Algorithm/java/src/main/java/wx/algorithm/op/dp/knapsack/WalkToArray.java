package wx.algorithm.op.dp.knapsack;

/**
 * Created by apple on 16/8/25.
 */

/**
 * @function 本题目是2014年谷歌在线笔试题其中之一
 * @description 给出一个整型数组，里面都是整数，每个数代表从该处开始向后行进的最大步伐数，问题是求出这个数组从第一个元素跳到最后一个元素所选择的最少的数组元素个数
 * 如：
 * input 1,3,5,2,9,3,1,1,8
 * out put 3;
 * <p>
 * path is 1->3->9->8
 * @algorithm 本题也是典型的动态规划类题目, 本题的变量为walk数组的长度, 那我们就建立一个jumps数组, 其中i表示截止到下标i所需要的最短的步数, 然后从第i个节点开始, 更新所有其能达到的点的最短步数
 */
public class WalkToArray {

    public static int jump(int[] walk) {

        //存放如果重点为i,此时所需要的最大的步数
        int[] jumps = new int[walk.length];

        //遍历所有的节点
        for (int i = 0; i < walk.length; i++) {

            //存放截止到该节点所需要的步数
            int maxJumpsRequired;

            //对于第1个节点,最大步数为1
            if (i == 0) {
                maxJumpsRequired = 1;
            } else {

                //否则设置为当前步数加1
                maxJumpsRequired = jumps[i] + 1;
            }
            for (int j = walk.length > i + walk[i] ? i + walk[i] : walk.length - 1; j > i; j--) {

                //遍历本步之后的所有步骤,即在本步骤所能达到的范围内,其最小值
                if (jumps[j] == 0 || jumps[j] > maxJumpsRequired) {
                    jumps[j] = maxJumpsRequired;
                } else {
                    break;
                }
            }
        }
        return jumps[jumps.length - 1];
    }
}


