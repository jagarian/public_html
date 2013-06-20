package neo.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

import neo.page.PageConstants;
import neo.data.Param;
import neo.data.MultiParam;
import neo.util.comm.DefaultNaming;
import neo.util.log.Log;
import neo.exception.PageException;

/**
 * 	@Class Name	: 	SqlManager.java
 * 	@���ϼ���		: 	
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
final class SqlManager {

	private String sqlStatement;
	private String stringLiteralSkppedSqlStatement;
	private Map paramNameArrayCache = AccumulationHashMap.getInstance().getParamNameArrayCache();

	public SqlManager(String sql) {
		this.sqlStatement = sql;
		Matcher match = PageConstants.SQL_STRING_LITERAL_SKIP_PATTERN.matcher(this.sqlStatement);
		this.stringLiteralSkppedSqlStatement = match.replaceAll("");
	}

	public final Param getParameters(Param requestData) throws PageException {
		if (paramNameArrayCache.containsKey(this.sqlStatement)) {
			String[] paramNameArrayForCache = (String[]) paramNameArrayCache.get(this.sqlStatement);
			return processCacheHit(requestData, paramNameArrayForCache);
		} else {
			return processCacheFail(requestData);
		}
	}

	private void putValue(Param valueData, String paramValue, int valueIndex) {
		valueData.putString("" + (valueIndex), paramValue);
	}

	private String getStringValIgnoreCase(	Param pageData,
											String paramKey,
											Param paramData,
											int paramIndex)
		throws PageException {
		Iterator iter = pageData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String keyStr = (String) entry.getKey();
			if (paramKey.equalsIgnoreCase(keyStr)) {
				paramData.putString("" + paramIndex, keyStr);
				return (String) entry.getValue();
			}
		}
		throw new PageException("Value for PARAM[" + paramKey + "] Not Assigned from HTTPServletRequest.");
	}

	private Object getObjectValIgnoreCase(	Param pageData,
											String paramKey,
											Param paramData,
											int paramIndex)
		throws PageException {
		Iterator iter = pageData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String keyStr = (String) entry.getKey();
			if (paramKey.equalsIgnoreCase(keyStr)) {
				paramData.putString("" + paramIndex, keyStr);
				return entry.getValue();
			}
		}
		throw new PageException("Value for PARAM[" + paramKey + "] Not Assigned from HTTPServletRequest.");
	}

	private void checkNull(String paramKey, Object paramValue)
		throws PageException {
		if (paramValue == null)
			throw new PageException("Value for PARAM[" + paramKey + "] Not Assigned from HTTPServletRequest.");
	}

	private final Param processCacheHit(
		Param requestData,
		String[] paramNameArrayForCache)
		throws PageException {
		Param valueData = new Param("VALUE_Param");
		int valueIndex = 0;
		for (int i = 0, size = paramNameArrayForCache.length; i < size; i++) {
			Object paramValueObj = requestData.get(paramNameArrayForCache[i]);
			checkNull(paramNameArrayForCache[i], paramValueObj);
			if (paramValueObj instanceof String) {
				valueIndex++;
				putValue(valueData, (String) paramValueObj, valueIndex);
			} else {
				ArrayList valList = (ArrayList) paramValueObj;
				int valListSize = valList.size();
				for (int j = 0; j < valListSize; j++) {
					valueIndex++;
					putValue(valueData, (String) valList.get(j), valueIndex);
				}
			}
		}
		return valueData;
	}

	private final int questionMarkMatchCount(String questionMarkString) {
		Matcher questionMarkMatch = PageConstants.QUESTION_MARK_PATTERN.matcher(questionMarkString);
		int matchCount = 0;
		while (questionMarkMatch.find())
			matchCount++;
		return matchCount;
	}

	private void checkQuestionMarkMatch(String questionMarkGroup)
		throws PageException {
		int questionMarkCount = questionMarkMatchCount(questionMarkGroup);
		checkQuestionMarkMatch(questionMarkCount, questionMarkCount);
	}

	private void checkQuestionMarkMatch(
		int questionMarkCount,
		int fragmentCount)
		throws PageException {
		if (questionMarkCount != fragmentCount) {
			throw new PageException("questionMarkCount["	+ questionMarkCount + "] is not equal to assignValueCount[" + fragmentCount + "]");
		}
	}

	private final Param processCacheFail(Param requestData)
		throws PageException {

		boolean REQUEST_DATA_IS_SINGLE = requestData.getBoolean(PageConstants.REQUEST_DATA_SINGLE_MODE);

		Param valueData = new Param("VALUE_Param");
		Param paramData = new Param("PARAM_Param");
		int questionMarkCount =	questionMarkMatchCount(this.stringLiteralSkppedSqlStatement);
		if (questionMarkCount == 0)
			return valueData;

		Matcher paramMatch = PageConstants.SQL_STMT_PATTERN.matcher( this.stringLiteralSkppedSqlStatement);

		String paramName = "";
		String paramValue = "";

		int valueIndex = 0;
		int paramIndex = 0;
		while (paramMatch.find()) {

			paramName = paramMatch.group(PageConstants.SQL_COL_NAME_INDEX);

			String unaryOperatorIndicator = paramMatch.group(PageConstants.SQL_UNARY_OPERATOR_INDEX);

			if (unaryOperatorIndicator != null) {
				// (=|like|<|>|<=|>=|<>|!=) ���Ͽ� �ɷ��� ���
				paramValue = getStringValIgnoreCase(requestData,
													paramName,
													paramData,
													++paramIndex);
				valueData.putString("" + ++valueIndex, paramValue);

			} else {
				// between ? and ? �Ǵ� in ( ? ,... )  ���Ͽ� �ɷ��� ���
				String questionMarkGroup =	paramMatch.group(PageConstants.SQL_Q_MARK_GROUP_INDEX);

				if (REQUEST_DATA_IS_SINGLE) {
					//Param �ΰ��
					paramValue = getStringValIgnoreCase(requestData,
														paramName,
														paramData,
														++paramIndex);
					valueData.putString("" + ++valueIndex, paramValue);
				} else {
					//LMultiParam �ΰ��
					Object valObject = getObjectValIgnoreCase(requestData,
																paramName,
																paramData,
																++paramIndex);

					if (valObject instanceof String) {
						valueData.putString("" + ++valueIndex, (String) valObject);
					} else {

						ArrayList valList = (ArrayList) valObject;
						int entryValListSize = valList.size();

						checkQuestionMarkMatch(questionMarkGroup);
						// in �� between �� ó�� �ϱ� ���ؼ� multi �� ó��
						for (int j = 0; j < entryValListSize; j++) {
							valueData.putString("" + ++valueIndex, (String) valList.get(j));
						}
					}
				}
			}
		}
		checkQuestionMarkMatch(questionMarkCount, valueIndex);

		String[] paramNameArrayForCache = (String[]) paramData.values().toArray(new String[paramData.size()]);

		paramNameArrayCache.put(this.sqlStatement, paramNameArrayForCache);

		return valueData;
	}

	public static final String mutateColumnName(String columnName) {
		return DefaultNaming.getAttributeName(columnName);
	}

	public final static String removeOrderByClause(String orgSql) {

		Matcher sqlMatch = PageConstants.SQL_ORDER_BY_PATTERN.matcher(orgSql);

		int lastOrderByMatchStartIndex = 0;
		while (sqlMatch.find()) {
			lastOrderByMatchStartIndex = sqlMatch.start();
		}

		if (lastOrderByMatchStartIndex == 0)
			return orgSql;

		int positionOfOrderBy = lastOrderByMatchStartIndex;
		final String candidateForOrderByClause = orgSql.substring(positionOfOrderBy);

		char[] candidateCharacters = candidateForOrderByClause.toCharArray();
		final int size = candidateCharacters.length;
		int balance = 0;
		for (int inx = 0; inx < size; inx++) {
			if (candidateCharacters[inx] == ')')
				balance++;
			else if (candidateCharacters[inx] == '(')
				balance--;
		}

		if (balance != 0)
			return orgSql;
		return orgSql.substring(0, positionOfOrderBy);

	}

	public final String replaceOrderByClause(String orderBy) {
		if (!(orderBy == null || orderBy.trim().equals("")))
			return removeOrderByClause(this.sqlStatement) + " " + orderBy;
		else
			return this.sqlStatement;
	}

	public static Param convertLMultiDataToParam(MultiParam pageMultiData) {
		try {
			Param pageData = new Param("PAGE_DATA");
			boolean REQUEST_DATA_SINGLE_MODE_FLAG = true;
			Iterator iter = pageMultiData.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String entryKey = (String) entry.getKey();
				ArrayList valList = (ArrayList) entry.getValue();
				if (valList.size() == 1)
					pageData.putString(entryKey, (String) valList.get(0));
				else {
					pageData.put(entryKey, valList);
					REQUEST_DATA_SINGLE_MODE_FLAG = false;
				}
			}
			pageData.putBoolean(PageConstants.REQUEST_DATA_SINGLE_MODE, REQUEST_DATA_SINGLE_MODE_FLAG);
			return pageData;
		} catch (Exception e) {
			Log.info(e.getMessage());
			return null;
		}
	}
}
