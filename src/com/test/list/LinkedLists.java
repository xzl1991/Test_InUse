package com.test.list;

public class LinkedLists<T>{
	/***
	 * 创建一个Node内部私有类，Node实例代表 节点
	 * @author xzl
	 *
	 */
	private class Node{
		//用来保存节点数据
		private T data;
		//指向下一个节点引用
		private Node next;
		public Node(){
			
		}
		//初始化全部属性的构造器
		public Node(T data,Node next){
			this.data = data;
			this.next = next;
		}
	}
	private Node head;
	private Node tail;
	private int size;
	//创建空链表
	public LinkedLists(){
		head = null;
		tail = null;
	}
	
	//以指定元素创建链表，该链表只有一个元素
	public LinkedLists(T element){
		head = new Node(element,null);
		//只有一个节点，head tail 都指向同一个节点
		tail = head;
		size++;
	}
	
	public int size(){
		return size;
	}
	
	public T get(int index){
		return (T) getNodeByIndex(index).data;
	}
	//根据索引 index 获取节点
	private Node getNodeByIndex(int index){
		check(index);
		//从头结点开始
		Node current = head ;
		//替换节点
		for(int i=0;i<size&&current!=null;i++,current=current.next){
			if(i==index){
				//返回第 index 个
				return current;
			}
		}
		return null;
	}
	
	//查找链式线性表指定元素的索引
	public int locate(T element){
		Node current = head;//从头开始
		for(int i=0;i<size&&current!=null;i++,current=current.next){
			if(current.data.equals(element)){
				return i;
			}
		}
		return -1;
	}
	//向链表尾部添加元素
	public  void add(T element){
		//还是空链表
		if(head==null){
			head = new Node(element,null);
			tail = head;
		}else{
			//不是空创建新节点
			Node newNode = new Node(element,null);
			//尾节点的next 指向 新节点
			tail.next = newNode;
			//新节点作为 新的尾节点
			tail = newNode;
		}
		size++;
	} 
	//向链式线性表指定位置添加元素
	public  void add(int index ,T element){
		check(index);
		//判断index位置
		if(index==size){
			add(element);
		}
		else {
			
			addBeforeLast(index, element);
		}
		
	} 
	public String toString(){
		
		if(isEmpty()){
			return "[]";
		}
		else{
			StringBuffer sb = new StringBuffer();
			sb.append("[");
//			for(int i=0;i<size&&head!=null;head=head.next,i++){
//				sb.append(head.data+",");
//			}
			for(Node current=head;current!=null;current=current.next){
				sb.append(current.data+",");
			}
			int len = sb.length();
			return sb.delete(len-1, len).append("]").toString();
		}
	}
	private void addBeforeLast(int index ,T element){
		if(index==0){//在表头添加
			addAtHead(element);
			}else{
				/**
				 * 取出第 index处的节点,把前一个节点的 tail 指向新节点，新节点 的 tail 指向 index+1 个节点 getNodeByIndex
				 * 也是 index-1 的next 节点
				 */
				//新建节点
				Node pre = getNodeByIndex(index-1);
				pre.next = new Node( element,pre.next);
				size++;
			}
	}
	//删除指定位置处的 元素
	public T delete(int index){
		check(index);
		/**
		 * 获取指定位置的 前一个节点 指向index的下一个节点
		 * */
		Node del=null;
		//删除第一个节点
		if(index==0){
//			head = getNodeByIndex(1);
			del =head;
			head = head.next;
		}else if(index!=size){
			//前一个元素
			/**
			 * 要改变思路，用节点的引用表示另一个节点
			 * */
//			Node pre = getNodeByIndex(index-1);
//			pre.next = getNodeByIndex(index+1);
//			tail = newTail;
			Node pre = getNodeByIndex(index-1);
			//获取将要删除的节点
			 del = pre.next;
			//前一个节点的尾部 指向 要删除的节点 下一个节点
			pre.next = del.next;
			//被删除节点的 引用置为空
			del.next = null; 
		}else{
//			Node pre = getNodeByIndex(index-1);
//			//获取将要删除的节点
//			 del = pre.next;
//			//被删除节点的 引用置为空
//			del.next = null; 
		}
		size--;
		return del.data;
	}
	public void delete(){
		//删除最后一个元素
		delete(size-1);
	}
	public boolean isEmpty(){
		return size==0;
	}
	public void clear(){
		//清空
		head = null;
		tail = null;
		size = 0;
	}
	private void addAtHead(T element){
		//新建节点，让新节点尾部指向 原来的head，
		Node newHead = new Node(element,head);
		//插入之前是空链表
		if(tail==null){
			head = tail;
		}else{
			newHead.next = head;
			head = newHead;
		}
		size++;
	}
	private void check(int index){
		if(index<0||index>size-1){
		System.out.println("线性表索引越界");
		throw new IndexOutOfBoundsException("线性表索引越界");
		}
	}
}










