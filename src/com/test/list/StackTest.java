package com.test.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.collections.map.LinkedMap;

public class StackTest{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Stack stack = new Stack();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		for(int i=0;i<stack.size();i++){
//			System.out.println(stack.peek());
			System.out.println(stack.pop());
		}
		
		
		Queue q = null;
		q = new ArrayBlockingQueue(3);
		q = new LinkedBlockingQueue(3);
		q = new PriorityBlockingQueue<>();
		
		
		ArrayList ls = null;
		LinkedList ls1 = null;
		Set s = new HashSet();
		Map map = new LinkedMap();
		map.put("s", "ss");
		System.out.println("linkedmap:"+map.get("s"));
		map = new LinkedHashMap();
		map.put("s", "ss");
		System.out.println("LinkedHashMap:"+map.get("s"));
		 map = (Map) new HashMap();
		 map.put("3", "谁说的3");
		 map.put("1", "谁说的1");
		 map.put("2", "谁说的2");
		 map.put("5", "谁说的5");
		 map.put("4", "谁说的4");
		TreeMap treeMap = new TreeMap();
		treeMap.put("3", "谁说的3");
		treeMap.put("1", "谁说的1");
		treeMap.put("2", "谁说的2");
		treeMap.put("5", "谁说的5");
		treeMap.put("4", "谁说的4");
		System.out.println("....");
	}

}


















