package a_生成xml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class CreateXml {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> xmlParam = new LinkedHashMap<String,String>();
		Map<String, String> xmlParam1 = new LinkedHashMap<String,String>();
		xmlParam.put("TimeUse", "");
		xmlParam.put("code", "112");
		xmlParam1.put("message1", "");
		xmlParam1.put("message2", "");
		xmlParam.put("message", nodes(xmlParam1));
		String xml = createXml("root",xmlParam);
		System.out.println("******");
		System.out.println(xml);
	}
	public static  String nodes(Map<String,String> args) throws Exception{
//		Element root=new Element("root");
		// 生成文档
//		Document doc=new Document(root);
			// 创建userId节点
		StringBuffer sb = new StringBuffer();
			 for(Map.Entry<String, String> entry:args.entrySet()){
				 String nodeName = entry.getKey();
				 sb.append("<"+nodeName+">");
				 sb.append("<![CDATA["+entry.getValue()+"]>");
				 sb.append("</"+nodeName+">");
				 sb.append("\n");
//				 System.out.print(entry.getKey()+"--->" +entry.getValue());
//	             Element node=new Element(entry.getKey());
//	 			// 设置节点值
//	 			node.addContent(new CDATA(entry.getValue()));
//	 			// 添加节点到根节点
////	 			root.addContent(node);
//	 			sb.append(node);
			 }
		System.out.println(sb.toString()+"........");
		return sb.toString();
	}
	public static  String createXml(String roots,Map<String,String> args) throws Exception{
		Element root=new Element(roots);
		// 生成文档
		Document doc=new Document(root);
			// 创建userId节点
			 for(Map.Entry<String, String> entry:args.entrySet()){
				 System.out.print(entry.getKey()+"--->" +entry.getValue());
	             Element node=new Element(entry.getKey());
	 			// 设置节点值
	 			node.addContent(new CDATA(entry.getValue()));
	 			// 添加节点到根节点
	 			root.addContent(node);
			 }
		 addNode("吼吼吼", "", root.getChild("TimeUse"));
		return xmltostring(doc);
	}
	private static void addNode(String nodeName,String value,Element parent){
		 Element temp = new Element(nodeName);
		 temp.addContent(value);
		 parent.addContent(temp);
	}
	public static  void addNodeValue(Map<String,String> map,String ids ,Element father,String children,String  child){
		//添加ask 节点
		//添加ask 节点
		List<Element> t = null;//.getChild("askUserUrl");
		 t = father.getChildren(children);//.getChild("askUserUrl");
		Element tempElement = null;
		String tempAskuserid = null;
		String info = null;
		// 创建userId节点
		for(int i=0;i<t.size();i++){
			//获取指定的子元素
			tempElement = (Element) t.get(i).getChild(child);
			tempAskuserid = tempElement.getText();
			if(ids.contains(tempAskuserid)){
				info = map.get(tempAskuserid);
				//把保存的askuser 赋值给对应的userid
				tempElement.setContent(new CDATA(info));
			}else{
				tempElement.setText("");
			}
		}
	}
	/**
	 * 把xml文档转换成字符串
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static String xmltostring(Document doc) throws Exception{
		if (doc == null)
			return "" ;
		// 格式化xml文件
		Format format=Format.getCompactFormat();
		format.setEncoding("GB2312");   // 设置编码
		format.setIndent(" ");
		
		XMLOutputter out =new XMLOutputter();
		out.setFormat(format);
		String result = out.outputString(doc);
		return result;
	}
	public static String xmltostring(String doc) throws Exception{
		if (doc == null)
			return "" ;
		// 格式化xml文件
		Format format=Format.getCompactFormat();
		format.setEncoding("GB2312");   // 设置编码
		format.setIndent(" ");
		
		XMLOutputter out =new XMLOutputter();
		out.setFormat(format);
		String result = out.toString();
		return result;
	}
}
