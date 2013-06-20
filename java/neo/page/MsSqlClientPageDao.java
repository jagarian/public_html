package neo.page;

import neo.page.PageConstants;
import neo.data.TData;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	MsSqlClientPageDao.java
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