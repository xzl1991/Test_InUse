package com.test.多线程;

public class TestThreadSafe implements Runnable {
	static int count = 0;

	/**
	 * @param argss
	 */
	public static void main(String[] args) {
		TestThreadSafe a = new TestThreadSafe();
		TestThreadSafe a1 = new TestThreadSafe();
		Thread t1 = new Thread(a,"线程1");
		Thread t2 = new Thread(a1,"线程2");
		t1.start();
		t2.start();

	}

	public void run() {
			try {
				if(Thread.currentThread().getName().contains("线程1")){
					System.out.println("死线程");
					for(int j=0;j<100000000;j++){
						
					}
					// TODO Auto-generated catch block
	        	}else{
			   count++;
				System.out.println(Thread.currentThread());
				Thread.sleep(100);
				System.out.println("线程："+Thread.currentThread().getName());
	        	}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

	}

}