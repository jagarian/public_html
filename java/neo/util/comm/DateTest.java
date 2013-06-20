package neo.util.comm;

/**
 * 	@Class Name	: 	DateTest.java
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
		int numOfLeapYear = 0; // ������ ��

		// ���⵵������ ������ ���� ���Ѵ�.
		for (int i = 0; i < year; i++) {
			if (isLeapYear(i))
				numOfLeapYear++;
		}

		// ���⵵������ �� ���� ���Ѵ�.
		int toLastYearDaySum = (year - 1) * 365 + numOfLeapYear;

		// ������ ���� �������� �ϼ� ���
		int thisYearDaySum = 0;
		// 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
		int[] endOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		for (int i = 0; i < month - 1; i++) {
			thisYearDaySum += endOfMonth[i];
		}

		// �����̰�, 2���� ���ԵǾ� ������ 1���� ������Ų��.
		if (month > 2 && isLeapYear(year)) {
			thisYearDaySum++;
		}

		thisYearDaySum += day;

		return toLastYearDaySum + thisYearDaySum - 1;
	}

	public static int getDayOfWeek(int year, int month, int day) {
		// 0~6�� ���� ��ȯ�Ѵ�. ����� 0�̸� �Ͽ����̴�.
		return convertDatetoDay(year, month, day) % 7;
	}
}
