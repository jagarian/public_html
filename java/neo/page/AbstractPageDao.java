package neo.page;

import java.sql.Connection;
import java.sql.SQLException;

import neo.page.PageConstants;
import neo.util.log.Log;
import neo.db.ConnectionManager;
import neo.data.Param;
import neo.data.MultiParam;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	AbstractPageDao.java
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
public abstract class AbstractPageDao {

	protected AbstractPageStatement pageStatement = null;
	protected String pageSpec = null;
	protected String dataSourceInfo = null;

	public AbstractPageStatement getPageStatement() {
		return pageStatement;
	}

	public void setDataSourceInfo(String dataSourceInfo) {
		this.dataSourceInfo = dataSourceInfo;
	}

	protected AbstractPageDao() {
		this.pageSpec = PageConstants.DEFAULT;
	}

	protected AbstractPageDao(String pageSpec) {
		this.pageSpec = pageSpec;
	}

	protected AbstractPageDao(String dataSourceInfo, String pageSpec) {
		this.dataSourceInfo = dataSourceInfo;
		this.pageSpec = pageSpec;
	}

	private final Connection getConnection() throws SQLException {
		if (this.dataSourceInfo == null)
			return ConnectionManager.getConnection();
		else
			return ConnectionManager.getConnection(dataSourceInfo);
	}

	public final MultiParam execute(String sql)
		throws PageException, SQLException {
		return execute(sql, null);
	}

	public MultiParam execute(String sql, Param paramData)
		throws PageException, SQLException {
		MultiParam rtnVBox = null;
		Connection con = null;
		try {
			con = getConnection();
			if (paramData != null)
				rtnVBox = pageStatement.execute(con, sql, paramData);
			else
				rtnVBox = pageStatement.execute(con, sql);
		} catch (SQLException e) {
			Log.info(e.getMessage(), this);
			throw e;
		} finally {
			ConnectionManager.closeConnection(con);
		}
		return rtnVBox;
	}

	public void setNumOfRowsOfPage(int numOfRowsOfPage) {
		this.pageStatement.setNumOfRowsOfPage(numOfRowsOfPage);
	}

	public void setNumberOfPagesOfIndex(int numberOfPagesOfIndex) {
		this.pageStatement.setNumberOfPagesOfIndex(numberOfPagesOfIndex);
	}

}
