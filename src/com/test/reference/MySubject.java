package com.test.reference;

public class MySubject extends AbstractSubject{

	@Override
	public void operation() {
		// TODO Auto-generated method stub
		System.out.println("单独更新..");
		notifyObserves();
	}
	
}
