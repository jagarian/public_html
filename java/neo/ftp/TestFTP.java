package neo.ftp;

import java.io.*;
import java.util.*;

/**
 * 	@Class Name	: 	SearchClass.java
 * 	@파일설명		: 	This program/class is meant to test the functionality
 * 						of the FTPConnection class.
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
class TestFTP
{
	public static void main (String[] args)
	{
		String serverName;
		FTPConnection ftp = null;
		
		try
		{
			if (args.length == 0)
			{
				serverName = getStringFromUser("Enter the server you would like to connect to: ");
				if (serverName.length() == 0)  {  return;  }
			}  else  {
				serverName = args[0];
			}
			
			// set the FTPConnection parameter to true if you want to
			// see debug output in your console window
			ftp = new FTPConnection(false);
			System.out.println("Trying to connect anonymously to " + serverName);
			ftp.connect(serverName);
			
			if (ftp.login("anonymous", "blah@blah.blah"))
			{
				System.out.println("Successfully logged in!");
				System.out.println("System type is: " + ftp.getSystemType());
				System.out.println("Current directory is: " + ftp.getCurrentDirectory());
				String files = ftp.listFiles();
				String subDirs = ftp.listSubdirectories();
				System.out.println("Files in Directory:\n" + files);
				System.out.println("Subdirectories:\n" + subDirs);
				
				// try to change to the first subdirectory
				StringTokenizer st = new StringTokenizer(subDirs, ftp.lineTerm);
				String sdName = "";
				if (st.hasMoreTokens())  { sdName = st.nextToken(); }
				
				if (sdName.length() > 0)
				{
					System.out.println("Changing to directory " + sdName);
					if (ftp.changeDirectory(sdName))
					{
						// just for kicks, try to download the first 3 files in the directory
						files = ftp.listFiles();
						st = new StringTokenizer(files, ftp.lineTerm);
						
						String fileName;
						int count = 1;
						while ((st.hasMoreTokens()) && (count < 3)) {
							fileName = st.nextToken();
							System.out.println("Downloading " + fileName + " to C:\\");
							try
							{
								if (ftp.downloadFile(fileName, "C:\\" + fileName))
								{
									System.out.println("Download successful!");
								}  else  {
									System.out.println("Error downloading " + fileName);
								}
							}  catch(Exception de)  {
								System.out.println("ERROR: " + de.getMessage());
							}
							
							count++;
						}
					}
					
				}  else  {
					System.out.println("There are no Subdirectories!");
				}
				
				ftp.logout();
				ftp.disconnect();
				System.out.println("Disconnected and Logged Out.");
			}  else  {
				System.out.println("Sorry. Could not connect.");
			}
		}  catch(Exception e)  {
			e.printStackTrace();
			try { ftp.disconnect(); }  catch(Exception e2)  {}
		}
	}
	
	// private function that gets console input from the user
	private static String getStringFromUser(String prompt) throws IOException
	{
		System.out.print(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return br.readLine();
	}

}

//URL url = 
//    new URL("ftp://username:password@ftp.whatever.com/file.zip;type=i");
//URLConnection con = url.openConnection();
//BufferedInputStream in = 
//    new BufferedInputStream(con.getInputStream());
//FileOutputStream out = 
//    new FileOutputStream("C:\\file.zip");

//int i = 0;
//byte[] bytesIn = new byte[1024];
//while ((i = in.read(bytesIn)) >= 0) {
//	out.write(bytesIn, 0, i);
//}
//out.close();
//in.close();

