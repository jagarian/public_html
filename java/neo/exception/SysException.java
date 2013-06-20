package neo.exception;

/**
 * 	@Class Name	: 	SysException.java
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
public class SysException extends NeoException {

	private static final long serialVersionUID = 1L;

	public SysException() {
		super();
	}

	public SysException(String msg) {
		super(msg);
	}
	
	public SysException(String code, String msg) {
		super(code, msg);
	}
	
	public SysException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	
	public SysException(String code, String msg, Throwable rootCause) {
		super(code, msg, rootCause);
	}
	
	public SysException(Throwable rootCause) {
		super(rootCause);
	}
}
