package a_常用算法;

public class 快速排序3 {  
	  
//	  int a[]={2,8,-6,71,12,52,8,-3};  
	  
	public 快速排序3(int a[]){  
	  
	    quick(a);  
	  
	    for(int i=0;i<a.length;i++)  
	  
	       System.out.print(a[i]+" ");  
	  
	}  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = new int[]{2,8,-6,71,12,52,8,-3};
		System.out.println("原来元素:"+java.util.Arrays.toString(arr));
		 new 快速排序3(arr);
		System.out.println("排序后:"+java.util.Arrays.toString(arr));
	}
	public int getMiddle(int[] list, int low, int high) {     
	  
	            int tmp = list[low];    //数组的第一个作为中轴     
	  
	            while (low < high) {     
	  
	                while (low < high && list[high] >= tmp) {     
	  
	                    high--;     
	  
	                }     
	  
	                list[low] = list[high];   //比中轴小的记录移到低端     
	  
	                while (low < high && list[low] <= tmp) {     
	  
	                    low++;     
	  
	                }     
	  
	                list[high] = list[low];   //比中轴大的记录移到高端     
	  
	            }     
	  
	           list[low] = tmp;              //中轴记录到尾     
	  
	            return low;                   //返回中轴的位置     
	  
	        }    
	  
	public void _quickSort(int[] list, int low, int high) {     
	  
	            if (low < high) {     
	  
	               int middle = getMiddle(list, low, high);  //将list数组进行一分为二     
	  
	                _quickSort(list, low, middle - 1);        //对低字表进行递归排序     
	  
	               _quickSort(list, middle + 1, high);       //对高字表进行递归排序     
	  
	            }     
	  
	        }   
	  
	public void quick(int[] a2) {     
	  
	            if (a2.length > 0) {    //查看数组是否为空     
	  
	                _quickSort(a2, 0, a2.length - 1);     
	  
	        }     
	  
	       }   
	  
	}  
