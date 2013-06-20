package neo.util.comm;

import java.util.List;

/**
 * 	@Class Name	: 	PagingHelper.java
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
public interface PagingHelper {

	/**
	 * 페이징 처리가 되는 페이지로 넘길 파라미터들을 세팅 한다.
	 * @param name
	 * @param value
	 */	
	public void setParameter(String name, String value);
	/**
	 * 현재 페이지의 레코드들을 얻는다.
	 * @return
	 * @throws Exception
	 */	
	public List getRecords() throws Exception;
	
	/**
	 * 현재 페이지의 페이징 처리과 된 html 문자열을 얻는다.
	 * @return
	 */
	public String getPageHtml();
	
	/**
	 * 각 페이지를 나타내는 문자열을 클릭하였을때 호출될 URI
	 * 보통 이 URI는 페이지를 표시하는 jsp 가 될 것이다.
	 * @param linkUrl
	 */
	public void setRequestURI(String linkUrl);
	
	/**
	 * 한 페이지에 표시될 레코드의 갯수(기본값:10)
	 * @param perPage
	 */
	public void setContentsPerPage(int perPage);
	
	/**
	 * 한 페이지에 표시될 페이지 번호의 갯수(기본값:10)
	 * @param perBlock
	 */	
	public void setPagesPerBlock(int perBlock);
	
	/**
	 * 표시할 페이지 번호를 전달할때 사용되는 파라미터 이름(기본값:page)
	 * @param pageParamName
	 */	
	public void setPageParamName(String pageParamName);
	
	/**
	 *  전체 레코드 갯수와 시작과 끝 번호 저장
	 * @param record_count
	 */
	public void setRecordCount(int record_count);

	/**
	 *  시작번호 호출
	 * @return
	 */
	public int getStart();
	
	/**
	 *  끝번호 호출
	 * @return
	 */
	public int getEnd();
	
	/**
	 *  한 페이지에 표시될 레코드의 갯수 정보
	 * @return
	 * @throws Exception
	 */
	public int getListSize() throws Exception;
	
	/**
	 *  MS-SQL에서 페이징 처리를 하기위한 서브쿼리의 레코드 수 (([해당 페이지]- 1) * [리스트사이즈])
	 * @return
	 * @throws Exception
	 */
	public int getSubListSize() throws Exception;
	
	/**
	 *  jsp 페이지에 가상 번호를 부여하기 위한 정보 번호 시작 부분(리스트의 첫번째로 나오는 번호)
	 * @return
	 * @throws Exception
	 */
	public int getListNumberStart() throws Exception;

	/**
	 * 표시할 페이지 번호를 전달할때 사용되는 파라미터 이름
	 * @return
	 */
	public String getPageParamName();
	
	/**
	 * 각 페이지를 나타내는 문자열을 클릭하였을때 호출될 URI
	 * @return
	 */
	
	public String getRequestURI();
	/**
	 * 한 페이지에 표시될 페이지 번호의 갯수(기본값:10)
	 * @return
	 */
	public int getPagesPerBlock();
	
	/**
	 * 한 페이지에 표시될 레코드의 갯수(기본값:10)
	 * @return
	 */
	public int getContentsPerPage();
}