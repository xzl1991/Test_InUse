package com.test.TreeSort;

public class QuickSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = new int[]{2,8,-6,71,12,52,8,-3};
		System.out.println("原来元素:"+java.util.Arrays.toString(arr));
		quickSort(arr);
		System.out.println("排序后:"+java.util.Arrays.toString(arr));
		
	}
	public static void quickSort(int[] arr){
		subSort( arr,0,arr.length-1);
	};
	private static void subSort(int[] arr,int begin,int end){
		int i = 0;
		int j = end+1;
		//需要排序
		if(begin<end){
			//先从左面找到 大于 分界值的 索引，
			while(true){
				while(i<end&&arr[++i]<=arr[begin]);
				//从右面找到 小于 分界值的 元素，
				System.out.println(arr[i]+".........."+arr[begin]);
				while(j>begin&&arr[--j]>=arr[begin]);
//				while(i<end&&(String.valueOf(arr[++i]).compareTo(String.valueOf(arr[begin]))<=0));
				//从右面找到 小于 分界值的 元素，
//				while(j>begin&&(String.valueOf(arr[--j]).compareTo(String.valueOf(arr[begin]))>=0));
				System.out.println(arr[j]+"....ss......"+arr[begin]);
				if(i<j){
					swap(arr,i,j);
					System.out.println("排序后1:"+java.util.Arrays.toString(arr));
				}
				else{
					break;
				}
//				//i>j 进行下次循环
//				if(i>j){
//					swap(arr,i,j);
//					subSort(arr, begin, end);
//					System.out.println("排序后2:"+java.util.Arrays.toString(arr));
//				}
			}
			//
			swap(arr,begin,j);
			System.out.println("排序后2:"+java.util.Arrays.toString(arr));
			//递归左子序列
			subSort(arr,begin,j-1);
			System.out.println("排序后3:"+java.util.Arrays.toString(arr));
			//递归右子序列
			subSort(arr,j+1,end);
			System.out.println("排序后4:"+java.util.Arrays.toString(arr));
		}
	}
	private static void swap(int[] arr,int i,int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
}
