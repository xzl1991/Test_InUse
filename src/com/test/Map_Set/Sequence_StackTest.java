package com.test.Map_Set;

public class Sequence_StackTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sequence_Stack<String > s = new Sequence_Stack<String>();
		System.out.println("新建栈:"+s.size()+","+s.isEmpty());
		for(int i=0;i<6;i++){
			s.push("压入元素:"+(char)(i+97));
			System.out.println("栈顶:"+s.peek());
		}
		
		for(int i=0;i<2;i++){
			System.out.println("取出栈顶元素:"+s.pop());
		}
		System.out.println("取出两个元素后:"+s.toString());
		System.out.println("压入数据:"+s.size()+","+s.isEmpty());
	}

}
