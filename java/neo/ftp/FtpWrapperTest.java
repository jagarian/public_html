package neo.ftp;

import neo.ftp.SunFtpWrapper;
import java.io.*;

/**
  * a very simple example of using the SunFtpWrapper class,
  * available at http://www.nsftools.com/tips/JavaFtp.htm
  */

/**
 * 	@Class Name	: 	FtpWrapperTest.java
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
public class FtpWrapperTest {
	public static void main (String[] args) {
		try {
			SunFtpWrapper ftp = new SunFtpWrapper();
			String serverName = "mirrors.kernel.org";
			ftp.openServer(serverName);
			if (ftp.serverIsOpen()) {
				System.out.println("Connected to " + serverName);
				try {
					ftp.login("Anonymous", "me@whatever.com");
					System.out.println("Welcome message:\n" + ftp.welcomeMsg);
					System.out.println("Current Directory: " + ftp.pwd());
					System.out.println("Results of a raw LIST command:\n" + ftp.listRaw());
					System.out.println("Downloading file robots.txt");
					ftp.ascii();
					ftp.downloadFile("robots.txt", "C:\\robots.txt");
				} catch (Exception ftpe) {
					ftpe.printStackTrace();
				} finally {
					ftp.closeServer();
				}
			} else {
				System.out.println("Unable to connect to" + serverName);
			}
			System.out.println("Finished");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


