package neo.page;

import neo.page.PageConstants;
import neo.data.TData;

/**
 * 	@Class Name	: 	CommonPageDao.java
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
public class CommonPageDao extends AbstractPageDao {

	public CommonPageDao(String dataSourceInfo,
						 String pageSpec,
						 TData pageData) {
		super(dataSourceInfo, pageSpec);
		pageStatement = new CommonPageStatement(pageSpec, pageData);
	}

	public CommonPageDao(TData pageData) {
		super(PageConstants.DEFAULT);
		this.pageStatement = new CommonPageStatement(pageSpec, pageData);
	}

	public CommonPageDao(String pageSpec, TData pageData) {
		super(pageSpec);
		pageStatement = new CommonPageStatement(pageSpec, pageData);
	}

}
