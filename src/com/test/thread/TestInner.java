package com.test.thread;

import com.test.thread.TestInner.TestIn;


 public class TestInner {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		
	}
	public  int cal(int i){
		int a = i+8;
		return a;
		
	}
	public static class TestIn{
		public void info(){
			System.out.println("TestIn:内部父类");
		}
	}
	
}













