package neo.thread;

import java.util.*;

/**
 * 	@Class Name	: 	BlockingQueue.java
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
public class BlockingQueue {
	private Vector elements = new Vector();

	private boolean closed = false;

	public class Closed extends RuntimeException {
		private Closed() {
			super("Tried to access closed BlockingQueue");
		}
	}

	public synchronized final void enqueue(Object new_element)
		throws BlockingQueue.Closed {
		if (closed)
			throw new Closed();
		elements.addElement(new_element);
		notify();
	}

	public synchronized final Object dequeue()
		throws InterruptedException, BlockingQueue.Closed {
		try {
			while (elements.size() <= 0) {
				wait();
				if (closed)
					throw new Closed();
			}

			Object ob = (Object) elements.firstElement();
			elements.removeElementAt(0);
			return ob;
		} catch (NoSuchElementException e) { // Shouldn't happen
			throw new Error("Internal error (neo.thread.BlockingQueue)");
		}
	}

	public synchronized final boolean is_empty() {
		return elements.size() > 0;
	}

	public synchronized void close() {
		closed = true;
		notifyAll();
	}
}
