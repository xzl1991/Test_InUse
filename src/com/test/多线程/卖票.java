package com.test.多线程;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class 卖票 implements Runnable {

	/**
	 * @param args
	 */
    private final AtomicInteger count = new AtomicInteger(0); 
    private static ThreadLocal<Integer> account = new ThreadLocal<Integer>();
	private  volatile static int num = 20;
	public static void main(String[] args) {
//		卖票 threadfordemo = new 卖票();  
//	        Thread th1 = new Thread(threadfordemo,"窗口1");  
//	        Thread th2 = new Thread(threadfordemo,"窗口2");  
//	        Thread th3 = new Thread(threadfordemo,"窗口3");  
//	        Thread th4 = new Thread(threadfordemo,"窗口4");  
//	        th1.start();  
//	        th2.start();  
//	        th3.start();  
//	        th4.start();  
		
		
		// TODO Auto-generated method stub  线程池...
		ExecutorService executor = Executors.newFixedThreadPool(4);
		boolean flag = true;
		while(flag){
			    if(num>0){  
			    	executor.execute(new 卖票());
                }else{  
                        System.out.println("票卖完了。。。");  
                        flag=false;  
                        executor.shutdown();
                }  
		}
	}

	@Override
	public  void run() {
		// TODO Auto-generated method stub
		while(num>0){
			 synchronized(""){
				 if(num>0){
					 System.out.println(count.addAndGet(1)+"当前窗口"+Thread.currentThread().getName()+"--卖出去一张票,还有:"+--num);
				 }else{
					 System.out.println("卖完了...");
				 }
			 }
		}
	}
}
