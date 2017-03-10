package com.test.多线程;

public class FloatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float f = Float.MAX_VALUE;
		Integer f2 = Integer.MAX_VALUE;
		int f1 =2;
		for(int i=0;i<29;i++){
			f1<<=1;
			System.out.println("左移:"+i+"次:"+f1);
		}
		System.out.println("Int最大值:"+f2+"****"+f1);
		System.out.println("integer比值:"+(f2/f1));
		int a = (int) (f/f1);
		System.out.println(f);
		System.out.println(f1);
		System.out.println("结果:"+a);
		int a1 = (int) (a/f1);
		System.out.println("结果:"+a1);
	}

}
