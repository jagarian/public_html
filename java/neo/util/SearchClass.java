package neo.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 	@Class Name	: 	SearchClass.java
 * 	@���ϼ���		: 	
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
public class SearchClass {

	BufferedWriter _fldfor;
	String a;
	String _fldif;
	int _flddo;

	public static void main(String args[]) {
		if (args.length != 2) {
			System.out.println("Usage: SearchClass searchkeyword position");
			System.exit(0);
		} else {
			(new SearchClass()).a(args[0].trim(), args[1].trim());
		}
	}

	public SearchClass() {
		a = "";
		_flddo = 0;
		_fldif = "./classinfo.txt";
	}

	public void a(String s, String s1) {
		a = s.trim();
		try {
			_fldfor = new BufferedWriter(new FileWriter(_fldif, false));
			a(new File(s1));
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		if (_fldfor != null) {
			try {
				_fldfor.close();
			} catch (IOException ioexception1) {
			}
		}
		return;
	}

	public void _mthdo(String s) {
		try {
			ZipFile zipfile = new ZipFile(s);
			ZipEntry zipentry;
			for (Enumeration enumeration = zipfile.entries(); enumeration
					.hasMoreElements(); a(zipentry.getName(), s, true)) {
				zipentry = (ZipEntry) enumeration.nextElement();
			}

		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	public void a(File file) throws IOException {
		if (file.isDirectory()) {
			String as[] = file.list();
			for (int i = 0; i < as.length; i++) {
				a(new File(file, as[i]));
			}

		} else {
			String s = file.getName().toLowerCase();
			if (!s.endsWith(".class")) {
				if ((s.endsWith(".jar") || s.endsWith(".zip")) && file.isFile()) {
					_mthdo(file.getPath());
				}
			} else {
				a(file.getPath());
			}
		}
	}

	private void a(String s) {
		a(s, "", false);
	}

	private void a(String s, String s1, boolean flag) {
		if (flag) {
			if (a.length() > 0) {
				if (s.toLowerCase().indexOf(a.toLowerCase()) != -1) {
					System.out.println(s1 + "-->" + s);
					_mthif(s1 + "-->" + s);
				}
			} else {
				System.out.println(s1 + "-->" + s);
				_mthif(s1 + "-->" + s);
			}
		} else if (a.length() > 0) {
			if (s.toLowerCase().indexOf(a.toLowerCase()) != -1) {
				System.out.println(s1 + "-->" + s);
				_mthif(s1 + "-->" + s);
			}
		} else {
			System.out.println(s);
			_mthif(s);
		}
	}

	private void _mthif(String s) {
		_flddo++;
		try {
			_fldfor.write(s + System.getProperty("line.separator"));
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}
}
