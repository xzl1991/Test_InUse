package com.test.list;

public class LinkedTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedLists<String> li = new LinkedLists<String>();
		System.out.println("LinkList为空:"+li.isEmpty());
		for(int i=0;i<6;i++){
			li.add("测试LinkList:"+i);
		}
		li.add(3, "添加新的");
		int l = li.locate("添加新的");
		System.out.println("新元素的位置:"+l);
		System.out.println(li.toString());
//		li.delete();
		li.delete(6);
		System.out.println("LinkList为空:"+li.isEmpty()+",大小:"+li.size());
		for(int i=0;i<li.size();i++){
			System.out.println(li.get(i));
		}
		System.out.println(li.toString());
		li.clear();
		System.out.println(li.toString());
		System.out.println("LinkList为空:"+li.isEmpty()+",大小:"+li.size());
	}

}
