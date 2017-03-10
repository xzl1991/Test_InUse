package com.test.reference;

public interface Subject {
	//增加观察者
	public void add(Observe observe);
	//删除观察者
	public void del(Observe observe);
	//通知所有观察者
	public void notifyObserves();
	//自身行为
	public void operation();
}
