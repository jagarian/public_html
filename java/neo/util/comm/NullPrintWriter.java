package neo.util.comm;

import java.io.PrintWriter;

/**
 * 	@Class Name	: 	NullPrintWriter.java
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
