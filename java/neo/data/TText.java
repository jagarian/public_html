package neo.data;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 	@Class Name	: 	TText.java
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
public class TText {

	public TText() {
	}

	public static String getFormattedMoney(String pInstr) {
		String rStr = pInstr;
		try {
			Object testArgs[] = { new Long(pInstr)};
			MessageFormat form = new MessageFormat("{0,number,###,###,##0}");
			rStr = form.format(((Object) (testArgs)));
		} catch (Exception e) {
		}
		return rStr;
	}

	public static String getFormattedNumber(String pInstr, String pInformat) {
		String rStr = pInstr;
		try {
			Object testArgs[] = { new Long(pInstr)};
			MessageFormat form =
				new MessageFormat("{0,number," + pInformat + "}");
			rStr = form.format(((Object) (testArgs)));
		} catch (Exception e) {
		}
		return rStr;
	}

	public static String getFormattedText(String pInstr, String pInformat) {
		StringBuffer rStr = new StringBuffer();
		try {
			int i = 0;
			int j = 0;
			for (; i < pInformat.length(); i++)
				if (pInformat.charAt(i) == '#') {
					rStr.append(pInstr.charAt(j));
					j++;
				} else {
					rStr.append(pInformat.charAt(i));
				}
		} catch (Exception e) {
		}
		return rStr.toString().trim();
	}

	public static ArrayList getToken(String pStr, String pDelimiter) {
		ArrayList al = new ArrayList();
		if (pStr == null || pStr == "")
			return al;
		if (pDelimiter == null || pDelimiter == "")
			return al;
		int vStart = 0;
		int vEnd = pStr.length();
		boolean vFirst = false;
		if (pStr.substring(0, pDelimiter.length()).equals(pDelimiter))
			vFirst = true;
		for (int i = 0; vEnd > -1; i++) {
			if (i > 0)
				vStart = vEnd + pDelimiter.length();
			vEnd = pStr.indexOf(pDelimiter, vStart);
			if (vEnd > -1)
				if (!vFirst)
					al.add(pStr.substring(vStart, vEnd));
				else
					vFirst = false;
		}
		if (vStart + pDelimiter.length() <= pStr.length())
			al.add(pStr.substring(vStart, pStr.length()));
		return al;
	}

	public static String getToken(String pStr, String pDelimiter, int pIndex) {
		if (pStr == null || pStr == "")
			return "";
		if (pDelimiter == null || pDelimiter == "")
			return pStr;
		int vStart = 0;
		int vEnd = pStr.length();
		if (pStr.substring(0, pDelimiter.length()).equals(pDelimiter)
			&& pIndex > 0)
			pIndex++;
		for (int i = 1; i <= pIndex; i++) {
			if (i > 1)
				vStart = vEnd + pDelimiter.length();
			vEnd = pStr.indexOf(pDelimiter, vStart);
			if (vEnd < 0)
				if (pIndex > i) {
					i += pIndex;
					vEnd = vStart;
				} else {
					i += pIndex;
					vEnd = pStr.length();
				}
		}
		return pStr.substring(vStart, vEnd);
	}

	public static String getFormattedMoney(long pInstr) {
		String rStr = Long.toString(pInstr);
		try {
			Object testArgs[] = { new Long(pInstr)};
			MessageFormat form = new MessageFormat("{0,number,###,###,##0}");
			rStr = form.format(((Object) (testArgs)));
		} catch (Exception e) {
		}
		return rStr;
	}

	public static String getFormattedNumber(long pInstr, String pInformat) {
		String rStr = Long.toString(pInstr);
		try {
			Object testArgs[] = { new Long(pInstr)};
			MessageFormat form =
				new MessageFormat("{0,number," + pInformat + "}");
			rStr = form.format(((Object) (testArgs)));
		} catch (Exception e) {
		}
		return rStr;
	}

	public static String getListString(String pStr[]) {
		return getListString(pStr, false);
	}

	public static String getListString(String pStr[], boolean pEtc) {
		return getListString(pStr, pEtc, ", ");
	}

	public static String getListString(ArrayList pAl) {
		return getListString(pAl, false);
	}

	public static String getListString(ArrayList pAl, boolean pEtc) {
		return getListString(pAl, pEtc, ", ");
	}

	public static final String lpad(String value, int length) {
		return lpad(value, length, " ");
	}

	public static final String lpad(String value, int length, String padder) {
		StringBuffer temp = new StringBuffer();
		if (value.length() >= length)
			return value.substring(0, length);
		while (temp.length() + value.length() < length)
			if (temp.length() + value.length() + padder.length() <= length)
				temp.append(padder);
			else
				temp.append(
					padder.substring(
						0,
						length - (temp.length() + value.length())));
		temp.append(value);
		return temp.toString();
	}

	public static final String rpad(String value, int length) {
		return rpad(value, length, " ");
	}

	public static final String rpad(String value, int length, String padder) {
		StringBuffer temp = new StringBuffer(value);
		if (value.length() > length)
			temp.setLength(length);
		else
			while (temp.length() < length)
				if (temp.length() + padder.length() <= length)
					temp.append(padder);
				else
					temp.append(padder.substring(0, length - temp.length()));
		return temp.toString();
	}

	public static String zeroToNull(int pInt) {
		if (pInt != 0)
			return Integer.toString(pInt);
		else
			return "";
	}

	public static String zeroToNull(long pLong) {
		if (pLong != 0L)
			return Long.toString(pLong);
		else
			return "";
	}

	public static String getListString(
		String pStr[],
		boolean pEtc,
		String pDel) {
		StringBuffer rSb = new StringBuffer();
		if (pStr == null || pStr.length < 1)
			return "";
		if (pEtc) {
			rSb.append(pStr[0]);
			rSb.append("\uC678");
			rSb.append(pStr.length - 1);
		} else {
			for (int i = 0; i < pStr.length; i++) {
				rSb.append(pStr[i]);
				if (i != pStr.length - 1)
					rSb.append(pDel);
			}
		}
		return rSb.toString();
	}

	public static String getListString(
		ArrayList pAl,
		boolean pEtc,
		String pDel) {
		StringBuffer rSb = new StringBuffer();
		if (pAl == null || pAl.size() < 1)
			return "";
		if (pEtc) {
			rSb.append(pAl.get(0));
			rSb.append("\uC678");
			rSb.append(pAl.size() - 1);
		} else {
			for (int i = 0; i < pAl.size(); i++) {
				rSb.append((String) pAl.get(i));
				if (i != pAl.size() - 1)
					rSb.append(pDel);
			}
		}
		return rSb.toString();
	}
}