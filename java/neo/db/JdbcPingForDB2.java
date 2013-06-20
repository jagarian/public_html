package neo.db;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 	@Class Name	: 	JdbcPingForDB2.java
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
        	addStr += "�� DB2 jdbc test " + endDelimitor;
        	
        	// RDz �� org.eclipse.datatools.connectivity.drivers.jdbc.JDBCConnection ���� createConnection method
        	// http://www-306.ibm.com/software/data/db2/udb/db2express/download.html���� db2exc_NTx86_XPHome.zip �ٿ�ε�
        	
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
