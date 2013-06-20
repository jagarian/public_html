package neo.page;

/**
 * 	@Class Name	: 	PageNavigationIF.java
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
public interface PageNavigationIF {

	int getCurrentPage();
	int getCurrentRow();
	int getPages();
	int getRows();
	String showMoveBeforeIndex(String skinSt);
	String showMoveNextIndex(String skinStr);
	String showMoveFirstPage(String skinStr);
	String showMoveBeforePage(String skinStr);
	String showIndex(String skinStr);
	String showMoveNextPage(String skinStr);
	String showMoveEndPage(String skinStr);
	String showSelectIndex();
	String showSortField(String title,
						String orderByColumn,
						String skinStr);
	String showJavaScript();
	String showHiddenParam();
}
