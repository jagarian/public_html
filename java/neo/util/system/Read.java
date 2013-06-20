package neo.util.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 	@Class Name	: 	Read.java
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
public class Read {

	public int getUnion(String text, String pattern) {

		int a = 0;
		String unionString = "";
		for (int i = 0; i < text.length(); i++) {

			if (unionString.indexOf(text.charAt(i)) == -1)
				unionString += text.charAt(i);
		}

		for (int i = 0; i < pattern.length(); i++) {

			if (unionString.indexOf(pattern.charAt(i)) == -1)
				unionString += pattern.charAt(i);
		}
		a = unionString.length();
		return a;

	}

	public static void main(String[] args) {
		try {
			// BufferedReader reader = new BufferedReader(new
			// FileReader(args[0]));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));

			while (true) {
				String _originalString = reader.readLine();
				String _subString = reader.readLine();

				Read re = new Read();
				int b = re.getUnion(_originalString, _subString);
				System.out.println(_originalString.length() + " "
						+ _subString.length() + " " + b);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
