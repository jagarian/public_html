package neo.util.security;

/**
 * 	@Class Name	: 	TestBase64Coder.java
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
public class TestBase64Coder {

	public static void main(String args[]) throws Exception {
		System.out.println("TestBase64Coder started");
		test1();
		test2();
		System.out.println("TestBase64Coder completed");
	}

	// Test Base64Coder with constant strings.
	private static void test1() {
		System.out.println("test1 started");
		check("Aladdin:open sesame", "QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
		check("", "");
		check("1", "MQ==");
		check("22", "MjI=");
		check("333", "MzMz");
		check("4444", "NDQ0NA==");
		check("55555", "NTU1NTU=");
		check("abc:def", "YWJjOmRlZg==");
		System.out.println("test1 completed");
	}

	private static void check(String plainText, String base64Text) {
		String s1 = Base64Coder.encodeString(plainText);
		String s2 = Base64Coder.decodeString(base64Text);
		if (!s1.equals(base64Text) || !s2.equals(plainText))
			System.out.println("check failed for \"" + plainText + "\" / \""
					+ base64Text + "\".");
	}

	// Compares two byte arrays.
	private static boolean compareByteArrays(byte[] a1, byte[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int p = 0; p < a1.length; p++)
			if (a1[p] != a2[p])
				return false;
		return true;
	}

	// Test Base64Coder against sun.misc.BASE64Encoder/Decoder with
	// random strings.
	private static void test2() throws Exception {
		System.out.println("test2 started");
		sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
		java.util.Random rnd = new java.util.Random(0x538afb92);
		for (int i = 0; i < 50000; i++) {
			int len = rnd.nextInt(55);
			byte[] b0 = new byte[len];
			rnd.nextBytes(b0);
			String e1 = new String(Base64Coder.encode(b0));
			String e2 = enc.encode(b0);
			if (!e1.equals(e2))
				System.out.println("Error\ne1=" + e1 + " len=" + e1.length()
						+ "\ne2=" + e2 + " len=" + e2.length());
			byte[] b1 = Base64Coder.decode(e1);
			byte[] b2 = dec.decodeBuffer(e2);
			if (!compareByteArrays(b1, b0) || !compareByteArrays(b2, b0))
				System.out.println("Decoded data not equal. len1=" + b1.length
						+ " len2=" + b2.length);
		}
		System.out.println("test2 completed");
	}

} // end class TestBase64Coder
