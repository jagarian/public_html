package neo.thread;

/**
 * 	@Class Name	: 	HookTest.java
 * 	@���ϼ���		: 	���� ����� run() �� ����� 
 * 	- Ctr + C - kill <preocess_id> 
 * 	- -9 �� ���� ���� ���� �ɼ��� ����ϴ� ��� �������� ����
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
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
public class HookTest extends Thread {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new HookTest());
		// System.exit(-1);
	}

	public void run() {
		System.out.println("~~~~~~~~~~~Shut Down Hook ~~~~~~~~~~~~~");
	}
};
