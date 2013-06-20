package neo.util.page;

/**
 * 	@Class Name	: 	PageList.java
 * 	@파일설명		:	UI단에서 데이타를 Page 형식으로 관리하기 위한 Entity Class 	
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
public class PageList {
	int viewRowCnt = 15; 		// 한 페이지에 보여줄 Row 개수 (Default)
	int viewPageCnt = 10; 		// 한 페이지에 보여줄 페이지 개수 (Default)
	int totalRowCnt = 0; 		// 전체 Row 개수
	int currPage = 0; 			// 현재 페이지
	int totalPageCnt = 0; 		// 전체 페이지 개수
	int startPageCnt = 0; 		// Start 페이지
	int endPageCnt = 0; 		// End   페이지
	int currPageListCnt = 0;	// 현재 작업중인 페이지리스트의 번호
	
	/**
	 * 한페이지에 보여줄 Row개수를 리턴한다.	
	 * 
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int getViewRowCnt() {
		return this.viewRowCnt;
	}
	
	/**
	 * XML 문서를 파싱하여 루트요소를 얻어낸 후, visitChild() 함수를 호출하는 메소드
	 * 
	 * @return
	 */
	public int getViewPageCnt() {
		return this.viewPageCnt;
	}

	/**
	 * 현재 작업중인 Page개수를 리턴한다.	
	 * 
	 * @return
	 */
	public int getCurrPage() {
		return this.currPage;
	}
	
	/**
	 * 현재 작업중인 PageList를 리턴한다. ('다음 4개페이지'를 하나의 단위기준으로 한다.)	
	 * 
	 * @param 
	 * @return 
	 * @exception 
	 */
	public int getCurrPageListCnt() {
		return this.currPageListCnt;
	}

	/**
	 * 한페이지에 보여줄 Row개수를 리턴한다.	
	 * 
	 * @return
	 */
	public int getTotalPageCnt() {
		return this.totalPageCnt;
	}

	/**
	 * 화면에 보여줄 처음 페이지 번호
	 * 
	 * @return
	 */
	public int getStartPageCnt() {
		return this.startPageCnt;
	}

	/**
	 * 한페이지에 보여줄 Row개수를 리턴한다.	
	 * 
	 * @return
	 */
	public int getEndPageCnt() {
		return this.endPageCnt;
	}

	/**
	 * 전체 Row 개수를 설정한다.	
	 * 
	 * @return
	 */
	public int getTotalRowCnt() {
		return this.totalRowCnt;
	}
	
	/**
	 * 한페이지에 보여줄 Row개수를 설정한다.	
	 * 
	 * @return
	 */
	public void setViewRowCnt(int viewRowCnt) {
		this.viewRowCnt = viewRowCnt;
	}
	
	/**
	 * 한페이지에 보여줄 Page개수를 설정한다.	
	 * 
	 * @return
	 */
	public void setViewPageCnt(int viewPageCnt) {
		this.viewPageCnt = viewPageCnt;
	}

	/**
	 * @title		현재 작업중인 PageList를 설정한다.	
	 * @return	
	 */
	public void setCurrPageListCnt(int currPageListCnt) {
		this.currPageListCnt = currPageListCnt;
	}

	/**
	 * 현재 작업중인 Page개수를 지정한다.	
	 * 
	 * @return
	 */
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	/**
	 * 전체 Row 개수를 설정한다.	
	 * 
	 * @return
	 */
	public void setTotalRowCnt(int totalRowCnt) {
		this.totalRowCnt = totalRowCnt;
	}

	/**
	 * totalPageCnt, startPageCnt, endPageCnt Setting (마지막 셋팅 부분)	
	 * 
	 * @return
	 */
	public void makeEnv() {
		this.totalPageCnt = this.totalRowCnt / this.viewRowCnt + ((this.totalRowCnt % this.viewRowCnt > 0) ? 1 : 0);
		this.startPageCnt = currPageListCnt * viewPageCnt + 1;
		this.endPageCnt =	((startPageCnt + viewPageCnt - 1) > totalPageCnt) ? totalPageCnt : (startPageCnt + viewPageCnt - 1);
	}
	
	/**
	 * 화면에 보여주기 위한 Data For문의 Start변수를 리턴한다	
	 * 
	 * @return 
	 */
	public int getForStart() {
		return getCurrPage() * getViewRowCnt();
	}
}