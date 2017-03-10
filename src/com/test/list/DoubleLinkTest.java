package com.test.list;

public class DoubleLinkTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DouLinkedList<String> ls = new DouLinkedList<String>(); 
		System.out.println(ls.isEmpty()+"初始化:"+ls.size());
		for(int i=0;i<6;i++){
			ls.add("元素"+i);
		}
		ls.add(4, "lss");
		ls.delete(0);
		
//		System.out.println(ls.get(4));
//		ls.add(0, "第一位");
		System.out.println(ls.toString());
		for(int i=0;i<ls.size();i++){
			System.out.println("循环:"+ls.get(i));
		}
		System.out.println(ls.local("lss"));
		ls.clear();
		System.out.println(ls.isEmpty()+"初始化:"+ls.size());
	}

}
