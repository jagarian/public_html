package neo.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

import neo.page.PageConstants;
import neo.data.Param;
import neo.data.MultiParam;
import neo.data.TData;
import neo.util.comm.ResultSetConverter;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	MsSqlClientPageStatement.java
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
public class MsSqlClientPageStatement extends AbstractPageStatement {

	private String keyField = "";

	private void throwException_If_MSSQL_KEY_FIELD_Is_Not_Set()
		throws PageException {
		String keyFieldExpected = pageData.getString(PageConstants.MSSQL_KEY_FIELD);
		if (keyFieldExpected == null || keyFieldExpected.equals("")) {
			throw new PageException("Error 'MSSQL_KEY_FIELD' Is NOT Set in Parameter Data ( from HTTPServletRequest ).");
		}
		this.keyField = keyFieldExpected;
	}

	public MsSqlClientPageStatement(TData pageData) throws PageException {
		super(pageData);
		throwException_If_MSSQL_KEY_FIELD_Is_Not_Set();
	}

	public MsSqlClientPageStatement(String pageSpec, TData pageData)
		throws PageException {
		super(pageSpec, pageData);
		throwException_If_MSSQL_KEY_FIELD_Is_Not_Set();
	}

	protected int getLastRow(java.sql.Connection conn,
								String sql,
								Param paramData)
		throws SQLException, PageException {
		int retRows = 0;
		StringBuffer neoSql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		neoSql.append("select count(*) from (").append( removeOrderByClause(sql)).append(") neoSubQuery ");
		try {
			pstmt = conn.prepareStatement(neoSql.toString());
			setParamData(pstmt, paramData);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retRows = rs.getInt(1);
			}
		} catch (SQLException e) {
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

		int totalRecords = this.pageIndexData.getInt(PageConstants.ROWS);

		targetRow = targetRow - 1;

		StringBuffer retSql = new StringBuffer();
		rawSql = processOrderByClause(rawSql, totalRecords, this.keyField);

		retSql
			.append(" select TOP ")
			.append(pageSize)
			.append(" * ")
			.append(" from ")
			.append("(")
			.append(rawSql)
			.append(") NEO_TEMP_TABLE_1 ")
			.append(" where ")
			.append(this.keyField)
			.append(" NOT IN ")
			.append(" ( select TOP ")
			.append(targetRow)
			.append("  ")
			.append(this.keyField)
			.append("   from ")
			.append("(")
			.append(rawSql)
			.append(") NEO_TEMP_TABLE_2 ")
			.append(" ) ");

		return retSql.toString();
	}

	protected MultiParam execute(Connection conn,
									String sql,
									Param paramData,
									int row,
									int size)
		throws PageException, SQLException {
		MultiParam pageResult = new MultiParam("PAGE_RESULT");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = getPagedPreparedStatement(conn, sql, paramData, row, size);
			setParamData(pstmt, paramData, paramData.size() + 1);
			rs = pstmt.executeQuery();
			pageResult = ResultSetConverter.toMultiParam(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}
		return pageResult;

	}

	private String[] splitOrderByClause(String orgSql) {

		Matcher sqlMatch = PageConstants.SQL_ORDER_BY_PATTERN.matcher(orgSql);

		int lastOrderByMatchStartIndex = 0;
		while (sqlMatch.find()) {
			lastOrderByMatchStartIndex = sqlMatch.start();
		}

		if (lastOrderByMatchStartIndex == 0)
			return new String[] { orgSql, "" };

		int positionOfOrderBy = lastOrderByMatchStartIndex;
		final String candidateForOrderByClause = orgSql.substring(positionOfOrderBy);

		char[] candidateCharacters = candidateForOrderByClause.toCharArray();
		final int size = candidateCharacters.length;
		int balance = 0;
		for (int inx = 0; inx < size; inx++) {
			if (candidateCharacters[inx] == ')')
				balance++;
			else if (candidateCharacters[inx] == '(')
				balance--;
		}

		if (balance != 0)
			return new String[] { orgSql, "" };
		String withoutOrderBy = orgSql.substring(0, positionOfOrderBy);
		String OrderByClase = orgSql.substring(positionOfOrderBy);
		return new String[] { withoutOrderBy, OrderByClase };
	}

	private String removeOrderByClause(String orgSql) {
		return splitOrderByClause(orgSql)[0];
	}

	private String processOrderByClause(String orgSql,
										int topSize,
										String keyField) {
		String[] sqlPartArray = splitOrderByClause(orgSql);
		String orderByAppliedString = " select TOP "
									+ topSize
									+ " *  from ( "
									+ sqlPartArray[0]
									+ " ) NEO_TEMP_TABLE ";
		if (sqlPartArray[1] == null || sqlPartArray[1].equals(""))
			orderByAppliedString += " ORDER BY " + keyField;
		else
			orderByAppliedString += sqlPartArray[1];
		return orderByAppliedString;
	}
}
