package com.test.list;
public class DouLinkedList<T> {
	private class Node{
		private T data;
		private Node pre;
		private Node next;
		public Node(){
			
		}
		//创建指定的节点构造器
		public Node(T element,Node pre,Node next){
			this.data = element ;
			this.pre = pre;
			this.next = next;
		}
	}
	
	private Node head;//链表里 头结点
	private Node tail;//尾节点
	private int size ;
	public DouLinkedList(){
		head = null;
		tail = null;
	}
	//以指定元素创建链表
	public DouLinkedList(T element){
		head = new Node(element,null,null);
//		head.next = head.pre;
		tail = head;
		size++;
	}
	public int size(){
		return size;
	}
	//添加元素
	public void add(T element){
		//空链表
		if(head==null){
			head = new Node(element,null,null);
			tail = head;
		}else{
//			Node newNode = new Node(element,null,null);
			Node newNode = new Node(element,tail,null);
			tail.next =newNode;//尾部指向新节点
			tail = newNode;//新节点成尾部
		}
		size++;
	}
	//在指定位置添加新节点
	public void add(int index,T element){
		check(index);
		if(index==size){//尾部添加
			add(element);
		}else{
			addBeforeLast(index,element);
		}
	}
	//删除最后一个元素
	public void delete(){
		//先获取最后一个节点
		delete(size-1);
	}
	//删除指定位置处的 节点
	public T delete(int index){
		check(index);
		Node del = null; 
		//删除的是第一个节点
		if(index==0){
			//下一个节点
			del = head;
			head = head.next;
			head.pre = null;
		}else{
			//获取指定位置节点 , 
			del = getNodeByIndex(index);
			
//			del.pre.next = del.next.pre;
			if(del.next!=null){//不是最后一个节点
				del.pre.next = del.next;
//				 del.next.pre = del.pre.next ;
				 del.next.pre = del.pre ;
			}else{
				System.out.println("是最后一个节点");
//				del.pre.next = null; 出错是 没保存前一个节点  设置tail节点
				tail = del.pre; 
				del.pre.next = null;
			}
			del.pre = null;
			del.next = null;
		}
		size--;
		return del.data;
	}
	
	public boolean isEmpty(){
		return size==0;
	}
	
	public void clear(){
		head = null ;
		tail = null;
		size = 0;
	}
	
	public String toString(){
		if(head == null){
			return "[]";
		}else{
			StringBuffer sb = new StringBuffer();
			//遍历取数据
			sb.append("[");
//			Node current=head;
//			for(int i=0;i<size&&current!=null;i++,current.next = head.next){
//				sb.append(current.data.toString()+",");
//			}
			int i=0;
			for(Node current=head;current!=null;current = current.next){
				sb.append(current.data.toString()+",");
			}
			int len = sb.length();
			return sb.delete(len-1, len).append("]").toString();
		}
	}
	
	private void addBeforeLast(int index,T element){
		
		if(index==0){
			//在头部添加
			addAtHead(element);
		}else{
			//获取index 之前的 节点取出
			Node preNode = getNodeByIndex(index-1);
			System.out.println("取出来的数据是:"+preNode.data+","+element);
			//新节点 指向前一个节点的 下一个节点
			Node newNode = new Node(element,preNode,preNode.next);
			//前一个节点 指向新节点
//			newNode.next = preNode.next ;
			preNode.next.pre = newNode ;
			preNode.next = newNode;
//			//新节点的下一个节点的 pre 指向 新节点
		
			size++;
		}
	}	
		//获取 指定位置处的 元素
	public T get(int index){
		return getNodeByIndex(index).data;
		
	}
	//获取指定元素的位置
	public int local(T element){
		//遍历节点
		Node current = head;
		for(int i=0;i<size&&current!=null;current=head.next,i++){
			if(current.data.equals(element)){
				return i;
			}
		}
		return -1;
	}
	//获取 指定位置处的 节点
	public Node getNodeByIndex(int index){
		check(index);
			//取节点
//			for(int i=0;i<size&&current!=null;current=head.next,i++){
//				if(i==index)
//				{
//					return current;
//				}
//			}
			int half = size/2;
			//判断 index
			if(index<=half){
				Node current = head;
				for(int i=0;i<=half&&current!=null;current=current.next,i++){
					if(i==index){
						return current;
					}
				}
			}else{
				Node current = tail;
				for(int i=size-1;i>half&&current!=null;current=current.pre,i--){
					if(i==index)
					{
//						 System.out.println("实际取到的:"+current.data);
						return current;
					}
				}
			}
			
		return null;
	}
	
	private void addAtHead(T element){
//		Node newHead = new Node();
		//创建新节点，新节点的next指向 原来的head
		head = new Node(element,null,head);
		//原来是空链表
		if(tail==null){
			tail = head;
		}
		size++;
	}
	private void check(int index) {
		// TODO Auto-generated method stub
		if(index<0||index>size){
			System.out.println("双向链表下标越界");
			throw new IndexOutOfBoundsException("双向链表下标越界");
		}
	}
}















