package neo.util.comm;

/**
 * 	@Class Name	: 	CurrentTime.java
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
public class CurrentTime {

	private CurrentTime() {
	}

	public static String currentTime() {
		return new StringBuffer()
			.append(mk2(currHH()))
			.append(':')
			.append(mk2(currMM()))
			.append(':')
			.append(mk2(currSS()))
			.toString();
	}
	public static String currentTimeMillis() {
		return new StringBuffer()
			.append(mk2(currHH()))
			.append(':')
			.append(mk2(currMM()))
			.append(':')
			.append(mk2(currSS()))
			.append('[')
			.append(mk3(currSSS()))
			.append(']')
			.toString();
	}

	private static long currHH() {
		long value = System.currentTimeMillis();
		return (value / (1000L * 60L * 60L) + 9) % 24L;
	}
	private static long currMM() {
		long value = System.currentTimeMillis();
		return (value / (1000L * 60L)) % 60L;
	}
	private static long currSS() {
		long value = System.currentTimeMillis();
		return (value / 1000L) % 60L;
	}
	private static long currSSS() {
		long value = System.currentTimeMillis();
		return value % 1000L;
	}
	private static String mk2(long value) {
		if (value < 10)
			return "0" + value;
		else
			return "" + (value);
	}
	private static String mk3(long value) {
		if (value < 10)
			return "00" + value;
		else if (value < 100)
			return "0" + value;
		else
			return "" + (value);
	}
}
