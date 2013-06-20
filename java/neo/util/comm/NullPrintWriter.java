package neo.util.comm;

import java.io.PrintWriter;

/**
 * 	@Class Name	: 	NullPrintWriter.java
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
public class NullPrintWriter extends PrintWriter {

	public NullPrintWriter() {
		super(new NullWriter());
	}

	public boolean checkError() {
		return false;
	}

	public void close() {
	}

	public void flush() {
	}

	public void print(char[] s) {
	}

	public void print(char c) {
	}

	public void print(double d) {
	}

	public void print(float f) {
	}

	public void print(int i) {
	}

	public void print(long l) {
	}

	public void print(Object obj) {
	}

	public void print(String s) {
	}

	public void print(boolean b) {
	}

	public void println() {
	}

	public void println(char[] x) {
	}

	public void println(char x) {
	}

	public void println(double x) {
	}

	public void println(float x) {
	}

	public void println(int x) {
	}

	public void println(long x) {
	}

	public void println(Object x) {
	}

	public void println(String x) {
	}

	public void println(boolean x) {
	}

	public void write(char[] buf) {
	}

	public void write(char[] buf, int off, int len) {
	}

	public void write(int b) {
	}

	public void write(String s) {
	}

	public void write(String s, int off, int len) {
	}
}
