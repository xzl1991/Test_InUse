package com.test.TreeSort;

public class ArrayBinTest {

	/**
	 * @param args
	 */
	public static  void main(String[] args) {
		// TODO Auto-generaed method stub
		ArrayBinTrees<String>  tree = new ArrayBinTrees<String>(4, "root");
		tree.add(0, "第一次子节点", false);
		tree.add(2, "第二次子节点", false);
		tree.add(0, "第1次子节点true", true);
		tree.add(6, "第三次子节点", false);
		System.out.println(tree);
		System.out.println(tree.deep());
	}

}
