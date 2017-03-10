package a_常用算法;

public class 快速排序 {

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

	public static void quickSort(int[] arr) {
		// TODO Auto-generated method stub
		quickSort(arr, 0, arr.length-1);
	}
	private static void quickSort(int[] arr,int first,int last) {
		// TODO Auto-generated method stub
		if(first<last){
			int pivIndex = partition(arr,first,last);
//			int  pivIndex = getMiddle(arr,first,last);
			quickSort(arr, first, pivIndex-1);
			quickSort(arr,pivIndex+1, last);
		}
	}
	public static int getMiddle(int[] list, int low, int high) {  
		
        int tmp = list[low];    //数组的第一个作为中轴     
        while (low < high) {     
            while (low < high && list[high--] >= tmp)
//            {     
//                high--;     
//            }     
            list[low] = list[high];   //比中轴小的记录移到低端     
            while (low < high && list[low++] <= tmp)
//            {     
//                low++;     
//            }     
            list[high] = list[low];   //比中轴大的记录移到高端     
        }     
       list[low] = tmp;              //中轴记录到尾     
        return low;                   //返回中轴的位置     

    }    

	private static int partition(int[] arr, int low, int high) {
		// TODO Auto-generated method stub
		int i = 0;
		int piv = arr[low];
//		int low = first+1;
//		int high = last;
		while(low<high){
			//从右侧 搜索
			while(low<high&&arr[high]>=piv){
				high--;
			}
//				high -- ;
			arr[low] = arr[high];   //比中轴小的记录移到低端     
			//从左侧 搜索
			while(low<high&&arr[low]<=piv){
				low++;
			}
//				low++;
			arr[high] = arr[low];   //比中轴大的记录移到高端    
			
//			if(high>low){
//				swap(arr, low, high);
//			}	
			System.out.println("low 和 high:"+low +","+high);
		}
//		while(high>first&&arr[high--]>=piv)
////			high--;
//		if(piv>arr[high]){
//			arr[first] = arr[high];
//			arr[high] = piv;
//			return high;
//		}else{
//			return first;
//		}
//		return high;
		 arr[low] = piv;              //中轴记录到尾     
	        System.out.println("排序后:"+(i++)+java.util.Arrays.toString(arr));
         return low;                   //返回中轴的位置  
	}
	private static void swap(int[] arr,int i,int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}































