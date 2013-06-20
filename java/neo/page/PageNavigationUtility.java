package neo.page;

/**
 * 	@Class Name	: 	PageNavigationUtility.java
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
public final class PageNavigationUtility {

	private PageNavigationUtility() {
		super();
	}

	public static int getFirstPageOfIndex(int index, int numberOfPagesOfIndex) {
		return (index - 1) * numberOfPagesOfIndex + 1;
	}

	public static int getFirstRowOfPage(int page, int numberOfRowsOfPage) {
		return (page - 1) * numberOfRowsOfPage + 1;
	}

	public static int getIndexOfPage(int pages, int numberOfPagesOfIndex) {
		return (int) Math.ceil((float) pages / numberOfPagesOfIndex);
	}

	public static int getPageOfRow(int rows, int numberOfRowsOfPage) {
		return (int) Math.ceil((float) rows / numberOfRowsOfPage);
	}
}
