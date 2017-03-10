package a_数据库连接池;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import a_小工具.ConnectPool;

public class TestSql {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		ConnectPool.PoolManagerRead();
		Connection conn = ConnectPool.instance().getConnection();
		 PreparedStatement pstmt = conn
			.prepareStatement("select top 3* from answerlist WHERE Id=?");
		pstmt.setString(1, "14364855");
		ResultSet rs = pstmt.executeQuery();
		List<String> ls = null;
		if(rs.next()){
			ls = (List<String>) rs.getArray(1);
		}
	}

}



















