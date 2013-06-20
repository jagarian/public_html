package neo.util.comm;

import java.io.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	PatternMatch.java
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
public class PatternMatch
{
	/**
	 * The main method has been provided to allow you to test the class,
	 * and to give you an example of calling the isMatch method.
	 */
	public static void main (String[] args)
	{
		if (args.length < 2)
		{
			Log.info("Usage: PatternMatch  \n", null);
			return;
		}  else  {
			PatternMatch pm = new PatternMatch();
			Log.info( (pm.isMatch(args[0], args[1])?"True":"False"), null);
		}
	}
	
	
	/**
	 * Returns a boolean value indicating whether or not checkString
	 * matches the pattern. The pattern can include single characters, 
	 * a range of characters enclosed in brackets, a question mark 
	 * (which matches exactly one character), or an asterisk (which
	 * matches zero or more characters).
	 * 
	 * If you're matching a character range, it can be either single
	 * characters, like [abc], or a range of characters, like [a-c], or a
	 * combination, like [a-clmnx-z]. For example, the pattern 'b[aiu]t'
	 * would match 'bat', 'bit', and 'but', and the pattern 'a[1-9]'
	 * would match a1, a2, a3, a4, a5, a6, a7, a8, and a9.
	 * 
	 * This should all work much (exactly?) like file matching in DOS.
	 * For example, a pattern of '*.txt' should match all strings ending
	 * in '.txt', '*.*' should match all strings with a '.' in them,
	 * '*.???' should match strings with a three letter extension, and 
	 * so on.
	 * 
	 * Also, please note that the pattern check IS case-sensitive. If you 
	 * don't want it to be, you should convert the checkString and the 
	 * pattern to lower-case as you're passing them.
	 */
	public boolean isMatch (String checkString, String pattern)
	{
		char patternChar;
		int patternPos = 0;
		char lastPatternChar;
		char thisChar;
		int i, j;
		
		for (i = 0; i < checkString.length(); i++)
		{
			// if we're at the end of the pattern but not the end
			// of the string, return false
			if (patternPos >= pattern.length())
				return false;
			
			// grab the characters we'll be looking at
			patternChar = pattern.charAt(patternPos);
			thisChar = checkString.charAt(i);
			
			
			switch (patternChar)
			{
				// check for '*', which is zero or more characters
				case '*' :
					// if this is the last thing we're matching,
					// we have a match
					if (patternPos >= (pattern.length() - 1))
						return true;
					
					// otherwise, do a recursive search
					for (j = i; j < checkString.length(); j++)
					{
						if (isMatch(checkString.substring(j), pattern.substring(patternPos + 1)))
							return true;
					}
					
					// if we never returned from that, there is no match
					return false;
					
					
				// check for '?', which is a single character
				case '?' :
					// do nothing, just advance the patternPos at the end
					break;
					
			
				// check for '[', which indicates a range of characters
				case '[' :
					// if there's nothing after the bracket, we have
					// a syntax problem
					if (patternPos >= (pattern.length() - 1))
						return false;
					
					lastPatternChar = '\u0000';
					for (j = patternPos + 1; j < pattern.length(); j++)
					{
						patternChar = pattern.charAt(j);
						if (patternChar == ']')
						{
							// no match found
							return false;
						}  else	if (patternChar == '-')  {
							// we're matching a range of characters
							j++;
							if (j == pattern.length())
								return false;		// bad syntax
							
							patternChar = pattern.charAt(j);
							if (patternChar == ']')
							{
								return false;		// bad syntax
							}  else  {
								if ((thisChar >= lastPatternChar) && (thisChar <= patternChar))
									break;		// found a match
							}
						}  else if (thisChar == patternChar)  {
							// if we got here, we're doing an exact match
							break;
						}
						
						lastPatternChar = patternChar;
					}
					
					// if we broke out of the loop, advance to the end bracket
					patternPos = j;
					for (j = patternPos; j < pattern.length(); j++)
					{
						if (pattern.charAt(j) == ']')
							break;
					}
					patternPos = j;
					break;
					
					
				default :
					// the default condition is to do an exact character match
					if (thisChar != patternChar)
						return false;
					
			}
			
			// advance the patternPos before we loop again
			patternPos++;
			
		}
		
		// if there's still something in the pattern string, check to
		// see if it's one or more '*' characters. If that's all it is,
		// just advance to the end
		for (j = patternPos; j < pattern.length(); j++)
		{
			if (pattern.charAt(j) != '*')
				break;
		}
		patternPos = j;
		
		// at the end of all this, if we're at the end of the pattern
		// then we have a good match
		if (patternPos == pattern.length())
		{
			return true;
		}  else  {
			return false;
		}
		
	}
	
	
}

