package neo.util.security;

/*
 * This is a program that listens for HTTP requests on a given port
 * and sends back the HTTP header and body that were sent by the
 * client. It's nice for looking at the header information that your
 * browser is sending, or looking at the contents of SOAP requests
 * that a program is generating. It's just a little single-threaded 
 * job, so don't pretend it's a real web server or anything.
 *
 * Julian Robichaux -- http://www.nsftools.com
 */

import java.io.*;
import java.net.*;

/**
 * 	@Class Name	: 	HttpEcho.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
public class HttpEcho {
	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("USAGE: java HttpEcho ");
			return;
		}

		try {
			int port = Integer.parseInt(args[0]);
			ServerSocket server = new ServerSocket(port);
			System.out.println("Started HttpEcho on port " + port
					+ ". Press CTRL-C to end.\n");

			while (true) {
				Socket client = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream());

				StringBuffer header = new StringBuffer("");
				StringBuffer body = new StringBuffer("");
				String data;
				int contentLength = 0;
				int pos = 0;

				// get the header
				while ((data = in.readLine()) != null) {
					// the header ends at the first blank line
					if (data.length() == 0)
						break;
					header.append(data + "\n");
					pos = data.toLowerCase().indexOf("content-length:");
					if (pos >= 0)
						contentLength = Integer.parseInt(data.substring(pos + 15).trim());
				}

				// get the body, if any
				if (contentLength > 0) {
					try {
						char[] cbuf = new char[contentLength];
						in.read(cbuf);
						body.append(cbuf);
					} catch (Exception e) {
						body.append("Error: " + e);
					}
				} else {
					body.append("No Body information retrieved. "
								+ "Content-Length equals zero or was not specified.");
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
				System.out.println("\n============ HttpEcho on Port " + port + ". Press CTRL-C to End ============\n");

				// close all the client streams so we can listen again
				out.close();
				in.close();
				client.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
