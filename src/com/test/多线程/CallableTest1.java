package com.test.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// 类似实现Runnable，这里实现自己的线程任务，假设返回的结果为String
//class TaskWithResult1 implements Callable<String> {
//    private int id;
// 
//    public TaskWithResult1(int id) {
//        this.id = id;
//    }
// 
//    public String call() throws Exception {
//        return "result of TaskWithResult " + id+"当前线程:"+Thread.currentThread().getName();
//    }
//}
 
public class CallableTest1<T> implements Callable<T>{
	private int id;
    public CallableTest1(int i) {
		// TODO Auto-generated constructor stub
    	this.id = i;
	}
	public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        // 这个线程池可以根据需要实例化不同的线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Callable<String>> tasks = new ArrayList();
        List<Future<String>> results = exec.invokeAll(tasks, 2000, TimeUnit.MILLISECONDS);//new ArrayList<Future<String>>();    //Future 相当于是用来存放Executor执行的结果的一种容器
        for (int i = 0; i < 10; i++) {
            // 这里之所以先将返回的Future放到List中，是避免串行化执行线程；先将任务放入线程池，再一个个调取结果
            results.add(exec.submit(new CallableTest1(i)));
        }
//        Thread.sleep(1);
//       exec.awaitTermination(20, TimeUnit.MILLISECONDS);
        for (Future<String> fs : results) {
        // 这个if判断可以去掉，就会变成阻塞式等等线程结果；不去掉的话，需要更复杂的实现来做多次遍历，以避免漏掉某个线程结果
//            while(!fs.isDone()){
//            	System.out.println("等待..............");
//            	Thread.sleep(1);
//            }
        	
//        	Thread.sleep(5000);
			   while(!fs.isDone()){
	            	System.out.println("等待..............");
	            	int k=0;
	            	Thread.sleep(1);
	            	k++;
	            	if(k==100)
	            		System.out.println("等待.............."+k);
	            		break;
	            }
        	if (fs.isDone()) {
            // 这里的get方法，会将String的结果拿到。
                System.out.println("结束"+fs.get());
            }else{
            	System.out.println("任务未结束:");//+fs.get());
            }
        }
//        System.out.println("所有线程结束？。。。。。"+completed);
        exec.shutdown();
        System.out.println("所有线程结束。。。。。"+exec.isTerminated());
    }
    public T call() throws Exception {
    	if(Thread.currentThread().getName().contains("2")){
    		System.out.println("繁琐的任务。。。");
    		for(int i=0;i<100;i++){
    			
    		}
    		Thread.sleep(300);
    		 System.out.println("任务是。。。"+Thread.currentThread().getName());
    		}else{
    			System.out.println("多线程执行完了"+Thread.currentThread().getName());
    		}
        return (T) ("result of TaskWithResult " + id+"取到的线程:"+Thread.currentThread().getName());
    }
}