package neo.core;

/*
 * An example of using reflection to get information about
 * a named Java class, even if that class hasn't already
 * been loaded. This is handy for getting some information
 * about a class that you don't have any documentation for.
 *
 * In this implementation, the class you're looking at must
 * be on your CLASSPATH, and you must use the full name of
 * the class (i.e. java.lang.String instead of just String).
 */

import java.lang.reflect.*;

/**
 * 	@Class Name	: 	ReflectClass.java
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
public class ReflectClass {
	static String indent = "  ";

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("USAGE: java ReflectClass className");
			return;
		}

		try {
			// you can use a ClassLoader to get an instance of the Class,
			// or you can use the Class.forName() method
			// ClassLoader cl = new ReflectClass().getClass().getClassLoader();
			// Class cls = cl.loadClass(args[0]);
			Class cls = Class.forName(args[0]);
			printClassInfo(cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printClassInfo(Class cls) {
		System.out.println("Reflected information about " + cls.getName() + ":");
		System.out.println(cls);
		System.out.println("Extends: " + cls.getSuperclass().getName());

		Class[] inters = cls.getInterfaces();
		if (inters.length > 0) {
			System.out.print("Implements: ");
			for (int i = 0; i < inters.length; i++)
				System.out.print((i > 0 ? ", " : "") + inters[i].getName());
			System.out.println("");
		}

		System.out.println("");

		// public fields
		System.out.println("FIELDS:");
		Field[] fields = cls.getFields();
		for (int i = 0; i < fields.length; i++)
			System.out.println(indent + fields[i]);

		// constructors
		System.out.println("CONSTRUCTORS:");
		Constructor[] cons = cls.getConstructors();
		for (int i = 0; i < cons.length; i++)
			System.out.println(indent + cons[i]);

		// public methods
		System.out.println("METHODS:");
		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++)
			System.out.println(indent + methods[i]);
	}
}
