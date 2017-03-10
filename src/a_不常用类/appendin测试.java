package a_不常用类;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class appendin测试 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> li = new ArrayList<String>();
		li.add("112");
		System.out.println(appendIn("id",li));
		System.out.println(appendIn("id","123"));
	}
	public static String appendIn(String name, List<String> values){
		int index = 0;
		StringBuffer result = new StringBuffer();
		result.append(" " + name + " in (");
		for(String value : values){
			if(index == values.size()-1){
				result.append("'"+value+"')");
			}else{
				result.append("'"+value+"',");
			}
			index++;
		}
		return result.toString();
	}
	public static String appendIn(String name, String values){
		int index = 0;
		String[] value = null;
		value = values.split(",");//默认不为空
		StringBuffer result = null;
		result = new StringBuffer(30);
		result.append(" " + name + " in (");
		for(String str : value){
			if(index == value.length-1){
				result.append("'"+str+"')");
			}else{
				result.append("'"+str+"',");
			}
			index++;
		}
		return result.toString();
	}
}






