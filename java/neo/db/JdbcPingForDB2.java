package neo.db;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 	@Class Name	: 	JdbcPingForDB2.java
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
public class JdbcPingForDB2
{
	private static final String jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
	private static final String jdbcUrl = "";
	
    // Strings to be added in the WORD column of the table.
    private static final String words[] = 
    		{	"One",      "Two",      "Three",    "Four",     "Five",
            	"Six",      "Seven",    "Eight",    "Nine",     "Ten",
            	"Eleven",   "Twelve",   "Thirteen", "Fourteen", "Fifteen",
            	"Sixteen",  "Seventeen","Eighteen", "Nineteen", "Twenty" };

    public String run (String url, String system, String user_id, String password, String endDelimitor)
    {
        String tableName		= "?";
        String addStr 			= endDelimitor + "====================Start====================" + endDelimitor;
        Connection connection   = null;

        try {
        	addStr += "■ DB2 jdbc test " + endDelimitor;
        	
        	// RDz 의 org.eclipse.datatools.connectivity.drivers.jdbc.JDBCConnection 에서 createConnection method
        	// http://www-306.ibm.com/software/data/db2/udb/db2express/download.html에서 db2exc_NTx86_XPHome.zip 다운로드
        	
        	ClassLoader classLoader =	null;
        	classLoader = Thread.currentThread().getContextClassLoader(); 
            classLoader.loadClass(jdbcDriver).newInstance();
            addStr += ">class loader ok..." + endDelimitor;    
            
            Properties dbProp = new Properties();            
            dbProp.put("user", user_id); addStr += "===1===" + endDelimitor;      
            dbProp.put("password", password); addStr += "===2===" + endDelimitor;      
            
            connection = DriverManager.getConnection(url+system, dbProp); addStr += "===4==="+endDelimitor;      
            addStr += ">connection successful ok..."+"("+url+system+")" + endDelimitor;
            
            DatabaseMetaData dbMetaData = connection.getMetaData();
            DBUtil.toStringMetadata(dbMetaData,"<BR>");
            
            Enumeration Drv = DriverManager.getDrivers();
            int cnt = 0;
            while(Drv.hasMoreElements()) {
            	addStr += "> ["+(cnt++)+"]" + Drv.nextElement()+endDelimitor;
            }
            
            Statement stmt = connection.createStatement();
            
            stmt.close();
        }

        catch (Exception e) {
            System.out.println ();
            System.out.println ("DB2 ERROR : " + e.getMessage());
            addStr += "DB2 ERROR : "+e.getMessage();
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



}
