package neo.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.*;
import java.applet.*;

/**
 * 	@Class Name	: 	FileInflater.java
 * 	@���ϼ���		: 	�ͽ��÷η� �󿡼� applet�� �̿��Ͽ� ���ڸ� ��������, bytearray�� ��Ʈ������ ��ȯ, 
 * 						http�� �������� �����Ͽ� ���������� �ٽ� bytearray�������� ������ Ǯ�� ��Ʈ������ ��ȯ�մϴ�. 
 * 						���� ���۽� Ư�����ڿ� ������� ������ �����ϵ��� �մϴ�.
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
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
	 * (a.unzipping("u cant watch korean �ѱ�")); }
	 */
}