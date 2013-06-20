package neo.util.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * 	@Class Name	: 	ExcelFormatAdapter.java
 * 	@파일설명		: 	http://poi.apache.org/hssf/quick-guide.html#Autofit
 * 						http://poi.apache.org/apidocs/index.html
 * 						http://blog.naver.com/btchae?Redirect=Log&logNo=80005156571
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
public class ExcelFormatAdapter {
  private HSSFWorkbook wb;
  private HSSFSheet [] sheets;

  private final int ROW_POSITION = 1;
  private final int COL_POSITION = 1;

  public ExcelFormatAdapter(String[] sheetNames, String title, String author, String[] header, int [] length) {
    wb = new HSSFWorkbook();
    sheets = new HSSFSheet[sheetNames.length];
    for(int index=0; index < sheetNames.length; index++) {
      sheets[index] = wb.createSheet(sheetNames[index]);
      makeHeader(index, title, author, header, length);
    }
  }

  public void makeTailCell(int sheetIndex, int rowIndex, int valueCount, String currentDate) {
    int dataRowIndex = ROW_POSITION + 3 + rowIndex;
    HSSFRow row = sheets[sheetIndex].createRow(dataRowIndex);

    // 데이터 를 입력한다.
    for(int index=0; index < valueCount; index++) {
      if(index == valueCount -1) {
        createTailCell(row, COL_POSITION + index, "출력일자 : " + currentDate);
      } else {
        createTailCell(row, COL_POSITION + index, "");
      }
    }
  }


  /**
   * 마지막에 출력할 출력일자 셀을 설정한다.
   * @param row
   * @param column
   * @param value
   */
  private void createTailCell(HSSFRow row, int column, String value) {
    HSSFCell cell = row.createCell((short)column);
    HSSFRichTextString richValue = new HSSFRichTextString(value);
    cell.setCellValue(richValue);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellStyle(getTailStyle());
  }

  private HSSFCellStyle getTailStyle() {
    // font를 설정한다.
    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 10);
    font.setFontName("돋움");

    HSSFCellStyle style = wb.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    style.setFont(font);
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);

    return style;
  }


  public void makeDataCell(int sheetIndex, int rowIndex, String [] values) {
    int dataRowIndex = ROW_POSITION + 3 + rowIndex;
    HSSFRow row = sheets[sheetIndex].createRow(dataRowIndex);

    // 데이터 를 입력한다.
    for(int index=0; index < values.length; index++) {
      createDataCell(row, COL_POSITION + index, values[index]);
    }
  }

  /**
   * 데이터 셀을 설정한다.
   * @param row
   * @param column
   * @param value
   */
  private void createDataCell(HSSFRow row, int column, String value) {
    HSSFCell cell = row.createCell((short)column);
    HSSFRichTextString richValue = new HSSFRichTextString(value);
    cell.setCellValue(richValue);
//    cell.setCellStyle(getDataStyle());
  }

  private HSSFCellStyle getDataStyle() {
    // font를 설정한다.
    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 10);
    font.setFontName("돋움");

    HSSFCellStyle style = wb.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_LEFT);

    style.setFont(font);

    return style;
  }

  private void makeHeader(int sheetIndex, String title, String author, String[] header, int [] length) {
    HSSFRow row = sheets[sheetIndex].createRow(ROW_POSITION);
    // Title을 만들어 준다.
    createTitleCell(row, COL_POSITION, title);
    sheets[sheetIndex].addMergedRegion(new Region(ROW_POSITION, (short)COL_POSITION, ROW_POSITION, (short)header.length));

    // 작성자 셀을 설정한다.
    row = sheets[sheetIndex].createRow(ROW_POSITION + 1);
    createAuthorCell(row, COL_POSITION + header.length -1, "작성자 : " + author);

    sheets[sheetIndex].setColumnWidth((short)0, (short) ( ( 50 * 1 ) / ( (double) 1 / 20 )));

    // 데이터 Header를 입력한다.
    row = sheets[sheetIndex].createRow(ROW_POSITION + 2);
    for(int index=0; index < header.length; index++) {
      sheets[sheetIndex].setColumnWidth((short)(COL_POSITION + index), (short) ( ( 50 * length[index] ) / ( (double) 1 / 20 )));
      createHeaderCell(row, COL_POSITION + index, header[index]);
    }
  }

  /**
   * Header 셀을 설정한다.
   * @param row
   * @param column
   * @param value
   */
  private void createHeaderCell(HSSFRow row, int column, String value) {
    HSSFCell cell = row.createCell((short)column);
    HSSFRichTextString richValue = new HSSFRichTextString(value);
    cell.setCellValue(richValue);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellStyle(getHeaderStyle());
  }

  private HSSFCellStyle getHeaderStyle() {
    // font를 설정한다.
    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 10);
    font.setFontName("돋움");
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(wb.getCustomPalette().findSimilarColor((byte)0x00, (byte)0x00, (byte)0x00).getIndex());


    HSSFCellStyle style = wb.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

    style.setFont(font);

    style.setFillForegroundColor(wb.getCustomPalette().findSimilarColor((byte)0x99, (byte)0xCC, (byte)0xFF).getIndex());
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);

    return style;
  }

  /**
   * 작성자 셀을 설정한다.
   * @param row
   * @param column
   * @param value
   */
  private void createAuthorCell(HSSFRow row, int column, String value) {
    HSSFCell cell = row.createCell((short)column);
    HSSFRichTextString richValue = new HSSFRichTextString(value);
    cell.setCellValue(richValue);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellStyle(getAuthorStyle());
  }

  private HSSFCellStyle getAuthorStyle() {
    // font를 설정한다.
    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 10);
    font.setFontName("돋움");

    HSSFCellStyle style = wb.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    style.setFont(font);

    return style;
  }

  /**
   * 제목 셀을 설정한다.
   * @param row
   * @param column
   * @param value
   */
  private void createTitleCell(HSSFRow row, int column, String value) {
    HSSFCell cell = row.createCell((short)column);
    HSSFRichTextString richValue = new HSSFRichTextString(value);
    cell.setCellValue(richValue);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellStyle(getTitleStyle());
  }

  private HSSFCellStyle getTitleStyle() {
    // font를 설정한다.
    HSSFFont font = wb.createFont();
    font.setFontHeightInPoints((short) 14);
    font.setFontName("돋움");
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(wb.getCustomPalette().findSimilarColor((byte)0xFF, (byte)0xFF, (byte)0xFF).getIndex());


    HSSFCellStyle style = wb.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

    style.setFont(font);

    style.setFillForegroundColor(wb.getCustomPalette().findSimilarColor((byte)0x00, (byte)0x80, (byte)0x80).getIndex());
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    return style;
  }

  public void write(OutputStream stream) throws IOException {
    wb.write(stream);
  }

  public static void main(String [] args) {
    String [] sheetNames = new String [] { "첫번째 시트" };
    String title = "타이틀";
    String author = "litwave";
    String createDate = "2008. 08. 21(목)";
    String [] header = new String [] { "제목 1", "제목2", "제목3" };
    int [] length = new int [] { 10, 15, 8 };

    ExcelFormatAdapter excelAdapter = new ExcelFormatAdapter(sheetNames, title, author, header, length);
    int index=0;
    for(; index < 10; index++) {
      excelAdapter.makeDataCell(0, index, new String [] { "data_" + index, "데이 터" + index, "테스트 " + index });
    }

    excelAdapter.makeTailCell(0, index, header.length, createDate);

    File f = new File("D:/Z02_temp/imsi/test.xls");
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(f);
      excelAdapter.write(fos);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if(fos != null) try { fos.close(); } catch (IOException e) { }
    }
  }
} 

