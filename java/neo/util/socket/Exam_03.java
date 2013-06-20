package neo.util.socket;

import java.io.*;
import java.net.*;

/**
 * 	@Class Name	: 	Exam_03.java
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
public class Exam_03 {

	public static void main(String[] ar) {
		Socket soc = null;
		InetAddress ia = null;

		try {

			ia = InetAddress.getByName("127.0.0.1");
		
			soc = new Socket(ia, 8545);

			String request = "GET http://thx4alice.tistory.com HTTP1/0\r\n\n";

			OutputStreamWriter osw = new OutputStreamWriter(soc.getOutputStream());

			BufferedWriter bos = new BufferedWriter(osw, 512);

			PrintWriter pw = new PrintWriter(bos);

			pw.println(request);

			pw.flush();

			InputStreamReader isr = new InputStreamReader(soc.getInputStream());

			BufferedReader br = new BufferedReader(isr, 512);

			while (true) {
				String str = br.readLine();

				if (str == null)
					break;

				System.out.println(str);
			}

			pw.close();
			
			br.close();

		} catch (IOException e) {
			System.out.println("Error :: "+e.getMessage());
		}

	}
}