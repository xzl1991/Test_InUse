package com.test.Map_Set;

public class TestInner1 {
	/**
	 * @param args
	 */
	
	public static int cal(int i){
		int a = i+8;
		return a;
		
	}
	public  class TestIn{
		public void info(){
			System.out.println("TestIn:内部父类");
		}
		public TestIn(){
			System.out.println("TestIn:构造器");
		}
	}
	public TestInner1(){
		
	}
	
}
