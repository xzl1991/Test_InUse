package com.test.TreeSort;

import java.util.ArrayList;
import java.util.List;


public class TreeNode<E> {
	/***
	 * 创建一个Node内部私有类，Node实例代表 节点
	 * @author xzl
	 * @param <T>
	 *
	 */
	public class Node<T>{
		//用来保存节点数据
		private T data;
		//指向下一个节点引用
		private int parent;
		
		public Node(){
			
		}
		public Node(T data){
			this.data = data;
		}
		//初始化全部属性的构造器
		public Node(T data,int parent){
			this.data = data;
			this.parent = parent;
		}
		//获取指定节点下的数据
		public E getData(){
			return (E) data;
		}
		//获取指定节点下的数据
		public int getIndex(){
			return parent;
		}
		
		public String toString(){
			return "[data:"+data+",parent:"+parent+"]";
			
		}
	}
	
	private final int DEFAULT_TREE_SIZE = 10;
	private int treeSize =0;
	//使用 node 数组 记录 该树 的所有节点
	private Node<E>[] nodes;
	private int nodeNum;
	//以指定根节点创建树
	public TreeNode(E data){
		treeSize = DEFAULT_TREE_SIZE;
		//创建数组
		nodes = new Node[treeSize];
		//新建数组的 第一个 元素节点
		nodes[0] = new Node<E>(data,-1); 
		nodeNum++;
	}
	//以指定 根节点 和 size 创建 树
	@SuppressWarnings("unchecked")
	public TreeNode(E data,int treeSize){
		this.treeSize = treeSize;
		nodes = new Node[treeSize];
		nodes[0] = new Node<E>(data,-1);
		nodeNum++;
	}
	
	public int size(){
		return nodeNum;
	}
	//为指定节点添加子节点
	public void addNode(E data,Node<E> parent) {
		//parent 不存在
		if(parent==null){
			throw new RuntimeException("指定的parent不存在，无法添加");
		}
		//找到第一个为null的元素，保存新元素
		for(int i=0;i<treeSize;i++){
			if(nodes[i]==null){
				//在 i 位置创建新节点元素
				//nodes[i] = new Node(data,i);
				nodes[i] = new Node(data,pos(parent));
				nodeNum++;
				return;
			}
		}
		throw new RuntimeException("该树已满，无法添加");
	}
	
	//判断书是否为空
	public boolean isEmpty(){
		return nodes[0]==null;
	}
	//返回根节点
	public Node<E> root(){
		return nodes[0];
	}
	//返回指定节点的父节点
	public Node<E> parent(Node node){
		if(node.parent==-1){
			return null;
		}else{
			return nodes[node.parent];
		}
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
	public String toString(){
		return java.util.Arrays.toString(nodes);//datas.toString();
	}
	//返回指定节点的所有子节点
	public List<Node<E>> children(Node node){
		List<Node<E>> nodeList = (List<Node<E>>) new ArrayList<E>();
		for(int i=0;i<treeSize;i++){
			//如果当前父节点位置 等于 当前指定节点位置
			if(nodes[i]!=null&&nodes[i].parent==pos(node)){
				//i 后面的
				nodeList.add(nodes[i]);
			}
		}
		return nodeList;
	}
	
	public int pos(Node node){
		//获取指定节点的位置
		for(int i=0;i<treeSize;i++){
			if(nodes[i]!=null&&nodes[i]==node){
				return i;
			}
		}
		return -1;
	}
	//返回树的深度
	public int deep(){
		//记录节点最大深度
		int max =0;
		for(int i=0;i<treeSize&&nodes[i]!=null;i++){
			//初始化本节点的深度
			int dep = 1;
			//当前节点父节点位置
			int m=nodes[i].parent;
			while(m!=-1&&nodes[i]!=null){
				//如果父节点存在，继续向上搜索
				m = nodes[m].parent;
				dep++;
			}
			if(max<dep){
				max = dep;
			}
			
		}
		
		return max;
	}
}

















