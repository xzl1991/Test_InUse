package com.test.TreeSort;

import java.util.List;

public class TreeNodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeNode<String> tree = new TreeNode<String>("root");
		TreeNode.Node root = tree.root();
		tree.addNode("第一个节点", root);
		System.out.println(root.getData());
		tree.addNode("第二个节点", root);
		tree.addNode("1的子节点", tree.getByIndex(1));
		tree.addNode("11的子节点", tree.getByData("1的子节点"));
		tree.addNode("12的", tree.getByData("1的子节点"));
//		tree.addNode("吼吼吼", tree.getByData("rootdsad"));
		//获取 子节点
		TreeNode.Node d = tree.getByData("吼吼吼");
//		System.out.println("父节点:"+tree.parent(d)+". Root:"+tree.parent(root));
		System.out.println("*******************开始******");
		for(int i=0;i<tree.size();i++){
			System.out.println(tree.getByIndex(i).getData());
		}
		System.out.println("*******************结束*******");
		
		List childTree = tree.children(tree.getByData("root"));
		System.out.println(tree.getByData("root").getData()+":下的节点数:"+childTree.size()+"分别是:");
		for(int i=0;i<childTree.size();i++){
			System.out.println(childTree.get(i));
		}
		System.out.println("*******************"+tree);
		System.out.println(tree.isEmpty()+",大小:"+tree.size()+tree.parent(tree.children(root).get(0))+",树的深度:"+tree.deep());
	}

}





















