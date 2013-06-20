package neo.util.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 	@Class Name	: 	JxlEdit.java
 * 	@���ϼ���		: 	jxl ������ ���� ���� ����, �б� �κи� �����ְ� ���� ���Ͽ� �߰��ϴ� ����� �ȳ��ͼ�,
 *						1���� ���� ������ ������ ���� ���� ù���� Select box �� ���� �� �� �ְ� �س��� ������.
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
public class JxlEdit {

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public File getExcelToOrder() throws Exception {
		File dir = new File("d:/springStudy/JxlTest/src/com/javarush/");
		File f = new File(dir, "excel_default.xls");

		if (!f.exists()) {
			throw new Exception("file not found");
		}

		if (!f.canRead()) {
			throw new Exception("can't read file");
		}

		Workbook workbook = Workbook.getWorkbook(f);

		if (workbook == null) {
			throw new Exception("Workbook is null!!");
		}

		File newExcel = new File(dir, System.currentTimeMillis() + ".xls");

		WritableWorkbook writeBook = Workbook
				.createWorkbook(newExcel, workbook);

		WritableSheet writeSheet = writeBook.getSheet(0);

		// 1���� 0���࿡ ^^ �� ���
		Label a = new Label(0, 1, "^^");

		// 1���� 1���࿡ ��¥ ���
		Label d = new Label(1, 1, sdf.format(Calendar.getInstance().getTime()));

		writeSheet.addCell(a);
		writeSheet.addCell(d);

		writeBook.write();
		writeBook.close();

		return newExcel;
	}

}
