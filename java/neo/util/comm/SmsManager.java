package neo.util.comm;

import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	SmsManager.java
 * 
 * 	@���ϼ���		:	error Code ����
 * 
 *  0000 : ����
 *	9999 : ���� ���� ���, �Ǵ� Ŭ���������� ���� URLConnection Exception
 *  8888 : ������ Input Ouput Stream ����
 * 	6666 : UTF-8 ��ȯ ����
 *  5555,5550 : ������ �������� ����.
 *  4444 : �޴��� ��ȣ���� ������ȣ�տ� ���� ��ȣ�� ���� ����
 *  3333 : Ư������ ���� ����
 *  1111,1110 : ��Ÿ����
 *  
 *  error Code�� ȭ��ܿ� �����ֱ� ���� SmsSendManager ȣ��ܿ����� ó����� 
 *  �����ڵ带 ���Ϲ޾Ƽ�
 *  ((HttpGauceResponse) response).addException(new GauceException("Native","errCode","errMessage"));
 *  �� �̿��Ͽ� ������ �߻����Ѿ� �Ѵ�.
 *
 *  error Code�� �޾Ƽ� �����޽����� ȭ�鿡���� ����ϱ� ���� ó�����
 *  onFail() event����
 *  tr.SrvErrCode(), tr.SrvErrMsg() �޼ҵ带 �̿��Ͽ� �����ڵ带 ����ϸ� �ȴ�. 
 * 
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
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
	 * @title	 ȭ�鿡�� 80byte���� �߶� ���� ���ڿ��� SMS �޽����� �޴´�.
	 *         	 	 ȭ�鿡�� �޴� ���ڿ��� URLEncoder.encode()ó����  body�� �α�ȭ�鿡�� �ѱ��� ���������� ��µǾ�� �Ѵ�.
	 *         		 MsgSendBody�� URLEncoder.encode()ó���� UTF-8�� ����ȴ�.
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
	 * @title	 �߼��� ��ȭ��ȣ�� ����Ʈ�� �޾� ���� ���� ���·� ��ȯ�Ѵ�.
	 *         	 	 �����̿��� Ư�����ڴ� �����Ѵ�.
	 *         		 MsgSendBody�� URLEncoder.encode()ó���� UTF-8�� ����ȴ�.
	 * 
	 * @param ArrayList
	 * @return	 Ŭ���� ���� MsgReceiver�� �����Ѵ�. ex) phone-number1,phone-number2,....
	 */
	public void setReceiver(String receiver_ph) {
		MsgReceiver = removeSpecialChar(receiver_ph);
	}

	/**
	 * @title	 82-31-11-9888-5418 ������ ��� �ڿ��� '-'���� 3��°���� ���� ������.�� (82-31)������.
	 *         		 ù���ڰ� 0�� ������ '0'�߰���.
	 *         		 ��ȭ��ȣ���� ���ڰ� �ƴ� ���ڵ��� �����Ѵ�.
	 * 
	 * @param   
	 * @return  
	 */
	public String removeSpecialChar(String ph) {
		String pnumber = "";
		String phTemp = "";
		//'-'���� 3��°���� ���� ������, 

		try {

			String[] splitStr = ph.split("-");
			int splitLen = splitStr.length;
			for (int i = 0; i < splitLen; i++) {
				if (splitLen == 6 && i >= 3) {
					if (splitStr[3].charAt(0) != '0') {
						//ù���� 0�� ������ �߰���.
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
			Log.error("�޴��� ��ȣ���� ������ȣ�տ� ���� ��ȣ�� ���� ����", this);
			errCode = "4444";
		}
		ph = phTemp;

		try {
			//Ư������ ����
			char[] phoneNum = ph.toCharArray();
			for (int j = 0; j < phoneNum.length; j++) {
				if (Character.isDigit(phoneNum[j])) {
					pnumber += String.valueOf(phoneNum[j]);
				}
			}
		} catch (Exception e) {
			Log.error("Ư������ ���� ����", this);
			errCode = "3333";
		}
		return pnumber;
	}
}
