package com.test.多线程;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestString1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Integer,String[]> map = new HashMap<Integer,String[]>();
		
		String[] widthAndh = {"这个","是","",""};
		String[] widthAndh1 = {"什么","东西","杀毒","好"};
//		for(int j =0;j<4;j++){
//			for(int i=0;i<4;i++){
//				widthAndh[i]= "新这个"+i+"第:"+j;
//			}
//			map.put(j, widthAndh);
//		}
//		map.put(1, widthAndh);
		map.put(2, widthAndh1);
		Set<Integer> it =map.keySet();
		int a = it.iterator().next();
		System.out.println("键是:"+a);
		for(int i:it){
			System.out.println("key："+i+";");
			String[] s = map.get(i);
			for(int j=0;j<4;j++){
				System.out.println("结果是:"+s[j]);
			}
		}
		
		String[] widthAndHeigth = {"","","",""};
		widthAndHeigth = widthAndh ;
			widthAndHeigth[2]= Integer.toString(3);
			widthAndHeigth[3]= "四级联考";
			String s = "null";
			s.isEmpty();
			System.out.println("是空？"+s.isEmpty());
		for(int j=0;j<4;j++){
			
			System.out.println(widthAndHeigth[j]);
		}
	}

}
