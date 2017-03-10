package a_不常用类;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class 判断类型 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> m1 = new HashMap<String, String>();
		m1.put("as2深度", "深度");
		Map<String,String> m2 = new HashMap<String, String>();
		m2.put("as", "深度");
		System.out.println(m1.containsKey("as")+m1.get(""));
		System.out.println(m1.containsKey("as2"));
		String s = null;
		System.out.println(s instanceof String);
		te("s");
		te(s);
		te(new HashMap<Integer,Integer>());
	}
	public static void te(Object ob){
		if(ob instanceof String){
			System.out.println(ob+"是String类型");
		}else if(ob instanceof Map){
			System.out.println(ob+":这是个Map");
		}else if(ob instanceof Integer){
			System.out.println(ob+"是int类型");
		}
	
	}
}
