package neo.util.comm;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import neo.data.Param;
import neo.data.MultiParam;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	ResultSetConverter.java
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
public class ResultSetConverter {

	public static Param toParam(ResultSet rs) throws SQLException {

		try {

			Param ld = new Param("ResultSet");
			
			if (rs.next()) {

				ResultSetMetaData rsmd = rs.getMetaData();

				int numberOfColumns = rsmd.getColumnCount();

				String columnName = "";
				int columnType = 0;

				for (int i = 1; i <= numberOfColumns; i++) {
					
					columnName = DefaultNaming.getAttributeName(rsmd.getColumnName(i).toLowerCase());
							
					columnType = rsmd.getColumnType(i);

					if (columnType == Types.VARCHAR
						|| columnType == Types.CHAR) {

						ld.put(columnName, rs.getString(i));

					} else if (columnType == Types.NUMERIC) {

						BigDecimal bd = rs.getBigDecimal(i);
						if (bd == null) {
							ld.putString(columnName, "0");
						} else {
							if (bd.scale() > 0) {
								double floatExpected = bd.doubleValue();
								if (floatExpected < Float.MAX_VALUE) {
									ld.putFloat(columnName, bd.intValue());
								} else {
									ld.putDouble(columnName, floatExpected);
								}
							} else {
								long intExpected = bd.longValue();
								if (intExpected < Integer.MAX_VALUE) {
									ld.putInt(columnName, bd.intValue());
								} else {
									ld.putLong(columnName, intExpected);
								}
							}
						}
					} else {
						ld.put(columnName, rs.getObject(i));
					}
				}
			}
			return ld;
		} catch (SQLException se) {
			Log.error("error occured while extracting data from ResultSet", null);
			throw se;
		}
	}

	public static MultiParam toMultiParam(ResultSet rs) throws SQLException {
		try {
			MultiParam lmd = new MultiParam("ResultSet");
			InputStream insurancedata;
			StringBuffer dataBuffer = new StringBuffer();
			int chunk;
			int count = 0;

			while (rs.next()) {

				count++;

				ResultSetMetaData rsmd = rs.getMetaData();

				int numberOfColumns = rsmd.getColumnCount();

				String columnName = "";
				int columnType = 0;

				for (int i = 1; i <= numberOfColumns; i++) {
					
					columnName = DefaultNaming.getAttributeName(rsmd.getColumnName(i).toLowerCase());							
					columnType = rsmd.getColumnType(i);

					if (columnType == Types.VARCHAR || 
						columnType == Types.CHAR) {
						lmd.add(columnName, rs.getString(i));

					} else if (columnType == Types.LONGVARCHAR) {

						try {
							lmd.add(columnName, rs.getString(i));
						} catch (Exception ioe) {
							Log.info("error occured while extracting LONG data from ResultSet", null);
						}

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
									lmd.addDouble(columnName, floatExpected);
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

			return lmd;
			
		} catch (SQLException se) {
			Log.error("error occured while extracting data from ResultSet", null);
			throw se;
		}
	}

}