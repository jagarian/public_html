package neo.util.file;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 	@Class Name	: 	CJarReader.java
 * 	@���ϼ���		: 	Jar file ���� �ִ� ���� �б� & ����
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
public class CJarReader {
	
	// //////////////////////////////////////////////////////////////////////////
	// �ܺ� ���Ͽ��� �ҷ� �� ��� �Ʒ� �ڵ�� ���� ȣ���Ͻÿ�!!!
	// String strFileNameSrc = "package �� / ... / file ��";
	// String strFileNameDst = "";
	// new CJarReader(this.getClass()).copyFile(strFileNameSrc, strFileNameDst);
	// //////////////////////////////////////////////////////////////////////////

	private JarFile jarFile;

	public CJarReader(Class c) throws Exception {
		String strJarFileName = getJarfileName(c);
		try {
			jarFile = new JarFile(strJarFileName);
		} catch (Exception e) {
			jarFile = null;
			throw (e);
		}
	}

	public String getJarfileName(Class c) {
		URL url = c.getProtectionDomain().getCodeSource().getLocation();
		String outputString = url.toString();
		String[] strsJarFileName;
		int index1 = outputString.indexOf(":");
		int index2 = outputString.lastIndexOf(":");
		if (index1 != index2)
			strsJarFileName = outputString.split("file:/");
		else
			strsJarFileName = outputString.split("file:");
		return strsJarFileName[1];
	}

	public InputStream getInputStream(String matchString) throws Exception {
		JarEntry jarEntry = jarFile.getJarEntry(matchString);
		InputStream is = jarFile.getInputStream(jarEntry);
		return is;
	}

	public void copyFile(String pStrInput, String pStrOutput) {
		int IO_BUFFER_SIZE = 4 * 1024;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = getInputStream(pStrInput);
			os = new FileOutputStream(pStrOutput);
			byte[] b = new byte[IO_BUFFER_SIZE];
			int read;
			while ((read = is.read(b)) != -1) {
				os.write(b, 0, read);
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
			} catch (Exception ex) {
			}
		}
	}
}
