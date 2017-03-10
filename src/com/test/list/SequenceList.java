package com.test.list;

import java.util.Arrays;

public class SequenceList<T> {
	private int DEFAULT_SIZE = 4;
	private int size;
	//数组长度
	private int capacity;
	//定义一个数组 保存线性表的元素
	private Object[] data;
	/**
	 *默认长度创建线性表 
	 */
	public SequenceList(){
		this.capacity = DEFAULT_SIZE;
		this.data =  new Object[capacity];
	}
	/***
	 * 初始长度创建线性表
	 */
	public SequenceList(T element){
		this();
		data[0] = element;
		size++;
	}
	/**
	 * 指定长度创建
	 * @param element 指定第一个元素
	 */
	public SequenceList(T element,int size){
		this.capacity = 1 ;
		//把capacity 设置为 2的最小n次方
//		while(capacity<size){
//			capacity<<=1;
//		}
		ensureCapacityInternal(size);
		data = new Object[capacity];
		data[0] = element;
		size++;
	}
	//获取线性表大小
	public int size(){
		return size;
	}
	//获取指定位置处的元素
	@SuppressWarnings("unchecked")
	public T  get(int index){
			check(index);
//			T element = (T) data[i];
			return (T) data[index];
		
	}
	//查找线性表指定元素的索引
	public int indexOf(T element){
		//遍历data
		for(int i=0;i<size;i++){
			if(data[i].equals(element)){
				return i;
			}
		}
		return -1;
	}
	//向线性表添加一个元素
	public void add(T element){
		ensureCapacityInternal(size+1);
		data[size++]=element;
	}
	
	
	//向线性表指定位置处插入一个元素*****
	public void add(int index,T element){
		
			//容量检查
			ensureCapacityInternal(size+1);
			//index 以后依次修改位置
			System.arraycopy(data, index, data, index + 1,
                    size - index);
			data[index]=element;
			size++;
//			for(int i=index;i<=size;i++){
//				data[i]
//			}
	}
	
	//返回元素比较合理,删除指定位置元素
	@SuppressWarnings("unchecked")
	public T delete(int index){
		check(index);
		//位置i 以后的元素调整
		System.arraycopy(data, index, data, index-1, size-index-1);
		//移除最后一个元素
		data[--size]=null;
		
		return (T) data[index];
	}
	//删除指定元素
	public boolean delete(T element){
		//获取位置
		int index = indexOf(element);
		if(index>0){
				
			//位置index 以后的元素调整
			System.arraycopy(data, index+1, data, index, size-index-1);
			//移除最后一个元素
			data[--size]=null;
			return true;
		}
		return false;
	}
	//清空线性表
	public void clear(){
		for(int i=0;i<size;i++){
			data[i]=null;
		}
		size=0;
	}
	public boolean isEmpty(){
		return size==0;
	}
	
	public String toString(){
		if(size==0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<size;i++){
			sb.append( data[i].toString()+",");
		}
		int len = sb.length();
		return sb.delete(len-1, len).append("]").toString();
	}
	//每次删除最后一个元素
	public void delete(){
		delete(size-1);
	}
	//检查位置是否合理
	private void check(int index){
			if(index<0||index>=size){
			System.out.println("数组下标越界");
			throw new IndexOutOfBoundsException("下标越界");
			}
	}
	//检查添加新元素后的容量
	private void ensureCapacityInternal(int size){
		if(size>capacity){
			while(size>capacity){
				capacity<<=1;
			}
			data = Arrays.copyOf(data, capacity);
		}
	}
}














