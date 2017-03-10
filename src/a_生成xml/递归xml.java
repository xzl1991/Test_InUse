package a_生成xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class 递归xml {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		Map<String,Object> value = null;
		List<Map<String,Object>> ls= new ArrayList<Map<String,Object>>();
		Document doc = null;
		Element root = new Element("root");
		Map<String,String> ansMap = null;
		doc = new Document(root);
		for(int i=0;i<4;i++){
			value = new LinkedHashMap<String, Object>();
			ansMap = new LinkedHashMap<String, String>();
			value.put("Phone", "深度"+i);
			value.put("Name", "说的"+i);
			value.put("Address", "递归"+i);
			ls.add(value);
			ansMap.put("Id", "asd");
			ansMap.put("Name", "撒的撒");
			value.put("Answer", ansMap);
		}
		map.put("Ask", value);
		map.put("User", ls);
		createXml(map,root);
		System.out.println(XmlTest.xmltostring(doc));
	}
	/**
	 * 递归生成ask节点内容，其中parentsnode为当前要生成节点的父节点
	 */
	public static void createXml(Object o, Element parentnode){
		if(o instanceof Element){
			parentnode.addContent((Element)o);
		}else if(ArrayList.class.equals(o.getClass())){
			List<Object> list = (List<Object>) o;
			for(Object listo : list){
				createXml(listo, parentnode);
			}
		}else if(LinkedHashMap.class.equals(o.getClass())){
			Map<String, Object> map = (Map<String, Object>) o;
			for(Entry<String, Object> entry : map.entrySet()){
				Element node = null;
				if(String.class.equals(entry.getValue().getClass())){
					node = new Element(entry.getKey());
					node.addContent(new CDATA(entry.getValue().toString()));
				}else{
					node = new Element(entry.getKey());
					createXml(entry.getValue(), node);
				}
				parentnode.addContent(node);
			}
		}
	}
	public static void createXml3(Object o, Element parentnode){
		Element node = null;
		String father = null;
		if(LinkedHashMap.class.equals(o.getClass())){
			Map<String, Object> map = (Map<String, Object>) o;
			for(Entry<String, Object> entry : map.entrySet()){
				if(String.class.equals(entry.getValue().getClass())){
					node = new Element(entry.getKey());
					node.addContent(new CDATA(entry.getValue().toString()));
				}else{
					father = entry.getKey();
					if(ArrayList.class.equals(o.getClass())){
						List<Map<String,String>> list = (List<Map<String,String>>) o;
						for(Map<String,String> listo : list){
							node = new Element(father);
							parentnode.addContent(node);
							addNode(listo, node);
						}
					}else{
						node = new Element(entry.getKey());
						createXml(entry.getValue(), node);
					}
				}
				parentnode.addContent(node);
			}
		}
	}
	private static  void addNode(Map<String,String> map,Element answer){
		//添加  节点
		Element tempElement = null;
		 for(Map.Entry<String, String> entry:map.entrySet()){
			 System.out.print(entry.getKey()+"--->" +entry.getValue());
			 tempElement=new Element(entry.getKey());
 			// 设置节点值
			 tempElement.addContent(new CDATA(entry.getValue()));
 			// 添加节点到根节点
 			answer.addContent(tempElement);
		 }
	
	}
	/**
	 * 递归生成ask节点内容，其中parentsnode为当前要生成节点的父节点
	 */
	public static void createXml2(Object o, Element parentnode){
		if(LinkedHashMap.class.equals(o.getClass())){
			Map<String, Object> map = (Map<String, Object>) o;
			for(Entry<String, Object> entry : map.entrySet()){
				Element node = null;
				if(String.class.equals(entry.getValue().getClass())){
					node = new Element(entry.getKey());
					node.addContent(new CDATA(entry.getValue().toString()));
				}else{
					if(entry.getValue() instanceof ArrayList){
						List<Object> mapvalue = (List<Object>) entry.getValue();
						for(Object value : mapvalue){
							node = new Element(entry.getKey());
							createXml2(entry.getValue(), node);
						}
					}else{
						node = new Element(entry.getKey());
						createXml(entry.getValue(), node);
					}
				}
				parentnode.addContent(node);
			}
		}
	}
}
