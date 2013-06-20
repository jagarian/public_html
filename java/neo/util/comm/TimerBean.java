package neo.util.comm;

/**
 * 	@Class Name	: 	TimerBean.java
 * 	@���ϼ���		: 	spentTime.jsp �� ������ �Ʒ� 3���̴�
 * 
 * 	<jsp:useBean id="timer" class="bean.TimerBean" scope="session" />
 * 	<font color="8080FF" style="font-size:8pt">Surfing Time:
 * 	<jsp:getProperty name="timer" property="elapsedSeconds" /> Secs</font>
 * 	���Ǻ��� �̿��ؼ� ����Ʈ���� �ӹ��� �ð��� ǥ���� �ݴϴ�.
 * 	<hr> &lt;jsp:include page="spentTime.jsp" flush="true" /><br>
 * 	<jsp:include page="spentTime.jsp" flush="true" />
 * 	<hr> &lt;%@ include file="spentTime.jsp" %><br>
 * 	<%@ include file="spentTime.jsp" %>  
 * 
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
