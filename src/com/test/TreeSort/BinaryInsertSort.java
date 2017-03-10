package com.test.TreeSort;

public class BinaryInsertSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = new int[]{2,8,-6,71,12,52,1,-3};
		System.out.println("原来元素:"+java.util.Arrays.toString(arr));
		binaryInsertSort(arr);
		System.out.println("排序后:"+java.util.Arrays.toString(arr));
	}

	private static void binaryInsertSort(int[] arr) {
		// TODO Auto-generated method stub
		int length = arr.length;
		for(int i=1;i<length;i++){
			int temp = arr[i];
//			if(arr[i]<arr[i-1]){
//				arr[i] = arr[i-1];
//			}
//			arr[i-1] = temp;
			//前后交换位置以后 ，要和 之前的 元素比较
			int low = 0;
			int height = i-1;
			while(low<= height){
				int mid = (low+height)/2;
				if(arr[mid]>temp){
					low = mid+1;
				}else{
					height = mid -1;
				}
			}
			//将 low 到 i 处所有元素 往后 移一位
			for(int j = i;j>low;j--){
				arr[j] = arr[j-1];
			}
			arr[low] = temp;
			//中间位置比 i 处大
//			int mid = (low+height)/2;
//			while(mid!=0){
//				if(arr[mid]>arr[i]){
//					mid = (low+mid)/2;
//				}else{
//					mid = (height+mid)/2;
//				}
//			}
//			arr[mid] = temp;
		}
	}

}
























