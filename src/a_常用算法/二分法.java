package a_常用算法;

public class 二分法 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] ary = new int[]{1,2,3,4,5,6,7};
		System.out.println(15/2^2);
		System.out.println(7^2);
		System.out.println(7^2);
		System.out.println(15/2*2);
		System.out.println("指定的元素:"+search(ary,3));
	}
	public static int search(int[] ary,int to){
		int low =0;
		int high = ary.length-1;
		while(low<=high){
			int mid = (low+high)/2;
			System.out.println(mid+"....");
			if(ary[mid]==to){
				return mid;
			}else if(ary[mid]<to){
				low = mid+1;
//				mid = (low+high)/2;
			}else{
				high = mid-1;
//				mid = (low+high)/2;
			}
		}
		return -1;
	}
}
