package neo.thread;

/**
 * 	@Class Name	: 	ThreadPool.java
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
public class ThreadPool extends ThreadGroup {
	private final BlockingQueue pool = new BlockingQueue();

	private int maximum_size;

	private int pool_size;

	private boolean has_closed = false;

	private static int group_number = 0;

	private static int thread_id = 0;

	private class Pooled_thread extends Thread {

		public Pooled_thread() {
			super(ThreadPool.this, "T" + thread_id);
		}

		public void run() {
			try {
				while (!has_closed) {
					((Runnable) (pool.dequeue())).run();
				}
			} catch (InterruptedException e) {
				/* ignore it, stop thread */
			} catch (BlockingQueue.Closed e) {
				/* ignore it, stop thread */
			}
		}
	}

	public ThreadPool(int initial_thread_count, int maximum_thread_count) {
		super("ThreadPool" + group_number++);
		maximum_size = (maximum_thread_count > 0)	? maximum_thread_count : Integer.MAX_VALUE;
		pool_size = Math.min(initial_thread_count, maximum_size);
		for (int i = pool_size; --i >= 0;)
			new Pooled_thread().start();
	}

	public ThreadPool() {
		super("ThreadPool" + group_number++);
		this.maximum_size = 0;
	}

	public synchronized void execute(Runnable action) throws Closed {
		if (has_closed)
			throw new Closed();
		if (pool_size < maximum_size)
			synchronized (pool) {
				if (pool.is_empty()) {
					++pool_size;
					new Pooled_thread().start(); // Add thread to pool
				}
			}
		pool.enqueue(action); // Attach action to it.
	}

	public class Closed extends RuntimeException {
		Closed() {
			super("Tried to execute operation on a closed ThreadPool");
		}
	}

	public synchronized void close() {
		has_closed = true;
		pool.close(); // release all waiting threads
	}
}
