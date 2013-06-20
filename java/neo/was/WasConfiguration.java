package neo.was;

/**
 * 	@Class Name	: 	WasConfiguration.java
 * 	@���ϼ���		: 	UI�ܿ��� ����Ÿ�� Page �������� �����ϱ� ���� Entity Class
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
public class WasConfiguration {

	/**
	 * ���� Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void resin_refresh() throws Exception {
	// }
	
	/**
	 * �����Ǿ� Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void resin_refresh() throws Exception {
	// }

	/**
	 * ������ Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void weblogic_refresh() throws Exception {
	// }

	/**
	 * ���콺 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void jeus_refresh() throws Exception {
	// }

	/**
	 * ���� Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void tomcat_refresh() throws Exception {
	// }

	/**
	 * ������ ���� ��� �ִ��� üũ ��ƾ
	 * 
	 * @param
	 * @exception Exception
	 */
	public boolean systemPing() {
		String pingStrOfWeblogic = "java weblogic.Admin -url t3://150.19.1.110:8001 -username system -password weblogic PING";
		// String pingStrOfResin = "";
		// String pingStrOfTomcat = "";
		// String pingStrOfOracle = "";

		boolean lchk = false;
		try {
			// Unix ps -ef�� ���� ���μ����� ��� �ִ��� Ȯ��
			Runtime rt = Runtime.getRuntime();

			// --------------------------------------------------------------
			// WAS1 Check
			// --------------------------------------------------------------
			Process p = rt.exec(pingStrOfWeblogic);
			// Process p = rt.exec("ps -ef");

			java.io.InputStream is = p.getInputStream();
			java.io.BufferedReader br = new java.io.BufferedReader(
					new java.io.InputStreamReader(is));

			StringBuffer buf = new StringBuffer();

			for (String str; (str = br.readLine()) != null;) {
				//System.out.println(str);
				buf.append(str);
			}

			// PING ��� Ȯ��
			String mystr = buf.toString();
			//System.out.println("Return �� " + mystr);

			// �������� HANG�ǰų� Shutdown �Ǿ� �ִ� ���
			if (mystr == null || mystr.equals("")) {
				return false;
			}

			// Web Logic�� ���������� PING�� �����ִ� ���
			int i = mystr.indexOf("RTT");

			if (i > 0) {
				lchk = true;
			} else {
				lchk = false;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return lchk;
	}
}
