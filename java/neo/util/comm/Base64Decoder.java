package neo.util.comm;

import java.io.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	Base64Decoder.java
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
public class Base64Decoder extends FilterInputStream {

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

	private static final int[] ints = new int[128];
	
	static {
		for (int i = 0; i < 64; i++) {
			ints[chars[i]] = i;
		}
	}

	public Base64Decoder(InputStream in) {
		super(in);
	}

	public int read() throws IOException {
		int x;
		do {
			x = in.read();
			if (x == -1) {
				return -1;
			}
		} while (Character.isWhitespace((char) x));
		charCount++;
		if (x == '=') {
			return -1;
		}
		x = ints[x];

		int mode = (charCount - 1) % 4;

		if (mode == 0) {
			carryOver = x & 63;
			return read();
		} else if (mode == 1) {
			int decoded = ((carryOver << 2) + (x >> 4)) & 255;
			carryOver = x & 15;
			return decoded;
		} else if (mode == 2) {
			int decoded = ((carryOver << 4) + (x >> 2)) & 255;
			carryOver = x & 3;
			return decoded;
		} else if (mode == 3) {
			int decoded = ((carryOver << 6) + x) & 255;
			return decoded;
		}
		return -1; // can't actually reach this line
	}

	public int read(byte[] buf, int off, int len) throws IOException {
		if (buf.length < (len + off - 1)) {
			throw new IOException("The input buffer is too small: "
									+ len
									+ " bytes requested starting at offset "
									+ off
									+ " while the buffer "
									+ " is only "
									+ buf.length
									+ " bytes long.");
		}
		int i;
		for (i = 0; i < len; i++) {
			int x = read();
			if (x == -1 && i == 0) { // an immediate -1 returns -1
				return -1;
			} else if (x == -1) { // a later -1 returns the chars read so far
				break;
			}
			buf[off + i] = (byte) x;
		}
		return i;
	}

	public static String decode(String encoded) {
		return new String(decodeToBytes(encoded));
	}

	public static byte[] decodeToBytes(String encoded) {
		byte[] bytes = null;
		try {
			bytes = encoded.getBytes("8859_1");
		} catch (UnsupportedEncodingException ignored) {
		}

		Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));

		ByteArrayOutputStream out =
			new ByteArrayOutputStream((int) (bytes.length * 0.67));

		try {
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} catch (IOException ignored) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			Log.info("Usage: java Base64Decoder fileToDecode", null);
			return;
		}
		Base64Decoder decoder = null;
		try {
			decoder =
				new Base64Decoder(
					new BufferedInputStream(new FileInputStream(args[0])));
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = decoder.read(buf)) != -1) {
				System.out.write(buf, 0, bytesRead);
			}
		} finally {
			if (decoder != null)
				decoder.close();
		}
	}
}
