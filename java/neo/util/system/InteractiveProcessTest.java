package neo.util.system;

import java.io.*;

/**
 * 	@Class Name	: 	InteractiveProcessTest.java
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
public class InteractiveProcessTest {

	public static void main(String[] args) {

		try {

			Process p = Runtime.getRuntime().exec("ping 203.252.134.126");

			byte[] msg = new byte[128];

			int len;

			while ((len = p.getInputStream().read(msg)) > 0) {

				System.out.print(new String(msg, 0, len));

			}

			String rs = "n";

			byte[] rb = new byte[] { (byte) 'n' }; // rs.getBytes();

			OutputStream os = p.getOutputStream();

			os.write(rb);

			os.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
