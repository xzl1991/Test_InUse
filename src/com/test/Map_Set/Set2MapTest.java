package com.test.Map_Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Set2MapTest<K, V> extends HashSet<TestSimpleEntry<K,V>>{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set2MapTest<String,Integer> map = new Set2MapTest<String, Integer>();
		Set2MapTest<String,String> map1 = new Set2MapTest<String, String>();
		 map1.put("测试", "100");
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
			for (int i=0;i<map1.size();i++) {
				System.out.println(map1.hashCode()+";");
			}
		 
		 
		 
		int a= map.put("map",50);
		 int b = map.put("数学", 88);
		 map.put("数学1", 88);
		 System.out.println(map.put("计算机", 818)+"：位置");
		 
		 System.out.println(map.get("map"));
		 System.out.println(map.containsValue(50)+"，Map大小是:"+map.size());
		
		 System.out.println(map1.hashCode());
  List ary = new ArrayList(2);
  ary.add(1);
  ary.add(2);
  ary.add(3);
  System.out.println("List的长度:"+ary.isEmpty());
		 map.removeEntry("map");
		 System.out.println(map.get("map"));
		 System.out.println(map.containsValue(50));
		 System.out.println(map.get("测试"));
		 System.out.println(map.containsValue(100));
		 map.clear();
		 System.out.println(map.get("测试"));
		 System.out.println(map.containsValue(100));
	}
	
	public void clear(){
		super.clear();
	}
	
	//判断是否包含某个 key
//	public boolean containsKey(K key){
//		return super.containsKey(new TestSimpleEntry<K, V>( key, null));
//	}
	
	public boolean containsValue(Object value){
		for(TestSimpleEntry<K, V> s:this){
			if(s.getValue().equals(value)){
				return true;
			}
		}
		return false;
	}
	
	//根据指定 key 获取value
	public V get(Object key){
		for(TestSimpleEntry<K, V> s:this){
			if(s.getKey().equals(key)){
				return (V) s.getValue();
			}
		}
		return null;
		
	}
	//将制定key -value 放入集合
	public V put(K key,V value){
		add(new TestSimpleEntry<K, V>(key, value));
		return value;
	}
	//另一个map 的 k-v放入该map
	public void putAll(Map< K, V> map){
		for(K key:map.keySet()){
			add(new TestSimpleEntry<K, V>(key, map.get(key)));
		}
	}
	
	//根据指定key 删除 key-value
	public V removeEntry(Object key){
		for(Iterator<TestSimpleEntry<K,V>> it = this.iterator();it.hasNext();){
			TestSimpleEntry<K, V> simp = it.next();
			if(simp.getKey().equals(key)){
				V v = simp.getValue();
				it.remove();
				return v;
			}
		}
		return null;
	}
	
	public int size(){
		return super.size();
	}
	
	
	
}



















