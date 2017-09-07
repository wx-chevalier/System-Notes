package wx.ds.indexed.stackqueue.stack;

/**
 * Created by apple on 16/8/13.
 */

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @function 火车进站问题 给定一个正整数N代表火车数量，0<N<10，接下来输入火车入站的序列，一共N辆火车，每辆火车以数字1-9编号。要求以字典序排序输出火车出站的序列号。
 * @OJ http://www.nowcoder.com/practice/97ba57c35e9f4749826dc3befaeae109?tpId=37&tqId=21300&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class Railway {

    //存放所有的出站信息以进行排序
    TreeSet<String> outOrderList = new TreeSet<String>();

    /**
     * @param candidateStack
     * @param inStack
     * @param outQueue
     * @function 迭代处理所有可能的情况
     */
    public void handle(Stack<Integer> candidateStack, Stack<Integer> inStack, LinkedBlockingQueue<Integer> outQueue) {

        //如果全部处理完毕,所有火车均已出站,则打印出站信息
        if (candidateStack.size() == 0 && inStack.size() == 0) {

            Queue<Integer> outQueueCopy = new LinkedBlockingQueue<>(outQueue);

            StringBuilder stringBuilder = new StringBuilder();

            while (outQueueCopy.size() > 0) {

                stringBuilder.append(outQueueCopy.poll() + " ");

            }

            outOrderList.add(stringBuilder.toString().trim());

        }

        //处理火车进站
        if (candidateStack.size() > 0) {

            //从待处理车辆内选择一辆进站
            inStack.push(candidateStack.pop());

            //进入下一轮迭代
            handle(candidateStack, inStack, outQueue);

            //回溯修正状态
            candidateStack.push(inStack.pop());
        }

        //处理火车出站
        if (inStack.size() > 0) {

            //保存弹出的数据
            Integer train = inStack.pop();

            //从进站车辆中选择一辆出站
            outQueue.add(train);

            //进入下一轮迭代
            handle(candidateStack, inStack, outQueue);

            //回溯修正状态
            inStack.push(train);

            outQueue.remove(train);

        }

    }

    public void display() {

        //将outOrderList按照字典序排序

        for (String str : outOrderList) {
            System.out.println(str);
        }
    }

    public static void main(String args[]) {

        Railway railway = new Railway();

        //进站顺序
//        int[] inOrder = new int[]{1, 2, 3};

        int[] inOrder;

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            int n = scanner.nextInt();

            inOrder = new int[n];

            for (int i = 0; i < n; i++) {
                inOrder[i] = scanner.nextInt();
            }

            //构造Candidate Stack
            Stack<Integer> candidateStack = new Stack<>();

            for (int i = inOrder.length - 1; i > -1; i--) {
                candidateStack.push(inOrder[i]);
            }

            railway.handle(candidateStack, new Stack<>(), new LinkedBlockingQueue<>());

            railway.display();
        }



    }
}
