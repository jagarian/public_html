package neo.exception;

import java.util.StringTokenizer;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	NeoException.java
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
public class NeoException extends Exception {

	private static final long serialVersionUID = 1L;
	private String code = "";
	private String message = "";

	public NeoException() {
		super();
	}
	
	public NeoException(String msg) { 
		super("[" + msg + "] ");
		this.message = msg;		
	}

	public NeoException(String code, String msg) {
		super("[" + code + "] " + msg);
		this.code = code;
		this.message = "[" + code + "] " + msg;
	}
	
	public NeoException(String msg, Throwable rootCause) { 
		super(msg, rootCause);
		this.message = msg;
	}

	public NeoException(String code, String msg, Throwable rootCause) {
		super("[" + code + "] " + msg, rootCause);
		this.code = code;
		this.message = "[" + code + "] " + msg;
	}
	
	public NeoException(Throwable rootCause) { 
		super(rootCause);		
	}
	
	//Exception Method
	public String getMessage() {
		return this.message;
	}

	public String getCode() {
		return this.code;
	}

	public String toString() {
		String s = getClass().getName();
		String message = this.message;
		return (message != null) ? (s + ": " + message) : s;
	}
	
	private String getClassName() {
		String className = null;
		StackTraceElement[] ste = this.getStackTrace();
		StringTokenizer st = new StringTokenizer(ste[0].getClassName(), ".");
		while (st.hasMoreElements()) {
			className = (String) st.nextElement();
		}
		return className;
	}

	private String getMethodName() {
		StackTraceElement[] ste = this.getStackTrace();
		return ste[0].getMethodName();
	}
}
