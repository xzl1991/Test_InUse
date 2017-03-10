package com.test.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class 多线程模板<T> implements Callable<T>{

	/**
	 * @param args
	 */
	private int id;
	private  static ExecutorService pool = Executors.newCachedThreadPool();
	public 多线程模板(int i) {
		this.id = i ;
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		List<Future<String[]>> results = new ArrayList<Future<String[]>>(); 
		for(int i=0;i<10;i++){
			results.add(pool.submit(new 多线程模板(i)));
		}
		for (Future<String[]> fs : results) {
			int k=0;
			 while(!fs.isDone()){
		         	System.out.println("等待.............."+k);
		         	Thread.sleep(1);
		         	k++;
		         	if(k==10){
		         		System.out.println("不等了.............."+k);
		         		break;
		         		}
		         }
			 if (fs.isDone()) {
		            // 这里的get方法，会将String的结果拿到。
		                System.out.println("结束"+fs.get());
		            }else{
		            	System.out.println("任务未结束:");//+fs.get());
		            }
		}
		
		  
		// TODO Auto-generated method stub
//		String[] widthAndh = {"这个","是","",""};
//		String[] widthAndHeigth = {"","","",""};
//		widthAndHeigth = widthAndh ;
//			widthAndHeigth[2]= Integer.toString(3);
//			widthAndHeigth[3]= "四级联考";
//			String s = "null";
//			s.isEmpty();
//			System.out.println("是空？"+s.isEmpty());
//		for(int j=0;j<4;j++){
//			
//			System.out.println(widthAndHeigth[j]);
//		}
	}
	@Override
	public T call() throws Exception {
		if(Thread.currentThread().getName().contains("2")){
    		System.out.print("繁琐的任务。。。");
    		for(int i=0;i<100;i++){
    			
    		}
    		Thread.sleep(300000);
    		 System.out.println("任务是。。。"+Thread.currentThread().getName());
    		}
//		Thread.sleep(200);
        return (T) ("result of TaskWithResult " + id+"当前线程:"+Thread.currentThread().getName());
	}

}
