package com.test.TreeSort;

public class ShellSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = new int[]{9,-16,21,23,-30,-49,21,30,30,2,8,-6,71,12,52,1,-3};
		System.out.println("原来元素:"+java.util.Arrays.toString(arr));
		shellSort(arr);
		System.out.println("排序后:"+java.util.Arrays.toString(arr));
	}

	private static void shellSort(int[] arr) {
		// TODO Auto-generated method stub
		 int h = 1;
		 int arrLength = arr.length;
		 //按 h*3+1 得到增量序列的最大值
		 while(h<=arrLength/3){
			 h = h*3+1;
		 }
		 while(h>0){
			 System.out.println("**h的值*:"+h);
			 //开始排序 插入排序方法 正序
			 for(int i=h;i<arrLength;i++){
				 int temp = arr[i];
				 //如果后一个元素比前一个小 交换位置
				 if(arr[i]<arr[i-h]){
					 int j = i-h;
					 //整体后移h
					 for(;j>=0&&arr[j]>temp;j-=h){
						  arr[j+h] = arr[j];
					 }
					 //最后将 temp 放在正确位置
					 arr[j+h] = temp;
				 }
				 System.out.println(""+java.util.Arrays.toString(arr));
			 }
			 h = (h-1)/3;
		 }
	}

}























