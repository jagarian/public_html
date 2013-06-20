package neo.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.exception.QueryException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 	@Class Name	: 	QueryXmlFactory.java
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
public class QueryXmlFactory {

	// private static LQueryXmlFactory singleton = null;
	private static final QueryXmlFactory singleton = new QueryXmlFactory();

	private Map map = null;
	private static final String PROP 			= "/config/neo/mapping/xml-query/directory";
	private static final String SEPERATOR		= "--";
	private static final String PROP_STATEMENT	= "statement";
	private static final String USE_XML_PARSER	= "/config/neo/mapping/xml-query/use-xml-parser";
	private static String PATTERN_STR 			= "<statement"
													+ "\\s+"
													+ "name\\s*=\\s*\""
													+ "(\\w+)"
													+ "\"\\s*>"
													+ "(.*?)"
													+ "</statement>";
	private static Pattern XML_QUERY_PATTERN =
		Pattern.compile(PATTERN_STR, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

	private QueryXmlFactory() {
		this.map = new java.util.HashMap();
	}

	public Query get(String statementName) throws QueryException {
		return get("query", statementName);
	}

	public Query get(String fileName, String statementName)
		throws QueryException {
		try {
			Config conf = Config.getInstance();
			String path = conf.getStringByServerEnv(PROP);

			if (path == null) {
				throw new QueryException("Can't find Query xml File directory information from config file.");
			}
			String filePath = "";

			if (path.endsWith("/") && fileName.startsWith("/"))
				filePath = path + fileName.substring(1);
			else if (!path.endsWith("/") && !fileName.startsWith("/"))
				filePath = path + "/" + fileName;
			else
				filePath = path + fileName;

			String fullFilePath = filePath.endsWith(".xml") ? filePath : filePath + ".xml";

			if (!map.containsKey(fullFilePath + SEPERATOR + statementName)) {
				boolean use_xml_parser = conf.getBoolean(USE_XML_PARSER, false);
				if (use_xml_parser)
					setQueriesXMLParser(fullFilePath);
				else
					setQueriesRegExp(fullFilePath);
			}

			return (Query) map.get(fullFilePath + SEPERATOR + statementName);

		} catch (ConfigException ce) {
			throw new QueryException("Can't load Configuration Info.");
		}
	}

	private void setQueriesXMLParser(String xmlFilePath) throws QueryException {

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(xmlFilePath));
			Element root = doc.getDocumentElement();

			NodeList children = root.getElementsByTagName(PROP_STATEMENT);

			for (int i = 0; i < children.getLength(); i++) {
				Element statement = (Element) children.item(i);
				Attr attrib = statement.getAttributeNode("name");
				String statementName = null;

				if (attrib != null)
					statementName = attrib.getValue();

				if (statementName == null)
					throw new QueryException();

				String query = statement.getFirstChild().getNodeValue();

				map.put(xmlFilePath + SEPERATOR + statementName, new Query(query));
			}
		} catch (IOException e) {
			throw new QueryException("Can't load a File : " + e.toString());
		} catch (org.xml.sax.SAXException se) {
			throw new QueryException("Can't process xml file : " + se.toString());
		} catch (ParserConfigurationException pce) {
			throw new QueryException("Can't process xml file : " + pce.toString());
		}
	}

	private String readFileToString(String file) throws IOException {
		InputStream input = new FileInputStream(file);
		InputStreamReader in = new InputStreamReader(input);
		String fileContents = "";
		try {
			StringWriter sw = new StringWriter();
			copy(in, sw);
			fileContents = sw.toString();
		} finally {
			if (in != null)
				in.close();
			if (input != null)
				input.close();
		}
		return fileContents;
	}

	private final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public int copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	private void setQueriesRegExp(String xmlFilePath) throws QueryException {

		try {
			String fileContents = readFileToString(xmlFilePath);
			Matcher sqlMatch = XML_QUERY_PATTERN.matcher(fileContents);
			while (sqlMatch.find()) {
				String statementName = sqlMatch.group(1);
				String query = sqlMatch.group(2);
				map.put(xmlFilePath + SEPERATOR + statementName, new Query(query));
			}
		} catch (IOException e) {
			throw new QueryException("Can't load a File : " + e.toString());
		} catch (Exception se) {
			throw new QueryException("Can't process xml file : " + se.toString());
		}
	}

	public static QueryXmlFactory getInstance() {
		return singleton;
	}

	public void reset() {
		synchronized (this) {
			if (map != null)
				map.clear();
		}
	}
}