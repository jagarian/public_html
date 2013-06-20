package neo.thread;

import java.util.Stack;

/**
 * 	@Class Name	: 	PooledObject.java
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
public class PooledObject {
	private static final int MAX_IDLE = 3; // ����� �� �ִ� �ִ� Object ��
	private static Stack POOL = new Stack();

	/**
	 * ��ü�� Ǯ���� �������ų� �����Ͽ� ��ȯ.
	 */
	public static synchronized PooledObject getInstance() {
		PooledObject object;
		if (POOL.empty()) {
			object = new PooledObject(); // Ǯ�� ��������� ���� ����
		} else {
			object = (PooledObject) POOL.pop();
		}
		object.open(); // object �� ���Ǿ����� ����
		return object;
	}

	/**
	 * ����� ��ģ ��ü�� Ǯ�� ����Ų��.
	 */
	private static synchronized void freeInstance(PooledObject object) {
		if (POOL.size() < MAX_IDLE)
			POOL.push(object);
	}

	public static int getPoolSize() {
		return POOL.size();
	}

	private boolean opened;

	// constructor �� private ���� �Ͽ� getInstance �θ� ��ü�� �����ϵ��� ����...
	private PooledObject() {
		this.opened = false;
	}

	private void open() {
		this.opened = true;
	}

	// ����� ��ģ Object �� �ٽ� ����ϰ��� �Ѵٸ� IllegalStateException �߻�..
	private void openCheck() {
		if (!this.opened)
			throw new IllegalStateException("PooledObject not opend.");
	}

	// �� ��ü�� �ñ������� �ϰ� �;��� ��...
	public void execute(Object condition) {
		openCheck(); // ����� ��ģ Object ���� Ȯ���� ����...
		//System.out.println("execute:" + condition);
	}

	/**
	 * ��ü ��� ��ħ.
	 */
	public void close() {
		this.opened = false;
		freeInstance(this);
	}
}
