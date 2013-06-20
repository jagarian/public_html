package neo.util.comm;

/**
 * 	@Class Name	: 	TimerBean.java
 * 	@파일설명		: 	spentTime.jsp 의 내용은 아래 3줄이다
 * 
 * 	<jsp:useBean id="timer" class="bean.TimerBean" scope="session" />
 * 	<font color="8080FF" style="font-size:8pt">Surfing Time:
 * 	<jsp:getProperty name="timer" property="elapsedSeconds" /> Secs</font>
 * 	세션빈을 이용해서 사이트에서 머무는 시간을 표시해 줍니다.
 * 	<hr> &lt;jsp:include page="spentTime.jsp" flush="true" /><br>
 * 	<jsp:include page="spentTime.jsp" flush="true" />
 * 	<hr> &lt;%@ include file="spentTime.jsp" %><br>
 * 	<%@ include file="spentTime.jsp" %>  
 * 
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
public class TimerBean {
	private long start;

	public TimerBean() {
		start = System.currentTimeMillis();
	}

	public long getElapsedMillis() {
		long now = System.currentTimeMillis();
		return now - start;
	}

	public long getElapsedSeconds() {
		return (long) this.getElapsedMillis() / 1000;
	}

	public long getElapsedMinutes() {
		return (long) this.getElapsedMillis() / 60000;
	}

	public void reset() {
		start = System.currentTimeMillis();
	}

	public long getStartTime() {
		return start;
	}

	public void setStartTime(long time) {
		if (time <= 0)
			reset();
		else
			start = time;
	}
}
