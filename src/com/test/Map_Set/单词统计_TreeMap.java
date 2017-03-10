package com.test.Map_Set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class 单词统计_TreeMap<T> implements Comparator<T>{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = "Good morning. Have a good class.Have a good visit.Have fun!";
		String[] words = name.split("[ \n\t\r.;:!?()]");
		sortByKey(words);
		System.out.println("..................");
		sortByValue(words);
		 
	          
	}
	public static void sortByKey(String[] words){
		Map<String,Integer> treeMap = new TreeMap<>();
		String key = null;
		for(int i=0;i<words.length;i++){
			key = words[i];
			if(key.length()>0){
				if(treeMap.get(key)==null){
					treeMap.put(key, 1);
				}else{
					int size = treeMap.get(key).intValue();
					treeMap.put(key, ++size);
				}
			}
		}
		Set<Map.Entry<String, Integer>> entySet = treeMap.entrySet();
		for(Map.Entry<String, Integer> ent : entySet){
			System.out.println(ent.getKey()+",出现:"+ent.getValue()+"次");
		}
	}
	public static void sortByValue(String[] words){
		Map<String,Integer> treeMap = new TreeMap<>();
		String key = null;
		for(int i=0;i<words.length;i++){
			key = words[i];
			if(key.length()>0){
				if(treeMap.get(key)==null){
					treeMap.put(key, 1);
				}else{
					int size = treeMap.get(key).intValue();
					treeMap.put(key, ++size);
				}
			}
		}
		Set<Map.Entry<String, Integer>> entySet = treeMap.entrySet();
		 List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(entySet);  
	     Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {  
	            //升序排序  
				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					// TODO Auto-generated method stub
					return o1.getValue().compareTo(o2.getValue()); 
				}  
	        });  
//		for(Map.Entry<String, Integer> ent : entySet){
//			System.out.println(ent.getKey()+",出现:"+ent.getValue()+"次");
//		}
	        for(Map.Entry<String,Integer> ent:list){ 
	        	System.out.println(ent.getKey()+",出现:"+ent.getValue()+"次");
	          }
	}
	@Override
	public int compare(T o1, T o2) {
		// TODO Auto-generated method stub
		return 0;
	}
}	



























