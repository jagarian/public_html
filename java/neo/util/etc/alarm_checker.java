package neo.util.etc;

import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;

import java.io.*;
import javax.xml.parsers.*; // XML ȭ�� ȭ��^^
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * 	@Class Name	: 	alarm_checker.java
 * 	@���ϼ���		: 	
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
public class alarm_checker extends DefaultHandler {
	final static int Num_of_Equip = 16;
	final String WARNING_01 = "���̻�";
	final String WARNING_02 = "COMP1";
	final String WARNING_03 = "COMP2";
	final String WARNING_04 = "�翭����";
	final String WARNING_05 = "��������";
	final String WARNING_06 = "����";
	final String WARNING_07 = "����";
	final String WARNING_08 = "���";
	final String WARNING_09 = "����";
	final String WARNING_10 = "���";
	final String WARNING_11 = "����";
	final String WARNING_12 = "ȭ��";
	final String WARNING_13 = "�޼�";

	String sms_receiver = ""; 	// �޽��� ������(�뺸���� ���)
	String sms_server = ""; 	// SMS���� ���� ������ IP
	String logfilepath = ""; 	// �α������� ��ġ���ִ� ���� ���
	int num_of_equip = 0; 		// ����� �ϴ� �α����� ����
	int check_interval = 0; 	// ���� ����(��)
	int valid_time = 0; 		// �α� ��� ��ȿ�ð�(��)

	// Parse the XML Using SAX
	// XML Parser Call Back function
	public void startDocument() {
	}

	// XML Parser Call Back function
	public void endDocument() {
	}

	// XML Parser Call Back function(���α׷� �⺻ ���� ���� �о� �´�.)
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

		// 1. ��ä ����
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		// 4. �̺�Ʈ �ڵ鷯 ��� �� xml ���� �Ľ�
		ParserAdapter adapter = new ParserAdapter(parser.getParser());
		adapter.setContentHandler(this);
		adapter.parse(urlString);
	}

	// ==================== Send SMS(���߽߰� ����ڿ��� ���ڸ޽��� �߼�) ===============
	// �Է°� : send_string(�߼� �޽���)
	// sms_url(���� �ּ�)
	// ��°� : ���ۼ���(true)
	// ���۽���(false)
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
			word = java.net.URLEncoder.encode(send_string, "EUC-KR"); // �ѱ���
																		// ���ڵ��Ѵ�.
			send_url.append(sms_url
							+ "?kind1=Z001&callback_ph=0&message_num=A12&sender_no=0&hp_num="
							+ contact_hp 
							+ "&word=" + word);

			url = new URL(send_url.toString());
			conn = url.openConnection();
			conn.connect();

			if (conn.getContentLength() != 0) {
				InputStream istream = conn.getInputStream(); // �������� �߼��� �Ǵ� �κ�
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

	public String getCurrentTime() /* ���� �ð��� ������ (hhmmss) */
	{
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return st.format(new Date(System.currentTimeMillis()));
	}

	public boolean isNumber(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			// CharacterŬ������ isDigit() �޼ҵ带 �̿��Ͽ� �����̸�
			// check�� false�� �����ϰ� break�� �̿��Ͽ� for���� ��������
			if (!Character.isDigit(str.charAt(i))) {
				check = false;
				break;
			}
		}
		return check;
	}

	// ==================================================================================================
	// �α� ���� üĿ(�Է� : ����� ����ȣ(00~16) , ���� : �����׸�(��Ʈ��:ȭ���� ������� ���� �޽���)
	// ��ֹ߻��ð��� ����ð��� ���Ͽ� ��ȿ�ð����� �������� �����Ѵ�.
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
							Equip_no); 	// �α�ȭ���� �ִ� ������ �ش�
										// �α�ȭ���� ȭ�ϸ�
		else
			file_name.append(file_path + 
							"/" + 
							date_string.substring(0, 6)	+ 
							"/" + 
							date_string + 
							".0" + 
							Equip_no); 	// �α�ȭ���� �ִ� ������ �ش�
										// �α�ȭ���� ȭ�ϸ�
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

		// �α�ȭ���� �ð��� ����ð��� ���Ͽ� ��ȿ�ð����� ��쿡�� ���� ������
		if ((right_now.getTimeInMillis() - log_datetime.getTimeInMillis()) < (valid_time * 60 * 1000)) { 	// Valid_time��
																											// ���̹Ƿ�
																											// millisecond��
																											// ȯ��
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
		System.out.println(" �׿��׽��� Log File Checker Ver 0.01");
		System.out.println("===========================================");

		System.out.println(" SMS�޽��� ���Ź�ȣ : " + myHandler.sms_receiver);
		System.out.println(" SMS���� URL : " + myHandler.sms_server);
		System.out.println(" üũ ����(��) : " + myHandler.check_interval);
		System.out.println(" �α� ��ȿ�ð�(��) : " + myHandler.valid_time);
		System.out.println(" �α�ȭ�� ��� : " + myHandler.logfilepath);
		System.out.println("");

		String[] contact_hp = myHandler.sms_receiver.split(",", 0);

		while (true) {
			for (innerloop = 0; innerloop < Num_of_Equip; innerloop++) // ������ŭ
																		// ����
			{
				Check_Result = myHandler.Check_Log_File(myHandler.logfilepath,
						innerloop); // �ش� ����ȣ �α�ȭ�� �ִ��� Ȯ��

				if (!Check_Result.equals("")
						&& !Check_Result.equals("NOTFOUND")) { // ��ְ� �߻����� ���!

					for (innerloop2 = 0; innerloop2 < contact_hp.length; innerloop2++) { 	// ���Ź�ȣ
																							// ����ŭ
																							// �������鼭
																							// SMS�߼�

						if (myHandler.SMS_Send(contact_hp[innerloop2].trim(),
												innerloop + " : " + Check_Result,
												myHandler.sms_server)) {
							System.out.println(" " + myHandler.getCurrentTime()
												+ "      ���(" + innerloop + ") ��� : "
												+ Check_Result + "=> [SMS�߼ۼ���("
												+ contact_hp[innerloop2].trim() + ")] ");
						} else {
							System.out.println(" " + myHandler.getCurrentTime()
												+ "      ���(" + innerloop + ") ��� : "
												+ Check_Result + "=> [SMS�߼۽���("
												+ contact_hp[innerloop2].trim() + ")] ");
						}
					}
					// ��� �߻��� ���

				} else if (!Check_Result.equals("NOTFOUND")) { // ������ ��ȣ
					System.out.println(" " + myHandler.getCurrentTime()	+ " ���(" + innerloop + ") OK! ");
				} else {
					log_notfound_count++;
				}
			}

			if (log_notfound_count == Num_of_Equip)
				System.out.println(" " + myHandler.getCurrentTime() + " ���� : �α�ȭ���� ã���� �����ϴ�!!!");

			System.out.println("");
			Thread.sleep(myHandler.check_interval * 1000 * 60); // millisecond�̹Ƿ�
																// �д����� ȯ��			
		}
	}
}
