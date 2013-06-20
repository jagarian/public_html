package neo.util.system;

import java.io.*;

/**
 * 	@Class Name	: 	ProcessTest.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
public class ProcessTest {

	static public void main(String args[]) {

		try {

			Process p1 = Runtime.getRuntime().exec("calc.exe");

			Process p2 = Runtime.getRuntime().exec("freecell.exe");

			Process p3 = Runtime.getRuntime().exec("Notepad.exe");

			p1.waitFor();

			p2.destroy();

			System.out.println("Exit value of p1: " + p1.exitValue());

			System.out.println("Exit value of p2: " + p2.exitValue());

		} catch (IOException e) {

			System.out.println(e.getMessage());

		} catch (InterruptedException e) {

			System.out.println(e.getMessage());

		}

		System.exit(0);

	}

}
