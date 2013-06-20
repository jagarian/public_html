package neo.page;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import neo.page.PageConstants;
import neo.data.Param;
import neo.data.TData;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	MySqlClientPageStatement.java
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
public class MySqlClientPageStatement extends AbstractPageStatement {

	public MySqlClientPageStatement(TData pageData) {
		super(pageData);
	}

	public MySqlClientPageStatement(String pageSpec, TData pageData) {
		super(pageSpec, pageData);
	}

	protected int getLastRow(	java.sql.Connection conn,
								String sql,
								Param paramData)
		throws SQLException, PageException {
		int retRows = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String neoSql = " select COUNT(*) from ( " + sql + " ) neoSubQuery  ";
			pstmt = conn.prepareStatement(neoSql);
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
		rawSql = " select * from ( " + rawSql + " ) neoSubQuery limit ? , ? ";
		paramData.putInt(PageConstants.PAGING_ROW_START, (targetRow - 1));
		paramData.putInt(PageConstants.PAGING_ROW_SIZE, pageSize);
		return rawSql;
	}
}
