package neo.page;

import neo.page.PageConstants;
import neo.data.TData;

/**
 * 	@Class Name	: 	OracleClientPageDao.java
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
public final class OracleClientPageDao extends AbstractPageDao {

	public OracleClientPageDao(String dataSourceInfo,
								String pageSpec,
								TData pageData) {
		super(dataSourceInfo, pageSpec);
		pageStatement = new OracleClientPageStatement(pageSpec, pageData);
	}

	public OracleClientPageDao(TData pageData) {
		super(PageConstants.DEFAULT);
		pageStatement = new OracleClientPageStatement(pageSpec, pageData);
	}

	public OracleClientPageDao(String pageSpec, TData pageData) {
		super(pageSpec);
		pageStatement = new OracleClientPageStatement(pageSpec, pageData);
	}

}
