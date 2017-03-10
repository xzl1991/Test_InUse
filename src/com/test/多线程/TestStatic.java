package com.test.多线程;

public class TestStatic {

	/**
	 * @param args
	 */
	public static int num = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestStatic t = new TestStatic();
		t.cal(3);
		t.cal1(3);
	}
	public void cal(int a){
		for(int i=0;i<a;i++){
			num++;
			System.out.println("num:"+num);
		}
		num =10;
	}
	public void cal1(int a){
		for(int i=0;i<a;i++){
			num++;
			System.out.println("num:"+num);
		}
	}
}
