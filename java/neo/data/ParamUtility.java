package neo.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import neo.util.comm.StringUtil;
import neo.util.log.Log;
import neo.util.session.SessionUtil;
import neo.fileupload.FormInfo;
import neo.fileupload.MultipartRequest;

/**
 * 	@Class Name	: 	ParamUtility.java
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
public class ParamUtility {

	private ParamUtility() {
	}

	public static String NVL(String originalStr, String defaultStr) {
		if (originalStr == null || originalStr.length() < 1)
			return defaultStr;
		return originalStr;
	}

	public static Param getAttributeParam(HttpServletRequest req) {
		Param data = new Param("REQUEST_DATA");
		Enumeration e = req.getAttributeNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			data.put(key, req.getAttribute(key));
		}
		return data;
	}

	public static Param getData(MultipartRequest mReq, HttpServletRequest req) {
		Param data = new Param("REQUEST_DATA_OF_MULTIPART");
		List formData = mReq.getParameters();
		Iterator iter = formData.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			String key = item.getFieldName();
			if (!data.containsKey(key))
				data.put(key, item.getFieldValue());
			//if (!data.containsKey(key)) 
			//	data.put( key, StringUtil.k2e(item.getFieldValue()) );
		}
		return defaultSessionInfoGetter(req, data); //2009.02.20 박현우 수정
	}

	public static Param getLocalData(HttpServletRequest req) {
		Param data = new Param("REQUEST_DATA");
		Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			//data.put( key, req.getParameter(key) );
			data.put(key, StringUtil.k2e(req.getParameter(key)));
		}
		return defaultSessionInfoGetter(req, data); //2009.02.20 박현우 수정
	}
	
	public static String getParamValuePrint(HttpServletRequest req) { 
		String prnStr = "";
		String prnStr2 = "";
		int cnt = 1;
		int cnt2 = 1;
		String[] temp = null;
		// 일반변수 및 hidden 조사
		Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			prnStr += (cnt++) + ". " + key + "=" + StringUtil.k2e(req.getParameter(key)) + "<br>";
		}
		// getParameterValues 도 파악한다.
		e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			temp = req.getParameterValues(key);
			if (temp != null && temp.length>1) {
				prnStr2 += "<font color=blue>"+(cnt++) + ". " + key + "=";
				cnt2 = 1;
				for (int i=0; i<temp.length; i++) {					
					prnStr2 += "[<font color=red>"+(cnt2++) + "</font>]" + StringUtil.k2e(temp[i])+(i<temp.length-1?"^":"");
				}
				prnStr2 += "***end***</font><br>";
			}
		}
		
		if ( !prnStr2.equals("") ) prnStr += prnStr2;
		
		return prnStr; //2009.07.09 박현우 수정
	}
	
	public static String getSessionValuePrint(HttpServletRequest req) { 
		String prnStr = "";
		String prnStr2 = "";
		int cnt = 1;
		int cnt2 = 1;
		String[] temp = null;
		// 일반변수 및 hidden 조사
		Enumeration e = req.getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			prnStr += (cnt++) + ". " + key + "=" + StringUtil.k2e(req.getParameter(key)) + "<br>";
		}		
		return prnStr; //2009.07.09 박현우 수정
	}

	public static Param getData(HttpServletRequest req) {
		Param data = new Param("REQUEST_DATA");
		Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			data.put(key, req.getParameter(key));
			//data.put( key, StringUtil.k2e(req.getParameter(key)) );
		}		
		return defaultSessionInfoGetter(req, data); //2009.02.20 박현우 수정
	}

	public static Param getDataFromCookie(HttpServletRequest req) {
		Param cookieData = new Param("COOKIE_DATA");
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			return cookieData;
		}
		for (int i = 0; i < cookies.length; i++) {
			String key = cookies[i].getName();
			String value = cookies[i].getValue();
			if (value == null) {
				value = "";
			}
			String cookiesValue = value;
			cookieData.put(key, cookiesValue);
		}
		return cookieData;
	}

	public static MultiParam getMultiData(MultipartRequest mReq) {
		MultiParam multiData = new MultiParam("REQUEST_DATA_OF_MULTIPART");
		List formData = mReq.getParameters();
		Iterator iter = formData.iterator();
		while (iter.hasNext()) {
			FormInfo item = (FormInfo) iter.next();
			String key = item.getFieldName();
			if (!multiData.containsKey(key)) {
				ArrayList list = new ArrayList();
				list.add(item.getFieldValue());
				//list.add(StringUtil.k2e(item.getFieldValue()));
				multiData.put(key, list);
			} else {
				ArrayList list = (ArrayList) multiData.get(key);
				list.add(item.getFieldValue());
				//list.add(StringUtil.k2e(item.getFieldValue()));
			}
		}
		
		return multiData;
	}

	public static MultiParam getMultiData(HttpServletRequest req) {
		MultiParam multiData = new MultiParam("REQUEST_PARAM");
		Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String[] values = req.getParameterValues(key);
			ArrayList list = new ArrayList();
			for (int i = 0; i < values.length; i++) {
				list.add(values[i]);
				//list.add(StringUtil.k2e(values[i]));
			}
			multiData.put(key, list);
		}
		return multiData;
	}
	
	public static Param defaultSessionInfoGetter(HttpServletRequest req, Param data) {
		/* ================================================== */
		// 사용자의 세션정보 dao 에서 사용할수 있도록 Getter 함. 
		// (프로그램마다 세션변수 대응 메모리변수 처리할 필요 없슴)
		// Dao 에서 Param에서 끄지버 내면 됨. (단, dao 까지 Param 객체가 넘어간 경우)
		/* ================================================== */
		String ip1 = req.getHeader("WL-Proxy-Client-IP");
	    String ip2 = req.getHeader("Proxy-Client-IP");
	    String ip3 = req.getHeader("X-Forwarded-For");
	    String ip4 = req.getRemoteAddr();
		if ( ip4 == null || ip4.length() < 4 ) 
			ip4 = req.getHeader("INTEL_SOURCE_IP");
		System.out.println("ip1 :: "+ip1);
		System.out.println("ip2 :: "+ip2);
		System.out.println("ip3 :: "+ip3);
		System.out.println("ip4 :: "+ip4);
		data.set("req_object",req);
		data.set("ss_userId",StringUtil.NVL((String)SessionUtil.getUserId(req),"hoon09"));
		data.set("ss_email",(String)SessionUtil.getUserId(req));
		data.set("ss_client_ip",ip4);
		Log.info(data.toString());
		return data;
	}

	public static Param deepClone(Param data) {
		Param newData = new Param(data.getName());
		Param src = data;
		Param target = (Param) newData;
		Set set = src.keySet();
		Iterator e = set.iterator();
		while (e.hasNext()) {
			String key = (String) e.next();
			Object value = src.get(key);
			target.put(key, value);
		}
		return (Param) newData;
	}

	public static Object convertToEntity(Param source, Object target) {
		if (target == null) {
			throw new NullPointerException("[convertToEntity() Using ERROR] :: target is null");
		}
		Class c = target.getClass();
		Field[] field = c.getDeclaredFields();
		Method[] methods = c.getMethods();
		int methodSize = methods.length;

		for (int i = 0; i < field.length; i++) {
			try {
				String fieldName = field[i].getName();
				String str = ((String) fieldName.substring(0, 1)).toUpperCase();
				String tmpFieldName = str + fieldName.substring(1);
				String strMethod = "set" + tmpFieldName;
				int methodIndex =getMethodIndex(methods, strMethod, methodSize);
				Object[] strField = new Object[1];
				strField[0] = source.get(fieldName);
				if (source.containsKey(fieldName)) {
					methods[methodIndex].invoke(target, strField);
				}
			} catch (Exception e) {
				throw new RuntimeException("[convertToEntity Exception occoure...]");
			}
		}
		return target;
	}

	public static ArrayList convertToMultiEntity(
		MultiParam source,
		Object target) {
		if (target == null) {
			throw new NullPointerException("[convertToMultiEntity() Using ERROR] :: target is null");
		}
		ArrayList list = new ArrayList();
		Class c = target.getClass();
		Field[] field = c.getDeclaredFields();
		Method[] methods = c.getMethods();
		int methodSize = methods.length;
		try {
			for (int i = 0; i < source.keySize(); i++) {
				Object tmp = (Object) c.newInstance();
				for (int j = 0; j < field.length; j++) {
					//String fieldType = field[j].getType().getName();
					String fieldName = field[j].getName();

					String str =	((String) fieldName.substring(0, 1)).toUpperCase();
					String tmpFieldName = str + fieldName.substring(1);

					String strMethod = "set" + tmpFieldName;
					int methodIndex = getMethodIndex(methods, strMethod, methodSize);

					Object[] strField = new Object[1];
					strField[0] = source.get(fieldName, i);

					if (source.containsKey(fieldName)) {
						methods[methodIndex].invoke(tmp, strField);
					}
				}
				list.add(tmp);
			}
		} catch (Exception e) {
			throw new RuntimeException("[convertToMultiEntity Exception occoure...]");
		}
		return list;
	}

	public static Param convertToLData(Object source) {
		Param target = new Param("covertedLData");
		Class c = source.getClass();
		Field[] field = c.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			try {
				String fieldType = field[i].getType().getName();
				String fieldName = field[i].getName();
				String str = ((String) fieldName.substring(0, 1)).toUpperCase();
				String tmpFieldName = str + fieldName.substring(1);
				Class[] classes = null;
				String strMethod = "get" + tmpFieldName;
				Method method = c.getMethod(strMethod, classes);
				String[] strField = null;
				if (fieldType.equals("java.lang.String")) {
					target.set(
						fieldName,
						(String) method.invoke(source, strField));
				} else if (fieldType.equals("int")) {
					target.setInt(
						fieldName,
						((Integer) method.invoke(source, strField)).intValue());
				} else if (fieldType.equals("double")) {
					target.setDouble(
						fieldName,
						((Double) method.invoke(source, strField)).intValue());
				} else if (fieldType.equals("long")) {
					target.setLong(
						fieldName,
						((Long) method.invoke(source, strField)).intValue());
				} else if (fieldType.equals("float")) {
					target.setFloat(
						fieldName,
						((Float) method.invoke(source, strField)).intValue());
				} else if (fieldType.equals("boolean")) {
					target.setBoolean(
						fieldName,
						((Boolean) method.invoke(source, strField))
							.booleanValue());
				} else {
					target.set(fieldName, method.invoke(source, strField));
				}
			} catch (Exception e) {
				throw new RuntimeException("[convertToLData Exception occoure...]");
			}
		}
		return target;
	}

	public static MultiParam convertToLMultiData(List source) {
		MultiParam target = new MultiParam("convertedLMultiData");

		Object tmpSource = (Object) source.get(0);
		Class c = tmpSource.getClass();
		Field[] field = c.getDeclaredFields();

		for (int i = 0; i < source.size(); i++) {
			Object tmp = (Object) source.get(i);
			for (int j = 0; j < field.length; j++) {
				try {
					String fieldType = field[j].getType().getName();
					String fieldName = field[j].getName();
					String str =
						((String) fieldName.substring(0, 1)).toUpperCase();
					String tmpFieldName = str + fieldName.substring(1);

					Class[] classes = null;
					String strMethod = "get" + tmpFieldName;
					Method method = c.getMethod(strMethod, classes);

					String[] strField = null;
					if (fieldType.equals("java.lang.String")) {
						target.addString(
							fieldName,
							(String) method.invoke(tmp, strField));
					} else if (fieldType.equals("int")) {
						target.addInt(
							fieldName,
							((Integer) method.invoke(tmp, strField))
								.intValue());
					} else if (fieldType.equals("double")) {
						target.addDouble(
							fieldName,
							((Double) method.invoke(tmp, strField)).intValue());
					} else if (fieldType.equals("long")) {
						target.addLong(
							fieldName,
							((Long) method.invoke(tmp, strField)).intValue());
					} else if (fieldType.equals("float")) {
						target.addFloat(
							fieldName,
							((Float) method.invoke(tmp, strField)).intValue());
					} else if (fieldType.equals("boolean")) {
						target.addBoolean(
							fieldName,
							((Boolean) method.invoke(tmp, strField))
								.booleanValue());
					} else {
						target.add(fieldName, method.invoke(tmp, strField));
					}
				} catch (Exception e) {
					throw new RuntimeException("[convertToLMultiData Exception occoure...]");
				}
			}
		}
		return target;
	}

	private static int getMethodIndex(
		Method[] methods,
		String methodName,
		int methodSize) {
		int i = 0;
		for (; i < methodSize; i++)
			if (methods[i].getName().equals(methodName))
				break;
		if (i == methodSize)
			return -1;
		else
			return i;
	}
}
