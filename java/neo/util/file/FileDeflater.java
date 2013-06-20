package neo.util.file;

import java.io.*;
import java.util.zip.*;
import java.applet.*;
import java.util.*;

/**
 * 	@Class Name	: 	FileDeflater.java
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
	 * FileDeflater(); System.out.println (a.zipping("u cant watch korean �ѱ�"));
	 * }
	 */
}