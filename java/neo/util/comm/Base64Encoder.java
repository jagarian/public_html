package neo.util.comm;

import java.io.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	Base64Encoder.java
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
public class Base64Encoder extends FilterOutputStream {

	private static final char[] chars = {
											'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
											'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
											'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
											'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
											'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
											'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
											'8', '9', '+', '/'
											};
	private int charCount;
	private int carryOver;

	public Base64Encoder(OutputStream out) {
		super(out);
	}

	public void write(int b) throws IOException {
		if (b < 0) {
			b += 256;
		}

		if (charCount % 3 == 0) {
			int lookup = b >> 2;
			carryOver = b & 3; // last two bits
			out.write(chars[lookup]);
		} else if (charCount % 3 == 1) {
			int lookup = ((carryOver << 4) + (b >> 4)) & 63;
			carryOver = b & 15; // last four bits
			out.write(chars[lookup]);
		} else if (charCount % 3 == 2) {
			int lookup = ((carryOver << 2) + (b >> 6)) & 63;
			out.write(chars[lookup]);
			lookup = b & 63; // last six bits
			out.write(chars[lookup]);
			carryOver = 0;
		}
		charCount++;

		if (charCount % 57 == 0) {
			out.write('\n');
		}
	}

	public void write(byte[] buf, int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			write(buf[off + i]);
		}
	}

	public void close() throws IOException {
		if (charCount % 3 == 1) { // one leftover
			int lookup = (carryOver << 4) & 63;
			out.write(chars[lookup]);
			out.write('=');
			out.write('=');
		} else if (charCount % 3 == 2) { // two leftovers
			int lookup = (carryOver << 2) & 63;
			out.write(chars[lookup]);
			out.write('=');
		}
		super.close();
	}

	public static String encode(String unencoded) {
		byte[] bytes = null;
		try {
			bytes = unencoded.getBytes("8859_1");
		} catch (UnsupportedEncodingException ignored) {
		}
		return encode(bytes);
	}

	public static String encode(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 1.37));
		Base64Encoder encodedOut = new Base64Encoder(out);

		try {
			encodedOut.write(bytes);
			encodedOut.close();

			return out.toString("8859_1");
		} catch (IOException ignored) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			Log.info("Usage: java com.oreilly.servlet.Base64Encoder fileToEncode", null);
			return;
		}

		Base64Encoder encoder = null;
		BufferedInputStream in = null;
		try {
			encoder = new Base64Encoder(System.out);
			in = new BufferedInputStream(new FileInputStream(args[0]));

			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				encoder.write(buf, 0, bytesRead);
			}
		} finally {
			if (in != null)
				in.close();
			if (encoder != null)
				encoder.close();
		}
	}
}
