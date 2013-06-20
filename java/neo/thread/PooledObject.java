package neo.thread;

import java.util.Stack;

/**
 * 	@Class Name	: 	PooledObject.java
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
public class PooledObject {
	private static final int MAX_IDLE = 3; // 대기할 수 있는 최대 Object 수
	private static Stack POOL = new Stack();

	/**
	 * 객체를 풀에서 가져오거나 생성하여 반환.
	 */
	public static synchronized PooledObject getInstance() {
		PooledObject object;
		if (POOL.empty()) {
			object = new PooledObject(); // 풀이 비어있으면 새로 생성
		} else {
			object = (PooledObject) POOL.pop();
		}
		object.open(); // object 가 사용되어짐을 선언
		return object;
	}

	/**
	 * 사용을 마친 객체를 풀에 대기시킨다.
	 */
	private static synchronized void freeInstance(PooledObject object) {
		if (POOL.size() < MAX_IDLE)
			POOL.push(object);
	}

	public static int getPoolSize() {
		return POOL.size();
	}

	private boolean opened;

	// constructor 를 private 으로 하여 getInstance 로만 객체를 생성하도록 유도...
	private PooledObject() {
		this.opened = false;
	}

	private void open() {
		this.opened = true;
	}

	// 사용을 마친 Object 를 다시 사용하고자 한다면 IllegalStateException 발생..
	private void openCheck() {
		if (!this.opened)
			throw new IllegalStateException("PooledObject not opend.");
	}

	// 이 객체가 궁극적으로 하고 싶었던 일...
	public void execute(Object condition) {
		openCheck(); // 사용을 마친 Object 인지 확인을 위해...
		//System.out.println("execute:" + condition);
	}

	/**
	 * 객체 사용 마침.
	 */
	public void close() {
		this.opened = false;
		freeInstance(this);
	}
}
