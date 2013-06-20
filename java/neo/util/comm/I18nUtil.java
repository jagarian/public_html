package neo.util.comm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 	@Class Name	: 	I18nUtil.java
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
public final class I18nUtil {

	public static final String DEFAULT_CHARSET = "MS949";
	public static final String KOR_CHARSET = "MS949"; //EUC-KR
	public static final String ENG_CHARSET = "ISO-8859-1";

	private I18nUtil() {
	}

	/**
	 * @title	 ������ �ѱ۷� Conversion���ִ� ���α׷�.
	 * 
	 * @param english : �ѱ۷� �ٲپ��� ���� String
	 * @return	 �ѱ۷� �ٲپ��� String
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
	 * @title	 �ѱ��� �������� Conversion���ִ� ���α׷�.
	 * 
	 * @param korean : �������� �ٲپ��� �ѱ� String
	 * @return ������ �ٲپ��� String
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
