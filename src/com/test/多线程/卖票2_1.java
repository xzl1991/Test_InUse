package com.test.多线程;

class MyThread1 extends Thread {
	private int ticket = 10;

	public void run() {
		for (int i = 0; i < 1; i++) {
			if (this.ticket > 0) {
				System.out.println("卖票：ticket" + this.ticket--);
			}
		}
	}
};

public class 卖票2_1 {
	public static void main(String[] args) {
		MyThread1 mt1 = new MyThread1();
		MyThread1 mt2 = new MyThread1();
		MyThread1 mt3 = new MyThread1();
		mt1.start();// 每个线程都各卖了10张，共卖了30张票
		mt1.run();
		mt2.start();// 但实际只有10张票，每个线程都卖自己的票
		mt3.start();// 没有达到资源共享
	}
}