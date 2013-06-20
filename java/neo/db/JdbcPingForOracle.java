package neo.db;

import java.sql.*;
import java.util.Enumeration;

/**
 * 	@Class Name	: 	JdbcPingForOracle.java
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
public class JdbcPingForOracle
{
	private static final String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
	private static final String jdbcUrl = "jdbc:oracle:thin:@127.0.0.1:1521:wind";
    // Strings to be added in the WORD column of the table.
    private static final String words[] = 
    		{	"One",      "Two",      "Three",    "Four",     "Five",
            	"Six",      "Seven",    "Eight",    "Nine",     "Ten",
            	"Eleven",   "Twelve",   "Thirteen", "Fourteen", "Fifteen",
            	"Sixteen",  "Seventeen","Eighteen", "Nineteen", "Twenty" };

    public static String run (String url, String system, String user_id, String password, String endDelimitor)
    {
        String tableName		= "test";
        String addStr 			= endDelimitor + "====================Start====================" + endDelimitor;
        Connection connection 	= null;

        try {
        	
        	addStr += "■ ORACLE jdbc test"+endDelimitor;

            // Load Java JDBC driver.
            ClassLoader classLoader =	Thread.currentThread().getContextClassLoader(); 
            classLoader.loadClass(jdbcDriver).newInstance();
            addStr += ">class loader ok..."+endDelimitor;
            
            // Get a connection to the database.
            connection = DriverManager.getConnection (url+system,user_id,password);
            addStr += ">connection successful ok..."+"("+url+system+")"+endDelimitor;
            
            DatabaseMetaData  dbMetaData = connection.getMetaData();
            DBUtil.toStringMetadata(dbMetaData, endDelimitor);
            
            Enumeration Drv = DriverManager.getDrivers();
            int cnt = 0;
            while(Drv.hasMoreElements()) {
            	addStr += "> ["+(cnt++)+"]" + Drv.nextElement() + endDelimitor;
            }
            
            // Drop the table if it already exists.
            Statement stmt = connection.createStatement();
                
            int ret = 0;
            ret = stmt.executeUpdate ("drop table " + tableName);
            addStr += ">test table drop ok...["+ret+"]" + endDelimitor;
            
            // Create the table.
            stmt.executeUpdate ("CREATE TABLE " + tableName + " (I int, WORD VARCHAR(20), SQUARE int, SQUAREROOT int) ");
            addStr += ">table creation ok..." + endDelimitor;

            // Prepare a statement for inserting rows.  Since we
            // execute this multiple times, it is best to use a
            // PreparedStatement and parameter markers.
            PreparedStatement pstmt = connection.prepareStatement ("INSERT INTO "
            													+ tableName + " (I, WORD, SQUARE, SQUAREROOT) "
            													+ " VALUES (?, ?, ?, ?)");

            // Populate the table.
            for (int i = 1; i <= words.length; ++i) {
            	pstmt.setInt (1, i);
            	pstmt.setString (2, words[i-1]);
            	pstmt.setInt (3, i*i);
            	pstmt.setDouble (4, Math.sqrt(i));
            	pstmt.executeUpdate ();
            }
            
            addStr += ">table insert ok..." + endDelimitor;

            // Output a completion message.
            System.out.println ("Table " + system + "." + tableName + " has been created.");
            
            pstmt.close();
        }

        catch (Exception e) {
            System.out.println ();
            System.out.println ("ORACLE ERROR : " + e.getMessage());
            addStr += "ORACLE ERROR : "+e.getMessage();
        }

        finally {

            // Clean up.
            try {
                if (connection != null)
                    connection.close ();
            } catch (SQLException e) {
                // Ignore.
            }
            addStr += ">connection close ok..." + endDelimitor;
        }
        return addStr;
    }
    
    public static void main(String[] args) {
    	JdbcPingForOracle ora = new JdbcPingForOracle();
    	String resultStr = ora.run("jdbc:oracle:thin:@127.0.0.1:1521:","wind","hoon09","park168cm", "\n\r");
		System.out.println("oracle connection info :: "+resultStr);
	}
    
}
