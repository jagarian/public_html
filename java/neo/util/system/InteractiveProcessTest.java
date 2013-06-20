package neo.util.system;

import java.io.*;

/**
 * 	@Class Name	: 	InteractiveProcessTest.java
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
