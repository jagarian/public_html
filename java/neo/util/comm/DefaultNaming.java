package neo.util.comm;

import java.util.StringTokenizer;

/**
 * 	@Class Name	: 	DefaultNaming.java
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
