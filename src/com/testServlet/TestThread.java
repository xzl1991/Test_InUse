package com.testServlet;

public class TestThread extends Thread{
	public static void main(String[] args){
		for(int i=0;i<10;i++){
			String name=Thread.currentThread().getName();
			new TestThread().start();
			System.out.println("默认线程:"+name);
			new Thread().start();
			System.out.println("默认线程1:"+name);
		}
	}
}
