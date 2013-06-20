package neo.util.exception;

import java.io.*;

/**
 * 	@Class Name	: 	ExceptionUtil.java
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
public class ExceptionUtil {
	public static String getStackTraceString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String traceString = "";

		try {
			t.printStackTrace(pw);
			traceString = sw.toString();
			sw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return traceString;
	}

	public static String getStackTraceUnitString(Exception e, int level) {
		String s = getStackTraceString(e);
		String delim = "at ";
		int index = 0;

		for (int i = 0; i < level; i++) {
			index = s.indexOf(delim, index) + 3;
		}

		return s.substring(index, s.indexOf(delim, index)).trim();
	}

	public static String getStackTrace(Throwable t) {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		PrintWriter printwriter = new PrintWriter(bytearrayoutputstream);
		t.printStackTrace(printwriter);
		printwriter.flush();
		return bytearrayoutputstream.toString();
	}
}
