package neo.util.comm;

import java.io.Writer;

/**
 * 	@Class Name	: 	NullWriter.java
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
public class NullWriter extends Writer {

	public NullWriter() {
	}

	public void close() {
	}

	public void flush() {
	}

	public void write(char[] cbuf) {
	}

	public void write(char[] cbuf, int off, int len) {
	}

	public void write(int c) {
	}

	public void write(String str) {
	}

	public void write(String str, int off, int len) {
	}
}
