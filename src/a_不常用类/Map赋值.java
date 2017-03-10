package a_不常用类;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Element;

public class Map赋值 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> nodeValue = null;
		Map<String,Map<String,String>> addNodeValue = null;
		addNodeValue = new HashMap<String, Map<String,String>>();
		nodeValue = new HashMap<String, String>();
		for(int i=0;i<6;i++){
			nodeValue.put("test", i+"呵呵");
			addNodeValue.put(i+"a", nodeValue);
		}
		Map<String,String> map = null;
		for(int i=0;i<5;i++){
			map = addNodeValue.get(i+"a");
			 for(Map.Entry<String, String> entry:map.entrySet()){
				 System.out.println(entry.getKey()+"--->" +entry.getValue());
//				 tempElement=new Element(entry.getKey());
//	 			// 设置节点值
//				 tempElement.addContent(new CDATA(entry.getValue()));
//	 			// 添加节点到根节点
//				 nodeName.addContent(tempElement);
			 }
		}
		
	
	}

}
