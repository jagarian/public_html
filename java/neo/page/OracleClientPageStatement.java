package neo.page;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import neo.page.PageConstants;
import neo.util.log.Log;
import neo.data.Param;
import neo.data.TData;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	OracleClientPageStatement.java
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
public class OracleClientPageStatement extends AbstractPageStatement {

	public OracleClientPageStatement(TData pageData) {
		super(pageData);
	}

	public OracleClientPageStatement(String pageSpec, TData pageData) {
		super(pageSpec, pageData);
	}

	protected int getLastRow(	java.sql.Connection conn,
											String sql,
											Param paramData)
		throws SQLException, PageException {
		
		int retRows = 0;
		StringBuffer neoSql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		neoSql.append("select count(*) from (")
		//.append(SqlManager.removeOrderByClause(sql))
		.append(sql).append(") neoSubQuery ");

		try {
			pstmt = conn.prepareStatement(neoSql.toString());
			setParamData(pstmt, paramData);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				retRows = rs.getInt(1);
			}

		} catch (SQLException e) {
			Log.info(e.getMessage());
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}

		return retRows;
	}

	protected String makePageSql(	String rawSql,
									Param paramData,
									int targetRow,
									int pageSize) {

		StringBuffer retSql = new StringBuffer();

		retSql
			.append("select ")
			.append(" * ")
			.append(" from ")
			.append("(select ")
			.append(" inner_temp.* , rownum as neoindex ")
			.append(" from ")
			.append("(")
			.append(rawSql)
			.append(") inner_temp )")
			.append(" where neoindex between ? and ?");

		paramData.putString(PageConstants.PAGING_ROW_START, targetRow + "");
		paramData.putString(PageConstants.PAGING_ROW_END, (targetRow + pageSize - 1) + "");

		return retSql.toString();
	}

}
