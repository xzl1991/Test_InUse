package com.test.Map_Set;

public class LinkedStackTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedStack<String> s = new LinkedStack<String>();
		s.push(null);
		System.out.println(s.peek());
		System.out.println(s.pop());
		System.out.println("赋值前:"+s.isEmpty()+","+s.size());
		for(int i=0;i<5;i++){
			s.push("推栈:"+(char)('A'+i));
			System.out.println(s.peek());
//			System.out.println(s.pop());
		}
		System.out.println(s.toString());
		s.clear();
		System.out.println("赋值123abd:"+s.isEmpty()+","+s.size());
	}

}
