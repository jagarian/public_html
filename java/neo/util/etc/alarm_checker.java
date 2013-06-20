package neo.util.etc;

import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;

import java.io.*;
import javax.xml.parsers.*; // XML 화일 화서^^
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * 	@Class Name	: 	alarm_checker.java
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
public class alarm_checker extends DefaultHandler {
	final static int Num_of_Equip = 16;
	final String WARNING_01 = "팬이상";
	final String WARNING_02 = "COMP1";
	final String WARNING_03 = "COMP2";
	final String WARNING_04 = "재열히터";
	final String WARNING_05 = "가습히터";
	final String WARNING_06 = "센서";
	final String WARNING_07 = "누수";
	final String WARNING_08 = "고온";
	final String WARNING_09 = "저온";
	final String WARNING_10 = "고습";
	final String WARNING_11 = "저습";
	final String WARNING_12 = "화재";
	final String WARNING_13 = "급수";

	String sms_receiver = ""; 	// 메시지 수신인(통보받을 사람)
	String sms_server = ""; 	// SMS서버 수신 페이지 IP
	String logfilepath = ""; 	// 로그파일이 위치해있는 로컬 경로
	int num_of_equip = 0; 		// 모니터 하는 로그파일 갯수
	int check_interval = 0; 	// 감시 간격(분)
	int valid_time = 0; 		// 로그 장애 유효시간(분)

	// Parse the XML Using SAX
	// XML Parser Call Back function
	public void startDocument() {
	}

	// XML Parser Call Back function
	public void endDocument() {
	}

	// XML Parser Call Back function(프로그램 기본 설정 값을 읽어 온다.)
	public void startElement(String namespace, String localName, String qName,
			Attributes atts) {

		if (localName.equals("contents")) {
			sms_receiver = atts.getValue("sms_receiver");
			check_interval = Integer.parseInt(atts.getValue("check_interval"));
			valid_time = Integer.parseInt(atts.getValue("valid_time"));
			sms_server = atts.getValue("sms_server");
			logfilepath = atts.getValue("logfilepath");
		}
	}

	// XML Parser function
	public void processWithSAX(String urlString) throws Exception {

		// 1. 객채 생성
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		// 4. 이벤트 핸들러 등록 및 xml 문서 파싱
		ParserAdapter adapter = new ParserAdapter(parser.getParser());
		adapter.setContentHandler(this);
		adapter.parse(urlString);
	}

	// ==================== Send SMS(경고발견시 담당자에게 문자메시지 발송) ===============
	// 입력값 : send_string(발송 메시지)
	// sms_url(서버 주소)
	// 출력값 : 전송성공(true)
	// 전송실패(false)
	//
	public boolean SMS_Send(	String contact_hp, 
								String send_string,
								String sms_url) {
		StringBuffer send_url = new StringBuffer();
		URL url;
		URLConnection conn = null;
		String word = "";
		String hp_num = "";

		try {
			word = java.net.URLEncoder.encode(send_string, "EUC-KR"); // 한글은
																		// 인코딩한다.
			send_url.append(sms_url
							+ "?kind1=Z001&callback_ph=0&message_num=A12&sender_no=0&hp_num="
							+ contact_hp 
							+ "&word=" + word);

			url = new URL(send_url.toString());
			conn = url.openConnection();
			conn.connect();

			if (conn.getContentLength() != 0) {
				InputStream istream = conn.getInputStream(); // 실질적인 발송이 되는 부분
				BufferedReader in = new BufferedReader(new InputStreamReader(
						istream));
				StringBuffer sbResult = new StringBuffer();
				char cBuffer[] = new char[1024];
				int nLength;
				while ((nLength = in.read(cBuffer, 0, 1024)) != -1) {
					sbResult.append(cBuffer, 0, nLength);
				}
				in.close();
				conn = null;
				return true;
			}

		} catch (Exception e) {
			conn = null;
		}
		conn = null;
		return false;

	}

	public String getCurrentTime() /* 현재 시각을 가져옴 (hhmmss) */
	{
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return st.format(new Date(System.currentTimeMillis()));
	}

	public boolean isNumber(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			// Character클래스의 isDigit() 메소드를 이용하여 문자이면
			// check에 false를 대입하고 break를 이용하여 for문을 빠져나옴
			if (!Character.isDigit(str.charAt(i))) {
				check = false;
				break;
			}
		}
		return check;
	}

	// ==================================================================================================
	// 로그 파일 체커(입력 : 모니터 장비번호(00~16) , 리턴 : 에러항목(스트링:화일이 없을경우 별도 메시지)
	// 장애발생시간과 현재시간을 비교하여 유효시간보다 지난건은 무시한다.
	// ==================================================================================================
	public String Check_Log_File(String file_path, int Equip_no) {

		java.text.SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		GregorianCalendar log_datetime = new GregorianCalendar();
		GregorianCalendar right_now = new GregorianCalendar();
		String line;
		String date_string;
		String file_last_line = "";
		StringBuffer r_string = new StringBuffer();
		StringBuffer file_name = new StringBuffer();

		int i = 0;

		date_string = dateformat.format(new java.util.Date());
		if (Equip_no < 10)
			file_name.append(file_path + 
							"/" + 
							date_string.substring(0, 6)	+ 
							"/" + 
							date_string + 
							".00" + 
							Equip_no); 	// 로그화일이 있는 폴더의 해당
										// 로그화일의 화일명
		else
			file_name.append(file_path + 
							"/" + 
							date_string.substring(0, 6)	+ 
							"/" + 
							date_string + 
							".0" + 
							Equip_no); 	// 로그화일이 있는 폴더의 해당
										// 로그화일의 화일명
		try {
			FileReader myFileReader = new FileReader(file_name.toString());
			BufferedReader br = new BufferedReader(myFileReader);
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0)
					continue;
				file_last_line = line;
			}
			br.close();
		} catch (Exception e) {
			return "NOTFOUND"; // File not found
		}

		log_datetime.set(right_now.get(Calendar.YEAR), right_now
				.get(Calendar.MONTH), right_now.get(Calendar.DATE), Integer
				.parseInt(file_last_line.substring(0, 2)), Integer
				.parseInt(file_last_line.substring(2, 4)), Integer
				.parseInt(file_last_line.substring(4, 6)));

		// 로그화일의 시간과 현재시간을 비교하여 유효시간내의 경우에만 값을 리턴함
		if ((right_now.getTimeInMillis() - log_datetime.getTimeInMillis()) < (valid_time * 60 * 1000)) { 	// Valid_time은
																											// 분이므로
																											// millisecond로
																											// 환산
			if (file_last_line.substring(50, 51).equals("1"))
				r_string.append(WARNING_01 + ",");
			if (file_last_line.substring(51, 52).equals("1"))
				r_string.append(WARNING_02 + ",");
			if (file_last_line.substring(52, 53).equals("1"))
				r_string.append(WARNING_03 + ",");
			if (file_last_line.substring(53, 54).equals("1"))
				r_string.append(WARNING_04 + ",");
			if (file_last_line.substring(54, 55).equals("1"))
				r_string.append(WARNING_05 + ",");
			if (file_last_line.substring(55, 56).equals("1"))
				r_string.append(WARNING_06 + ",");
			if (file_last_line.substring(56, 57).equals("1"))
				r_string.append(WARNING_07 + ",");
			if (file_last_line.substring(57, 58).equals("1"))
				r_string.append(WARNING_08 + ",");
			if (file_last_line.substring(58, 59).equals("1"))
				r_string.append(WARNING_09 + ",");
			if (file_last_line.substring(59, 60).equals("1"))
				r_string.append(WARNING_10 + ",");
			if (file_last_line.substring(60, 61).equals("1"))
				r_string.append(WARNING_11 + ",");
			if (file_last_line.substring(61, 62).equals("1"))
				r_string.append(WARNING_12 + ",");
			if (file_last_line.substring(62, 63).equals("1"))
				r_string.append(WARNING_13);
			return r_string.toString();
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		int innerloop = 0;
		int innerloop2 = 0;
		int log_notfound_count = 0;
		String Check_Result = "";
		alarm_checker myHandler = new alarm_checker();
		myHandler.processWithSAX("config.xml");
		
		System.out.println("");
		System.out.println(" 항온항습기 Log File Checker Ver 0.01");
		System.out.println("===========================================");

		System.out.println(" SMS메시지 수신번호 : " + myHandler.sms_receiver);
		System.out.println(" SMS서버 URL : " + myHandler.sms_server);
		System.out.println(" 체크 간격(분) : " + myHandler.check_interval);
		System.out.println(" 로그 유효시간(분) : " + myHandler.valid_time);
		System.out.println(" 로그화일 경로 : " + myHandler.logfilepath);
		System.out.println("");

		String[] contact_hp = myHandler.sms_receiver.split(",", 0);

		while (true) {
			for (innerloop = 0; innerloop < Num_of_Equip; innerloop++) // 장비수만큼
																		// 루프
			{
				Check_Result = myHandler.Check_Log_File(myHandler.logfilepath,
						innerloop); // 해당 장비번호 로그화일 있는지 확인

				if (!Check_Result.equals("")
						&& !Check_Result.equals("NOTFOUND")) { // 장애가 발생했을 경우!

					for (innerloop2 = 0; innerloop2 < contact_hp.length; innerloop2++) { 	// 수신번호
																							// 수만큼
																							// 루프돌면서
																							// SMS발송

						if (myHandler.SMS_Send(contact_hp[innerloop2].trim(),
												innerloop + " : " + Check_Result,
												myHandler.sms_server)) {
							System.out.println(" " + myHandler.getCurrentTime()
												+ "      장비(" + innerloop + ") 장애 : "
												+ Check_Result + "=> [SMS발송성공("
												+ contact_hp[innerloop2].trim() + ")] ");
						} else {
							System.out.println(" " + myHandler.getCurrentTime()
												+ "      장비(" + innerloop + ") 장애 : "
												+ Check_Result + "=> [SMS발송실패("
												+ contact_hp[innerloop2].trim() + ")] ");
						}
					}
					// 장애 발생의 경우

				} else if (!Check_Result.equals("NOTFOUND")) { // 장비상태 양호
					System.out.println(" " + myHandler.getCurrentTime()	+ " 장비(" + innerloop + ") OK! ");
				} else {
					log_notfound_count++;
				}
			}

			if (log_notfound_count == Num_of_Equip)
				System.out.println(" " + myHandler.getCurrentTime() + " 에러 : 로그화일을 찾을수 없습니다!!!");

			System.out.println("");
			Thread.sleep(myHandler.check_interval * 1000 * 60); // millisecond이므로
																// 분단위로 환산			
		}
	}
}
