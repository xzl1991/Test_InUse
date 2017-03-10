package a_生成xml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Document doc = null;
		Map<String, String> xmlParam1 = new LinkedHashMap<String,String>();
		xmlParam1.put("TimeUse", "");
		xmlParam1.put("code", "112");
		xmlParam1.put("message1", "2321");
		xmlParam1.put("message2", "112");
		xmlParam1.put("message", "sada沙发垫");
		doc = StringUtils.createXmlDoc("root", xmlParam1);
		System.out.println("\n"+doc+"1111111111111111111111");
		
//		// TODO Auto-generated method stub
		Element root=doc.getRootElement();
		System.out.println(root.getChild("TimeUse")+".....");
//		// 生成文档
//		
//		// 创建userId节点
//	    Element node= null;
//	    doc = new Document(root);
//			// 设置节点值
	    Element timeUse = new Element("TimeUse");
	    timeUse.addContent(new CDATA("哈哈哈哈实施的"));
		root.addContent(timeUse);
		Element timeUse1 = new Element("TimeUse1");
		if(timeUse.getChildren().contains(timeUse1)){
			
		}else{
			timeUse.addContent(timeUse1);
		}
		Element c = timeUse.getChild("TimeUse1");
		Element c1 = new Element("code");
		c1.addContent(new CDATA("12321"));
		c.addContent(c1);
		//添加并列的 节点
		Element cc1 = new Element("TimeUse1");
		cc1.addContent(new CDATA("实施的"));
		timeUse.addContent(cc1);
		System.out.println(xmltostring(doc));
		
		List t = root.getChildren("TimeUse");//.getChild("TimeUse");
		Element t1 = (Element) t.get(1);
//		t1.setText("看见了考试的就离开");
		t1.setContent(new CDATA("看见了考试的就离开"));
		System.out.println("0000"+t.size()+t1.getText());
		System.out.println(xmltostring(doc));
	}
	public void add(){
		
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
}
