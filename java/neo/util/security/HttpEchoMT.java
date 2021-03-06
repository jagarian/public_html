package neo.util.security;

/*
 * This is a simple multi-threaded version of the HttpEcho program.
 * It listens for HTTP requests on a given port and sends back 
 * the HTTP header and body that were sent by the client. 
 * It's nice for looking at the header information that your
 * browser is sending, or looking at the contents of SOAP requests
 * that a program is generating.
 *
 * Julian Robichaux -- http://www.nsftools.com
 */

import java.io.*;
import java.net.*;

/**
 * 	@Class Name	: 	HttpEchoMT.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ��������������,���)
 **********************************************************************************************             
 */
public class HttpEchoMT
{
	public static void main (String args[])
	{
		if (args.length == 0)
		{
			System.out.println("USAGE: java HttpEchoMT ");
			return;
		}
			
		
		try {
			int port = Integer.parseInt(args[0]);
			ServerSocket server = new ServerSocket(port);
			System.out.println("Started HttpEchoMT on port " + port + ". Press CTRL-C to end.\n");
			
			while (true)
			{
				Socket client = server.accept();
				EchoThread t = new EchoThread(client);
				t.start();
			}
		}  catch (Exception e)  {
			System.out.println(e);
		}
	}
}


class EchoThread extends Thread
{
	private Socket echoSocket;
	
	public EchoThread(Socket s)
	{
		echoSocket = s;
	}

	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader(
								new InputStreamReader(echoSocket.getInputStream()));
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream());
				
			StringBuffer header = new StringBuffer("");
			StringBuffer body = new StringBuffer("");
			String data;
			int contentLength = 0;
			int pos = 0;
				
			// get the header
			while ((data = in.readLine()) != null)
			{
				// the header ends at the first blank line
				if (data.length() == 0)
					break;
				header.append(data + "\n");
				pos = data.toLowerCase().indexOf("content-length:");
				if (pos >= 0)
					contentLength = Integer.parseInt(data.substring(pos + 15).trim());
			}
				
			// get the body, if any
			if (contentLength > 0)
			{
				try {
					char[] cbuf = new char[contentLength];
					in.read(cbuf);
					body.append(cbuf);
				}  catch (Exception e)  {
					body.append("Error: " + e);
				}
			}  else  {
				body.append("No Body information retrieved. " + 
							"Content-Length equals zero or was not specified.");
			}
				
			// write back to the client
			out.print("HTTP/1.0 200\nContent Type: text/plain\n\n");
			out.println("HEADER:");
			out.println(header);
			out.println("BODY:");
			out.println(body);
				
			// write to the console too
			System.out.println("HEADER:");
			System.out.println(header);
			System.out.println("BODY: (" + contentLength + " / " + body.length() + ")");
			System.out.println(body);
			System.out.println("\n============ HttpEchoMT on Port " + echoSocket.getLocalPort() + 
								". Press CTRL-C to End ============\n");
			
			// close all the client streams so we can listen again
			out.close();
			in.close();
			echoSocket.close();
		}  catch (Exception e)  {
			System.out.println(e);
		}

	}

}


