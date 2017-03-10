package com.test.Map_Set;

public class LinkedStack <T> {
	
	//创建内部类 Node node 的实例代表 链式栈的 节点
	private class Node{
		//指向下一个节点
		private Node next;
		//保存数据
		private T data;
		public Node(){
			
		}
		
		public Node(T element,Node next){
			this.data = element;
			this.next = next;
		}
	}
	
	//保存该链式栈的 栈顶元素
	private Node top;
	//该栈具有的节点数
	private int size;
	//创建空栈
	public LinkedStack(){
		top = null;
	}
	//已指定元素创建 节点 只有一个元素,引用的时候初始化
	public LinkedStack(T element){
		top = new Node(element,null);
		size++;
	}
	//压栈，存入数据
	public void push(T element){
//		if(size==0){
//			//top = new Node(element,null);
//			//空的创建新栈, 新元素的 next 指向原来的 top
//			top = new Node(element,top);
//		}else{
//			//创建新元素 ,新元素的 next 指向原来的 top
//			Node newTop = new Node(element,top);
//			top = newTop;
//		}
		
		//top = new Node(element,null);
		//空的创建新栈, 新元素的 next 指向原来的 top
		top = new Node(element,top);
		size++;
	}
	//出栈
	@SuppressWarnings("null")
	public T pop(){
		if(size==0){
			throw new IndexOutOfBoundsException("下标越界");
		}else{
//			T data = top.data;
			//要释放 原来的 top
			Node oldTop = top;
			top = top.next;
//			oldTop=null;
			//原 top 的next 引用释放
			oldTop.next = null;
			size--;
			return oldTop.data;
		}
	}
	public void clear(){
		top = null;
		size=0;
	}
	//查询栈顶元素
	public T peek(){
		if(size == 0){
//			return null;
			throw new IndexOutOfBoundsException("下标越界");
		}else{
			return top.data;
		}
	}
	
	public boolean isEmpty(){
		return size==0;
	}
	
	public int size(){
		return size;
	}
	public String toString(){
		if(top==null){
			return "[]";
		}else{
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(Node current = top;current!=null;current=current.next){
				System.out.println("*****:"+current.data);
				sb.append(current.data+",");
			}
			int len = sb.length();
			return sb.delete(len-1,len).append("]").toString();
		}
	}
}
