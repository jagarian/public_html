package neo.db.jndi;

import java.*;

/**
 * 	@Class Name	: 	JNDIConnect.java
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
* BBS���̺��� CONTENT �ʵ尡 TEXT Type �̶�� �Ҷ�
-------------------------------------------------------------------------
pstmt = conn.prepareStatement("INSERT INTO BBS(BBS_ID, CONTENT) VALUES(newid(), ?)");
// pstmt.setString(1, content);  16K�ʰ��� �����߻� 
InputStream is=new ByteArrayInputStream(content.getBytes()); 
pstmt.setAsciiStream(1, is, content.length()); 
pstmt.executeUpdate(); 
pstmt.close(); 
------------------------------------------------------------------------- 
���� TEXT Ÿ���̶�� �ϴ��� 16K���� �������� ����Ÿ�� �����ϴ� ����� 
// pstmt.setString(1, content); 
�� ����Ͽ��� ������,  
���̻��� ������ �����ؾ� �Ѵٸ� 
InputStream is=new ByteArrayInputStream(help_desc.getBytes()); 
pstmt.setAsciiStream(1, is, content.length()); 
�� ����Ͽ��� �մϴ�.
*/