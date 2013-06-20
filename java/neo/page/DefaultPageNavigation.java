package neo.page;

import java.lang.reflect.Constructor;

import neo.data.Param;
import neo.data.TData;
import neo.data.MultiParam;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	DefaultPageNavigation.java
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
public class DefaultPageNavigation {

	protected Object resultMultiData;

	public Object getResultMultiData() {
		return this.resultMultiData;
	}

	protected MultiParam pageMultiData;

	public MultiParam getPageMultiData() {
		return this.pageMultiData;
	}

	protected PageNavigationIF pageNavigation;

	public PageNavigationIF getPageNavigation() {
		return this.pageNavigation;
	}

	private static MultiParam convertToMultiParam(Param pageData) {
		MultiParam pageMultiData = new MultiParam("PAGE_MULTI_DATA");
		pageMultiData.addParam(pageData);
		return pageMultiData;
	}

	private boolean isPageIndexType(int numberOfPagesOfIndex) {
		if (numberOfPagesOfIndex < 1)
			return false;
		else
			return true;

	}
	private AbstractPageNavigation getPageNavigationObject(MultiParam pageMultiData,
															String pageSpec)
		throws PageException {
		
		AbstractPageNavigation navigationObject = null;
		String navigationClassName =	PageConstants.getPageNavigationClassName(pageSpec);

		try {

			Class[] paramClassType = new Class[] { MultiParam.class };
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			Class navigationClass = classLoader.loadClass(navigationClassName);

			Constructor navigationClassConstructor = navigationClass.getConstructor(paramClassType);
			
			Object[] paramObjectVal = new Object[] { pageMultiData };
			
			navigationObject =	(AbstractPageNavigation) navigationClassConstructor.newInstance(paramObjectVal);

		} catch (Exception e) {
			throw new PageException("Instanciation Failed with Fully-Qualified class name[" + navigationClassName + "]Page Spec[" + pageSpec + "]", e);
		}
		return navigationObject;
	}

	public DefaultPageNavigation() {
	}

	public DefaultPageNavigation(TData pageData, MultiParam resultMultiData)
		throws PageException {
		MultiParam pageMultiData = null;

		if (pageData instanceof Param)
			pageMultiData = convertToMultiParam((Param) pageData);
		else
			pageMultiData = (MultiParam) pageData;
		
		Param pageIndexDataFromServer = (Param) resultMultiData.getParam(PageConstants.PAGE_INDEX, 0); 
		int targetRow = pageIndexDataFromServer.getInt(PageConstants.TARGET_ROW); 
		int rows = pageIndexDataFromServer.getInt(PageConstants.ROWS); 
		String pageSpec = pageIndexDataFromServer.getString(PageConstants.PAGE_SPEC); 

		// �Ķ���ͷ� �̸� ������ ���� �켱���� �ϰ� , ���� ����  ��쿡�� SPEC �� ���� ���� ���� �´�.
		int numberOfRowsOfPage = 0;
		// �Ķ���ͷ� NUMBER_OF_ROWS_OF_PAGE �� �����ߴ��� üũ�غ���.
		String NUMBER_OF_ROWS_OF_PAGE_STR =	pageIndexDataFromServer.getString(PageConstants.NUMBER_OF_ROWS_OF_PAGE);
		if (NUMBER_OF_ROWS_OF_PAGE_STR == null || 
			NUMBER_OF_ROWS_OF_PAGE_STR.equals("")) {
			numberOfRowsOfPage = PageConstants.getNumberOfRowsOfPage(pageSpec);
		} else {
			numberOfRowsOfPage =	pageIndexDataFromServer.getInt(PageConstants.NUMBER_OF_ROWS_OF_PAGE);
		}
		
		int numberOfPagesOfIndex = 0;
		// �Ķ���ͷ� NUMBER_OF_PAGES_OF_INDEX �� �����ߴ��� üũ�غ���.
		String NUMBER_OF_PAGES_OF_INDEX_STR = pageIndexDataFromServer.getString(
				PageConstants.NUMBER_OF_PAGES_OF_INDEX);
		if (NUMBER_OF_PAGES_OF_INDEX_STR == null || 
			NUMBER_OF_PAGES_OF_INDEX_STR.equals("")) {
			numberOfPagesOfIndex = PageConstants.getNumberOfPagesOfIndex(pageSpec);
		} else {
			numberOfPagesOfIndex = pageIndexDataFromServer.getInt(PageConstants.NUMBER_OF_PAGES_OF_INDEX);
		}
		
		AbstractPageNavigation navigationObject = getPageNavigationObject(pageMultiData, pageSpec);

		// ��ü �Ǽ� �� ��� ������ ���� ROW ����
		pageMultiData.addInt(PageConstants.ROWS, rows);
		pageMultiData.addInt(PageConstants.TARGET_ROW, targetRow);
		pageMultiData.addString(PageConstants.PAGE_SPEC, pageSpec);

		if (isPageIndexType(numberOfPagesOfIndex))
			navigationObject.setConfig(targetRow,
										rows,
										numberOfRowsOfPage,
										numberOfPagesOfIndex);
		else
			navigationObject.setConfig(targetRow, rows, numberOfRowsOfPage);

		this.pageMultiData = pageMultiData;
		this.pageNavigation = (PageNavigationIF) navigationObject;
		this.resultMultiData =	resultMultiData.get(PageConstants.PAGE_RESULT, 0);
	}

	public final int getCurrentPage() {
		return pageNavigation.getCurrentPage();
	}

	public final int getCurrentRow() {
		return pageNavigation.getCurrentRow();
	}

	public final int getPages() {
		return pageNavigation.getPages();
	}

	final public int getRows() {
		return pageNavigation.getRows();
	}

	public final String showIndex(String skinStr) {
		return pageNavigation.showIndex(skinStr);
	}

	public final String showMoveBeforeIndex(String skinStr) {
		return pageNavigation.showMoveBeforeIndex(skinStr);
	}

	public final String showMoveBeforePage(String skinStr) {
		return pageNavigation.showMoveBeforePage(skinStr);
	}

	public final String showMoveEndPage(String skinStr) {
		return pageNavigation.showMoveEndPage(skinStr);
	}

	public final String showMoveFirstPage(String skinStr) {
		return pageNavigation.showMoveFirstPage(skinStr);
	}

	public final String showMoveNextIndex(String skinStr) {
		return pageNavigation.showMoveNextIndex(skinStr);
	}

	public final String showMoveNextPage(String skinStr) {
		return pageNavigation.showMoveNextPage(skinStr);
	}

	public final String showSelectIndex() {
		return pageNavigation.showSelectIndex();
	}

	public String showSortField(String title, String orderBy, String skinStr) {
		return pageNavigation.showSortField(title, orderBy, skinStr);
	}

	public String showJavaScript() {
		return pageNavigation.showJavaScript();
	}

	public String showHiddenParam() {
		return pageNavigation.showHiddenParam();
	}
}
