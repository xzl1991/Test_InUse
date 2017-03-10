package a_不常用类;

import java.util.Scanner;

public class a_大数字计算 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("s"==null);
		String as = "sd";
		t(as);
		Scanner sc = new Scanner(System.in);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		System.out.println(Float.MAX_VALUE);
		System.out.println(Double.MAX_VALUE);
		System.out.println("输入数字:");
		double s = sc.nextDouble();
		if(s%1679==0){
			System.out.println(s+"是");
		}else{
			System.out.println(s+"不是");
		}
	}
	public static void t(String as){
		if(as==null){
			System.out.println("121");
		}else{
			System.out.println("false");
		}
	}
}
