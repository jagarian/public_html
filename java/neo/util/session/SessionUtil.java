package neo.util.session;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

import neo.util.comm.StringUtil;
import neo.util.comm.WebKey;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	SessionUtil.java
 * 	@파일설명		: 	
 * 	HttpSession session = request.getSession(true); //세션이 있으면 기존세션을 리턴하고, 세션이 없으면 새로운 세션을 생성하겠다 !!!
 * 	HttpSession session = request.getSession(false);//세션이 있으면 기존세션을 리턴하고, 세션이 없으면 null값을 리턴하겠다 !!!
 * 	if(session.isNew()){
 *		session.setAttribute("id",id);
 * 		out.print("새로운 세션이 생성되었습니다.");
 *		out.print("세션 ID: " + session.getId());
 * 	} else {
 * 		out.print("이미 세션이 존재합니다.");
 * 		out.print("세션 ID: " + session.getId());
 *	}
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
	 * 세션 시작 (로그인시 호출)
	 */
	public static void init(	HttpServletRequest req,
								String loginId,
								String sessionId) {
		HttpSession insHsession = req.getSession(true);
		
		if (insHsession.isNew()) { //세션이 만들어지고 클라이언트가 아직 세션에 조인되지 않았으면 true를 리턴한다.
			
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
	 * 세션 관련 Setter
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
	 * 세션 관련 Getter
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
	 * 세션 값 구하기
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
	 * 세션 값 삭제
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
	
	// 세션객체에 저장된 객체값을 Hashtable로 반환한다.
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
	 * 기타 세션 처리 함수들
	 */
	public static boolean isNew(HttpServletRequest req) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			return insSession.isNew();

		else
			return false;
	}
	
	// 세션객체의 Timeout시간을 설정한다.	
	public static boolean setTimeOut(HttpServletRequest req,
										int pintTimeoutSec) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null) {
			insSession.setMaxInactiveInterval(pintTimeoutSec);
			return true;
		} else
			return false;
	}
	
	// 세션객체의 Timeout시간을 설정한다.
	public static int getTimeOut(HttpServletRequest req) {
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			return insSession.getMaxInactiveInterval();
		else
			return -1;
	}
	
	// 세션객체의 Timeout시간을 반환한다.
	public static long getCreationTime(HttpServletRequest req) {
		long rlSessionCrtTime = 0;
		HttpSession insSession = req.getSession(false);
		if (insSession != null)
			rlSessionCrtTime = insSession.getCreationTime();
		return rlSessionCrtTime;
	}

	// 세션객체의 생성시간을 반환한다.
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
     * 로그인전 필요한 정보를 제외한 나머지 정보를 모두 세션에서 제거한다.
	 * 제거할 항목을 정의해 보자~ 있나 ?
     */
    public static void resetSession(HttpServletRequest pinsRequest) {
        HttpSession insHsession = pinsRequest.getSession(true) ;
        if( insHsession != null ){
            Enumeration keys = insHsession.getAttributeNames() ;
            while(keys.hasMoreElements()) {
                String Key = (String) keys.nextElement() ;
                // 해당 세션키로 저장된 객체를 세션에서 제거합니다.
                if(	!Key.equals(WebKey.SESSION_LISTENER_ID) ) {
                    insHsession.removeAttribute(Key) ;
                }
            }
        }
    }
	
	// 이미 로그인 했는지 여부를 조사한다.
	public static boolean isAlreadyLogin(HttpServletRequest req) {
		if (!getUserId(req).equals(""))
			return true;
		else
			return false;
	}
	
	//사용하시려는 JRE버전을 확인하시고 "Java Runtime 매개변수"에 다음과 같은 매개변수를
	//-XX:MaxPermSize=768m -Xms768m -Xmx768m
	//MaxPerSize : Heap 최대 허용 크기 설정
	//Xms : Heap 최소 크기
	//Xmx : Heap 최대 크기
	////float는 4바이트 double은 8바이트입니다. float인경우는 4*1024*1024=8MB 입니다. 
	//여기에 3차원배열을 설정시 4*180 = 720MB까지 배열설정해도 Heap오류가 발생하지않습니다.
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
