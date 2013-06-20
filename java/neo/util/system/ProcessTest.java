package neo.util.system;

import java.io.*;

/**
 * 	@Class Name	: 	ProcessTest.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
