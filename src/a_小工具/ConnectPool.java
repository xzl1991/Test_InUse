package a_小工具;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
public class ConnectPool implements DataSource {


	static Properties p = new Properties();

	/**
	 * 初始化 * @return
	 * 
	 * @throws Exception
	 */
	public static void PoolManagerRead() {

		InputStream in = ConnectPool.class
				.getResourceAsStream("/readdb.properties");

		try {
			p.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Connection getDbConnection() {

		if (p.isEmpty() || null == p) {
			PoolManagerRead();
		}

		Connection conn = null;
		try {
			Class.forName(p.getProperty("driverClassName")).newInstance();
			conn = DriverManager.getConnection(p.getProperty("url"),
					p.getProperty("username"), p.getProperty("password"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	/**
	 * 关闭数据库连接
	 * */
	public static void freeCon(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	// 连接池队列
	private static LinkedList<Connection> pool = new LinkedList<Connection>();
	private static ConnectPool instance = new ConnectPool();

	/**
	 * 获取数据源单例
	 */
	public static ConnectPool instance() {
		if (instance == null)
			instance = new ConnectPool();
		return instance;
	}

	/**
	 * 获取一个数据库连接
	 */
	public Connection getConnection() throws SQLException {
		String url =p.getProperty("url");
		String user =p.getProperty("user");
		String pswd =p.getProperty("pswd");
		synchronized (pool) {
			if (pool.size() > 0) {
				return pool.removeFirst();
			} else {
				return DriverManager.getConnection(url, user, pswd);
			}
		}
	}

	/**
	 * 连接归池，这里的实现思想是使用过的线程入池以备下次使用
	 */
	public static void freeConnection(Connection conn) {
		pool.addLast(conn);
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}

