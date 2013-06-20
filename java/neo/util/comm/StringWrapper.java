package neo.util.comm;

/**
 * 	@Class Name	: 	StringWrapper.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
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
public class StringWrapper {

	private String mString;
	private int mIndex = 0;
	private int mUsefulLength;

	private boolean isUsefulChar(char c) {
		return 	(c >= 'A' && c <= 'Z') || 
					(c >= 'a' && c <= 'z')	|| 
					(c >= '0' && c <= '9')	|| 
					(c == '+') || 
					(c == '/');
	}

	public StringWrapper(String s) {
		mString = s;
		mUsefulLength = 0;
		int length = mString.length();
		for (int i = 0; i < length; i++) {
			if (isUsefulChar(mString.charAt(i)))
				mUsefulLength++;
		}
	}

	public int getUsefulLength() {
		return mUsefulLength;
	}

	public char getNextUsefulChar() {
		char result = '_';
		// start with a non-Base64 Character
		while (!isUsefulChar(result))
			result = mString.charAt(mIndex++);

		return result;
	}
}
