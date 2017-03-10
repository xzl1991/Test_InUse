package com.test.多线程;

import java.util.ArrayList;
import java.util.List;

public class TestString3 {

	/**
	 * @param args
	 */
	List urlList = new ArrayList();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		urlList != null
		String s="快乐深深地";
		System.out.println(s.charAt(2));
		deal();
	}
	@SuppressWarnings("unused")
	public static void deal(){
		List urlList = null;
		if(urlList==null){
			System.out.println("null的list");
		}
		else{
			System.out.println("不是null的list");
		}
	}
}
