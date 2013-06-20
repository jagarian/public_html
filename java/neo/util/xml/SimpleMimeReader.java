package neo.util.xml;

import java.io.*;
import java.util.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	SimpleMimeReader.java
 * 	@파일설명		: 	
 * 
 *	SimpleMimeReader smr = new SimpleMimeReader(new FileInputStream(args[0]));
 * 
 * 	Log.info("BOUNDARY: " + smr.getBoundaryText());
 * 	Log.info("PREAMBLE: " + smr.getPreamble());
 * 	Log.info("CONTENT-TYPE: " + getHeaderValue(smr.getMessageHeader(), "content-type"));
 * 	Log.info("MESSAGE HEADER:\n" + smr.getMessageHeader());
 *
 * 	ByteArrayOutputStream baos = new ByteArrayOutputStream();
 * 	int count = 0;
 * 	while (smr.nextPart()) {
 *  	count++;
 *     	Log.info("----------\nPART " + count + "\n----------");
 *     	Log.info("TYPE: " + smr.getPartType());
 *     	Log.info("ENCODING: " + smr.getPartEncoding());
 *     	Log.info("CONTENT ID: " + smr.getPartID());
 *     	Log.info("HEADER:\n" + smr.getPartHeader());
 *     	long size = smr.getPartData(baos);
 *		Log.info("BODY LENGTH: " + size);
 *     
 *     	baos.writeTo(new FileOutputStream(args[0] + ".file" + count));
 *     	baos.reset();
 * 	}
 * 
 * 	Log.info("----------");
 * 	Log.info("EPILOGUE: " + smr.getEpilogue());
 * 
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
public class SimpleMimeReader
{
	private InputStream in = null;
	private String boundary = "";
	private String lastBoundary = "";
	private String lastHeader = "";
	private String docHeader = "";
	private byte[] preamble = { };
	private byte[] epilogue = { };
	private boolean justGotPart = false;
	private ByteArrayOutputStream readBuffer = new ByteArrayOutputStream(1024);
	
	
	/**
	 * A simple main method, in case you want to test the basic
	 * functionality of this class by running it stand-alone.
	 */
	public static void main (String args[]) {
		if (args.length == 0) {
			Log.info("USAGE: java SimpleMimeReader MimeFileName");
			return;
		}
		
		long startTime = System.currentTimeMillis();
		try {
			SimpleMimeReader smr = new SimpleMimeReader(new FileInputStream(args[0]));
			
			Log.info("BOUNDARY: " + smr.getBoundaryText());
			Log.info("PREAMBLE: " + smr.getPreamble());
			Log.info("CONTENT-TYPE: " + getHeaderValue(smr.getMessageHeader(), "content-type"));
			Log.info("MESSAGE HEADER:\n" + smr.getMessageHeader());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = 0;
			while (smr.nextPart()) {
				count++;
				Log.info("----------\nPART " + count + "\n----------");
				Log.info("TYPE: " + smr.getPartType());
				Log.info("ENCODING: " + smr.getPartEncoding());
				Log.info("CONTENT ID: " + smr.getPartID());
				Log.info("HEADER:\n" + smr.getPartHeader());
				long size = smr.getPartData(baos);
				Log.info("BODY LENGTH: " + size);
				
				baos.writeTo(new FileOutputStream(args[0] + ".file" + count));
				baos.reset();
			}
			
			Log.info("----------");
			Log.info("EPILOGUE: " + smr.getEpilogue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		Log.info(String.valueOf(endTime - startTime) + " ms");
	}
	
	
	/**
	 * The sole constructor for this class, which takes any kind
	 * of InputStream as a parameter. 
	 *
	 * @param    inStream    an InputStream that contains a Multi-part
	 *                       MIME message
	 */
	public SimpleMimeReader (InputStream inStream) {
		// make sure we're buffering the input stream, for efficiency
		this.in = new BufferedInputStream(inStream, 4096);
		getMimeBoundary();
	}
	
	
	/**
	 * Advances to the next part of the message, if there is a
	 * next part. When you create an instance of a SimpleMimeReader,
	 * you need to call nextPart() before you start getting data.
	 *
	 * @return    true if there is a next part, false if there isn't 
	 *            (which generally means you're at the end of the
	 *            message)
	 */
	public boolean nextPart ()	{
		// if the last boundary we got was the boundary plus a "--",
		// then the message is officially over (the RFC allows for
		// epilogues after this last boundary, but they're supposed
		// to be ignored)
		if (lastBoundary.equals(boundary + "--")) {
			String tempBoundary = boundary;
			boundary = "";
			justGotPart = false;
			epilogue = getPartDataAsBytes();
			boundary = tempBoundary;
			lastBoundary = "";
			lastHeader = "";
			return false;
		}
		
		// we need to advance to the next boundary, unless we've
		// already got the previous part's data (which means we're
		// already there)
		if (!justGotPart)
			getPartData(null);
		
		// special consideration if we never found a boundary
		// (set the lastHeader to the docHeader on the first
		// call to this function)
		if ((boundary.length() == 0) && (lastHeader.length() == 0))
			lastHeader = docHeader;
		else
			lastHeader = getHeader();
		
		// reset our justGotPart flag
		justGotPart = false;
		
		// if our lastHeader variable has any data at all, we
		// should be at the next section; otherwise, we're at
		// the end of the input stream and should return false
		return (lastHeader.length() > 0);
		
	}
	
	
	/**
	 * Get the boundary that we're breaking the message up on
	 *
	 * @return	 a String containing the message boundary, 
	 * 				or an empty String if the boundary isn't available
	 */
	public String getBoundaryText ()
	{
		return boundary;
	}
	
	
	/**
	 * Get the overall header of the message
	 *
	 * @return a String containing the message header,
	 *            or an empty String if the header isn't available
	 */
	public String getMessageHeader () {
		return docHeader;
	}
	
	
	/**
	 * Get the header of the current message part that we're
	 * looking at
	 *
	 * @return a String containing the current part's header,
	 *            or an empty String if the header isn't available
	 */
	public String getPartHeader () {
		return lastHeader;
	}
	
	
	/**
	 * Get the preamble (anything after the message header and before
	 * the first boundary) of the current message that we're looking at
	 * as a String
	 *
	 * @return a String containing the preamble, or an empty String
	 *            if there is no preamble
	 */
	public String getPreamble () {
		return new String(preamble);
	}
	
	
	/**
	 * Get the preamble (anything after the message header and before
	 * the first boundary) of the current message that we're looking at
	 * as a byte array
	 *
	 * @return a byte array containing the preamble, or an empty byte array
	 *            if there is no preamble
	 */
	public byte[] getPreambleBytes () {
		return preamble;
	}
	
	
	/**
	 * Get the epilogue (anything after the ending boundary) 
	 * of the current message that we're looking at as a String
	 * (available only after all the parts have been read)
	 *
	 * @return a String containing the epilogue, or an empty String
	 *            if there is no epilogue or if you haven't read through
	 *            all the parts of the message yet
	 */
	public String getEpilogue () {
		return new String(epilogue);
	}
	
	
	/**
	 * Get the epilogue (anything after the ending boundary) 
	 * of the current message that we're looking at as a byte array
	 * (available only after all the parts have been read)
	 *
	 * @return a byte array containing the epilogue, or an empty byte array
	 *            if there is no epilogue or if you haven't read through
	 *            all the parts of the message yet
	 */
	public byte[] getEpilogueBytes () {
		return epilogue;
	}
	
	
	/**
	 * Gets the data contained in the current message part as
	 * a byte array (this will return an empty byte array if you've already 
	 * got the data from this message part)
	 *
	 * @return a byte array containing the data in this message part,
	 *            or an empty byte array if you've already read this data
	 */
	public byte[] getPartDataAsBytes () {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getPartData(baos);
		return baos.toByteArray();
	}
	
	
	/**
	 * Gets the data contained in the current message part as
	 * a String (this will return an empty String if you've already 
	 * got the data from this message part)
	 *
	 * @return a String containing the data in this message part,
	 *            or an empty String if you've already read this data
	 */
	public String getPartDataAsString () {
		return new String(getPartDataAsBytes());
	}
	
	
	/**
	 * Writes the data contained in the current message part to
	 * the OutputStream of your choice (this will return zero and
	 * write nothing if you've already got the data from this
	 * message part)
	 *
	 * @param outStream    the OutputStream to write data to
	 * @return a long value indicating the number of bytes
	 *            written to your output stream
	 */
	public long getPartData (OutputStream outStream) {
		long count = 0;
		String line;
		
		// if we've already got the data for this part, don't
		// even try to read any further (because we should be 
		// at the next boundary, or at the end of the stream)
		if (justGotPart)
			return 0;
		
		// make sure we're buffering our output, for efficiency
		BufferedOutputStream out = null;
		if (outStream != null)
			out = new BufferedOutputStream(outStream, 1024);
		
		// start getting data -- this is going to seem a little cumbersome because
		// technically the CRLF (\r\n) that is supposed to appear just before the
		// boundary actually belongs to the boundary, not to the body data (if the
		// body is binary, an extra CRLF at the end could screw it up), so we're
		// always writing the previous line until we find the boundary
		byte[] blineLast = new byte[0];
		byte[] bline = readByteLine(in);
		
		while (bline.length > 0) {
			line = new String(bline);
			if ((boundary.length() > 0) && (line.startsWith(boundary))) {
				// once we've found the next boundary, make sure we write the
				// data in the last line, minus the CRLF that's supposed to be
				// at the end (just to be nice, we'll even try to act properly
				// if the line terminates with a \n instead of a \r\n)
				if (blineLast.length > 1) {
					int len = (blineLast[blineLast.length-2] == '\r') ? 
								blineLast.length-2 : blineLast.length-1;
					count += writeOut(out, blineLast, len);
				}
				lastBoundary = line.trim();
				break;
			} else {
				count += writeOut(out, blineLast, blineLast.length);
				blineLast = bline;
			}
			
			// read the next line
			bline = readByteLine(in);
		}
		
		// if we hit the end of the file, make sure we write the blineLast
		// data before we finish up
		if ((bline.length == 0) && (blineLast.length > 0)) {
			count += writeOut(out, blineLast, blineLast.length);
		}
		
		// flush the buffered stream, to make sure the original
		// output stream gets everything
		if (out != null)
			try { out.flush(); }  catch (Exception e) {}
		
		justGotPart = true;
		return count;
	}
	
	
	/*
	 * A private method that tries to write a byte array to an OutputStream,
	 * and returns the number of bytes that were written (0 if there was an error).
	 * It's just a way of checking the stream and catching the exceptions in
	 * one place, so we don't have to keep duplicating this logic in different
	 * places in our code.
	 */
	private int writeOut (OutputStream out, byte[] data, int len) {
		// don't even try if the OutputStream is null
		if (out == null)
			return 0;
		
		try { 
			out.write(data, 0, len);
			return len;
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	/**
	 * A convenience method to get the Content-Type for the current
	 * message part
	 *
	 * @return a String containing the Content-Type entry of the header,
	 *            if it's available; null if it's not
	 */
	public String getPartType () {
		return getHeaderValue(lastHeader, "Content-Type");
	}
	
	
	/**
	 * A convenience method to get the Content-Transfer-Encoding for the current
	 * message part
	 *
	 * @return a String containing the Content-Transfer-Encoding entry of the header,
	 *            if it's available; null if it's not
	 */
	public String getPartEncoding () {
		return getHeaderValue(lastHeader, "Content-Transfer-Encoding");
	}
	
	
	/**
	 * A convenience method to get the Content-ID for the current
	 * message part
	 *
	 * @return a String containing the Content-ID entry of the header,
	 *            if it's available; null if it's not
	 */
	public String getPartID () {
		return getHeaderValue(lastHeader, "Content-ID");
	}
	
	
	/**
	 * Gets the specified value from a specified header, or null if
	 * the entry does not exist
	 *
	 * @param header : the header to look at
	 * @param entry : the name of the entry you're looking for
	 * @return a String containing the value you're looking for,
	 *            or null if the entry cannot be found
	 */
	public static String getHeaderValue (String header, String entry) {
		String line;
		String value = null;
		boolean gotit = false;
		
		// use the lowercase version of the name, to avoid any case issues
		entry = entry.toLowerCase();
		if (!entry.endsWith(":"))
			entry = entry + ":";
		
		StringTokenizer st = new StringTokenizer(header, "\r\n");
		while (st.hasMoreTokens()) {
			line = st.nextToken();
			if (line.toLowerCase().startsWith(entry)) {
				value = line.substring(entry.length()).trim();
				gotit = true;
			} else if ((gotit) && (line.length() > 0)) {
				// headers can actually span multiple lines, as long as
				// the next line starts with whitespace
				if (Character.isWhitespace(line.charAt(0)))
					value += " " + line.trim();
				else
					gotit = false;
			}
		}
		
		return value;
	}
	
	
	/*
	 * A private method to get the next header block on the InputStream.
	 * For our purposes, a header is a block of text that ends with a
	 * blank line.
	 */
	private String getHeader () {
		StringBuffer header = new StringBuffer("");
		String line;
		byte[] bline = readByteLine(in);
		while (bline.length > 0) {
			line = new String(bline);
			if (line.trim().length() == 0)
				break;
			else
				header.append(line);
			bline = readByteLine(in);
		}
		
		return header.toString();
	}
	
	
	/*
	 * A private method to attempt to read the MIME boundary from the
	 * Content-Type entry in the first header it finds. This should be
	 * called once, when the class is first instantiated.
	 */
	private void getMimeBoundary () {
		String value;
		
		// this shouldn't happen, but in case the Stream starts with
		// one or more blank lines, we'll just skip those to get to
		// our header
		while (docHeader.trim().length() == 0)
			docHeader += getHeader();
		
		// get the Content-Type entry in the header, and read the
		// boundary (if any)
		value = getHeaderValue(docHeader, "content-type");
		if (value != null) {
			int pos1 = value.toLowerCase().indexOf("boundary");
			int pos2 = value.indexOf(";", pos1);
			if (pos2 < 0)
				pos2 = value.length();
			if ((pos1 > 0) && (pos2 > pos1))
				boundary = value.substring(pos1+9, pos2);
		}
		
		// you're allowed to enclose your boundary in quotes too,
		// so we need to account for that possibility
		if (boundary.startsWith("\""))
			boundary = boundary.substring(1);
		if (boundary.endsWith("\""))
			boundary = boundary.substring(0, boundary.length()-1);
		
		boundary = boundary.trim();
		
		// if we didn't find a boundary, we'll treat this as a 
		// single-part message (which means we set justGotPart
		// to true so we don't go looking for anything when we
		// call nextPart() the first time)
		if (boundary.length() == 0) {
			justGotPart = true;
		} else {
			boundary = "--" + boundary;
			preamble = getPartDataAsBytes();
		}
	}	
	
	/*
	 * A way to read a single "line" of bytes from an InputStream.
	 * The byte array that is returned will include the line
	 * terminator (\n), unless we reached the end of the stream.
	 */
	private byte[] readByteLine (InputStream in) {
		// we made readBuffer global, so we don't have to keep recreating it
		//ByteArrayOutputStream readBuffer = new ByteArrayOutputStream(1024);
		readBuffer.reset();
		int c;
		
		try
		{
			// read the bytes one-by-one until we hit a line terminator
			// or the end of the file (we're only checking for \n here, 
			// although if we really wanted to be picky we'd probably 
			// check for \r and \0 as well)
			while ((c = in.read()) != -1)
			{
				readBuffer.write(c);
				if (c == '\n')
					break;
			}
			
		}  catch (Exception e)  {
			// we're not reporting any exceptions here
		}
		
		// and return what we have
		return readBuffer.toByteArray();
	}
}