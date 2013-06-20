package neo.util.excel;

import java.io.File; // ���� ������ ���ؼ� �ʿ�
import java.io.FileNotFoundException; // ���� ������ ���ؼ� �ʿ�
import java.io.IOException; // ���� ������ ���ؼ� �ʿ�

// �Ʒ��� jxl �κ��� ��Ŭ�������� ����Ͻñ� ���ؼ��� �޴��ٿ��� ������ ������Ʈ���� ������ Ŭ�� - Build Path - Add External

// jxl API�� �ٿ�޾Ƽ� ������ Ǭ ������ jxl.jar ������ ã�Ƽ� ���Խ��Ѿ� �ؿ�. ��.... �ݵ��.... ������....
// ������ ����Ѵٸ� ���� ������ ���������� ����. ����� ���� ������ Ǯ� �ҷ�����. ^^

import jxl.Workbook; // ���� ���� ������ ���� ó���� ���� �ش��ϴ� �⺻�� �Ǵ� �߻� Ŭ����

import jxl.write.WritableWorkbook; // ���� ���� ���� ������ ���� Workbook, Sheet�� �����ϴ� Swing�� Frame�� ���� �縷�� ���ƽý� �����ϴ� �߻� Ŭ����
import jxl.write.WritableSheet; // ��Ʈ�� �����ϴ� �������̽�
import jxl.write.WritableCellFormat; // ���� ���� ���� Ŭ����
import jxl.write.WriteException; // ���� ���� ������ �����ϱ� ����
import jxl.write.Label; // �� ���� Ŭ����
import jxl.write.Blank; // �� ���� Ŭ����
import jxl.format.*; // �� ����, ����, �÷� �κ� ���� Ŭ���� ����Ʈ

/**
 * 	@Class Name	: 	Jxl_Write.java
 * 	@���ϼ���		: 	
 * 	1. ��Ʈ ����
 *		WritableFont TitleFont = new WritableFont(WritableFont.ARIAL, 13,WritableFont.BOLD, false); 
 *		WritableCellFormat TitleCellFont = new WritableCellFormat (TitleFont); 
 *	2. align ����
 *		TitleCellFont.setAlignment(jxl.format.Alignment.CENTRE); // align
 *	3. Border ����
 *		TitleCellFont.setBorder(Border.ALL, BorderLineStyle.THIN); // border
 *	4. color ����
 *		TitleCellFont.setBackground(jxl.format.Colour.GOLD);  // color
 *	5. sheet ����
 *		a. sheet ����
 *			WritableSheet sheet = workbook.createSheet('��Ʈ�̸�", worksheet_idx++);
 *			// worksheet_idx 0,1,2,... ����
 *		b. sheet ��������
 *			WritableSheet sheet = workbook.getSheet("��Ʈ�̸�");
 *	6. cell ���ֱ�
 *		a. ����
 *			Label label = new Label(0, 0, "��",TitleCellFont);  // A0 �� ���ֱ�
 *			sheet.addCell(label); 
 *		b. ����
 *			Number number = new Number(col++, row, 123, TitleCellFont); 
 *			sheet.addCell(number);
 *	7. cell merge
 *		sheet.mergeCells(0, 1, 4, 2); // A1 ~ E2 �� ����
 *	8. cell columnũ��
 *		public static void setColumnWidth(int col, int new_size) {
 *			CellView cv = sheet.getColumnView(col);
 *			cv.setSize(new_size);
 *			// Yes, only if I give this it works:
 *			sheet.setColumnView(col, cv);
 *		}
 *	9. ����/����
 *		a. ����
 *			Workbook workbook = Workbook.createWorkbook(new File("sample.xls"));
 *		b. ����
 *			workbook.write(); 
 *			workbook.close(); 
 *			<td style='mso-number-format:"@";'>01112344567</td> //���� �ٿ�ε� �� '0001' ó�� ������ �ϱ�
 *			<td style='mso-number-format:"#,##0_'>123456789</td>
 *
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
public class Jxl_Write {

	public static void main(String args[]) throws FileNotFoundException,
													IOException, 
													WriteException { // ����ó���� �ƿ� ���ְ� �����ɴϴ�.

		WritableWorkbook myWorkbook = Workbook.createWorkbook(new File("jxl_Smile.xls")); // �����̸��� ���Ͽ� �����Ѵ�.

		WritableSheet mySheet = myWorkbook.createSheet("first sheet", 0); // WritableWorkbook���� �޼ҵ带 �̿��Ͽ� ����. 0��, �� ù��° ��Ʈ�� first sheet��� �̸����� �����Ѵ�.

		WritableCellFormat numberFormat = new WritableCellFormat(); // ��ȣ �� ���� ����
		WritableCellFormat nameFormat = new WritableCellFormat(); // �̸� �� ���� ����
		WritableCellFormat dataFormat = new WritableCellFormat(); // ������ �� ���� ����

		// ��ȣ ���̺� �� ���� ����(�ڼ��� ���� ��ũ �� API�� �����ϼ�) ������� : ���� �κп��� WriteException�� �߻��ϳ׿�.
		// �׷��� ��ܿ��� �̸� ���� ó���� �ؼ� ��� ���ڳ׿�.
		numberFormat.setAlignment(Alignment.CENTRE); // �� ��� ����
		numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // �� ���� ��� ����
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // ������ �������ν�Ÿ�� ����
		numberFormat.setBackground(Colour.BLUE); // ������ �´� ����

		// �̸� ���̺� �� ���� ����(�ڼ��� ���� ��ũ �� API�� �����ϼ�)
		nameFormat.setAlignment(Alignment.CENTRE); // �� ��� ����
		nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // �� ���� ��� ����
		nameFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR); // ������ �������ν�Ÿ�� ����
		nameFormat.setBackground(Colour.GOLD); // ������ �´� ����

		// ������ �� ���� ����
		dataFormat.setAlignment(Alignment.CENTRE); // �� ��� ����
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // �� ���� ��� ����

		// ��Ʈ�� �÷� ���� ����
		mySheet.setColumnView(0, 8); // ��Ʈ�� ��ȣ �÷�(0��°)�� ���� ����. setCloumnView(���° �÷�, ����)
		mySheet.setColumnView(1, 15); // ��Ʈ�� �̸� �÷�(1��°)�� ���� ����
		mySheet.setColumnView(2, 20); // ��Ʈ�� ��� �÷�(2��°)�� ���� ����

		// ���� �̿��Ͽ� �ش� ���� ���� �ֱ� ����
		Label numberLabel = new Label(0, 0, "�й�", numberFormat); // �й� ��(��,��,"����",����)
		mySheet.addCell(numberLabel); // ��Ʈ�� addCell �޼ҵ带 ����Ͽ� ����

		Label nameLabel = new Label(1, 0, "����", nameFormat); // ���� ��(��,��,"����",����)
		mySheet.addCell(nameLabel); // ��Ʈ�� addCell �޼ҵ带 ����Ͽ� ����

		// Blank blank = new Blank(2, 0, numberFormat); // �� ��(��,��,����) -- �ʿ� �� �ּ� ó�� Ǯ�� ����ϼ�.
		// sheet.addCell(blank);

		Label postScript = new Label(2, 0, "���", nameFormat); // ��� ��(��,��,"����",����)
		mySheet.addCell(postScript); // ��Ʈ�� addCell �޼ҵ带 ����Ͽ� ����

		for (int no = 1; no < 6; no++) {
			Label numberLabels = new Label(0, no, "[" + no + "]", dataFormat); // ������ ���信 �°� 1���� 5���� ��ȣ����
			mySheet.addCell(numberLabels); // ���� ����

			Label nameLabels = new Label(1, no, (char) (no + 64) + "", dataFormat); // ������ ���信 �°� �빮�� A���� E���� ����
			mySheet.addCell(nameLabels); // ���� ����
		}
		
		myWorkbook.write(); // �غ�� ������ ���� ���信 �°� ����
		myWorkbook.close(); // ó�� �� �޸𸮿��� ���� ó��
	}
}
