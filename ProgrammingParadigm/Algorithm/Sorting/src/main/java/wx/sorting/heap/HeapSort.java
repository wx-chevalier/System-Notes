package wx.sorting.heap;

import wx.sorting.quick.QuickSort;

/**
 * Created by apple on 16/4/23.
 */
public class HeapSort {

    /**
     * 堆筛选，除了start之外，start~end均满足大顶堆的定义。
     * 调整之后start~end称为一个大顶堆。
     *
     * @param arr   待调整数组
     * @param start 起始指针
     * @param end   结束指针
     */
    public static void heapAdjust(int[] arr, int start, int end) {

        int temp = arr[start];

        //这里需要重复交换是因为将父节点交换给子节点之后,子节点所处的堆可能就不是大顶堆了,所以需要递归的来调整
        for (int i = 2 * start + 1; i <= end; i *= 2) {

            //左右孩子的节点分别为2*i+1,2*i+2

            //选择出左右孩子较大的下标
            if (i < end && arr[i] < arr[i + 1]) {
                i++;
            }

            if (temp >= arr[i]) {
                break; //已经为大顶堆，=保持稳定性。
            }

            arr[start] = arr[i]; //将子节点上移

            start = i; //下一轮筛选
        }

        arr[start] = temp; //插入正确的位置
    }

    /**
     * @param arr
     * @function 进行堆排序操作
     */
    public static void heapSort(int[] arr) {

        //首先根据输入的序列构造大顶堆,即把值最大的元素移到堆顶

        //从第一个非子节点开始调整
        //这里需要从子节点循环调用的原因是,只有到两个子节点所在的子堆已经是最大堆了,才能保证父节点与其子节点比较后就能保证把全局最大的元素替换上来
        for (int i = arr.length / 2; i >= 0; i--) {

            //这里调整当前非子节点与其子节点
            heapAdjust(arr, i, arr.length - 1);
        }

        System.out.println("大顶堆为:");

        for (int a : arr) {
            System.out.print(a + "  ");
        }

        //不断将堆顶值放入有序区,并且重复对无序区进行大顶堆修正
        for (int i = arr.length - 1; i >= 0; i--) {

            //把值最大的放到最后一个叶子节点处,即放入有序区中
            swap(arr, 0, i);

            //调整无序区的结构,使之成为大顶堆
            heapAdjust(arr, 0, i - 1);

        }

    }

    //交换左右的数
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String args[]) {

        int[] arr = new int[]{3, 9, 7, 8, 5, 11, 32};

        HeapSort.heapSort(arr);

        System.out.println("排序后的数组为:");

        for (int a : arr) {
            System.out.print(a + "  ");
        }

    }

    /**
     *  大顶堆为:
     32  9  11  8  5  3  7
     排序后的数组为:
     3  5  7  8  9  11  32
     */

}