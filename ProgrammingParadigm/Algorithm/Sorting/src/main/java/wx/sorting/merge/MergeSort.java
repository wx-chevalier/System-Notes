package wx.sorting.merge;

/**
 * Created by apple on 16/4/23.
 */

import wx.sorting.quick.QuickSort;

/**
 * @author 王旭
 * @Description:<p>归并排序算法的实现</p>
 * @time 2016-3-4 上午8:14:20
 */
public class MergeSort {

    /**
     * @param arr   待排数组
     * @param left  左指针
     * @param mid   分割指针
     * @param right 右指针
     * @function 对两个数组进行合并
     */
    public static void merge(int[] arr, int left, int mid, int right) {

        //临时数组
        int[] temp = new int[right - left + 1];

        //指向左半部分下标
        int left_i = left;

        //指向右半部分下标
        int right_i = mid + 1;

        //指向临时数组下标
        int k = 0;

        while (left_i <= mid && right_i <= right) {
            if (arr[left_i] < arr[right_i]) {
                //如果左边的较小,则选择左边的
                temp[k++] = arr[left_i++];
            } else {
                temp[k++] = arr[right_i++];
            }
        }

        //已经遍历结束了左半部分或者右半部分

        while (left_i <= mid) {
            temp[k++] = arr[left_i++];
        }

        while (right_i <= right) {
            temp[k++] = arr[right_i++];
        }

        //将临时数组覆盖到arr中
        for (int i = 0; i < temp.length; i++) {
            arr[left + i] = temp[i];
        }

    }

    /**
     * @param arr   待排数组
     * @param left  左指针
     * @param right 右指针
     * @function
     */
    public static void mSort(int[] arr, int left, int right) {

        //如果只有一个数字,则直接返回
        if (arr == null || arr.length == 0 || left >= right) {
            return;
        }

        //获取中间指针
        int mid = (left + right) / 2;

        //对于左半部分进行排序
        mSort(arr, left, mid);

        //对于右半部分进行排序,注意这里是mid+1
        mSort(arr, mid + 1, right);

        //将排序好的左右部分进行合并
        merge(arr, left, mid, right);

    }

    public static void sort(int[] arr) {
        mSort(arr, 0, arr.length - 1);
    }

    public static void main(String args[]) {

        int[] arr = new int[]{3, 9, 7, 8, 5, 11, 32, 2, 4, 99, 56, 213, 136, 4, 7, 43};

        MergeSort.sort(arr);

        for (int a : arr) {
            System.out.print(a + " ");
        }

    }
}