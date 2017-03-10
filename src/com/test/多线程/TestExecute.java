package com.test.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TestExecute  implements Runnable{

	public static Vector<String> cityList = new Vector<String>();
	private ExecutorService executor = Executors.newFixedThreadPool(3);
	public static void main(String args[]){
		TestExecute ts = new TestExecute("tw");
		for(int i=0;i<3;i++){
			ts.run();
		}
	}
	public TestExecute(String city) {
	}
	@Override
	public void run() {
		try {
			if(Thread.currentThread().getName().contains("2")){
				System.out.println("正在执行。。"+Thread.currentThread().getName());
				for(int i=0;i<100000000;i++){
					
				}
				System.out.println("执行结束");
			}else{
				List<String> list = new ArrayList<String>();
						list.add("xg");
						list.add("tw");
						list.add("aomen");
				if(list!=null&&list.size()>0){
					cityList.addAll(list);
				}
				//执行run
				for (String city : cityList) {
					TestExecute task = new TestExecute(city);
					System.out.println("正常:"+Thread.currentThread().getName()+":"+city);
					executor.execute(task);
					executor.shutdown();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}


