package neo.page;

/**
 * 	@Class Name	: 	AbstractPageNavigation.java
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
public abstract class AbstractPageNavigation implements PageNavigationIF {

	private int currentRow;
	private int currentPage;
	private int currentIndex;

	private int numberOfRowsOfPage;
	private int numberOfPagesOfIndex;

	private int rows;
	private int pages;
	private int indexes;

	public AbstractPageNavigation() {
		super();
	}

	protected final int getCurrentIndex() {
		return currentIndex;
	}

	public final int getCurrentPage() {
		return currentPage;
	}

	public final int getCurrentRow() {
		return currentRow;
	}

	protected final int getIndexes() {
		return indexes;
	}

	protected final int getNumberOfPagesOfIndex() {
		return numberOfPagesOfIndex;
	}

	protected final int getNumberOfRowsOfPage() {
		return numberOfRowsOfPage;
	}

	public final int getPages() {
		return pages;
	}

	public final int getRows() {
		return rows;
	}

	protected final void setConfig(	int currentRow,
										int rows,
										int numberOfRowsOfPage) {
		this.currentRow = currentRow;
		this.rows = rows;
		this.numberOfRowsOfPage = numberOfRowsOfPage;
		this.currentPage =	PageNavigationUtility.getPageOfRow(this.currentRow, getNumberOfRowsOfPage());
		this.pages = PageNavigationUtility.getPageOfRow(rows, getNumberOfRowsOfPage());
		this.numberOfPagesOfIndex = pages;
		this.currentIndex =	PageNavigationUtility.getIndexOfPage(this.currentPage,	numberOfPagesOfIndex);
		this.indexes = PageNavigationUtility.getIndexOfPage( this.pages, numberOfPagesOfIndex);
	}

	protected final void setConfig(	int currentRow,
										int rows,
										int numberOfRowsOfPage,
										int numberOfPagesOfIndex) {
		this.currentRow = currentRow;
		this.rows = rows;
		this.numberOfRowsOfPage = numberOfRowsOfPage;
		this.currentPage = PageNavigationUtility.getPageOfRow(this.currentRow, getNumberOfRowsOfPage());
		this.pages = PageNavigationUtility.getPageOfRow(rows, getNumberOfRowsOfPage());
		this.numberOfPagesOfIndex = numberOfPagesOfIndex;
		this.currentIndex =	PageNavigationUtility.getIndexOfPage(this.currentPage, numberOfPagesOfIndex);
		this.indexes = PageNavigationUtility.getIndexOfPage(this.pages, numberOfPagesOfIndex);
	}
}
