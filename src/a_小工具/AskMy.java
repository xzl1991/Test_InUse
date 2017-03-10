package com.core.http.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.core.dao.PoolManagerRead;
import com.core.utils.StringUtils;


public class AskBeg {

    private String Message = "";   
    private  int groupid ;
    public static String AskBegXuanShang(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	
        //提问成功后调用 求悬赏
    	String  userId = request.getParameter("userid") ;//? 0 : long.Parse(Request["userid"]);
        String askId = request.getParameter("askid") ;//string.IsNullOrEmpty(Request["askid"]) ? 0 : int.Parse(Request["askid"]);
        String source = request.getParameter("askid") ;//string.IsNullOrEmpty(Request["source"]) ? -1 : int.Parse(Request["source"]);
        //检查是否为数字,参数正确才查询
        boolean useid = StringUtils.checkParamIsInt(userId);
        boolean askid = StringUtils.checkParamIsInt(userId);
        boolean sourceCheck = StringUtils.checkParamIsInt(userId);
        
        if (source!=null&&userId!=null&&askId!=null)
        {	
        	if(useid&&askid&&sourceCheck){
        		//添加时间参数时间
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		Calendar cal = Calendar.getInstance();
            	cal.setTime(new Date());
            	cal.add(Calendar.WEEK_OF_YEAR, 1);
            	Date time = cal.getTime();
            	String sql = "SELECT * FROM [AskBegXuanShang] WITH(NOLOCK) WHERE UserID=?  AND State=-1  AND AddTime<='" + sdf.format(new Date()) + "' AND AddTime>'" + sdf.format(time) + "'";
            	
            	Connection conn=PoolManagerRead.getDbConnection();
        		PreparedStatement prepstmt=null;
        		ResultSet rs=null;
        		try{
        			prepstmt=conn.prepareStatement(sql);
        			prepstmt.setString(1, userId);
        			rs=prepstmt.executeQuery();
        			if(rs.next()){
        				//返回结果不为空
        				return createXml("211","Error:参数有误");
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
        		
        		//判断查询结果
        		 if (Integer.parseInt(askId) > 0)
        	        {
        			 
        	        }
                return "";
        	}else{
        		//参数不是数字
        		return createXml("211","Error:参数有误");
        	}
        	
        }else{
        	//参数有空值 //参数不是数字 code 相同
        	return createXml("211","Error:参数有误");
        }
    }
    /**
	 * 
	 * @author 许泽龙
	 * 生成文档
	 * */
	public static  String createXml(String codes,String messages){
		Element root=new Element("root");
		// 生成文档
		Document doc=new Document(root);
		// 创建userId节点
		Element code=new Element("code");
		// 设置节点值
		code.addContent(new CDATA(codes));
		// 创建date节点
		Element message=new Element("message"); 
		message.addContent(new CDATA(messages));
		// 添加节点到根节点
		root.addContent(code);
		root.addContent(message);
		XMLOutputter out=new XMLOutputter();
		String result=out.outputString(doc);
		return result;
	}  

}
