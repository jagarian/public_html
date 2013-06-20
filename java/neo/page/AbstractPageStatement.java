package neo.page;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import neo.page.PageConstants;
import neo.data.Param;
import neo.data.TData;
import neo.data.MultiParam;
import neo.util.comm.ResultSetConverter;
import neo.util.log.Log;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	AbstractPageStatement.java
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
public abstract class AbstractPageStatement {

	protected String pageSpec = null;
	protected Param pageData = null;
	protected Param pageIndexData = null;

	public AbstractPageStatement(TData pageData) {
		this(PageConstants.DEFAULT, pageData);
	}

	public AbstractPageStatement(String pageSpec, TData pageData) {
		this.pageSpec = pageSpec;
		if (pageData instanceof Param) {
			((Param) pageData).putBoolean(PageConstants.REQUEST_DATA_SINGLE_MODE, true);
			this.pageData = (Param) pageData;
		} else {
			this.pageData =	SqlManager.convertLMultiDataToParam((MultiParam) pageData);
		}
	}

	private Param getParameterParam(Param paramData, String sql)
		throws PageException {
		SqlManager mySqlManager = new SqlManager(sql);
		return mySqlManager.getParameters(paramData);
	}

	private int getInt(String intExpectedStr, int defaultVal)
		throws PageException {
		int intVal = defaultVal;
		try {
			if (intExpectedStr == null || intExpectedStr.equals(""))
				return defaultVal;
			intVal = Integer.parseInt(intExpectedStr);

		} catch (NumberFormatException nfe) {
			throw new PageException(" Error Type-Converting Config Key[" + intExpectedStr + "]", nfe);
		}
		return intVal;
	}

	private void processPageIndex(Connection con, String sql, Param paramData)
		throws SQLException, PageException {
		String strTargetRow = this.pageData.getString(PageConstants.TARGET_ROW);
		int numberOfRowsOfPage = 0;
		// �Ķ���ͷ� NUMBER_OF_ROWS_OF_PAGE �� �����ߴ��� üũ�غ���.
		String NUMBER_OF_ROWS_OF_PAGE_STR =	this.pageData.getString(PageConstants.NUMBER_OF_ROWS_OF_PAGE);
		if (NUMBER_OF_ROWS_OF_PAGE_STR == null	|| NUMBER_OF_ROWS_OF_PAGE_STR.equals("")) {
			numberOfRowsOfPage =	PageConstants.getNumberOfRowsOfPage(this.pageSpec);
		} else {
			numberOfRowsOfPage =	this.pageData.getInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE);
		}
		int numberOfPagesOfIndex = 0;
		// �Ķ���ͷ� NUMBER_OF_PAGES_OF_INDEX �� �����ߴ��� üũ�غ���.
		String NUMBER_OF_PAGES_OF_INDEX_STR = this.pageData.getString(PageConstants.NUMBER_OF_PAGES_OF_INDEX);
		if (NUMBER_OF_PAGES_OF_INDEX_STR == null || NUMBER_OF_PAGES_OF_INDEX_STR.equals("")) {
			numberOfPagesOfIndex = PageConstants.getNumberOfPagesOfIndex(this.pageSpec);
		} else {
			numberOfPagesOfIndex = this.pageData.getInt(PageConstants.NUMBER_OF_PAGES_OF_INDEX);
		}
		int targetRow = getInt(strTargetRow, 1);
		int rows = getLastRow(con, sql, paramData);
		if (numberOfRowsOfPage < 0) {
			setNumOfRowsOfPage(rows); // 0 ���� ���� ��� ��ü Select
			numberOfRowsOfPage = rows;
		}
		// ���������� �ٸ� ������� �۾����� ��ü�Ǽ��� ����Ǿ�
		// targetRow �� �� �Ǽ� ���� Ŭ ���  �� ������ �������� ù��° row �� ����
		if (rows == 0) {
			targetRow = 1;
		} else if (targetRow > rows) {
			int targetRowPage = (rows / numberOfRowsOfPage);
			if (rows % numberOfRowsOfPage == 0)
				targetRowPage--;
			targetRow = targetRowPage * numberOfRowsOfPage + 1;
		}
		this.pageIndexData = new Param("PAGE_INDEX");
		this.pageIndexData.putInt(PageConstants.TARGET_ROW, targetRow);
		this.pageIndexData.putInt(PageConstants.ROWS, rows);
		this.pageIndexData.putString(PageConstants.PAGE_SPEC, this.pageSpec);
		this.pageIndexData.putInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE, numberOfRowsOfPage);
		this.pageIndexData.putInt(PageConstants.NUMBER_OF_PAGES_OF_INDEX, numberOfPagesOfIndex);
	}

	private String getPageSQL(String orgSQL)
		throws SQLException, PageException {
		String strOrderBy = this.pageData.getString(PageConstants.NEO_ORDER_BY);
		SqlManager mySqlManager = new SqlManager(orgSQL);
		return mySqlManager.replaceOrderByClause(strOrderBy);
	}

	private boolean NoRowsFound() throws SQLException, PageException {
		int rows = pageIndexData.getInt(PageConstants.ROWS);
		if (rows < 1)
			return true;
		else
			return false;
	}

	public MultiParam getPageIndexMultiParam(Object pageResult) {
		MultiParam pageResultAndIndex = new MultiParam("RESULT_INDEX");
		pageResultAndIndex.add(PageConstants.PAGE_RESULT, pageResult);
		pageResultAndIndex.addParam(PageConstants.PAGE_INDEX, this.pageIndexData);
		return pageResultAndIndex;
	}

	public Param getPageIndexParam() {
		return this.pageIndexData;
	}

	public MultiParam execute(Connection con, String sql, Param paramData)
		throws SQLException, PageException {

		processPageIndex(con, sql, paramData);
		
		if (NoRowsFound()) {
			MultiParam pageResult = new MultiParam("PAGE_RESULT");
			return getPageIndexMultiParam(pageResult);
		}

		MultiParam pageResult = execute(con,
										getPageSQL(sql),
										paramData,
										pageIndexData.getInt(PageConstants.TARGET_ROW),
										pageIndexData.getInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE));

		return getPageIndexMultiParam(pageResult);
	}

	public MultiParam execute(Connection con, String sql)
		throws SQLException, PageException {
		Param paramData = getParameterParam(this.pageData, sql);
		return execute(con, sql, paramData);
	}

	private void callSetMethod(PreparedStatement pstmt,
								int inx,
								String key,
								Object paramObj,
								Param paramData)
		throws SQLException, PageException {
		
		Class paramClassType = paramObj.getClass();
		
		if (paramClassType == String.class) {
			pstmt.setString(inx, paramData.getString(key));
		} else if (paramClassType == Integer.class) {
			pstmt.setInt(inx, paramData.getInt(key));
		} else if (paramClassType == Long.class) {
			pstmt.setLong(inx, paramData.getLong(key));
		} else if (paramClassType == Float.class) {
			pstmt.setFloat(inx, paramData.getFloat(key));
		} else if (paramClassType == Double.class) {
			pstmt.setDouble(inx, paramData.getFloat(key));
		} else if (paramClassType == Date.class) {
			pstmt.setDate(inx, (Date) paramData.get(key));
		} else if (paramClassType == Timestamp.class) {
			pstmt.setTimestamp(inx, (Timestamp) paramData.get(key));
		} else if (paramClassType == BigDecimal.class) {
			pstmt.setBigDecimal(inx, (BigDecimal) paramData.get(key));
		} else {
			throw new PageException("Error Unsupported Type - [" + paramClassType + "]");
		}

	}
	protected void setParamData(PreparedStatement pstmt, Param paramData)
		throws SQLException, PageException {
		setParamData(pstmt, paramData, 1);
	}

	protected void setParamData(PreparedStatement pstmt,
								Param paramData,
								int startInx)
		throws SQLException, PageException {

		if (paramData == null)
			throw new PageException("Error Unsupported Type - [ Parameter Data ] IS NULL ");
			
		int pstmtInx = startInx;
		
		Iterator iter = paramData.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			Object entryValObject = entry.getValue();
			// �Ķ���� �������� NULL �� ��쿡 �ڵ���ȯ�� �ش� �÷� Ÿ�����˼� ���� ������ �Ұ���
			// ���� Exception ���� ó�� �ϰ� , ���� �ٱ����� �̸� NULL üũ�� ��ȯ�� �Ŀ�
			// �Ķ���͸� �����ϴ� ������ ���̵� �Ѵ�.
			if (entryValObject == null)
				throw new PageException("Error Unsupported Type - [ Parameter Object ] IS NULL ");
			callSetMethod(pstmt, pstmtInx, key, entryValObject, paramData);
			pstmtInx++;
		}
	}

	protected PreparedStatement getPagedPreparedStatement(Connection conn,
															String sql,
															Param paramData,
		int row,
		int size)
		throws SQLException, PageException {
		PreparedStatement pstmt = null;
		try {
			final String neoSql = makePageSql(sql, paramData, row, size);
			pstmt = conn.prepareStatement(neoSql);
			setParamData(pstmt, paramData);

		} catch (SQLException e) {
			Log.info(e.getMessage(), this);
			throw e;
		}
		return pstmt;
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
			rs = pstmt.executeQuery();
			pageResult = ResultSetConverter.toMultiParam(rs);
		} catch (SQLException e) {
			Log.info(e.getMessage(), this);
			throw e;
		} finally {
			close(rs);
			close(pstmt);
		}

		return pageResult;

	}

	public PreparedStatement getPagedPreparedStatement(Connection con,
														String sql)
		throws PageException, SQLException {
		Param paramData = getParameterParam(this.pageData, sql);
		return getPagedPreparedStatement(con, sql, paramData);
	}

	public PreparedStatement getPagedPreparedStatement(Connection con,
														String sql,
														Param paramData)
		throws PageException, SQLException {
		
		processPageIndex(con, sql, paramData);

		PreparedStatement lpstmt =	getPagedPreparedStatement(con,
															getPageSQL(sql),
															paramData,
															pageIndexData.getInt(PageConstants.TARGET_ROW),
															pageIndexData.getInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE));
		return lpstmt;
	}

	protected abstract int getLastRow(Connection conn,
										String sql,
										Param paramData)
		throws SQLException, PageException;

	protected abstract String makePageSql(String rawSql,
											Param paramData,
											int targetRow,
											int pageSize)
		throws SQLException;

	protected final void close(PreparedStatement pstmt) throws SQLException {
		if (pstmt != null)
			pstmt.close();
	}

	protected final void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	public void setNumOfRowsOfPage(int numOfRowsOfPage) {
		this.pageData.putInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE, numOfRowsOfPage);
	}

	public void setNumberOfPagesOfIndex(int numberOfPagesOfIndex) {
		this.pageData.putInt(PageConstants.NUMBER_OF_PAGES_OF_INDEX, numberOfPagesOfIndex);
	}
}
