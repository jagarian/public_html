package neo.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import neo.data.MultiParam;

/**
 * 	@Class Name	: 	DefaultPageNavigationByPost.java
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
public class DefaultPageNavigationByPost extends AbstractPageNavigation {

	private MultiParam pageMultiData;

	public String showJavaScript() {

		String targetRow = PageConstants.TARGET_ROW;
		String neoOrderBy = PageConstants.NEO_ORDER_BY;

		StringBuffer retParam = new StringBuffer("\n\n");
		retParam
			.append("<!-- PAGE JavaScript Start -->\n")
			.append("<script language='JavaScript'>\n")
			.append("function goPage(row)\n")
			.append("{\n")
			.append("  var pageForm = null;\n")
			.append("  pageForm = document.all['" + targetRow + "'].form;\n ")
			.append("  pageForm." + targetRow + ".value = row;\n")
			.append("  pageForm.submit();\n")
			.append("}\n")
			.append("function goOrderByPage(row,orderBy)\n")
			.append("{\n")
			.append("  var pageForm = null;\n")
			.append("  pageForm = document.all['" + targetRow + "'].form;\n ")
			.append("  pageForm." + targetRow + ".value = row;\n")
			.append("  pageForm." + neoOrderBy + ".value = orderBy;\n")
			.append("  pageForm.submit();\n")
			.append("}\n\n")
			.append("function changePage(mySelect) {\n")
			.append("  var pageForm = null;\n")
			.append("  pageForm = mySelect.form;\n")
			.append("  pageForm." + targetRow + ".value = mySelect.value;\n")
			.append("  pageForm.submit();\n")
			.append("}\n")
			.append("</script>\n")
			.append("<!-- PAGE JavaScript End -->\n");

		return retParam.toString();
	}

	public DefaultPageNavigationByPost(MultiParam pageMultiData) {
		this.pageMultiData = pageMultiData;
	}

	public String showHiddenParam() {

		String neoOrderBy = "";

		StringBuffer retParam = new StringBuffer("\n\n");
		retParam.append("\n<!-- PAGE Hidden Parameters Start -->\n");

		Iterator iter = pageMultiData.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object entryValObject = entry.getValue();
			if (entryValObject instanceof ArrayList) {
				ArrayList entryValList = (ArrayList) entryValObject;
				String key = (String) entry.getKey();
				if (key.equals(PageConstants.NEO_ORDER_BY)) {
					neoOrderBy = ((String) entryValList.get(0)).trim();
				} else if (!(key.equals(PageConstants.TARGET_ROW))) {
					for (int i = 0, size = entryValList.size();
						i < size;
						i++) {
						Object paramValObj = entryValList.get(i);
						if (!(paramValObj instanceof String))
							continue;
						retParam.append("\n<input type=hidden name='"	+ key + "' value='" + ((String) paramValObj).trim() + "'>");
					}
				}
			}
		}
		retParam.append("\n<input type=hidden name='" + PageConstants.TARGET_ROW + "'>");
		retParam.append("\n<input type=hidden name='" + PageConstants.NEO_ORDER_BY + "' value='" + neoOrderBy + "'>");
		retParam.append("\n\n<!-- PAGE Hidden Parameters End -->\n");
		return retParam.toString();
	}

	public String showMoveBeforeIndex(String skinStr) {
		final String moveBeforeIndexImage = "◀◀";
		final int targetIndex = getCurrentIndex() - 1;
		StringBuffer rtnStr = new StringBuffer();
		if (targetIndex > 0) {
			final int targetPage =	PageNavigationUtility.getFirstPageOfIndex(targetIndex, getNumberOfPagesOfIndex());
			final int targetRow =	PageNavigationUtility.getFirstRowOfPage(targetPage,	getNumberOfRowsOfPage());
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\"  style='text-decoration:none'>")
				.append(moveBeforeIndexImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showMoveFirstPage(String skinStr) {
		String moveFirstPageImage = "FIRST";
		StringBuffer rtnStr = new StringBuffer();

		if (skinStr.equals("Default")) {
			moveFirstPageImage = "<img src='/images/paging_prv10.gif' width='19' height='9' border='0'>";
		}

		if (this.getCurrentPage() > 1) {
			int targetRow = 1;
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\" style='text-decoration:none'>")
				.append(moveFirstPageImage)
				.append("</a>");
		} else {
			//rtnStr.append(moveFirstPageImage);
		}

		return rtnStr.toString();
	}

	public String showMoveBeforePage(String skinStr) {
		String moveBeforePageImage = "Prev";
		final int targetPage = getCurrentPage() - 1;
		StringBuffer rtnStr = new StringBuffer();

		if (skinStr.equals("Default")) {
			moveBeforePageImage = "<img src='/images/paging_prv.gif' border='0'>";
		}

		if (targetPage > 0) {
			final int targetRow =
				PageNavigationUtility.getFirstRowOfPage(
					getCurrentPage() - 1,
					getNumberOfRowsOfPage());
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\" style='text-decoration:none'>")
				.append(moveBeforePageImage)
				.append("</a>");
			return rtnStr.toString();
		} else {
			//rtnStr.append(moveBeforePageImage);
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
				final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
				if (skinStr.equals("Default")) {
					if (currentPage == targetPage) {
						retStr.append("<td width='1'> <img src='/images/mid_line.gif' width='1' height='8'></td><td class='pagenumber_selected'>" 
											+ targetPage 
											+ "</td>");
					} else {
						retStr.append("<td width='1'>"
											+ "<img src='/images/mid_line.gif' width='1' height='8'></td>"
											+ "<td onmouseover=this.style.backgroundColor='#F7F7F7'   onmouseout=this.style.backgroundColor='FFFFFF' class='pagenumber'>"
											+ "<a href=\"javascript:goPage('"
											+ targetRow
											+ "')\" style='text-decoration:none' >");
						retStr.append(" [" + targetPage + "] ");
						retStr.append("</a></td>");
					}
				} else {
					if (currentPage == targetPage) {
						retStr.append(" <b>" + targetPage + "</b> ");
					} else {
						retStr.append("<a href=\"javascript:goPage('" + targetRow	+ "')\" style='text-decoration:none' >");
						retStr.append(" [" + targetPage + "] ");
						retStr.append("</a>");
					}
				}
			}
		}
		if (skinStr.equals("Default")) {
			retStr.append("<td width='1'> <img src='/images/mid_line.gif' width='1' height='8'></td>");
		}
		return retStr.toString();
	}

	public String showMoveNextPage(String skinStr) {
		String moveNextPageImage = "Next";
		final int targetPage = getCurrentPage() + 1;
		StringBuffer rtnStr = new StringBuffer();

		if (skinStr.equals("Default")) {
			moveNextPageImage = "<img src='/images/paging_next.gif' border='0'>";
		}

		if (targetPage <= getPages()) {

			final int targetRow =
				PageNavigationUtility.getFirstRowOfPage(targetPage,	getNumberOfRowsOfPage());
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\" style='text-decoration:none'>")
				.append(moveNextPageImage)
				.append("</a>");
		} else {
			//rtnStr.append(moveNextPageImage);
		}
		return rtnStr.toString();
	}

	public String showMoveEndPage(String skinStr) {
		String moveEndPageImage = "END";
		final int targetPage = this.getPages();
		StringBuffer rtnStr = new StringBuffer();

		if (skinStr.equals("Default")) {
			moveEndPageImage = "<img src='/images/paging_next10.gif' width='19' height='9' border='0'>";
		}

		if (getCurrentPage() < targetPage) {
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\" style='text-decoration:none'>")
				.append(moveEndPageImage)
				.append("</a>");
		} else {
			//rtnStr.append(moveEndPageImage);
		}
		return rtnStr.toString();
	}

	public String showMoveNextIndex(String skinStr) {
		final int targetIndex = getCurrentIndex() + 1;
		final String moveNextIndexImage = "▶▶";
		StringBuffer rtnStr = new StringBuffer();

		if (targetIndex <= getIndexes()) {
			final int targetPage = PageNavigationUtility.getFirstPageOfIndex(targetIndex, getNumberOfPagesOfIndex());
			final int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
			rtnStr
				.append("<a href=\"javascript:goPage('")
				.append(targetRow)
				.append("')\" style='text-decoration:none'>")
				.append(moveNextIndexImage)
				.append("</a>");
		}
		return rtnStr.toString();
	}

	public String showSortField(String title,
								String neoOrderByColumnName,
								String skinStr) {
		final String ascImage = "▲";
		final String descImage = "▼";

		final String orderBy = pageMultiData.getParam(0).getString(PageConstants.NEO_ORDER_BY);

		StringBuffer content = new StringBuffer();
		StringBuffer neoOrderBy = new StringBuffer();
		StringBuffer rtnStr = new StringBuffer();

		if (("order by " + neoOrderByColumnName + " desc").equals(orderBy)) {
			content.append(descImage).append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" asc");
		} else if (
			("order by " + neoOrderByColumnName + " asc").equals(orderBy)) {
			content.append(ascImage).append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" desc");
		} else {
			content.append(title);
			neoOrderBy.append("order by ").append(neoOrderByColumnName).append(" asc");
		}

		rtnStr
			.append("<a href=\"javascript:goOrderByPage(1,'")
			.append(neoOrderBy.toString())
			.append("')\"; onMouseover=\"self.status=''; return true; \">")
			.append(content.toString())
			.append("</a>");

		return rtnStr.toString();
	}
	
	public String showSelectIndex() {

		final int currentPage = getCurrentPage();
		final StringBuffer retStr = new StringBuffer();

		retStr.append("\n<select Onchange='changePage(this)'>");

		for (int targetPage = 1; targetPage <= getPages(); targetPage++) {

			int targetRow = PageNavigationUtility.getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());

			retStr.append("\n<option value=" + targetRow);
			if (targetPage == currentPage)
				retStr.append(" selected");
			retStr.append(">" + targetPage + "</option>");
		}
		retStr.append("\n</select>");

		return retStr.toString();
	}
}
