package com.test.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolExecutor implements Runnable{
	/**
	 * ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, 
		long keepAliveTime, TimeUnit unit, 
		BlockingQueue<Runnable> workQueue, 
		RejectedExecutionHandler handler) 
		
		corePoolSize： 线程池维护线程的最少数量 
		maximumPoolSize：线程池维护线程的最大数量 
		keepAliveTime： 线程池维护线程所允许的空闲时间 
		unit： 线程池维护线程所允许的空闲时间的单位 
		workQueue： 线程池所使用的缓冲队列 
		handler： 线程池对拒绝任务的处理策略 
		
		
		一个任务通过 execute(Runnable)方法被添加到线程池，任务就是一个 Runnable类型的对象，任务的执行方法就是 Runnable类型对象的run()方法。 
		
		当一个任务通过execute(Runnable)方法欲添加到线程池时： 
		
		如果此时线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。 
		如果此时线程池中的数量等于 corePoolSize，但是缓冲队列 workQueue未满，那么任务被放入缓冲队列。 
		如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。 
		如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过 handler所指定的策略来处理此任务。 
		
		也就是：处理任务的优先级为： 
		核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。 
		
		当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。 
		
		unit可选的参数为java.util.concurrent.TimeUnit中的几个静态属性： 
		NANOSECONDS、MICROSECONDS、MILLISECONDS、SECONDS。 
		
		workQueue我常用的是：java.util.concurrent.ArrayBlockingQueue 
		
		handler有四个选择： 
		ThreadPoolExecutor.AbortPolicy() 
		抛出java.util.concurrent.RejectedExecutionException异常 
		ThreadPoolExecutor.CallerRunsPolicy() 
		重试添加当前的任务，他会自动重复调用execute()方法 
		ThreadPoolExecutor.DiscardOldestPolicy() 
		抛弃旧的任务 
		ThreadPoolExecutor.DiscardPolicy() 
		抛弃当前的任务 
	 * */
	private static int  producerTaskSleepTime = 2;
	private static int  producerTaskMaxNumber = 5;
	public static void main(String[] args){
		// 构造一个线程池  
	ThreadPoolExecutor tpool = new ThreadPoolExecutor(4, 6, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.DiscardOldestPolicy());
		for(int i=0;i<producerTaskMaxNumber;i++){
			try 
			{
				
				//产生新的任务，并加入线程池
				String task = "task@"+i;
				System.out.println("put:"+task+"\n");
				List ls = new ArrayList();
				tpool.execute(new TestThreadPoolExecutor());
				System.out.println("创建");
				 for(int j=0;j<=400000000;j++){
					 if(j==400000000){
						 System.out.println("等待了多久？"+"\n");
					 }
				        }
				        System.out.println("继续 "+name);
				System.out.println(name+"线程：.启动"+i+" "+Thread.currentThread().getName());
//				Thread.sleep(producerTaskSleepTime);
				
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
		}
		tpool.shutdown();
		
		
		
}
	private static String name = null;
	@Override
	public void run() {
		System.out.println("执行线程"+"\n");
		// TODO Auto-generated method stub
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句  
        System.out.println(Thread.currentThread().getName());  
        System.out.println("start .." + Thread.currentThread().getName());  
  
        for (int i = 0; i < 5; i++) {
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
/** 
 * 线程池执行的任务 
 */  
//class ThreadPoolTask implements Runnable,Serializable{
//	private final long serialVersionUID = 0;  
//    private static int consumeTaskSleepTime = 2000;
//    // 保存任务所需要的数据  
//    private Object threadPoolTaskData;  
//	public ThreadPoolTask(Object tasks){
//		this.threadPoolTaskData = tasks;
//	}
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句  
//        System.out.println(Thread.currentThread().getName());  
//        System.out.println("start .." + threadPoolTaskData);  
//  
//        try  
//        {  
//            // //便于观察，等待一段时间  
//            Thread.sleep(consumeTaskSleepTime);  
//        }  
//        catch (Exception e)  
//        {  
//            e.printStackTrace();  
//        }  
//        threadPoolTaskData = null;
//	}
//	public Object task(){
//		return this.threadPoolTaskData;
//	}
//}


















