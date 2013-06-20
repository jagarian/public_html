package neo.util.session;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import java.sql.Date;

/**
 * 	@Class Name	: 	SessionChecker.java
 * 	@���ϼ���		: 	��Ű�� �̿��� �湮�� �� ī��Ʈ ���.
 * 	@Version		: 	1.0
 * 	@Author		: 	hoon09
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
public class SessionChecker {

	public void setSession(HttpSession session, String ip) {
		session.setAttribute("listener", new CustomBindingListener(ip));
	}
}

class CustomBindingListener implements HttpSessionBindingListener {
	String num = null;
	String ip = "";
	String read_ip = null;

	public CustomBindingListener(String ip) {
		this.ip = ip;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		// ������ ������ �� �� ����
		long t = System.currentTimeMillis();
		Date date = new Date(t);
		String chdate = date.toString();
		chdate = chdate.replace('-', '/');
		String today_count = null;
		String today = null;
		String today_num = null;// ���� �湮�� ī��Ʈ

		Vector v = new Vector();// ���� ������ ������ üũ
		try {
			String dir = System.getProperty("user.dir") + "/ResinCount/test/admin/";
			String filename = dir + "count.txt";
			String filename2 = dir + "ip_count.txt";
			String filename3 = dir + "today_count.txt";
			File file = new File(filename);
			File file2 = new File(filename2);
			File file3 = new File(filename3);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (!file2.exists()) {
				file2.createNewFile();
			}
			if (!file3.exists()) {
				file3.createNewFile();
			}
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			FileReader fr2 = new FileReader(filename2);
			BufferedReader br2 = new BufferedReader(fr2);
			FileReader fr3 = new FileReader(filename3);
			BufferedReader br3 = new BufferedReader(fr3);

			num = br.readLine();// �� ī��Ʈ �� �о����
			while ((read_ip = br2.readLine()) != null) {
				read_ip = read_ip.trim();
				v.addElement(read_ip);
			}// ���� ������ ������ ��������
			today_count = br3.readLine();// ���� �湮�� ī��Ʈ ��������
			br.close();
			br2.close();
			br3.close();
			// �ѹ湮�� �ʱ�ȭ
			if (num == null) {
				FileWriter fw = new FileWriter(filename);
				fw.write("0");
				num = "0";
				fw.close();
			}
			// ���������� �ʱ�ȭ
			if (read_ip == null) {
				String start = "127.0.0.1";
				FileWriter fw = new FileWriter(filename2);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(start, 0, start.length());
				bw.close();
				fw.close();
			}
			// ���� �湮�� �ʱ�ȭ
			if (today_count == null) {
				FileWriter fw3 = new FileWriter(filename3);
				fw3.write(chdate + ",1");
				today_num = "0";
				fw3.close();
			}
			// �� ī��Ʈ �Է�
			int now = (Integer.parseInt(num)) + 1;
			Integer new_num = new Integer(now);
			num = new_num.toString();
			FileWriter fw = new FileWriter(filename);
			fw.write(num);
			fw.close();
			// ���� ������ ������ �Է�
			FileWriter fw2 = new FileWriter(filename2);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			boolean match = false;
			for (int i = 0; i < v.size(); i++) {
				String read = (String) v.elementAt(i);
				if (read.equals(ip)) {
					v.setElementAt(ip, i);// ������ ������ �� �� ������Ʈ
					match = true;
				}
			}
			if (!match) {
				v.addElement(ip);
			}
			for (int j = 0; j < v.size(); j++) {
				String wr = (String) v.elementAt(j);
				if (j != 0)
					bw2.newLine();
				bw2.write(wr, 0, wr.length());
			}
			bw2.close();
			fw2.close();
			// ���� �湮�� �Է�
			if (today_count != null) {
				String[] today_Cnvt = new String[3];
				int idx = 0;
				StringTokenizer strToken = new StringTokenizer(today_count,
						",", true);
				while (strToken.hasMoreTokens()) {
					today = strToken.nextToken();
					today_Cnvt[idx] = today;
					idx++;
				}
				if (today_Cnvt[0].equals(chdate)) {
					int today_num2 = (Integer.parseInt(today_Cnvt[2])) + 1;

					Integer new_today = new Integer(today_num2);
					today_num = new_today.toString();

					FileWriter fw3 = new FileWriter(filename3);
					fw3.write(chdate + "," + today_num);
					fw3.close();
				} else {
					today_num = "1";
					FileWriter fw3 = new FileWriter(filename3);
					fw3.write(chdate + "," + today_num);
					fw3.close();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		// ������ ����Ǿ��� �� �� ��
		Vector v = new Vector();
		try {
			String dir = System.getProperty("user.dir") + "/ResinCount/test/admin/";
			String filename = dir + "ip_count.txt";

			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			while ((read_ip = br.readLine()) != null) {
				read_ip = read_ip.trim();
				v.addElement(read_ip);
			}
			br.close();

			FileWriter fw = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < v.size(); i++) {
				String read = (String) v.elementAt(i);
				if (read.equals(ip)) {
					v.removeElementAt(i);
				}
			}

			for (int j = 0; j < v.size(); j++) {
				String wr = (String) v.elementAt(j);
				if (j != 0)
					bw.newLine();
				bw.write(wr, 0, wr.length());
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}