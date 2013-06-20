package neo.util.comm;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

/**
 * 	@Class Name	: 	ServletUtil.java
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
class ServletUtil {

	public static void showHeader(HttpServletRequest req) {
		ServletUtil.showHeader(req, System.out);
	}

	public static void showHeader(HttpServletRequest req, PrintStream out) {
		Enumeration e = req.getHeaderNames();
		out.println("** HttpServletRequest Header Information **");
		String name = null;
		while (e.hasMoreElements()) {
			name = (String) e.nextElement();
			out.println("- " + name + " : " + req.getHeader(name));
		}
		out.println("*******************************************\n");
	}

	public static String getParameter(HttpServletRequest req, String sName) {
		String value = req.getParameter(sName.trim());
		String retr = (value == null ? "" : value.trim());
		return retr;
	}

	public static String getParameter(
		HttpServletRequest req,
		String pName,
		String defaultValue) {

		String value = req.getParameter(pName.trim());
		String retr = (value == null ? defaultValue : value.trim());

		return retr;
	}

	public static String getParameter(
		HttpServletRequest req,
		String pName,
		String defaultValue,
		String encodingFlag) {

		String value = req.getParameter(pName.trim());
		String retr = (value == null ? defaultValue : StringUtil.e2k(value.trim()));
		return retr;
	}

	public static boolean getParameterAsBoolean(
		HttpServletRequest req,
		String sName,
		boolean defaultValue) {
		String value = req.getParameter(sName);

		if (null == value) {
			return defaultValue;
		}

		value = value.trim().toUpperCase();
		boolean retr;
		if (value.equals("TRUE") || 
			value.equals("ON") || 
			value.equals("1")) {
			retr = true;
		} else if (
			value.equals("FALSE") || 
			value.equals("OFF") || 
			value.equals("0")) {
			retr = false;
		} else {
			retr = defaultValue;
		}

		return retr;
	}

	public static String[] getParameter(
		HttpServletRequest req,
		String key,
		int length) {

		String[] retr = new String[length];
		String[] values = req.getParameterValues(key);

		for (int i = 0; i < length; i++) {
			if (i < values.length) {
				retr[i] = (values[i] == null ? "" : values[i].trim());
			} else {
				retr[i] = "";
			}
		}

		return retr;
	}

}
