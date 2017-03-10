package com.test.reference;

public class ObserveTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Subject sub = new MySubject();
		sub.add(new Observe1());
		sub.add(new Observe2());
		System.out.println("...dd");
		sub.operation();
	}

}
