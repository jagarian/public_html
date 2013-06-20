package neo.was;

/**
 * 	@Class Name	: 	WasConfiguration.java
 * 	@파일설명		: 	UI단에서 데이타를 Page 형식으로 관리하기 위한 Entity Class
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
public class WasConfiguration {

	/**
	 * 레신 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void resin_refresh() throws Exception {
	// }
	
	/**
	 * 웹스피어 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void resin_refresh() throws Exception {
	// }

	/**
	 * 웹로직 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void weblogic_refresh() throws Exception {
	// }

	/**
	 * 제우스 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void jeus_refresh() throws Exception {
	// }

	/**
	 * 톰켓 Server Reload
	 * 
	 * @param
	 * @exception Exception
	 */
	// private void tomcat_refresh() throws Exception {
	// }

	/**
	 * 웹로직 서버 살아 있는지 체크 루틴
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
			// Unix ps -ef을 통해 프로세스가 살아 있는지 확인
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

			// PING 결과 확인
			String mystr = buf.toString();
			//System.out.println("Return 값 " + mystr);

			// 웹로직이 HANG되거나 Shutdown 되어 있는 경우
			if (mystr == null || mystr.equals("")) {
				return false;
			}

			// Web Logic이 정상적으로 PING을 보내주는 경우
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
