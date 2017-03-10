package a_���ݿ����ӳ�;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Properties;

import a_С����.ConnectPool;




public class TestPool {
	static Properties p = new Properties();
	private static String sql="SELECT TOP 10000 * FROM [Ask_Data_test].[dbo].[answerlist] WHERE city=? and AskId=?";
	public static void main(String[] args) throws Exception {
		
		Connection conn =ConnectPool.getDbConnection();
		DatabaseMetaData da = conn.getMetaData();//
		
		Savepoint save = conn.setSavepoint("save1");
		System.out.println("֧�ֵ����"+da.supportsTransactions());
		System.out.println("֧�ֵ����"+da.supportsTransactionIsolationLevel(7));
		conn.setTransactionIsolation(1);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		conn.rollback(save);
		
		PreparedStatement prepstmt = null;
		ResultSet rs=null;
		/*************/
		
		//û��ʹ�����ӳ�
		long start=System.currentTimeMillis();
		ConnectPool.PoolManagerRead();
		String url ="jdbc:jtds:sqlserver://123.103.36.11;DatabaseName=Ask_Data_test";
		String user ="Ask_Data_test_admin";
		String pswd ="37aa773B";
		for (int i = 0; i < 100; i++) {
		   conn =  DriverManager.getConnection(url, user, pswd);
		   prepstmt = conn.prepareStatement("select top 100* from answerlist WHERE Id=?");
		   prepstmt.setString(1, "14364855");
		   rs = prepstmt.executeQuery();
//		   while(rs.next()){
//				String city=rs.getString("city");
//				String AskId=rs.getString("AskId");
//				System.out.println("city=="+city+"  AskId=="+AskId);
//			}
		   prepstmt.close();
			conn.close();
		}
		System.out.println("û��ʹ�����ӳػ���ʱ��1��"+(System.currentTimeMillis()-start));
		//û��ʹ�����ӳ�
		 start=System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			conn = ConnectPool.getDbConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select top 100* from answerlist WHERE Id=?");
			pstmt.setString(1, "14364855");
			rs = pstmt.executeQuery();
//			 while(rs.next()){
//					String city=rs.getString("city");
//					String AskId=rs.getString("AskId");
//					System.out.println("city=="+city+"  AskId=="+AskId);
//				}
			pstmt.close();
			conn.close();
		}
		System.out.println("û��ʹ�����ӳػ���ʱ�䣺"+(System.currentTimeMillis()-start));
		//ʹ�����ӳ�
		start=System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			 conn=ConnectPool.instance().getConnection();
			 PreparedStatement pstmt = conn
				.prepareStatement("select top 100* from answerlist WHERE Id=?");
			pstmt.setString(1, "14364855");
			rs = pstmt.executeQuery();
//			 while(rs.next()){
//					String city=rs.getString("city");
//					String AskId=rs.getString("AskId");
//					System.out.println("city=="+city+"  AskId=="+AskId);
//				}
			pstmt.close();
			ConnectPool.freeConnection(conn);
//			ConnectPool.freeRs(rs);
//			ConnectPool.freePs(prepstmt);
//			ConnectPool.freeCon(conn);
		
		}
		System.out.println("ʹ�����ӳػ���ʱ�䣺"+(System.currentTimeMillis()-start));
		
		
		//ʹ�����ӳ�2
		start=System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			 conn=ConnectPool.instance().getConnection();
			 PreparedStatement pstmt = conn
				.prepareStatement("select top 100* from answerlist WHERE Id=?");
			pstmt.setString(1, "14364855");
			rs = pstmt.executeQuery();
//			 while(rs.next()){
//					String city=rs.getString("city");
//					String AskId=rs.getString("AskId");
//					System.out.println("city=="+city+"  AskId=="+AskId);
//				}
			pstmt.close();
			ConnectPool.freeConnection(conn);
//			ConnectPool.freeRs(rs);
//			ConnectPool.freePs(prepstmt);
//			ConnectPool.freeCon(conn);
		
		}
		System.out.println("ʹ�����ӳػ���ʱ��2��"+(System.currentTimeMillis()-start));
		
		
		/*********************/
		try{
			conn.setAutoCommit(false);
			 //����Ҫ�������Ĵ���һͬ����try��catch����
              //����ִ�����

              String sql2 ="UPDATE answerlist SET State=911 WHERE Id= '14364855'";

              String sql1 = "UPDATE asklist SET ClassId=222 WHERE Id= '143648551'";
              String sql3 = "UPDATE askdetail SET Thanks= @Thanks,BestAnswerDate=@BestAnswerDate,BestAnswerUserName=" +
              		"@BestAnswerUserName,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
              //�ֱ�ִ������

              prepstmt = conn.prepareStatement(sql1);

              prepstmt.executeUpdate();
//
              prepstmt = conn.prepareStatement(sql2);

              prepstmt.executeUpdate();
//              
//              prepstmt = conn.prepareStatement(sql3);

//              prepstmt.executeUpdate();
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
		}
		
		try {
			 start=System.currentTimeMillis();
			prepstmt = conn.prepareStatement(sql);
			prepstmt.setString(1, "����");
			prepstmt.setString(2, "14364855");
			rs = prepstmt.executeQuery();
			Long end=System.currentTimeMillis();
			System.out.println("��ʱΪ��"+(end-start));
//			while(rs.next()){
//				String city=rs.getString("city");
//				String AskId=rs.getString("AskId");
////				System.out.println("city=="+city+"  AskId=="+AskId);
//			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
