package com.test.多线程;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestPool implements Runnable {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 ExecutorService executor = Executors.newFixedThreadPool(5);
		for(int i=0;i<4;i++){
//			TestRunnable c=new TestRunnable(); 
			executor.execute(new TestPool());
			 for(int j=0;j<4;j++){
			        System.out.println("继续还是执行线程？"+j+name);
			        }
			        System.out.println("继续"+name);
			System.out.println(name+"线程：.启动"+i+" "+Thread.currentThread().getName());
		}
		executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务  
	}
	private static String name = null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 15; i++) {
			System.out.println(Thread.currentThread().getName());
	    	if(Thread.currentThread().getName().contains("2")){
				System.out.print("死线程："+Thread.currentThread().getName()+"等待");
				for(int j=0;j<1000000001;j++){
					if(j==1000000000){
						name = "带2的线程";
						System.out.println(name+"结束。。");
					}
				}
				// TODO Auto-generated catch block
			}else{
				name ="不带2的线程"+i;
				System.out.println( "运行： " +i+" "+name+"\n");
				System.out.println(Thread.currentThread().getName());
			}
	    	}
	    } 
	}
	
class TestRunnable implements Runnable { 
    public void run() { 
    	for (int i = 0; i < 15; i++) {
    	if(Thread.currentThread().getName().contains("2")){
			System.out.print("死线程"+Thread.currentThread().getName()+"等待");
			for(int j=0;j<1000000001;j++){
				if(j==1000000000){
					System.out.println("结束了");
				}
			}
			// TODO Auto-generated catch block
		}else{
			System.out.print( "运行 " +i+" ");
			System.out.println(Thread.currentThread().getName());
		}
    	}
    } 
}
