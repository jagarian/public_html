package neo.config;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import neo.data.TText;
import neo.util.comm.StringUtil;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	WebUtil.java
 * 	@���ϼ���		:	
 * 	-----------------------------------------------------------------------------------------
 * 	WebUtil.printFile();								//js + css only (default key)
 *	WebUtil.printFile("/board/board_list"); 			//js + css only
 *	-----------------------------------------------------------------------------------------
 *	WebUtil.printJsFile();								//js only (default key)
 *	WebUtil.printJsFile("/board/board_list");			//js only 
 *	WebUtil.printCssFile();								//css only (default key)
 *	WebUtil.printCssFile("/board/board_list");			//css only
 *	-----------------------------------------------------------------------------------------
 *	WebUtil.printViewFiles("js");						//file node �� type key ��� (default key)
 *	WebUtil.printViewFiles("/board/board_list","image");//file node �� type key ���
 *	-----------------------------------------------------------------------------------------
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
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
public class WebUtil {

	private static final String DEFAULT_SERVLET_PATH = "/weblet?do=";

	public static long timeStamp;

	public WebUtil() {
		super();
	}
	
	//---------------------------------------------------------------------------------------------
	//  javascript or css �� xml ���� get �ϴ� method 
	//---------------------------------------------------------------------------------------------
	
	/**
	 * @title	 key=default �� �ش�Ǵ� ��忡�� CSS ���� (1���� �����Ѵ�), �� file ���� ���ܵ�  
	 * 				 (JS + CSS ��� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printFile() {
		return printFile("default");
	}
	
	/**
	 * @title	 key=any �� �ش�Ǵ� ��忡�� CSS ���� (1���� �����Ѵ�), �� file ���� ���ܵ�  
	 * 				 (JS + CSS ��� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printFile(String pstrJsp) {
		String rstrStr = printCssFile(pstrJsp) + printJsFile(pstrJsp);
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No Files in View XML "+pstrJsp+"-->";
		else
			return rstrStr; // + "\n";
	}
	
	//---------------------------------------------------------------------------------------------

	/**
	 * @title	 key='default' �� �ش�Ǵ� ��忡�� CSS ���� (1��), �� file ���� ���ܵ�    
	 * 				 (CSS�� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printCssFile() {
		return printCssFile("default");
	}
	
	/**
	 * @title	 key='any' �� �ش�Ǵ� ��忡�� CSS ���� (1��), �� file ���� ���ܵ�    
	 * 				 (CSS�� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printCssFile(String pstrCss) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrCss).getViewCSSFile();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No Css Files in View XML "+pstrCss+"-->";
		else
			return rstrStr; // + "\n";
	}
	
	/**
	 * @title	 key='default' �� �ش�Ǵ� ��忡�� CSS ���� (1��), �� file ���� ���ܵ�    
	 * 				 (JS�� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printJsFile() {
		return printJsFile("default");
	}
	
	/**
	 * @title	 key='any' �� �ش�Ǵ� ��忡�� CSS ���� (1��), �� file ���� ���ܵ�    
	 * 				 (JS�� ������)
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printJsFile(String pstrJs) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJs).getViewJSFile();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No Js Files in View XML "+pstrJs+"-->";
		else
			return rstrStr; // + "\n";
	}
	
	//---------------------------------------------------------------------------------------------
	
	/**
	 * @title	 key='default' �� �ش�Ǵ� file ��忡�� Ÿ���� pstrText �� �ش�Ǵ� �ڿ� ��� ����
	 * 				 (��� ������))
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printViewFiles(String pstrText) {
		return printViewFiles("default", pstrText);
	}
	
	/**
	 * @title	 key='any' �� �ش�Ǵ� file ��忡�� Ÿ���� pstrText �� �ش�Ǵ� �ڿ� ��� ����
	 * 				 (��� ������))
	 * @param 
	 * @return	 java.lang.String
	 */
	public static String printViewFiles(String pstrJsp, String pstrText) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewFiles(pstrText);
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No File in View XML "+pstrJsp+"-->";
		else
			return rstrStr; // + "\n";
	}
	
	//---------------------------------------------------------------------------------------------
	// name, path, title, help �� view.xml ���� get �ϴ� method
	//---------------------------------------------------------------------------------------------
	
	public static String printKey() {
		return printKey("default");
	}
	
	public static String printKey(String pstrJsp) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewKey();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No View Key Name in View XML -->";
		else
			return rstrStr;
	}

	public static String printName() {
		return printName("default");
	}

	public static String printName(String pstrJsp) {
		String rstrStr =
			ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewName();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No View Name in View XML -->";
		else
			return rstrStr;
	}

	public static String printTitle() {
		return printTitle("default");
	}

	public static String printTitle(String pJsp) {
		String rStr = ResourceMappingModel.getInstance().getSubView(pJsp).getViewTitle();
		if (rStr == null || rStr.equals(""))
			return "<!-- No Title Name in View XML -->";
		else
			return rStr;
	}
	
	public static String printPathList() {
		return printPath("default");
	}

	public static String printPath(String pJsp) {
		String rPathStr = ResourceMappingModel.getInstance().getSubView(pJsp).getViewPathList();
		String rPathUrlStr = ResourceMappingModel.getInstance().getSubView(pJsp).getViewPathUrlList();
		String[] rPathArr = StringUtil.split(rPathStr, ",", false);
		String[] rPathUrlArr = StringUtil.split(rPathUrlStr, ",", false);
		String addStr = "";
		if (rPathStr == null || rPathStr.equals(""))
			return "<!-- No Title Name in View XML -->";
		else {
			for (int i=0; i<rPathArr.length; i++) {
				addStr += "&gt; <a href=\"" + DEFAULT_SERVLET_PATH + rPathUrlArr[i]+"\">" + rPathArr[i] + "</a>\n";
			}
			
		}
		return addStr;
	}

	public static String printHelp() {
		return printHelp("default");
	}

	public static String printHelp(String pstrJsp) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewHelp();
		StringBuffer rinsSb = new StringBuffer();
		if (rstrStr == null || rstrStr.equals("")) {
			return "<!-- No Help Files in View XML -->";
		} else {
			rinsSb.append("<SCRIPT LANGUAGE=\"JScript\">var vUrl = \"");
			rinsSb.append(rstrStr);
			rinsSb.append("\";function f_help() { ");
			rinsSb.append(printOpenWindow("����", "help"));
			rinsSb.append("  } </SCRIPT>");
			rinsSb.append("<A HREF='#' onClick=\"javascript:f_help()\">");
			rinsSb.append(printImage(pstrJsp, "help.gif"));
			rinsSb.append("</A>");
			return rinsSb.toString();
		}
	}

	public static String printHelpName(String pstrJsp) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewHelp();
		if (rstrStr == null || rstrStr.equals("")) {
			return "<!-- No Help Files in View XML -->";
		} else {
			return rstrStr;
		}
	}

	public static String printImageOnly(String pstrImg) {
		return printImageOnly("default", pstrImg);
	}

	public static String printImageOnly(String pstrJsp, String pstrImg) {
		String rStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewImageFile();
		return rStr + pstrImg;
	}

	public static String printImage(String pstrImg) {
		return printImage("default", pstrImg, "");
	}

	public static String printImage(String pstrJsp, String pstrImg) {
		return printImage(pstrJsp, pstrImg, "");
	}

	public static String printImage(String pstrJsp,
												String pstrImg,
												String pstrParam) {
												String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewImageFile();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No ImagePath in View XML "+pstrImg+"-->";
		else
			return "<img src=\""
						+ rstrStr
						+ pstrImg
						+ "\" border=0 "
						+ pstrParam
						+ ">";
	}

	public static String printImageTitle(String pstrJsp,
														String pstrImg,
		String pstrParam) {
		String rstrStr = ResourceMappingModel.getInstance().getSubView(pstrJsp).getViewImageFile();
		if (rstrStr == null || rstrStr.equals(""))
			return "<!-- No ImagePath in View XML "+pstrImg+"-->";
		else
			return "<img name=\"imgTitle\" src=\""
						+ rstrStr
						+ pstrImg
						+ "\" border=0 "
						+ pstrParam
						+ ">";
	}
	
	//---------------------------------------------------------------------------------------------
	// servlet path �� view.xml ���� get �ϴ� method 
	//---------------------------------------------------------------------------------------------
	
	/**
	 * @title	 ���� ȣ���ڸ� ���Ѵ�.
	 * @param pstrServletName :
	 * @return	 java.lang.String
	 */
	public static String makeServletPath(String pstrServletName) {
		if (pstrServletName == null)
			return null;
		String rstrServletName = "";
		if (pstrServletName.indexOf(".") >= 0) {
			rstrServletName = pstrServletName;
		} else {
			try {
				Config insConf = Config.getInstance();
				rstrServletName = insConf.getStringByServerEnv("/config/neo/mapping/servlet/servlet-path") + pstrServletName;
			} catch (Exception e) {
				rstrServletName = DEFAULT_SERVLET_PATH + pstrServletName;
			}
		}
		return rstrServletName;
	}
	
	public static String makeServletPath() {
		return DEFAULT_SERVLET_PATH;
	}
	
	//---------------------------------------------------------------------------------------------
	// open ��ɾ� �� job.xml ���� get �ϴ� method 
	//---------------------------------------------------------------------------------------------
	
	/**
	 * @title	 HTML���� ���Ǵ� Open Window�� ������ִ� Method�̴�.
	 * @param pstrName :
	 * @return	 java.lang.String
	 */
	public static String printOpenWindow(String pstrName) {
		return printOpenWindow(pstrName, "open");
	}

	/**
	 * @title	 HTML���� ���Ǵ� Open Window�� ������ִ� Method�̴�.
	 * @param pstrName :
	 * 				 pstrSpec :
	 * @return	 java.lang.String
	 */
	public static String printOpenWindow(String pstrName, String pstrSpec) {
		StringBuffer rinsSb = new StringBuffer();
		try {
			Config conf = Config.getInstance();
			rinsSb.append("window.open(vUrl,");
			rinsSb.append(pstrName);
			rinsSb.append(",\"");
			rinsSb.append(conf.get("/config/neo/openwindow/" + pstrSpec + "/feature"));
			rinsSb.append("\");");
		} catch (Exception e) {
			return "window.open(vUrl);";
		}
		return rinsSb.toString();
	}

	/**
	 * @title	 HTML���� ���Ǵ� Open Window�� ������ִ� Method�̴�.
	 * @param pstrName :
	 * @return	 java.lang.String
	 */
	public static String printOpenWindowCenter(String pstrName) {
		return printOpenWindowCenter(pstrName, "open");
	}

	/**
	 * @title	 HTML���� ���Ǵ� Open Window�� ����� �� ��� ����ִ� Method
	 * @param pstrName String
	 * 				 pstrSpec String
	 * @return	 java.lang.String
	 */
	public static String printOpenWindowCenter(
		String pstrName,
		String pstrSpec) {
		StringBuffer rinsSb = new StringBuffer();
		try {
			Config conf = Config.getInstance();
			rinsSb.append(conf.get("/config/neo/openwindow/" + pstrSpec + "/script"));
			rinsSb.append("window.open(vUrl,\"");
			rinsSb.append(pstrName);
			rinsSb.append("\",\"");
			rinsSb.append(conf.get("/config/neo/openwindow/" + pstrSpec + "/feature"));
			rinsSb.append("\");");
		} catch (Exception e) {
			return "window.open(vUrl);";
		}
		return rinsSb.toString();
	}

	/**
	 * @title	 HTML���� ���Ǵ� Modal Window�� ������ִ� Method�̴�.
	 * @param pstrName :
	 * @return	 java.lang.String
	 */
	public static String printModalWindow(String pstrName) {
		return printModalWindow(pstrName, "modal");
	}

	/**
	 * @title	 HTML���� ���Ǵ� Modal Window�� ������ִ� Method�̴�.
	 * @param pstrName :
	 * 				 pstrSpec :
	 * @return	 java.lang.String
	 */
	public static String printModalWindow(String pstrName, String pstrSpec) {
		StringBuffer rinsSb = new StringBuffer();
		try {
			Config conf = Config.getInstance();
			rinsSb.append("window.showModalDialog(vUrl,");
			rinsSb.append(pstrName);
			rinsSb.append(",\"");
			rinsSb.append(conf.get("/config/neo/modalwindow/" + pstrSpec + "/feature"));
			rinsSb.append(";\");");
		} catch (Exception e) {
			return "window.showModalDialog(vUrl);";
		}
		return rinsSb.toString();
	}

	/**
	 * @title	 HTML���� ���Ǵ� Modal Window�� ����� ��� ����ִ� Method�̴�.
	 * @param pstrName :
	 * @return	 java.lang.String
	 */
	public static String printModalWindowCenter(String pstrName) {
		return printModalWindowCenter(pstrName, "modal");
	}

	/**
	 * @title	 HTML���� ���Ǵ� Modal Window�� ������ִ� ��� ���� Method�̴�.
	 * @param pstrName :
	 * 				 pstrSpec :
	 * @return	 java.lang.String
	 */
	public static String printModalWindowCenter(
		String pstrName,
		String pstrSpec) {
		StringBuffer rinsSb = new StringBuffer();
		try {
			Config conf = Config.getInstance();
			//rinsSb.append(conf.get( "/configuration/neo/modalwindow/" +pstrSpec + "/script" ));
			rinsSb.append("window.showModalDialog(vUrl,");
			rinsSb.append(pstrName);
			rinsSb.append(",\"");
			rinsSb.append(conf.get("/config/neo/modalwindow/" + pstrSpec + "/feature"));
			rinsSb.append("\");");
		} catch (Exception e) {
			return "window.showModalDialog(vUrl);";
		}
		return rinsSb.toString();
	}

	/**
	 * @title	 HTML���� ���Ǵ� Printing Window�� ������ִ� Method�̴�.		
	 * @param pstrName :
	 * @return	 java.lang.String
	 */
	public static String printPrintingWindow(String pstrName) {
		return printModalWindow(pstrName, "printing");
	}
	
	//---------------------------------------------------------------------------------------------
	// combo box �� �����ϴ� method (������ ����� �ƴ�, �ϵ� �ڵ� ���ٴ� ���� ���) 
	//---------------------------------------------------------------------------------------------

	/**
	 * @title	 HTML���� ���Ǵ� SELECT BOX�� ����� �ִ� Method�̴�.
	 * 				 ����Ʈ�� ǥ���� ���� 2���� String Array�� ArrayList�� ���ȴ�.
	 * 				 ���õ� ��(SELECTED)�� �Է� Parameter�� ���� ���� �����ϴ�.
	 * 				 �ʱⰪ ���θ� true�� �����ϸ� ���õ� ���� �ʱ� ������ �̿�ȴ�.
	 * @param pstrInput String : List�� ���߿� ���� ���� ������ SELECTED�� ǥ��
	 * 				 pstrName String : SELECT Tag �� Name
	 * 				 pstrScript String : SELECT Tag �� �����ϰ��� �ϴ� Java Script
	 * 				 pinsOptions ArrayList : �������� List��. 2���� String Array�� ArrayList.
	 * 				 pbInitial boolean : '�����ϼ���'���� �ʱ� �۾��� �ʿ��ϸ� True
	 * @return	java.lang.String
	 */
	public static String printSelect(
		String pstrInput,
		String pstrName,
		String pstrScript,
		ArrayList pinsOptions,
		boolean pbInitial) {
		StringBuffer rinsSb = new StringBuffer("<SELECT ALIGN=middle NAME='");
		rinsSb.append(pstrName);
		rinsSb.append("' SIZE=1 ");
		rinsSb.append(pstrScript);
		rinsSb.append(" >");
		if (pbInitial) {
			rinsSb.append("<OPTION VALUE='' SELECTED>");
			rinsSb.append(pstrInput);
			rinsSb.append("</OPTION>");
		}
		for (int i = 0; i < pinsOptions.size(); i++) {
			rinsSb.append("<OPTION VALUE='");
			rinsSb.append(((String[]) pinsOptions.get(i))[0]);
			if (pstrInput.equals(((String[]) pinsOptions.get(i))[0]) && !pbInitial)
				rinsSb.append("' SELECTED>");
			else
				rinsSb.append("'>");
			rinsSb.append(((String[]) pinsOptions.get(i))[1]);
			rinsSb.append("</OPTION>");
		}
		rinsSb.append("</SELECT>");
		return rinsSb.toString();
	}

	public static String printSelect(String pstrInput,
													String pstrName,
													ArrayList pinsOptions) {
		return printSelect(pstrInput, pstrName, pinsOptions, false);
	}

	public static String printSelect(String pstrInput,
													String pstrName,
													ArrayList pinsOptions,
													boolean pbInitial) {
		return printSelect(pstrInput, pstrName, "", pinsOptions, pbInitial);
	}	

	public static String printSelect(String pstrInput,
												String pstrName,
												String pstrScript,
												ArrayList pinsOptions,
												String pstrDelimiter,
		boolean pbInitial) {
		StringBuffer rinsSb = new StringBuffer("<SELECT ALIGN=middle NAME='");
		rinsSb.append(pstrName);
		rinsSb.append("' SIZE=1 ");
		rinsSb.append(pstrScript);
		rinsSb.append(" >\n");
		if (pbInitial) {
			rinsSb.append("<OPTION VALUE='' SELECTED>");
			rinsSb.append(pstrInput);
			rinsSb.append("</OPTION>\n");
		}
		for (int i = 0; i < pinsOptions.size(); i++) {
			rinsSb.append("<OPTION VALUE='");
			rinsSb.append(TText.getToken((String) pinsOptions.get(i), pstrDelimiter, 1));
			if (pstrInput.equals(TText.getToken((String) pinsOptions.get(i),pstrDelimiter,1)) && !pbInitial)
				rinsSb.append("' SELECTED>\n");
			else
				rinsSb.append("'>");
			rinsSb.append(TText.getToken((String) pinsOptions.get(i), pstrDelimiter, 2));
			rinsSb.append("</OPTION>\n");
		}
		rinsSb.append("</SELECT>\n");
		return rinsSb.toString();
	}
	
	public static void hashE2K(java.util.Hashtable hash) {
		java.util.Enumeration e = hash.keys();
		if (e.hasMoreElements()) {
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				hash.put(
					name.toLowerCase(),
					StringUtil.e2k(hash.get(name).toString()));
			}
		}
	}

	public static void hashEmptyDefault(
		java.util.Hashtable hash,
		String name,
		Object value) {
		if (hash.get(name).toString().equals(""))
			hash.put(name, value);
	}

	public static java.util.Hashtable setReq2Hashtable(
		javax.servlet.http.HttpServletRequest req) {
		java.util.Hashtable hs = new java.util.Hashtable();
		try {
			java.util.Enumeration e = req.getParameterNames();

			if (e.hasMoreElements()) {
				while (e.hasMoreElements()) {
					String name = (String) e.nextElement();
					if (req.getParameterValues(name) == null)
						hs.put(name.toLowerCase(), "");
					else if (req.getParameterValues(name).length == 1)
						hs.put(name.toLowerCase(), req.getParameter(name));
					else
						hs.put(
							name.toLowerCase(),
							req.getParameterValues(name));
				}
			}
			return hs;
		} catch (Exception e1) {
			e1.printStackTrace(System.out);
		}

		return hs;
	}

	// name ��Ű�� value ���� �־ iMinute �е��� �����ϰ� �մϴ�.
	public static void setCookie(HttpServletResponse res,
												String name,
												String value,
												int iMinute) {
		//value = java.net.URLEncoder.encode(value);
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * iMinute);
		res.addCookie(cookie);
	}

	// name ��Ű�� value ���� �־ 15�� ���� �����ϰ� �մϴ�.
	public static void setCookie(HttpServletResponse res,
												String name,
												String value) {
		//value = java.net.URLEncoder.encode(value);
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * 60 * 24 * 15);
		res.addCookie(cookie);
	}

	//name ��Ű�� �����Ѵ�.
	public static void killCookie(HttpServletResponse res,
												String name) {
		//value = java.net.URLEncoder.encode(value);
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0); //�� ���� 0�� ��쿡�� �������� ����Ǵ� ������ �ڵ������� �ش� ��Ű�� �����ȴ�.
		res.addCookie(cookie);
	}

	// cookieName �� ���� �����ɴϴ�.
	public static String getCookieValue(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();

		String retvVal = "";

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				Log.info(cookie.getName() + " == " + name, null);
				if (cookie.getName().equals(name)) {
					retvVal = cookie.getValue();
					//retvVal = java.net.URLDecoder.decode(cookie.getValue());
					Log.info(cookie.getName() + " => " + retvVal, null);
					break;
				}
			}
		}
		return retvVal;
	}

	// ���ڵ��� �Ķ������ ���� ���ڵ��Ͽ� �����մϴ�
	public static String UrlDecode(String strEncode) {
		String sRet = "";
		try {
			sRet = strEncode;
			StringTokenizer token = new StringTokenizer(sRet, ";");
			sRet = "";
			Character ch = null;
			while (token.hasMoreTokens()) {
				ch = new Character((char) Integer.parseInt(token.nextToken()));
				sRet += ch.toString();
			}
		} catch (Exception e) {
			sRet = "";
		}
		return sRet;
	}

	/*
	public static boolean checkUrl(String url) {
		boolean rt = false;
	  if (url.startsWith("www.")) {
	   url = "http://" + url;
	  }
	  HttpServletRequest myRequest = (HttpServletRequest)WebRequest.Create(url); 
	  HttpStatusCode code = new HttpStatusCode(); 
	  HttpWebResponse myResponse = null; 
	  try
	  { 
	   myResponse = (HttpWebResponse)myRequest.GetResponse();
	   code = myResponse.StatusCode;
	   rt = true;
	  }
	  catch(WebException err)
	  {
	   rt = false;
	  } 
	  return rt;
	}
	*/
}
