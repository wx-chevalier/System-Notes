package wx.algorithm.sorting.quick;

import java.util.Arrays;

/**
 * @function 快速排序算法实现
 */
public class QuickSort {

    /**
     * 划分
     *
     * @param arr
     * @param left
     * @param right
     * @return
     */
    public static int partition(int[] arr, int left, int right) {

        //选定左侧的为标杆
        int pivotKey = arr[left];


        while (left < right) {

            //从右侧开始,寻找到第一个小于标杆的数
            while (left < right && arr[right] >= pivotKey)
                right--;

            //把小的移动到左边
            arr[left] = arr[right];

            //从左侧开始,寻找到第一个大于标杆的数
            while (left < right && arr[left] <= pivotKey)
                left++;

            //把大的移动到右边
            arr[right] = arr[left];
        }

        //到这里left和right必然重合
        assert left == right;

        //最后把pivot赋值到中间
        arr[left] = pivotKey;

        return left;
    }

    /**
     * 递归划分子序列
     *
     * @param arr
     * @param left
     * @param right
     */
    public static void quickSort(int[] arr, int left, int right) {

        //判断是否只剩下最后一个数了
        if (left >= right)
            return;

        //如果还有多个数,则先进行划分,划分的结果即是左侧肯定比右侧小
        int pivotPos = partition(arr, left, right);

        //对于左侧进行排序
        quickSort(arr, left, pivotPos - 1);

        //对于右侧进行排序
        quickSort(arr, pivotPos + 1, right);

    }

    public static void sort(int[] arr) {

        //判断字符串是否为空或者长度是否为0
        if (arr == null || arr.length == 0)
            return;

        //进行全部的快速排序
        quickSort(arr, 0, arr.length - 1);
    }

    public static void main(String args[]) {

        int[] arr = new int[]{3, 9, 7, 8, 5, 11, 32};

        QuickSort.sort(arr);

        for (int a : arr) {
            System.out.println(a);
        }

    }

}