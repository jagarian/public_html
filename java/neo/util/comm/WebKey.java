package neo.util.comm;

/**
 * 	@Class Name	: 	WebKey.java
 * 	@파일설명		: 	UI / BO 단에서 사용하는 일반적인 상수
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		:	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class WebKey {

	public static final String	SESSION_LISTENER_ID			= "_SESSION_LISTENER_0923_";//세션리스너ID	

	/**
	 * @title	로그인한 사용자에 관한 WebKey
	 */
	public static final String	SESSION_ID 					= "_SESSION_ID_"; 			// 세션ID
	public static final String	SESSION_USER_ID 			= "_LOGIN_USER_ID_"; 		// 사용자id
	public static final String	SESSION_USER_NAME 			= "_LOGIN_USER_NAME_"; 		// 사용자명 
	public static final String	SESSION_USER_DEPT 			= "_LOGIN_USER_DEPT_";		// 부서
	public static final String	SESSION_USER_EMAIL 			= "_LOGIN_USER_EMAIL_"; 	// 이메일
	public static final String	SESSION_USER_PWD 			= "_LOGIN_USER_PWD_"; 		// 암호
	public static final String	SESSION_USER_ROLE			= "_LOGIN_USER_ROLE_";		// 롤
	public static final String	SESSION_USER_LOGIN_CNT		= "_LOGIN_USER_LOGIN_CNT_";	// 로그인횟수
	public static final String	SESSION_LAST_LOGIN_DATE		= "_LOGIN_LAST_LOGIN_DATE_";// 최근로그인일자
	public static final String	SESSION_LAST_LOGIN_IP		= "_LOGIN_LAST_LOGIN_IP_";	// 최근로그인IP
}
