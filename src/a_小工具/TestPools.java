package a_小工具;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;



public class TestPools {
	private static String sql="SELECT TOP 10000 * FROM [Ask_Data_test].[dbo].[answerlist] WHERE city=? and AskId=?";
	public static void main(String[] args) throws Exception {
		
		Connection conn =ConnectPool.getDbConnection();
		PreparedStatement prepstmt = null;
		ResultSet rs=null;
		
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
			Long start=System.currentTimeMillis();
			prepstmt = conn.prepareStatement(sql);
			prepstmt.setString(1, "北京");
			prepstmt.setString(2, "14364855");
			rs = prepstmt.executeQuery();
			Long end=System.currentTimeMillis();
			System.out.println("耗时为："+(end-start));
			while(rs.next()){
				String city=rs.getString("city");
				String AskId=rs.getString("AskId");
//				System.out.println("city=="+city+"  AskId=="+AskId);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
