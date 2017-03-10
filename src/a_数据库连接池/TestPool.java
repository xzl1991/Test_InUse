package a_数据库连接池;


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

import a_小工具.ConnectPool;




public class TestPool {
	static Properties p = new Properties();
	private static String sql="SELECT TOP 10000 * FROM [Ask_Data_test].[dbo].[answerlist] WHERE city=? and AskId=?";
	public static void main(String[] args) throws Exception {
		
		Connection conn =ConnectPool.getDbConnection();
		DatabaseMetaData da = conn.getMetaData();//
		
		Savepoint save = conn.setSavepoint("save1");
		System.out.println("支持的事物："+da.supportsTransactions());
		System.out.println("支持的事物："+da.supportsTransactionIsolationLevel(7));
		conn.setTransactionIsolation(1);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		conn.rollback(save);
		
		PreparedStatement prepstmt = null;
		ResultSet rs=null;
		/*************/
		
		//没有使用连接池
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
		System.out.println("没有使用连接池花费时间1："+(System.currentTimeMillis()-start));
		//没有使用连接池
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
		System.out.println("没有使用连接池花费时间："+(System.currentTimeMillis()-start));
		//使用连接池
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
		System.out.println("使用连接池花费时间："+(System.currentTimeMillis()-start));
		
		
		//使用连接池2
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
		System.out.println("使用连接池花费时间2："+(System.currentTimeMillis()-start));
		
		
		/*********************/
		try{
			conn.setAutoCommit(false);
			 //将需要添加事务的代码一同放入try，catch块中
              //创建执行语句

              String sql2 ="UPDATE answerlist SET State=911 WHERE Id= '14364855'";

              String sql1 = "UPDATE asklist SET ClassId=222 WHERE Id= '143648551'";
              String sql3 = "UPDATE askdetail SET Thanks= @Thanks,BestAnswerDate=@BestAnswerDate,BestAnswerUserName=" +
              		"@BestAnswerUserName,BestAnswerUserId=@BestAnswerUserId WHERE Id= @Id";
              //分别执行事务

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

                 //设置事务提交方式为自动提交：

                 conn.setAutoCommit(true);

              } catch(SQLException e) {
                 // TODO Auto-generatedcatch block
                 e.printStackTrace();
              }
		}
		
		try {
			 start=System.currentTimeMillis();
			prepstmt = conn.prepareStatement(sql);
			prepstmt.setString(1, "北京");
			prepstmt.setString(2, "14364855");
			rs = prepstmt.executeQuery();
			Long end=System.currentTimeMillis();
			System.out.println("耗时为："+(end-start));
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
