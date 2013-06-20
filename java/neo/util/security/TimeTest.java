package neo.util.security;

import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.initech.provider.crypto.rsa.*;
import com.initech.provider.crypto.InitechProvider;

/**
 * 	@Class Name	: 	TimeTest.java
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
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class TimeTest {

	static {
		InitechProvider.addAsProvider();
	}

	private SecretKey key;
	private PublicKey pubKey;
	private PrivateKey privKey;
	Cipher cipher, cipher2;

	public void generateKey() throws Exception {
		cipher = Cipher.getInstance("SEED", "Initech");
		cipher2 = Cipher.getInstance("RSA");
		KeyGenerator keyGenerator = KeyGenerator.getInstance("SEED", "Initech");
		key = keyGenerator.generateKey();
	}

	public byte[] seedEnc(byte[] pt) throws Exception {

		cipher.init(Cipher.ENCRYPT_MODE, key);

		return cipher.doFinal(pt);
	}

	public void seedDec(byte[] ct) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key);

		cipher.doFinal(ct);
	}

	public void generateRsaKey() throws Exception {
		SecureRandom random = new SecureRandom();

		// RAND �ӵ� ���� (200byte)
		byte[] r = new byte[200];
		long start = System.nanoTime();
		for (int i = 0; i < 100; i++)
			random.nextBytes(r);
		long end = System.nanoTime();
		System.out.println("RAND: " + (end - start) / 100000 + "��");

		RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
		keyGen.initialize(1024, random);
		KeyPair kp = keyGen.generateKeyPair();

		KeyFactory kf = KeyFactory.getInstance("RSA", "Initech");

		RSAPublicKeySpec pukeySpec = (RSAPublicKeySpec) kf.getKeySpec(kp
				.getPublic(), RSAPublicKeySpec.class);
		RSAPrivateKeySpec prikeySpec = (RSAPrivateKeySpec) kf.getKeySpec(kp
				.getPrivate(), RSAPrivateKeySpec.class);
		pubKey = kf.generatePublic(pukeySpec);
		privKey = kf.generatePrivate(prikeySpec);
	}

	public byte[] rsaEnc(byte[] pt) throws Exception {
		cipher2.init(Cipher.ENCRYPT_MODE, pubKey);

		return cipher2.doFinal(pt);
	}

	public void rsaDec(byte[] ct) throws Exception {
		cipher2.init(Cipher.DECRYPT_MODE, privKey);
		cipher2.doFinal(ct);
	}

	public static void main(String[] args) throws Exception {
		long start, end;
		byte[] ct = null;
		TimeTest tt = new TimeTest();
		String str = "", str2 = "", str3 = "";

		// ��/��ȣȭ�� ���� ������ ����
		for (int i = 0; i < 200; i++)
			str += "1";
		for (int i = 0; i < 16; i++)
			str2 += "2";
		for (int i = 0; i < 64; i++)
			str3 += "3";

		// Ű ���� (Ű ���� �ð��� ��/��ȣȭ �ð����� ����)
		tt.generateKey();
		tt.generateRsaKey();

		// SEED ��ȣȭ �ӵ� ���� (200byte)
		start = System.nanoTime();

		for (int i = 0; i < 100; i++)
			ct = tt.seedEnc(str.getBytes());
		end = System.nanoTime();

		System.out.println("SEDD��ȣȭ: " + (end - start) / 100000 + "��");

		// SEED ��ȣȭ �ӵ� ���� (200byte)
		start = System.nanoTime();
		for (int i = 0; i < 100; i++)
			tt.seedDec(ct);
		end = System.nanoTime();
		System.out.println("SEDD��ȣȭ: " + (end - start) / 100000 + "��");

		// ����Ű ��ȣȭ �ӵ� ���� (Ű ������ : 1024, ������ : 16byte)
		start = System.nanoTime();
		for (int i = 0; i < 100; i++)
			ct = tt.rsaEnc(str2.getBytes());
		end = System.nanoTime();
		System.out.println("RSA��ȣȭ: " + (end - start) / 100000 + "��");

		ct = tt.rsaEnc(str3.getBytes());

		// ����Ű ��ȣȭ �ӵ� ���� (128byte)
		start = System.nanoTime();
		for (int i = 0; i < 100; i++)
			tt.rsaDec(ct);
		end = System.nanoTime();
		System.out.println("RSA��ȣȭ: " + (end - start) / 100000 + "��");

	}

}
