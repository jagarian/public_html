package neo.util.comm;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Enumeration;

import java.util.Vector;

/**
 * 	@Class Name	: 	NeoUtil.java
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
public class NeoUtil {

	public static Object[] clone(Object[] objects) {
		int length = objects.length;
		Class c = objects.getClass().getComponentType();
		Object array = Array.newInstance(c, length);

		for (int i = 0; i < length; i++) {
			Array.set(array, i, clone(objects[i]));
		}
		return (Object[]) array;
	}

	public static Object clone(Object object) {
		Class c = object.getClass();
		Object newObject = null;
		try {
			newObject = c.newInstance();
		} catch (Exception e) {
			return null;
		}

		Field[] field = c.getFields();
		for (int i = 0; i < field.length; i++) {
			try {
				Object f = field[i].get(object);
				field[i].set(newObject, f);
			} catch (Exception e) {
			}
		}
		return newObject;
	}

	public static Vector clone(Vector objects) {
		Vector newObjects = new Vector();
		Enumeration e = objects.elements();
		while (e.hasMoreElements()) {
			Object o = e.nextElement();
			newObjects.addElement(clone(o));
		}
		return newObjects;
	}

	public static void fixNull(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		Field[] fields = c.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object f = fields[i].get(o);
				Class fc = fields[i].getType();

				if (fc.getName().equals("java.lang.String")) {
					if (f == null)
						fields[i].set(o, "");
					else
						fields[i].set(o, f);
				}
			} catch (Exception e) {
			}
		}
	}

	public static void fixNullAll(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		if (c.isArray()) {
			int length = Array.getLength(o);
			for (int i = 0; i < length; i++) {
				Object element = Array.get(o, i);
				fixNullAll(element);
			}
		} else {
			Field[] fields = c.getFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					Object f = fields[i].get(o);
					Class fc = fields[i].getType();
					if (fc.isPrimitive())
						continue;
					if (fc.getName().equals("java.lang.String")) {
						if (f == null)
							fields[i].set(o, "");
						else
							fields[i].set(o, f);
					} else if (f != null) {
						fixNullAll(f);
					} else {
					} // Some Object, but it's null.
				} catch (Exception e) {
				}
			}
		}
	}

	public static void fixNullAndTrim(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		Field[] fields = c.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object f = fields[i].get(o);
				Class fc = fields[i].getType();
				if (fc.getName().equals("java.lang.String")) {
					if (f == null)
						fields[i].set(o, "");
					else {
						String item = trim((String) f);
						fields[i].set(o, item);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static void fixNullAndTrimAll(Object o) {
		if (o == null)
			return;

		Class c = o.getClass();
		if (c.isPrimitive())
			return;

		if (c.isArray()) {
			int length = Array.getLength(o);
			for (int i = 0; i < length; i++) {
				Object element = Array.get(o, i);
				fixNullAndTrimAll(element);
			}
		} else {
			Field[] fields = c.getFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					Object f = fields[i].get(o);
					Class fc = fields[i].getType();
					if (fc.isPrimitive())
						continue;
					if (fc.getName().equals("java.lang.String")) {
						if (f == null)
							fields[i].set(o, "");
						else {
							String item = trim((String) f);
							fields[i].set(o, item);
						}
					} else if (f != null) {
						fixNullAndTrimAll(f);
					} else {
					} // Some Object, but it's null.
				} catch (Exception e) {
				}
			}
		}
	}

	public static String getStackTrace(Throwable e) {
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.PrintWriter writer = new java.io.PrintWriter(bos);
		e.printStackTrace(writer);
		writer.flush();
		return bos.toString();
	}

	public static String[] hanConvert(String[] s) throws IOException {
		if (s == null) {
			return null;
		} else
			return s;
	}

	public static String hanConvert(String s) throws IOException {
		if (s != null)
			//return new String(s.getBytes("8859_1"), "KSC5601");
			return s;
		else
			return null;
	}

	public static String trim(String s) {
		int st = 0;
		char[] val = s.toCharArray();
		int count = val.length;
		int len = count;

		while ((st < len) && ((val[st] <= ' ') || (val[st] == '　')))
			st++;
		while ((st < len) && ((val[len - 1] <= ' ') || (val[len - 1] == '　')))
			len--;

		return ((st > 0) || (len < count)) ? s.substring(st, len) : s;
	}
}
