package com.test.Map_Set;

import java.util.Arrays;

public class Sequence_Stack<T> {

	/**
	 * @author user
	 * @param args
	 */
	//数组默认长度
	private int DEFAULT_SIZE = 10;
	private int capacity;//定义数组容量
	private int size;//大小
	private T element;//元素
	private Object[] data;//保存元素的数组
	//以默认数组长度创建 空顺序栈
	public Sequence_Stack(){
		capacity = DEFAULT_SIZE;
		data = new Object[capacity];
	}
	//以一个指定元素初始化顺序栈
	public Sequence_Stack(T element){
		this();//调用 无参构造器
		this.element = element;
		size++;
	}
	/***
	 * 以指定长度创建顺序栈
	 */
	public Sequence_Stack(T element,int i){
		this.capacity = i;
		data = new Object[capacity];
//		this.element = element;
		data[0] = element;
		size++;
	}
	//压栈 存入数据
	public void push(T element){
		//先判断 压栈后 size 是否大于 容量
		ensureCapacity(size+1);
		data[size++] = element;
	}
	private void ensureCapacity(int size) {
		// TODO Auto-generated method stub
		if(size>capacity){
			//扩大一倍
			capacity<<=1;
			data = Arrays.copyOf(data, capacity);
		}
	}
	
	//出栈
	public T pop(){
		//return (T) data[size--]; 要对最后一个元素置空
		T value = (T) data[size-1];
		data[--size] =  null;
		return value;
	}
	//返回栈顶元素
	public T peek(){
		return (T) data[size-1];
	}
	//获取栈大小
	public int size(){
		return size;
	}
	//判断是否为空
	public boolean isEmpty(){
		return size==0;
	}
	
	//清空
	public void clear(){
		Arrays.fill(data, null);
		size=0;
	}
	public String toString(){
		if(size==0){
			return "[]";
		}else{
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			//遍历数组
			for(int i=size-1;i>=0;i--){
				sb.append(data[i]+",");
			}
			int len = data.length;
			sb.delete(len-1, len).append("]").toString();
			return sb.toString();
		}
		
	}
}















