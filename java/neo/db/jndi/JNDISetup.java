package neo.db.jndi;

import com.microsoft.jdbcx.sqlserver.SQLServerDataSource;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.*;
import java.sql.*;

/**
 * 	@Class Name	: 	JNDISetup.java
 * 	@파일설명		: 	The constructor expects the naming server URL and the context
 * 						provider class. For example, the sample URL for ldap server on
 *       				localhost can be
 *       				ldap://localhost:389/ou=jdbc,cn=manager,dc=microsoft,dc=com, and the
 *       				provider class name can be com.sun.jndi.ldap.LdapCtxFactory	
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
public class JNDISetup {

	Context ctx = null;
	String url;
	String factory;

	JNDISetup(String url, String factory) {
		this.url = url;
		this.factory = factory;
		getContext();
	}

	private void getContext() {
		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
			env.put(Context.PROVIDER_URL, url);
			ctx = new InitialContext(env);
		} catch (Exception e) {
			System.out.println("Error in SetupJNDI:getContext() " + e.getMessage());
			e.printStackTrace();
		}

	}

	public boolean bindDataSource(String bindName) {
		boolean isRegistered = false;
		try {
			SQLServerDataSource mds = new SQLServerDataSource();
			mds.setDescription("MS SQLServerDataSource");
			mds.setServerName("sqlserver");
			mds.setPortNumber(1433);
			mds.setDatabaseName("pubs");
			mds.setSelectMethod("cursor");
			ctx.rebind(bindName, mds);
			System.out.println("Bind success");
			isRegistered = true;
		} catch (Exception e) {
			System.out.println("Error Occured in JNDISetup : " + e.getMessage());
			e.printStackTrace();
		}
		return isRegistered;
	}

	public javax.sql.ConnectionPoolDataSource getDataSource(String bindName) {
		javax.sql.ConnectionPoolDataSource ds = null;
		try {
			ds = (javax.sql.ConnectionPoolDataSource) ctx.lookup(bindName);
		} catch (Exception e) {
			System.out.println("Error in JNDISetup:getDataSource() : " + e.getMessage());
			e.printStackTrace();
		}
		return ds;
	}
}