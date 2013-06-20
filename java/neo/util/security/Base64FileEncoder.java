package neo.util.security;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;

/**
 * 	@Class Name	: 	Base64FileEncoder.java
 * 	@파일설명		: 	Sample program to encode a binary file into a Base64 text file.
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
public class Base64FileEncoder {

	public static void main(String args[]) throws IOException {
		if (args.length != 2) {
			System.out.println("Command line parameters: inputFileName outputFileName");
			System.exit(9);
		}
		encodeFile(args[0], args[1]);
	}

	private static void encodeFile(String inputFileName, String outputFileName)
			throws IOException {
		BufferedInputStream in = null;
		BufferedWriter out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(inputFileName));
			out = new BufferedWriter(new FileWriter(outputFileName));
			encodeStream(in, out);
			out.flush();
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	private static void encodeStream(InputStream in, BufferedWriter out)
			throws IOException {
		int lineLength = 72;
		byte[] buf = new byte[lineLength / 4 * 3];
		while (true) {
			int len = in.read(buf);
			if (len <= 0)
				break;
			out.write(Base64Coder.encode(buf, len));
			out.newLine();
		}
	}
}
