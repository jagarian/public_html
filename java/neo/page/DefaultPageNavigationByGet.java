package neo.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import neo.data.MultiParam;

/**
 * 	@Class Name	: 	DefaultPageNavigationByGet.java
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
public class DefaultPageNavigationByGet extends AbstractPageNavigation {

	private MultiParam pageMultiData;
	private String commonHrefParamString = "";

	public String showJavaScript() {
		StringBuffer retStr = new StringBuffer();
		retStr
			.append("\n<!-- PAGE JavaScript Start -->\n\n")
			.append("<script language='JavaScript'>\n")
			.append("function changePage(mySelect)\n")
			.append(" {\n")
			.append("    document.location.href = mySelect.value;\n")
			.append(" }\n")
			.append("</script>\n")
			.append("\n<!-- PAGE JavaScript End -->\n");
		return retStr.toString();
	}

	public String showHiddenParam() {
		return "";
	}

	public DefaultPageNavigationByGet(MultiParam pageMultiData) {
		this.pageMultiData = pageMultiData;
		this.commonHrefParamString = getCommonHrefParam(pageMultiData);
	}

	private String getHrefParam(int targetRow) {
		StringBuffer hrefParam = new StringBuffer();
		hrefParam
			.append(commonHrefParamString)
			.append("&")
			.append(PageConstants.TARGET_ROW)
			.append("=")
			.append(targetRow);
		hrefParam.setCharAt(0, '?');
		return hrefParam.toString();
	}

	private String getCommonHrefParam(MultiParam pageMultiData) {
		StringBuffer retParam = new StringBuffer();
		Iterator iter = pageMultiData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object entryValObject = entry.getValue();
			if (entryValObject instanceof ArrayList) {
				ArrayList entryValList = (ArrayList) entryValObject;
				String key = (String) entry.getKey();
				if (!(key.equals(PageConstants.TARGET_ROW))) {
					for (int i = 0, size = entryValList.size(); i < size; i++) {
						Object paramValObj = entryValList.get(i);
						if (!(paramValObj instanceof String))
							continue;
						retParam.append("&" + key + "=" + ((String) paramValObj).trim());
					}
				}
			}
		}
		return retParam.toString();
	}

	public String showMoveBeforeIndex(String skinStr) {
		final String moveBeforeIndexImage = "◀◀";
		final int targetIndex = getCurrentIndex() - 1;
		StringBuffer rtnStr = new StringBuffer();
		if (targetIndex > 0) {
			final int targetPage =	PageNavigationUtility.getFirstPageOfIndex(targetIndex, getNumberOfPagesOfIndex());
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none' >")
				.append(moveBeforeIndexImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showMoveNextIndex(String skinStr) {
		final String moveNextIndexImage = "▶▶";
		final int targetIndex = getCurrentIndex() + 1;
		StringBuffer rtnStr = new StringBuffer();
		if (targetIndex <= getIndexes()) {
			final int targetPage = PageNavigationUtility.getFirstPageOfIndex(targetIndex, getNumberOfPagesOfIndex());
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none'>")
				.append(moveNextIndexImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showMoveFirstPage(String skinStr) {
		final String moveFirstPageImage = "FIRST";
		StringBuffer rtnStr = new StringBuffer();
		if (this.getCurrentPage() > 1) {
			int targetRow = 1;
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none'>")
				.append(moveFirstPageImage)
				.append("</a>");
		}

		return rtnStr.toString();
	}

	public String showMoveBeforePage(String skinStr) {
		final String moveBeforePageImage = "◀";
		final int targetPage = getCurrentPage() - 1;
		StringBuffer rtnStr = new StringBuffer();
		if (targetPage > 0) {
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none'>")
				.append(moveBeforePageImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showIndex(String skinStr) {
		final int currentPage = getCurrentPage();
		final int currentIndexes = getCurrentIndex();
		final int startPage = PageNavigationUtility.getFirstPageOfIndex(currentIndexes, getNumberOfPagesOfIndex());
		final int endPage = PageNavigationUtility.getFirstPageOfIndex(currentIndexes + 1, getNumberOfPagesOfIndex());
		StringBuffer retStr = new StringBuffer();
		for (int targetPage = startPage; targetPage < endPage; targetPage++) {
			if (targetPage <= getPages()) {
				final int targetRow =
					PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
				if (currentPage == targetPage) {
					retStr.append("<font color = red>")
							.append(targetPage)
							.append("</font>")
							.append("</a>");
				} else {
					retStr.append("<a href='")
							.append(getHrefParam(targetRow))
							.append("'>");
					retStr.append(targetPage).append("</a>");
				}
				if (targetPage != endPage - 1 && targetPage != getPages())
					retStr.append(" | ");
			}
		}
		return retStr.toString();
	}
	
	public String showMoveNextPage(String skinStr) {
		final String moveNextPageImage = "▶";
		final int targetPage = getCurrentPage() + 1;
		StringBuffer rtnStr = new StringBuffer();
		if (targetPage <= getPages()) {
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none'>")
				.append(moveNextPageImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showMoveEndPage(String skinStr) {
		final String moveEndPageImage = "END";
		final int targetPage = this.getPages();
		StringBuffer rtnStr = new StringBuffer();
		if (getCurrentPage() < targetPage) {
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href='")
				.append(getHrefParam(targetRow))
				.append("' style='text-decoration:none'>")
				.append(moveEndPageImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showSelectIndex() {
		final int currentPage = getCurrentPage();
		StringBuffer retStr = new StringBuffer();
		retStr.append("\n<select Onchange='changePage(this)'>\n");
		for (int targetPage = 1; targetPage <= getPages(); targetPage++) {
			int targetRow =
				PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			retStr.append("<option value='")
					.append(getHrefParam(targetRow))
					.append("' ");
			if (targetPage == currentPage)
				retStr.append(" selected ");
			retStr.append(">").append(targetPage).append("</option>\n");
		}
		retStr.append("</select>\n");
		return retStr.toString();
	}

	private String updateOrderBy(String orgCommonHrefParamString,
											String neoOrderBy) {
		int neoOrderByStartInx =
			orgCommonHrefParamString.indexOf(PageConstants.NEO_ORDER_BY + "=");
		StringBuffer hrefParam = new StringBuffer();
		if (neoOrderByStartInx == -1) {
			hrefParam
				.append(orgCommonHrefParamString)
				.append("&")
				.append(PageConstants.NEO_ORDER_BY)
				.append("=")
				.append(neoOrderBy);
			hrefParam.setCharAt(0, '?');
		} else {
			int neoOrderByLength = PageConstants.NEO_ORDER_BY.length();
			int neoOrderByEndInx =
				orgCommonHrefParamString.indexOf('&',	neoOrderByStartInx + neoOrderByLength + 1);
			if (neoOrderByEndInx == -1)
				neoOrderByEndInx = orgCommonHrefParamString.length();
			hrefParam
				.append(orgCommonHrefParamString.substring(0,	neoOrderByStartInx - 1))
				.append("&")
				.append(PageConstants.NEO_ORDER_BY)
				.append("=")
				.append(neoOrderBy)
				.append(orgCommonHrefParamString.substring(neoOrderByEndInx));
			hrefParam.setCharAt(0, '?');
		}
		return hrefParam.toString();
	}

	public String showSortField(String title,
								String neoOrderByColumnName,
								String skinStr) {
		final String ascImage = "▲";
		final String descImage = "▼";
		final String orderBy =	pageMultiData.getParam(0).getString(PageConstants.NEO_ORDER_BY);
		StringBuffer content = new StringBuffer();
		StringBuffer neoOrderBy = new StringBuffer();
		StringBuffer rtnStr = new StringBuffer();
		if (("order by " + neoOrderByColumnName + " desc").equalsIgnoreCase(orderBy)) {
			content.append(descImage).append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" asc");
		} else if (
			("order by " + neoOrderByColumnName + " asc").equalsIgnoreCase(orderBy)) {
			content.append(ascImage).append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" desc");
		} else {
			content.append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" asc");
		}
		rtnStr
			.append("<a href='")
			.append(updateOrderBy(getHrefParam(1), neoOrderBy.toString()))
			.append("' onMouseover=\"self.status=''; return true; \">")
			.append(content.toString())
			.append("</a>");
		return rtnStr.toString();
	}
}
