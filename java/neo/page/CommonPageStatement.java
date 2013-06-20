package neo.page;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import neo.data.Param;
import neo.data.TData;
import neo.data.MultiParam;
import neo.util.comm.DefaultNaming;
import neo.util.log.Log;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	CommonPageStatement.java
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
public class CommonPageStatement extends AbstractPageStatement {

	public CommonPageStatement(TData pageData) {
		super(pageData);
	}

	public CommonPageStatement(String pageSpec, TData pageData) {
		super(pageSpec, pageData);

	}

	protected int getLastRow(Connection conn, String sql, Param paramData)
		throws SQLException, PageException {
		int retRows = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String neoSql = sql;

		try {
			pstmt = conn.prepareStatement(neoSql);
			setParamData(pstmt, paramData);

			rs = pstmt.executeQuery();

			while (rs.next())
				retRows++;

		} catch (SQLException e) {
			Log.info(e.getMessage());
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}

		return retRows;
	}

	protected String makePageSql(String rawSql,
								Param paramData,
								int targetRow,
								int pageSize) {
		return rawSql;
	};

	protected MultiParam execute(Connection conn,
									String sql,
									Param paramData,
									int row,
									int pageSize)
		throws SQLException, PageException {

		final int readStartPoint = row;
		final int readEndPoint = row + pageSize - 1;
		int rows = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MultiParam lmd = new MultiParam("PAGE_RESULT");

		try {

			pstmt = conn.prepareStatement(sql);
			setParamData(pstmt, paramData);

			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			while (rs.next()) {
				rows++;
				if (rows >= readStartPoint && rows <= readEndPoint) {
					String columnName = "";
					int columnType = 0;

					for (int i = 1; i <= numberOfColumns; i++) {
						
						columnName = DefaultNaming.getAttributeName( rsmd.getColumnName(i).toLowerCase() );
						
						columnType = rsmd.getColumnType(i);

						if (columnType == Types.VARCHAR || 
						    columnType == Types.CHAR) {
						    	
							lmd.add(columnName, rs.getString(i));
							
						} else if (columnType == Types.NUMERIC) {
							
							BigDecimal bd = rs.getBigDecimal(i);
							
							if (bd == null) {
								lmd.addString(columnName, "0");
							} else {
								if (bd.scale() > 0) {
									double floatExpected = bd.doubleValue();
									if (floatExpected < Float.MAX_VALUE) {
										lmd.addFloat(columnName, bd.intValue());
									} else {
										lmd.addDouble(columnName,	floatExpected);
									}
								} else {
									long intExpected = bd.longValue();
									if (intExpected < Integer.MAX_VALUE) {
										lmd.addInt(columnName, bd.intValue());
									} else {
										lmd.addLong(columnName, intExpected);
									}
								}
							}
						} else {
							lmd.add(columnName, rs.getObject(i));
						}
					}
				}
			}

		} catch (SQLException e) {
			Log.info(e.getMessage());
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}

		return lmd;
	}

}