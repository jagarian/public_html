package neo.exception;

/**
 * 	@Class Name	: 	BizException.java
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
public class BizException extends NeoException {

	private static final long serialVersionUID = 1L;

	public BizException() {
		super();
	}

	public BizException(String msg) {
		super(msg);
	}
	
	public BizException(String code, String msg) {
		super(code, msg);
	}

	public BizException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	
	public BizException(String code, String msg, Throwable rootCause) {
		super(code, msg, rootCause);
	}
	
	public BizException(Throwable rootCause) {
		super(rootCause);
	}
}
