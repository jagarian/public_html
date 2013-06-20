package neo.page;

import neo.page.PageConstants;
import neo.data.TData;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	MsSqlClientPageDao.java
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
public final class MsSqlClientPageDao extends AbstractPageDao {

	public MsSqlClientPageDao(String dataSourceInfo, String pageSpec, TData pageData)
		throws PageException {
		super(dataSourceInfo, pageSpec);
		pageStatement = new MsSqlClientPageStatement(pageSpec, pageData);
	}

	public MsSqlClientPageDao(TData pageData) throws PageException {
		super(PageConstants.DEFAULT);
		pageStatement = new MsSqlClientPageStatement(pageSpec, pageData);
	}

	public MsSqlClientPageDao(String pageSpec, TData pageData) throws PageException {
		super(pageSpec);
		pageStatement = new MsSqlClientPageStatement(pageSpec, pageData);
	}
}