package com.test.list;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TestList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Object> ls = new ArrayList(16);
		Collection ls1 = new LinkedList();
		Stack s = new Stack();
		Collections.synchronizedList(ls);
		
//		ls1.size();
		System.out.println("ls大小"+ls.size());
		int size = 0;
		Object[] obj = new Object[10];
		for(int i=0;i<5;i++){
			obj[size++]=i;
//			System.out.println(ls.get(i));
		}
		for(int i=0;i<obj.length;i++){
//			String s =  obj[(Integer) i].toString();
			Object o = obj[i];
			System.out.println("Obj的内容:"+o);
		}
		ls.add(1);
		ls.add(2);
		ls.add("多少3");
		ls.add("多少4");
		ls.add(3, "dd");
		ls.removeAll(ls);
		ls.toString();
		ls.clear();
//		final List<Integer> piDigits = [ 1,2,3,4,5,8 ]; 
//		boolean Character.equalsIgnoreCase('a', 'b');
//		ls.remove(-1);
		for(int i=0;i<ls.size();i++){
			System.out.println(ls.get(i));
		}
		System.out.println(ls.indexOf(null));
		System.out.println("we".equals(null));
	}

}
