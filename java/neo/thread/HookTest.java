package neo.thread;

/**
 * 	@Class Name	: 	HookTest.java
 * 	@파일설명		: 	강제 종료시 run() 이 실행됨 
 * 	- Ctr + C - kill <preocess_id> 
 * 	- -9 와 같이 강제 종료 옵션을 사용하는 경우 동작하지 않음
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
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
public class HookTest extends Thread {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new HookTest());
		// System.exit(-1);
	}

	public void run() {
		System.out.println("~~~~~~~~~~~Shut Down Hook ~~~~~~~~~~~~~");
	}
};
