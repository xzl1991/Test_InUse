package com.test.TreeSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCompare {
	public static void main(String[] args){
		String a ="12";
		String b = "2";
		List l = new ArrayList();
		l.add(1);
		Map m = new HashMap();
		System.out.println(l.size()+m.size());
		
		System.out.println(a.compareTo(b));
	}
}
