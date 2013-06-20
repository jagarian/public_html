package neo.util.log; 

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import neo.config.Config;
import neo.exception.ConfigException;

/**
 * 	@Class Name	: 	Tracer.java
 * 	@파일설명		: 	
 * 컴포넌트의 실행중에 발생하는 각종 예외에 대한 로그를 발생시킨다.
 * <br>
 * 때문에 예외가 발생하였을시 예외처리 이전에 TRACER롤 호출하여야
 * 로그를 남길수 있다.
 * 각 컴포넌트내에서 먼저 import 시킨뒤, static으로 선언된 메소드를
 * 적당한 parameter 값과 함께 호출하면 된다.
 * TRACER의 시스템 환경값은 현재 weblogic.properties에 설정되어 있다.
 * <p>
 *
 * TRACER의 로그 레벨은 크게 3가지로 구분된다. <br>
 * I: Information Level <br>
 * D: Debug Level <br>
 * E: Error Level <br>
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
public final class Tracer
{
	public static final char INFO	= 'I';
    public static final char DEBUG	= 'D';
    public static final char ERROR	= 'E';
    public static final DateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy:");
    public static final String COMMANDLINE_OPTION = "-trace";
    public static final String FILE_OUTPUTDIRECTION = "file";
    public static final char TRACER_ALL_LOGARKS_FLAG = '+';

	private static String DEFAUL_LOG_FILE_NAME = "default.log";
	private static String outputDirection = "";
    private static String levels = "";
    private static boolean trace_all = false;

    private static PrintWriter out = new PrintWriter(System.out);
	private static FileWriter filewriter = null;

	// log file의 suffix로 사용될 현재 날짜 포멧
    static DateFormat today = new SimpleDateFormat("yyyyMMdd");
	// log file 경로를 rdzEnv.xml file로 부터 획득
	static String logFilePath;	
	static String logFileName;

	// initialize
	static {
		if (logFilePath == null || logFilePath.length() == 0) {	
			
			try {
				System.out.println("trace.xml 파일이 존재하지 않습니다.");
				Config insConf = null;
				String logLocation = "";
				String tempStr = "";
				
				insConf = Config.getInstance();				
				logLocation = insConf.getStringByServerEnv("/config/neo/mapping/log/log-dir");
				tempStr = insConf.getStringByServerEnv("/config/neo/mapping/log/trace-all");
				levels = insConf.getStringByServerEnv("/config/neo/mapping/log/trace-level");
				logFileName = insConf.getStringByServerEnv("/config/neo/mapping/log/log-file-name");
				System.out.println("logLocation(Tracer) :: "+logLocation);
				System.out.println("trace_all :: "+tempStr);
				System.out.println("levels :: "+levels);
				System.out.println("logFileName :: "+logFileName);
				
				if ( tempStr.toLowerCase().equals("true") )
					trace_all = true;
				File logFilePath = new File(logLocation);
				if (logFilePath.exists() && logFilePath.isDirectory()) {
					System.out.println("'log' 디렉토리가 존재합니다.");
				} else {
					logFilePath.mkdir();
					System.out.println("'log' 디렉토리를 생성합니다.");
				}
			} catch (ConfigException e) {
				System.out.println("=================================");
				System.out.println(">>> Tracer error :: "+ e.getMessage());
				System.out.println("=================================");
			}	
		}

		if (logFileName == null || logFileName.length() == 0) 
			logFileName = DEFAUL_LOG_FILE_NAME;
	}


	/**
	 * Constructs a <code>TRACER</code> object
	 */
    public Tracer() {}


	/**
   	 * 로그의 출력 방향을 설정한다.
   	 *
   	 * @param flag				String
  	 * @return void
   	 *
   	 */
	public static final void setOutputDirection(String flag)
	{
		outputDirection = flag;
	}


	/**
   	 * 지정된 디바이스로 로그를 출력한다.
   	 *
   	 * @param level					Log level
   	 * @param Objec					obj
   	 * @param log message			message
   	 *
   	 */
	public synchronized static final void LOG(char level, Object obj, String message)
	{
        if (trace_all || levels.indexOf(level) != -1) {
        	if (outputDirection != null) {
				if (logFileName == null )
        			logFileName = DEFAUL_LOG_FILE_NAME;

        		String FullLogFilePath = logFilePath + '/' + logFileName + '.' + today.format(new Date());

        		try {
        			filewriter = new FileWriter(FullLogFilePath, true);

        		} catch(Exception exception) {
        			Tracer.LOG(Tracer.ERROR, exception.getMessage());
				}

        		setPrintWriter( new PrintWriter(filewriter) );
        	}

            out.println(sdf.format(new Date()) + "<" + level + "> ---" + obj.getClass().getName() + "---: " + message);
            out.flush();

            if (filewriter != null) {
            	try {
					filewriter.close();
				} catch (Exception ex) {
					Tracer.LOG(Tracer.ERROR, ex.getMessage());
				}
			}
        }
    }

	/**
   	 * 지정된 디바이스로 로그를 출력한다.
   	 *
   	 * @param level						Log level
   	 * @param errMsg					Error Message
   	 *
   	 */
   	public synchronized static final void LOG(char level, String errMsg)
   	{
        if (trace_all || levels.indexOf(level) != -1) {
        	if (outputDirection != null) {
				if (logFileName == null )
        			logFileName = DEFAUL_LOG_FILE_NAME;

        		String FullLogFilePath = logFilePath + '/' + logFileName + '.' + today.format(new Date());

        		try {
        			filewriter = new FileWriter(FullLogFilePath, true);
        		} catch(Exception exception) {
        			Tracer.LOG(Tracer.ERROR, exception.getMessage());
				}

	        	setPrintWriter( new PrintWriter(filewriter) );
        	}

            out.println(sdf.format(new Date()) + "<" + level + "> " + errMsg);
            out.flush();

            if (filewriter != null) {
            	try {
					filewriter.close();
				} catch (Exception ex) {
					Tracer.LOG(Tracer.ERROR, ex.getMessage());
				}
			}
        }
    }


	/**
   	 * 지정된 디바이스로 로그를 출력한다.
   	 *
   	 * @param level						Log level
   	 * @param errMsg					Error Message
   	 * @param exception					Exception
   	 *
   	 */
    public synchronized static final void LOG(char level, String errMsg, Exception exception)
    {
        if (trace_all || levels.indexOf(level) != -1) {
        	if (outputDirection != null) {
				if (logFileName == null )
        			logFileName = DEFAUL_LOG_FILE_NAME;

        		String FullLogFilePath = logFilePath + '/' + logFileName + '.' + today.format(new Date());

        		try {
        			filewriter = new FileWriter(FullLogFilePath, true);
        		} catch(Exception e) {
        			Tracer.LOG(Tracer.ERROR, exception.getMessage());
				}

       			setPrintWriter( new PrintWriter(filewriter) );
        	}

            out.println(sdf.format(new Date()) + "<" + level + "> " + errMsg);
            exception.printStackTrace(out);
            out.flush();

            if (filewriter != null) {
            	try {
					filewriter.close();
				} catch (Exception ex) {
					Tracer.LOG(Tracer.ERROR, ex.getMessage());
				}
			}

        }
    }


	/**
   	 * 지정된 디바이스로 로그를 출력한다.
   	 *
   	 * @param level						Log level
	 * @param errMsg					Error Message
   	 * @param Objec						obj
   	 *
   	 */
    public synchronized static final void LOG(char level, String errMsg, Object obj)
    {
        if (trace_all || levels.indexOf(level) != -1) {

	    	if (outputDirection != null) {
				if (logFileName == null )
        			logFileName = DEFAUL_LOG_FILE_NAME;

        		String FullLogFilePath = logFilePath + '/' + logFileName + '.' + today.format(new Date());

        		try {
        			filewriter = new FileWriter(FullLogFilePath, true);
        		} catch(Exception exception) {
        			Tracer.LOG(Tracer.ERROR, exception.getMessage());
				}

       			setPrintWriter( new PrintWriter(filewriter) );
        	}

            out.println(sdf.format(new Date()) + "<" + level + "> ---" + obj.getClass().getName() + "---:" + errMsg);
            out.flush();

            if (filewriter != null) {
            	try {
					filewriter.close();
				} catch (Exception ex) {
					Tracer.LOG(Tracer.ERROR, ex.getMessage());
				}
			}
        }
    }


	/**
   	 * 지정된 디바이스로 로그를 출력한다.
   	 *
   	 * @param level						Log level
	 * @param errMsg					Error Message
   	 * @param Objec						obj
   	 * @param exception					Exception
   	 *
   	 */
    public synchronized static final void LOG(char level, String errMsg, Object obj, Exception exception)
    {
        if (trace_all || levels.indexOf(level) != -1) {

        	if (outputDirection != null) {
				if (logFileName == null )
        			logFileName = DEFAUL_LOG_FILE_NAME;

        		String FullLogFilePath = logFilePath + '/' + logFileName + '.' + today.format(new Date());

        		try {
        			filewriter = new FileWriter(FullLogFilePath, true);
        		} catch(Exception e) {
        			Tracer.LOG(Tracer.ERROR, exception.getMessage());
				}

        		setPrintWriter( new PrintWriter(filewriter) );
        	}

            out.println(sdf.format(new Date()) + "<" + level + "> ---" + obj.getClass().getName() + "---:" + errMsg);
            exception.printStackTrace(out);
            out.flush();

            if (filewriter != null) {
            	try {
					filewriter.close();
				} catch (Exception ex) {
					Tracer.LOG(Tracer.ERROR, ex.getMessage());
				}
			}
        }
    }


	public static final String getLogText(String filename)
	{
        FileInputStream in = null;
        String   line; 
        StringBuffer strf = new StringBuffer("");
        
        try 
        {
        	System.out.println(logFilePath + "/" + filename);
            in = new FileInputStream(logFilePath + "/" + filename);
        } catch (Throwable e) {
            return ("I/O Error");
        }
                
        BufferedReader dis = new BufferedReader(new InputStreamReader(in));

        // End-of-File is -1, but in this case as long as the
        // string returned is not null, continue
        try 
        {
              while ((line = dis.readLine()) != null) 
                	strf.append(asc2ksc(line)).append("\n");

              in.close();
              
              return strf.toString();
              
        } catch(IOException e) {
              return ("I/O Error");
        }
    }
	
	public static String asc2ksc(String str) throws UnsupportedEncodingException	{

		if (str == null) return null;

		String language = System.getProperty("user.language");
		if (language.equals("ko")) {
			return new String(str.getBytes("8859_1"), "KSC5601");
		} else {
			return str;
		}
	}


	/**
   	 * Tracer의 모든 플래그들의 모드값을 active로 변경.
   	 *
   	 * @param level						Log level
   	 *
   	 */
    public static final void addTraceFlag(char level)
    {
        if (level == '+')
            trace_all = true;
        else if (levels.indexOf(level) == -1)
            levels = levels + level;
    }


	/**
   	 * Tracer의 순차적인 플래그들의 모드값을 active로 변경.
   	 *
   	 * @param 	levels						Log level
   	 * @param 	messagecatalogexception		Message Catalog Exception
   	 * @exception               				Embeded Exception
   	 *
   	 */
    public static final void addTraceFlags(String levels)
    {
        for (int i=0; i<levels.length(); i++)
            addTraceFlag(levels.charAt(i));
    }


	/**
   	 * 메인 프로그램의 표준 입력(args[])으로 입력된 arguments들을
   	 * 파싱하여 플래그 설정값을 추출.
   	 *
   	 * @param args						String
   	 *
     */
    public static final void parseArgs(String[] args)
    {
        if (args == null)
			return;

        for (int i=0; i<args.length; i++) {
            if (args[i].equals("-trace")) {
                if (i == args.length - 1) {
                    return;
                } else {
                    addTraceFlags(args[i + 1]);
                    return;
                }
            }
        }
    }


	/**
   	 * 현재 Tracer의 모든 설정을 초기화한다.
   	 *
   	 */
    protected static final void reset()
    {
        levels = "";
        out = new PrintWriter(System.out);
        trace_all = false;
    }


    /**
   	 * 현재의 출력방향을 설정한다.
   	 * 디폴트로 System.out이 설정된다.
   	 *
   	 * @param printwriter				PrintWriter
	 *
	 */
    public static final void setPrintWriter(PrintWriter printwriter)
    {
        out = printwriter;
    }


    /**
   	 * 모든 Tracer의 플래그를 active로 설정
   	 */
    public static final void turnOnAllTraces()
    {
        addTraceFlag('+');
    }

}
