/**
 * 快速排序
 * @param {*} arr 待排序数组
 * @returns 纯函数，排序后的数组
 */
function quickSort(arr) {
  // 如果数组长度小于等于 1 则返回
  if (arr.length <= 1) {
    return arr;
  }

  let letfArr = [],
    rightArr = [],
    pivot = arr[0];

  // 遍历剩余的内容，进行排序
  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < pivot) {
      letfArr.push(arr[i]);
    } else {
      rightArr.push(arr[i]);
    }
  }
  return [...quickSort(letfArr), pivot, ...quickSort(rightArr)];
}

console.log(quickSort([4, 5, 3, 1, 2]));
