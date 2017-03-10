package a_可重入锁;

import java.util.concurrent.locks.ReentrantLock;

public class Test implements Runnable {
	ReentrantLock lock = new ReentrantLock();

	public void get() {
		lock.lock();
		System.out.println(Thread.currentThread().getId());
//		lock.wait();
		set();
		lock.unlock();
	}

	public void set() {
		lock.lock();
		System.out.println(Thread.currentThread().getId());
		lock.unlock();
	}

	@Override
	public void run() {
		get();
	}

	public static void main(String[] args) {
		Test ss = new Test();
		new Thread(ss).start();
		new Thread(ss).start();
		new Thread(ss).start();
	}
}
