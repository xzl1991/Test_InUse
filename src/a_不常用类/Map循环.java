package a_不常用类;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Element;

public class Map循环 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String, String>();
		String s = "";
		map.put("ds", s);
		System.out.println(map.get("ds")=="");
	}

}
