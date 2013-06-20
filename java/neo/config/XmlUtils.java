package neo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import neo.exception.ConfigException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 	@Class Name	: 	XmlUtils.java
 * 	@파일설명		: 	
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
public abstract class XmlUtils {

	protected static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	protected static DocumentBuilderFactory vfactory = DocumentBuilderFactory.newInstance();

	public abstract String evalToString(Document doc, String str)
		throws ConfigException;
		
	public abstract NodeList evalToNodeList(Document doc, String str)
		throws ConfigException;
		
	public abstract Node selectSingleNode(Document doc, String str)
		throws ConfigException;
		
	protected static Class utilsClass = null;

	static {
		factory.setValidating(false);
		vfactory.setValidating(true);
	}

	static {
		String className = "neo.config.XmlPathUtils";
		try {
			ClassLoader classLoader =	Thread.currentThread().getContextClassLoader();
			utilsClass = classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static XmlUtils getImpl() throws ConfigException {
		try {
			if (utilsClass == null)
				throw new InternalError("Don't have a XmlUtils implementation");
			return (XmlUtils) utilsClass.newInstance();
		} catch (InternalError e) {
			throw new ConfigException("XmlUtils class exists", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigException("Couldn't instantiate the XmlUtils implementation", e);
		}
	}

	public static DocumentBuilder createBuilder(boolean validating)
		throws ConfigException {
		try {
			DocumentBuilder builder = null;
			if (validating) {
				builder = vfactory.newDocumentBuilder();
			} else {
				builder = factory.newDocumentBuilder();
			}
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicID, String systemID) {
					publicID = System.getProperty("neo.home") + "/conf/" + publicID;
					systemID = System.getProperty("neo.home") + "/conf/" + systemID;
					if (publicID != null && publicID.endsWith(".dtd")) {
						try {
							FileInputStream in = new FileInputStream(publicID);
							if (in != null) {
								return new InputSource(in);
							}
						} catch (FileNotFoundException e) {
							throw new InternalError(e.getMessage());
						}
					}
					return null;
				}
			});
			return builder;
		} catch (InternalError e) {
			throw new ConfigException("Error caused by InternalError on parsing", e);
		} catch (ParserConfigurationException e) {
			throw new ConfigException("Error caused by ParserConfigurationException on parsing", e);
		}
	}

	public static Document createEmptyDocument() throws Exception {
		try {
			return createBuilder(false).newDocument();
		} catch (Exception e) {
			throw e;
		}
	}

	public static Document parse(File file, boolean validating)
		throws ConfigException {
		try {
			return createBuilder(validating).parse(file);
		} catch (IOException e) {
			throw new ConfigException("Error caused by IOException on parsing", e);
		} catch (SAXException e) {
			throw new ConfigException("Error caused by SAXException on parsing",	e);
		}
	}

	public static Document parse(InputStream in, boolean validating)
		throws ConfigException {
		try {
			return createBuilder(validating).parse(in);
		} catch (IOException e) {
			throw new ConfigException("Error caused by IOException on parsing", e);
		} catch (SAXException e) {
			throw new ConfigException("Error caused by SAXException on parsing", e);
		}
	}

}
