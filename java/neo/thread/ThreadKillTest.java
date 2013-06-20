package neo.thread;

import java.io.*;

/**
 * This is a simple command-line Java program that demonstrates
 * one way you can structure a multi-threaded program to kill
 * its threads properly when the program is ready to terminate.
 * 
 * version 1.0
 * Julian Robichaux ( http://www.nsftools.com )
 * 
 * @author Julian Robichaux ( http://www.nsftools.com )
 * @version 1.0
 */
/**
 * 	@Class Name	: 	ThreadKillTest.java
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
public class ThreadKillTest
{
	public static void main (String[] args)
	{
		TestThread t = new TestThread();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String userInput = "";
		
		t.start();
		while (!userInput.toLowerCase().equals("quit")) {
			try {
				System.out.println("*** Type 'quit' to stop this program ***");
				userInput = br.readLine();
			} catch (Exception e) {
				System.out.println(e);
				userInput = "quit";
			}
		}
		
		t.stopThread();
		while (t.isAlive()) {
			System.out.println("Waiting for thread to stop...");
			try { Thread.sleep(300); } catch (Exception sleepe) {}
		}
		
		System.out.println("Thread stopped. Exiting.");
	}
	
}
	
class TestThread extends Thread
{
	private boolean stopped = true;
	
	public void run ()
	{
		// you'll probably want to do a little more exception handling 
		// than this...
		stopped = false;
		while (!stopped) {
			System.out.println("The current time is " + new java.util.Date());
			try { sleep(5000); } catch (Exception sleepe) {}
		}
		cleanup();
	}
	
	public void stopThread ()
	{
		stopped = true;
		// calling interrupt() should throw an InterruptedException
		// if the run() method is in the middle of sleeping (which
		// it probably will be in this case if the Thread is currently 
		// running), which will break us out of the sleep
		interrupt();
	}
	
	private void cleanup()
	{
		// do any of your global cleanup processes here...
		
		// if you're really concerned about cleanup, at the end
		// of this routine you can also call things like:
		// System.runFinalization();
		// System.gc();
	}	
}
	

