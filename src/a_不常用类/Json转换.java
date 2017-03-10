package a_不常用类;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

public class Json转换 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<String, String>();
		m.put("sd", "撒的");
		m.put("深度", "干活");
		m.put("地方", "覆盖");
		m.put("多个", "合格");
		System.out.println(mapToJson(m));
	}
	public static String mapToJson(Map<String, String> map){
		JSONObject jsonObject = new JSONObject();   
		 for (Entry<String, String> entry : map.entrySet()) {
			 jsonObject.put(entry.getKey(), entry.getValue());  
	     }
		return jsonObject.toString();
	}
}
