package neo.util.security;   

import java.io.IOException;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.Key;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.SecretKeyFactory;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	UtilCrypto.java
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
public class UtilCrypto {
	
	public native static String des(String msg, String type);
	public native static String getKey(String type);
	
	private static Key keyneo = null; 
	
	private static Cipher cipher = null;
	private static DESedeKeySpec kspec = null;
	private static SecretKeyFactory skf= null;		
	
	private static String seedKey = "7CD9F0A8E5446FBF";	
	
	static {	
		System.loadLibrary("des");
		try {
			cipher = Cipher.getInstance("DESede");
			skf= SecretKeyFactory.getInstance("DESede");
			setKey();
		} catch (Exception e) {
			Log.info(e.toString());
		} 	
	}
	
	/************************ START HASH  ******************************/	 
	 
	public static String encryptSHA(String inputValue) throws Exception {
		if( inputValue == null ) 
			throw new Exception("Can't conver to Message Digest 5 String value!!");
			
		MessageDigest md = MessageDigest.getInstance("SHA"); //step 2
		//md.update(inputValue.getBytes("UTF-8")); //step 3
		//byte raw[] = md.digest();
		
		byte raw[] = md.digest(inputValue.getBytes()); //step 4
		return (new BASE64Encoder()).encode(raw); //step 5
	}
  
	public static String encryptMD5(String inputValue) throws Exception {
		if( inputValue == null ) 
			throw new Exception("Can't conver to Message Digest 5 String value!!");			
		
		if (inputValue.length() < 1) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			byte[] raw =  md.digest(inputValue.getBytes());	
			return (new BASE64Encoder()).encode(raw); 
		}
		else {
			cipher.init(Cipher.ENCRYPT_MODE, keyneo);
			return (new BASE64Encoder()).encode(cipher.doFinal(inputValue.getBytes()));
		}
	}
	
	/************************ END HASH  *******************************/




	/************************ START 3DES  ******************************/	
		
	/*
	public static String getKey() {
		return String.valueOf(key.getEncoded());
	}
	*/
	
	public static String encrypt3DES(String input, String systemType)	
		throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		
		if (systemType.equals("neo"))
			cipher.init(Cipher.ENCRYPT_MODE, keyneo);
		return (new BASE64Encoder()).encode(cipher.doFinal(input.getBytes()));
	}
	
	public static String decrypt3DES(String input, String systemType)
		throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
		
		byte[] encryptionBytes = new sun.misc.BASE64Decoder().decodeBuffer(input);

		if (systemType.equals("neo"))
			cipher.init(Cipher.DECRYPT_MODE, keyneo);
		return 	new String(cipher.doFinal(encryptionBytes));
	}

    public static void setKey() throws InvalidKeyException, InvalidKeySpecException{
    	
    	String tmp = null;
		tmp = des(seedKey, "Enc");
		kspec = new DESedeKeySpec(tmp.getBytes());
		keyneo = skf.generateSecret(kspec);
	}
    
    /************************ END 3DES  ******************************/
	
	/* TEST */	
    public static void main(String args[]) throws Exception {
    	 String strResult = null;
    	 
    	 /* 성능 테스트*/
    	 /*
    	 Calendar c = Calendar.getInstance();
    	 Log.info("start time : " +c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND) );
    	 for (int i=0; i < 1000 ; i++) {
	    	 	strResult = UtilCrypto.encrypt3DES("abcdefefghijklmnop"+String.valueOf(i), "neo");
	    	 	strResult = UtilCrypto.decrypt3DES(strResult, "neo");	    
	    	 //Log.info(i + " : " + strResult);	 
	     }	     
    	 	
         c = Calendar.getInstance();
    	 Log.info("end   time : " +c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND) );        
	     */
	     if (args[0].equals("enc")) {
	     	Log.info("Encode : " + UtilCrypto.encrypt3DES(args[1],"neo"));
	     	Log.info("Encode : " + UtilCrypto.encryptMD5(args[1]));
	     }
	     else {
	     	Log.info("Decode : " + UtilCrypto.decrypt3DES(args[1],"neo"));
	     }
    }
    
    //======================================================================================
    //======================================================================================
    //======================================================================================
    
    public static String BASE64Encoder2(byte[] plainText)
    {
        String b64enc="";
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        b64enc  = encoder.encode(plainText);
        return b64enc;
    }

    public static byte[] BASE64Decoder2(String b64encoded)
    {
        byte[] plainText = null;
        try
        {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            plainText = decoder.decodeBuffer(b64encoded);
        }
        catch (IOException ex) {
        }
        return plainText;
    }


    
    public static void main2(String[] args) {
        String str="123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        String b64encoded=null;
        byte[] b64decoded=null;

        System.out.println("Plain Text : ["+str+"]");

        b64encoded = BASE64Encoder2(str.getBytes());
        System.out.println("BASE 64 encoded : ["+b64encoded+"]");

        b64decoded = BASE64Decoder2(b64encoded);
        System.out.println("BASE 64 decoded : ["+new String(b64decoded)+"]");
    }
}
