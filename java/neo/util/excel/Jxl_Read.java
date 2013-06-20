package neo.util.excel;

import java.io.*; // ���� �Է� ���� Ŭ����
import jxl.*; // jxl ���� Ŭ����

/**
 * 	@Class Name	: 	Jxl_Read.java
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
public class Jxl_Read {
	public static void main(String args[]) throws FileNotFoundException,	
																		IOException, 
																		jxl.read.biff.BiffException { // �̹����� ���ϰ� ���� ���� �Է� ���� ���� ������ �̸� ���ְ� ���ɴϴ�.

		Workbook myWorkbook = Workbook.getWorkbook(new File("jxl_Smile.xls")); // ������ �о� �ͼ�...
		Sheet mySheet = myWorkbook.getSheet(0); // ��Ȯ�� ��Ʈ�� �Է� �޾Ƽ�...

		System.out.print("�й�\t����\t���\t\n"); // ���� ������ ���� �ְ�

		for (int no = 1; no < 6; no++) { // ���� ���� ��ŭ ������
			for (int i = 0; i < 3; i++) { // ���� ���� ��ŭ ������
				Cell myCell = mySheet.getCell(i, no); // ���� ��� ���� ������ ������ ��...
				System.out.print(myCell.getContents() + "\t"); // ���� �Ÿ� ��ŭ ���� ���� �ϰ�...
				// getContents()�޼ҵ忡 ����
				// Quick and dirty function to return the contents of this cell as a string. �̶�� API�� ���� �ִ�����.
			}
			System.out.println(); // ���� �ٲ� �� ���� �����Ͽ� ����� ����.
		}
	}
}
