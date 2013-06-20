package neo.util.excel;

import java.io.File; // 파일 생성을 위해서 필요
import java.io.FileNotFoundException; // 파일 생성을 위해서 필요
import java.io.IOException; // 파일 생성을 위해서 필요

// 아래의 jxl 부분을 이클립스에서 사용하시기 위해서는 메뉴바에서 현재의 프로젝트명을 오른쪽 클릭 - Build Path - Add External

// jxl API를 다운받아서 압축을 푼 곳에서 jxl.jar 파일을 찾아서 삽입시켜야 해요. 꼭.... 반드시.... 무조건....
// 배포를 고려한다면 엄한 곳에서 가져오지는 마셈. 가까운 곳에 압축을 풀어서 불러오셈. ^^

import jxl.Workbook; // 엑셀 파일 관리를 위해 처음과 끝에 해당하는 기본이 되는 추상 클래스

import jxl.write.WritableWorkbook; // 실제 엑셀 파일 관리를 위해 Workbook, Sheet을 생성하는 Swing의 Frame과 같은 사막의 오아시스 역할하는 추상 클래스
import jxl.write.WritableSheet; // 쉬트를 관리하는 인터페이스
import jxl.write.WritableCellFormat; // 셀의 포멧 관련 클래스
import jxl.write.WriteException; // 셀의 쓰기 오류를 관리하기 위한
import jxl.write.Label; // 라벨 관리 클래스
import jxl.write.Blank; // 빈셀 관리 클래스
import jxl.format.*; // 셀 정렬, 보더, 컬러 부분 관련 클래스 임포트

/**
 * 	@Class Name	: 	Jxl_Write.java
 * 	@파일설명		: 	
 * 	1. 폰트 설정
 *		WritableFont TitleFont = new WritableFont(WritableFont.ARIAL, 13,WritableFont.BOLD, false); 
 *		WritableCellFormat TitleCellFont = new WritableCellFormat (TitleFont); 
 *	2. align 설정
 *		TitleCellFont.setAlignment(jxl.format.Alignment.CENTRE); // align
 *	3. Border 설정
 *		TitleCellFont.setBorder(Border.ALL, BorderLineStyle.THIN); // border
 *	4. color 설정
 *		TitleCellFont.setBackground(jxl.format.Colour.GOLD);  // color
 *	5. sheet 설정
 *		a. sheet 생성
 *			WritableSheet sheet = workbook.createSheet('쉬트이름", worksheet_idx++);
 *			// worksheet_idx 0,1,2,... 설정
 *		b. sheet 가져오기
 *			WritableSheet sheet = workbook.getSheet("쉬트이름");
 *	6. cell 값넣기
 *		a. 문자
 *			Label label = new Label(0, 0, "값",TitleCellFont);  // A0 에 값넣기
 *			sheet.addCell(label); 
 *		b. 숫자
 *			Number number = new Number(col++, row, 123, TitleCellFont); 
 *			sheet.addCell(number);
 *	7. cell merge
 *		sheet.mergeCells(0, 1, 4, 2); // A1 ~ E2 값 병합
 *	8. cell column크기
 *		public static void setColumnWidth(int col, int new_size) {
 *			CellView cv = sheet.getColumnView(col);
 *			cv.setSize(new_size);
 *			// Yes, only if I give this it works:
 *			sheet.setColumnView(col, cv);
 *		}
 *	9. 생성/저장
 *		a. 생성
 *			Workbook workbook = Workbook.createWorkbook(new File("sample.xls"));
 *		b. 저장
 *			workbook.write(); 
 *			workbook.close(); 
 *			<td style='mso-number-format:"@";'>01112344567</td> //엑셀 다운로드 시 '0001' 처럼 나오게 하기
 *			<td style='mso-number-format:"#,##0_'>123456789</td>
 *
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
public class Jxl_Write {

	public static void main(String args[]) throws FileNotFoundException,
													IOException, 
													WriteException { // 예외처리를 아예 해주고 내려옵니다.

		WritableWorkbook myWorkbook = Workbook.createWorkbook(new File("jxl_Smile.xls")); // 파일이름을 정하여 생성한다.

		WritableSheet mySheet = myWorkbook.createSheet("first sheet", 0); // WritableWorkbook에서 메소드를 이용하여 생성. 0번, 즉 첫번째 쉬트를 first sheet라는 이름으로 생성한다.

		WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
		WritableCellFormat nameFormat = new WritableCellFormat(); // 이름 셀 포멧 생성
		WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀 포멧 생성

		// 번호 레이블 셀 포멧 구성(자세한 것은 링크 된 API를 참조하셈) 참고사항 : 여기 부분에서 WriteException이 발생하네요.
		// 그러나 상단에서 미리 예외 처리를 해서 상관 없겠네요.
		numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // 보더와 보더라인스타일 설정
		numberFormat.setBackground(Colour.BLUE); // 여름에 맞는 색깔

		// 이름 레이블 셀 포멧 구성(자세한 것은 링크 된 API를 참조하셈)
		nameFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
		nameFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR); // 보더와 보더라인스타일 설정
		nameFormat.setBackground(Colour.GOLD); // 여름에 맞는 색깔

		// 데이터 셀 포멧 구성
		dataFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬

		// 쉬트의 컬럼 넓이 설정
		mySheet.setColumnView(0, 8); // 쉬트의 번호 컬럼(0번째)의 넓이 설정. setCloumnView(몇번째 컬럼, 넓이)
		mySheet.setColumnView(1, 15); // 쉬트의 이름 컬럼(1번째)의 넓이 설정
		mySheet.setColumnView(2, 20); // 쉬트의 비고 컬럼(2번째)의 넓이 설정

		// 라벨을 이용하여 해당 셀에 정보 넣기 시작
		Label numberLabel = new Label(0, 0, "학번", numberFormat); // 학번 라벨(열,행,"문장",포멧)
		mySheet.addCell(numberLabel); // 쉬트의 addCell 메소드를 사용하여 삽입

		Label nameLabel = new Label(1, 0, "성명", nameFormat); // 성명 라벨(열,행,"문장",포멧)
		mySheet.addCell(nameLabel); // 쉬트의 addCell 메소드를 사용하여 삽입

		// Blank blank = new Blank(2, 0, numberFormat); // 빈 셀(열,행,포멧) -- 필요 시 주석 처리 풀고 사용하셈.
		// sheet.addCell(blank);

		Label postScript = new Label(2, 0, "비고", nameFormat); // 비고 라벨(열,행,"문장",포멧)
		mySheet.addCell(postScript); // 쉬트의 addCell 메소드를 사용하여 삽입

		for (int no = 1; no < 6; no++) {
			Label numberLabels = new Label(0, no, "[" + no + "]", dataFormat); // 데이터 포멧에 맞게 1에서 5까지 번호생성
			mySheet.addCell(numberLabels); // 셀에 삽입

			Label nameLabels = new Label(1, no, (char) (no + 64) + "", dataFormat); // 데이터 포멧에 맞게 대문자 A에서 E까지 생성
			mySheet.addCell(nameLabels); // 셀에 삽입
		}
		
		myWorkbook.write(); // 준비된 정보를 엑셀 포멧에 맞게 생성
		myWorkbook.close(); // 처리 후 메모리에서 해제 처리
	}
}
