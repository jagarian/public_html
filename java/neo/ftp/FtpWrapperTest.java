package neo.ftp;

import neo.ftp.SunFtpWrapper;
import java.io.*;

/**
  * a very simple example of using the SunFtpWrapper class,
  * available at http://www.nsftools.com/tips/JavaFtp.htm
  */

/**
 * 	@Class Name	: 	FtpWrapperTest.java
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


