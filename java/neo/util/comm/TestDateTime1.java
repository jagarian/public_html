package neo.util.comm;

// Code snippet from www.source-code.biz.
// Author: Christian d'Heureuse (http://www.inventec.ch/chdh)

import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
 * 	@Class Name	: 	TestDateTime1.java
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
public final class TestDateTime1 {

	// Illustrates how to decode a time value and add it to todays date.
	public static void main(String args[]) throws Exception {
		int t = decodeTime("12:34:56");
		Date today = getTodaysDate();
		Date d = new Date(today.getTime() + t);
		System.out.println(d);
	}

	// Decodes a time value in "hh:mm:ss" format and returns it as milliseconds
	// since midnight.
	public static synchronized int decodeTime(String s) throws Exception {
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		f.setTimeZone(utcTimeZone);
		f.setLenient(false);
		ParsePosition p = new ParsePosition(0);
		Date d = f.parse(s, p);
		if (d == null || !isRestOfStringBlank(s, p.getIndex()))
			throw new Exception("Invalid time value (hh:mm:ss): \"" + s + "\".");
		return (int) d.getTime();
	}

	// Returns todays date without the time component.
	public static Date getTodaysDate() {
		return truncateDate(new Date());
	}

	// Truncates the time component from a date/time value.
	// (The default time zone is used to determine the begin of the day).
	public static Date truncateDate(Date d) {
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.clear();
		gc1.setTime(d);
		int year = gc1.get(Calendar.YEAR), month = gc1.get(Calendar.MONTH), day = gc1
				.get(Calendar.DAY_OF_MONTH);
		GregorianCalendar gc2 = new GregorianCalendar(year, month, day);
		return gc2.getTime();
	}

	// Returns true if string s is blank from position p to the end.
	public static boolean isRestOfStringBlank(String s, int p) {
		while (p < s.length() && Character.isWhitespace(s.charAt(p)))
			p++;
		return p >= s.length();
	}

} // end class TestDateTime1

