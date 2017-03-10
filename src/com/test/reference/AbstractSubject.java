package com.test.reference;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public abstract class AbstractSubject implements Subject{
	private Vector<Observe> vector = new Vector<Observe>();

	@Override
	public void add(Observe observe) {
		// TODO Auto-generated method stub
		vector.add(observe);
	}

	@Override
	public void del(Observe observe) {
		// TODO Auto-generated method stub
		vector.remove(observe);
	}

	@Override
	public void notifyObserves() {
		// TODO Auto-generated method stub
		Enumeration<Observe> enumo = vector.elements();
		while(enumo.hasMoreElements()){
			enumo.nextElement().update();
		}
		//Iterator<Observe> enumo = vector.iterator();
	}

}
