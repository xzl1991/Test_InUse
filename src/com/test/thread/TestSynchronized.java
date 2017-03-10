package com.test.thread;

public class TestSynchronized {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("执行主方法。。。");
		System.out.println("主方法获取数据:"+TestSynchronized.name);
	}
	static String name = "修改之前";
	static{
		//匿名内部类
		Thread t = new Thread(){
			public void run(){
				System.out.println("进入run方法...");
				System.out.println("run:"+name);
				name = "修改之后";
				System.out.println("run 结束:");
			}
		};
		t.start();
		try {
			//加入t 线程
			Thread.sleep(2000);
//			t.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
