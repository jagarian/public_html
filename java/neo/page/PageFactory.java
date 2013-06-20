package neo.page;

import java.lang.reflect.Constructor;

import neo.page.PageConstants;
import neo.data.TData;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	PageFactory.java
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
public class PageFactory {
	final String dataSourceInfo;

	public PageFactory() {
		dataSourceInfo = null;
	}

	public PageFactory(String dataSourceInfo) {
		this.dataSourceInfo = dataSourceInfo;
	}

	public AbstractPageDao createDao(TData pageData) throws PageException {
		return createDao(PageConstants.DEFAULT, pageData);
	}

	public AbstractPageDao createDao(String pageSpec, TData pageData)
		throws PageException {

		AbstractPageDao pageDao = null;
		String pageDaoClassName = PageConstants.getPageDaoClassName(pageSpec);

		try {
			Class[] paramClassType = new Class[] { String.class, String.class, TData.class };

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class pageDaoClass = classLoader.loadClass(pageDaoClassName);

			Constructor pageDaoClassConstructor = pageDaoClass.getConstructor(paramClassType);
			Object[] paramObjectVal =	new Object[] { this.dataSourceInfo, pageSpec, pageData };
			pageDao =	(AbstractPageDao) (pageDaoClassConstructor.newInstance(paramObjectVal));

		} catch (Exception e) {
			throw new PageException("Instanciation Failed with Fully-Qualified class name[" + pageDaoClassName + "]Page Spec[" + pageSpec + "]"	+ e.getMessage(),	e);
		}

		return pageDao;
	}
	public AbstractPageStatement createStatement(TData pageData) throws PageException {
		return createStatement(PageConstants.DEFAULT, pageData);
	}

	public AbstractPageStatement createStatement(String pageSpec, TData pageData)
		throws PageException {

		AbstractPageStatement pageStatement = null;

		String pageStatementClassName = PageConstants.getPageStatementClassName(pageSpec);

		try {
			Class[] paramClassType = new Class[] { String.class, TData.class };
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class pageStatementClass = classLoader.loadClass(pageStatementClassName);

			Constructor pageStatementClassConstructor = pageStatementClass.getConstructor(paramClassType);
			Object[] paramObjectVal = new Object[] { pageSpec, pageData };
			pageStatement = (AbstractPageStatement) (pageStatementClassConstructor.newInstance(paramObjectVal));

		} catch (Exception e) {
			throw new PageException("Instanciation Failed with Fully-Qualified class name[" + pageStatementClassName + "]Page Spec[" + pageSpec + "]"	+ e.getMessage(), e);
		}
		return pageStatement;
	}
}
