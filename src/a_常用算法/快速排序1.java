package a_常用算法;

public class 快速排序1 {

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
	public static void quickSort(int[] ary){
		sort(ary, 0, ary.length-1);
	}
	private static void sort(int[] ary,int start,int end){
		int i = 0;
		int j = end+1;
		if(start<end){
			//先从左面找到 大于 分界值的 索引，
			while(true){
				while(i<end&&ary[++i]<=ary[start]);
				//从右面找到 小于 分界值的 元素，
				while(j>start&&ary[--j]>=ary[start]);
				if(i<j){
						swap(ary,i,j);
						System.out.println("排序后1:"+java.util.Arrays.toString(ary));
				}
				else{
					break;
				}
			}
			swap(ary,start,j);
			sort(ary, start,j-1);
			sort(ary, j+1, end);
		}
	}
	private static void swap(int[] arr,int i,int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
