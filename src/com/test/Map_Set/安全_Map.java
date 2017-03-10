package com.test.Map_Set;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class 安全_Map {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<>(3);
		ConcurrentHashMap<String, String> map1 = new ConcurrentHashMap();
		map1.put("1", "11");
		map1.put("2", "21");
		map1.put("3", "31");
		map1.put("s", "s1");
		map1.put("a", "a1");
		map1.put("b", "b1");
		map1.put("4", "41");
		map1.put("5", "51");
		map1.put("6", "61");
		for(int i=0;i<10;i++){
			map1.put(i+"1", "61");
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			
		}
		for (Map.Entry<String, String> entry : map1.entrySet()) {
			System.out.println(entry.getKey()+"...."+entry.hashCode()+":"+entry.getKey().hashCode()+":value--:"+entry.getValue());
		}
	}

}

