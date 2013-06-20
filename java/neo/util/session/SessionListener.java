package neo.util.session;

import java.io.*;

import javax.servlet.http.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	SessionListener.java
 *	@���ϼ���		: 	
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
public class SessionListener implements HttpSessionBindingListener, Serializable {

	private static final long serialVersionUID = 1L;
	private String loginId_;
	private String sessionId_;
	private HttpServletRequest req_;

	public SessionListener() {
	}

	public SessionListener(String loginId,
							String seesionid,
							HttpServletRequest req) {
		this.loginId_ = loginId;
		this.sessionId_ = seesionid;
		this.req_ = req;
	}

	public void valueBound(HttpSessionBindingEvent pinsEvent) {
		SessionUtil.setNowUser(1);
		Log.info(" <SESSIONLISTENER> <CREATED> <SESSIONID:" + loginId_ + ">");
	}

	public void valueUnbound(HttpSessionBindingEvent pinsEvent) {
		SessionUtil.setNowUser(-1);
		Log.info(" <SESSIONLOGINID> <DELETED FROM SESSION> <LOGIN_ID:"
								+ loginId_
								+ "><SESSION_ID:"
								+ sessionId_
								+ ">");
	}

	public HttpServletRequest getRequest() {
		return req_;
	}

	public String getLoginID() {
		return loginId_;
	}

	public void setLoginID(String loginId) {
		this.loginId_ = loginId;
	}

	public void sessionDestroyed(HttpSessionBindingEvent se) {
		try {
			Log.info("HttpSession Destroyed...");
		} catch (Exception e) {
			Log.info("Exception from sessionDestroyed ::" + e);
		}
	}

	public void sessionCreated(HttpSessionBindingEvent se) {
		try {
			Log.info("HttpSession Created...");
		} catch (Exception e) {
			Log.info("Exception from sessionCreated ::" + e);
		}
	}
}
