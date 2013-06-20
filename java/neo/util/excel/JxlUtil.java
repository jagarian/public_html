package neo.util.excel;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 	@Class Name	: 	JxlUtil.java
 * 	@ÆÄÀÏ¼³¸í		: 	I am using JXL 2.6.8 release with JDK 5 update 15. 
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	ÀÛ¾÷ÀÏ 		¹öÁ¯	±¸ºÐ	ÀÛ¾÷ÀÚ		³»¿ë
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		»ý¼º	hoon09		source create (»ï¼ºÀü±â)
 *	2006-11-23 	1.4		¼öÁ¤	hoon09		code convention apply (¸ÖÆ¼Ä·ÆÛ½º)
 *	2009-07-03	1.6		¼öÁ¤	hoon09		code convention apply (±¹¹ÎÀºÇà, ÆæÅ¸½ÃÅ¥¸®Æ¼)
 *	2009-09-23	1.7		¼öÁ¤	hoon09		code valid check (Çª¸£µ§¼£»ý¸íº¸Çè,¹ð¹ð)
 **********************************************************************************************             
 */
public class JxlUtil {
	/**
	 * Class to define a formula.
	 * 
	 * @author sacrosanctblood
	 * 
	 */
	public static class Formulae {
		private String formulae;

		public String getFormulae() {
			return formulae;
		}

		public void setFormulae(String formulae) {
			this.formulae = formulae;
		}
	}

	/**
	 * RuntimeException to wrap all the checked exceptions of JXL
	 * 
	 * @author sacrosanctblood
	 * 
	 */
	public static class JxlUtilException extends RuntimeException {
		private static final long serialVersionUID = -1189965636139763776L;
		private Exception _ex;

		public JxlUtilException(Exception ex) {
			super(ex);
			this._ex = ex;
		}

		public Exception getActualJExcelException() {
			return _ex;
		}
	}

	/**
	 * Creates a WritableWorkBook from the file given.
	 * 
	 * @param name
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(File name) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(name);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		return workbook;
	}

	/**
	 * Creates a WritableWorkbook with a sheet given by sheet name.
	 * 
	 * @param name
	 * @param sheetName
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(File name, String sheetName) {
		WritableWorkbook workBook;
		try {
			workBook = Workbook.createWorkbook(name);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		createSheet(sheetName, workBook);
		return workBook;
	}

	/**
	 * Creates a WritableWorkbook from file, with the settings given.
	 * 
	 * @param name
	 * @param setting
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(File name, WorkbookSettings setting) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(name, setting);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		return workbook;
	}

	/**
	 * Creates a WritableWorkbook from file, with the settings given and
	 * sheetname given.
	 * 
	 * @param name
	 * @param sheetName
	 * @param setting
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(File name, String sheetName,
			WorkbookSettings setting) {
		WritableWorkbook workBook;
		try {
			workBook = Workbook.createWorkbook(name, setting);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		createSheet(sheetName, workBook);
		return workBook;
	}

	/**
	 * Creates a WritableWorkbook from file name, with the settings given.
	 * 
	 * @param name
	 * @param setting
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(String name, WorkbookSettings setting) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(new File(name), setting);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		return workbook;
	}

	/**
	 * Creates a WritableWorkbook from file name, with the settings given and
	 * sheetname given.
	 * 
	 * @param name
	 * @param sheetName
	 * @param setting
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(String name, String sheetName,
			WorkbookSettings setting) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(new File(name), setting);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		createSheet(sheetName, workbook);
		return workbook;
	}

	/**
	 * Creates a WritableWorkbook from file name and sheet name given.
	 * 
	 * @param name
	 * @param sheetName
	 * @return WritableWorkbook
	 */
	public WritableWorkbook createWorkBook(String name, String sheetName) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(new File(name));
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		createSheet(sheetName, workbook);
		return workbook;
	}

	/**
	 * Creates settings with US locale.
	 * 
	 * @return WorkbookSettings
	 */
	public WorkbookSettings createSettings() {
		WorkbookSettings wbSettings = new WorkbookSettings();
		Locale locale = Locale.US;
		wbSettings.setLocale(locale);
		return wbSettings;
	}

	/**
	 * Creates sheet in the workbook with given name.
	 * 
	 * @param sheetName
	 * @param workBook
	 */
	public void createSheet(String sheetName, WritableWorkbook workBook) {
		workBook.createSheet(sheetName, workBook.getNumberOfSheets());
	}

	/**
	 * Creates a cell format.
	 * 
	 * @param pointSize
	 * @param fontName
	 *            Can be null, default : Times
	 * @param isBold
	 * @param italic
	 * @param underLineStyle
	 *            Can be null, default : No Underline
	 * @return WritableCellFormat
	 */
	public WritableCellFormat createFormattedCell(int pointSize,
			jxl.write.WritableFont.FontName fontName, boolean isBold,
			boolean italic, UnderlineStyle underLineStyle) {

		WritableFont font = new WritableFont(null != fontName ? fontName
				: jxl.write.WritableFont.TIMES,

		pointSize,

		isBold ? WritableFont.BOLD : WritableFont.NO_BOLD,

		italic,

		null != underLineStyle ? underLineStyle : UnderlineStyle.NO_UNDERLINE

		);
		WritableCellFormat writableCellFormat = new WritableCellFormat(font);
		return writableCellFormat;
	}

	/**
	 * Creates a cell format.
	 * 
	 * @param pointSize
	 * @param fontName
	 *            Can be null, default : Times
	 * @param isBold
	 * @param italic
	 * @param underLineStyle
	 *            Can be null, default : No Underline
	 * @param border
	 *            Can be null, default : ALL
	 * @param lineStyle
	 *            Can be null, default : THICK
	 * @return WritableCellFormat
	 */
	public WritableCellFormat createFormattedCell(int pointSize,
			jxl.write.WritableFont.FontName fontName, boolean isBold,
			boolean italic, UnderlineStyle underLineStyle, Border border,
			BorderLineStyle lineStyle) {
		WritableFont font = new WritableFont(null != fontName ? fontName
				: jxl.write.WritableFont.TIMES, pointSize,
				isBold ? WritableFont.BOLD : WritableFont.NO_BOLD, italic,
				null != underLineStyle ? underLineStyle
						: UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat writableCellFormat = new WritableCellFormat(font);
		if (null == lineStyle) {
			lineStyle = BorderLineStyle.THICK;
		}
		if (null == border) {
			border = Border.ALL;
		}
		try {
			writableCellFormat.setBorder(border, lineStyle);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
		return writableCellFormat;
	}

	/**
	 * Creates a cell format.
	 * 
	 * @param pointSize
	 * @param fontName
	 *            Can be null, default : Times
	 * @param isBold
	 * @param italic
	 * @param underLineStyle
	 *            Can be null, default : No Underline
	 * @param border
	 *            Can be null, default : ALL
	 * @param lineStyle
	 *            Can be null, default : THICK
	 * @param alignment
	 *            Can be null, default : CENTRE
	 * @return WritableCellFormat
	 */
	public WritableCellFormat createFormattedCell(int pointSize,
			jxl.write.WritableFont.FontName fontName, boolean isBold,
			boolean italic, UnderlineStyle underLineStyle, Border border,
			BorderLineStyle lineStyle, Alignment alignment) {
		WritableFont font = new WritableFont(null != fontName ? fontName
				: jxl.write.WritableFont.TIMES, pointSize,
				isBold ? WritableFont.BOLD : WritableFont.NO_BOLD, italic,
				null != underLineStyle ? underLineStyle
						: UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat writableCellFormat = new WritableCellFormat(font);
		if (null == lineStyle) {
			lineStyle = BorderLineStyle.THICK;
		}
		if (null == border) {
			border = Border.ALL;
		}
		if (null == alignment) {
			alignment = Alignment.CENTRE;
		}
		try {
			writableCellFormat.setBorder(border, lineStyle);
			writableCellFormat.setAlignment(alignment);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
		return writableCellFormat;
	}

	/**
	 * Adds the Label cell to sheet at given row and column.
	 * 
	 * @param column
	 * @param row
	 * @param data
	 * @param format
	 *            Can be null
	 * @param sheet
	 */
	public void addCellToSheet(int column, int row, String data,
			WritableCellFormat format, WritableSheet sheet) {
		try {
			if (null != format) {
				sheet.addCell(new Label(column, row, data, format));
			} else {
				sheet.addCell(new Label(column, row, data));
			}
		} catch (RowsExceededException e) {
			throw new JxlUtilException(e);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}

	}

	/**
	 * Adds Integer cell to sheet at given row and column.
	 * 
	 * @param column
	 * @param row
	 * @param data
	 * @param format
	 *            Can be null.
	 * @param sheet
	 */
	public void addCellToSheet(int column, int row, Integer data,
			WritableCellFormat format, WritableSheet sheet) {
		try {
			if (null != format) {
				sheet.addCell(new jxl.write.Number(column, row, data, format));
			} else {
				sheet.addCell(new jxl.write.Number(column, row, data));
			}
		} catch (RowsExceededException e) {
			throw new JxlUtilException(e);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
	}

	/**
	 * Adds a formula to sheet at given row and column.
	 * 
	 * @param column
	 * @param row
	 * @param data
	 * @param format
	 *            Can be null.
	 * @param sheet
	 */
	public void addCellToSheet(int column, int row, Formulae data,
			WritableCellFormat format, WritableSheet sheet) {
		try {
			if (null != format) {
				sheet.addCell(new Formula(column, row, data.getFormulae(),
						format));
			} else {
				sheet.addCell(new Formula(column, row, data.getFormulae()));
			}
		} catch (RowsExceededException e) {
			throw new JxlUtilException(e);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
	}

	/**
	 * Merges cell between two ranges([col1,row1] to [col2,row2])
	 * 
	 * @param sheet
	 * @param col1
	 * @param row1
	 * @param col2
	 * @param row2
	 * @return Range
	 */
	public Range mergeCells(WritableSheet sheet, int col1, int row1, int col2,
			int row2) {
		try {
			return sheet.mergeCells(col1, row1, col2, row2);
		} catch (RowsExceededException e) {
			throw new JxlUtilException(e);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
	}

	/**
	 * Finder on data to retrieve a cell.
	 * 
	 * @param sheet
	 * @param data
	 * @param isLabelCell
	 * @return Cell
	 */
	public Cell find(Sheet sheet, String data, boolean isLabelCell) {
		return isLabelCell ? sheet.findLabelCell(data) : sheet.findCell(data);
	}

	/**
	 * Gets the content of Cell a given row and column.
	 * 
	 * @param sheet
	 * @param col
	 * @param row
	 * @return String
	 */
	public String getCellContents(Sheet sheet, int col, int row) {
		Cell componentCell = sheet.getCell(col, row);
		return componentCell.getContents();
	}

	/**
	 * Set the Wrap property to true.
	 * 
	 * @param format
	 */
	public void setWrapTrue(WritableCellFormat format) {
		try {
			format.setWrap(true);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
	}

	/**
	 * Set the Wrap property to false.
	 * 
	 * @param format
	 */
	public void setWrapFalse(WritableCellFormat format) {
		try {
			format.setWrap(false);
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		}
	}

	/**
	 * Flushes the buffer, by writing the data to file and closing the workbook.
	 * 
	 * @param book
	 */
	public void flush(WritableWorkbook book) {
		try {
			book.write();
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
		try {
			book.close();
		} catch (WriteException e) {
			throw new JxlUtilException(e);
		} catch (IOException e) {
			throw new JxlUtilException(e);
		}
	}
	
	/**
	 * jxl(¿¢¼¿read)Áß ºó¼¿À» ¸¸³ª¸é null ¿¡·¯Ã³¸® ¹æ¹ý
	 * 
	 * @param str
	 */
	public String checkNull(String str)
	{ 
		String strTmp; 
		if (str == null) 
			strTmp = ""; 
		else 
			strTmp = str; 
		return strTmp; 
	} 


}
