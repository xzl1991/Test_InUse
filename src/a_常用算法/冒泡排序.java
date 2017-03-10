package a_常用算法;

public class 冒泡排序 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object[] ary = new Object[]{"2012","1010","2014","2015","2000","1997","1558","2016"};
		sort(ary);
		System.out.println("2012".compareTo("1010"));
		for(int i=0;i<ary.length;i++){
			System.out.print(ary[i]+"..");
		}
		int[] ar = new int[]{2,1,5,6,3,9,8};
		sort(ar);
		for(int i=0;i<ar.length;i++){
			System.out.print(ar[i]+"..");
		}
	}
	public static void sort(Object[] obj){
		int count  = 0;
//		for(int i = 1; i<obj.length ; i++){   //方法1
//			for(int j = 0; j < obj.length; j++){
		for(int i = 0; i<obj.length ; i++){
			for(int j = 0; j < obj.length-1-i; j++){		
//		  for (int i = obj.length - 1; i > 0; --i){//速递快   方法2
//	        for (int j = 0; j < i; ++j){
	        	++count;
	        	if(((String) obj[i]).compareTo((String) obj[j])<0){
					//i>jl
					swap(obj, i, j);
				}
			}
		}
		System.out.println("总共执行："+count+" 次");
	}
	public static void sort(int[] obj){
		int count  = 0;
		for(int i = 1; i<obj.length ; i++){
			for(int j = 0; j < obj.length; j++){
	        	++count;
	        	if(obj[i]>obj[j]){
					//i>jl
					swap(obj, i, j);
				}
			}
		}
		System.out.println("总共执行："+count+" 次");
	}
	private static void swap(Object[] ary,int i,int j){
		Object temp = ary[i];
		ary[i] = ary[j];
		ary[j] = temp;
	}
	private static void swap(int[] ary,int i,int j){
		int temp = ary[i];
		ary[i] = ary[j];
		ary[j] = temp;
	}
}




















