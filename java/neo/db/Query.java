package neo.db;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import neo.exception.QueryException;

/**
 * 	@Class Name	: 	Query.java
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
public class Query {

	private List fragmentList = new ArrayList();
	private List parameterList = new ArrayList();

	public Query(String query) {
		processingQuery(query);
	}

	public String getSql() throws QueryException {
		return parsedSQL();
	}

	public void setVarType(int index, String value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(value, false);
	}

	public void setReplaceType(int index, String value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(value, true);
	}

	public void setNumType(int index, String value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(new Integer(value), false);
	}

	public void setNumTypeArray(int index, String[] value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(value, false);
	}

	public void setVarTypeArray(int index, String[] value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(value, false);
	}

	public void setReplaceTypeArray(int index, String[] value) {
		((InnerParameter) parameterList.get(index - 1)).setParameterValue(value, true);
	}

	private String parsedSQL() throws QueryException {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < fragmentList.size(); i++) {

			if (fragmentList.get(i) instanceof Integer) {
				InnerParameter ip =
					(InnerParameter) parameterList.get(
						((Integer) fragmentList.get(i)).intValue());
				sb.append(ip.getParameterValue());
			} else {
				sb.append((String) fragmentList.get(i));
			}
		}

		return sb.toString();
	}

	private void processingQuery(String query) {

		StringTokenizer parser = new StringTokenizer(query, "#", true);

		String token = null;
		String lastToken = null;

		int i = 0;

		while (parser.hasMoreTokens()) {
			token = parser.nextToken();
			if ("#".equals(lastToken)) {
				if (!"#".equals(token)) {
					InnerParameter param = new InnerParameter();
					param.setParameterName(token);
					parameterList.add(param);

					token = parser.nextToken();
					token = null;

					fragmentList.add(new Integer(i++));
				}
			} else {
				if (!"#".equals(token))
					fragmentList.add(token);
			}
			lastToken = token;
		}
	}

	private class InnerParameter {

		private String parameterName;
		private Object parameterValue;
		private boolean isReplaceVariable = false;

		public void setParameterName(String paramName) {
			parameterName = paramName;
		}

		public void setParameterValue(Object obj, boolean isReplaceVariable) {
			this.parameterValue = obj;
			this.isReplaceVariable = isReplaceVariable;
		}

		public String getParameterName() {
			return parameterName;
		}

		public String getParameterValue() throws QueryException {
			try {
				Class c = parameterValue.getClass();
				if (c.isArray()) {
					int length = Array.getLength(parameterValue);
					if (length == 0) {
						return "";
					} else {
						Object item = Array.get(parameterValue, 0);
						StringBuffer inParam = new StringBuffer();

						if (item instanceof String) {

							if (!isReplaceVariable)
								inParam.append("'" + (String) item + "'");
							else
								inParam.append((String) item);

							for (int i = 1; i < length; i++)
								if (!isReplaceVariable)
									inParam.append(", '"
													+ Array.get(parameterValue, i)
													+ "'");

								else
									inParam.append(", " + Array.get(parameterValue, i));

						} else {
							inParam.append(((Integer) item).intValue());
							for (int i = 1; i < length; i++)
								inParam.append(", " + Array.get(parameterValue, i));
						}
						return inParam.toString();
					}
				} else if (parameterValue instanceof String)
					if (!isReplaceVariable)
						return "'" + (String) parameterValue + "'";
					else
						return (String) parameterValue;
				else
					return "" + parameterValue;

			} catch (Exception e) {
				throw new QueryException("Parameter("+ getParameterName()+ ") must be set before parsing to query.");
			}
		}
	}
}