package com.test.TreeSort;

import java.util.ArrayList;
import java.util.List;

import com.test.TreeSort.TreeNode.Node;

public class TreeNodeC <E>{
	// 每个节点要维护 一个子节点链
	private static class SonNode{
		private int pos;
		private SonNode next;
		@SuppressWarnings("unused")
		public SonNode(int pos,SonNode next){
			this.pos = pos;
			this.next = next;
		}
	}
	
	//static
	public static class Node<T>{
		
//		private int child;//指向上一个节点引用
		private T data;
		private SonNode first;//1.记录第一个子节点
		//节点生成
		public Node(){
			
		}
		public Node(T data){
			this.data = data;
			this.first =  null;
		}
		/*public Node(T data,SonNode next){
			this.first = next;
			this.data = data;
		}*/
		//获取指定节点下的数据
		//获取指定节点下的数据
		/*public int getIndex(){
			return child;
		}*/
		
		public String toString(){
			if(first==null){
				return "[data:"+data+",first:-1]";
			}else{
				return "[data:"+data+",first:"+first.pos+"]";
			}
			
		}
	}
	//设置创建参数
	private final int DEFAULT_SIZE = 10;
	private int treeSize=0;
	//节点个数
	private int nodeNum;
	//创建 数组
	private Node<E>[] nodes;
	//已制定根节点创建树
	@SuppressWarnings("unchecked")
	public TreeNodeC(E data){
		treeSize = DEFAULT_SIZE;
		nodes = new Node[treeSize];
		nodes[0] = new Node<E>(data);
		nodeNum++;
	}
	//以指定根节点 和 size 创建
	public TreeNodeC(E data,int treeSize){
		this.treeSize = treeSize;
		nodes = new Node[treeSize];
		nodes[0] = new Node(data);
		nodeNum++;
	}
	//返回指定位置处的节点
	public Node<E> getByIndex(int i){
		return nodes[i];
	}
	
	//根据data名字 返回指定位置处的节点
	public Node<E> getByData(E data){
		for(int i=0;i<treeSize;i++){
			if(nodes[i]!=null&&data!=null&&nodes[i].data.equals(data)){
				return nodes[i];
			}
		}
		return null;
	}
	//为制定节点 添加子节点
	@SuppressWarnings("unchecked")
	public void add(E data,Node parent){
		if(pos(parent) == -1){
			throw new RuntimeException("指定节点不存在，添加失败！");
		}
		for(int i=0;i<treeSize;i++){
			if(nodes[i]==null){
				//创建新节点 ，并使用指定 数组元素保存
				nodes[i] = new Node(data);
				//nodes[i].first = null;
				if(parent.first==null){
					parent.first =  new SonNode(i, null);// new SonNode(i,null);
				}else{
					//遍历 first 后面的 节点
					SonNode next = parent.first;
					while(next.next!=null){
						next = next.next;
					}
					//next.next = null;
					next.next = new SonNode(i,null);
				}
				nodeNum++;
				return;
			}
		}
		throw new RuntimeException("添加新节点失败，树已满");
	}
	
	//获取指定节点的位置
	public int pos(Node node){
		for(int i=0;i<treeSize;i++){
			if(nodes[i]!=null&&nodes[i]==node){
				return i;
			}
		}
		return -1;
	}
	// 获取size
	public int size(){
		return nodeNum;
	}
	//判断是否为空
	public boolean isEmpty(){
		return nodes[0]==null;
//		if(nodes!=null){
//			return false;
//		}
//		return true;
	}
	//返回根节点
	public Node<E> root(){
//		return nodes[nodeNum];
		return nodes[0];
	}
	//返回指定节点所有子节点
	public List<Node<E>> children(Node<E> node){
		List<Node<E>> list = new ArrayList<Node<E>>();
		//子节点存在
		//获取节点链 遍历 节点链上的元素
//		if(node.first!=null){
//			SonNode next = node.first.next;
//			while(next.next!=null){
//				list.add(nodes[next.pos]);
//			}
//		};
		
		SonNode next = node.first;
		while(next!=null){
			list.add(nodes[next.pos]);
			next = next.next;
		}
		return list;
	}
	//返回指定节点的 第 index 子节点
	public Node<E> child(Node<E> parent,int index){
//		if(parent.first==null){
//			throw new RuntimeException("子节点不存在");
//		}
//		for(int i=0;i<index;i++){
//			if(){}
//		}
		SonNode next = parent.first;
		for(int i=0;next!=null;i++){
			if(i==index){
				//return nodes[i];
				return nodes[next.pos];
			}
			next = next.next;
		}
		return null;
	}
	//获取树的深度
	public int deep(){
		return deep(root());
	}
	//每棵子树的深度是其子树的最大深度+1
	private int deep(Node<E> root) {
		// TODO Auto-generated method stub
		if(root.first==null){
			return 1;
		}
		int deep=0;
		//遍历每个子节点的 深度下层每有 一个子节点 深度 加1
		SonNode next = root.first;
		//获取位置 看是否存在 子节点 ----------这个思路是对的
//		for(int i=0;next!=null;i++){
//			while(next){
//			}
//		}
		int dep = 0;
		while(next!=null){
			//遍历 每个子节点的深度
			System.out.println(nodes[next.pos].data);
			System.out.println(next.pos);
			dep = deep(nodes[next.pos]);
			int temp = deep(nodes[next.pos]);
			System.out.println("temp值:"+temp);
			if(temp>deep){
				deep = temp;
				System.out.println(temp+",深度："+temp);
			}
			next = next.next;
			if(next!=null){
				System.out.println("位置:"+next.pos);
			}
		}
		return deep+1;
	}
}
























































