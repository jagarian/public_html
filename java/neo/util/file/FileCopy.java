package neo.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;

import neo.exception.NeoException;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	FileCopy.java
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
public abstract class FileCopy {

	public static final int BLOCK_SIZE = 4096;

	public static void copy(File in, File out) throws NeoException {
		try {
			copy( new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
	}

	public static void copy(byte[] in, File out) throws NeoException {
		try {
			ByteArrayInputStream inStream = new ByteArrayInputStream(in);
			OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
			copy(inStream, outStream);
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
	}

	public static byte[] copyToByteArray(File in) throws NeoException {
		try {
			return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
	}

	public static void copy(InputStream in, OutputStream out)
		throws IOException {
		try {
			byte[] buffer = new byte[BLOCK_SIZE];
			int nrOfBytes = -1;
			while ((nrOfBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, nrOfBytes);
			}
			out.flush();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				Log.error(ex.getMessage(), null);
			}
			try {
				out.close();
			} catch (IOException ex) {
				Log.error(ex.getMessage(), null);
			}
		}
	}

	public static void copy(byte[] in, OutputStream out) throws NeoException {
		try {
			copy(new ByteArrayInputStream(in), out);
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
	}

	public static byte[] copyToByteArray(InputStream in) throws NeoException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			copy(in, out);
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
		return out.toByteArray();
	}

	public static void copy(Reader in, Writer out) throws IOException {
		try {
			char[] buffer = new char[BLOCK_SIZE];
			int nrOfBytes = -1;
			while ((nrOfBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, nrOfBytes);
			}
			out.flush();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				Log.error(ex.getMessage());
			}
			try {
				out.close();
			} catch (IOException ex) {
				Log.error(ex.getMessage());
			}
		}
	}

	public static void copy(String in, Writer out) throws NeoException {
		try {
			copy(new StringReader(in), out);
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
	}

	public static String copyToString(Reader in) throws NeoException {
		StringWriter out = new StringWriter();
		try {
			copy(in, out);
		} catch (IOException ie) {
			Log.error("[IOException in FileCopy] : " + ie.getMessage(), null);
			throw new NeoException(ie);
		}
		return out.toString();
	}
}
