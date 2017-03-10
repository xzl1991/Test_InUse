package com.core.generalask;


import java.text.Format;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.core.dao.PoolManagerRead;
import com.core.utils.StringUtils;

public class Askbeg {

	/**
	 * 问题求悬赏接口
	 * @param Userid 用户ID
	 * @param Askid 问题ID
	 * @param Source 用户来源  限定三种 0=pc 1=wap 2=大房APP
	 * @return xml格式
	 * @throws Exception 
	 * */
	public static String AskBegXuanShang(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//返回结果
		String result="";
		//返回结果信息
		String codevalue="";
		String messagevalue="";
		// 拼接参数值
		StringBuilder sbuild=new StringBuilder(20);
		Map<String,String> map=StringUtils.getMapOfParasGBK(request);
		// 获取参数，进行参数判断
		String userid=map.get("Userid");
		String askid=map.get("Askid");
		String source=map.get("Source");
		sbuild.append(userid).append(askid).append(source);
		System.out.println(sbuild.toString());
		if(StringUtils.isNull(userid) || StringUtils.isNull(askid) || StringUtils.isNull(source) || StringUtils.checkParamIsInt(sbuild.toString())){
			codevalue="101";
			messagevalue="参数不正确";
		}else{
        		//添加时间参数时间
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		Calendar cal = Calendar.getInstance();
            	cal.setTime(new Date());
            	cal.add(Calendar.WEEK_OF_YEAR, -1);
            	Date time = cal.getTime();
            	String sql = "SELECT * FROM [AskBegXuanShang] WITH(NOLOCK) WHERE UserID=?  AND State=-1  AND AddTime<='?"
            		 + "' AND AddTime>'?'"; 
            	
            	Connection conn=PoolManagerRead.getDbConnection();
        		PreparedStatement prepstmt=null;
        		ResultSet rs=null;
        		try{
        			prepstmt=conn.prepareStatement(sql);
        			prepstmt.setString(1, userid);
        			prepstmt.setString(2,sdf.format(new Date()));
        			prepstmt.setString(3,sdf.format(time));
        			rs=prepstmt.executeQuery();
        			if(rs.next()){
        				//返回结果不为空
        				codevalue="211";
        				messagevalue="参数有误";
//        				return createXml("211","Error:参数有误");
        			}else{
        				/*
        				 * 根据userid 在 manageruser表中判断用户是否是经纪人存在返回GroupId 不存在默认-1
        				 * */
        				/*
        				 * 开始求悬赏 向AskBegXuanShang插入数据 
        				 * */
        				
        			}
//        			while(rs.next()){
//        				userId=rs.getString("UserId");
//        				askId=rs.getString("askId");
//        				source=rs.getString("State");
//        			}
        			
        		}catch(SQLException e){
        			e.printStackTrace();
        		}finally{
        			rs.close();
        		}
		}
		// 创建xml文件
		// 根节点
		Element root=new Element("root");
		// xml文档
		Document doc=new Document(root);
		// 根节点下的子节点
		Element code=new Element("code");
		code.addContent(new CDATA(codevalue));
		root.addContent(code);
		if(StringUtils.isNull(messagevalue)){
			
		}else{
			Element message=new Element("message");
			message.addContent(new CDATA(messagevalue));
			root.addContent(message);
		}	
		// 输出xml内容
		result=StringUtils.xmltostring(doc);
		System.out.println(result+"000");
		return result;
		
	}
	
}
