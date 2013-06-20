package neo.util.page;

/**
 * 	@Class Name	: 	PageList.java
 * 	@���ϼ���		:	UI�ܿ��� ����Ÿ�� Page �������� �����ϱ� ���� Entity Class 	
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
public class PageList {
	int viewRowCnt = 15; 		// �� �������� ������ Row ���� (Default)
	int viewPageCnt = 10; 		// �� �������� ������ ������ ���� (Default)
	int totalRowCnt = 0; 		// ��ü Row ����
	int currPage = 0; 			// ���� ������
	int totalPageCnt = 0; 		// ��ü ������ ����
	int startPageCnt = 0; 		// Start ������
	int endPageCnt = 0; 		// End   ������
	int currPageListCnt = 0;	// ���� �۾����� ����������Ʈ�� ��ȣ
	
	/**
	 * ���������� ������ Row������ �����Ѵ�.	
	 * 
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int getViewRowCnt() {
		return this.viewRowCnt;
	}
	
	/**
	 * XML ������ �Ľ��Ͽ� ��Ʈ��Ҹ� �� ��, visitChild() �Լ��� ȣ���ϴ� �޼ҵ�
	 * 
	 * @return
	 */
	public int getViewPageCnt() {
		return this.viewPageCnt;
	}

	/**
	 * ���� �۾����� Page������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public int getCurrPage() {
		return this.currPage;
	}
	
	/**
	 * ���� �۾����� PageList�� �����Ѵ�. ('���� 4��������'�� �ϳ��� ������������ �Ѵ�.)	
	 * 
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int getCurrPageListCnt() {
		return this.currPageListCnt;
	}

	/**
	 * ���������� ������ Row������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public int getTotalPageCnt() {
		return this.totalPageCnt;
	}

	/**
	 * ȭ�鿡 ������ ó�� ������ ��ȣ
	 * 
	 * @return
	 */
	public int getStartPageCnt() {
		return this.startPageCnt;
	}

	/**
	 * ���������� ������ Row������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public int getEndPageCnt() {
		return this.endPageCnt;
	}

	/**
	 * ��ü Row ������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public int getTotalRowCnt() {
		return this.totalRowCnt;
	}
	
	/**
	 * ���������� ������ Row������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public void setViewRowCnt(int viewRowCnt) {
		this.viewRowCnt = viewRowCnt;
	}
	
	/**
	 * ���������� ������ Page������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public void setViewPageCnt(int viewPageCnt) {
		this.viewPageCnt = viewPageCnt;
	}

	/**
	 * @title		���� �۾����� PageList�� �����Ѵ�.	
	 * @return	
	 */
	public void setCurrPageListCnt(int currPageListCnt) {
		this.currPageListCnt = currPageListCnt;
	}

	/**
	 * ���� �۾����� Page������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	/**
	 * ��ü Row ������ �����Ѵ�.	
	 * 
	 * @return
	 */
	public void setTotalRowCnt(int totalRowCnt) {
		this.totalRowCnt = totalRowCnt;
	}

	/**
	 * totalPageCnt, startPageCnt, endPageCnt Setting (������ ���� �κ�)	
	 * 
	 * @return
	 */
	public void makeEnv() {
		this.totalPageCnt = this.totalRowCnt / this.viewRowCnt + ((this.totalRowCnt % this.viewRowCnt > 0) ? 1 : 0);
		this.startPageCnt = currPageListCnt * viewPageCnt + 1;
		this.endPageCnt =	((startPageCnt + viewPageCnt - 1) > totalPageCnt) ? totalPageCnt : (startPageCnt + viewPageCnt - 1);
	}
	
	/**
	 * ȭ�鿡 �����ֱ� ���� Data For���� Start������ �����Ѵ�	
	 * 
	 * @return 
	 */
	public int getForStart() {
		return getCurrPage() * getViewRowCnt();
	}
}