package neo.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.*;
import java.applet.*;

/**
 * 	@Class Name	: 	FileInflater.java
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
public class FileInflater {
	public String unzipping(String param) {
		try {
			ArrayList zipArrayList = new ArrayList();
			StringTokenizer st = new StringTokenizer(param, ",");
			while (st.hasMoreTokens()) {
				zipArrayList.add(st.nextToken());
			}

			String[] zipArray = new String[zipArrayList.size()];
			for (int i = 0; i < zipArrayList.size(); i++) {
				zipArray[i] = (String) zipArrayList.get(i);
			}

			byte[] forUnZipByte = new byte[zipArray.length];
			for (int i = 0; i < zipArray.length; i++) {
				forUnZipByte[i] = (Byte.decode(zipArray[i])).byteValue();
			}

			ByteArrayInputStream bif = new ByteArrayInputStream(forUnZipByte);
			InflaterInputStream iis = new InflaterInputStream(bif);
			ByteArrayOutputStream unZipbof = new ByteArrayOutputStream();
			int position = 0;
			for (int read_byte = 0; (read_byte = iis.read()) != -1; position++) {
				unZipbof.write(read_byte);
			}
			unZipbof.flush();

			byte[] unZipbyteArray = unZipbof.toByteArray();
			String returnUnZipString = new String(unZipbyteArray);
			return returnUnZipString;
		} catch (Exception ex) {
			return null;
		}
	}
	/*
	 * public static void main(String[] args) { FileInflater a = new
	 * FileInflater(); System.out.println
	 * (a.unzipping("u cant watch korean 한글")); }
	 */
}