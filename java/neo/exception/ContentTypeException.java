package neo.exception;

/**
 * 	@Class Name	: 	ContentTypeException.java
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
public class ContentTypeException extends NeoException {

	private static final long serialVersionUID = 1L;

	public ContentTypeException() {
		super();
	}

	public ContentTypeException(String msg) {
		super(msg);
	}
	
	public ContentTypeException(String code, String msg) {
		super(code, msg);
	}
	
	public ContentTypeException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	
	public ContentTypeException(String code, String msg, Throwable rootCause) {
		super(code, msg, rootCause);
	}
	
	public ContentTypeException(Throwable rootCause) {
		super(rootCause);
	}
}
