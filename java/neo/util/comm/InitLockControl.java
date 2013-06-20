package neo.util.comm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	InitLockControl.java
 * 	@파일설명		: 	
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
public class InitLockControl {

	protected void doLock() {
		File locFile = new File(System.getProperty("user.home"), ".xdolock");
		boolean lock = false;
		while (!lock) {
			try {
				lock = locFile.createNewFile();
				if (!lock) {
					Log.info(" Re-Trying....... [.xdolock file creation] ", this);
					Log.info(" If you see this message repeatedly, kill this process and remove .xdolock file in your user home directory.", this);
					Log.info(" And then CHECK [if you can create file in your user home directory]. You must have a file creation authority.", this);
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected void doUnlock() {
		File locFile = new File(System.getProperty("user.home"), ".xdolock");
		locFile.delete();
	}
	public String getString() throws Exception {
		char ch = 'A';

		try {
			doLock();
			if (exist() == false) {
				write(ch);
			} else {
				ch = read();
				if (ch == 'Z')
					ch = 'a';
				else if (ch == 'z')
					ch = 'A';
				else
					ch++;
				write(ch);
			}
		} finally {
			try {
				doUnlock();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		return "" + ch;
	}
	protected boolean exist() {
		File file = new File(System.getProperty("user.home"), ".xdo");
		return file.canRead();
	}

	protected void write(char ch) {
		PrintWriter pw = null;
		try {
			File file = new File(System.getProperty("user.home"), ".xdo");
			pw = new PrintWriter(new FileWriter(file));
			pw.write(ch);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw == null)
					pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected char read() {
		BufferedInputStream bis = null;
		char ch = 'A';
		try {
			File file = new File(System.getProperty("user.home"), ".xdo");
			bis = new BufferedInputStream(new FileInputStream(file));
			ch = (char) bis.read();
		} catch (Exception e) {
			ch = 'A';
			e.printStackTrace();
		} finally {
			try {
				if (bis == null)
					bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ch;
	}
}