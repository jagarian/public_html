package neo.util.comm;

import java.io.*;
import java.lang.reflect.*;

/*
 * This is just a routine to test a few different versions
 * of a replaceSubstring function.
 *
 * I realize that it's a funny-looking way of running a test,
 * but I got tired of copying and pasting long testing routines
 * (one for each function I was going to test), and it was a real
 * pain to update all the duplicate testing blocks if I wanted to
 * change the test in any way, so I made everything nice and 
 * reusable using reflection.
 *
 * If you want to add an additional function to test, all you have
 * to do is paste it as a new method at the end of this class and
 * add the method name to the methodNameList array in the startTesting
 * method. The only caveat is that the new method has to accept
 * parameters exactly like the existing methods do, and it has to
 * return the same type of object.
 *
 * Sept 24, 2003
 * UPDATE: Jim Cakalic sent some code for a replaceSubstring5 method,
 * which provides some optimizations over replaceSubstring4. He also
 * pointed out that the initial "sanity checks" should actually have
 *   if (find == null || find.length() == 0)
 * instead of
 *   if (find == null || find == "")
 * Doh! Thanks Jim.
 *
 * March 14, 2004
 * UPDATE: John Marshall (http://swapcode.com) pointed out that calling 
 * the ReplaceSubstring methods with "" as both the original string and 
 * the find string would return "", when it should really return the 
 * value of whatever you're using as the replace value. His modifications 
 * are included in the ReplaceSubstring6 method.
 * 
 * Julian Robichaux ( http://www.nsftools.com )
 * March 14, 2004
 */
/**
 * 	@Class Name	: 	ReplaceSubstringTest.java
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
public class ReplaceSubstringTest {
	PrintStream out = null;
	
	public static void main (String[] args) {
		ReplaceSubstringTest rst = new ReplaceSubstringTest();
		rst.startTesting();
	}
	
	
	public ReplaceSubstringTest () {
		this(System.out);
	}
	
	
	public ReplaceSubstringTest (PrintStream out) {
		this.out = out;
	}
	
	
	public void startTesting () {
		try {
			int i;
			int iterations = 50;
			String [] methodNameList = { "replaceSubstring1", 
										"replaceSubstring2", 
										"replaceSubstring3", 
										"replaceSubstring4", 
										"replaceSubstring5", 
										"replaceSubstring6" };
			Class[] paramTypes = {String.class, String.class, String.class};
			Method[] testMethodList = new Method[methodNameList.length];
			ReplaceSubstringTest thisClass = new ReplaceSubstringTest();
			
			// based on our methodNameList, create an array of Method objects
			// (this is a little more efficient than creating a Method every
			// time you want to test a function)
			for (i = 0; i < testMethodList.length; i++) {
				testMethodList[i] = thisClass.getMethod(thisClass.getClass(), methodNameList[i], paramTypes);
			}
			
			// first we'll test each function for correctness (the validateMethod
			// routine), and if a function passes that test then we'll time how
			// long it takes to run the timeMethod routine. All information is
			// sent to the "out" PrintStream defined in the constructor
			for (i = 0; i < testMethodList.length; i++) {
				if (thisClass.validateMethod(thisClass, testMethodList[i])) {
					thisClass.timeMethod(thisClass, testMethodList[i], iterations);
				}
			}

		} catch(Exception e) {
			e.printStackTrace(out);
		}
	}
	
	
	/*
	 * This is a simple wrapper around the Class.getMethod method.
	 * It keeps us from having to write try-catch blocks in the sections
	 * of code where we're trying to get the individual methods, so
	 * it makes the code a little easier to read (we just return null
	 * if there's an error)
	 */
	public Method getMethod (Class cls, String methodName, Class[] paramTypes) {
		Method meth = null;
		try {
			meth = cls.getMethod(methodName, paramTypes);
		} catch (Exception e) {
			out.println("Error getting method " + methodName + ": " + e);
		}
		return meth;
	}
	
	
	/*
	 * This is a simple wrapper around the Method.invoke method.
	 * It keeps us from having to write try-catch blocks in the sections
	 * of code where we're trying to run the individual methods, so
	 * it makes the code a little easier to read (we just return null
	 * if there's an error)
	 */
	public Object runMethod (Object classInstance, Method meth, Object[] params) {
		Object objResult = null;
		try {
			objResult = meth.invoke(classInstance, params);
		} catch (InvocationTargetException ite) {
			out.println("The method " + meth.getName() + " threw an error as it was running:");
			ite.printStackTrace(out);
		} catch (Exception e) {
			out.println("Error trying to run method " + meth.getName() + ": " + e);
		}
		return objResult;
	}
	
	
	/*
	 * This is our validation routine. It runs a few simple replace
	 * examples and tests to see if the return value is correct.
	 * If you want to add any additional tests, you can just add
	 * them to the testParams and results arrays.
	 */
	public boolean validateMethod (Object classInstance, Method meth) {
		boolean retVal = true;
		if ((classInstance == null) || (meth == null))
			return false;
		
		Object[][] testParams = {	{"aaaaaaaaaa", "a", "b"},
									{"aaabaaabaaaa", "aa", "a"}, 
									{"aaabaaabaaaa", "baa", "baba"}, 
									{"ddogddogddog", "dog", "og"},
									{"abababababa", "a", ""},
									{"abababababa", "a", null},
									{"abababababa", "", "c"},
									{"abababababa", null, "c"},
									{"abababababa", "", ""},
									{"abababababa", null, null},
									{null, "anything", "whatever"}	};
		String[] results = {	"bbbbbbbbbb",
								"aabaabaa",
								"aaababaababaaa",
								"dogdogdog",
								"bbbbb",
								"bbbbb",
								"abababababa",
								"abababababa",
								"abababababa",
								"abababababa",
								null	};
		
		Object objResult;
		out.println("\nTESTING METHOD " + meth.getName());
		
		for (int i = 0; i < testParams.length; i++) {
			objResult = runMethod(classInstance, meth, testParams[i]);
			if ((results[i] == null) && (objResult == null)) {
				out.println("Method " + meth.getName() + " passed test " + i);
			} else if ((results[i] != null) && (results[i].equals(objResult))) {
				out.println("Method " + meth.getName() + " passed test " + i);
			} else {
				out.println("FAILED. Method " + meth.getName() + " failed test " + i);
				out.println("\tdesired result: " + results[i]);
				out.println("\tactual result: " + objResult);
				retVal = false;
			}
		}
		
		return retVal;
	}
	
	
	/*
	 * This is a routine that times how long it takes to search
	 * and replace on a large String. We're assuming that the
	 * Method has already been tested for correctness.
	 */
	public boolean timeMethod (Object classInstance, Method meth, int iterations) {
		boolean retVal = true;
		int i = 0;
		
		if ((classInstance == null) || (meth == null))
			return false;
		
		String testString = "abcdef";
		// after 12 iterations, the 6 character testString
		// will have grown to over 24,000 characters in length
		for (i = 0; i < 12; i++) {
			testString += testString;
		}
		
		Object[] testParams = { testString, "abc", "xyz" };
		
		out.println("\nTIMING METHOD " + meth.getName());
		long startTime = System.currentTimeMillis();
		
		for (i = 0; i < iterations; i++) {
			if (runMethod(classInstance, meth, testParams) == null) {
				out.println("FAILED. " + meth.getName() + " is null");
				i = iterations;
				retVal = false;
			} else if (System.currentTimeMillis() - startTime > 10000) {
				out.println("TAKING TOO LONG: Breaking out of testing loop after " + i + 
							" of " + iterations + " iterations.");
				break;
			}
		}
		
		long endTime = System.currentTimeMillis();
		out.println(Long.toString(endTime - startTime) + " ms elapsed for " + i + " iterations");
		
		return retVal;
	}
	
	
	/*
	 * This routine actually fails the validation test, but it's good to
	 * see what happens in the case of a failure
	 */
	public String replaceSubstring1 (String str, String find, String replace)
	{
		for(int i = str.lastIndexOf(find); i >= 0; i = str.lastIndexOf(find, i - 1))
			str = str.substring(0, i) + replace + str.substring(i + find.length(), str.length());
		
		return str;
	}
	
	
	/*
	 * This routine works, but it's dog slow when you're working with
	 * large Strings and large numbers of replacements because it's
	 * doing all sorts of in-place String concatenations.
	 */
	public String replaceSubstring2 (String str, String find, String replace)
	{
		// sanity checks (you could argue that the method should throw
		// an error in the case of nulls, but I'd rather deal with it
		// this way)
		if (str == null) return null;
		if ((find == null) || (find.length() == 0)) return str;
		if (replace == null) replace = "";
		
		for(int i = str.indexOf(find); i >= 0; i = str.indexOf(find, i + replace.length()))
			str = str.substring(0, i) + replace + str.substring(i + find.length(), str.length());
		
		return str;
	}
	
	
	/*
	 * This routine is similar to the one above, but it keeps appending
	 * to a separate String instead of rebuilding the original String
	 * each time (which makes it just a tiny bit faster)
	 */
	public String replaceSubstring3 (String str, String find, String replace)
	{
		// sanity checks (you could argue that the method should throw
		// an error in the case of nulls, but I'd rather deal with it
		// this way)
		if (str == null) return null;
		if ((find == null) || (find.length() == 0)) return str;
		if (replace == null) replace = "";
		
		String newString = "";
		int pos = 0;
		int lastPos = 0;
		
		while (pos >= 0) {
			pos = str.indexOf(find, lastPos);
			if (pos >= 0) {
				newString += str.substring(lastPos, pos) + replace;
			} else {
				newString += str.substring(lastPos);
			}
			lastPos = pos + find.length();
		}
		
		return newString;
	}
	
	
	/*
	 * This routine is very fast, because it uses a StringBuffer
	 */
	public String replaceSubstring4 (String str, String find, String replace)
	{
		// sanity checks (you could argue that the method should throw
		// an error in the case of nulls, but I'd rather deal with it
		// this way)
		if (str == null) return null;
		if ((find == null) || (find.length() == 0)) return str;
		if (replace == null) replace = "";
		
		StringBuffer sb = new StringBuffer(str.length());
		int pos = 0;
		int lastPos = 0;
		
		while (pos >= 0) {
			pos = str.indexOf(find, lastPos);
			if (pos >= 0) {
				sb.append(str.substring(lastPos, pos));
				sb.append(replace);
			} else {
				sb.append(str.substring(lastPos));
			}
			lastPos = pos + find.length();
		}
		
		return sb.toString();
	}
	
	/*
	 * Here's a slightly faster method, with modifications from Jim Cakalic.
	 * The difference in speed between this and replaceSubstring4 isn't
	 * apparent until you start doing large numbers of iterations, but
	 * the two essential changes were to store find.length() in a variable
	 * so you don't have to keep calling the method every time you make a
	 * replacement, and to deal with the original String as a char array,
	 * so you avoid the overhead of all the String creation involved with
	 * many calls to String.substring. He also "stacked" the calls to
	 * StringBuffer.append as a small efficiency gain.
	 */
	public String replaceSubstring5 (String str, String find, String replace)
	{ 
		// sanity checks (you could argue that the method should throw
		// an error in the case of nulls, but I'd rather deal with it
		// this way)
		if (str == null) return null;
		if (find == null || find.length() == 0) return str;
		if (replace == null) replace = "";

		StringBuffer sb = new StringBuffer(str.length()); 
		char[] chars = str.toCharArray(); 
		int findLen = find.length(); 
		int pos = 0; 
		int lastPos = 0; 
		
		while (pos >= 0) { 
			pos = str.indexOf(find, lastPos); 
			if (pos >= 0) { 
				sb.append(chars, lastPos, pos - lastPos).append(replace); 
			} else { 
				sb.append(chars, lastPos, str.length() - lastPos); 
			} 
			lastPos = pos + findLen; 
		} 
		
		return sb.toString(); 
        } 

	/*
	 * Modified by: John Marshall
	 * Similar to replaceSubstring5 but with added variable for length of source string.
	 * Prevents the calculation of source string length per iteration.
	 * Fixed possible bug where replaceSubstring("", "", "data")
	 * returned null instead of the value of the replacing string.
	 */
	public String replaceSubstring6 (String str, String find, String replace)
	{ 
		// sanity checks (you could argue that the method should throw
		// an error in the case of nulls, but I'd rather deal with it
		// this way)
		if (null == str) return null;
		if (null == find) return str;
		if (null == replace) replace = "";
		
		int strLen = str.length();
		int findLen = find.length();
		
		if ((0 == strLen) && (0 == findLen)) return replace;
		if (0 == findLen) return str;
		
		StringBuffer sb = new StringBuffer(strLen);
		char[] chars = str.toCharArray();
		int pos = 0;
		int lastPos = 0;
		
		while (pos >= 0) {
			pos = str.indexOf(find, lastPos);
			if (pos >= 0) {
				sb.append(chars, lastPos, pos - lastPos).append(replace);
			} else {
				sb.append(chars, lastPos, strLen - lastPos);
			}
			lastPos = pos + findLen;
		}
		
		return sb.toString();
	}

}

