package neo.util.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 	@Class Name	: 	CompileRun.java
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
public class CompileRun {
	/***************************************************************************
	 * Complies the file at location "filename" with the java compiler
	 * 
	 * @param filename
	 * @return true when no errors in compiling
	 */
	public static boolean javaCompiler(String filename) {

		System.out.println("compiling : " + filename);
		
		try {

			Process p = Runtime.getRuntime().exec("C:\\Program Files\\Java\\jdk1.5.0_07\\bin\\javac.exe " + filename);

			BufferedReader stdInput = new BufferedReader(
											new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(
											new InputStreamReader(p.getErrorStream()));

			// read the output from the command
			String s = null;
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			
		} catch (Exception e) {
			System.err.println("There was an error compiling " + filename + " : " + e);
			return false;
		}

		return true;
	}

	/**
	 * Runs the java file at location "filename" with java
	 * 
	 * @param filename
	 * @return true when no errors in running
	 */
	public static boolean javaRunner(String filename, String input) {
		System.out.println("     running : " + filename);
		filename = filename.substring(0, filename.length() - 5);
		// String data = ReadFile.read(input);

		try {
			File directory = new File("C:\\Users\\Jay\\Desktop\\l2\\lab2submit\\team1\\problem1\\run-2007_04_30-00_33_37\\");
			Process p = Runtime.getRuntime().exec("C:\\Program Files\\Java\\jdk1.5.0_07\\bin\\java.exe " + "Read", null, directory);// +" "+data);
			String line;
			System.out.println("C:\\Program Files\\Java\\jdk1.5.0_07\\bin\\java.exe " + filename);

			// File reader
			FileReader readFile = new FileReader(input);
			BufferedReader bufRead = new BufferedReader(readFile);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			// Print input
			while ((line = bufRead.readLine()) != null) {
				// need to find a way to write to input
				out.write(line);
				// System.out.println(line);
				String s = null;
				System.out.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				// read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			}
			// read the output from the command
			// while ((line = bufRead.readLine()) != null) {
			// 		System.out.println(line);
			// }

			bufRead.close();

		} catch (ArrayIndexOutOfBoundsException e) {
			/*
			 * If no file was passed on the command line, this expception is
			 * generated. A message indicating how to the class should be called
			 * is displayed
			 */
			System.out.println("Usage: java ReadFile filename\n");

		} catch (IOException e) {
			// If another exception is generated, print a stack trace
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
