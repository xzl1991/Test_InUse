package a_生成xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.print.Doc;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlTest1 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Document doc = null;
		Element root=new Element("soufunaskinterface");
		// 创建userId节点
	    doc = new Document(root);
	    Map<String, String> xmlParam = null;
	    xmlParam = new HashMap<String, String>();
		xmlParam.put("TimeUse", "0");
		xmlParam.put("code", "sadsa");
		xmlParam.put("message", "撒的撒");
	    Element ask = new Element("Ask");
	    root.addContent(ask);
	    addNode(xmlParam,ask);
//	    getNode1(root);
//	    root.addContent(getNode1(ask));
		System.out.println(xmltostring(doc));
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
	private static void getNode1(Element root){
		//添加ask 节点
		Element ask = new Element("Ask");
		Element Askid = null;
		Element AskUrl = null;
		Element AnswerCount = null;
		Element AnswerCountForApp = null;
		Element ClassId = null;
		Element ClassName = null;
		Element AskDate = null;
		Element Source = null;
		Element NewCode = null;
		Element AskUserId = null;
		Element AskUserUrl = null;
		Element AskUser = null;
		Element Title = null;
		Element Tags = null;
		Element XuanShang = null;
		Element State = null;
		StringBuffer ids = null;
		StringBuffer userids = null;
		String temp = null;
		String city = null;
		ids = new StringBuffer();
		String id = null;
		String userid = null;
				Askid = new Element("Askid");
				id = "Id";
				ids.append(id+",");
				Askid.addContent(id);
				ask.addContent(Askid);
				/*
				 *  条件1：问题信息.City=美国=>问题信息.Id，
				条件2：以外的场合, “http://www.fang.com/ask/”+问题信
				 * */
				city = "city";
				AskUrl = new Element("AskUrl");
				AskUrl.addContent(city.equals("美国")?"http://www.fang.com/ask/"+id+".html":id);
				ask.addContent(AskUrl);
				
				
				AnswerCount = new Element("AnswerCount");
				AnswerCount.addContent("AnswerCount");
				ask.addContent(AnswerCount);
				
				AnswerCountForApp = new Element("AnswerCountForApp");
				AnswerCountForApp.addContent("AnswerCountForApp");
				ask.addContent(AnswerCountForApp);
				temp = "ClassId";
				ClassId = new Element("ClassId");
				ClassId.addContent(temp);
				ask.addContent(ClassId);
				
				ClassName = new Element("ClassName");
				ClassName.addContent(temp);
				ask.addContent(ClassName);
				
				AskDate = new Element("AskDate");
				AskDate.addContent("date");
				ask.addContent(AskDate);
				
				Source = new Element("Source");
				Source.addContent("Source");
				ask.addContent(Source);
				
				
				
				NewCode = new Element("NewCode");
				NewCode.addContent("NewCode");
				ask.addContent(NewCode);
				
				AskUserId = new Element("AskUserId");
				userid = "UserId";
				userids = new StringBuffer();
				userids.append(userid+",");
				AskUserId.addContent(userid);
				ask.addContent(AskUserId);
				//view_alluser
				AskUserUrl = new Element("AskUserUrl");
				AskUserUrl.addContent(userid);
				ask.addContent(AskUserUrl);
				
				AskUser = new Element("AskUser");
				AskUser.addContent(userid);
				ask.addContent(AskUser);
				
				Title = new Element("Title");
				Title.addContent(id);
				ask.addContent(Title);
				Tags = new Element("Tags");
				Tags.addContent(id);
				ask.addContent(Tags);
				XuanShang = new Element("XuanShang");
				XuanShang.addContent("XuanShang");
				ask.addContent(XuanShang);
				
				State = new Element("State");
				State.addContent("State");
				ask.addContent(State);
				System.out.println(ask);
				root.addContent(ask);
		}
	private static Element getNode(Element root){
		//添加ask 节点
		
		Element ask = new Element("Ask");
		Element Askid = null;
		Element AskUrl = null;
		Element AnswerCount = null;
		Element AnswerCountForApp = null;
		Element ClassId = null;
		Element ClassName = null;
		Element AskDate = null;
		Element Source = null;
		Element NewCode = null;
		Element AskUserId = null;
		Element AskUserUrl = null;
		Element AskUser = null;
		Element Title = null;
		Element Tags = null;
		Element XuanShang = null;
		Element State = null;
		StringBuffer ids = null;
		StringBuffer userids = null;
		String temp = null;
		String city = null;
		ids = new StringBuffer();
		String id = null;
		String userid = null;
				Askid = new Element("Askid");
				id = "Id";
				ids.append(id+",");
				Askid.addContent(id);
				ask.addContent(Askid);
				/*
				 *  条件1：问题信息.City=美国=>问题信息.Id，
				条件2：以外的场合, “http://www.fang.com/ask/”+问题信
				 * */
				city = "city";
				AskUrl = new Element("AskUrl");
				AskUrl.addContent(city.equals("美国")?"http://www.fang.com/ask/"+id+".html":id);
				ask.addContent(AskUrl);
				
				
				AnswerCount = new Element("AnswerCount");
				AnswerCount.addContent("AnswerCount");
				ask.addContent(AnswerCount);
				
				AnswerCountForApp = new Element("AnswerCountForApp");
				AnswerCountForApp.addContent("AnswerCountForApp");
				ask.addContent(AnswerCountForApp);
				temp = "ClassId";
				ClassId = new Element("ClassId");
				ClassId.addContent(temp);
				ask.addContent(ClassId);
				
				ClassName = new Element("ClassName");
				ClassName.addContent(temp);
				ask.addContent(ClassName);
				
				AskDate = new Element("AskDate");
				AskDate.addContent("date");
				ask.addContent(AskDate);
				
				Source = new Element("Source");
				Source.addContent("Source");
				ask.addContent(Source);
				
				
				
				NewCode = new Element("NewCode");
				NewCode.addContent("NewCode");
				ask.addContent(NewCode);
				
				AskUserId = new Element("AskUserId");
				userid = "UserId";
				userids = new StringBuffer();
				userids.append(userid+",");
				AskUserId.addContent(userid);
				ask.addContent(AskUserId);
				//view_alluser
				AskUserUrl = new Element("AskUserUrl");
				AskUserUrl.addContent(userid);
				ask.addContent(AskUserUrl);
				
				AskUser = new Element("AskUser");
				AskUser.addContent(userid);
				ask.addContent(AskUser);
				
				Title = new Element("Title");
				Title.addContent(id);
				ask.addContent(Title);
				Tags = new Element("Tags");
				Tags.addContent(id);
				ask.addContent(Tags);
				XuanShang = new Element("XuanShang");
				XuanShang.addContent("XuanShang");
				ask.addContent(XuanShang);
				
				State = new Element("State");
				State.addContent("State");
				ask.addContent(State);
				System.out.println(ask);
				return ask;
		}
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
