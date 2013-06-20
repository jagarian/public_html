package neo.util.comm;

/**
 * 	@Class Name	: 	StopWatch.java
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
public class StopWatch {

	private static final long UNSET = -999999;
	int count = 0;
	long start = 0;
	long current = 0;
	long max = 0;
	long min = 0;

	public StopWatch() {
		reset();
	}

	public long getElapsed() {

		count++;
		long now = System.currentTimeMillis();
		long elapsed = (now - current);
		current = now;

		if (max == UNSET || elapsed > max) {
			max = elapsed;
		}
		if (min == UNSET || elapsed < min) {
			min = elapsed;
		}

		return elapsed;
	}

	public int getCount() {
		return count;
	}

	public long getMinTime() {
		return min;
	}

	public long getMaxTime() {
		return max;
	}

	public long getAvgTime() {
		if (count > 0) {
			return Math.round((double) (current - start) / (double) count);
		} else {
			return 0;
		}
	}

	public long getTotalElapsed() {
		current = System.currentTimeMillis();
		return (current - start);
	}

	public void reset() {
		start = System.currentTimeMillis();
		current = start;
		count = 0;
		max = 0;
		min = 0;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		float taskCount = getCount();
		float taskTime = getTotalElapsed() / 1000F;
		float taskMin = getMinTime() / 1000F;
		float taskMax = getMaxTime() / 1000F;
		float taskAvg = getAvgTime() / 1000F;
		buffer.append(	"\nCount : "
						+ taskCount
						+ "\nTotal : "
						+ taskTime
						+ "\nMax : "
						+ taskMax
						+ "\nMin : "
						+ taskMin
						+ "\nAvg : "
						+ taskAvg
						+ "\n");
		return buffer.toString();
	}
}