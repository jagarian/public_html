package neo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import neo.config.Config;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	DataSourcePool.java
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
public class DataSourcePool implements Observer {

	public static final int JDBCTYPE = 1;
	public static final int JNDITYPE = 2;
	private static DataSourcePool singleton = null;
	private static final String DEFAULT_DATASOURCE = "default";
	private HashMap jdbcDataSourcePool = null;
	private HashMap jndiDataSourcePool = null;
	private boolean traceSwitch = false;
	private int defaultType = 1;

	private DataSourcePool() {
		super();
		jdbcDataSourcePool = new HashMap();
		jndiDataSourcePool = new HashMap();
		try {
			Config conf = Config.getInstance();
			String defaultPool	= conf.getString("/config/neo/default-pool", "JDBC");
			traceSwitch =	conf.getBoolean("/config/neo/log/db-trace", false);			
			if (defaultPool.toLowerCase().equals("jndi"))
				defaultType = JNDITYPE;
		} catch (neo.exception.ConfigException ce) {
			defaultType = JDBCTYPE;
		}
	}

	public Connection getConnection() throws SQLException {
		if (defaultType == 1)
			return getJDBCConnection();
		else
			return getJNDIConnection();
	}

	public Connection getConnection(String dataSourceSpec)
		throws SQLException {
		if (defaultType == 1)
			return getJDBCConnection(dataSourceSpec);
		else
			return getJNDIConnection(dataSourceSpec);
	}

	public Connection getJDBCConnection() throws SQLException {
		return getJDBCConnection(DEFAULT_DATASOURCE);
	}

	public Connection getJDBCConnection(String dataSourceSpec)
		throws SQLException {
		JdbcDataSource simpleDataSource = null;
		synchronized (this) {
			simpleDataSource = (JdbcDataSource) jdbcDataSourcePool.get(dataSourceSpec);
			if (simpleDataSource == null) {
				JdbcDataSource ds = new JdbcDataSource(dataSourceSpec);
				jdbcDataSourcePool.put(dataSourceSpec, ds);
				simpleDataSource = ds;
			}
		}
		if (traceSwitch)
			return new TConnection(simpleDataSource.getConnection());
		else
			return simpleDataSource.getConnection();
	}

	public String printJDBCConnStatus(String sep) {
		return printJDBCConnStatus(DEFAULT_DATASOURCE, sep);
	}

	public String printJDBCConnStatus(String dataSourceSpec, String sep) {
		JdbcDataSource simpleDataSource = null;
		synchronized (this) {
			simpleDataSource =
				(JdbcDataSource) jdbcDataSourcePool.get(dataSourceSpec);
			if (simpleDataSource == null) {
				JdbcDataSource ds = new JdbcDataSource(dataSourceSpec);
				jdbcDataSourcePool.put(dataSourceSpec, ds);
				simpleDataSource = ds;
			}
		}
		return simpleDataSource.getStatus(sep);
	}
	
	public String printJDBCConnStatus_old(String sep) {
		return printJDBCConnStatus_old(DEFAULT_DATASOURCE, sep);
	}
	
	public String printJDBCConnStatus_old(String dataSourceSpec, String sep) {
		JdbcDataSource simpleDataSource = null;
		synchronized (this) {
			simpleDataSource =
				(JdbcDataSource) jdbcDataSourcePool.get(dataSourceSpec);
			if (simpleDataSource == null) {
				JdbcDataSource ds = new JdbcDataSource(dataSourceSpec);
				jdbcDataSourcePool.put(dataSourceSpec, ds);
				simpleDataSource = ds;
			}
		}
		return simpleDataSource.getStatus_old(sep);
	}

	public Connection getJNDIConnection() throws SQLException {
		return getJNDIConnection(DEFAULT_DATASOURCE);
	}

	public Connection getJNDIConnection(String dataSourceSpec)
		throws SQLException {
		JndiDataSource dataSource = null;

		synchronized (this) {
			dataSource =
				(JndiDataSource) jndiDataSourcePool.get(dataSourceSpec);
			if (dataSource == null) {
				JndiDataSource ds = new JndiDataSource(dataSourceSpec);
				jndiDataSourcePool.put(dataSourceSpec, ds);
				dataSource = ds;
			}
		}
		if (traceSwitch)
			return new TConnection(dataSource.getConnection());
		else
			return dataSource.getConnection();
	}

	public static synchronized DataSourcePool getInstance() {
		if (singleton == null) {
			singleton = new DataSourcePool();

			try {
				Config conf = Config.getInstance();
				conf.addObserver(singleton);
			} catch (Exception e) {
				Log.error("[DataSourcePool.getInstance()] : Fail to regist DataSourcePool Object as observer.", singleton);
			}

		}
		return singleton;
	}

	public void update(Observable o, Object arg) {
		if (singleton != null) {
			singleton = new DataSourcePool();
		}
	}

}
