package neo.util.excel;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.write.WritableCellFormat;
import jxl.write.WritableWorkbook;
import neo.util.excel.JxlUtil;

/**
 * 	@Class Name	: 	JxlClient.java
 * 	@파일설명		: 	
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
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
public class JxlClient {
	private static final JxlUtil util = new JxlUtil();

	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[2];
			args[0] = "default.xls";
			args[1] = "defaultSheet";
		}
		if (args.length == 1) {
			String[] tempArgs = new String[2];
			tempArgs[0] = args[0];
			tempArgs[1] = "defaultSheet";
			args = tempArgs;
		}
		// Create workbook and worksheet with default settings
		WritableWorkbook workBook = util.createWorkBook(args[0], args[1]);
		util.mergeCells(workBook.getSheet(args[1]), 0, 1, 4, 1);
		WritableCellFormat centerAlignedAllBoldformattedCell = util.createFormattedCell(10, null, true, false, null, null, null,	Alignment.CENTRE);
		WritableCellFormat bottomBoldFormatedCell = util.createFormattedCell(6, null, false, false, null, Border.BOTTOM, null);
		// Header
		util.addCellToSheet(0, 1, "Header", centerAlignedAllBoldformattedCell, workBook.getSheet(args[1])); // Column 1,2,3,4,5
		util.addCellToSheet(0, 2, "Subtitle1", bottomBoldFormatedCell, workBook.getSheet(args[1]));
		util.addCellToSheet(1, 2, "Subtitle2", bottomBoldFormatedCell, workBook.getSheet(args[1]));
		util.addCellToSheet(2, 2, "Subtitle3", bottomBoldFormatedCell, workBook.getSheet(args[1]));
		util.addCellToSheet(3, 2, "Subtitle4", bottomBoldFormatedCell, workBook.getSheet(args[1]));
		util.addCellToSheet(4, 2, "Subtitle5", bottomBoldFormatedCell, workBook.getSheet(args[1]));
		util.flush(workBook);
	}

}
