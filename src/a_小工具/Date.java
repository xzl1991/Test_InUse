package a_小工具;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class Date {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean a = true;
		System.out.println(Integer.parseInt("10235s000")+"00..");
		if(a){
			System.out.println("true");
		}else{
			System.out.println("false");
		}
		Element root=new Element("root");
		// 生成文档
		Document doc=new Document(root);
		// 创建userId节点
		Element userId=new Element("UserId");
		// 设置节点值
		userId.addContent(new CDATA("112"));
		// 创建date节点
		Element Date=new Element("Date"); 
		Date.addContent(new CDATA("233"));
		// 添加节点到根节点
		root.addContent(userId);
		root.addContent(Date);
		XMLOutputter out=new XMLOutputter();
		String result=out.outputString(doc);
		System.out.println("****:"+result);
	}

}
