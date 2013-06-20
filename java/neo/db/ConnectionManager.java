package neo.db;

import java.sql.Connection;
import java.sql.SQLException;

import neo.db.DataSourcePool;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	ConnectionManager.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class ConnectionManager {

	public static Connection getConnection() throws SQLException {
		Connection conn = DataSourcePool.getInstance().getConnection();
		conn.setAutoCommit(true);
		return conn;
	}

	public static Connection getConnection(String dataSourceSpec)
		throws SQLException {
		Connection conn = DataSourcePool.getInstance().getConnection(dataSourceSpec);
		conn.setAutoCommit(true);
		return conn;
	}
	
	public static DataSourcePool getDataSourcePool()
		throws SQLException {
		return DataSourcePool.getInstance();
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException se) {
			Log.info(se.toString(), null);
		}
	}
}