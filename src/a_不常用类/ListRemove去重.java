package a_不常用类;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Element;

public class ListRemove去重 {
	public static void main(String args[]){
		List<String> ls = new ArrayList<String>();
		for(int i=0;i<6;i++){
			ls.add(i+"ss");
		}
		Map<Integer, String> m = new HashMap<Integer, String>();
		m.put(1, "深度");
		m.put(2, "深度2");
		m.put(3, "深度3");
		
		Map m1 = new HashMap<String, String>();
		m.put(1, "深度111");
		m.put(21, "深度2");
		m.put(31, "深度3");
		m.putAll(m1);
		 for(Map.Entry<Integer, String> entry:m.entrySet()){
			 System.out.println(entry.getKey()+"--->" +entry.getValue());
		 }
		
		
		
		List<String> ls1 = new ArrayList<String>();
		ls1 .addAll(ls);
		List l = new ArrayList<String>();
		l.add("4ss");
		l.add("2ss");
		ls.removeAll(l);
		for(int i=0;i<ls.size();i++){
			System.out.println(ls.get(i));
		}
		ls.addAll(l);
		System.out.println("...........");
		for(int i=0;i<ls.size();i++){
			System.out.println(ls.get(i));
		}
		System.out.println(ls.contains("4ss"));
		System.out.println(ls.contains("4s11s"));
	}
	
}
