package neo.thread;

/**
 * 	@Class Name	: 	TestDump.java
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
public class TestDump {

	public static void main(String args[]) {

		int TR_NUM = 3;
		Thread trds[] = new Thread[TR_NUM];
		for (int i = 0; i < TR_NUM; i++) {
			trds[i] = new TestTrd();
			trds[i].start();
		}

		// 5초마다 쓰레드 덤프 스택
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			for (int i = 0; i < TR_NUM; i++) {
				StackTraceElement ste[] = trds[i].getStackTrace();
				System.out.println("---------------------Thread dump start index-" + i);
				for (int j = 0; j < ste.length; j++) {
					System.out.println(ste[j]);
				}
				System.out.println("---------------------Thread dump end index-" + i);
			}
		}
	}
}

class TestTrd extends Thread {
	boolean RUN = true;

	public void run() {
		while (RUN) {
			wait1();
			wait2();
		}
	}

	public void wait1() {
		try {
			Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void wait2() {
		try {
			Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
