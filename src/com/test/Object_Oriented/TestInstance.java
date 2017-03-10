package com.test.Object_Oriented;

public class TestInstance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object st = "nn";
		Math ma = (Math) st;
		System.out.println(st instanceof String);
		System.out.println("十大"+st instanceof String);
		System.out.println(ma instanceof Math);
		System.out.println(st instanceof Integer);
	}

}
