package wx.algorithm.op.backtracking;

/**
 * Created by apple on 16/9/7.
 */

import java.util.*;

/**
 * @function 如果袋子里面的球的号码是{1, 1, 2, 3}，这个袋子就是幸运的，因为1 + 1 + 2 + 3 > 1 * 1 * 2 * 3
 * @OJ http://www.nowcoder.com/question/next?pid=2252291&qid=45838&tid=4850436
 */
public class LuckyNumber {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < n; i++)
                nums[i] = scanner.nextInt();

            Arrays.sort(nums);

            ArrayList<Integer> arrayList;

            System.out.println(find(nums, 0, 0, 1));
        }
    }

    private static int find(int[] nums, int index, long sum, long multi) {
        int count = 0;
        for (int i = index; i < nums.length; i++) {
            sum += nums[i];
            multi *= nums[i];
            if (sum > multi)
                count += 1 + find(nums, i + 1, sum, multi);
            else if (nums[i] == 1)
                count += find(nums, i + 1, sum, multi);
            else
                break;
            sum -= nums[i];
            multi /= nums[i];
            while (i < nums.length - 1 && nums[i] == nums[i + 1])
                i++;
        }
        return count;
    }
}
