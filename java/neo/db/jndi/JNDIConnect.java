package neo.db.jndi;

import java.*;

/**
 * 	@Class Name	: 	JNDIConnect.java
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
public class JNDIConnect {	
	private java.sql.Connection con = null;
	private final String url = "jdbc:microsoft:sqlserver://";
	private final String serverName = "localhost";
	private final String portNumber = "1433";
	private final String databaseName = "pubs";
	private final String userName = "user";
	private final String password = "password";
	
	// Informs the driver to use server a side-cursor,
	// which permits more than one active statement
	// on a connection.
	private final String selectMethod = "cursor";

	// Constructor
	public JNDIConnect() {
	}

	private String getConnectionUrl() {
		return 	url + 
					serverName + ":" + portNumber + 
					";databaseName=" + databaseName + 
					";selectMethod=" + selectMethod + ";";
	}

	private java.sql.Connection getConnection() {
		try {
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			con = java.sql.DriverManager.getConnection(getConnectionUrl(), userName, password);
			if (con != null)
				System.out.println("Connection Successful!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Trace in getConnection() : " + e.getMessage());
		}
		return con;
	}

	/*
	 * Display the driver properties, database details
	 */
	public void displayDbProperties() {
		java.sql.DatabaseMetaData dm = null;
		java.sql.ResultSet rs = null;
		try {
			con = this.getConnection();
			if (con != null) {
				dm = con.getMetaData();
				//System.out.println("Driver Information");
				//System.out.println("tDriver Name: " + dm.getDriverName());
				//System.out.println("tDriver Version: " + dm.getDriverVersion());
				//System.out.println("nDatabase Information ");
				//System.out.println("tDatabase Name: " + dm.getDatabaseProductName());
				//System.out.println("tDatabase Version: " + dm.getDatabaseProductVersion());
				//System.out.println("Avalilable Catalogs ");
				rs = dm.getCatalogs();
				while (rs.next()) {
					System.out.println("tcatalog: " + rs.getString(1));
				}
				rs.close();
				rs = null;
				closeConnection();
			} else
				System.out.println("Error: No active Connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dm = null;
	}

	private void closeConnection() {
		try {
			if (con != null)
				con.close();
			con = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		JNDIConnect myDbTest = new JNDIConnect();
		myDbTest.displayDbProperties();
	}
}

/*
* BBS테이블의 CONTENT 필드가 TEXT Type 이라고 할때
-------------------------------------------------------------------------
pstmt = conn.prepareStatement("INSERT INTO BBS(BBS_ID, CONTENT) VALUES(newid(), ?)");
// pstmt.setString(1, content);  16K초과시 오류발생 
InputStream is=new ByteArrayInputStream(content.getBytes()); 
pstmt.setAsciiStream(1, is, content.length()); 
pstmt.executeUpdate(); 
pstmt.close(); 
------------------------------------------------------------------------- 
만약 TEXT 타입이라고 하더라도 16K보다 적은양의 데이타를 저장하는 경우라면 
// pstmt.setString(1, content); 
를 사용하여도 되지만,  
그이상의 정보를 저장해야 한다면 
InputStream is=new ByteArrayInputStream(help_desc.getBytes()); 
pstmt.setAsciiStream(1, is, content.length()); 
를 사용하여야 합니다.
*/