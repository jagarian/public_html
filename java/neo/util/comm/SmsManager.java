package neo.util.comm;

import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	SmsManager.java
 * 
 * 	@파일설명		:	error Code 정의
 * 
 *  0000 : 성공
 *	9999 : 서버 접속 장애, 또는 클래스변수값 오류 URLConnection Exception
 *  8888 : 접속후 Input Ouput Stream 에러
 * 	6666 : UTF-8 변환 에러
 *  5555,5550 : 수신자 존재하지 않음.
 *  4444 : 휴대폰 번호에서 국내번호앞에 붙은 번호들 제거 에러
 *  3333 : 특수문자 제거 에러
 *  1111,1110 : 기타오류
 *  
 *  error Code를 화면단에 던져주기 위한 SmsSendManager 호출단에서의 처리방법 
 *  에러코드를 리턴받아서
 *  ((HttpGauceResponse) response).addException(new GauceException("Native","errCode","errMessage"));
 *  를 이용하여 에러를 발생시켜야 한다.
 *
 *  error Code를 받아서 에러메시지를 화면에서의 출력하기 위한 처리방법
 *  onFail() event에서
 *  tr.SrvErrCode(), tr.SrvErrMsg() 메소드를 이용하여 에러코드를 출력하면 된다. 
 * 
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
public class SmsManager {

	String MsgSendID; 			//sms send primary key ex)20051219134456
	String MsgSender; 			//sender tel number ex)023102323
	String MsgSysID; 			//send System ID ex) GRMS
	String MsgStateType; 		//send status ex)0
	String MsgSendType; 		//send type ex)25
	String MsgReceiver; 		//receiver phone number ex)
	String MsgSendBody; 		//message in 80 byte
	String errCode = "0000"; 	//error code

	Date d = new Date();
	DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");
	String strDate = df.format(d);
	String msg = "";
	Connection conn = null;
	PreparedStatement pstmt = null;

	public SmsManager() {
		MsgSendID = "";
		MsgSender = "";
		MsgSysID = "";
		MsgStateType = "";
		MsgSendType = "";
		MsgReceiver = "";
		MsgSendBody = "";
	}

	public String setWebPage() {
		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url =	"jdbc:sqlserver://XXX.XXX.XXX.XXX:9999;DatabaseName=XXX";
			conn = DriverManager.getConnection(url, "XXX", "XXX");
			pstmt = conn.prepareCall("{call dbo.XXX_XXXCallMessage(?,?,?,?,?,?,?,?,?)}");
			pstmt.setString(1, "i");
			pstmt.setString(2, "");
			pstmt.setString(3, "");
			pstmt.setString(4, "GRMS");
			pstmt.setString(5, "1");
			pstmt.setString(6, "25");
			pstmt.setString(7, MsgSender);
			pstmt.setString(8, MsgReceiver);
			pstmt.setString(9, MsgSendBody);
			pstmt.execute();
			errCode = "0000";
		} catch (SQLException e) {
			Log.error(e.getMessage(), this);
			errCode = "9999";
		} catch (Exception e) {
			Log.error("SmsSendManager Exception=>" + e, this);
			errCode = "1111";
		}
		return errCode;
	}

	public void setSendID(String Sysid) {
		MsgSendID = ""; //Sysid+SemDate.getCurrentTime("yyyyMMddHHmmss");
	}

	public void setSender(String sender_ph) {
		MsgSender = removeSpecialChar(sender_ph);
	}

	public void setSysID(String Sysid) {
		MsgSysID = Sysid;
	}

	public void setStateType(String stateType) {
		MsgStateType = stateType;
	}

	public void setSendType(String sendType) {
		MsgSendType = sendType;
	}

	/**
	 * @title	 화면에서 80byte까지 잘라서 받은 문자열을 SMS 메시지로 받는다.
	 *         	 	 화면에서 받는 문자열은 URLEncoder.encode()처리전  body는 로그화면에서 한글이 정상적으로 출력되어야 한다.
	 *         		 MsgSendBody는 URLEncoder.encode()처리후 UTF-8로 변경된다.
	 * 
	 * @param 
	 * @return
	 */
	public void setSendBody(String body) {
		Log.info("body==>" + body, this);
		//MsgSendBody = URLEncoder.encode(body, "UTF-8");
		MsgSendBody = body;
		//Log.info("MsgSendBody==>"+MsgSendBody, this);
	}

	/**
	 * @title	 발송자 전화번호를 리스트를 받아 최종 전달 형태로 변환한다.
	 *         	 	 숫자이외의 특수문자는 제거한다.
	 *         		 MsgSendBody는 URLEncoder.encode()처리후 UTF-8로 변경된다.
	 * 
	 * @param ArrayList
	 * @return	 클래스 변수 MsgReceiver에 삽입한다. ex) phone-number1,phone-number2,....
	 */
	public void setReceiver(String receiver_ph) {
		MsgReceiver = removeSpecialChar(receiver_ph);
	}

	/**
	 * @title	 82-31-11-9888-5418 형식인 경우 뒤에서 '-'문자 3번째이전 숫자 제거함.즉 (82-31)제거함.
	 *         		 첫문자가 0이 없으면 '0'추가함.
	 *         		 전화번호에서 숫자가 아닌 문자들을 제거한다.
	 * 
	 * @param   
	 * @return  
	 */
	public String removeSpecialChar(String ph) {
		String pnumber = "";
		String phTemp = "";
		//'-'문자 3번째이전 숫자 제거함, 

		try {

			String[] splitStr = ph.split("-");
			int splitLen = splitStr.length;
			for (int i = 0; i < splitLen; i++) {
				if (splitLen == 6 && i >= 3) {
					if (splitStr[3].charAt(0) != '0') {
						//첫문자 0이 없으면 추가함.
						splitStr[3] = "0" + splitStr[3];
					}
					phTemp += splitStr[i];
				} else if (splitLen == 5 && i >= 2) {
					if (splitStr[2].charAt(0) != '0') {
						splitStr[2] = "0" + splitStr[2];
					}
					phTemp += splitStr[i];
				} else if (splitLen == 4 && i >= 1) {
					if (splitStr[1].charAt(0) != '0') {
						splitStr[1] = "0" + splitStr[1];
					}
					phTemp += splitStr[i];
				} else if (splitLen == 3 && i >= 0) {
					if (splitStr[0].charAt(0) != '0') {
						splitStr[0] = "0" + splitStr[0];
					}
					phTemp += splitStr[i];
				} else {
				}
			}
		} catch (Exception e) {
			Log.error("휴대폰 번호에서 국내번호앞에 붙은 번호들 제거 에러", this);
			errCode = "4444";
		}
		ph = phTemp;

		try {
			//특수문자 제거
			char[] phoneNum = ph.toCharArray();
			for (int j = 0; j < phoneNum.length; j++) {
				if (Character.isDigit(phoneNum[j])) {
					pnumber += String.valueOf(phoneNum[j]);
				}
			}
		} catch (Exception e) {
			Log.error("특수문자 제거 에러", this);
			errCode = "3333";
		}
		return pnumber;
	}
}
