package neo.db;

import java.sql.*;
import java.util.Enumeration;

/**
 * 	@Class Name	: 	JdbcPingForMysql.java
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
public class JdbcPingForMysql
{
	private static final String jdbcDriver = "com.mysql.jdbc.Driver";
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
        	
        	addStr += "�� Mysql jdbc test" + endDelimitor; //http://download.softagency.net/MySQL/Downloads/MySQLGUITools/
        	
        	try {
            	ClassLoader classLoader =	Thread.currentThread().getContextClassLoader(); 
                classLoader.loadClass(jdbcDriver).newInstance();
            	addStr += ">class loader ok..." + endDelimitor;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
            
            // Get a connection to the database.
            connection = DriverManager.getConnection (url+system,user_id,password); 
            addStr += ">connection successful ok..."+"("+url+system+")" + endDelimitor;
            
            DatabaseMetaData  dbMetaData = connection.getMetaData();
            DBUtil.toStringMetadata(dbMetaData,"<BR>");
            
            Enumeration Drv = DriverManager.getDrivers();
            int cnt = 0;
            while(Drv.hasMoreElements()) {
            	addStr += "> ["+(cnt++)+"]" + Drv.nextElement() + endDelimitor;
            }
            
            Statement  stmt = connection.createStatement();
            
            stmt.close();
        }

        catch (Exception e) {
            System.out.println ();
            System.out.println ("Mysql ERROR : " + e.getMessage());
            addStr += "Mysql ERROR : "+e.getMessage();
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
