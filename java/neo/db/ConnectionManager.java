package neo.db;

import java.sql.Connection;
import java.sql.SQLException;

import neo.db.DataSourcePool;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	ConnectionManager.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
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