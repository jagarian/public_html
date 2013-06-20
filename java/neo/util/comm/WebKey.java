package neo.util.comm;

/**
 * 	@Class Name	: 	WebKey.java
 * 	@���ϼ���		: 	UI / BO �ܿ��� ����ϴ� �Ϲ����� ���
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		:	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class WebKey {

	public static final String	SESSION_LISTENER_ID			= "_SESSION_LISTENER_0923_";//���Ǹ�����ID	

	/**
	 * @title	�α����� ����ڿ� ���� WebKey
	 */
	public static final String	SESSION_ID 					= "_SESSION_ID_"; 			// ����ID
	public static final String	SESSION_USER_ID 			= "_LOGIN_USER_ID_"; 		// �����id
	public static final String	SESSION_USER_NAME 			= "_LOGIN_USER_NAME_"; 		// ����ڸ� 
	public static final String	SESSION_USER_DEPT 			= "_LOGIN_USER_DEPT_";		// �μ�
	public static final String	SESSION_USER_EMAIL 			= "_LOGIN_USER_EMAIL_"; 	// �̸���
	public static final String	SESSION_USER_PWD 			= "_LOGIN_USER_PWD_"; 		// ��ȣ
	public static final String	SESSION_USER_ROLE			= "_LOGIN_USER_ROLE_";		// ��
	public static final String	SESSION_USER_LOGIN_CNT		= "_LOGIN_USER_LOGIN_CNT_";	// �α���Ƚ��
	public static final String	SESSION_LAST_LOGIN_DATE		= "_LOGIN_LAST_LOGIN_DATE_";// �ֱٷα�������
	public static final String	SESSION_LAST_LOGIN_IP		= "_LOGIN_LAST_LOGIN_IP_";	// �ֱٷα���IP
}
