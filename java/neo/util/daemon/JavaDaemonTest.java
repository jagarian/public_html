package neo.util.daemon;

// JavaDaemonTest  -  A test program that can be used as a dummy daemon

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 	@Class Name	: 	JavaDaemonTest.java
 * 	@파일설명		: 	JavaDaemonTest  -  A test program that can be used as a dummy daemon
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
public class JavaDaemonTest {

	private static final int shutdownTime = 2; 	// shutdown hook delay time in
													// seconds

	public static void main(String[] args) {
		log("JavaDaemonTest started");
		System.err.println(getTimeStamp() + " This is output to StdErr...");
		Thread runtimeHookThread = new Thread() {
			public void run() {
				shutdownHook();
			}
		};
		Runtime.getRuntime().addShutdownHook(runtimeHookThread);
		try {
			while (true) {
				Thread.sleep(3000);
				log("running");
			}
		} catch (Throwable t) {
			log("Exception: " + t.toString());
		}
	}

	private static void shutdownHook() {
		log("ShutdownHook started");
		long t0 = System.currentTimeMillis();
		while (true) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				log("Exception: " + e.toString());
				break;
			}
			if (System.currentTimeMillis() - t0 > shutdownTime * 1000)
				break;
			log("shutdown");
		}
		log("ShutdownHook completed");
	}

	private static void log(String msg) {
		System.out.println(getTimeStamp() + " " + msg);
	}

	private static String getTimeStamp() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(new Date());
	}

}

