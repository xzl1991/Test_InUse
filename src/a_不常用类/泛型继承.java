package a_不常用类;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.CDATA;
import org.jdom2.Element;

public class 泛型继承 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<? extends Collection<String>,String> ma = null;
		String[] k = null;
		String s ="1,2,s";
		k = s.split(",");
		System.out.println(k.length);
	}
	/*
	 * 情况1 修改 map
	 * 		如果1 的key 包含2
	 * 		取出map 
	 * 		遍历2 的map 修改 1 map的值
	 * 情况2 添加 map
	 * 		<ask>
	 * 			<map value>
	 * 		</>
	 * 		<answer>
	 * 			<map1 value>
	 * 如果两个部分存在相同的节点名称 则会被覆盖
	 * k1    v
	 * k1 k2 v
	 * k2    v
	 * 第二个参数的key==<k1,k2>
	 * 用于连接前后不关联的  map 
	 * 同时只允许一个Object 类型是Map 
	 * 前一个Object 类型是List  取key1
	 * 否则取key2
	 * */
	public static void mapValueReplace_Add(Map<? extends Object,Map<String,String>> map,Map<? extends Object,Map<String,String>> replaceWith){
		Map<String,String> value = null;
		Map<String,String> valueMap = null;
		Object obj = null;
			for(Entry<? extends Object, Map<String, String>> entry:replaceWith.entrySet()){
				obj = entry.getKey();
				if(obj instanceof String[]){//后一个是k1,k2 取 k2
					 for(Entry<String, String> ob:((Map<String,String>) obj).entrySet()){
						 System.out.println(ob.getKey()+"--->" +ob.getValue());
						 obj = ob.getValue();
					 }
				}else if(map.containsKey(obj)){
					obj = entry.getKey();
					if(obj instanceof Map){
						for(Entry<String, String> ob:((Map<String,String>) obj).entrySet()){
							 System.out.println(ob.getKey()+"--->" +ob.getValue());
							 obj = ob.getKey();
						 }
					}
				}
				if(map.containsKey(obj)){
					value = map.get(obj);
					valueMap = replaceWith.get(obj);
						value.putAll(valueMap);
				}
			}
	}
}
