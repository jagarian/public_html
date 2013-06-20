package neo.util.system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

import neo.util.comm.DateUtil;
import neo.util.comm.StringUtil;
import neo.util.file.FileUtil;

import org.apache.commons.lang.SystemUtils;

/**
 * 	@Class Name	: 	SystemUtil.java
 * 	@���ϼ���		: 	
 *	
 *	System.getProperties().list(System.out);
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
public class SystemUtil {
	
	/**
	 * �� �Ǵ� �α׿��� ��� �� �����ڷ� �����ؼ� �ý��� ������ ����Ѵ�.
	 * 
	 */
	public static String toJavaInfo(String endDelimitor) {		
		String addStr = "";
		addStr += "SystemUtils.FILE_ENCODING					: " + SystemUtils.FILE_ENCODING+endDelimitor; 
		addStr += "SystemUtils.FILE_SEPARATOR					: " + SystemUtils.FILE_SEPARATOR+endDelimitor; 
		addStr += "SystemUtils.JAVA_CLASS_PATH					: " + SystemUtils.JAVA_CLASS_PATH+endDelimitor; 
		addStr += "SystemUtils.JAVA_CLASS_VERSION				: " + SystemUtils.JAVA_CLASS_VERSION+endDelimitor; 
		addStr += "SystemUtils.JAVA_COMPILER					: " + SystemUtils.JAVA_COMPILER+endDelimitor; 
		addStr += "SystemUtils.JAVA_EXT_DIRS					: " + SystemUtils.JAVA_EXT_DIRS+endDelimitor; 
		addStr += "SystemUtils.JAVA_HOME						: " + SystemUtils.JAVA_HOME    +endDelimitor; 
		addStr += "SystemUtils.JAVA_IO_TMPDIR					: " + SystemUtils.JAVA_IO_TMPDIR+endDelimitor; 
		addStr += "SystemUtils.JAVA_LIBRARY_PATH				: " + SystemUtils.JAVA_LIBRARY_PATH+endDelimitor; 
		addStr += "SystemUtils.JAVA_RUNTIME_NAM					: " + SystemUtils.JAVA_RUNTIME_NAME+endDelimitor; 
		addStr += "SystemUtils.JAVA_RUNTIME_VERSION				: " + SystemUtils.JAVA_RUNTIME_VERSION+endDelimitor; 
		addStr += "SystemUtils.JAVA_SPECIFICATION_NAME			: " + SystemUtils.JAVA_SPECIFICATION_NAME+endDelimitor; 
		addStr += "SystemUtils.JAVA_SPECIFICATION_VENDOR		: " + SystemUtils.JAVA_SPECIFICATION_VENDOR +endDelimitor;
		addStr += "SystemUtils.JAVA_SPECIFICATION_VERSION		: " + SystemUtils.JAVA_SPECIFICATION_VERSION+endDelimitor; 
		addStr += "SystemUtils.JAVA_VENDOR						: " + SystemUtils.JAVA_VENDOR +endDelimitor; 
		addStr += "SystemUtils.JAVA_VENDOR_URL					: " + SystemUtils.JAVA_VENDOR_URL+endDelimitor; 
		addStr += "SystemUtils.JAVA_VERSION						: " + SystemUtils.JAVA_VERSION +endDelimitor;  
		addStr += "SystemUtils.JAVA_VM_INFO						: " + SystemUtils.JAVA_VM_INFO +endDelimitor;  
		addStr += "SystemUtils.JAVA_VM_NAME						: " + SystemUtils.JAVA_VM_NAME +endDelimitor; 
		addStr += "SystemUtils.JAVA_VM_SPECIFICATION_NAME		: " + SystemUtils.JAVA_VM_SPECIFICATION_NAME+endDelimitor; 
		addStr += "SystemUtils.JAVA_VM_SPECIFICATION_VENDOR		: " + SystemUtils.JAVA_VM_SPECIFICATION_VENDOR+endDelimitor; 
		addStr += "SystemUtils.JAVA_VM_SPECIFICATION_VERSION	: " + SystemUtils.JAVA_VM_SPECIFICATION_VERSION+endDelimitor; 
		addStr += "SystemUtils.JAVA_VM_VENDOR					: " + SystemUtils.JAVA_VM_VENDOR+endDelimitor; 
		addStr += "SystemUtils.JAVA_VM_VERSION					: " + SystemUtils.JAVA_VM_VERSION+endDelimitor; 
		addStr += "SystemUtils.LINE_SEPARATOR					: " + SystemUtils.LINE_SEPARATOR+endDelimitor; 
		addStr += "SystemUtils.OS_ARCH							: " + SystemUtils.OS_ARCH+endDelimitor; 
		addStr += "SystemUtils.OS_NAME							: " + SystemUtils.OS_NAME+endDelimitor; 
		addStr += "SystemUtils.OS_VERSION						: " + SystemUtils.OS_VERSION+endDelimitor; 
		addStr += "SystemUtils.PATH_SEPARATOR					: " + SystemUtils.PATH_SEPARATOR+endDelimitor; 
		addStr += "SystemUtils.USER_COUNTRY						: " + SystemUtils.USER_COUNTRY+endDelimitor; 
		addStr += "SystemUtils.USER_DIR							: " + SystemUtils.USER_DIR+endDelimitor; 
		addStr += "SystemUtils.USER_HOME						: " + SystemUtils.USER_HOME+endDelimitor; 
		addStr += "SystemUtils.USER_LANGUAGE					: " + SystemUtils.USER_LANGUAGE+endDelimitor; 
		addStr += "SystemUtils.USER_NAME						: " + SystemUtils.USER_NAME+endDelimitor; 
		addStr += "SystemUtils.JAVA_VERSION_FLOAT				: " + SystemUtils.JAVA_VERSION_FLOAT +endDelimitor; 
		addStr += "SystemUtils.JAVA_VERSION_INT					: " + SystemUtils.JAVA_VERSION_INT+endDelimitor; 
		addStr += "SystemUtils.IS_JAVA_1_1						: " + SystemUtils.IS_JAVA_1_1+endDelimitor; 
		addStr += "SystemUtils.IS_JAVA_1_2						: " + SystemUtils.IS_JAVA_1_2+endDelimitor; 
		addStr += "SystemUtils.IS_JAVA_1_3 						: " + SystemUtils.IS_JAVA_1_3+endDelimitor; 
		addStr += "SystemUtils.IS_JAVA_1_4						: " + SystemUtils.IS_JAVA_1_4+endDelimitor; 
		addStr += "SystemUtils.IS_JAVA_1_5						: " + SystemUtils.IS_JAVA_1_5+endDelimitor; 
		addStr += "SystemUtils.IS_OS_AIX						: " + SystemUtils.IS_OS_AIX+endDelimitor; 
		addStr += "SystemUtils.IS_OS_HP_UX						: " + SystemUtils.IS_OS_HP_UX+endDelimitor; 
		addStr += "SystemUtils.IS_OS_IRIX						: " + SystemUtils.IS_OS_IRIX+endDelimitor; 
		addStr += "SystemUtils.IS_OS_LINUX						: " + SystemUtils.IS_OS_LINUX+endDelimitor; 
		addStr += "SystemUtils.IS_OS_MAC						: " + SystemUtils.IS_OS_MAC+endDelimitor; 
		addStr += "SystemUtils.IS_OS_MAC_OSX					: " + SystemUtils.IS_OS_MAC_OSX+endDelimitor; 
		addStr += "SystemUtils.IS_OS_OS2						: " + SystemUtils.IS_OS_OS2+endDelimitor; 
		addStr += "SystemUtils.IS_OS_SOLARIS					: " + SystemUtils.IS_OS_SOLARIS+endDelimitor; 
		addStr += "SystemUtils.IS_OS_SUN_OS						: " + SystemUtils.IS_OS_SUN_OS+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS					: " + SystemUtils.IS_OS_WINDOWS+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_2000				: " + SystemUtils.IS_OS_WINDOWS_2000+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_95					: " + SystemUtils.IS_OS_WINDOWS_95+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_98					: " + SystemUtils.IS_OS_WINDOWS_98+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_ME					: " + SystemUtils.IS_OS_WINDOWS_ME+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_NT					: " + SystemUtils.IS_OS_WINDOWS_NT+endDelimitor; 
		addStr += "SystemUtils.IS_OS_WINDOWS_XP					: " + SystemUtils.IS_OS_WINDOWS_XP+endDelimitor;                                                             
		return addStr;
	}
	
	/**
	 * �ý��� ������ ���Ϸ� ���� �� ������ �д´�.
	 * �ӽ÷� ���� ������ �����Ѵ�.
	 */
	public static String toJava2Info() throws FileNotFoundException {		
		String addStr = "";
		String fileNm = System.getProperty("user.dir")+"\\log\\"+"system.properties_"+DateUtil.getCurrentTimeString();
		try {
			System.getProperties().list(new PrintStream(new FileOutputStream(fileNm))); 
		} catch (Exception e ) {			
		}		
		addStr = FileUtil.readFromFile(fileNm);
		addStr = StringUtil.rplc(addStr, "line.separator=\n\n", "");
		addStr = StringUtil.rplc(addStr, "\n", "<br>");
		FileUtil.deleteFile(fileNm);
		return addStr;
	}
	
	/**
	 * �ý��� ���� ���
	 * 
	 */
	public static String toSystemInfo() {		
		String addStr = "";
		String endDelimitor = "<br>";
		for (Map.Entry entry: System.getenv().entrySet())
			addStr += entry.getKey() + "=" + entry.getValue() + endDelimitor;                
		return addStr;
	}
}
