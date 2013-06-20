package neo.page;

import java.util.regex.Pattern;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	PageConstants.java
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
public class PageConstants {

	public static String	MSSQL_KEY_FIELD 			= "MSSQL_KEY_FIELD";
	public static String	PAGING_ROW_START 			= "PAGING_ROW_START";
	public static String	PAGING_ROW_END 				= "PAGING_ROW_END";
	public static String	PAGING_ROW_SIZE 			= "PAGING_ROW_SIZE";

	public static String	DEFAULT 					= "default";
	public static String	ROWS 						= "rows";
	public static String	TARGET_ROW 					= "targetRow";
	public static String	NEO_ORDER_BY 				= "neoOrderBy";
	public static String	PAGE_SPEC 					= "pageSpec";

	public static String	PAGE_INDEX 					= "PAGE_INDEX";
	public static String	PAGE_RESULT 				= "PAGE_RESULT";
	public static String	NUMBER_OF_ROWS_OF_PAGE 		= "NUMBER_OF_ROWS_OF_PAGE";
	public static String	NUMBER_OF_PAGES_OF_INDEX 	= "NUMBER_OF_PAGES_OF_INDEX";
	public static String	REQUEST_DATA_SINGLE_MODE	= "REQUEST_DATA_SINGLE_MODE";

	private static String	DEFAULT_PAGE_STATEMENT 		= "neo.page.OraclePageStatement";
	private static String	DEFAULT_PAGE_DAO 			= "neo.page.OraclePageDao";
	private static String	DEFAULT_PAGE_NAVIGATION 	= "neo.page.DefaultPageNavigationByPost";
	private static int 		DEFAULT_ROW_SIZE 		= 15;
	private static int 		DEFAULT_PAGE_SIZE 		= 10;

	private static String	MORE_THAN_ONCE 				= "+";
	private static String	ZERO_OR_MORE 				= "*";
	private static String	BLANK 						= "\\s" + ZERO_OR_MORE;
	private static String	LPAREN 						= "\\(";
	private static String	RPAREN 						= "\\)";
	private static String	COMMA 						= "\\,";
	private static String	WORD 						= "\\w";
	private static String	Q_MARK_ONLY 				= "[\\?]";
	private static String	Q_MARK 						=	BLANK
														+ WORD
														+ ZERO_OR_MORE
														+ LPAREN
														+ ZERO_OR_MORE
														+ BLANK
														+ Q_MARK_ONLY
														+ BLANK
														+ RPAREN
														+ ZERO_OR_MORE;
	private static String	VAR 						= "[\\.]|" + BLANK + "(" + WORD + MORE_THAN_ONCE + ")" + BLANK;
	private static String	UNARY_OPERATOR 				= "(=|like|<|>|<=|>=|<>|!=)" + Q_MARK;
	private static String	BETWEEN_PATTERN 			= "(between)" + Q_MARK + "(and)" + Q_MARK;
	private static String	IN_PATTERN 					= "(in)"
														+ BLANK
														+ LPAREN
														+ "("
														+ Q_MARK
														+ BLANK
														+ COMMA
														+ ZERO_OR_MORE
														+ BLANK
														+ ")"
														+ MORE_THAN_ONCE
														+ RPAREN;
	public static Pattern	QUESTION_MARK_PATTERN 		= Pattern.compile(Q_MARK_ONLY);
	public static Pattern	SQL_STMT_PATTERN 			= Pattern.compile(VAR
														+ "("
														+ UNARY_OPERATOR
														+ "|"
														+ BETWEEN_PATTERN
														+ "|"
														+ IN_PATTERN
														+ ")", Pattern.CASE_INSENSITIVE);
	public static Pattern	SQL_ORDER_BY_PATTERN 		= Pattern.compile(
														BLANK + "order" + BLANK + MORE_THAN_ONCE + "by" + BLANK,	Pattern.CASE_INSENSITIVE);

	public static Pattern	SQL_STRING_LITERAL_SKIP_PATTERN = Pattern.compile(BLANK + "\\'.*\\'", Pattern.CASE_INSENSITIVE);
	public static int 	SQL_COL_NAME_INDEX 			= 1;
	public static int 	SQL_Q_MARK_GROUP_INDEX 		= 2;
	public static int 	SQL_UNARY_OPERATOR_INDEX 	= 3;
	public static int 	SQL_BETWEEN_OPERATOR_INDEX 	= 4;
	public static int 	SQL_IN_OPERATOR_INDEX 		= 6;

	private static String getValueFromConfig(String configKey, String defaultVal)
		throws PageException {
		String configValue = null;
		try {
			Config conf = Config.getInstance();
			configValue = conf.getString(configKey, defaultVal).trim();
		} catch (ConfigException le) {
			throw new PageException(le.getCode(), le.getMessage(), le);
		}
		return configValue;
	}

	private static int getInt(String configKey, int defaultVal)
		throws PageException {
		int configValue = defaultVal;
		try {
			Config conf = Config.getInstance();
			configValue = conf.getInt(configKey, defaultVal);
		} catch (ConfigException le) {
			throw new PageException(le.getCode(), le.getMessage(), le);
		}
		return configValue;
	}

	public static int getNumberOfRowsOfPage(String pageSpec)
		throws PageException {
		String numberOfRowsOfPageConfig = "/config/neo/page/spec<" + pageSpec + ">/row-per-page";
		return getInt(numberOfRowsOfPageConfig, DEFAULT_ROW_SIZE);

	}

	public static int getNumberOfPagesOfIndex(String pageSpec)
		throws PageException {
		String numberOfPagesOfIndexConfig = "/config/neo/page/spec<" + pageSpec + ">/page-per-block";
		return getInt(numberOfPagesOfIndexConfig, DEFAULT_PAGE_SIZE);
	}

	public static String getPageStatementClassName(String pageSpec)
		throws PageException {
		String pageStatementClassName = "/config/neo/page/spec<" + pageSpec + ">/statement";
		return getValueFromConfig(pageStatementClassName, DEFAULT_PAGE_STATEMENT);
	}

	public static String getPageDaoClassName(String pageSpec)
		throws PageException {
		String pageDaoClassName = "/config/neo/page/spec<" + pageSpec + ">/dao";
		return getValueFromConfig(pageDaoClassName, DEFAULT_PAGE_DAO);
	}

	public static String getPageNavigationClassName(String pageSpec)
		throws PageException {
		String pageDaoClassName = "/config/neo/page/spec<" + pageSpec + ">/navigation";
		return getValueFromConfig(pageDaoClassName, DEFAULT_PAGE_NAVIGATION);
	}
}