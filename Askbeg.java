package com.core.commonask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;

import com.core.dao.Pool;
import com.core.dao.ProxoolManagerRead;
import com.core.dao.ProxoolManagerWrite;
import com.core.implement.DealWithSql;
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
	
	public static String CommonBegXuanShang(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String result="";
		//返回结果信息
		String codevalue="";
		String messagevalue="";
		// 拼接参数值
		StringBuilder sbuild=new StringBuilder(20);
		Map<String,String> map=StringUtils.getMapOfParas(request);
		// 获取参数，进行参数判断
		String userid=map.get("Userid");
		String askid=map.get("Askid");
		String source=map.get("Source");
		sbuild.append(userid).append(askid).append(source);
		if(StringUtils.isNull(userid) || StringUtils.isNull(askid) || StringUtils.isNull(source) || StringUtils.checkParamIsInt(sbuild.toString())){
			codevalue="211";
			messagevalue="Error:参数有误";
		}else{
    		//添加时间参数时间
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String sql = "SELECT * FROM [AskBegXuanShang] WITH(NOLOCK) WHERE UserID=?   AND AddTime<=?"
        		 + " AND AddTime>?"; 
        	
        	Connection connread=null;   //读数据库连接
    		PreparedStatement prepstmt=null;
    		ResultSet rs=null;
    		boolean rsbool=false;  //rs结果集是否为空，true不为空，false为空
    		String groupId = null; //判断用户是否是经纪人
    		try{
    			connread = ProxoolManagerRead.getDbConnection();
    			prepstmt=connread.prepareStatement(sql);
    			prepstmt.setString(1, userid);
    			prepstmt.setString(2,sdf.format(new Date()));
    			prepstmt.setString(3,StringUtils.getMondayOfThisWeek());
    			rs=prepstmt.executeQuery();
    			rsbool=rs.next();
    			/*
    			 * 查询AskBegXuanShang表 验证该用户本周内是否以求过悬赏【一个用户一周只能求悬赏一次】
    			 */
    			if(rsbool){
    				//返回结果不为空
    				codevalue="212";
    				messagevalue="Error:该用户本周内已求过悬赏！";
    			}else{
    				/*
    				 * 根据userid 在 manageruser表中判断用户是否是经纪人存在返回GroupId 不存在默认-1
    				 * */
    				String sqlGroupId = "SELECT groupid  FROM [ManagerUser] WITH(NOLOCK) where managerid=?";
    				
    				prepstmt = connread.prepareStatement(sqlGroupId);
    				prepstmt.setString(1, userid);
    				if(rs.next()){
    					/*返回结果不为空*/
    					groupId = rs.getString("groupId");
    				}else{
    					groupId="-1"; // groupId不存在默认为-1
    				}   				
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			ProxoolManagerRead.release(rs, prepstmt,connread);
    		}
    		System.out.println(rsbool);
    		/*
			 * 开始求悬赏 向AskBegXuanShang插入数据 
			 * */
    		if(rsbool==false){
    			
				String sqlForAskXuanShang ="INSERT INTO  [AskBegXuanShang]  (UserID,AskID,State,AddTime,Source,GroupId)" +
						" values(?,?,?,?,?,?)";
				
				String[] params = new String[]{userid,askid,"-1",sdf.format(new Date()),source,groupId};  // sql语句中相应参数
				int rows=0;   //执行更新数记录
				rows=ProxoolManagerWrite.executeUpdate(sqlForAskXuanShang, params);
				if(rows>0){
					codevalue = "213";
					messagevalue ="申请成功，等待审核";
				}else{
					codevalue = "214";
					messagevalue ="Error：申请失败";
				}
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
		return result;
		
	}
		
	/**
	 * 问题求悬赏接口
	 * @param Userid 用户ID
	 * @param Askid 问题ID
	 * @param Source 平台来源  Int 备注：Web:0;wap:1;App:2;
						搜房帮：3；装修帮：4新房帮：5；ZhuangXiuAPP：6；IPAD=7
	 * @param bestanswerid 最佳回答的ID
	 * @param askuserid 回答用户的ID
	 * @param channelusername 业务线调用的用户名
	 * @param vcode  认证码   备注：（用户名+密码+问题ID的MD5值）
	 * @return xml格式
	 * @throws Exception 
	 * */
	public static String SetBestAnswerForAPP(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pool pools = null;	
		//返回结果
		String result=null;
		//返回结果信息
		String codevalue=null;
		String messagevalue=null;
		// 拼接参数值
		StringBuilder sbuild=new StringBuilder(20);
		Map<String,String> map=StringUtils.getMapOfParas(request);
		// 获取参数，进行参数判断
		String userid=map.get("Userid");
		String askid=map.get("Askid");
		String source=map.get("Source");
		String bestanswerid = map.get("bestanswerid");
		String askuserid = map.get("askuserid");
		String channelusername = map.get("channelusername");
		String vcode = map.get("vcode");
		
		sbuild.append(userid).append(askid).append(source).append(bestanswerid).append(askuserid)
				.append(channelusername).append(vcode);
		System.out.println(sbuild.toString());
		//1.判断参数是否正确
		if(StringUtils.isNull(userid) || StringUtils.isNull(askid) || StringUtils.isNull(source) ||
				StringUtils.isNull(bestanswerid) ||StringUtils.isNull(askuserid) || StringUtils.isNull(channelusername) || StringUtils.isNull(vcode) ||
				StringUtils.checkParamIsInt(sbuild.toString())){
			codevalue="100";
			messagevalue="认证信息错误";
		}else{
			/**
			 * @author 许泽龙
			 * 通过业务线获取对象 查询
			 * SELECT * FROM ChannelUser WHERE ChannelName=@ChannelName
				参数ChannelName 为ChanneluserName
				查询结果如果为 null或id为空则返回 false
				否则 将查询的ChannelName，Password（此密码需要进行解密 解密的key为achsankl），及参数askid 进行MD5 加密
				最后将MD5的值与参数vcode进行对比（注意MD5的大小写要一致） 返回 bool结果
			 * 
			 * */
			pools = new Pool();
			//1.判断认证信息是否合法 .通过业务线获取对象 查询
			String sql = "SELECT * FROM ChannelUser WHERE ChannelName=?";
			String str = null;
        	Connection conn=pools.getConnection();//PoolManagerRead.getDbConnection();
    		PreparedStatement prepstmt=null;
    		ResultSet rs=null;
    		try{
    			prepstmt=conn.prepareStatement(sql);
    			prepstmt.setString(1, channelusername);
    			rs=prepstmt.executeQuery();
    			if(rs.next()){
    				/**返回结果不为空*
    				 * 将查询的ChannelName，Password（此密码需要进行解密 解密的key为achsankl），及参数askid 进行MD5 加密
						最后将MD5的值与参数vcode进行对比（注意MD5的大小写要一致） 返回 bool结果
    				 */
    				pools.returnConnection(conn);
    				conn = pools.getConnection();
    				//4.1通过主键id获得对象  不存在 返回空
    				String  asklistSql= "SELECT * FROM asklist WITH(NOLOCK) WHERE Id=?";
    				prepstmt = conn.prepareStatement(asklistSql);
    				prepstmt.setString(1, askid);
    				rs = prepstmt.executeQuery();
    				
    				/**
    				 * @author 许泽龙
    				 * 使用提出的方法处理
    				 * */
    				if(rs.next()&&Integer.parseInt(rs.getString("id"))>0&&Integer.parseInt(rs.getString("state"))==1){
    					//返回结果不为空进行下一步
    					//4.2 补全问题详细信息 获取问题的详情
    					sql = "SELECT * FROM askdetail WITH(NOLOCK) WHERE Id in (?) order by Id DESC";
    					str = userid;
    					rs = DealWithSql.getBySql(sql, str, rs,conn, prepstmt);
    					if(rs.next()){
    						pools.returnConnection(conn);
    						conn = pools.getConnection();
    						//5.1 通过 BestAnswerId 参数  查询回答的详细信息 ;回答不存在则返回空 
    						sql = "SELECT * FROM asklist WITH(NOLOCK) WHERE Id=?";
        					rs = DealWithSql.getBySql(sql, bestanswerid,rs, conn, prepstmt);
        					//疑问？？？？？？**********************************************
        					//5.2补全问题详细信息  in (?)  将查询出来的数据中的 Content字段的内容要进行关键词 检验
        					if(rs.next()&&Integer.parseInt(rs.getString("id"))>0&&Integer.parseInt(rs.getString("state"))==1){
        						pools.returnConnection(conn);
        						conn = pools.getConnection();
        						sql = "SELECT * FROM askdetail WITH(NOLOCK) WHERE Id in (?) order by Id DESC";
            					str = userid;
            					rs = DealWithSql.getBySql(sql, str,rs, conn, prepstmt);//.executeQuery();
            					if(rs.next()){
            						pools.returnConnection(conn);
            						conn = pools.getConnection();
            						//Content字段的内容要进行关键词
            						//6.1设置最佳答案
            						try{
            							conn.setAutoCommit(false);
            							 //将需要添加事务的代码一同放入try，catch块中
            			                  //创建执行语句
            			                  String sql2 ="UPDATE answerlist SET State=1 WHERE Id= @Id";
            			                  String sql1 = "UPDATE asklist SET State= 1,SearchDate= @SearchDate,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
            			                  String sql3 = "UPDATE askdetail SET Thanks= @Thanks,BestAnswerDate=@BestAnswerDate,BestAnswerUserName=" +
            			                  		"@BestAnswerUserName,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
            			                  //分别执行事务

            			                  prepstmt = conn.prepareStatement(sql2);

            			                  prepstmt.executeUpdate();

            			                  prepstmt = conn.prepareStatement(sql1);

            			                  prepstmt.executeUpdate();
            			                  
            			                  prepstmt = conn.prepareStatement(sql3);

            			                  prepstmt.executeUpdate();
            							conn.commit();
            						}catch(Exception e){
            							conn.rollback();
            						}finally{

            			                  try {

            			                     //设置事务提交方式为自动提交：
            			                     conn.setAutoCommit(true);

            			                  } catch(SQLException e) {
            			                     // TODO Auto-generatedcatch block
            			                     e.printStackTrace();
            			                  }
            			                  //释放数据连接
            			                  pools.returnConnection(conn);
            						}
            						// 7.1 家居评论经验操作 
 	    //http://interface2.ebs.home.fang.com/forZxb3_6_2/CreateExperienceLog.aspx?configid=" + model.Configid + 
        //"&servicesoufunid=" + model.Servicesoufunid + "&logtype=" + model.Logtype + "&questionid=" 
        //+ model.Questionid + "&yzsoufunid=" + model.Yzsoufunid		
            						StringBuffer sb = new StringBuffer("http://interface2.ebs.home.fang.com/forZxb3_6_2/CreateExperienceLog.aspx?");
            						sb.append("configid=1012");
            						sb.append("&servicesoufunid=2");
            						sb.append("&logtype=");
            						sb.append("&questionid=");
            						sb.append("&yzsoufunid=");
            						
            							
            					}else{
            						codevalue="";
                    				messagevalue="回答不存在或者不是该问题的回答";
            					}
            					
            					
        					}else{
        						codevalue="";
                				messagevalue="";
        					}
    					}else{
    						codevalue="103";
            				messagevalue="问题不存在或者已经设置了最佳答案";
    					}
    				}else{
    					//3
    					/**
    					 * 如果取得数据为空或者问题id<0 或者所取数据的state值为1 返回 code=103；ErrorMes = "问题不存在或者已经设置了最佳答案"; 不在执行以
    					 * */
    					codevalue="";
        				messagevalue="";
    				}
    				pools.returnConnection(conn);
//    				return createXml("211","Error:参数有误");
    			}else{
    				//如果为false 返回code=102，message=“认证信息错误”
    				codevalue="102";
    				messagevalue="认证信息错误";
    			}
//    			while(rs.next()){
//    				userId=rs.getString("UserId");
//    				askId=rs.getString("askId");
//    				source=rs.getString("State");
//    			}
    			
    		}catch(Exception e){
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
		return result;
		
	}
	/**
	 * 长尾词投票
	 * @author xzl
	 * @param classid 分类ID 不为空
	 * @param source  来源0-pc 1-wap 2-app 3-搜房帮 4-装修帮 5-新房帮  不为空
	 * @param userid 用户id    				默认:0
	 * @param type  投票类型   1：赞成 0:反对		默认:1
	 * @return xml格式
	 * @throws Exception 
	 * */
	public static String longTailVote(HttpServletRequest request,
			HttpServletResponse response) throws Exception  {
		/*
		 * 	Code：100      message：参数source(来源0-pc 1-wap 2-app 3-搜房帮 4-装修帮 5-新房帮)输入为空，请重新输入！								
			Code：101      message：参数classid输入为空，请重新输入！
			Code:103       message：参数userid不得小于0，请重新输入！或：ClassId不存在，请重新输入！
			Code：109      message：您已经投过赞成票，请勿重复投票
			Code：110      message：您已经投过反对票，请勿重复投票
			Code：111      message：投票成功
			Code：112      message：投票失败
			Code：119      message：ex.Message（异常信息）
		 * */
		//返回结果信息
		String codevalue=null;
		String messagevalue=null;
		Connection connread = null;
		PreparedStatement ps = null ;
		ResultSet rs = null;
		boolean rsNotNull = false;//rs结果集是否为空，true不为空，false为空
		// 拼接参数值
		StringBuffer sb=new StringBuffer("");
		Map<String, String> map= null;
		try {
			map = StringUtils.getMapOfParas(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
			获取参数
		 * { ID = ClassID, UserID = UserId, Vote = Type, Date = DateTime.Now, Source = Source }
		 * */
		String classid = map.get("classid");
		String source = map.get("source");
		String userid = map.get("userid");
		String type = map.get("type");
		//判断参数是否正确
		sb.append(classid).append(source).append(userid).append(type);
		try {
			if(StringUtils.isNull(classid)){
				codevalue="101";
				messagevalue="参数ClassID输入为空，请重新输入！";
				return StringUtils.createXmlForError(codevalue,messagevalue);
			}
			if(StringUtils.isNull(source)){
				codevalue="100";
				messagevalue="参数source(来源0-pc 1-wap 2-app 3-搜房帮 4-装修帮 5-新房帮)输入为空，请重新输入！";
				return StringUtils.createXmlForError(codevalue,messagevalue);
			}
			if(StringUtils.isNull(type)){
				type = "1";
				map.put("type", type);
			}
			if(!StringUtils.isNull(userid)){
				if(userid.contains("-")){
					codevalue="103";
					messagevalue="参数userid不得小于0，请重新输入！或：ClassId不存在，请重新输入！";
					return StringUtils.createXmlForError(codevalue,messagevalue);
				}
				//步骤四.
				/*
				 * 查询是否投过票：
				参数：id为分类id，userid为参数中的userid
				 * */
				connread = ProxoolManagerRead.getDbConnection();
				String weiVoteSql = "SELECT * FROM ChangWeiVote WITH(NOLOCK) WHERE ID=? AND UserID=?";
				ps = connread.prepareStatement(weiVoteSql);
				ps.setString(1, classid);
				ps.setString(2, userid);
				rs = ps.executeQuery();
				rsNotNull = rs.next();
				try{
					if(rsNotNull){
						/*
						 * 查询对象不为空，即已投过票，查询vote属性的值
						如果vote=1返回
						<root>
						<TimeUse></TimeUse>
						<code>109</code>
						<message>您已经投过赞成票，请勿重复投票</message>
						</root>
						如果vote=0返回
						<root>
						<TimeUse></TimeUse>
						<code>110</code>
						<message>您已经投过反对票，请勿重复投票</message>
						</root>
						如果查询对象为空，即没有投过票则重复步骤二中的投票逻辑操作
						投票成功返回
						 * */
						int vote = rs.getInt("vote");
						map.clear();
						if(vote==1){
							map.put("TimeUse", "");
							map.put("code", "109");
							map.put("message", "您已经投过赞成票，请勿重复投票");
						}else if(vote==0){
							map.put("TimeUse", "");
							map.put("code", "110");
							map.put("message", "您已经投过反对票，请勿重复投票");
						}
						return StringUtils.createXml(map);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					ProxoolManagerRead.release(rs, ps, connread);
				}
				//执行insert
				return changeWeVote(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userid="0";
		map.put("userId", userid);
		try {
			return changeWeVote(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2.根据传进来的classid参数查询changweilist表，获取相关长尾词
//		String sqlForLongTail = "Select * FROM ChangWeiList WITH(NOLOCK) WHERE ID= ?";
//		//参数：ID为传进来的分类id
//		int ApproveCount = 0;
//		int OpposeCount = 0;
//		try {
//			ps  = connread.prepareStatement(sqlForLongTail);
//			ps.setString(1, classid);
//			rs = ps.executeQuery();
//			rsNotNull = rs.next();
//			if(rsNotNull){
//				ApproveCount = rs.getInt("ApproveCount");
//				OpposeCount = rs.getInt("OpposeCount");
//			}else{
//				/*
//				 * 
//				 * 查询出的数据为空则返回：
//					<root>
//					<TimeUse></TimeUse>
//					<code>103</code>
//					<message>ClassId不存在，请重新输入！</message>
//					</root>
//				 * */
//				map.put("TimeUse", "");
//				map.put("code", "103");
//				map.put("message", "ClassId不存在，请重新输入！");
//				return StringUtils.createXml(map);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			/*
//			 * <root>
//				<TimeUse></TimeUse>
//				<code>119</code>
//				<message>异常信息</message>
//				</root>
//			 * */
//			map.put("TimeUse", "");
//			map.put("code", "119");
//			map.put("message", "异常信息");
//			try {
//				return StringUtils.createXml(map);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}finally{
//			ProxoolManagerRead.release(rs, ps, connread);
//		}
//			/*userid=0  sqlForLongTail返回结果不为空--
//			 * 或者 userid>0
//			 * SELECT * FROM ChangWeiVote 为空
//
//			 * 查询结果不为空时创建一个changweivote类型的对象，并用参数中的值对其初始化
//			 * 
//			 * ChangWeiVote model = new ChangWeiVote() 
//			 * { ID = ClassID, UserID = UserId, Vote = Type, Date = DateTime.Now, Source = Source };
//			 * */
//			
//			try {
////				map.
//				return changeWeVote(map);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String sql = "INSERT INTO ChangWeiVote (ID,UserID,Vote,Date,Source) VALUES (?,?,?,?,?)";
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String[] args = new String[]{classid,userid,type,sdf.format(new Date()),source};
//			int i = 0;//记录insert 次数
//			int rows = 0;//执行更新数记录
//			while(i<3&&rows==0){
//				rows = ProxoolManagerWrite.executeUpdate(sql, args);//执行更新数记录
//				i++;
//			}
//			if(i<4){
//				String updateSql = null;
//				if(type.equals("1")){
//					/*
//					 * 参数type=1时执行以下语句
//					 * */
//					 updateSql = "UPDATE ChangWeiList SET ApproveCount=? WHERE ID=?";
//					 args =new String[]{ApproveCount+1+"",classid};
//					 ProxoolManagerWrite.executeUpdate(updateSql, args);
//				}else{
//					/*
//					 * 参数type=0时执行以下语句
//				UPDATE ChangWeiList SET OpposeCount=OpposeCount+1 WHERE ID=@ID
//					 * */
//					 updateSql = "UPDATE ChangWeiList SET OpposeCount=? WHERE ID=?";
//					 args =new String[]{OpposeCount+1+"",classid};
//					 ProxoolManagerWrite.executeUpdate(updateSql, args);
//				}
//				map.put("TimeUse", "");
//				map.put("code", "111");
//				map.put("message", "投票成功");
//				
//			}else{
//				map.put("TimeUse", "");
//				map.put("code", "112");
//				map.put("message", "投票失败");
//				}
//			
//		return null;
		map.clear();
		map.put("TimeUse", "");
		map.put("code", "119");
		map.put("message", "异常信息");
		return StringUtils.createXml(map);
	}
	
	private static String changeWeVote(Map<String,String> map) throws Exception{
		/*userid=0  sqlForLongTail返回结果不为空--
		 * 或者 userid>0
		 * SELECT * FROM ChangWeiVote 为空

		 * 查询结果不为空时创建一个changweivote类型的对象，并用参数中的值对其初始化
		 * 
		 * ChangWeiVote model = new ChangWeiVote() 
		 * { ID = ClassID, UserID = UserId, Vote = Type, Date = DateTime.Now, Source = Source };
		 * */
		Map<String,String> xmlParam = new HashMap<String, String>();
		Connection connread = null;
		PreparedStatement ps = null ;
		ResultSet rs = null;
		int ApproveCount = 0;
		int OpposeCount = 0;
		int i = 0;//记录insert 次数
		int rows = 0;//执行更新数记录
		boolean rsNotNull = false;
		String classid = map.get("classid");
		String source = map.get("source");
		String userid = map.get("userid");
		String type = map.get("type");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] args = null;
		try {
			String sqlForLongTail = "Select * FROM ChangWeiList WITH(NOLOCK) WHERE ID= ?";
			//参数：ID为传进来的分类id
			connread = ProxoolManagerRead.getDbConnection();
			ps  = connread.prepareStatement(sqlForLongTail);
			ps.setString(1, classid);
			rs = ps.executeQuery();
			rsNotNull = rs.next();
			if(rsNotNull){
				ApproveCount = rs.getInt("ApproveCount");
				OpposeCount = rs.getInt("OpposeCount");
			}else{
				/*
				 * 
				 * 查询出的数据为空则返回：
					<root>
					<TimeUse></TimeUse>
					<code>103</code>
					<message>ClassId不存在，请重新输入！</message>
					</root>
				 * */
				xmlParam.put("TimeUse", "");
				xmlParam.put("code", "103");
				xmlParam.put("message", "ClassID不存在，请重新输入！");
				return StringUtils.createXml(xmlParam);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*
			 * <root>
				<TimeUse></TimeUse>
				<code>119</code>
				<message>异常信息</message>
				</root>
			 * */
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "119");
			xmlParam.put("message", "异常信息");
			try {
				return StringUtils.createXml(xmlParam);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			ProxoolManagerRead.release(rs, ps, connread);
		}
		
		String sql = "INSERT INTO ChangWeiVote (ID,UserID,Vote,Date,Source) VALUES (?,?,?,?,?)";
		while(i<3&&rows==0){
			args = new String[]{classid,userid,type,sdf.format(new Date()),source};
			rows = ProxoolManagerWrite.executeUpdate(sql, args);//执行更新数记录
			i++;
		}
		if(i<4){
			String updateSql = null;
			if(type.equals("1")){
				/*
				 * 参数type=1时执行以下语句
				 * */
				 updateSql = "UPDATE ChangWeiList SET ApproveCount=? WHERE ID=?";
				 args =new String[]{ApproveCount+1+"",classid};
				 ProxoolManagerWrite.executeUpdate(updateSql, args);
			}else{
				/*
				 * 参数type=0时执行以下语句
			UPDATE ChangWeiList SET OpposeCount=OpposeCount+1 WHERE ID=@ID
				 * */
				 updateSql = "UPDATE ChangWeiList SET OpposeCount=? WHERE ID=?";
				 args =new String[]{OpposeCount+1+"",classid};
				 ProxoolManagerWrite.executeUpdate(updateSql, args);
			}
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "111");
			xmlParam.put("message", "投票成功");
		}else{
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "112");
			xmlParam.put("message", "投票失败");
			}
		return StringUtils.createXml(xmlParam);
	}
}



















