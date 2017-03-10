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
	 * ���������ͽӿ�
	 * @param Userid �û�ID
	 * @param Askid ����ID
	 * @param Source �û���Դ  �޶����� 0=pc 1=wap 2=��APP
	 * @return xml��ʽ
	 * @throws Exception 
	 * */
	
	public static String CommonBegXuanShang(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String result="";
		//���ؽ����Ϣ
		String codevalue="";
		String messagevalue="";
		// ƴ�Ӳ���ֵ
		StringBuilder sbuild=new StringBuilder(20);
		Map<String,String> map=StringUtils.getMapOfParas(request);
		// ��ȡ���������в����ж�
		String userid=map.get("Userid");
		String askid=map.get("Askid");
		String source=map.get("Source");
		sbuild.append(userid).append(askid).append(source);
		if(StringUtils.isNull(userid) || StringUtils.isNull(askid) || StringUtils.isNull(source) || StringUtils.checkParamIsInt(sbuild.toString())){
			codevalue="211";
			messagevalue="Error:��������";
		}else{
    		//���ʱ�����ʱ��
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String sql = "SELECT * FROM [AskBegXuanShang] WITH(NOLOCK) WHERE UserID=?   AND AddTime<=?"
        		 + " AND AddTime>?"; 
        	
        	Connection connread=null;   //�����ݿ�����
    		PreparedStatement prepstmt=null;
    		ResultSet rs=null;
    		boolean rsbool=false;  //rs������Ƿ�Ϊ�գ�true��Ϊ�գ�falseΪ��
    		String groupId = null; //�ж��û��Ƿ��Ǿ�����
    		try{
    			connread = ProxoolManagerRead.getDbConnection();
    			prepstmt=connread.prepareStatement(sql);
    			prepstmt.setString(1, userid);
    			prepstmt.setString(2,sdf.format(new Date()));
    			prepstmt.setString(3,StringUtils.getMondayOfThisWeek());
    			rs=prepstmt.executeQuery();
    			rsbool=rs.next();
    			/*
    			 * ��ѯAskBegXuanShang�� ��֤���û��������Ƿ���������͡�һ���û�һ��ֻ��������һ�Ρ�
    			 */
    			if(rsbool){
    				//���ؽ����Ϊ��
    				codevalue="212";
    				messagevalue="Error:���û���������������ͣ�";
    			}else{
    				/*
    				 * ����userid �� manageruser�����ж��û��Ƿ��Ǿ����˴��ڷ���GroupId ������Ĭ��-1
    				 * */
    				String sqlGroupId = "SELECT groupid  FROM [ManagerUser] WITH(NOLOCK) where managerid=?";
    				
    				prepstmt = connread.prepareStatement(sqlGroupId);
    				prepstmt.setString(1, userid);
    				if(rs.next()){
    					/*���ؽ����Ϊ��*/
    					groupId = rs.getString("groupId");
    				}else{
    					groupId="-1"; // groupId������Ĭ��Ϊ-1
    				}   				
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			ProxoolManagerRead.release(rs, prepstmt,connread);
    		}
    		System.out.println(rsbool);
    		/*
			 * ��ʼ������ ��AskBegXuanShang�������� 
			 * */
    		if(rsbool==false){
    			
				String sqlForAskXuanShang ="INSERT INTO  [AskBegXuanShang]  (UserID,AskID,State,AddTime,Source,GroupId)" +
						" values(?,?,?,?,?,?)";
				
				String[] params = new String[]{userid,askid,"-1",sdf.format(new Date()),source,groupId};  // sql�������Ӧ����
				int rows=0;   //ִ�и�������¼
				rows=ProxoolManagerWrite.executeUpdate(sqlForAskXuanShang, params);
				if(rows>0){
					codevalue = "213";
					messagevalue ="����ɹ����ȴ����";
				}else{
					codevalue = "214";
					messagevalue ="Error������ʧ��";
				}
    		}
	}
		// ����xml�ļ�
		// ���ڵ�
		Element root=new Element("root");
		// xml�ĵ�
		Document doc=new Document(root);
		// ���ڵ��µ��ӽڵ�
		Element code=new Element("code");
		code.addContent(new CDATA(codevalue));
		root.addContent(code);
		if(StringUtils.isNull(messagevalue)){
			
		}else{
			Element message=new Element("message");
			message.addContent(new CDATA(messagevalue));
			root.addContent(message);
		}

		// ���xml����
		result=StringUtils.xmltostring(doc);
		return result;
		
	}
		
	/**
	 * ���������ͽӿ�
	 * @param Userid �û�ID
	 * @param Askid ����ID
	 * @param Source ƽ̨��Դ  Int ��ע��Web:0;wap:1;App:2;
						�ѷ��3��װ�ް4�·��5��ZhuangXiuAPP��6��IPAD=7
	 * @param bestanswerid ��ѻش��ID
	 * @param askuserid �ش��û���ID
	 * @param channelusername ҵ���ߵ��õ��û���
	 * @param vcode  ��֤��   ��ע�����û���+����+����ID��MD5ֵ��
	 * @return xml��ʽ
	 * @throws Exception 
	 * */
	public static String SetBestAnswerForAPP(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pool pools = null;	
		//���ؽ��
		String result=null;
		//���ؽ����Ϣ
		String codevalue=null;
		String messagevalue=null;
		// ƴ�Ӳ���ֵ
		StringBuilder sbuild=new StringBuilder(20);
		Map<String,String> map=StringUtils.getMapOfParas(request);
		// ��ȡ���������в����ж�
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
		//1.�жϲ����Ƿ���ȷ
		if(StringUtils.isNull(userid) || StringUtils.isNull(askid) || StringUtils.isNull(source) ||
				StringUtils.isNull(bestanswerid) ||StringUtils.isNull(askuserid) || StringUtils.isNull(channelusername) || StringUtils.isNull(vcode) ||
				StringUtils.checkParamIsInt(sbuild.toString())){
			codevalue="100";
			messagevalue="��֤��Ϣ����";
		}else{
			/**
			 * @author ������
			 * ͨ��ҵ���߻�ȡ���� ��ѯ
			 * SELECT * FROM ChannelUser WHERE ChannelName=@ChannelName
				����ChannelName ΪChanneluserName
				��ѯ������Ϊ null��idΪ���򷵻� false
				���� ����ѯ��ChannelName��Password����������Ҫ���н��� ���ܵ�keyΪachsankl����������askid ����MD5 ����
				���MD5��ֵ�����vcode���жԱȣ�ע��MD5�Ĵ�СдҪһ�£� ���� bool���
			 * 
			 * */
			pools = new Pool();
			//1.�ж���֤��Ϣ�Ƿ�Ϸ� .ͨ��ҵ���߻�ȡ���� ��ѯ
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
    				/**���ؽ����Ϊ��*
    				 * ����ѯ��ChannelName��Password����������Ҫ���н��� ���ܵ�keyΪachsankl����������askid ����MD5 ����
						���MD5��ֵ�����vcode���жԱȣ�ע��MD5�Ĵ�СдҪһ�£� ���� bool���
    				 */
    				pools.returnConnection(conn);
    				conn = pools.getConnection();
    				//4.1ͨ������id��ö���  ������ ���ؿ�
    				String  asklistSql= "SELECT * FROM asklist WITH(NOLOCK) WHERE Id=?";
    				prepstmt = conn.prepareStatement(asklistSql);
    				prepstmt.setString(1, askid);
    				rs = prepstmt.executeQuery();
    				
    				/**
    				 * @author ������
    				 * ʹ������ķ�������
    				 * */
    				if(rs.next()&&Integer.parseInt(rs.getString("id"))>0&&Integer.parseInt(rs.getString("state"))==1){
    					//���ؽ����Ϊ�ս�����һ��
    					//4.2 ��ȫ������ϸ��Ϣ ��ȡ���������
    					sql = "SELECT * FROM askdetail WITH(NOLOCK) WHERE Id in (?) order by Id DESC";
    					str = userid;
    					rs = DealWithSql.getBySql(sql, str, rs,conn, prepstmt);
    					if(rs.next()){
    						pools.returnConnection(conn);
    						conn = pools.getConnection();
    						//5.1 ͨ�� BestAnswerId ����  ��ѯ�ش����ϸ��Ϣ ;�ش𲻴����򷵻ؿ� 
    						sql = "SELECT * FROM asklist WITH(NOLOCK) WHERE Id=?";
        					rs = DealWithSql.getBySql(sql, bestanswerid,rs, conn, prepstmt);
        					//���ʣ�����������**********************************************
        					//5.2��ȫ������ϸ��Ϣ  in (?)  ����ѯ�����������е� Content�ֶε�����Ҫ���йؼ��� ����
        					if(rs.next()&&Integer.parseInt(rs.getString("id"))>0&&Integer.parseInt(rs.getString("state"))==1){
        						pools.returnConnection(conn);
        						conn = pools.getConnection();
        						sql = "SELECT * FROM askdetail WITH(NOLOCK) WHERE Id in (?) order by Id DESC";
            					str = userid;
            					rs = DealWithSql.getBySql(sql, str,rs, conn, prepstmt);//.executeQuery();
            					if(rs.next()){
            						pools.returnConnection(conn);
            						conn = pools.getConnection();
            						//Content�ֶε�����Ҫ���йؼ���
            						//6.1������Ѵ�
            						try{
            							conn.setAutoCommit(false);
            							 //����Ҫ�������Ĵ���һͬ����try��catch����
            			                  //����ִ�����
            			                  String sql2 ="UPDATE answerlist SET State=1 WHERE Id= @Id";
            			                  String sql1 = "UPDATE asklist SET State= 1,SearchDate= @SearchDate,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
            			                  String sql3 = "UPDATE askdetail SET Thanks= @Thanks,BestAnswerDate=@BestAnswerDate,BestAnswerUserName=" +
            			                  		"@BestAnswerUserName,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
            			                  //�ֱ�ִ������

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

            			                     //���������ύ��ʽΪ�Զ��ύ��
            			                     conn.setAutoCommit(true);

            			                  } catch(SQLException e) {
            			                     // TODO Auto-generatedcatch block
            			                     e.printStackTrace();
            			                  }
            			                  //�ͷ���������
            			                  pools.returnConnection(conn);
            						}
            						// 7.1 �Ҿ����۾������ 
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
                    				messagevalue="�ش𲻴��ڻ��߲��Ǹ�����Ļش�";
            					}
            					
            					
        					}else{
        						codevalue="";
                				messagevalue="";
        					}
    					}else{
    						codevalue="103";
            				messagevalue="���ⲻ���ڻ����Ѿ���������Ѵ�";
    					}
    				}else{
    					//3
    					/**
    					 * ���ȡ������Ϊ�ջ�������id<0 ������ȡ���ݵ�stateֵΪ1 ���� code=103��ErrorMes = "���ⲻ���ڻ����Ѿ���������Ѵ�"; ����ִ����
    					 * */
    					codevalue="";
        				messagevalue="";
    				}
    				pools.returnConnection(conn);
//    				return createXml("211","Error:��������");
    			}else{
    				//���Ϊfalse ����code=102��message=����֤��Ϣ����
    				codevalue="102";
    				messagevalue="��֤��Ϣ����";
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
		// ����xml�ļ�
		// ���ڵ�
		Element root=new Element("root");
		// xml�ĵ�
		Document doc=new Document(root);
		// ���ڵ��µ��ӽڵ�
		Element code=new Element("code");
		code.addContent(new CDATA(codevalue));
		root.addContent(code);
		if(StringUtils.isNull(messagevalue)){
			
		}else{
			Element message=new Element("message");
			message.addContent(new CDATA(messagevalue));
			root.addContent(message);
		}

		// ���xml����
		result=StringUtils.xmltostring(doc);
		return result;
		
	}
	/**
	 * ��β��ͶƱ
	 * @author xzl
	 * @param classid ����ID ��Ϊ��
	 * @param source  ��Դ0-pc 1-wap 2-app 3-�ѷ��� 4-װ�ް� 5-�·���  ��Ϊ��
	 * @param userid �û�id    				Ĭ��:0
	 * @param type  ͶƱ����   1���޳� 0:����		Ĭ��:1
	 * @return xml��ʽ
	 * @throws Exception 
	 * */
	public static String longTailVote(HttpServletRequest request,
			HttpServletResponse response) throws Exception  {
		/*
		 * 	Code��100      message������source(��Դ0-pc 1-wap 2-app 3-�ѷ��� 4-װ�ް� 5-�·���)����Ϊ�գ����������룡								
			Code��101      message������classid����Ϊ�գ����������룡
			Code:103       message������userid����С��0�����������룡��ClassId�����ڣ����������룡
			Code��109      message�����Ѿ�Ͷ���޳�Ʊ�������ظ�ͶƱ
			Code��110      message�����Ѿ�Ͷ������Ʊ�������ظ�ͶƱ
			Code��111      message��ͶƱ�ɹ�
			Code��112      message��ͶƱʧ��
			Code��119      message��ex.Message���쳣��Ϣ��
		 * */
		//���ؽ����Ϣ
		String codevalue=null;
		String messagevalue=null;
		Connection connread = null;
		PreparedStatement ps = null ;
		ResultSet rs = null;
		boolean rsNotNull = false;//rs������Ƿ�Ϊ�գ�true��Ϊ�գ�falseΪ��
		// ƴ�Ӳ���ֵ
		StringBuffer sb=new StringBuffer("");
		Map<String, String> map= null;
		try {
			map = StringUtils.getMapOfParas(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
			��ȡ����
		 * { ID = ClassID, UserID = UserId, Vote = Type, Date = DateTime.Now, Source = Source }
		 * */
		String classid = map.get("classid");
		String source = map.get("source");
		String userid = map.get("userid");
		String type = map.get("type");
		//�жϲ����Ƿ���ȷ
		sb.append(classid).append(source).append(userid).append(type);
		try {
			if(StringUtils.isNull(classid)){
				codevalue="101";
				messagevalue="����ClassID����Ϊ�գ����������룡";
				return StringUtils.createXmlForError(codevalue,messagevalue);
			}
			if(StringUtils.isNull(source)){
				codevalue="100";
				messagevalue="����source(��Դ0-pc 1-wap 2-app 3-�ѷ��� 4-װ�ް� 5-�·���)����Ϊ�գ����������룡";
				return StringUtils.createXmlForError(codevalue,messagevalue);
			}
			if(StringUtils.isNull(type)){
				type = "1";
				map.put("type", type);
			}
			if(!StringUtils.isNull(userid)){
				if(userid.contains("-")){
					codevalue="103";
					messagevalue="����userid����С��0�����������룡��ClassId�����ڣ����������룡";
					return StringUtils.createXmlForError(codevalue,messagevalue);
				}
				//������.
				/*
				 * ��ѯ�Ƿ�Ͷ��Ʊ��
				������idΪ����id��useridΪ�����е�userid
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
						 * ��ѯ����Ϊ�գ�����Ͷ��Ʊ����ѯvote���Ե�ֵ
						���vote=1����
						<root>
						<TimeUse></TimeUse>
						<code>109</code>
						<message>���Ѿ�Ͷ���޳�Ʊ�������ظ�ͶƱ</message>
						</root>
						���vote=0����
						<root>
						<TimeUse></TimeUse>
						<code>110</code>
						<message>���Ѿ�Ͷ������Ʊ�������ظ�ͶƱ</message>
						</root>
						�����ѯ����Ϊ�գ���û��Ͷ��Ʊ���ظ�������е�ͶƱ�߼�����
						ͶƱ�ɹ�����
						 * */
						int vote = rs.getInt("vote");
						map.clear();
						if(vote==1){
							map.put("TimeUse", "");
							map.put("code", "109");
							map.put("message", "���Ѿ�Ͷ���޳�Ʊ�������ظ�ͶƱ");
						}else if(vote==0){
							map.put("TimeUse", "");
							map.put("code", "110");
							map.put("message", "���Ѿ�Ͷ������Ʊ�������ظ�ͶƱ");
						}
						return StringUtils.createXml(map);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					ProxoolManagerRead.release(rs, ps, connread);
				}
				//ִ��insert
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
		//2.���ݴ�������classid������ѯchangweilist����ȡ��س�β��
//		String sqlForLongTail = "Select * FROM ChangWeiList WITH(NOLOCK) WHERE ID= ?";
//		//������IDΪ�������ķ���id
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
//				 * ��ѯ��������Ϊ���򷵻أ�
//					<root>
//					<TimeUse></TimeUse>
//					<code>103</code>
//					<message>ClassId�����ڣ����������룡</message>
//					</root>
//				 * */
//				map.put("TimeUse", "");
//				map.put("code", "103");
//				map.put("message", "ClassId�����ڣ����������룡");
//				return StringUtils.createXml(map);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			/*
//			 * <root>
//				<TimeUse></TimeUse>
//				<code>119</code>
//				<message>�쳣��Ϣ</message>
//				</root>
//			 * */
//			map.put("TimeUse", "");
//			map.put("code", "119");
//			map.put("message", "�쳣��Ϣ");
//			try {
//				return StringUtils.createXml(map);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}finally{
//			ProxoolManagerRead.release(rs, ps, connread);
//		}
//			/*userid=0  sqlForLongTail���ؽ����Ϊ��--
//			 * ���� userid>0
//			 * SELECT * FROM ChangWeiVote Ϊ��
//
//			 * ��ѯ�����Ϊ��ʱ����һ��changweivote���͵Ķ��󣬲��ò����е�ֵ�����ʼ��
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
//			int i = 0;//��¼insert ����
//			int rows = 0;//ִ�и�������¼
//			while(i<3&&rows==0){
//				rows = ProxoolManagerWrite.executeUpdate(sql, args);//ִ�и�������¼
//				i++;
//			}
//			if(i<4){
//				String updateSql = null;
//				if(type.equals("1")){
//					/*
//					 * ����type=1ʱִ���������
//					 * */
//					 updateSql = "UPDATE ChangWeiList SET ApproveCount=? WHERE ID=?";
//					 args =new String[]{ApproveCount+1+"",classid};
//					 ProxoolManagerWrite.executeUpdate(updateSql, args);
//				}else{
//					/*
//					 * ����type=0ʱִ���������
//				UPDATE ChangWeiList SET OpposeCount=OpposeCount+1 WHERE ID=@ID
//					 * */
//					 updateSql = "UPDATE ChangWeiList SET OpposeCount=? WHERE ID=?";
//					 args =new String[]{OpposeCount+1+"",classid};
//					 ProxoolManagerWrite.executeUpdate(updateSql, args);
//				}
//				map.put("TimeUse", "");
//				map.put("code", "111");
//				map.put("message", "ͶƱ�ɹ�");
//				
//			}else{
//				map.put("TimeUse", "");
//				map.put("code", "112");
//				map.put("message", "ͶƱʧ��");
//				}
//			
//		return null;
		map.clear();
		map.put("TimeUse", "");
		map.put("code", "119");
		map.put("message", "�쳣��Ϣ");
		return StringUtils.createXml(map);
	}
	
	private static String changeWeVote(Map<String,String> map) throws Exception{
		/*userid=0  sqlForLongTail���ؽ����Ϊ��--
		 * ���� userid>0
		 * SELECT * FROM ChangWeiVote Ϊ��

		 * ��ѯ�����Ϊ��ʱ����һ��changweivote���͵Ķ��󣬲��ò����е�ֵ�����ʼ��
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
		int i = 0;//��¼insert ����
		int rows = 0;//ִ�и�������¼
		boolean rsNotNull = false;
		String classid = map.get("classid");
		String source = map.get("source");
		String userid = map.get("userid");
		String type = map.get("type");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] args = null;
		try {
			String sqlForLongTail = "Select * FROM ChangWeiList WITH(NOLOCK) WHERE ID= ?";
			//������IDΪ�������ķ���id
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
				 * ��ѯ��������Ϊ���򷵻أ�
					<root>
					<TimeUse></TimeUse>
					<code>103</code>
					<message>ClassId�����ڣ����������룡</message>
					</root>
				 * */
				xmlParam.put("TimeUse", "");
				xmlParam.put("code", "103");
				xmlParam.put("message", "ClassID�����ڣ����������룡");
				return StringUtils.createXml(xmlParam);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*
			 * <root>
				<TimeUse></TimeUse>
				<code>119</code>
				<message>�쳣��Ϣ</message>
				</root>
			 * */
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "119");
			xmlParam.put("message", "�쳣��Ϣ");
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
			rows = ProxoolManagerWrite.executeUpdate(sql, args);//ִ�и�������¼
			i++;
		}
		if(i<4){
			String updateSql = null;
			if(type.equals("1")){
				/*
				 * ����type=1ʱִ���������
				 * */
				 updateSql = "UPDATE ChangWeiList SET ApproveCount=? WHERE ID=?";
				 args =new String[]{ApproveCount+1+"",classid};
				 ProxoolManagerWrite.executeUpdate(updateSql, args);
			}else{
				/*
				 * ����type=0ʱִ���������
			UPDATE ChangWeiList SET OpposeCount=OpposeCount+1 WHERE ID=@ID
				 * */
				 updateSql = "UPDATE ChangWeiList SET OpposeCount=? WHERE ID=?";
				 args =new String[]{OpposeCount+1+"",classid};
				 ProxoolManagerWrite.executeUpdate(updateSql, args);
			}
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "111");
			xmlParam.put("message", "ͶƱ�ɹ�");
		}else{
			xmlParam.put("TimeUse", "");
			xmlParam.put("code", "112");
			xmlParam.put("message", "ͶƱʧ��");
			}
		return StringUtils.createXml(xmlParam);
	}
}



















