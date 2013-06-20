package neo.util.comm;

/**
 * 	@Class Name	: 	DateTest.java
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
public class DateTest {
	public static void main(String[] args) {
		int year = 2004;
		int month = 4;
		int day = 31;

		System.out.println("2004. 5. 31 :" + getDayOfWeek(2004, 5, 31));
		System.out.println("2002. 6. 1 :" + getDayOfWeek(2002, 6, 1));
		System.out.println("1972. 1. 2 :" + getDayOfWeek(1972, 1, 2));
		System.out.println("2004. 5. 1 - 2004.4.28 :" + dayDiff("2004-05-01", "2004-04-28"));
		System.out.println("2004. 6. 10 - 2004.6.3 :" + dayDiff("2004-06-10", "2004-06-03"));
	}

	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static int dayDiff(String fromDate, String toDate) {
		int year1 = 0, month1 = 0, day1 = 0;
		int year2 = 0, month2 = 0, day2 = 0;

		String[] date1 = fromDate.split("-");
		String[] date2 = toDate.split("-");

		year1 = Integer.parseInt(date1[0]);
		month1 = Integer.parseInt(date1[1]);
		day1 = Integer.parseInt(date1[2]);

		year2 = Integer.parseInt(date2[0]);
		month2 = Integer.parseInt(date2[1]);
		day2 = Integer.parseInt(date2[2]);

		int fromDay = convertDatetoDay(year1, month1, day1);
		int toDay = convertDatetoDay(year2, month2, day2);
		return fromDay - toDay;
	}

	public static int convertDatetoDay(int year, int month, int day) {
		int numOfLeapYear = 0; // 윤년의 수

		// 전년도까지의 윤년의 수를 구한다.
		for (int i = 0; i < year; i++) {
			if (isLeapYear(i))
				numOfLeapYear++;
		}

		// 전년도까지의 일 수를 구한다.
		int toLastYearDaySum = (year - 1) * 365 + numOfLeapYear;

		// 올해의 현재 월까지의 일수 계산
		int thisYearDaySum = 0;
		// 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
		int[] endOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		for (int i = 0; i < month - 1; i++) {
			thisYearDaySum += endOfMonth[i];
		}

		// 윤년이고, 2월이 포함되어 있으면 1일을 증가시킨다.
		if (month > 2 && isLeapYear(year)) {
			thisYearDaySum++;
		}

		thisYearDaySum += day;

		return toLastYearDaySum + thisYearDaySum - 1;
	}

	public static int getDayOfWeek(int year, int month, int day) {
		// 0~6의 값을 반환한다. 결과가 0이면 일요일이다.
		return convertDatetoDay(year, month, day) % 7;
	}
}
