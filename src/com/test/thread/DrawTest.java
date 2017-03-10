package com.test.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DrawTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadPoolExecutor tpool = new ThreadPoolExecutor(4, 6, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.DiscardOldestPolicy());
		// TODO Auto-generated method stub
		 AccountSyn account = new AccountSyn("123456",1000);
//		 new DrawThread("甲",account,100).start();
//		 new DrawThread("已",account,100).start();
		 for(int i=0;i<3;i++){
			 System.out.println("新任务");
			 if(i/2==0){
				 tpool.execute(new DrawThread("路人甲",account,300));
			 }else{
				 tpool.execute(new DrawThread("路人乙",account,60));
			 }
		 }
		 System.out.println("主线程完成");
		 DrawTest dt = new DrawTest();
		System.out.println( dt.toString());
//		 "name".toString();
	}
	public String toString(){
		return "输出结果.......................";
		
	}
}
class DrawThread implements Runnable{
	private AccountSyn account ;
	private double drawBalance ;
	private String name;
	public DrawThread(String name, AccountSyn account2, double drawBalance) {
//		super(name);
//		System.out.println("..."+Thread.currentThread().setName(name));
		this.name =name ;
//		Thread.currentThread().setName(name);
//		System.out.println("构造器里面:"+Thread.currentThread().getName());
		this.account = account2;
		this.drawBalance = drawBalance;
	}
	public void run(){
		Thread.currentThread().setName(name);
		System.out.println("开始取钱验证....");
		for(int i=0;i<10000000;i++){
			
		}
		System.out.println("验证结束...");
		account.draw(drawBalance);
		System.out.println("run 结束....");
	}
}