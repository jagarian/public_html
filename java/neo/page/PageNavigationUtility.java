package neo.page;

/**
 * 	@Class Name	: 	PageNavigationUtility.java
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
