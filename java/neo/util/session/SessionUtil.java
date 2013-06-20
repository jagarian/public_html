package neo.util.session;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

import neo.util.comm.StringUtil;
import neo.util.comm.WebKey;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	SessionUtil.java
 * 	@���ϼ���		: 	
 * 	HttpSession session = request.getSession(true); //������ ������ ���������� �����ϰ�, ������ ������ ���ο� ������ �����ϰڴ� !!!
 * 	HttpSession session = request.getSession(false);//������ ������ ���������� �����ϰ�, ������ ������ null���� �����ϰڴ� !!!
 * 	if(session.isNew()){
 *		session.setAttribute("id",id);
 * 		out.print("���ο� ������ �����Ǿ����ϴ�.");
 *		out.print("���� ID: " + session.getId());
 * 	} else {
 * 		out.print("�̹� ������ �����մϴ�.");
 * 		out.print("���� ID: " + session.getId());
 *	}
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
public class SessionUtil {

	ServletContext context = null;
	public static int nowUser = 0; 
	
	public static void setNowUser(int x){ 
		nowUser += x; 
	} 

	public int getNowUser(){ 
		return this.nowUser; 
	} 
	
	/**
	 * ���� ���� (�α��ν� ȣ��)
	 */
	public static void init(	HttpServletRequest req,
								String loginId,
								String sessionId) {
		HttpSession insHsession = req.getSession(true);
		
		if (insHsession.isNew()) { //������ ��������� Ŭ���̾�Ʈ�� ���� ���ǿ� ���ε��� �ʾ����� true�� �����Ѵ�.
			
			SessionListener sessionListener = new SessionListener(loginId, sessionId, req);
			setValue(req, WebKey.SESSION_LISTENER_ID, sessionListener);
			
		} else {
			
			if ((SessionListener) getValue(req,	WebKey.SESSION_LISTENER_ID) == null) {
				SessionListener sessionListener = new SessionListener(loginId, sessionId, req);
				setValue(req, WebKey.SESSION_LISTENER_ID, sessionListener);
			}
		}
	}
	
	public static void init(HttpServletRequest req) {
		init(req, (String)getValue(req,WebKey.SESSION_USER_ID), (String)getValue(req,"SESSION_ID"));		
	}	
	
	/**
	 * ���� ���� Setter
	 */
	public static boolean setSessionID(HttpServletRequest req, String pstrSessionID) {
		if (pstrSessionID != null && !pstrSessionID.trim().equals("")) {
			setValue(req, WebKey.SESSION_ID, pstrSessionID);
			return true;
		} else
			return false;
	}
	
	public static boolean setUserID(HttpServletRequest req, String pstrUserID) {
		if (pstrUserID != null && !pstrUserID.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_ID, pstrUserID);
			return true;
		} else
			return false;
	}

	public static boolean setUserName(HttpServletRequest req, String pstrUserName) {
		if (pstrUserName != null && !pstrUserName.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_NAME, pstrUserName);
			return true;
		} else
			return false;
	}

	public static boolean setUserEmail(HttpServletRequest req, String pstrUserEmail) {
		if (pstrUserEmail != null && !pstrUserEmail.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_EMAIL, pstrUserEmail);
			return true;
		} else
			return false;
	}

	public static boolean setUserDept(HttpServletRequest req,	String pstrUserDept) {
		if (pstrUserDept != null && !pstrUserDept.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_DEPT, pstrUserDept);
			return true;
		} else
			return false;
	}

	public static boolean setUserPwd(HttpServletRequest req,	String pstrUserPwd) {
		if (pstrUserPwd != null && !pstrUserPwd.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_PWD, pstrUserPwd);
			return true;
		} else
			return false;
	}

	public static boolean setUserLoginCnt(HttpServletRequest req, int pstrLoginCnt) {
		if (pstrLoginCnt > 0) {
			setValue(req,	WebKey.SESSION_USER_LOGIN_CNT, pstrLoginCnt + "");
			return true;
		} else
			return false;
	}

	public static boolean setUserRole(HttpServletRequest req, String pstrUserLevel) {
		if (pstrUserLevel != null && !pstrUserLevel.trim().equals("")) {
			setValue(req, WebKey.SESSION_USER_ROLE, pstrUserLevel + "");
			return true;
		} else
			return false;
	}

	public static boolean setUserSessionId(HttpServletRequest req, String pstrSessionId) {
		if (pstrSessionId != null && !pstrSessionId.trim().equals("")) {
			setValue(req, WebKey.SESSION_ID, pstrSessionId);
			return true;
		} else
			return false;
	}

	/**
	 * ���� ���� Getter
	 */
	public static String getSessionID(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_ID);
	}
	
	public static String getUserId(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_ID);
	}

	public static String getUserName(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_NAME);
	}

	public static String getUserEmail(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_EMAIL);
	}

	public static String getUserDept(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_DEPT);
	}

	public static String getUserPwd(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_PWD);
	}

	public static String getUserSessionId(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_ID);
	}

	public static int getUserLoginCnt(HttpServletRequest req) {
		return (int) getInt(req, WebKey.SESSION_USER_LOGIN_CNT);
	}

	public static String getUserRole(HttpServletRequest req) {
		return (String) getValue(req, WebKey.SESSION_USER_ROLE);
	}
	
	public static boolean setValue(HttpServletRequest req,
													String pstrKey,
												  	Object pinsVal) {
		HttpSession insSession = req.getSession(false);
		boolean rbSetObjToSession = false;
		if (insSession != null) {
			insSession.setAttribute(pstrKey, pinsVal);
			rbSetObjToSession = true;
		}
		return rbSetObjToSession;
	}
	
	/**
	 * ���� �� ���ϱ�
	 */
	public static Object getValue(HttpServletRequest req,
												String pstrKey) {
		HttpSession insSession = req.getSession(false);
		Object insVal = null;

		if (insSession != null) {
			insVal = insSession.getAttribute(pstrKey);
		}
		return insVal;
	}
	
	/**
	 * ���� �� ����
	 */
	public static boolean removeValue(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);

		boolean rbRemoved = false;

		if (insSession != null) {
			insSession.removeAttribute(pstrKey);
			rbRemoved = true;
		}
		return rbRemoved;
	}	

	public static String getString(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);
		String rstrVal = null;

		if (insSession != null) {
			Object obj = insSession.getAttribute(pstrKey);
			if (obj != null)
				rstrVal = (String) obj;
		}
		return rstrVal;
	}

	public static int getInt(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);
		int riVal = 0;

		if (insSession != null) {
			Object insObj = insSession.getAttribute(pstrKey);
			Log.info("insObj :: " + insObj.toString());
			String cvar = insObj.toString();
			if (insObj != null) {
				//riVal =  ((Integer) insObj).intValue() ;
				riVal = StringUtil.strToInt(cvar);
			}

		}
		return riVal;
	}

	public static long getLong(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);
		long rlVal = 0;

		if (insSession != null) {
			Object insObj = insSession.getAttribute(pstrKey);

			if (insObj != null)
				rlVal = ((Long) insObj).longValue();

		}
		return rlVal;
	}

	public static float getFloat(HttpServletRequest req,	String pstrKey) {

		HttpSession insSession = req.getSession(false);
		float rfVal = 0;

		if (insSession != null) {
			Object insObj = insSession.getAttribute(pstrKey);

			if (insObj != null)
				rfVal = ((Float) insObj).floatValue();
		}
		return rfVal;
	}

	public static Vector getVector(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);
		Vector rinsVal = null;

		if (insSession != null) {
			Object insObj = insSession.getAttribute(pstrKey);

			if (insObj != null)
				rinsVal = (Vector) insObj;
		}
		return rinsVal;
	}
	
	// ���ǰ�ü�� ����� ��ü���� Hashtable�� ��ȯ�Ѵ�.
	public static Hashtable getHashtable(HttpServletRequest req, String pstrKey) {

		HttpSession insSession = req.getSession(false);
		Hashtable rinsVal = null;

		if (insSession != null) {
			Object insObj = insSession.getAttribute(pstrKey);

			if (insObj != null)
				rinsVal = (Hashtable) insObj;
		}
		return rinsVal;
	}

	/**
	 * ��Ÿ ���� ó�� �Լ���
	 */
	public static boolean isNew(HttpServletRequest req) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			return insSession.isNew();

		else
			return false;
	}
	
	// ���ǰ�ü�� Timeout�ð��� �����Ѵ�.	
	public static boolean setTimeOut(HttpServletRequest req,
										int pintTimeoutSec) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null) {
			insSession.setMaxInactiveInterval(pintTimeoutSec);
			return true;
		} else
			return false;
	}
	
	// ���ǰ�ü�� Timeout�ð��� �����Ѵ�.
	public static int getTimeOut(HttpServletRequest req) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			return insSession.getMaxInactiveInterval();
		else
			return -1;
	}
	
	// ���ǰ�ü�� Timeout�ð��� ��ȯ�Ѵ�.
	public static long getCreationTime(HttpServletRequest req) {
		long rlSessionCrtTime = 0;
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			rlSessionCrtTime = insSession.getCreationTime();
		return rlSessionCrtTime;
	}

	// ���ǰ�ü�� �����ð��� ��ȯ�Ѵ�.
	public static boolean destroy(HttpServletRequest req) {
		HttpSession insSession = req.getSession(false);
		boolean rbDestroyed = false;
		if (insSession != null) {
			insSession.invalidate();
			rbDestroyed = true;
		}
		return rbDestroyed;
	}
	
	/**
     * �α����� �ʿ��� ������ ������ ������ ������ ��� ���ǿ��� �����Ѵ�.
	 * ������ �׸��� ������ ����~ �ֳ� ?
     */
    public static void resetSession(HttpServletRequest pinsRequest) {
        HttpSession insHsession = pinsRequest.getSession(true) ;
        if( insHsession != null ){
            Enumeration keys = insHsession.getAttributeNames() ;
            while(keys.hasMoreElements()) {
                String Key = (String) keys.nextElement() ;
                // �ش� ����Ű�� ����� ��ü�� ���ǿ��� �����մϴ�.
                if(	!Key.equals(WebKey.SESSION_LISTENER_ID) ) {
                    insHsession.removeAttribute(Key) ;
                }
            }
        }
    }
	
	// �̹� �α��� �ߴ��� ���θ� �����Ѵ�.
	public static boolean isAlreadyLogin(HttpServletRequest req) {
		if (!getUserId(req).equals(""))
			return true;
		else
			return false;
	}
	
	//����Ͻ÷��� JRE������ Ȯ���Ͻð� "Java Runtime �Ű�����"�� ������ ���� �Ű�������
	//-XX:MaxPermSize=768m -Xms768m -Xmx768m
	//MaxPerSize : Heap �ִ� ��� ũ�� ����
	//Xms : Heap �ּ� ũ��
	//Xmx : Heap �ִ� ũ��
	////float�� 4����Ʈ double�� 8����Ʈ�Դϴ�. float�ΰ��� 4*1024*1024=8MB �Դϴ�. 
	//���⿡ 3�����迭�� ������ 4*180 = 720MB���� �迭�����ص� Heap������ �߻������ʽ��ϴ�.
	//float [][][] memory_allocation= new float [180][1024][1024];
	//for(int i = 0;i<180;i++)
	// {
	//  for(int j = 0;j<1024;j++)
	//  {
	//   for(int k = 0;k<1024;k++)
	//   {
	//    memory_allocation[i][j][k] = k;
	//   }
	//  }
	// }
	// }
	//}
	public static String getRuntimeTotalMemory() {
		long  heapSize   = Runtime.getRuntime().totalMemory();
		 String m = (heapSize / (1024*1024)) + "MB";		
		return m;
	}
	
	public static String getRuntimeFreeMemory() {
		long  heapSize   = Runtime.getRuntime().freeMemory();
		 String m = (heapSize / (1024*1024)) + "MB";		
		return m;
	}
	
	public static String getRuntimeCalMemory() {
		long  heapSize   = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		 String m = (heapSize / (1024*1024)) + "MB";		
		return m;
	}
	
	public static String getRuntimeMaxMemory() {
		long  heapSize   = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		 String m = (heapSize / (1024*1024)) + "MB";		
		return m;
	}
	
	public static String getRuntimeAvailableProcessors() {
		long cnt   = Runtime.getRuntime().availableProcessors();
		return cnt+"";
	}
}
