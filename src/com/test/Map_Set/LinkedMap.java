package com.test.Map_Set;

import java.util.LinkedHashMap;
import java.util.Map;

import a_路由算法.LRUCache2;

public class LinkedMap {
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LRUCache2 map = new LRUCache2(5);
		Map<Object,Object> map1 = new LinkedHashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(1, 1);
		map.put(5, 5);
		System.out.println(map.toString());
		map.get(3);
		System.out.println(map.toString());
	}
	public String toString(Map<Object,Object> map){
		 StringBuilder sb = new StringBuilder();
	        for (Map.Entry<Object,Object> entry : map.entrySet()) {
	            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
	        }
	        return sb.toString();
	}
}















