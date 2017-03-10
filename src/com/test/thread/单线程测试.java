package com.test.thread;

public class 单线程测试  implements Runnable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyThread mt1=new MyThread("线程a");  
		mt1.run();
		mt1.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("呵呵呵");
	}

}
class MyThread extends Thread{  
	private String name;  
	public MyThread(String name) {  
//			super();  
			this.name = name;  
		}  
		public void run(){  
//			for(int i=0;i<10;i++){  
			System.out.println("线程开始："+this.name+",i=");  
//			}  
		}  
}