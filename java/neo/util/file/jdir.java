package neo.util.file;

import java.io.*;
import neo.util.comm.PatternMatch;

/* This is a class that makes use of the PatternMatch class to implement
 * a simple file filter. When running this class, make sure you enclose
 * any arguments with wildcard characters in quotation marks so they will
 * be treated as a literal string; otherwise they will be expanded for you
 * on the command line before being passed to the program. For example:
 *   jdir "c:\*.bat"
 * instead of:
 *   jdir c:\*.bat
 */
/**
 * 	@Class Name	: 	jdir.java
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
public class jdir
{
	public static void main(String args[])
	{
		String dirName = new String(".");		// default to current directory
		String filePattern = new String("*.*");	// default to all files
		int i;
		
		if (args.length > 0)
		{
			// if there's a path separator in the argument, split the
			// directory name and the file pattern filter; otherwise,
			// treat the whole argument as a file pattern
			i = args[0].lastIndexOf(File.separator);
			
			if (i >= 0)
			{
				dirName = args[0].substring(0, i);
				if (i < args[0].length() - 1)
					filePattern = args[0].substring(i+1);
			}  else  {
				filePattern = args[0];
			}
		}

		File f = new File(dirName);
		String[] theFiles = f.list(new PatternFilter(filePattern));
		for (i = 0; i < theFiles.length; i++)
			System.out.println(theFiles[i]);
	}
}

class PatternFilter implements FilenameFilter
{
	String pattern;
	
	public PatternFilter (String thePattern)
	{
		pattern = thePattern.toLowerCase();
	}
	
	public boolean accept(File dir, String name)
	{
		// make sure everything's lowercase, so the match will not be
		// case-sensitive
		String ldir = dir.toString().toLowerCase();
		String lname = name.toLowerCase();
		PatternMatch pm = new PatternMatch();

		return ((pm.isMatch(ldir + dir.separator + lname, pattern)) || (pm.isMatch(name, pattern)) );
	}
}


