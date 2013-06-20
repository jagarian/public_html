package neo.util.comm;

import java.util.StringTokenizer;

/**
 * 	@Class Name	: 	DefaultNaming.java
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
public class DefaultNaming {

	public static String getAttributeName(String columnName) {
		String result = "";
		StringTokenizer dbColumnName = new StringTokenizer(columnName, "_");
		int cnt = dbColumnName.countTokens();
		for (int i = 0; i < cnt; i++)
			if (i == 0) {
				result += dbColumnName.nextToken();
			} else {
				String str = dbColumnName.nextToken();
				result += str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
			}
		return result;
	}

	public static String getSetMethodName(String columnName) {
		String str = "";
		str = getAttributeName(columnName);
		return "set" + str.substring(0, 1).toUpperCase()	+ str.substring(1, str.length());
	}

	public static String getGetMethodName(String columnName) {
		String str = "";
		str = getAttributeName(columnName);
		return "get" + str.substring(0, 1).toUpperCase()	+ str.substring(1, str.length());
	}
}
