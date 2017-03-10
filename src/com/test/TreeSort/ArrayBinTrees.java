package com.test.TreeSort;


public class ArrayBinTrees<T> {
	//用数组保存节点
	private Object[] datas;
	private int DEFAULT_SIZE=8;
	//保存树的深度
	private int deep;
	private int arraySize;
	//以默认形式 保存二叉树
	public ArrayBinTrees(){
		this.deep =DEFAULT_SIZE;
		this.arraySize = (int) (Math.pow(2, deep)-1);//Math.pow(deep, 2)-1;
		datas = new Object[arraySize];
	}
	//以指定深度创建二叉树
	public ArrayBinTrees(int deep){
		this.deep = deep;
		this.arraySize = (int) (Math.pow(2, deep)-1);
		datas = new Object[arraySize];
	}
	//以指定深度，指定根节点创建二叉树
	public ArrayBinTrees(int deep,T data){
		this.deep = deep;
		this.arraySize = (int) (Math.pow(2, deep)-1);
		datas = new Object[arraySize];
		datas[0] = data;
	}
	/**
	 * 为指定节点添加子节点
	 * index 父节点的索引
	 */
	public void add(int index,T data,boolean left){
		if(datas[index]==null){
			throw new RuntimeException(index+"指定父节点不存在,添加失败");
		}
		if(index*2+1>arraySize){
			throw new RuntimeException("树底层数组已满，树下标越界异常");
		}
		if(left){
			datas[index*2+1] = data;
		}else{
			datas[index*2+2] = data;
		}
	}
	//判断是否为空
	public boolean isEmpty(){
		return datas[0]==null;
	}
	//返回根节点
	public T root(){
		return (T) datas[0];
	}
	//返回指定节点(非父节点)的父节点
	public T parent(int index){
		
		return (T) datas[(index-1)/2];
	}
	//返回指定节点的左节点
	public T left(int index){
//		if(datas[index*2+1]!=null){
//			return (T) datas[index*2+1];
//		}
//		return null;
		if(index*2+1>arraySize){
			throw new RuntimeException("该节点为叶子节点,无子节点");
		}
		return (T) datas[index*2+1];
	}
	//返回指定节点的右节点
	public T right(int index){
		if(datas[index*2+2]!=null){
			return (T) datas[index*2+2];
		}
		if(index*2+1>arraySize){
			throw new RuntimeException("该节点为叶子节点,无子节点");
		}
		return (T) datas[index*2+2];
	}
	//返回该二叉树的深度
	public int deep(){
		return deep;
	}
	//返回指定节点的位置
	public int pos(int data){
		//广度遍历每个节点
		for(int i=0;i<arraySize;i++){
			if(datas[i].equals(data)){
				return i;
			}
		}
		return -1;
		
	}
	public String toString(){
		return java.util.Arrays.toString(datas);//datas.toString();
	}
}






















