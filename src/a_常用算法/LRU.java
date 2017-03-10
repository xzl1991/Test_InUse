package a_常用算法;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.management.RuntimeErrorException;

import a_路由算法.LRUCache1;

public class LRU<K, V> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		lru();
	}
	static   void lru() {
        System.out.println();
        System.out.println("===========================LRU 链表实现===========================");
        LRU<Integer, String> lru = new LRU(5);
        lru.put(1, "11");
        lru.put(2, "12");
        lru.put(3, "13");
        lru.put(4, "11");
        lru.put(5, "14");
        System.out.println(lru.toString());
        lru.put(6, "66");
        System.out.println(lru.toString());
        lru.get(2);
        System.out.println(lru.toString());
        lru.put(7, "77");
        System.out.println(lru.toString());
        lru.get(4);
        System.out.println(lru.toString());
        System.out.println();
    }
	private final  int MAX_CACHE_SIZE; 
	private Entry head;
	private Entry last;
	private  HashMap<K,Entry<K,V>> hashMap;
	//初始化
	public LRU(int size){
		this.MAX_CACHE_SIZE = size;
		hashMap = new HashMap<>();
	}
	//存放数据
	public void put(K key,V value){
		Entry entry = hashMap.get(key);
		if(entry == null){//新的
			if(hashMap.size()>=MAX_CACHE_SIZE){
				//移除最后一个元素
				hashMap.remove(last.key);
				removeLast();
			}
			entry = new Entry();
            entry.key = key;
		}
		entry.value = value;
		//放在第一个位置
		putFirst(entry);
		hashMap.put(key, entry);
	}
	public V get(K key){
		Entry entry =  hashMap.get(key);
		if(entry==null) return null;
		putFirst(entry);
		return (V) entry.value;
	}
	public void remove(K key){
		 Entry entry = hashMap.get(key);
	        if (entry != null) {
	            if (entry.pre != null) entry.pre.next = entry.next;
	            if (entry.next != null) entry.next.pre = entry.pre;
	            if (entry == head) head = entry.next;
	            if (entry == last) last = entry.pre;
	        }
	        hashMap.remove(key);
	}
	private void putFirst(Entry entry) {
		// TODO Auto-generated method stub
		if (entry == head) return;
		//中间的节点操作
		if (entry.pre != null) entry.pre.next = entry.next;
	    if (entry.next != null) entry.next.pre = entry.pre;
	    if (entry == last) last = last.pre;
	    if (head == null || last == null) {
            head = last = entry;
            return;
        }
	    entry.next = head;
	    head.pre = entry;
	    head = entry;
	    entry.pre = null;
	}
	//
	private void removeLast() {
		// TODO Auto-generated method stub
		if(last!=null){
			last = last.pre;
			if(last==null) head = null;
			else last.next = null;
		}
	}
	   @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        Entry entry = head;
	        while (entry != null) {
	            sb.append(String.format("%s:%s ", entry.key, entry.value));
	            entry = entry.next;
	        }
	        return sb.toString();
	    }
	//内部类
	  class Entry<K, V> {
	        public Entry pre;
	        public Entry next;
	        public K key;
	        public V value;
	    }
}


































