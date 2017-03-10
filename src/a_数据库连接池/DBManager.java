package a_数据库连接池;
	import java.sql.ResultSet;
import java.sql.SQLException;


import a_数据库连接池.ConnectionPool.PooledConnection;

	public class DBManager {

		private static PooledConnection conn;
		private static ConnectionPool connectionPool;
		private static DBManager inst;

		public void close() {
			try {
				connectionPool.closeConnectionPool();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public DBManager() {
			if (inst != null)
				return;

			// TODO Auto-generated constructor stub

//			String connStr = String.format("jdbc:mysql://%s:%d/%s", Config.getInstance().mysqlHost, Config.getInstance().mysqlPort,
//					Config.getInstance().mysqlDB);
//			connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, Config.getInstance().mysqlUser, Config.getInstance().mysqlPassword);
			connectionPool = new ConnectionPool("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://123.103.36.11;DatabaseName=Ask_Data_test", "Ask_Data_test_admin","37aa773B");
			try {
				connectionPool.createPool();
				inst = this;
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public static PooledConnection getConnection() {
			if (inst == null)
				new DBManager();

			try {
				
				conn = connectionPool.getConnection();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return conn;
		}

		public static void main(String[] args) {
			 String sql = "select top 3* from answerlist ";
				ResultSet rs = null;
				PooledConnection conn = null;
				try {
					conn = DBManager.getConnection();
					rs = conn.executeQuery(sql);
					if (rs.next()){
						System.out.println(rs.getString("city"));
					}
					return;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					conn.close();
				}
		}

	}
