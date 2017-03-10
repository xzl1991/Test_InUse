package com.test.TreeSort;


public class HeapSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] data = new int[]{21,30,49,30,21,16,9};//9,79,46,30,58,49};
		System.out.println("排序前:"+java.util.Arrays.toString(data));
		 heapSort(data);
		System.out.println("排序后:"+java.util.Arrays.toString(data));
		 
	}
	//
	public static void heapSort(int[] data){
		System.out.println("开始排序");
		for(int i=0;i<data.length;i++){
			//依次创建堆
			buildMaxHeap(data, data.length-1-i);
			System.out.println("第"+i+"次创建堆:"+java.util.Arrays.toString(data));
			//交换建堆后 第一个和最后一个元素
			swap(data,0, data.length-1-i);
			System.out.println(java.util.Arrays.toString(data));
		}
	}
	
	//对数组从0 到  lastIndex 创建大顶堆   
	private static void buildMaxHeap(int[] data,int lastIndex){
		//每次比较 最后一个节点的 父节点开始
		for(int i=(lastIndex-1)/2;i>=0;i--){
			//保存当前正在判断的节点
			int k = i;
			//判断当前节点子点是否存在
			while(k*2+1<=lastIndex){
				// 如果 k*2+1 < lastIndex. 代表又节点存在
				int biggerIndex = k*2+1 ;
				if(biggerIndex < lastIndex){
					//比较左右节点大小
					//右节点大
					if(data[biggerIndex]<data[biggerIndex+1]){
						biggerIndex ++;
					}
				}
				//比较父节点 和 较大的子节点的值
				if(data[k]<data[biggerIndex]){
					swap(data,k,biggerIndex);

					
					//将 被替换的 位置的 新元素 与其下 的 子元素比较
					k = biggerIndex;
				}
				else{
				break;
				}
//				System.out.println("第"+i+"次移动:"+java.util.Arrays.toString(data));
			}
			
		}
		//判断大小 交换数据
	}
	private static void swap(int[] data, int i, int j) {
		// TODO Auto-generated method stub
		int temp = data[i] ;
		data [i] = data[j];
		data [j] = temp;
	}
}

























