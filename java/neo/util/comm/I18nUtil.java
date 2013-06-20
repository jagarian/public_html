package neo.util.comm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 	@Class Name	: 	I18nUtil.java
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
public final class I18nUtil {

	public static final String DEFAULT_CHARSET = "MS949";
	public static final String KOR_CHARSET = "MS949"; //EUC-KR
	public static final String ENG_CHARSET = "ISO-8859-1";

	private I18nUtil() {
	}

	/**
	 * @title	 영문을 한글로 Conversion해주는 프로그램.
	 * 
	 * @param english : 한글로 바꾸어질 영문 String
	 * @return	 한글로 바꾸어진 String
	 */
	public static String E2K(String english) {
		String korean = null;

		if (english == null) {
			return null;
		}

		try {
			korean = new String(english.getBytes(ENG_CHARSET), KOR_CHARSET);
		} catch (UnsupportedEncodingException e) {
			korean = new String(english);
		}

		return korean;
	}

	/**
	 * @title	 한글을 영문으로 Conversion해주는 프로그램.
	 * 
	 * @param korean : 영문으로 바꾸어질 한글 String
	 * @return 영문로 바꾸어진 String
	 */
	public static String K2E(String korean) {
		String english = null;

		if (korean == null) {
			return null;
		}

		try {
			english = new String(korean.getBytes(KOR_CHARSET), ENG_CHARSET);
		} catch (UnsupportedEncodingException e) {
			english = new String(korean);
		}

		return english;
	}

	/**
	 * @title	 Method encode.
	 * 
	 * @param s :
	 * @return	 String
	 */
	public static String encode(String s) {
		try {
			return URLEncoder.encode(s, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

}
