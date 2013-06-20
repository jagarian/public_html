package neo.util.comm;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import neo.util.log.Log;

import com.oreilly.servlet.LocaleNegotiator;

/**
 * 	@Class Name	: 	PLang.java
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
public class PLang implements java.io.Serializable {
	Locale locale_;
	String charset_;
	ResourceBundle bundle_;

	public PLang(	String fileName,
					HttpServletRequest req,
					HttpServletResponse res) throws ServletException, IOException {			
		String bundleName = fileName;
		String acceptLanguage = req.getHeader("Accept-Language");
		String acceptCharset = req.getHeader("Accept-Charset");

		Log.info("bundleName is :: " + bundleName, this);
		Log.info("acceptLanguage is :: " + acceptLanguage, this);
		Log.info("acceptCharset is :: " + acceptCharset, this);

		LocaleNegotiator nogotiator = new LocaleNegotiator(bundleName, acceptLanguage, acceptCharset);
		
		this.locale_ = nogotiator.getLocale();
		this.charset_ = nogotiator.getCharset();
		this.bundle_ = nogotiator.getBundle(); //null일수도 있다
	}

	public Locale getLocale() {
		return this.locale_;
	}
	public String getCharset() {
		return this.charset_;
	}
	public ResourceBundle getBundle() {
		return this.bundle_;
	}
}
