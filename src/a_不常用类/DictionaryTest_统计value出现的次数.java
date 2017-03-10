package a_不常用类;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DictionaryTest_统计value出现的次数 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, String> map= new HashMap<String, String>();
		
		map.put("1", "a");
		map.put("2", "b");
		map.put("3", "a");
		map.put("4", "c");
		map.put("5", "a");
		
		Iterator<String> ite = map.values().iterator();
		
		Map<String, Integer> countMap =new HashMap<String, Integer>();
		
		while(ite.hasNext()){
			String item = ite.next();
			
			if(countMap.containsKey(item)){
				countMap.put(item, Integer.valueOf(countMap.get(item).intValue() + 1));
			}else{
				countMap.put(item, new Integer(1));
			}
		}
		
		//print the count
		for (Iterator iter = countMap.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println(key + " apperas " + countMap.get(key) + " times");
		}

}

}
