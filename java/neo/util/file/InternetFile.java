package neo.util.file;

import java.io.*;
import java.net.*;

/**
 * A class that will get a file from a web or ftp server.
 * 
 * You may use this code as you wish, just don't pretend 
 * that you wrote it yourself, and don't hold me liable for 
 * anything that it does or doesn't do. If you're feeling 
 * especially honest, please include a link to nsftools.com
 * along with the code.
 * 
 * For updates and more information, please visit 
 * www.nsftools.com
 *
 * @author Julian Robichaux
 * @version 0.9
 */
/**
 * 	@Class Name	: 	InternetFile.java
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
public class InternetFile
{
	private String globalUrlString;
	private String globalProxyServer;
	private int globalProxyPort;
	
	static int BLOCK_SIZE = 4096;
	
	// CONSTRUCTORS
	public InternetFile ()
	{
		this("", "", 0);
	}
	
	public InternetFile (String urlString)
	{
		this(urlString, "", 0);
	}
	
	public InternetFile (String urlString, String proxyServer, int proxyPort)
	{
		globalUrlString = urlString;
		globalProxyServer = proxyServer;
		globalProxyPort = proxyPort;
	}
	
	
	// ACCESSORS AND MUTATORS
	public void SetURL (String urlString)
	{
		globalUrlString = urlString;
	}
	
	public String GetURL ()
	{
		return globalUrlString;
	}
	
	public void SetProxy (String proxyServer, int proxyPort)
	{
		globalProxyServer = proxyServer;
		globalProxyPort = proxyPort;
	}
	
	public String GetProxyServer ()
	{
		return globalProxyServer;
	}
	
	public int GetProxyPort ()
	{
		return globalProxyPort;
	}
	
	
	// PUBLIC METHODS
	public String toString ()
	{
		// standard toString method
		if (globalProxyServer.length() == 0)
		{
			return globalUrlString;
		}  else  {
			return globalUrlString + 
				" (proxy: " + globalProxyServer + " at port " + globalProxyPort;
		}
	}
	
	
	// overloaded method to get a file by its URL
	public boolean GetURLFile ()
	{
		return GetURLFile(globalUrlString, "", globalProxyServer, globalProxyPort);
	}
	
	public boolean GetURLFile (String urlString)
	{
		return GetURLFile(urlString, "", globalProxyServer, globalProxyPort);
	}
	
	public boolean GetURLFile (String urlString, String localFileName)
	{
		return GetURLFile(urlString, localFileName, globalProxyServer, globalProxyPort);
	}
	
	/**
	 * Get whatever's at the URL specified by urlString, and
	 * put it on the local machine as a file specified by
	 * localFileName. You can use http:// or ftp:// URLs, and
	 * you can optionally specify a proxy server you need to 
	 * go through.
	 *
	 * @param urlString		the fully-qualified URL you wish to
	 *						access (can be http:// or ftp://).
	 *
	 * @param localFileName	 the name of the file on the local
	 *						 computer that will contain the
	 *						 information found at the URL. If
	 *						 this parameter is "", the name of
	 *						 the file at the end of the URL is
	 *						 used.
	 *
	 * @param proxyServer	the name of the proxy server
	 *						you'll have to go through to access
	 *						the URL, if any (use "" if no proxy
	 *						server is required).
	 *
	 * @param proxyPort		the port on the proxy server that
	 *						should be used, if any (use 0 if no
	 *						proxy server is required).
	 */
	public boolean GetURLFile(String urlString, String localFileName, String proxyServer, int proxyPort)
	{
		BufferedInputStream fileStream = null;
		RandomAccessFile outFile = null;
		
		try
		{
			URL theUrl;
			
			if ((proxyServer.length() > 0) && (proxyPort > 0))
			{
				// use HTTP proxy, even for FTP downloads
				theUrl = new URL("http", proxyServer, proxyPort, urlString);
			}  else  {
				theUrl = new URL(urlString);
			}
			
			// create a default file name, if none was given
			if (localFileName.length() == 0)
			{
				localFileName = getFileName(urlString);
			}
			
			System.out.println("Attempting to connect to " + theUrl);
			
			// go get the file
			URLConnection con = theUrl.openConnection();
			
			// if we were able to connect (we would have errored out
			// by now if not), try to get the file.
			// Use a BufferedInputStream instead of a BufferedReader,
			// because a BufferedReader won't retrieve non-text files
			// properly
			fileStream = new BufferedInputStream(con.getInputStream());
			
			// if we got the remote file, create the local file that
			// we can write the information to
			outFile = new RandomAccessFile(localFileName, "rw");
			
			System.out.println("Downloading to local file " + localFileName);
			
			// write to the file in bytes (in case it's not text)
			int howManyBytes;
			byte[] bytesIn = new byte[BLOCK_SIZE];
			while((howManyBytes = fileStream.read(bytesIn)) >= 0){
				outFile.write(bytesIn, 0, howManyBytes);
				//stringBuf.append(bytesIn, 0, howManyBytes); // to send to a StringBuffer
				//System.out.write(bytesIn, 0, howManyBytes);  // to send to the console
			}
			
			// close up the streams
			fileStream.close();
			outFile.close();
			
			System.out.println("Finished downloading file to " + localFileName);
			return true;
			
		}  catch(MalformedURLException e)  {
			System.out.println("ERROR: Invalid URL: " + urlString);
		}  catch(NoRouteToHostException e)  {
			System.out.println("ERROR: URL cannot be reached: " + urlString);
		}  catch(ConnectException e)  {
			System.out.println("ERROR: Connection error: " + e.getMessage());
		}  catch(FileNotFoundException e)  {
			System.out.println("ERROR: File or Path not found: " + e.getMessage());
		}  catch(Exception e)  {
			e.printStackTrace();
			//System.err.println(e.getMessage());
		}  finally  {
			// make sure the streams got closed, in case of an error
			try  {  fileStream.close(); outFile.close();  }  catch(Exception e)  {}
		}
		
		//if we got here, there was some kind of error
		return false;
		
	}
	
	
	// PRIVATE FUNCTIONS
	
	// private function that gets the file name from a URL string
	private static String getFileName(String urlString)
	{
		String tempString;
		int lastSlash = urlString.lastIndexOf('/');
		
		if (lastSlash >= 0)
		{
			tempString = urlString.substring(lastSlash + 1);
		}  else  {
			tempString = new String("");
		}
		
		if (tempString.length() == 0)
		{
			tempString = new String("Default.txt");
		}
		
		return tempString;
	}

}

