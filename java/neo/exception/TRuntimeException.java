package neo.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	TRuntimeException.java
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
public class TRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private java.lang.Throwable rootCause;
	private boolean isFirst = true;

	public TRuntimeException() {
		super();
	}

	public TRuntimeException(String code) {
		super(code);
		Log.fatal(code, this);
	}
	
	public TRuntimeException(String code, String msg) {
		super("[" + code + "]" + msg);
		Log.info("[" + code + "]" + msg, this);
	}
	
	public TRuntimeException(String message, Throwable rootCause) {
		super(message);
		this.rootCause = rootCause;
		this.isFirst = false;
		Log.fatal(message, this);
	}
	
	public TRuntimeException(String code, String message, Throwable rootCause) {
		super("[" + code + "]" + message);
		this.rootCause = rootCause;
		this.isFirst = false;
		Log.fatal("[" + code + "]" + message, this);
	}
	
	public TRuntimeException(Throwable rootCause) {
		this();
		this.rootCause = rootCause;
		this.isFirst = false;
		Log.fatal("", this);
	}
	
	//Exception Method
	public Throwable getRootCause() {
		return rootCause;
	}

	public String getStackTraceString() {
		StringWriter s = new StringWriter();
		printStackTrace(new PrintWriter(s));
		return s.toString();
	}

	public void printStackTrace() {
		printStackTrace(System.out);
	}

	public void printStackTrace(java.io.PrintStream s) {
		synchronized (s) {
			super.printStackTrace(s);
			if (rootCause != null) {
				rootCause.printStackTrace(s);
			}
			if (isFirst || !(rootCause instanceof RuntimeException)) {
				s.println("-----------------------------");
			}
		}
	}

	public void printStackTrace(java.io.PrintWriter s) {
		synchronized (s) {
			super.printStackTrace(s);
			if (rootCause != null) {
				rootCause.printStackTrace(s);
			}
			if (isFirst || !(rootCause instanceof RuntimeException)) {
				s.println("-----------------------------");
			}
		}
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
