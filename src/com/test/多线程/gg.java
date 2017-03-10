package com.test.多线程;

public class gg {
	private static boolean state;
	public static void main(String[] args) throws InterruptedException{
		gg g = new gg();
		Threa s =  g.new Threa();
		 new Thread(s,"窗口1").start();
//		 Thread.sleep(1000);
		 System.out.println("结束....");
		 state = true;
		 System.out.println("结束....1");
		 state = false;
		 state = true;
	}
	class Threa implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!state){
				System.out.println("将将计就计");
			}
			System.out.println("sss");
		}
	}
}