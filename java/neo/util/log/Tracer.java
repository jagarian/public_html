package neo.util.log; 

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import neo.config.Config;
import neo.exception.ConfigException;

/**
 * 	@Class Name	: 	Tracer.java
 * 	@���ϼ���		: 	
 * ������Ʈ�� �����߿� �߻��ϴ� ���� ���ܿ� ���� �α׸� �߻���Ų��.
 * <br>
 * ������ ���ܰ� �߻��Ͽ����� ����ó�� ������ TRACER�� ȣ���Ͽ���
 * �α׸� ����� �ִ�.
 * �� ������Ʈ������ ���� import ��Ų��, static���� ����� �޼ҵ带
 * ������ parameter ���� �Բ� ȣ���ϸ� �ȴ�.
 * TRACER�� �ý��� ȯ�氪�� ���� weblogic.properties�� �����Ǿ� �ִ�.
 * <p>
 *
 * TRACER�� �α� ������ ũ�� 3������ ���еȴ�. <br>
 * I: Information Level <br>
 * D: Debug Level <br>
 * E: Error Level <br>
 * 
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

	// log file�� suffix�� ���� ���� ��¥ ����
    static DateFormat today = new SimpleDateFormat("yyyyMMdd");
	// log file ��θ� rdzEnv.xml file�� ���� ȹ��
	static String logFilePath;	
	static String logFileName;

	// initialize
	static {
		if (logFilePath == null || logFilePath.length() == 0) {	
			
			try {
				System.out.println("trace.xml ������ �������� �ʽ��ϴ�.");
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
					System.out.println("'log' ���丮�� �����մϴ�.");
				} else {
					logFilePath.mkdir();
					System.out.println("'log' ���丮�� �����մϴ�.");
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
   	 * �α��� ��� ������ �����Ѵ�.
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
   	 * ������ ����̽��� �α׸� ����Ѵ�.
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
   	 * ������ ����̽��� �α׸� ����Ѵ�.
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
   	 * ������ ����̽��� �α׸� ����Ѵ�.
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
   	 * ������ ����̽��� �α׸� ����Ѵ�.
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
   	 * ������ ����̽��� �α׸� ����Ѵ�.
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
   	 * Tracer�� ��� �÷��׵��� ��尪�� active�� ����.
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
   	 * Tracer�� �������� �÷��׵��� ��尪�� active�� ����.
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
   	 * ���� ���α׷��� ǥ�� �Է�(args[])���� �Էµ� arguments����
   	 * �Ľ��Ͽ� �÷��� �������� ����.
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
   	 * ���� Tracer�� ��� ������ �ʱ�ȭ�Ѵ�.
   	 *
   	 */
    protected static final void reset()
    {
        levels = "";
        out = new PrintWriter(System.out);
        trace_all = false;
    }


    /**
   	 * ������ ��¹����� �����Ѵ�.
   	 * ����Ʈ�� System.out�� �����ȴ�.
   	 *
   	 * @param printwriter				PrintWriter
	 *
	 */
    public static final void setPrintWriter(PrintWriter printwriter)
    {
        out = printwriter;
    }


    /**
   	 * ��� Tracer�� �÷��׸� active�� ����
   	 */
    public static final void turnOnAllTraces()
    {
        addTraceFlag('+');
    }

}
