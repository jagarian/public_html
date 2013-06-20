package neo.util.security;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 	@Class Name	: 	Base64FileDecoder.java
 * 	@파일설명		: 	Sample program to decode a Base64 text file into a binary file.
 * 						Author: Christian d'Heureuse (www.source-code.biz)
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
public class Base64FileDecoder {

	public static void main(String args[]) throws IOException {
		if (args.length != 2) {
			System.out.println("Command line parameters: inputFileName outputFileName");
			System.exit(9);
		}
		decodeFile(args[0], args[1]);
	}

	private static void decodeFile(String inputFileName, String outputFileName)
			throws IOException {
		BufferedReader in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedReader(new FileReader(inputFileName));
			out = new BufferedOutputStream(new FileOutputStream(outputFileName));
			decodeStream(in, out);
			out.flush();
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	private static void decodeStream(BufferedReader in, OutputStream out)
			throws IOException {
		while (true) {
			String s = in.readLine();
			if (s == null)
				break;
			byte[] buf = Base64Coder.decode(s);
			out.write(buf);
		}
	}
}
