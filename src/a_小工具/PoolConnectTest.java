package a_小工具;

import java.sql.Connection;

public class PoolConnectTest {
	public static void main(String[] args) {
		PoolConnect.setUrl("jdbc:jtds:sqlserver://123.103.36.11;DatabaseName=Ask_Data_test");
		PoolConnect.setUser("Ask_Data_test_admin");
		PoolConnect.setPassword("37aa773B");

		Connection con = PoolConnect.getConnection();
		Connection con1 = PoolConnect.getConnection();
		Connection con2 = PoolConnect.getConnection();

		// do something with con ...

		try {
			con.close();
		} catch (Exception e) {
		}

		try {
			con1.close();
		} catch (Exception e) {
		}

		try {
			con2.close();
		} catch (Exception e) {
		}

		con = PoolConnect.getConnection();
		con1 = PoolConnect.getConnection();
		try {
			con1.close();
		} catch (Exception e) {
		}

		con2 = PoolConnect.getConnection();
		PoolConnect.printDebugMsg();

	}
}