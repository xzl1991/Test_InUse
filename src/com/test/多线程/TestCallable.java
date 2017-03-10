package com.test.多线程;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.sun.jmx.snmp.tasks.Task;

public class TestCallable implements Callable<String>{
	 private static String[] outputs = {"",""};
	 private static int i;
	// 一个有7个作业线程的线程池，老大的老大找到一个管7个人的小团队的老大
	public static void main(String args[]){
    ExecutorService laodaA = Executors.newFixedThreadPool(7);
    Future<String> names = null ;
    FutureTask tsk = null;
		 //提交作业给老大，作业内容封装在Callable中，约定好了输出的类型是String。
			try {
				for( i=0;i<10;i++){
					System.out.println(i);
				
			    names =	laodaA.submit(new TestCallable());
				outputs = laodaA.submit(
				         new Callable<String[]>() {
				             public String[] call() throws Exception 
				             {		outputs[0]= "姓名:"+i;
				             		outputs[1]="年龄:"+i;
				                 return outputs;
				             }
//				             提交后就等着结果吧，到底是手下7个作业中谁领到任务了，老大是不关心的。
				         }).get();
				if(!Thread.currentThread().getName().contains("main")){
					System.out.println(outputs[0]+".."+outputs[1]+"线程是："+Thread.currentThread().getName());
				}else{
					for(int j=0;j<9;j++){}
					System.out.println(outputs[0]+".."+outputs[1]+"线程是："+"主线程");
				}
				}
				laodaA.shutdown();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				try {
					//获取结果
					System.out.println(names.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return "空间和快乐";
	}

}
