package com.test.多线程;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Test_Automic {  
	   public  static AtomicInteger count = new AtomicInteger(0);  
		private  volatile static int num = 0;
	    public static void inc() {  
	    	count.addAndGet(1);
	        //这里延迟1毫秒，使得结果明显  
	      System.out.println(count);
	    }  
	   
	    public static void main(String[] args) throws InterruptedException {  
	          
//	        final CountDownLatch latch = new CountDownLatch(100);  
	        //同时启动1000个线程，去进行i++计算，看看实际结果  
	        for (int i = 0; i < 20; i++) {  
	            new Thread(new Runnable() {  
	                @Override  
	                public void run() {  
	                	Test_Automic.inc();
	                	System.out.println("num的值:"+(num++));
//	                	   System.out.println(count.getAndIncrement());
//	                    latch.countDown();  
	                }  
	            }).start();  
	        }  
//	        latch.await();  
	        //这里每次运行的值都有可能不同,可能为1000  
	    }  
	}  
