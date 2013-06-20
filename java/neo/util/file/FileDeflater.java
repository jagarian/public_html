package neo.util.file;

import java.io.*;
import java.util.zip.*;
import java.applet.*;
import java.util.*;

/**
 * 	@Class Name	: 	FileDeflater.java
 * 	@파일설명		: 	익스플로러 상에서 applet을 이용하여 문자를 압축한후, bytearray를 스트링으로 변환, 
 * 						http로 웹서버에 전송하여 웹서버에서 다시 bytearray추출한후 압축을 풀고 스트링으로 변환합니다. 
 * 						문자 전송시 특수문자에 상관없이 전송이 가능하도록 합니다.
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
public class FileDeflater extends Applet {
	public String zipping(String param) {
		System.out.println("in applet");
		try {
			byte[] unzip = param.getBytes();
			ByteArrayInputStream bif = new ByteArrayInputStream(unzip);

			ByteArrayOutputStream zipbof = new ByteArrayOutputStream();
			DeflaterOutputStream dos = new DeflaterOutputStream(zipbof);
			int position = 0;
			for (int read_byte = 0; (read_byte = bif.read()) != -1; position++) {
				dos.write(read_byte);
			}
			dos.finish();
			zipbof.flush();

			byte[] zipbyteArray = zipbof.toByteArray();
			// String returnZipString = new String(zipbyteArray);

			String returnbyteArray = "";
			for (int i = 0; i < zipbyteArray.length; i++) {
				if (i == (zipbyteArray.length - 1))
					returnbyteArray += zipbyteArray[i];
				else
					returnbyteArray += zipbyteArray[i] + ",";
			}
			return returnbyteArray;

		} catch (Exception ex) {
			return null;
		}
	}
	/*
	 * public static void main(String[] args) { FileDeflater a = new
	 * FileDeflater(); System.out.println (a.zipping("u cant watch korean 한글"));
	 * }
	 */
}