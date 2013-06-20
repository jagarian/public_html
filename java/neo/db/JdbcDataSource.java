package neo.db;

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import neo.config.Config;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	JdbcDataSource.java
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
public class JdbcDataSource implements DataSource {

	private static final String PROP_BASE 							=	"/config/neo/jdbc-datasource/spec<";
	private static final String PROP_JDBC_DRIVER 					= 	">/driver";
	private static final String PROP_JDBC_URL 						= 	">/url";
	private static final String PROP_JDBC_USERNAME 					= 	">/user";
	private static final String PROP_JDBC_PASSWORD 					=	">/password";
	private static final String PROP_POOL_MAX_ACTIVE_CONN 			=	"/config/neo/jdbc-datasource/pool/max-active-connections";
	private static final String PROP_POOL_MAX_IDLE_CONN 				=	"/config/neo/jdbc-datasource/pool/max-idle-connections";
	private static final String PROP_POOL_MAX_CHECKOUT_TIME 			=	"/config/neo/jdbc-datasource/pool/max-checkout-time";
	private static final String PROP_POOL_TIME_TO_WAIT 				=	"/config/neo/jdbc-datasource/pool/wait-time";
	private static final String PROP_POOL_PING_QUERY 				=	"/config/neo/jdbc-datasource/pool/ping-query";
	private static final String PROP_POOL_PING_CONN_OLDER_THAN 		=	"/config/neo/jdbc-datasource/pool/ping-connection-time";
	private static final String PROP_POOL_PING_ENABLED 				=	"/config/neo/jdbc-datasource/pool/ping-enabled";
	private static final String PROP_POOL_QUIET_MODE 				=	"/config/neo/jdbc-datasource/pool/quiet-mode";
	private static final String PROP_POOL_PING_CONN_NOT_USED_FOR 	=	"/config/neo/jdbc-datasource/pool/ping-connections-notused";

	private int expectedConnectionTypeCode;

	private static final Object POOL_LOCK = new Object();

	private List idleConnections = new ArrayList();
	private List activeConnections = new ArrayList();

	private long requestCount = 0;
	private long accumulatedRequestTime = 0;
	private long accumulatedCheckoutTime = 0;
	private long claimedOverdueConnectionCount = 0;
	private long accumulatedCheckoutTimeOfOverdueConnections = 0;
	private long accumulatedWaitTime = 0;
	private long hadToWaitCount = 0;
	private long badConnectionCount = 0;

	private String jdbcDriver;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;

	private int poolMaximumActiveConnections;
	private int poolMaximumIdleConnections;
	private int poolMaximumCheckoutTime;
	private int poolTimeToWait;

	private String poolPingQuery;
	private boolean poolQuietMode;
	private boolean poolPingEnabled;

	private int poolPingConnectionsOlderThan;
	private int poolPingConnectionsNotUsedFor;

	public JdbcDataSource() {
		initialize("default");
	}

	public JdbcDataSource(String dbSpec) {
		initialize(dbSpec);
	}

	private void initialize(String dbSpec) {
		try {
			Config conf = Config.getInstance();

			if (dbSpec == null)
				dbSpec = "default";

			jdbcDriver 						= conf.getString(PROP_BASE + dbSpec + PROP_JDBC_DRIVER, null);
			jdbcUrl 						= conf.getString(PROP_BASE + dbSpec + PROP_JDBC_URL, null);
			jdbcUsername 					= conf.getString(PROP_BASE + dbSpec + PROP_JDBC_USERNAME, null);
			jdbcPassword 					= conf.getString(PROP_BASE + dbSpec + PROP_JDBC_PASSWORD, null);

			poolMaximumActiveConnections 	= (conf.getInt(PROP_POOL_MAX_ACTIVE_CONN, 10));
			poolMaximumIdleConnections 		= (conf.getInt(PROP_POOL_MAX_IDLE_CONN, 5));
			poolMaximumCheckoutTime 		= (conf.getInt(PROP_POOL_MAX_CHECKOUT_TIME, 20000));
			poolTimeToWait 					= (conf.getInt(PROP_POOL_TIME_TO_WAIT, 15000));
			poolPingEnabled 				= (conf.getBoolean(PROP_POOL_PING_ENABLED, false));
			poolPingQuery 					= conf.getString(PROP_POOL_PING_QUERY, "No Ping QUERY SET");
			poolPingConnectionsOlderThan 	= (conf.getInt(PROP_POOL_PING_CONN_OLDER_THAN, 0));
			poolPingConnectionsNotUsedFor 	= (conf.getInt(PROP_POOL_PING_CONN_NOT_USED_FOR, 0));
			poolQuietMode 					= (conf.getBoolean(PROP_POOL_QUIET_MODE, true));
			expectedConnectionTypeCode 		= assembleConnectionTypeCode(jdbcUrl, jdbcUsername, jdbcPassword);
			
			Log.debug("===============================");
			Log.debug("jdbcDriver :: " + jdbcDriver);
			Log.debug("jdbcUrl :: " + jdbcUrl);
			Log.debug("jdbcUsername :: " + jdbcUsername);
			Log.debug("jdbcPassword :: " + jdbcPassword);
			Log.debug("===============================");
			Log.debug("poolMaximumActiveConnections :: "+ poolMaximumActiveConnections);
			Log.debug("poolMaximumIdleConnections :: " + poolMaximumIdleConnections);
			Log.debug("poolMaximumCheckoutTime :: " + poolMaximumCheckoutTime);
			Log.debug("poolTimeToWait :: " + poolTimeToWait);
			Log.debug("poolPingEnabled :: " + poolPingEnabled);
			Log.debug("poolPingQuery :: " + poolPingQuery);
			Log.debug("poolPingConnectionsOlderThan :: "+ poolPingConnectionsOlderThan);
			Log.debug("poolPingConnectionsNotUsedFor :: "+ poolPingConnectionsNotUsedFor);
			Log.debug("poolQuietMode :: " + poolQuietMode);
			Log.debug("expectedConnectionTypeCode :: " + expectedConnectionTypeCode);
			Log.debug("===============================");

			ClassLoader classLoader =	Thread.currentThread().getContextClassLoader();
			Log.debug("classLoader :: " + classLoader);
			
			classLoader.loadClass(jdbcDriver).newInstance();
			Log.debug("loaded => ok");

			getStatus("");

		} catch (Exception e) {
			Log.error("error :: " + e.getMessage());
			throw new RuntimeException("[JdbcDataSource] Error while loading properties. Cause: "+ e.toString());
		}
	}

	private int assembleConnectionTypeCode(String url,
											String username,
											String password) {
		return ("" + url + username + password).hashCode();
	}

	public Connection getConnection() throws SQLException {
		return popConnection(jdbcUsername, jdbcPassword);
	}

	public Connection getConnection(String username, String password)
		throws SQLException {
		return popConnection(username, password);
	}

	public void setLoginTimeout(int loginTimeout) throws SQLException {
		DriverManager.setLoginTimeout(loginTimeout);
	}

	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		DriverManager.setLogWriter(logWriter);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	public int getPoolPingConnectionsNotUsedFor() {
		return poolPingConnectionsNotUsedFor;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public int getPoolMaximumActiveConnections() {
		return poolMaximumActiveConnections;
	}

	public int getPoolMaximumIdleConnections() {
		return poolMaximumIdleConnections;
	}

	public int getPoolMaximumCheckoutTime() {
		return poolMaximumCheckoutTime;
	}

	public int getPoolTimeToWait() {
		return poolTimeToWait;
	}

	public String getPoolPingQuery() {
		return poolPingQuery;
	}

	public boolean isPoolPingEnabled() {
		return poolPingEnabled;
	}

	public int getPoolPingConnectionsOlderThan() {
		return poolPingConnectionsOlderThan;
	}

	public boolean isPoolQuietMode() {
		return poolQuietMode;
	}

	private void log(Object o) {
		if (!isPoolQuietMode()) {
			Log.info(o.toString(), this);
		}
	}

	public void setPoolQuietMode(boolean poolQuietMode) {
		this.poolQuietMode = poolQuietMode;
	}

	private int getExpectedConnectionTypeCode() {
		return expectedConnectionTypeCode;
	}

	public long getRequestCount() {
		synchronized (POOL_LOCK) {
			return requestCount;
		}
	}

	public long getAverageRequestTime() {
		synchronized (POOL_LOCK) {
			return requestCount == 0 
						? 0 
						: accumulatedRequestTime / requestCount;
		}
	}

	public long getAverageWaitTime() {
		synchronized (POOL_LOCK) {
			return hadToWaitCount == 0
						? 0
						: accumulatedWaitTime / hadToWaitCount;
		}
	}

	public long getHadToWaitCount() {
		synchronized (POOL_LOCK) {
			return hadToWaitCount;
		}
	}

	public long getBadConnectionCount() {
		synchronized (POOL_LOCK) {
			return badConnectionCount;
		}
	}

	public long getClaimedOverdueConnectionCount() {
		synchronized (POOL_LOCK) {
			return claimedOverdueConnectionCount;
		}
	}

	public long getAverageOverdueCheckoutTime() {
		synchronized (POOL_LOCK) {
			return claimedOverdueConnectionCount == 0
						? 0
						: accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
		}
	}

	public long getAverageCheckoutTime() {
		synchronized (POOL_LOCK) {
			return requestCount == 0
						? 0
						: accumulatedCheckoutTime / requestCount;
		}
	}
	
	public String getStatus(String sep) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n==============================================================="+sep);
		buffer.append("\n activeConnections				= " + activeConnections.size()+sep);
		buffer.append("\n idleConnections				= " + idleConnections.size()+sep);
		buffer.append("\n getRequestCount				= " + getRequestCount()+sep);
		buffer.append("\n==============================================================="+sep);
		return buffer.toString();
	}

	public String getStatus_old(String sep) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n==============================================================="+sep);
		buffer.append("\n jdbcDriver					= " + jdbcDriver+sep);
		buffer.append("\n jdbcUrl						= " + jdbcUrl+sep);
		buffer.append("\n jdbcUsername					= " + jdbcUsername+sep);
		buffer.append("\n jdbcPassword					= " + (jdbcPassword == null ? "NULL" : "************")+sep);
		buffer.append("\n poolMaxActiveConnections		= " + poolMaximumActiveConnections+sep);
		buffer.append("\n poolMaxIdleConnections		= " + poolMaximumIdleConnections+sep);
		buffer.append("\n poolMaxCheckoutTime			= " + poolMaximumCheckoutTime+sep);
		buffer.append("\n poolTimeToWait				= " + poolTimeToWait+sep);
		buffer.append("\n poolQuietMode					= " + poolQuietMode+sep);
		buffer.append("\n poolPingEnabled				= " + poolPingEnabled+sep);
		buffer.append("\n poolPingQuery					= " + poolPingQuery+sep);
		buffer.append("\n poolPingConnectionsOlderThan	= "+ poolPingConnectionsOlderThan+sep);
		buffer.append("\n poolPingConnectionsNotUsedFor	= "+ poolPingConnectionsNotUsedFor+sep);
		buffer.append("\n --------------------------------------------------------------"+sep);
		buffer.append("\n activeConnections				= " + activeConnections.size()+sep);
		buffer.append("\n idleConnections				= " + idleConnections.size()+sep);
		buffer.append("\n requestCount					= " + getRequestCount()+sep);
		buffer.append("\n averageRequestTime			= " + getAverageRequestTime()+sep);
		buffer.append("\n averageCheckoutTime			= " + getAverageCheckoutTime()+sep);
		buffer.append("\n claimedOverdue				= " + getClaimedOverdueConnectionCount()+sep);
		buffer.append("\n averageOverdueCheckoutTime	= " + getAverageOverdueCheckoutTime()+sep);
		buffer.append("\n hadToWait						= " + getHadToWaitCount()+sep);
		buffer.append("\n averageWaitTime				= " + getAverageWaitTime()+sep);
		buffer.append("\n badConnectionCount			= " + getBadConnectionCount()+sep);
		buffer.append("\n==============================================================="+sep);
		return buffer.toString();
	}
	
	public void forceCloseAll() throws SQLException {
		synchronized (POOL_LOCK) {
			for (int i = activeConnections.size(); i > 0; i--) {
				PooledJDBCConnection conn =
					(PooledJDBCConnection) activeConnections.remove(i - 1);
				Connection realConn = conn.getRealConnection();
				conn.invalidate();
				realConn.rollback();
				realConn.close();
			}
			for (int i = idleConnections.size(); i > 0; i--) {
				PooledJDBCConnection conn =
					(PooledJDBCConnection) idleConnections.remove(i - 1);
				Connection realConn = conn.getRealConnection();
				conn.invalidate();
				realConn.rollback();
				realConn.close();
			}
		}
		//log("[JdbcDataSource] Forcefully closed all connections.");
	}

	private void pushConnection(PooledJDBCConnection conn)
		throws SQLException {

		synchronized (POOL_LOCK) {
			activeConnections.remove(conn);
			if (conn.isValid()) {
				if (idleConnections.size() < poolMaximumIdleConnections
					&& conn.getConnectionTypeCode()	== getExpectedConnectionTypeCode()) {
					accumulatedCheckoutTime += conn.getCheckoutTime();
					conn.getRealConnection().rollback();
					PooledJDBCConnection newConn =	new PooledJDBCConnection(
																			conn.getRealConnection(),
																			this);
					idleConnections.add(newConn);
					newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
					newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
					conn.invalidate();					
					POOL_LOCK.notifyAll();
				} else {
					accumulatedCheckoutTime += conn.getCheckoutTime();
					conn.getRealConnection().rollback();
					conn.getRealConnection().close();
					conn.invalidate();
				}
			} else {
				badConnectionCount++;
			}
		}
	}

	private PooledJDBCConnection popConnection(
		String username,
		String password)
		throws SQLException {
		boolean countedWait = false;
		PooledJDBCConnection conn = null;
		long t = System.currentTimeMillis();
		int localBadConnectionCount = 0;

		while (conn == null) {

			synchronized (POOL_LOCK) {
				if (idleConnections.size() > 0) {
					// Pool has available connection
					conn = (PooledJDBCConnection) idleConnections.remove(0);
				} else {
					// Pool does not have available connection
					if (activeConnections.size() < poolMaximumActiveConnections) {
						// Can create new connection
						conn = new PooledJDBCConnection(
									DriverManager.getConnection(jdbcUrl,
																username,
																password),
																this);
						if (conn.getRealConnection().getAutoCommit()) {
							conn.getRealConnection().setAutoCommit(false);
						}
					} else {
						// Cannot create new connection
						PooledJDBCConnection oldestActiveConnection = (PooledJDBCConnection) activeConnections.get(0);
						long longestCheckoutTime =
							oldestActiveConnection.getCheckoutTime();
						if (longestCheckoutTime > poolMaximumCheckoutTime) {
							// Can claim overdue connection
							claimedOverdueConnectionCount++;
							accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
							accumulatedCheckoutTime += longestCheckoutTime;
							activeConnections.remove(oldestActiveConnection);
							oldestActiveConnection.getRealConnection().rollback();
							conn = new PooledJDBCConnection(oldestActiveConnection.getRealConnection(),this);
							oldestActiveConnection.invalidate();
						} else {
							// Must wait
							try {
								if (!countedWait) {
									hadToWaitCount++;
									countedWait = true;
								}
								long wt = System.currentTimeMillis();
								POOL_LOCK.wait(poolTimeToWait);
								accumulatedWaitTime += System.currentTimeMillis()	- wt;
							} catch (InterruptedException e) {
								break;
							}
						}
					}
				}
				if (conn != null) {
					if (conn.isValid()) {
						conn.getRealConnection().rollback();
						conn.setConnectionTypeCode(
							assembleConnectionTypeCode(jdbcUrl,
														username,
														password));
						conn.setCheckoutTimestamp(System.currentTimeMillis());
						conn.setLastUsedTimestamp(System.currentTimeMillis());
						activeConnections.add(conn);
						requestCount++;
						accumulatedRequestTime += System.currentTimeMillis() - t;
					} else {
						badConnectionCount++;
						localBadConnectionCount++;
						conn = null;
						if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
							throw new SQLException("[JdbcDataSource] Could not get a good connection to the database.");
						}
					}
				}
			}
		}

		if (conn == null) {
			throw new SQLException("[JdbcDataSource Error] Unknown severe error condition.  The connection pool returned a null connection.");
		}

		return conn;
	}

	private boolean pingConnection(PooledJDBCConnection conn) {
		boolean result = true;

		try {
			result = !conn.getRealConnection().isClosed();
		} catch (SQLException e) {
			result = false;
		}

		if (result) {
			if (poolPingEnabled) {
				if ((poolPingConnectionsOlderThan > 0 && conn.getAge() > poolPingConnectionsOlderThan) || 
					(poolPingConnectionsNotUsedFor > 0 && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor)) {

					try {
						Statement statement = conn.createStatement();
						ResultSet rs = statement.executeQuery(poolPingQuery);
						rs.close();
						statement.close();
						conn.rollback();
						result = true;
					} catch (Exception e) {
						try {
							conn.getRealConnection().close();
						} catch (Exception e2) {
							//ignore
						}
						result = false;
					}
				}
			}
		}
		return result;
	}

	public static Connection unwrapConnection(Connection conn) {
		if (conn instanceof PooledJDBCConnection) {
			return ((PooledJDBCConnection) conn).getRealConnection();
		} else {
			return conn;
		}
	}

	private static class PooledJDBCConnection implements Connection {

		private int hashCode = 0;
		private JdbcDataSource dataSource;
		private Connection realConnection;
		private long checkoutTimestamp;
		private long createdTimestamp;
		private long lastUsedTimestamp;
		private int connectionTypeCode;
		private boolean valid;

		public PooledJDBCConnection(Connection connection,
													JdbcDataSource dataSource) {
			this.hashCode = connection.hashCode();
			this.realConnection = connection;
			this.dataSource = dataSource;
			this.createdTimestamp = System.currentTimeMillis();
			this.lastUsedTimestamp = System.currentTimeMillis();
			this.valid = true;
		}

		public void invalidate() {
			valid = false;
		}

		public boolean isValid() {
			return valid
				&& realConnection != null
				&& dataSource.pingConnection(this);
		}

		public Connection getRealConnection() {
			return realConnection;
		}

		public int getRealHashCode() {
			if (realConnection == null)
				return 0;
			else
				return realConnection.hashCode();
		}

		public int getConnectionTypeCode() {
			return connectionTypeCode;
		}

		public void setConnectionTypeCode(int connectionTypeCode) {
			this.connectionTypeCode = connectionTypeCode;
		}

		public long getCreatedTimestamp() {
			return createdTimestamp;
		}

		public void setCreatedTimestamp(long createdTimestamp) {
			this.createdTimestamp = createdTimestamp;
		}

		public long getLastUsedTimestamp() {
			return lastUsedTimestamp;
		}

		public void setLastUsedTimestamp(long lastUsedTimestamp) {
			this.lastUsedTimestamp = lastUsedTimestamp;
		}

		public long getTimeElapsedSinceLastUse() {
			return System.currentTimeMillis() - lastUsedTimestamp;
		}

		public long getAge() {
			return System.currentTimeMillis() - createdTimestamp;
		}

		public long getCheckoutTimestamp() {
			return checkoutTimestamp;
		}

		public void setCheckoutTimestamp(long timestamp) {
			this.checkoutTimestamp = timestamp;
		}

		public long getCheckoutTime() {
			return System.currentTimeMillis() - checkoutTimestamp;
		}

		private Connection getValidConnection() {
			if (!valid) {
				throw new RuntimeException("[PooledJDBCConnection Error] Connection has been invalidated (probably released back to the pool).");
			}
			return realConnection;
		}

		public Statement createStatement() throws SQLException {
			return getValidConnection().createStatement();
		}

		public PreparedStatement prepareStatement(String sql)
			throws SQLException {
			return getValidConnection().prepareStatement(sql);
		}

		public CallableStatement prepareCall(String sql) throws SQLException {
			return getValidConnection().prepareCall(sql);
		}

		public String nativeSQL(String sql) throws SQLException {
			return getValidConnection().nativeSQL(sql);
		}

		public void setAutoCommit(boolean autoCommit) throws SQLException {

			getValidConnection().setAutoCommit(autoCommit);
		}

		public boolean getAutoCommit() throws SQLException {
			return getValidConnection().getAutoCommit();
		}

		public void commit() throws SQLException {
			getValidConnection().commit();
		}

		public void rollback() throws SQLException {
			getValidConnection().rollback();
		}

		public void close() throws SQLException {
			dataSource.pushConnection(this);
		}

		public boolean isClosed() throws SQLException {
			return getValidConnection().isClosed();
		}

		public DatabaseMetaData getMetaData() throws SQLException {
			return getValidConnection().getMetaData();
		}

		public void setReadOnly(boolean readOnly) throws SQLException {
			getValidConnection().setReadOnly(readOnly);
		}

		public boolean isReadOnly() throws SQLException {
			return getValidConnection().isReadOnly();
		}

		public void setCatalog(String catalog) throws SQLException {
			getValidConnection().setCatalog(catalog);
		}

		public String getCatalog() throws SQLException {
			return getValidConnection().getCatalog();
		}

		public void setTransactionIsolation(int level) throws SQLException {
			getValidConnection().setTransactionIsolation(level);
		}

		public int getTransactionIsolation() throws SQLException {
			return getValidConnection().getTransactionIsolation();
		}

		public SQLWarning getWarnings() throws SQLException {
			return getValidConnection().getWarnings();
		}

		public void clearWarnings() throws SQLException {
			getValidConnection().clearWarnings();
		}

		public Statement createStatement(int resultSetType,
											int resultSetConcurrency)
											throws SQLException {
			return getValidConnection().createStatement(resultSetType,
														resultSetConcurrency);
		}

		public PreparedStatement prepareStatement(String sql,
												int resultSetType,
												int resultSetConcurrency)
			throws SQLException {
			return getValidConnection().prepareCall(sql,
													resultSetType,
													resultSetConcurrency);
		}

		public CallableStatement prepareCall(	String sql,
												int resultSetType,
												int resultSetConcurrency)
			throws SQLException {
			return getValidConnection().prepareCall(sql,
													resultSetType,
													resultSetConcurrency);
		}

		public Map getTypeMap() throws SQLException {
			return getValidConnection().getTypeMap();
		}

		public void setTypeMap(Map map) throws SQLException {
			getValidConnection().setTypeMap(map);
		}

		public int hashCode() {
			return hashCode;
		}

		public boolean equals(Object obj) {
			if (obj instanceof PooledJDBCConnection) {
				return realConnection.hashCode()
					== (((PooledJDBCConnection) obj).realConnection.hashCode());
			} else if (obj instanceof Connection) {
				return hashCode == obj.hashCode();
			} else {
				return false;
			}
		}

		public void setHoldability(int holdability) throws SQLException {
			getValidConnection().setHoldability(holdability);
		}

		public int getHoldability() throws SQLException {
			return getValidConnection().getHoldability();
		}

		public Savepoint setSavepoint() throws SQLException {
			return getValidConnection().setSavepoint();
		}

		public Savepoint setSavepoint(String name) throws SQLException {
			return getValidConnection().setSavepoint(name);
		}

		public void rollback(Savepoint savepoint) throws SQLException {
			getValidConnection().rollback(savepoint);
		}

		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			getValidConnection().releaseSavepoint(savepoint);
		}

		public Statement createStatement(	int resultSetType,
											int resultSetConcurrency,
											int resultSetHoldability)
			throws SQLException {
			return getValidConnection().createStatement(resultSetType,
														resultSetConcurrency,
														resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql,
												int resultSetType,
												int resultSetConcurrency,
												int resultSetHoldability)
			throws SQLException {
			return getValidConnection().prepareStatement(sql,
														resultSetType,
														resultSetConcurrency,
														resultSetHoldability);
		}

		public CallableStatement prepareCall( String sql,
											int resultSetType,
											int resultSetConcurrency,
											int resultSetHoldability)
			throws SQLException {
			return getValidConnection().prepareCall(sql,
													resultSetType,
													resultSetConcurrency,
													resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql,
												int autoGeneratedKeys)
			throws SQLException {
			return getValidConnection().prepareStatement(sql,
														autoGeneratedKeys);
		}

		public PreparedStatement prepareStatement(String sql,	int columnIndexes[])
			throws SQLException {
			return getValidConnection().prepareStatement(sql, columnIndexes);
		}

		public PreparedStatement prepareStatement(String sql, String columnNames[])
			throws SQLException {
			return getValidConnection().prepareStatement(sql, columnNames);
		}
		
	}
}