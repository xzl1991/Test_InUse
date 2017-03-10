package com.test.TreeSort;

import java.util.List;

public class TestNodeCTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeNodeC<String> tree = new TreeNodeC<String>("root");
		for(int i=0;i<3;i++){
			tree.add("元素"+i, tree.root());
		}
		List get = tree.children(tree.root());
		System.out.println(tree.getByData("root"));
		tree.add("11", tree.getByData("元素0"));
		tree.add("111", tree.getByData("11"));
		for(int i=0;i<get.size();i++){
			System.out.println(get.get(i)+".."+tree.size());
		}
		System.out.println(tree.getByIndex(4));
		System.out.println(tree.getByIndex(5));
		System.out.println(tree.isEmpty()+",深度:"+tree.deep());
	}

}










