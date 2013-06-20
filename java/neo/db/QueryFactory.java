package neo.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import neo.config.Config;

import neo.exception.QueryException;
import neo.exception.ConfigException;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	QueryFactory.java
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
public class QueryFactory {

	private static final QueryFactory singleton = new QueryFactory();

	private Map map = null;
	private static final String PROP_PREFIX = "/config/neo/mapping/xml-query<";
	private static final String PROP_POSTFIX = ">/directory";

	private QueryFactory() {
		this.map = new java.util.HashMap();
	}

	public Query get(String fileName) throws QueryException {
		return get("default", fileName);
	}

	public Query get(String pSystem, String fileName) throws QueryException {

		try {
			Config conf = Config.getInstance();
			String sysValue = conf.getString(PROP_PREFIX + pSystem + PROP_POSTFIX, null);
			String filePath = "";

			if (sysValue == null)
				throw new QueryException("Can't find Query Directory Information for '"+ pSystem+ "' system from configuration file.");

			if (sysValue.endsWith("/") && fileName.startsWith("/"))
				filePath = sysValue + fileName.substring(1);
			else if (!sysValue.endsWith("/") && !fileName.startsWith("/"))
				filePath = sysValue + "/" + fileName;
			else
				filePath = sysValue + fileName;

			String fullFilePath =	filePath.endsWith(".sql") ? filePath : filePath + ".sql";

			if (!map.containsKey(fullFilePath))
				map.put(fullFilePath, createQuery(fullFilePath));

			return (Query) map.get(fullFilePath);

		} catch (ConfigException ce) {
			throw new QueryException("Can't load Configuration Info.");
		}
	}

	private Query createQuery(String filePath) throws QueryException {
		return new Query(extractQuery(filePath));
	}

	private String extractQuery(String filePath) throws QueryException {

		FileInputStream inStream = null;
		BufferedReader in = null;

		StringBuffer sb = new StringBuffer();

		try {
			File file = new File(filePath);

			if (!file.canRead())
				throw new QueryException("Can not extract sql File from '"+ filePath+ "'.[file.canRead()-Error]");

			inStream = new FileInputStream(file);
			in = new BufferedReader(new InputStreamReader(inStream));

			String line = in.readLine();

			if (line == null) {
				return "";
			}
			while (in.ready()) {
				if (line.length() > 0 && line.charAt(0) != '-')
					sb.append(trimSpace(line) + "\n");
				line = in.readLine();
			}

			if (line.length() > 0 && line.charAt(0) != '-')
				sb.append(trimSpace(line));

			return sb.toString();
		} catch (Exception e) {
			throw new QueryException("Can not extract sql File from '"+ filePath+ "'.["+ e.getMessage()+ "]");
		} finally {
			try {
				if (inStream != null)
					inStream.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				Log.error("Error whil Closing streams - LQueryFactory.extractQuery() "+ e.getMessage(), this);
			}
		}
	}

	private String trimSpace(String s) {

		int position = s.indexOf("--");
		String target = "";

		if (position == -1)
			target = s;
		else
			target = s.substring(0, position);

		return target;
	}

	public static QueryFactory getInstance() {
		return singleton;
	}

	public void reset() {
		synchronized (this) {
			if (map != null)
				map.clear();
		}
	}
}