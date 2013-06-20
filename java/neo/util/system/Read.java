package neo.util.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 	@Class Name	: 	Read.java
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
