package neo.util.log;

import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import neo.config.Config;

/**
 * @Class Name 	: Log.java * 
 * @파일설명 		: Log4J하나만으로도 디버깅하기 쉽고 뽀대나고 어딘가 비싸보이는 프로그램이 된다. ^^;
 *                   (default 메세지 레벨을 info 이상으로 하자.)
 * 
 * http://apache.tt.co.kr/logging/log4j/1.2.15/apache-log4j-1.2.15.zip  에서 log4j 다운로드
 * (※ 현재는 log4j 2.0 버전까지 나왔으나, 신뢰할 수 있는 수준이 아니기 때문에, 보통 log4 1.2를 권장한다.)
 * 
 *	- fatal : 빠른 시간안에 시스템을 멈추게 할수 있는 에러의 경우 fatal 레벨을 사용하여 로그를 남긴다.
 *	- error : 기타 런타임 에러나 기대하지 않은 상태였을때 error 레벨을 사용하여 로그를 남긴다.
 *	- warn : deprecated 된 API 사용이나 error에 가까운 잘못된 API 사용같은 경우 warn 레벨을 사용하여 로그를 남긴다. 반드시 잘못된 경우에만 사용하는 건 아니다.
 *	- info : 시스템 시작이나 종료처럼 관심가질만한 런타임 이벤트일 경우 info 레벨을 사용하여 로그를 남긴다.
 *	- debug : 시스템 흐름상 자세한 정보를 원하면 debug 레벨을 사용하여 로그를 남긴다.
 *	- trace : 더욱 자세한 정보를 남기길 원할때 trace 레벨을 사용하여 로그를 남긴다.
 * 
 * 사용예 : Log.info("selectCount BEGIN", this);
 * 
 * 다른 사용예 : 이것만으로도 Log.info 과는 비교할 수 없는 강력하고 편리한 로깅 기능을 사용할 수 있다.
 *                    log4j.properties 파일을 클래스패스가 걸린 디렉토리에 두면 된다. 웹 어플리케이션은 WEB-INF/classes에 두면 된다
 *                    
 *	private Logger log = Logger.getLogger(ClassName.class); //되도록 static 으로 로거를 생성하지 말라
 * 	//혹은 private Logger log = Logger.getLogger(this.getClass());
 *	public void method() {
 *      
 *      //isInfoEnabled
 *		if (log.isDebugEnabled()) {
 *			Log.info("디버깅용 메시지");
 *		}
 *		Log.info("정보를 남기기위한 메시지");
 *        
 *		try {
 *			//어쩌구 저쩌구 실행...
 *		} catch (Exception ex) {
 *			//로그에 예외 메시지도 함께 남기기
 *			Log.info("예외가 발생했어요~~", ex);
 *		}
 *	}
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
public class Log {

	private static Logger logger = Logger.getRootLogger();
	private static boolean initFlag = true;
	
	public static void debug(String sql, Object[] param) {
		String message = "";

		try {
			if (param != null && param.length > 0) {
				String[] arrSql = sql.split("\\?");

				if (arrSql.length - 1 == param.length) {
					message = "";
					for (int idx = 0; idx < arrSql.length - 1; idx++) {
						message += arrSql[idx] + "'" + param[idx] + "'";
					}
					message += arrSql[arrSql.length - 1];
				}
			}
		} catch (Exception e) {
			message = sql;
		}

		debug(message);
	}
	
	/**
	 * @title	오브젝트를 넘기는 로깅
	 */
	public static void debug(String message, Object ref) {
		if (logger.isEnabledFor(Level.DEBUG)) 
			log(Level.DEBUG, message, null, ref);
	}

	public static void info(String message, Object ref) {
		if (logger.isEnabledFor(Level.INFO))
			log(Level.INFO, message, null, ref);
	}

	public static void warn(String message, Object ref) {
		if (logger.isEnabledFor(Level.WARN))
			log(Level.WARN, message, null, ref);
	}
	
	public static void error(String message, Object ref) {
		if (logger.isEnabledFor(Level.ERROR))
			log(Level.ERROR, message, null, ref);
	}

	public static void error(String message, Throwable t, Object ref) {
		if (logger.isEnabledFor(Level.ERROR))
			log(Level.ERROR, message, t, ref);
	}

	public static void fatal(String message, Object ref_path_str) {
		if (logger.isEnabledFor(Level.FATAL))
			log(Level.FATAL, message, null, ref_path_str);
	}
	
	/**
	 * @title	로그만 넘기는 로깅
	 */
	public static void debug(String message) {
		if (logger.isEnabledFor(Level.DEBUG)) 
			log(Level.DEBUG, message, null, null);
	}

	public static void info(String message) {
		if (logger.isEnabledFor(Level.INFO)) 
			log(Level.INFO, message, null, null);
	}

	public static void warn(String message) {
		if (logger.isEnabledFor(Level.WARN)) 
			log(Level.WARN, message, null, null);
	}
	
	public static void error(String message) {
		if (logger.isEnabledFor(Level.ERROR)) 
			log(Level.ERROR, message, null, null);
	}

	public static void error(String message, Throwable t) {
		if (logger.isEnabledFor(Level.ERROR)) 
			log(Level.ERROR, message, t, null);
	}

	public static void fatal(String message) {
		if (logger.isEnabledFor(Level.FATAL)) 
			log(Level.FATAL, message, null, null);
	}
	
	/**
	 * @title	실제 로깅 작업
	 */
	public static void log(Level level, String message, Throwable t, Object ref) {
		try {			
			if ( initFlag ) {
				Config conf = Config.getInstance();
				String logLocation = conf.getStringByServerEnv("/config/neo/mapping/log/log-file");				
				System.out.println("logLocation(log4j) :: "+logLocation);
				
				File readFile = new File(logLocation);
				if (readFile.canRead()) {
					PropertyConfigurator.configure(logLocation);
				}
				
				initFlag = false;
			}
			
			message = message.replaceAll("\\s*\n", "\n");

			if (ref != null) {
				if ( ref.getClass().getName().equals("java.lang.String") ) {
					message = "[" + ref.toString() + "]" +  message;
				} else {
					message = "[" + ref.getClass().getName() + "]" +  message;
				}
			}

			logger.log(level, message, t);
		
		 } catch ( Exception e ) {
			 System.out.println("=================================");
			 System.out.println(">>> log4j error :: "+ e.getMessage());
			 System.out.println("=================================");
		 }
	}
}