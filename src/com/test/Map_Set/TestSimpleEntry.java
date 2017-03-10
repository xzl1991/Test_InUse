package com.test.Map_Set;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TestSimpleEntry<K, V> implements Map<K, V>{
	private final  K key;
	private V value;
	public TestSimpleEntry(K key,V value){
		this.key = key;
		this.value = value;
	}
	/**
	 * @param args
	 */

	public V getValue() {
		return value;
	}
		/***
		 * 改变 key-value的 value值
		 * @param value
		 */
	public V setValue(V value) {
		V oldvalue = this.value;
		this.value = value;
		return oldvalue;
	}
	
	/**
	 * 根据key 判断是否相等
	 */
	public boolean equals(Object obj){
		if(obj==this){
			return true;
		}
		if(obj.getClass()==TestSimpleEntry.class){
			TestSimpleEntry ts = (TestSimpleEntry) obj;
			return ts.getKey().equals(getKey());
		}
		return false;
	}
	
	public int hashcode(){
		
		return key==null?0:key.hashCode();
	}
	public K getKey() {
		return key;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

//	public boolean containsKey(K key) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	@Override
//	public boolean containsKey(Object key){
//		return false;
//	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}
	public String toString(){
		return key+":"+value;
		
	}
}
