package neo.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	TPreparedStatement.java
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
public class TPreparedStatement implements PreparedStatement {

	private PreparedStatement statement;
	private ArrayList params = new ArrayList();
	private ArrayList params_type = new ArrayList();

	private String sqlTemplate; // SQL문장

	public TPreparedStatement(PreparedStatement statement) {
		this.statement = statement;
	}

	public TPreparedStatement(Connection conn, String sql)
		throws SQLException {
		statement = conn.prepareStatement(sql);
		this.sqlTemplate = sql;
		params = new ArrayList();
	}

	public ResultSet executeQuery() throws SQLException {
		try {
			return statement.executeQuery();
		} catch (SQLException se) {
			Log.info(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public int executeUpdate() throws SQLException {
		int result = 0;
		try {
			result = statement.executeUpdate();
		} catch (SQLException se) {
			Log.info(se.toString(), this);
			throw se;
		} finally {
		}
		return result;
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		saveParam(parameterIndex, null, null);
		statement.setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		saveParam(parameterIndex, new Boolean(x), "Boolean");
		statement.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		saveParam(parameterIndex, new Byte(x), "Byte");
		statement.setByte(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		saveParam(parameterIndex, new Short(x), "Short");
		statement.setShort(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		saveParam(parameterIndex, new Integer(x), "Int");
		statement.setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		saveParam(parameterIndex, new Long(x), "Long");
		statement.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		saveParam(parameterIndex, new Float(x), "Float");
		statement.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		saveParam(parameterIndex, new Double(x), "Double");
		statement.setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
		throws SQLException {
		saveParam(parameterIndex, x, "BigDecimal");
		statement.setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		saveParam(parameterIndex, x, "String");
		statement.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		saveParam(parameterIndex, "" + x, "Bytes");
		statement.setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		saveParam(parameterIndex, x, "setDate");
		statement.setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		saveParam(parameterIndex, x, "Time");
		statement.setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
		throws SQLException {
		saveParam(parameterIndex, x, "Timestamp");
		statement.setTimestamp(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
		throws SQLException {
		saveParam(parameterIndex, "" + x + "(" + length + ")", "AsciiStream");
		statement.setAsciiStream(parameterIndex, x, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
		throws SQLException {
		throw new SQLException("Not Support : setUnicodeStream(int parameterIndex, InputStream x, int length)");
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
		throws SQLException {
		saveParam(parameterIndex, "InputStream(" + length + ")", "BinaryStream");
		statement.setBinaryStream(parameterIndex, x, length);
	}

	public void clearParameters() throws SQLException {
		params = new ArrayList();
		statement.clearParameters();
	}

	public void setObject(
		int parameterIndex,
		Object x,
		int targetSqlType,
		int scale)
		throws SQLException {
		saveParam(parameterIndex, x, "Object");
		statement.setObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
		throws SQLException {
		saveParam(parameterIndex, x, "Object");
		statement.setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		saveParam(parameterIndex, x, "Object");
		statement.setObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		try {
			return statement.execute();
		} catch (SQLException se) {
			Log.info(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public void addBatch() throws SQLException {
		statement.addBatch();
	}

	public void setCharacterStream(
		int parameterIndex,
		Reader reader,
		int length)
		throws SQLException {
		saveParam(parameterIndex, "" + reader, "Boolean");
		statement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int i, Ref x) throws SQLException {
		statement.setRef(i, x);
	}

	public void setBlob(int i, Blob x) throws SQLException {
		saveParam(i, "" + x, "Blob");
		statement.setBlob(i, x);
	}

	public void setClob(int i, Clob x) throws SQLException {
		saveParam(i, "" + x, "Clob");
		statement.setClob(i, x);
	}

	public void setArray(int i, java.sql.Array x) throws SQLException {
		saveParam(i, "" + x, "Array");
		statement.setArray(i, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return statement.getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
		throws SQLException {
		saveParam(parameterIndex, "" + x, "Date");
		statement.setDate(parameterIndex, x, cal);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
		throws SQLException {
		saveParam(parameterIndex, "" + x, "Time");
		statement.setTime(parameterIndex, x, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
		throws SQLException {
		saveParam(parameterIndex, "" + x, "Timestamp");
		statement.setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int paramIndex, int sqlType, String typeName)
		throws SQLException {
		saveParam(paramIndex, null, "null");
		statement.setNull(paramIndex, sqlType, typeName);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException se) {
			Log.info(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		int result = 0;
		try {
			result = statement.executeUpdate(sql);
		} catch (SQLException se) {
			Log.info(se.toString(), this);
			throw se;
		} finally {
		}
		return result;
	}

	public void close() throws SQLException {
		try {
			statement.close();
		} catch (SQLException e) {
			Log.error("pstmt close error => " + e, this);
			throw e;
		}
	}

	public int getMaxFieldSize() throws SQLException {
		return statement.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		statement.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return statement.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		statement.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		statement.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return statement.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		statement.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		statement.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return statement.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		statement.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		statement.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		try {
			return statement.execute(sql);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public ResultSet getResultSet() throws SQLException {
		return statement.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return statement.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return statement.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		statement.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return statement.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		statement.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return statement.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return statement.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return statement.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		statement.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		statement.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		try {
			return statement.executeBatch();
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public Connection getConnection() throws SQLException {
		return new TConnection(this.statement.getConnection());
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return statement.getParameterMetaData();
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		statement.setURL(parameterIndex, x);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
		throws SQLException {
		try {
			return statement.execute(sql, autoGeneratedKeys);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public boolean execute(String sql, int[] columnIndexes)
		throws SQLException {
		try {
			return statement.execute(sql, columnIndexes);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public boolean execute(String sql, String[] columnNames)
		throws SQLException {
		try {
			return statement.execute(sql, columnNames);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
		throws SQLException {
		int result = 0;
		try {
			result = statement.executeUpdate(sql, autoGeneratedKeys);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
		return result;
	}

	public int executeUpdate(String sql, int[] columnIndexes)
		throws SQLException {
		int result = 0;
		try {
			result = statement.executeUpdate(sql, columnIndexes);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
		return result;
	}

	public int executeUpdate(String sql, String[] columnNames)
		throws SQLException {
		int result = 0;
		try {
			result = statement.executeUpdate(sql, columnNames);
		} catch (SQLException se) {
			Log.error(se.toString(), this);
			throw se;
		} finally {
		}
		return result;
	}

	private void saveParam(int idx, Object arg, String arg_type) {
		if (idx < 1)
			return;
		while (idx > params.size()) {
			params.add(null);
			params_type.add(null);
		}
		params.set(idx - 1, arg);
		params_type.add(idx - 1, arg_type);
	}

	public String getQueryString() {
		StringBuffer buf = new StringBuffer();
		int qMarkCount = 0;
		int qPos = 0;
		StringTokenizer tok = new StringTokenizer(sqlTemplate + " ", "?");		
		while (tok.hasMoreTokens()) {
			String oneChunk = tok.nextToken();
			buf.append(oneChunk);
			//Log.info("=== size :: " + params.size(), this);
			//Log.info("=== qMarkCount :: " + qMarkCount, this);
			try {
				Object value;
				String value_type = "";
				if (params.size() > 0 + qMarkCount) {
					qPos = qMarkCount++;
					value = params.get(0 + qPos);
					value_type = (String)params_type.get(0 + qPos);
				} else {
					if (tok.hasMoreTokens()) {
						value = null;
					} else {
						value = "";
					}
				}
				if ( value_type.equals("String"))
					buf.append("					'" + value+"'");
				else
					buf.append("					" + value);
			} catch (Throwable e) {
				buf.append("ERROR WHEN PRODUCING QUERY STRING FOR LOG."+ e.toString());
				Log.error(buf.toString(), this);
				// catch this without whining, if this fails the only thing wrong is probably this class
			}
		}
		return buf.toString().trim();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return statement.getGeneratedKeys();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return statement.getMoreResults(current);
	}

	public int getResultSetHoldability() throws SQLException {
		return statement.getResultSetHoldability();
	}

	
}
