package neo.ftp;

import neo.ftp.JakartaFtpWrapper;
import java.io.*;

/**
  * a very simple example of using the JakartaFtpWrapper class,
  * available at http://www.nsftools.com/tips/JavaFtp.htm
  */
/**
 * 	@Class Name	: 	JakartaWrapperTest.java
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
public class JakartaWrapperTest {
	public static void main (String[] args) {
		try {
			JakartaFtpWrapper ftp = new JakartaFtpWrapper();
			String serverName = "mirrors.kernel.org";
			if (ftp.connectAndLogin(serverName, "Anonymous", "me@whatever.com")) {
				System.out.println("Connected to " + serverName);
				try {
					System.out.println("Welcome message:\n" + ftp.getReplyString());
					System.out.println("Current Directory: " + ftp.printWorkingDirectory());
					ftp.setPassiveMode(true);
					System.out.println("Files in this directory:\n" + ftp.listFileNamesString());
					System.out.println("Subdirectories in this directory:\n" + ftp.listSubdirNamesString());
					System.out.println("Downloading file robots.txt");
					ftp.ascii();
					ftp.downloadFile("robots.txt", "C:\\robots.txt");
				} catch (Exception ftpe) {
					ftpe.printStackTrace();
				} finally {
					ftp.logout();
					ftp.disconnect();
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


