package neo.util.log;

import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import neo.config.Config;

/**
 * @Class Name 	: Log.java * 
 * @���ϼ��� 		: Log4J�ϳ������ε� ������ϱ� ���� �Ǵ볪�� ��� ��κ��̴� ���α׷��� �ȴ�. ^^;
 *                   (default �޼��� ������ info �̻����� ����.)
 * 
 * http://apache.tt.co.kr/logging/log4j/1.2.15/apache-log4j-1.2.15.zip  ���� log4j �ٿ�ε�
 * (�� ����� log4j 2.0 �������� ��������, �ŷ��� �� �ִ� ������ �ƴϱ� ������, ���� log4 1.2�� �����Ѵ�.)
 * 
 *	- fatal : ���� �ð��ȿ� �ý����� ���߰� �Ҽ� �ִ� ������ ��� fatal ������ ����Ͽ� �α׸� �����.
 *	- error : ��Ÿ ��Ÿ�� ������ ������� ���� ���¿����� error ������ ����Ͽ� �α׸� �����.
 *	- warn : deprecated �� API ����̳� error�� ����� �߸��� API ��배�� ��� warn ������ ����Ͽ� �α׸� �����. �ݵ�� �߸��� ��쿡�� ����ϴ� �� �ƴϴ�.
 *	- info : �ý��� �����̳� ����ó�� ���ɰ������� ��Ÿ�� �̺�Ʈ�� ��� info ������ ����Ͽ� �α׸� �����.
 *	- debug : �ý��� �帧�� �ڼ��� ������ ���ϸ� debug ������ ����Ͽ� �α׸� �����.
 *	- trace : ���� �ڼ��� ������ ����� ���Ҷ� trace ������ ����Ͽ� �α׸� �����.
 * 
 * ��뿹 : Log.info("selectCount BEGIN", this);
 * 
 * �ٸ� ��뿹 : �̰͸����ε� Log.info ���� ���� �� ���� �����ϰ� ���� �α� ����� ����� �� �ִ�.
 *                    log4j.properties ������ Ŭ�����н��� �ɸ� ���丮�� �θ� �ȴ�. �� ���ø����̼��� WEB-INF/classes�� �θ� �ȴ�
 *                    
 *	private Logger log = Logger.getLogger(ClassName.class); //�ǵ��� static ���� �ΰŸ� �������� ����
 * 	//Ȥ�� private Logger log = Logger.getLogger(this.getClass());
 *	public void method() {
 *      
 *      //isInfoEnabled
 *		if (log.isDebugEnabled()) {
 *			Log.info("������ �޽���");
 *		}
 *		Log.info("������ ��������� �޽���");
 *        
 *		try {
 *			//��¼�� ��¼�� ����...
 *		} catch (Exception ex) {
 *			//�α׿� ���� �޽����� �Բ� �����
 *			Log.info("���ܰ� �߻��߾��~~", ex);
 *		}
 *	}
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
	 * @title	������Ʈ�� �ѱ�� �α�
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
	 * @title	�α׸� �ѱ�� �α�
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
	 * @title	���� �α� �۾�
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