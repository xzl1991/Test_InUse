package com.test.overflow;


public class SynchronizedTest implements Runnable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			//SynchronizedTest(1,2);
			new Thread(new SynchronizedTest(1, 2)).start();
			new Thread(new SynchronizedTest(2, 1)).start();
			System.out.println(i);
		}
	}
	int a;int b;
	public SynchronizedTest(int a,int b){
		this.a =a;
		this.b =b;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (Integer.valueOf(a)) {
			synchronized (Integer.valueOf(b)) {
				System.out.println("result:"+(a+b));
			}
		}
	}
}



















