package neo.util.comm;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	StringUtil.java
 * 	@���ϼ���		: 	���� ���� ��ƿ
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
public class StringUtil {
	public final static String DEFAULT_DATE_FORMAT = "yyyyMMdd";
	public final static String SLASH_DATE_FORMAT 	= "yyyy/MM/dd";
	public final static String BAR_DATE_FORMAT = "yyyy-MM-dd";

	public static String objectToString(Object obj) {
		String result = "";
		if (obj != null)
			result = (String) obj.toString();
		return result;
	}

	public static String[] vectorToStrings(Vector lists) {
		String[] tempArr = null;
		if (lists != null) {
			tempArr = new String[lists.size()];
			for (int i = 0; i < lists.size(); i++) {
				tempArr[i] = (String) lists.elementAt(i);
			}
		}
		return tempArr;
	}

	public static Vector stringArrToVector(String[] lists) {
		Vector targetList = new Vector();
		if (lists != null) {
			for (int i = 0; i < lists.length; i++) {
				targetList.add(lists[i]);
			}
		}
		return targetList;
	}

	public static String[] stringToStringArray(String instr, String delim)
		throws NoSuchElementException, NumberFormatException {
		StringTokenizer toker = new StringTokenizer(instr, delim);
		String stringArray[] = new String[toker.countTokens()];
		int i = 0;

		while (toker.hasMoreTokens()) {
			stringArray[i++] = toker.nextToken();
		}
		return stringArray;
	}

	public static int[] stringToIntArray(String instr, String delim)
		throws NoSuchElementException, NumberFormatException {
		StringTokenizer toker = new StringTokenizer(instr, delim);
		int intArray[] = new int[toker.countTokens()];
		int i = 0;

		while (toker.hasMoreTokens()) {
			String sInt = toker.nextToken();
			int nInt = Integer.parseInt(sInt);
			intArray[i++] = new Integer(nInt).intValue();
		}
		return intArray;
	}

	public static String intArrayToString(int[] intArray) {
		String ret = new String();
		for (int i = 0; i < intArray.length; i++) {
			if (ret.length() > 0)
				ret = ret + "," + Integer.toString(intArray[i]);
			else
				ret = Integer.toString(intArray[i]);
		}
		return ret;
	}
	
	public static String iif(boolean lsw, String trueStr, String falseStr) {
		String retvStr = "";
		if (lsw) {
			retvStr = trueStr;
		} else {
			retvStr = falseStr;
		}
		return retvStr;
	}

	public static boolean stringisNull(String str) {
		boolean resultBool = false;
		if (!"".equals(str) || str != null)
			resultBool = true;
		return resultBool;
	}

	public static boolean isNull(String str) {
		boolean resultBool = false;
		if (str == null || str.length() == 0)
			resultBool = true;

		return resultBool;
	}

	public static String NVL(String cstr) {
		if (cstr == null) {
			cstr = "";
		}
		return cstr;
	}

	public static int NVL_Num(String cstr) {
		if (cstr == null) {
			return 0;
		} else {
			return strToInt(cstr);
		}
	}

	public static String NVL(String cstr, String ndef) {
		if (cstr == null) {
			cstr = ndef;
		}
		return cstr;
	}

	public static int NVL_Num(String cstr, int ndef) {
		if (cstr == null) {
			return ndef;
		} else {
			return strToInt(cstr);
		}
	}

	public static String OVL(Object obj) {
		return obj != null && !obj.toString().equals("") ? obj.toString() : "";
	}

	public static String OVL(Object obj, String str) {
		return obj != null && !obj.toString().equals("") ? obj.toString() : str;
	}

	public static String dateToString(java.util.Date date) {
		String result = "";
		if (date != null) {
			result = date.toString();
		}
		return result;
	}

	public static boolean equals(String str1, String str2) {
		return (str1 == null || str2 == null)
			? false
			: str1.trim().equals(str2.trim());
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return (str1 == null || str2 == null)
			? false
			: str1.trim().equalsIgnoreCase(str2.trim());
	}

	public static String GetBunStr(String sysloc, String str) {
		try {
			if (sysloc.equals("ko")) {
				return new String(str.getBytes("8859_1"), "KSC5601");
			}
		} catch (UnsupportedEncodingException e1) {
		}
		return str;
	}

	public static String getDbStr(String sysloc, String charset, String str) {
		//try {
		if (!sysloc.equals("ko") && !sysloc.equals("en")) {
			Log.info("[PUtil] org is :: " + "\u65e5\u672c\u8a9e\u6587\u5b57\u5217", null);
			Log.info("[PUtil Argument] str is :: " + str, null);
			/*
			 * byte[] buffer = str.getBytes(); ByteArrayInputStream is = new
			 * ByteArrayInputStream(buffer); BufferedReader readstr = new
			 * BufferedReader( new InputStreamReader( is, charset)); return
			 * readstr.readLine();
			 */
			//String retvStr = new
			// String("\u65e5\u672c\u8a9e\u6587\u5b57\u5217");
			String retvStr = new String(str);
			return retvStr;

		}
		//} catch ( IOException e2 ) {
		//}
		return str;
	}

	public static String e2k(String str) {
		//return str;
		return fromDB(str);
	}

	public static String k2e(String str) {
		//return str;
		return toDB(str);
	}

	public static String fromDB(String str) {
		try {
			if (str != null) {
				return new String(str.getBytes("KSC5601"), "8859_1");
			} else {
				return "";
			}
		} catch (UnsupportedEncodingException e1) {
		}
		return str;
	}

	public static String toDB(String str) {
		try {
			if (str != null) {
				return new String(str.getBytes("8859_1"), "KSC5601");
			} else {
				return "";
			}
		} catch (UnsupportedEncodingException e1) {
		}
		return str;
	}

	public static synchronized String Br2N(String s) {
		return replace("<BR>", "\n", s);
	}

	public static synchronized String N2Br(String s) {
		return replace(s, "\n", "<BR>");
	}

	public static String nSp(String s) {
		String Br;
		Br = replace(s, "\n", "<BR>");
		Br = replace(Br, "\r", "");
		//Br = replace(Br, "\t", "&nbsp;&nbsp;");    
		Br = replace(Br, "&", "&amp;");
		Br = replace(Br, "\"", "&quot;");
		return Br;
	}

	public static String HTML(String s) {
		String html;
		html = replace(s, "&", "&amp;");
		//html = replace(html, "\t", "&nbsp;&nbsp;");
		html = replace(html, "\"", "&quot;");
		html = replace(html, "<", "&lt;");
		html = replace(html, ">", "&gt;");
		html = replace(html, " ", "&nbsp;");
		html = replace(html, "\n", "<BR>");
		return html;
	}

	public static String BrHTML(String s) {
		String html;
		html = replace(s, "\n", "<BR>");
		//html = replace(html, "\t", "&nbsp;&nbsp;");
		html = replace(html, "\"", "&quot;");
		return html;
	}

	public static String encodeHTMLSpecialChar(String str, int n) {
		switch (n) {
			case 1 : // text mode db �Է�
				str = rplc(str, "<", "&lt;");
				str = rplc(str, "\"", "&quot;");
				break;
			case 2 : // html mode db �Է�
				str = rplc(str, "<sc", "<x-sc");
				str = rplc(str, "<title", "<x-title");
				str = rplc(str, "<xmp", "<x-xmp");
				break;
			case 11 : // text �϶� CONENT ó��
				str = rplc(str, " ", "&nbsp;");
				str = rplc(str, "\n", "<br>");
				break;
			case 13 : // comment ���� �϶�
				str = rplc(str, "<sc", "<x-sc");
				str = rplc(str, "<title", "<x-title");
				str = rplc(str, "<xmp", "<x-xmp");
				str = rplc(str, "\n", "<br>");
				break;
			case 14 : // text mode db �Է�
				str = rplc(str, "&quot;", "\"");
				break;
		}
		return str;
	}

	/**
	 * html ���� �±׷� �Դ� ����(<,>,",')�� ��ȯ
	 * 
	 * @param szDetail �������ڿ�
	 * @return ����� ���ڿ�
	 */
	public static String replaceToHtml(String szDetail) {

		if (szDetail == null)
			return "";

		StringBuffer szbWriteDetail = new StringBuffer();

		for (int i = 0, len = szDetail.length(); i < len; i++) {
			//if( szDetail.charAt(i) == ' ')
			//  szbWriteDetail.append("&nbsp;");
			if (szDetail.charAt(i) == '<')
				szbWriteDetail.append("&lt;");
			else if (szDetail.charAt(i) == '>')
				szbWriteDetail.append("&gt;");
			else if (szDetail.charAt(i) == '\"')
				szbWriteDetail.append("&quot;");
			else if (szDetail.charAt(i) == '\'')
				szbWriteDetail.append("&#39;");
			else if (szDetail.charAt(i) == '&')
				szbWriteDetail.append("&amp;");
			else if (szDetail.charAt(i) == '\n')
				szbWriteDetail.append("<br/>");
			else
				szbWriteDetail.append(szDetail.charAt(i));
		}

		return szbWriteDetail.toString();
	}
	
	/**
	 * html ���� �±׷� �Դ� ����(<,>,",')�� ��ȯ(\n����)
	 * 
	 * @param szDetail �������ڿ�
	 * @return ����� ���ڿ�
	 */
	public static String replaceToHtml_AREA(String szDetail) {

		if (szDetail == null)
			return "";

		StringBuffer szbWriteDetail = new StringBuffer();

		for (int i = 0, len = szDetail.length(); i < len; i++) {
			//if( szDetail.charAt(i) == ' ')
			//  szbWriteDetail.append("&nbsp;");
			if (szDetail.charAt(i) == '<')
				szbWriteDetail.append("&lt;");
			else if (szDetail.charAt(i) == '>')
				szbWriteDetail.append("&gt;");
			else if (szDetail.charAt(i) == '\"')
				szbWriteDetail.append("&quot;");
			else if (szDetail.charAt(i) == '\'')
				szbWriteDetail.append("&#39;");
			else if (szDetail.charAt(i) == '&')
				szbWriteDetail.append("&amp;");
			else
				szbWriteDetail.append(szDetail.charAt(i));
		}

		return szbWriteDetail.toString();
	}

	public static String CrBr(String s) {
		String html;
		html = replace(s, "\n", "<BR>");
		return html;
	}

	public static String BrCr(String s) {
		String html;
		html = replace(s, "<BR>", "\n");
		return html;
	}

	public static String spaceTonbsp(String s) {
		String html;
		html = replace(s, " ", "&nbsp;");
		return html;
	}

	public static String checkNull(String str) {
		String strTmp;
		if (str == null)
			strTmp = "&nbsp";
		else
			strTmp = str;
		return strTmp;
	}
	
	//�־��� ���ڿ��� ��� �ش� ���ڸ� �����Ѵ�
	public static String removeEach( char cChar, String strString ) {
        String strResult = strString;
        for ( int i = 0; i < strResult.length(); i++ )
            if ( strResult.charAt(i) == cChar ) {
                StringBuffer insBuff = new StringBuffer( strResult );
                insBuff.deleteCharAt(i);
                strResult = insBuff.toString();
            }
        return strResult;
    }

	public static String replace(String src, String oldstr, String newstr) {
		if (src == null) {
			return null;
		}

		StringBuffer dest = new StringBuffer("");
		int len = oldstr.length();
		int srclen = src.length();
		int pos = 0;
		int oldpos = 0;

		while ((pos = src.indexOf(oldstr, oldpos)) >= 0) {
			dest.append(src.substring(oldpos, pos));
			dest.append(newstr);
			oldpos = pos + len;
		}

		if (oldpos < srclen) {
			dest.append(src.substring(oldpos, srclen));

		}
		return dest.toString();
	}

	public static String crop(String str, int i) {
		//return shortCutString(str, i);

		if (str == null)
			return "";

		String tmp = str;

		if (tmp.length() > i)
			tmp = tmp.substring(0, i) + "...";

		return tmp;
	}

	public static synchronized String getRandomColor() {
		String s = Integer.toHexString((int) (Math.random() * 256D));
		String s1 = Integer.toHexString((int) (Math.random() * 256D));
		String s2 = Integer.toHexString((int) (Math.random() * 256D));
		return "#" + s + s1 + s2;
	}

	public static String chkRandom() throws Exception { //sms����Ű �߻�
		String chk_no = "";
		try {
			java.util.Random ran1 = new java.util.Random();
			int no = ran1.nextInt() % 40000000 + 50000000;
			chk_no = new Integer(no).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chk_no;
	}

	public static String wwwlink(String str) {
		if (str == null)
			return "";

		String tmp = str;
		int itmp = 0;
		int wend = 0;

		StringBuffer sb = new StringBuffer();
		sb.append("");

		while (tmp.indexOf("http://") > -1) {

			itmp = tmp.indexOf("http://");
			wend = tmp.indexOf(" ", itmp);

			if (wend > tmp.indexOf("\r", itmp))
				wend = tmp.indexOf("\r", itmp);
			if (wend > tmp.indexOf("<", itmp))
				wend = tmp.indexOf("<", itmp);
			else if (wend == -1)
				wend = tmp.length();

			sb.append(tmp.substring(0, itmp));

			if (itmp > 3 && tmp.substring(itmp - 3, itmp).indexOf("=") > -1) {
				wend = tmp.indexOf("</a>", itmp) + 3;

				if (wend == 2)
					wend = tmp.indexOf(">", itmp);
				sb.append(tmp.substring(itmp, wend));
			} else {
				sb.append(
						"<a href=\""
						+ tmp.substring(itmp, wend)
						+ "\" target=\"_blank\">");
				sb.append(tmp.substring(itmp, wend));
				sb.append("</a>");
			}

			tmp = tmp.substring(wend);
		}
		sb.append(tmp);

		tmp = sb.toString();
		sb.setLength(0);

		while (tmp.indexOf("www.") > -1) {
			itmp = tmp.indexOf("www.");
			wend = tmp.indexOf(" ", itmp);

			if (wend > tmp.indexOf("\r", itmp))
				wend = tmp.indexOf("\r", itmp);
			if (wend > tmp.indexOf("<", itmp))
				wend = tmp.indexOf("<", itmp);
			if (wend == -1)
				wend = tmp.length();

			sb.append(tmp.substring(0, itmp));

			if (itmp > 10
				&& tmp.substring(itmp - 10, itmp).indexOf("=") > -1) {
				wend = tmp.indexOf("</a>", itmp) + 3;
				if (wend == 2)
					wend = tmp.indexOf(">", itmp);
				sb.append(tmp.substring(itmp, wend));
			} else {
				sb.append(
						"<a href=\"http://"
						+ tmp.substring(itmp, wend)
						+ "\" target=\"_blank\">");
				sb.append(tmp.substring(itmp, wend));
				sb.append("</a>");
			}
			tmp = tmp.substring(wend);
		}
		sb.append(tmp);

		return sb.toString();
	}

	public static String useSinglequot(String text) {
		int pos = 0;
		while ((pos = text.indexOf("\'", pos)) != -1) {
			String left = text.substring(0, pos);
			String rigth = text.substring(pos, text.length());
			text = left + "\'" + rigth;
			pos += 2;
		}
		return text;
	}

	public static String convQuoto(String cstr) {
		StringBuffer sb = new StringBuffer(cstr);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '\'') {
				sb.insert(++i, '\'');
			}
		}
		cstr = sb.toString();

		StringBuffer sb2 = new StringBuffer(cstr);
		for (int i = 0; i < sb2.length(); i++) {
			if (sb2.charAt(i) == '\"') {
				sb2.insert(++i, '\"');
			}
		}
		return sb2.toString();
	}
	
	/**
	 * body �±׾ȿ��� ��������
	 * 
	 * @param �������ڿ�
	 * @return body tag ���� ���ڿ�
	 */
	public static String replaceToHtmlBody(String szDetail) {
		int startIdx = szDetail.indexOf("<body>") + "<body>".length();
		int endIdx = szDetail.indexOf("</body>");
		return substr(szDetail, startIdx, endIdx);

	}

	protected static String removeComma(String s) {

		if (s == null) {
			return null;
		}
		if (s.indexOf(",") != -1) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c != ',') {
					buf.append(c);
				}
			}
			return buf.toString();
		}

		return s;
	}

	public static String removeCharacter(String sStr, String sChr) {

		if (sStr == null) {
			return "";
		}
		if (sChr == null) {
			return "";
		}

		if (sChr.length() == 1) {
			return removeCharacter(sStr, sChr.charAt(0));
		} else {
			return removeCharacter2(sStr, sChr);
		}
	}

	public static String removeCharacter2(String sStr, String sChr) {
		String retr = "";
		StringTokenizer st = new StringTokenizer(sStr, sChr);

		while (st.hasMoreTokens()) {
			retr += st.nextToken();
		}

		return retr;
	}

	public static String removeCharacter(String sStr, char sChr) {

		if (sStr == null) {
			return "";
		}

		char[] fromchars = sStr.toCharArray();
		StringBuffer tochars = new StringBuffer(fromchars.length);
		for (int i = 0, p = fromchars.length; i < p; i++) {
			if (sChr != fromchars[i]) {
				tochars.append(fromchars[i]);
			}
		}

		return tochars.toString();
	}

	public static String trim(String s) {
		int st = 0;
		char[] val = s.toCharArray();
		int count = val.length;
		int len = count;

		while ((st < len) && ((val[st] <= ' ') || (val[st] == '��'))) {
			st++;
		}
		while ((st < len)
			&& ((val[len - 1] <= ' ') || (val[len - 1] == '��'))) {
			len--;
		}

		return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
	}

	public static String ltrim(String str) {
		if (str == null)
			return "";
		for (int i = 0; i < str.length(); i++)
			if (" \t\n\r\f".indexOf(str.charAt(i)) == -1)
				return str.substring(i);

		return "";
	}

	public static String rtrim(String str) {
		if (str == null || str.equals(""))
			return "";
		for (int i = str.length() - 1; i >= 0; i--)
			if (" \t\n\r\f".indexOf(str.charAt(i)) == -1)
				return str.substring(0, i + 1);

		return "";
	}

	public static String alltrim(String str) {
		String result = "";

		if (str != null) {
			result = rplc(str," ","");
		} else {
			result = "";
		}

		return result;
	}

	/**
	 * Returns value argument, left-padded to length padLen argument with the sequence of character in padChar argument. 
	 * If the value argument is null, value argument think of empty string ("").
	 * 
	 * @param s   String value.
	 * 				 i - the total length of the return value.
	 * 				 c - padded character.
	 * @return left padded string
	 */
	public static String lpad(String s, int i, char c) {
		if (s == null)
			s = "";
		for (; s.length() < i; s = c + s);
		return s;
	}

	public static String lpad(short word0, int i, char c) {
		return lpad(String.valueOf(word0), i, c);
	}

	public static String lpad(int i, int j, char c) {
		return lpad(String.valueOf(i), j, c);
	}

	public static String lpad(long l, int i, char c) {
		return lpad(String.valueOf(l), i, c);
	}

	public static String lpad(float f, int i, char c) {
		return lpad(String.valueOf(f), i, c);
	}

	public static String lpad(double d, int i, char c) {
		return lpad(String.valueOf(d), i, c);
	}

	/**
	 * Returns value argument, right-padded to length padLen argument with the sequence of character in padChar argument.
	 * 
	 * @param s a double value.
	 * 				 i - the total length of the return value.
	 * 				 c - padded character.
	 * @return right  padded string
	 */
	public static String rpad(String s, int i, char c) {
		if (s == null)
			s = "";
		for (; s.length() < i; s = s + c);
		return s;
	}

	public static String rpad(short word0, int i, char c) {
		return rpad(String.valueOf(word0), i, c);
	}

	public static String rpad(int i, int j, char c) {
		return rpad(String.valueOf(i), j, c);
	}

	public static String rpad(long l, int i, char c) {
		return rpad(String.valueOf(l), i, c);
	}

	public static String rpad(float f, int i, char c) {
		return rpad(String.valueOf(f), i, c);
	}

	public static String rpad(double d, int i, char c) {
		return rpad(String.valueOf(d), i, c);
	}

	public static String substring(String str, int length) {
		String result = "";
		if (str.length() > 0 && length <= str.length())
			result = str.substring(0, length);
		return result;
	}

	public static String substr(String str, int start, int end) {
		String result = "";
		if (str.length() > 0
			&& start <= str.length()
			&& end <= str.length()
			&& start <= end)
			result = str.substring(start, end);

		return result;
	}

	/**
	 * ���ڿ��� ���ϴ� ���̸�ŭ �ڸ��� �����ʿ� ���ϴ� ���ڸ� �߰��Ѵ�.
	 * 
	 * @param obj : �������ڿ�
	 * 				 endIndex  : �߶� ��ġ
	 * 				 addStr  : �߰��� ���ڿ�
	 * @return ����� ���ڿ�: Null �� ��� ����, ���ϴ� ���̺��� �������ڿ��� ������� �������ڿ� return
	 */
	public static String rightCutAddStr(
		String str,
		int beginIndex,
		int endIndex,
		String addStr) {
		String temp_str = substr(str, beginIndex, endIndex);

		if (temp_str == null)
			return "";

		if (temp_str.length() > endIndex) {
			str += addStr;
		}

		return str;
	}
	
	/**
	 * ���ڿ��� ���ϴ� ���̸�ŭ �ڸ��� �����ʿ� ���ϴ� ���ڸ� �߰��Ѵ�.
	 * 
	 * @param obj : �������ڿ�
	 * 				 endIndex  : �߶� ��ġ
	 * 				 addStr  : �߰��� ���ڿ�
	 * @return ����� ���ڿ�: Null �� ��� ����, ���ϴ� ���̺��� �������ڿ��� ������� �������ڿ� return
	 */
	public static String getOmitted( String strStr, int iValidLength ) {
        String strResult = null;
        if ( strStr.length() > iValidLength - 3 )
            strResult = strStr.substring( 0, iValidLength ) + "...";
        else
            strResult = strStr;
        return strResult;
    }

    public static String getOmitted( String strStr ) {
        return getOmitted( strStr, 80 );
    }

	public static String convertYn(String ynStr) {
		if (ynStr == null)
			return "";
		if (ynStr.trim().toUpperCase().equals("Y"))
			return "��";
		if (ynStr.trim().toUpperCase().equals("N"))
			return "�ƴϿ�";

		return "";
	}

	public static String shortCutString(String str, int limit) {
		if (str == null || limit < 4)
			return str;

		int len = str.length();
		int cnt = 0, index = 0;

		while (index < len && cnt < limit) {
			if (str.charAt(index++) < 256) // 1����Ʈ ���ڶ��...
				cnt++; // ���� 1 ����
			else
				// 2����Ʈ ���ڶ��...
				cnt += 2; // ���� 2 ����
		}

		if (index < len)
			str = str.substring(0, index) + "...";

		return str;
	}

	public static String removeTag(String str) {
		int lt = str.indexOf("<");
		if (lt != -1) {
			int gt = str.indexOf(">", lt);
			if ((gt != -1)) {
				str = str.substring(0, lt) + str.substring(gt + 1);
				str = removeTag(str);
			}
		}
		return str;
	}

	/**
	 * Method removes HTML tags from given string.
	 * 
	 * @param text  Input parameter containing HTML tags (eg. <b>cat</b>)
	 * @return String without HTML tags (eg. cat)
	 */
	public static String removeHtml(String text) {
		try {
			int idx = text.indexOf("<");
			if (idx == -1)
				return text;

			String plainText = "";
			String htmlText = text;
			int htmlStartIndex = htmlText.indexOf("<", 0);
			if (htmlStartIndex == -1) {
				return text;
			}
			while (htmlStartIndex >= 0) {
				plainText += htmlText.substring(0, htmlStartIndex);
				int htmlEndIndex = htmlText.indexOf(">", htmlStartIndex);
				htmlText = htmlText.substring(htmlEndIndex + 1);
				htmlStartIndex = htmlText.indexOf("<", 0);
			}
			plainText = plainText.trim();
			return plainText;
		} catch (Exception e) {
			Log.info(
				"Error while removing HTML: "
					+ e.getClass().getName()
					+ " "
					+ e.toString());
			return text;
		}
	}

	public static String insert(String strTarget, int loc, String strInsert) {
		String result = null;
		StringBuffer strBuf = new StringBuffer();
		int lengthSize = strTarget.length();

		if (loc >= 0) {
			if (lengthSize < loc) {
				loc = lengthSize;
			}
			strBuf.append(strTarget.substring(0, loc));
			strBuf.append(strInsert);
			strBuf.append(strTarget.substring(loc + strInsert.length()));
		} else {
			if (lengthSize < Math.abs(loc)) {
				loc = lengthSize * (-1);
			}
			strBuf.append(strTarget.substring(0, (lengthSize - 1) + loc));
			strBuf.append(strInsert);
			strBuf.append(
				strTarget.substring(
					(lengthSize - 1) + loc + strInsert.length()));
		}
		result = strBuf.toString();
		return result;
	}

	public static String delete(String strTarget, String strDelete) {
		return replace(strTarget, strDelete, "");
	}

	public static String[] token(String str, String tokenStr) {
		StringTokenizer stringTokenizer = new StringTokenizer(str, tokenStr);
		String tokenString[] = new String[stringTokenizer.countTokens()];

		int i = 0;
		while (stringTokenizer.hasMoreElements()) {
			tokenString[i++] = stringTokenizer.nextToken();
		}
		return tokenString;
	}

	public static String[] split(
		String strTarget,
		String strDelim,
		boolean bContainNull) {
		int index = 0;
		String[] resultStrArray = null;

		resultStrArray = new String[search(strTarget, strDelim) + 1];
		String strCheck = new String(strTarget);
		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strDelim);
			if (begin == -1) {
				resultStrArray[index] = strCheck;
				break;
			} else {
				int end = begin + strDelim.length();
				if (bContainNull) {
					resultStrArray[index++] = strCheck.substring(0, begin);
				}
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					resultStrArray[index] = strCheck;
					break;
				}
			}
		}
		return resultStrArray;
	}

	/**
	 * Split string into multiple strings
	 * 
	 * @param original : Original string
	 * 				 separator : Separator string in original string
	 * @return Splitted string array
	 */
	public static String[] split(String original, String separator) {
		Vector nodes = new Vector();

		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++)
				result[loop] = (String) nodes.elementAt(loop);
		}
		return result;
	}

	/**
	 * Split string into multiple strings
	 * 
	 * @param originals : Original strings
	 * 				 joinStr : Join string
	 * 				 index : Index to start at
	 * @return Joined string
	 * @exception Exception
	 */
	public static String join(String[] originals, String joinStr, int index) {
		if (originals == null)
			return null;
		if (joinStr == null)
			joinStr = "";
		StringBuffer sb = new StringBuffer(originals[index]);

		// Parse nodes into vector
		for (int ic = index + 1; ic < originals.length; ic++) {
			sb.append(joinStr + originals[ic]);
		}
		return sb.toString();
	}

	public static int search(String strTarget, String strSearch) {
		int result = 0;
		String strCheck = new String(strTarget);

		for (int i = 0; i < strTarget.length();) {
			int loc = strCheck.indexOf(strSearch);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}

		return result;
	}

	public static String searchWord(String strSubject, String strKeyword) {
		String alterKey = "<font color=red>" + strKeyword + "</font>";
		alterKey = rplc(strSubject, strKeyword, alterKey);
		return alterKey;
	}

	public static String rplc(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}

		result.append(str.substring(s));

		return result.toString();
	}
	
	/**
	 * Replace all instances of a String in a String.
	 * 
	 * @param s : String to alter.
	 * 				 f : String to look for.
	 * @return r  String to replace it with, or null to just remove it.
	 */
	public static String replace2(String s, String f, String r) {
		if (s == null)
			return s;
		if (f == null)
			return s;
		if (r == null)
			r = "";

		int index01 = s.indexOf(f);
		while (index01 != -1) {
			s = s.substring(0, index01) + r + s.substring(index01 + f.length());
			index01 += r.length();
			index01 = s.indexOf(f, index01);
		}
		return s;
	}

	public static String commaIn(String str) {
		String strCommaIn = "";
		if (!str.equals("")) {
			strCommaIn =
				NumberFormat.getInstance().format(Double.parseDouble(str));
		}
		return strCommaIn;
	}

	public static String commaOut(String str) {
		String strCommaOut = "";
		if (!str.equals("")) {
			StringTokenizer strTokenizer = new StringTokenizer(str, ",");
			StringBuffer strBuffer = new StringBuffer();
			while (strTokenizer.hasMoreTokens()) {
				strBuffer.append(strTokenizer.nextToken());
			}
			strCommaOut = strBuffer.toString();
		}
		return strCommaOut;
	}

	public static String getDecimalFormat(double doubleNumber, int fraction) {
		String result = "";

		java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();

		if (fraction > 4)
			fraction = 0;

		switch (fraction) {
			case 0 :
				numberFormat.applyPattern("#,##0");
				break;
			case 1 :
				numberFormat.applyPattern("#,##0.0");
				break;
			case 2 :
				numberFormat.applyPattern("#,##0.00");
				break;
			case 3 :
				numberFormat.applyPattern("#,##0.000");
				break;
			case 4 :
				numberFormat.applyPattern("#,##0.0000");
				break;
			case 5 :
				numberFormat.applyPattern("#0");
				break;
			default :
				numberFormat.applyPattern("#,##0.00");
				break;
		}

		try {
			result = numberFormat.format(doubleNumber);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		return result;
	}

	public static String getDecimalFormat(String str, int fraction)
		throws Exception {
		String stringNumber = str;
		double doubleNumber = 0;

		if (stringNumber.equals(""))
			stringNumber = "0";

		try {
			doubleNumber = Double.parseDouble(stringNumber);
		} catch (NumberFormatException e) {
			throw e;
		}

		return getDecimalFormat(doubleNumber, fraction);
	}

	public static String getDecimalFormat(Object obj, int fraction)
		throws Exception {
		String stringNumber = obj.toString();
		double doubleNumber = 0;

		if (stringNumber.equals(""))
			stringNumber = "0";

		try {
			doubleNumber = Double.parseDouble(stringNumber);
		} catch (NumberFormatException e) {
			throw e;
		}

		return getDecimalFormat(doubleNumber, fraction);
	}

	public static boolean isInteger(String str) {
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) < '0' || str.charAt(i) > '9')
				&& str.charAt(i) != '-')
				return false;
		}
		return true;
	}

	public static String getDecimalFormat(long number, int fraction) {
		Long longNumber = new Long(number);
		double doubleNumber = longNumber.doubleValue();
		return getDecimalFormat(doubleNumber, fraction);
	}

	public static int strToInt(String num) {
		int intNumber = 0;
		if (num == null) {
			num = "0";
		}
		try {
			intNumber = Integer.parseInt(num);
		} catch (NumberFormatException e) {
		}

		return intNumber;
	}

	public static int strToInt(String num, int def) {

		int intNumber = 0;

		if (num == null) {
			num = def + "";
		}

		try {
			intNumber = Integer.parseInt(num);
		} catch (NumberFormatException e) {
		}

		return intNumber;
	}

	public static double strToDouble(String num) {
		double doubleNumber = 0;

		try {
			doubleNumber = Double.parseDouble(num);
		} catch (NumberFormatException e) {
			throw e;
		}

		return doubleNumber;
	}

	public static float strToFloat(String num) {
		float floatNumber = 0;

		try {
			floatNumber = Float.parseFloat(num);
		} catch (NumberFormatException e) {
			throw e;
		}

		return floatNumber;
	}

	public static void sortLongs(
		int[] indexes,
		long[] lngValues,
		int lo0,
		int hi0) {
		long mid;

		Vector vlos = new Vector();
		vlos.addElement(new Integer(lo0));
		Vector vhis = new Vector();
		vhis.addElement(new Integer(hi0));

		for (int ic = 0; ic < vlos.size(); ic++) {
			lo0 = ((Integer) vlos.elementAt(ic)).intValue();
			int lo = lo0;
			hi0 = ((Integer) vhis.elementAt(ic)).intValue();
			int hi = hi0;
			if (hi0 > lo0) {

				mid = lngValues[indexes[(lo0 + hi0) / 2]];

				while (lo <= hi) {

					while ((lo < hi0) && (lngValues[indexes[lo]] < mid)) {
						++lo;
					}

					while ((hi > lo0) && (lngValues[indexes[hi]] > mid)) {
						--hi;
					}

					if (lo <= hi) {
						int swap;
						swap = indexes[lo];
						indexes[lo] = indexes[hi];
						indexes[hi] = swap;
						++lo;
						--hi;
					}
				}

				if (lo0 < hi) {
					vlos.addElement(new Integer(lo0));
					vhis.addElement(new Integer(hi));
				}

				if (lo < hi0) {
					vlos.addElement(new Integer(lo));
					vhis.addElement(new Integer(hi0));
				}
			}
		}
	}

	public static String num2zero(int num) {
		String formatStr = "";
		formatStr = ((num < 10) ? "0" + num : "" + num);

		return formatStr;
	}

	public static String cardString(int param, int size) {

		String ret = Integer.toString(param);
		int length = ret.length();

		for (int i = 0; i < size - length; i++) {
			ret = "0" + ret;
		}

		return ret;
	}

	public static String getCutString(String value, int position) {
		String retValue = "";
		try {
			// Log.info(value.length()-(position), this);
			retValue = value.substring(0, value.length() - (position));
		} catch (Exception ex) {
			retValue = "error";
		}

		return retValue;

	}

	public static double cut(double dAmount, int i) {
		long rest = 1;
		for (int x = 0; x < Math.abs(i); x++) {
			rest *= 10;

		}
		if (i > 0) {
			return (double) ((long) (dAmount / rest)) * rest;
		} else {
			return (double) ((long) (dAmount * rest)) / rest;
		}
	}

	/**
	 * �ڸ��� �ݿø�
	 *				usage :  StringUtil.round(20.20, 2);
	 *  			��)
	 *  				StringUtil.round(44.44,-2) =&gt; 44.44
	 *  				StringUtil.round(55.55,-2) =&gt; 55.55
	 *  				StringUtil.round(44.44,-1) =&gt; 44.4
	 *  				StringUtil.round(55.55,-1) =&gt; 55.6
	 *  				StringUtil.round(44.44, 0) =&gt; 44.0
	 *  				StringUtil.round(55.55, 0) =&gt; 56.0
	 *  				StringUtil.round(44.44, 1) =&gt; 40.0
	 *  				StringUtil.round(55.55, 1) =&gt; 60.0
	 *  				StringUtil.round(44.44, 2) =&gt; 0.0
	 *  				StringUtil.round(55.55, 2) =&gt; 100.0
	 * @param dAmount �����Ϸ��� double ���� ���� 	
	 * @return 
	 * @exception 
	 */
	public static double round(double dAmount, int i) {
		long rest = 1;
		for (int x = 0; x < Math.abs(i); x++) {
			rest *= 10;

		}
		if (i > 0) {
			return Math.round(dAmount / rest) * rest;
		} else {
			return (double) Math.round(dAmount * rest) / rest;
		}
	}

	public static String printThread(int ndepth) {
		String retvThread = "";
		for (int i = 0; i < ndepth; i++) {
			retvThread = retvThread + "&nbsp;&nbsp;&nbsp;&nbsp;";
		}
		return retvThread;
	}

	public static String extacFileExt(String cVar) {
		int pathLen = cVar.indexOf(".");
		String strExt = cVar.substring(pathLen + 1);
		return strExt;
	}

	public static String cutStringByLimit(String str, int length) {

		int initLength = str.length();
		int cnt = 0;

		if (initLength <= length) {
			return str;
		}

		if (initLength > length) {
			for (int i = length; i >= 0; i--) {
				if ((int) str.charAt(i) < 127) {
					break;
				} else {
					cnt = cnt + 1;
				}
			}

			if (cnt == 0) {
				cnt = 1;
			}

			cnt = cnt % 2;

			if (cnt == 0) {
				length = length - 1;
			}
		}

		return str.substring(0, length);
	}

	public static String cutStringByLimitWithSign(String str, int length) {

		int initLength = str.length();
		int cnt = 0;

		if (initLength <= length) {
			return str;
		}

		if (initLength > length) {
			for (int i = length; i >= 0; i--) {
				if ((int) str.charAt(i) < 127) {
					break;
				} else {
					cnt = cnt + 1;
				}
			}

			if (cnt == 0) {
				cnt = 1;
			}

			cnt = cnt % 2;

			if (cnt == 0) {
				length = length - 1;
			}
		}

		return str.substring(0, length) + "...";
	}

	public static String cutStringForBalloon(
		String orgStr,
		int gubun,
		int cutlength) {
		int strLength = orgStr.length();
		if (strLength < cutlength) {
			return orgStr;
		}
		String cutStr = "";
		String strTemp = removeCharacter(orgStr, "\"");
		if (gubun == 1) {
			cutStr = cutStringByLimitWithSign(strTemp, cutlength);
		} else {
			cutStr = cutStringByLimit(strTemp, cutlength);
		}
		return "<font title=\"" + strTemp + "\">" + cutStr + "</font>";
	}

	public static void fixBlank(Object o) {
		if (o == null) {
			return;
		}

		Class c = o.getClass();
		if (c.isPrimitive()) {
			return;
		}

		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {

			try {

				Object f = fields[i].get(o);
				Class fc = fields[i].getType();

				if (fc.getName().equals("java.lang.String")) {
					int mod = fields[i].getModifiers();
					if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
						continue;
					}

					if (f == null || ((String) f).trim().equals("")) {
						fields[i].set(o, "&nbsp;");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.info(e.getMessage(), null);
			}
		}
	}

	public static String remove_Comma(String str) {
		String rs_str = "";
		for (int i = 0; i < str.length(); i++) {
			if (!str.substring(i).startsWith(",")) {
				rs_str += str.substring(i, i + 1);
			}
		}
		return rs_str;
	}

	public static String nvlHtml(String str) {
		String result = "";
		result = encodeHTMLSpecialChar(NVL(str), 1);
		return result;
	}

	public static String nvlHtml(String str, String alter_str) {
		String result = "";
		result = encodeHTMLSpecialChar(NVL(str, alter_str), 1);
		return result;
	}

	public static String nvlHtml(
		String str,
		String alter_str,
		int actionType) {
		String result = "";
		result = encodeHTMLSpecialChar(NVL(str, alter_str), actionType);
		return result;
	}

	public static String getPriceForm(int Ppprc) throws Exception {
		String strPpprc = Integer.toString(Ppprc);
		String strNewPrice = null;
		String strFormatPrice = "";

		int intPointIndex = strPpprc.indexOf('.');
		if (intPointIndex == -1) {
			strNewPrice = strPpprc;
		} else {
			strNewPrice = strPpprc.substring(0, intPointIndex);
		}

		int intFirstIndex = 0;
		int intNextIndex = 0;
		int intLength = strNewPrice.length();

		intNextIndex = intLength % 3;
		if (intNextIndex == 0) {
			intNextIndex += 3;
		}

		for (;
			intNextIndex < intLength;
			intFirstIndex = intNextIndex, intNextIndex += 3) {
			strFormatPrice
				+= strNewPrice.substring(intFirstIndex, intNextIndex);
			strFormatPrice += ",";
		}
		strFormatPrice += strNewPrice.substring(intFirstIndex, intNextIndex);
		return strFormatPrice;
	}

	public static String getPriceForm(String strPpprc) throws Exception {

		String strNewPrice = null;
		String strFormatPrice = "";

		int intPointIndex = strPpprc.indexOf('.');
		if (intPointIndex == -1) {
			strNewPrice = strPpprc;
		} else {
			strNewPrice = strPpprc.substring(0, intPointIndex);
		}

		int intFirstIndex = 0;
		int intNextIndex = 0;
		int intLength = strNewPrice.length();

		intNextIndex = intLength % 3;
		if (intNextIndex == 0) {
			intNextIndex += 3;
		}

		for (;
			intNextIndex < intLength;
			intFirstIndex = intNextIndex, intNextIndex += 3) {
			strFormatPrice
				+= strNewPrice.substring(intFirstIndex, intNextIndex);
			strFormatPrice += ",";
		}
		strFormatPrice += strNewPrice.substring(intFirstIndex, intNextIndex);
		return strFormatPrice;
	}

	public static String[] emailDoubleCheck(String[] emailArr) {
		String[] targetArr = null;
		Vector lists = new Vector();
		String tempStr = "";
		String tempStr2 = "";
		int listSize = 0;
		boolean lfound = false;
		for (int i = 0; i < emailArr.length; i++) {
			tempStr2 = emailArr[i];
			if (lists.size() == 0) {
				lists.add(tempStr2);
			} else {
				listSize = lists.size();
				lfound = false;
				for (int k = 0; k < listSize; k++) {
					tempStr = (String) lists.elementAt(k); //�񱳴���� ���Ѵ�.
					//Log.info("tempStr2/tempStr :: "+tempStr2+"/"+tempStr, this);
					if (tempStr2.equals(tempStr)) {
						lfound = true;
					}
				}
				if (!lfound) {
					lists.add(tempStr2);
				}
			}
			//Log.info("[���� email] => "+tempStr2, this);
		}
		//Log.info("size => "+lists.size(), this);
		targetArr = new String[lists.size()];
		for (int j = 0; j < lists.size(); j++) {
			targetArr[j] = (String) lists.elementAt(j);
			tempStr2 = targetArr[j];
			//Log.info("[���� email] => "+tempStr2, this);
		}
		return targetArr;
	}

	public static String getChecked(String s, String word) {
		return s.equals(word) ? "checked" : "";
	}

	public static String getSelected(String s, String word) {
		return s.equals(word) ? "selected" : "";
	}

	public static String getChecked(int s, int w) {
		return (s == w) ? "checked" : "";
	}

	public static String getSelected(int s, int w) {
		return (s == w) ? "selected" : "";
	}

	/**
	 * ���ڿ��� �ѱ� ����ũ�� �������� ���������� ���ڿ� ���Ŀ� ���Ӹ� ǥ�ø� ��Ÿ���� �Ѵ�.
	 * �۲��� ����� ������ ����ȭ.
	 * @param 
	 * @return 
	 * @exception
	 */
	public static String strCutting(String strInput, float strLength) {

		String regex = "[a-z]";
		String regex2 = "[A-Z]";
		String regex3 = "[0-9]";

		float stringSize = strLength; //�ѱ� ���� ũ�� �������� 6��
		float addStrSize = 0;
		String cutString = "";

		Matcher m;
		Matcher m2;
		Matcher m3;
		for (int i = 0; i < strInput.length(); i++) {
			m = Pattern.compile(regex).matcher(String.valueOf(strInput.charAt(i)));
			m2 = Pattern.compile(regex2).matcher(String.valueOf(strInput));
			m3 =	Pattern.compile(regex3).matcher(String.valueOf(strInput.charAt(i)));
			if (m.matches()) { //���� (�ҹ���) 51%
				addStrSize += 0.51;
				cutString += strInput.charAt(i);
			} else if (
				strInput.charAt(i) >= 0xAC00
					&& strInput.charAt(i) <= 0xD743) { //�ѱ� 100%
				addStrSize += 1;
				cutString += strInput.charAt(i);
			} else if (m2.matches()) { //����(�빮��) 68%
				addStrSize += 0.68;
				cutString += strInput.charAt(i);
			} else if (m3.matches()) { //���� 51%
				addStrSize += 0.51;
				cutString += strInput.charAt(i);
			} else { //Ư����ȣ 63%
				addStrSize += 0.63;
				cutString += strInput.charAt(i);
			}
			if (stringSize < addStrSize) {
				cutString += "...";
				break;
			}
		}

		return replaceToHtml(cutString);
	}

	/**
	 * ��ҹ��ڸ� ������� �ʰ� str ���ڿ��� ���Ե� keyword �� ã�Ƽ� 
	 * ������ ���ڿ� ������ ��Ʈ�±׸� ������ ���ڿ� ��ȯ Method markKeyword.
	 * 
	 * @param 
	 * @return 
	 * @exception
	 */
	public static String markKeyword(String str, String keyword) {
		keyword =
			replace(
				replace(replace(keyword, "[", "\\["), ")", "\\)"),
				"(",
				"\\(");

		Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		int start = 0;
		int lastEnd = 0;
		StringBuffer sbuf = new StringBuffer();
		while (m.find()) {
			start = m.start();
			sbuf.append(str.substring(lastEnd, start)).append(
				"<font color='#F7919C'>" + m.group() + "</font>");
			lastEnd = m.end();
		}
		return sbuf.append(str.substring(lastEnd)).toString();
	}

	public static String getTitleLimit(
		String title,
		int maxNum,
		int re_level) {
		int blankLen = 0;
		if (re_level != 0) {
			blankLen = (re_level + 1) * 2;
		}
		int tLen = title.length();
		int count = 0;
		char c;
		int s = 0;
		for (s = 0; s < tLen; s++) {
			c = title.charAt(s);
			if ((int) (count) > (int) (maxNum - blankLen))
				break;
			if (c > 127)
				count += 2;
			else
				count++;
		}
		return (tLen > s) ? title.substring(0, s) + "..." : title;
	}

	public static String getTitleLimit(String title, int maxNum) {
		int tLen = title.length();
		int count = 0;
		char c;
		int s = 0;
		for (s = 0; s < tLen; s++) {
			c = title.charAt(s);
			if (count > maxNum)
				break;
			if (c > 127)
				count += 2;
			else
				count++;
		}
		return (tLen > s) ? title.substring(0, s) + "..." : title;
	}

	public static int getTotal(int rc, int ps) {
		int t = (int) ((rc - 1) / ps) + 1;
		if (t == 0)
			t = 1;
		return t;
	}

	public static String printThread(String string) {
		return null;
	}

	public static String printDepthSpace(String numStr) {
		String returnStr = "";
		int num = StringUtil.strToInt(numStr);
		for (int i = 0; i < num; i++) {
			if (i > 0) {
				returnStr = returnStr + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
		return returnStr + "<img src='/images/icon_row_add.gif'>&nbsp;";
	}

	public static String printDepthSpace2(String numStr) {
		String returnStr = "";
		int num = StringUtil.strToInt(numStr);
		for (int i = 0; i < num; i++) {
			if (i > 0) {
				returnStr = returnStr + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
		return returnStr;
	}

	public static String strToLenSpace(String num, int len) {
		String cstr = "";
		if (num.length() < len) {
			for (int i = 0; i < (len - num.length()); i++) {
				cstr = cstr + "0";
			}
			cstr = cstr + num;
		} else {
			cstr = num;
		}
		return cstr;
	}

	public static String strToLenNBSP(String num, int len) {
		String cstr = "";
		if (num.length() < len) {
			for (int i = 0; i < (len - num.length()); i++) {
				cstr = cstr + "&nbsp;";
			}
			cstr = num + cstr;
		} else {
			cstr = num;
		}
		return cstr;
	}

	public static String strToLenLine(String num) {
		String cstr = "";
		for (int i = 0; i < num.length(); i++) {
			cstr = cstr + "=";
		}
		return cstr;
	}

	/**
	 * �ڸ��� �Լ� 
	 * 					String str = "�̰��� Hangul(�ѱ�)�Դϴ�.";
	 * 					Log.info(str);
	 * 					Log.info(" 2: " + truncateNicely(str,  2, "*"));
	 * 					Log.info(" 3: " + truncateNicely(str,  3, "*"));
	 * 					Log.info(" 9: " + truncateNicely(str,  9, "*"));
	 * 					Log.info("10: " + truncateNicely(str, 10, "*"));
	 * 					Log.info("11: " + truncateNicely(str, 11, "*"));
	 * 					Log.info("12: " + truncateNicely(str, 12, "*"));
	 * 					Log.info("13: " + truncateNicely(str, 13, "*"));
	 * 					Log.info("14: " + truncateNicely(str, 14, "*"));
	 * 					Log.info("15: " + truncateNicely(str, 15, "*"));
	 * 					Log.info("16: " + truncateNicely(str, 16, "*"));
	 * 
	 * 					Log.info(" 2: " + truncateNicely(str,  2, "..."));
	 * 					Log.info(" 3: " + truncateNicely(str,  3, "..."));
	 * 					Log.info(" 9: " + truncateNicely(str,  9, "..."));
	 * 					Log.info("10: " + truncateNicely(str, 10, "..."));
	 * 					Log.info("11: " + truncateNicely(str, 11, "..."));
	 * 					Log.info("12: " + truncateNicely(str, 12, "..."));
	 * 					Log.info("13: " + truncateNicely(str, 13, "..."));
	 * 					Log.info("14: " + truncateNicely(str, 14, "..."));
	 * 					Log.info("15: " + truncateNicely(str, 15, "..."));
	 * 					Log.info("16: " + truncateNicely(str, 16, "..."));
	 * 
	 * 					Log.info(" 2: " + truncateNicely(str,  2, ""));
	 * 					Log.info(" 3: " + truncateNicely(str,  3, ""));
	 * 					Log.info(" 9: " + truncateNicely(str,  9, ""));
	 * 					Log.info("10: " + truncateNicely(str, 10, ""));
	 * 					Log.info("11: " + truncateNicely(str, 11, ""));
	 * 					Log.info("12: " + truncateNicely(str, 12, ""));
	 * 					Log.info("13: " + truncateNicely(str, 13, ""));
	 * 					Log.info("14: " + truncateNicely(str, 14, ""));
	 * 					Log.info("15: " + truncateNicely(str, 15, ""));
	 *					Log.info("16: " + truncateNicely(str, 16, ""));
	 *  
	 * 					Log.info("16: " + truncateNicely(str, 16, null));
	 *
	 *  				String data = str;
	 *  				while (realLength(data) > 5) {
	 *    				String tmp = truncateNicely(data, 5, "");
	 *    				Log.info(tmp);
	 *    				data = data.substring(tmp.length());
	 *  				}
	 *  				if (data.length() > 0)
	 *    				Log.info(data);
	 * @param 
	 * @return 
	 * @exception
	 */
	public static String truncateNicely(String s, int len, String tail) {
		if (s == null)
			return null;

		int srcLen = realLength(s);
		if (srcLen < len)
			return s;

		String tmpTail = tail;
		if (tail == null)
			tmpTail = "";

		int tailLen = realLength(tmpTail);
		if (tailLen > len)
			return "";

		char a;
		int i = 0;
		int realLen = 0;
		for (i = 0; i < len - tailLen && realLen < len - tailLen; i++) {
			a = s.charAt(i);
			if ((a & 0xFF00) == 0)
				realLen += 1;
			else
				realLen += 2;
		}

		while (realLength(s.substring(0, i)) > len - tailLen) {
			i--;
		}
		return s.substring(0, i) + tmpTail;
	}

	public static int realLength(String s) {
		return s.getBytes().length;
	}

	/**
	 * �־��� ������ �־��� ���̷� ����� �迭�� �������ش�.
	 * 
	 * @param 
	 * @return 
	 * @exception
	 */
	public static String[] StringParseToArray(String article, int parseLang) {
		String[] returnArray = new String[StringParseLang(article, parseLang)];

		for (int i = 0; i < StringParseLang(article, parseLang); i++) {
			if (StringParseLang(article, parseLang) == 1) {
				returnArray[i] = new String(article);
			} else if (StringParseLang(article, parseLang) == i + 1) {
				returnArray[i] = new String(article.substring(i * parseLang));
			} else {
				returnArray[i] =
					new String(
						article.substring(i * parseLang, (i + 1) * parseLang));
			}

		}
		return returnArray;
	}

	public static int StringParseLang(String article, int parseLang) {
		if (article.length() % parseLang != 0) {
			return article.length() / parseLang + 1;
		} else {
			return article.length() / parseLang;
		}
	}

	public static HashMap getRequest(HttpServletRequest p_req) {
		HashMap map = new HashMap();

		Enumeration enum_req = p_req.getParameterNames();

		while (enum_req.hasMoreElements()) {
			String key = (String) enum_req.nextElement();

			String[] value = p_req.getParameterValues(key);
			if (value.length == 1) {
				map.put(key, p_req.getParameter(key));
			} else if (value.length > 1) {
				Vector v = new Vector();

				for (int i = 0; i < value.length; i++) {
					v.add(v.size(), value[i]);
				}
				map.put(key, v);
			}
		}
		return map;
	}

	public static void toString(HashMap map) {
		Set key = map.keySet();
		Object[] keys = key.toArray();
		for (int i = 0; i < keys.length; i++) {
			Log.info("KEY = [" + keys[i] + "]\nVALUE = " + map.get(keys[i]) + "\n", null);
			if (map.get(keys[i]) instanceof Vector) {
				//Log.info(" Keys : -> " + keys[i] + " Vector");
			}
		}
	}

	public static Vector getResult(ResultSet p_rset) throws Exception {

		Vector voList = new Vector();

		try {
			ResultSetMetaData rsetmeta = p_rset.getMetaData();
			int colcnt = rsetmeta.getColumnCount(); // �÷� ī��Ʈ..  
			String colName = null; // �÷� ��
			//String colType = null;  // �÷� Ÿ��..

			while (p_rset.next()) {
				HashMap vo = new HashMap();
				for (int j = 1; j <= colcnt; j++) {
					colName = rsetmeta.getColumnName(j); // �÷���...
					//colType = rsetmeta.getColumnTypeName(j); // �÷� Ÿ�� DB Type
					if (p_rset.getObject(j) != null) {
						vo.put(colName, p_rset.getString(j));
					} else {
						vo.put(colName, "");
					}
				}
				voList.add(voList.size(), vo);
			}
		} catch (SQLException sqle) {
			Log.info(sqle.getMessage(), null);
		} catch (Exception e) {
			Log.info(e.getMessage(), null);
		}
		return voList;
	}

	public static String getDBInsertString(String _s) {
		StringBuffer s_buf = new StringBuffer();
		for (int i = 0; i < _s.length(); i++) {
			if ((int) _s.charAt(i) == 39) //single quote
				s_buf = s_buf.append("''");
			else
				s_buf = s_buf.append(_s.charAt(i));
		}
		return s_buf.toString().trim();
	}

	/**
	 * �������� <BR>�� �ٲپ� �ִ� �Լ�
	 * 
	 * @param 
	 * @return 
	 * @exception 
	 */
	public static String getText2HTML(String s) {
		if (s != null) {
			StringBuffer buf = new StringBuffer();
			int i = 0;
			while (i < s.length()) {
				char c = s.charAt(i);
				if (c == 10 || c == 13) {
					buf.append("<BR>");
					if (i + 1 < s.length()) {
						if (c == 10 || c == 13)
							i++;
					}
				} else if (c == ' ')
					buf.append("&nbsp;");
				else
					buf.append(c);
				i++;
			}
			return buf.toString();
		} else {
			return "";
		}
	}

	/**
	 * 2-bytes �ڵ�Ÿ���� ���ڿ��� Ư�� ũ��� �߶󳽴�.
	 * 
	 * @param str : ���� String.
	 * 				 size : �ڸ����� �ϴ� ����
	 * @return ���ϴ� ���̷� �߸� String.
	 */
	public static String cutString(String str, int size) {
		if (size < 0)
			return "...";

		byte[] bytes = str.getBytes();
		if (bytes.length <= size)
			return str;

		int cut_size = size;
		int first_ascii_idx = size;

		if (bytes[size - 1] < 0) { // non ascii
			first_ascii_idx = 0;
			for (int i = size - 2; i >= 0; i--) {
				if (bytes[i] > 0) {
					first_ascii_idx = i + 1;
					break;
				}
			}
			int j = first_ascii_idx;
			while (j <= size) {
				cut_size = j;
				j += 2;
			}
		}
		try {
			String tempStr = new String(bytes, 0, cut_size, "KSC5601");
			return tempStr + "...";
		} catch (java.io.UnsupportedEncodingException uee) {
			return str + "...";
		}
	}
	
    /**
	 * @title	 	���� ������ �ݾ� �������� ��ȯ�Ѵ�
	 * 
	 * @param	�ٲ� �ѱ� String          
	 * @return 	�ٲ� �ѱ� String       
	 */
    public static String convNumToWon(String str) {
         if ( str.indexOf(',') != -1 ) return str;
         String str1 = new String("");
         String str2 = new String("");
         double m = 0.0;
         
         String output = new String(""), els = new String("");
         int length = 0;
         int pos = 0, i = 0, mod = 0, mod1 = 0, ismin = 0, point = -1;
         
         if (str == null) return "";
         try {
             str1 = String.valueOf(str.trim());
         } catch(NumberFormatException ne) {
             return "";
         }
         
         point =  str1.indexOf('.');
         if (point >= 0) {
             els = str1.substring(point, str1.length());
             str1 = str1.substring(0, point);
         }
         
         if (str1.substring(0,1).equals("-")) {
             ismin=1;
             str1 = str1.substring(1,str1.length());
         }
         
         ismin=0;

        if (str1 == null) return "";

        if (str1.length() < 1) return "";

        length = str1.length();
        
        str2 = "";
        mod = length%3;
        mod1 = length/3;

        if (mod==0) {
             mod1-=1;
             mod=3;
        }
        
        str2 = str2+str1.substring(0,mod);
        if (mod==3) {
            for(i=1; i<=mod1; i++) {
                 str2 += ",";
                 str2 += str1.substring(mod+(3*(i-1)),((i+1)*3));
            }
        } else if (mod==2) {
             for(i=1; i<=mod1; i++) {
            	 str2 += ",";
            	 str2 += str1.substring(mod+(3*(i-1)),(i*3)+2);
             }
        } else {
             for(i=1; i<=mod1; i++) {
            	 str2 += ",";
            	 str2 += str1.substring(mod+(3*(i-1)),(i*3)+1);
             }
        }
        
        if ( els.length() != 0 ) {
             String  remain = els.substring(1, els.length());

             if ( Integer.parseInt(remain) == 0 ) {
            	 if(ismin==1) return "-" + str2;
            	 else return str2;
             } else {
            	 if(ismin==1) return "-" + str2 + els;
            	 else return str2 + els;
             }
        } else {
             if(ismin==1) return "-" + str2 + els;
             else return str2 + els;
        }
    }
    
    /**
	 * @title	 	�ݾ� ������ ���ڷ� ��ȯ�Ѵ�
	 * 
	 * @param	�ٲ� �ݾ� String
	 * @return 	�ٲ� ���� String
	 */
    public static String convWonToNum(String str) {
    	if (str == null) return "";
        String strWon = str;
        String strWonToNum = "";
        StringTokenizer insStrToken = new StringTokenizer(strWon, ",");
        while (insStrToken.hasMoreTokens()) {
        	strWonToNum = strWonToNum + insStrToken.nextToken();
        }
        return strWonToNum;
    }

	public static String getExtension(String _str) {
		String ext = null;
		if (_str != null && _str.lastIndexOf(".") > 0 && _str.length() > 1) {
			int dot = _str.lastIndexOf(".");
			ext = _str.substring(dot + 1, _str.length());
		}
		return ext;
	}

	/**
	 * property��ü�� �ָ�, parameter ��Ʈ�� ���·� ��ȯ���ش�.
	 * 
	 * @param sPage : �� ������.
	 * 				 prop : �� �������� �����Ϸ��� Name�� Value pairs.
	 * @return Parameter�� �����ϴ� ������ ��Ʈ��
	 */
	public static String getProp2Params(String sPage, Properties prop) {
		StringBuffer buf = new StringBuffer(sPage);
		buf.append("?");

		String sKey = null;
		for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
			sKey = e.nextElement().toString();
			buf.append(sKey);
			buf.append("=");
			buf.append(prop.getProperty(sKey));

			if (e.hasMoreElements())
				buf.append("&");

		}
		return buf.toString();
	}

	public static String getFldName(String columnName) {
		String result = "";
		columnName = columnName.toLowerCase();
		//Log.info("columnName :: "+columnName);
		StringTokenizer dbColumnName = new StringTokenizer(columnName, "_");
		int cnt = dbColumnName.countTokens();
		//Log.info("dbColumnName :: "+dbColumnName, this);
		//Log.info("cnt :: "+cnt, this);
		for (int i = 0; i < cnt; i++)
			if (i == 0) {
				String str = dbColumnName.nextToken().toLowerCase();
				//Log.info("i==0 :: "+str, this);
				result += str;
			} else {
				String str = dbColumnName.nextToken();
				//Log.info("i!=0 :: "+str, this);          
				result += str.substring(0, 1).toUpperCase()
					+ str.substring(1, str.length());
			}
		//Log.info("e :: "+result, this);          
		return result;
	}

	public static String makeEucKr(String s_rtn) {
		String temp = "";
		try {
			if (s_rtn == null)
				s_rtn = null;
			else {
				Log.info("org string :: " + s_rtn, null);

				temp = new String(s_rtn.getBytes("EUC-KR"), "ISO-8859-1");
				Log.info("test1 :: EUC-KR -> ISO-8859-1 :: " + temp, null);

				temp = new String(s_rtn.getBytes("ISO-8859-1"), "EUC-KR");
				Log.info("test1 :: ISO-8859-1 -> EUC-KR :: " + temp, null);

				temp = new String(s_rtn.getBytes("EUC-KR"), "8859_1");
				Log.info("test1 :: EUC-KR -> 8859_1 :: " + temp, null);

				temp = new String(s_rtn.getBytes("8859_1"), "EUC-KR");
				Log.info("test1 :: 8859_1 -> EUC-KR :: " + temp, null);

				temp = new String(s_rtn.getBytes("KSC5601"), "8859_1");
				Log.info("test1 :: KSC5601 -> 8859_1 :: " + temp, null);

				temp = new String(s_rtn.getBytes("8859_1"), "KSC5601");
				Log.info("test1 :: 8859_1 -> KSC5601 :: " + temp, null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("SemUtil Exception fail...makeEucKr()", null);
		}
		return s_rtn;
	}
	
	/**
	 * String�� ������ ���̸�ŭ�� ����� �� �ֵ��� �ϸ�, ���� 
	 * �Ϻκи��� ��µǴ� ��쿡�� "..."�� ���� �߰��Ѵ�.
	 * strSource�� �յڿ� ���鹮�ڰ� ���ԵǾ� �ִ� ��� trim��
	 * �����Ѵ�.
	 * 
	 *  cutByte���� ���ڿ��� byte ���̸� �ǹ��Ѵ�.  �ѱ۰� ����
	 *  2byte character�� 2�� ����ϸ�, ���� �� �ݰ� ��ȣ�� 1��
	 * ����Ѵ�.
	 * 
	 * @param strSource : ��ȯ�ϰ��� �ϴ� ���� ���ڿ�. null�� 
	 *                           	 ��� ���鹮�ڿ��� ��ȯ�ȴ�.
	 * 			  					 cutByte ��ȯ�� �� ����(postfix ���ڿ� "..."�� ����
	 *                         		 ����).  �ݵ�� 4 �̻��� ���ڸ� �Է��ؾ� 
	 *                         		 �Ѵ�.  �׷��� ���� ��� ���� ���ڿ��� 
	 *                         		 �״�� ��ȯ�Ѵ�.
	 * @return ��ȯ�� ��� ���ڿ��� return �Ѵ�. ��, strSource�� 
	 *              null�� ��� ���鹮�ڿ��� ��ȯ�Ǹ� cutByte�� 4 
	 *              �̸��� ���ڰ� ���� ��� ���� ���ڿ��� �״�� 
	 *              ��ȯ�Ѵ�.
	 */
	public static String cutBytes(String strSource, int cutByte) {
		return cutBytes(strSource, cutByte, true, "...");
	}

	/**
	 * String�� ������ ���̸�ŭ�� ����� �� �ֵ��� �ϸ�, ���� �Ϻκи��� ��µǴ� ��쿡�� "..."�� ���� �߰��Ѵ�.
	 * ������ postfix ���ڿ��� ���� �߰��Ѵ�.
	 *  cutByte���� ���ڿ��� byte ���̸� �ǹ��Ѵ�.  �ѱ۰� ���� 2byte character�� 2�� ����ϸ�, ���� �� �ݰ� ��ȣ�� 1�� ����Ѵ�.
	 * 
	 * @param strSource ��ȯ�ϰ��� �ϴ� ���� ���ڿ�. null�� ��� ���鹮�ڿ��� ��ȯ�ȴ�.
	 * 				 cutByte ��ȯ�� �� ����(postfix ���ڿ��� ���� ����).  �ݵ�� 4 �̻��� ���ڸ� �Է��ؾ� �Ѵ�.  �׷��� ���� ��� ���� ���ڿ��� �״�� ��ȯ�Ѵ�.
	 * 				 bTrim ���� ���ڿ��� �յڿ� ���鹮�ڰ� ������� trim�� ���������� �����Ѵ�. 
	 * @return ��ȯ�� ��� ���ڿ��� return �Ѵ�. ��, strSource�� null�� ��� ���鹮�ڿ��� ��ȯ�Ǹ� cutByte�� 4 �̸��� ���ڰ� ���� ��� ���� ���ڿ��� �״�� ��ȯ�Ѵ�.
	 */
	public static String cutBytes(
		String strSource,
		int cutByte,
		boolean bTrim) {
		return cutBytes(strSource, cutByte, bTrim, "...");
	}

	/**
	 * String�� ������ ���̸�ŭ�� ����� �� �ֵ��� �ϸ�, ���� �Ϻκи��� ��µǴ� ��쿡��
	 * ������ postfix ���ڿ��� ���� �߰��Ѵ�.
	 * cutByte���� ���ڿ��� byte ���̸� �ǹ��Ѵ�.  �ѱ۰� ���� 2byte character�� 2�� ����ϸ�, ���� �� �ݰ� ��ȣ�� 1�� ����Ѵ�.
	 * 
	 * @param strSource : ��ȯ�ϰ��� �ϴ� ���� ���ڿ�. null�� ��� ���鹮�ڿ��� ��ȯ�ȴ�.
	 * 				 cutByte : ��ȯ�� �� ����(postfix ���ڿ��� ���� ����).  �ݵ�� strPostfix���ڿ��� byteũ�� �̻��� ���ڸ� �Է��ؾ� �Ѵ�.  �׷��� ���� ��� ���� ���ڿ��� �״�� ��ȯ�Ѵ�.
	 *  			 bTrim : ���� ���ڿ��� �յڿ� ���鹮�ڰ� ������� trim�� ���������� �����Ѵ�. 
	 * 				 strPostfix : ���ڿ��� �߸���� �̸� ǥ���ϱ� ���� ���ڿ�. null�� ��� "..."�� �߰��ȴ�.
	 * @return ��ȯ�� ��� ���ڿ��� return �Ѵ�. ��, strSource�� null�� ��� ���鹮�ڿ��� ��ȯ�Ǹ� cutByte�� strPostfix���ڿ��� byteũ�� �̸��� ���ڰ� ���� ��� ���� ���ڿ��� �״�� ��ȯ�Ѵ�.
	 */
	public static String cutBytes(
		String strSource,
		int cutByte,
		boolean bTrim,
		String strPostfix) {

		if (strSource == null)
			return "";

		strPostfix = (strPostfix == null) ? "..." : strPostfix;
		int postfixSize = 0;
		for (int i = 0; i < strPostfix.length(); i++) {
			if (strPostfix.charAt(i) < 256)
				postfixSize += 1;
			else
				postfixSize += 2;
		}

		if (postfixSize > cutByte)
			return strSource;

		if (bTrim)
			strSource = strSource.trim();
		char[] charArray = strSource.toCharArray();

		int strIndex = 0;
		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character �̸�
				byteSize = 1;
			} else {
				// 2byte character �̸�
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte - postfixSize) {
				break;
			}

			byteLength = byteLength += byteSize;
		}

		if (strIndex == strSource.length())
			strPostfix = "";
		else {
			if ((byteLength + postfixSize) < cutByte)
				strPostfix = " " + strPostfix;
		}

		return strSource.substring(0, strIndex) + strPostfix;
	}

	public static String orderState(String order) {
		String orderState = "";

		if (order.length() % 2 == 0) {
			orderState = "DESC";
		} else {
			orderState = "ASC";
		}

		return orderState;
	}

	/**
	 * �������ڿ� �迭�� DB�������� IN���� ����� �� �ֵ��� ��������ǥ�� �����ְ� �޸��� ������ ��� ���ڿ��� ��ȯ�Ѵ�.
	 * 
	 * @param s[] : �������ڿ� �迭
	 * @return ġȯ�� ���ڿ� (ex: 'ȫ�浿','������' )
	 */
	public static String makeQueryInString(String[] str) {
		if (str == null)
			return "";
		String r_str = "";
		for (int i = 0; i < str.length; i++) {
			if (!str[i].equals(""))
				r_str += "'" + str[i] + "',";
		}
		if (!r_str.equals(""))
			r_str = r_str.substring(0, r_str.length() - 1);
		return r_str;
	}

	/**
	 * ���ڿ����� ���ϴ� ���� �տ� �ٸ� ���ڸ� �����Ѵ�.
	 * 
	 * @param orgStr : ���� ����
	 * 				 addStr : �߰��� ����
	 * 				 cutStr : �ش� ��ġ����
	 * @return 
	 */
	public static String strAdd(String orgStr, String addStr, String cutStr) {
		int iCutPosition = orgStr.lastIndexOf(cutStr);

		if (iCutPosition > 0) {
			orgStr =
				orgStr.substring(0, iCutPosition)
					+ addStr
					+ orgStr.substring(iCutPosition);
		}
		return orgStr;
	}

	/**
	 * Xss���� ����� �ɸ��� "script���ڿ��� ��ȿȭ ��Ű�� ���� ���ڿ� ��ü��.
	 * 
	 * @param orgStr : ���� ���ڿ�.
	 * @return 
	 */
	public static String strBlockXss(String orgStr) {
		orgStr = replace(orgStr, "javascript", "x-javascript");
		orgStr = replace(orgStr, "<script", "<x-script");
		orgStr = replace(orgStr, "</script", "</x-script");
		return orgStr;
	}

}