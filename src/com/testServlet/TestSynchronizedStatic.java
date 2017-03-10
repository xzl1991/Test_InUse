package com.testServlet;

public class TestSynchronizedStatic implements Runnable{

	/**
	 * @param args
	 */
	static boolean staicFlag ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestSynchronizedStatic ts = new TestSynchronizedStatic();
		new Thread(ts).start();
		new Thread(ts).start();
	}
	
	public static synchronized void test(){
		//静态用类控制
		for(int i=0;i<100;i++){
			System.out.println("test方法:"+Thread.currentThread().getName()+" "+i);
		}
	}
	
	public  void test1(){
		//非静态用 this控制
		synchronized (this) {
			for(int i=0;i<100;i++){
				System.out.println("test1方法:"+Thread.currentThread().getName()+" "+i);
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(staicFlag){
			staicFlag = false ;
			test();
			System.out.println(staicFlag);
		}else{
			staicFlag = true;
			test1();
			System.out.println(staicFlag);
		}
	}

}
