package neo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import neo.config.Config;
import neo.exception.ConfigException;

/**
 * 	@Class Name	: 	JndiDataSource.java
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
class JndiDataSource {

	private DataSource localDataSource = null;
	private String FACTORY_NAME = null;
	private String PROVIDER_URL = null;
	private String DATASOURCE_URL = null;
	private String USER_ID = null;
	private String USER_PASS = null;
	private final String PROP_PREFIX = "/jndi-datasource<";

	private JndiDataSource() {
		super();
	}

	JndiDataSource(String dataSourceSpec) throws SQLException {
		super();
		Properties props = null;
		InitialContext ctx = null;

		if (localDataSource == null) {
			try {
				this.getParamFromConfig(dataSourceSpec);

				if (this.FACTORY_NAME != null && this.PROVIDER_URL != null) {
					props = new Properties();
					props.put(Context.INITIAL_CONTEXT_FACTORY, this.FACTORY_NAME);
					props.put(Context.PROVIDER_URL, this.PROVIDER_URL);

					ctx = new InitialContext(props);

				} else {
					ctx = new InitialContext();
				}

				localDataSource = (DataSource) ctx.lookup(this.DATASOURCE_URL);

			} catch (NamingException ne) {
				throw new SQLException("[JndiDataSource Error] fail to instantiate JndiDataSource : "+ ne.getMessage());
			}
		}
	}

	Connection getConnection() throws SQLException {
		try {
			if (this.USER_ID == null || this.USER_PASS == null)
				return this.localDataSource.getConnection();
			else
				return this.localDataSource.getConnection(	this.USER_ID,
															this.USER_PASS);
		} catch (SQLException se) {
			throw new SQLException("[JndiDataSource Error] Fail to get Connection : "+ se.getMessage());
		}
	}

	private void getParamFromConfig(String dataSourceSpec)
		throws SQLException {
		Config conf = null;

		try {
			conf = Config.getInstance();
		} catch (ConfigException le) {
			throw new SQLException("[JndiDataSource Error] Check the configuration file : "+ le.getMessage());
		}

		try {
			this.DATASOURCE_URL 	= conf.getString(PROP_PREFIX + dataSourceSpec + ">/url", null);
			this.USER_ID 			= conf.getString(PROP_PREFIX + dataSourceSpec + ">/user", null);
			this.USER_PASS 			= conf.getString(PROP_PREFIX + dataSourceSpec + ">/password", null);
			this.FACTORY_NAME 		= conf.getString(PROP_PREFIX + dataSourceSpec + ">/context-factory", null);
			this.PROVIDER_URL 		= conf.getString(PROP_PREFIX + dataSourceSpec + ">/provider-url",	null);
		} catch (ConfigException le) {
			throw new SQLException("[JndiDataSource Error] Check the DataSource configuration in neo.xml File : "+ le.getMessage());
		}
	}
}