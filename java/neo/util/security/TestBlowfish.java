package neo.util.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * 	@Class Name	: 	TestBlowfish.java
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
public class TestBlowfish {

	static public void main(String args[]) throws Exception {
		byte[] key = { 0x11, 0x22, 0x33, 0x44 };
		byte[] plainData = { 0x55, (byte) 0xaa, 0x12, 0x34, 0x56, 0x78,
				(byte) 0x9a, (byte) 0xbc };
		byte[] encryptedData = new byte[8];
		encrypt(plainData, encryptedData, key);
		byte[] decryptedData = new byte[8];
		decrypt(encryptedData, decryptedData, key);
		if (!Arrays.equals(plainData, decryptedData))
			throw new Exception("Decrypted data not equal.");
		System.out.println("ok");
	}

	private static void encrypt(byte[] input, byte[] output, byte[] key)
			throws Exception {
		crypt(Cipher.ENCRYPT_MODE, input, output, key);
	}

	private static void decrypt(byte[] input, byte[] output, byte[] key)
			throws Exception {
		crypt(Cipher.DECRYPT_MODE, input, output, key);
	}

	private static void crypt(int opmode, byte[] input, byte[] output,
			byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
		SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
		cipher.init(opmode, keySpec);
		cipher.doFinal(input, 0, input.length, output);
	}

}
