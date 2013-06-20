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
 * 	@파일설명		: 	jxl 예제에 엑셀 파일 생성, 읽기 부분만 나와있고 기존 파일에 추가하는 방법이 안나와서,
 *						1번줄 부터 생성한 이유는 엑셀 파일 첫줄이 Select box 로 정렬 할 수 있게 해놨기 때문임.
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

		// 1열의 0번행에 ^^ 를 출력
		Label a = new Label(0, 1, "^^");

		// 1열의 1번행에 날짜 출력
		Label d = new Label(1, 1, sdf.format(Calendar.getInstance().getTime()));

		writeSheet.addCell(a);
		writeSheet.addCell(d);

		writeBook.write();
		writeBook.close();

		return newExcel;
	}

}
