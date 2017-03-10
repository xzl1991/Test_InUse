package com.test.thread;

public class Wolf extends Animal{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Animal animal = new Animal();
		Wolf wo = new Wolf();
		Animal wo1 = new Wolf();
//		Wolf wo2 = (Wolf) new Animal();
		animal.info();
		wo.info();
		wo1.info();
//		wo2.info();
	}
	
	public static void info(){
		System.out.println("子类wolf...");
	}
	
	 void tes(){
		
	}
}

class Animal{
	public static void info(){
		System.out.println("父类Animal...");
	}
	void tes(){
		System.out.println("默认");
	}
}









