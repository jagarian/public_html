package neo.util.comm;

import java.util.List;
import java.util.ResourceBundle;

/**
 * 	@Class Name	: 	Paging.java
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
public class Paging implements PagingHelper {
	protected int curPage = 1;
	protected int totalPage = 0;
	protected int totalBlock = 0;
	protected int totalCount;
	protected int curBlock = 0;
	protected int cPerPage = 10; 		// 페이지당 레코드수
	protected int pPerBlock = 10; 	// 블럭수

	protected int record_count = 0; 	// 전체 레코드 수
	protected int start = 0; 			// 시작번호
	protected int end = 0; 			// 끝번호

	private static String firstImg;
	private static String preImg;
	private static String nextImg;
	private static String endImg;

	protected String pageParamName = "page";
	protected String linkUrl;
	protected Object[] args = null;
	protected javax.servlet.http.HttpServletRequest request = null;
	protected java.util.Hashtable params = new java.util.Hashtable();

	private void loadProperties() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages");
		String ctx = request.getContextPath();

		String paging_page = "paging.page";
		this.cPerPage = Integer.parseInt(bundle.getString(paging_page + ".cnt"));
		this.pPerBlock = Integer.parseInt(bundle.getString(paging_page + ".block"));

		String paging_path_img = "paging.path.img";
		String img_path = bundle.getString(paging_path_img);

		firstImg = ctx + img_path + bundle.getString(paging_path_img + ".first");
		preImg = ctx + img_path + bundle.getString(paging_path_img + ".pre");
		nextImg = ctx + img_path + bundle.getString(paging_path_img + ".next");
		endImg = ctx + img_path + bundle.getString(paging_path_img + ".end");
	}

	public Paging() {
		curPage = 1;
		args = null;
	}

	public Paging(int curpage) {
		curPage = curpage;
		args = null;
		if (curPage == 0)
			curPage = 1;
	}
	public Paging(int curpage, Object[] args) {
		curPage = curpage;
		this.args = args;
		if (curPage == 0)
			curPage = 1;

	}

	public Paging(javax.servlet.http.HttpServletRequest req, Object[] args) {
		request = req;
		this.args = args;

	}

	public Paging(javax.servlet.http.HttpServletRequest req) {
		args = null;
		request = req;
		loadProperties();
	}

	public void setParameter(String name, String value) {
		params.put(name, value);
	}

	public void setRecordCount(int record_count) {
		this.record_count = record_count;

		calculatePage();

		this.end = record_count - (cPerPage * (curPage - 1));
		this.start = this.end - cPerPage + 1;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getQueryString() {
		StringBuffer sb = new StringBuffer("");
		if (params.size() == 0)
			return "";
		java.util.Enumeration enumeration = params.keys();
		String key = null;
		while (enumeration.hasMoreElements()) {
			key = (String) enumeration.nextElement();
			sb.append(key + "=" + params.get(key) + "&");
		}
		key = sb.toString();

		return key;
	}
	protected String getOnePageHtml(int curpage) {
		String url = "";
		if (curpage == curPage) //현제 페이지 이면 
			{
			url =	"\t\t<td align='center' class='chapter-num' width='5'><strong>"
					+ String.valueOf(curpage)
					+ "</strong></td> \n";
		} else {
			url =	"\t\t<td align='center' class='chapter-num_mouseout' width='5' onclick=\"javascript:location.href='"
					+ getFullRequestURI(curpage)
					+ "'\" onmouseover=\"setLineColor(this,'chapter-num_mouseover');\" onmouseout=\"setLineColor(this,'chapter-num_mouseout');\" >"
					+ curpage
					+ "</td> \n";
		}
		return url;
	}
	protected String getFullRequestURI(int page) {
		String url =
			linkUrl + "?" + getQueryString() + pageParamName + "=" + page;
		return url;
	}

	protected String getPrevHtmlLink(int prevBlockPage) {
		StringBuffer sb = new StringBuffer("");
		sb.append(
				"\t\t<td><a href = \""
				+ getFullRequestURI(1)
				+ "\""
				+ "><img src='"
				+ firstImg
				+ "' border='0' alt='처음페이지'/></a></td> \n");
		sb.append(
				"\t\t<td><a href = \""
				+ getFullRequestURI(prevBlockPage)
				+ "\""
				+ "><img src='"
				+ preImg
				+ "' border='0' alt='이전"
				+ pPerBlock
				+ "개' /></a></td> \n");
		return sb.toString();
	}

	protected String getNextHtmlLink(int nextBlockPage) {
		StringBuffer sb = new StringBuffer("");
		sb.append(
				"\t\t<td><a href = \""
				+ getFullRequestURI(nextBlockPage)
				+ "\""
				+ "><img src='"
				+ nextImg
				+ "' border='0' alt='다음"
				+ pPerBlock
				+ "개' /></a></td> \n");
		sb.append(
				"\t\t<td><a href = \""
				+ getFullRequestURI(totalPage)
				+ "\"><img src='"
				+ endImg
				+ "' border='0' alt='마지막페이지("
				+ totalPage
				+ ")' /></a></td> \n");
		return sb.toString();
	}

	protected String getPrevHtml() {
		StringBuffer sb = new StringBuffer("");
		sb.append("\t\t<td><img src='" + firstImg + "' alt='처음페이지'/></td> \n");
		sb.append("\t\t<td><img src='" + preImg + "' alt='이전' /></td> \n");
		return sb.toString();
	}

	protected String getNextHtml() {
		StringBuffer sb = new StringBuffer("");
		sb.append("\t\t<td><img src='" + nextImg + "' alt='다음' /></td> \n");
		sb.append("\t\t<td><img src='" + endImg + "' alt='마지막페이지' /></td> \n");
		return sb.toString();
	}

	public String getPageHtml() {
		StringBuffer sb = new StringBuffer("");

		sb.append("<table border='0' cellspacing='2' cellpadding='2'> \n");
		sb.append("\t<tr>\n");
		if (curBlock != 0) {
			int prevBlock = (curBlock) * pPerBlock; //이전 블록을 표시하기 위해 가야하는 페이지
			sb.append(getPrevHtmlLink(prevBlock));
		} else {
			sb.append(getPrevHtml());
		}

		int startPage = curBlock * pPerBlock + 1;
		int endPage =
			totalPage < ((curBlock + 1) * pPerBlock)
				? totalPage
				: (curBlock + 1) * pPerBlock;

		for (int i = startPage; i <= endPage; i++) {
			sb.append(getOnePageHtml(i));
		}

		if ((totalBlock - 1) > curBlock) {
			int nextBlock = (curBlock + 1) * pPerBlock + 1;
			sb.append(getNextHtmlLink(nextBlock));

		} else {
			sb.append(getNextHtml());
		}

		sb.append("\t</tr> \n");
		sb.append("</table> \n");

		return sb.toString();
	}

	protected void calculatePage() {
		try {
			curPage = Integer.parseInt(request.getParameter(pageParamName));
		} catch (NumberFormatException e) {
		}
		try {
			totalCount = this.record_count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((totalCount % cPerPage) == 0)
			if (totalCount == 0)
				totalPage = 1;
			else
				totalPage = totalCount / cPerPage;
		else
			totalPage = totalCount / cPerPage + 1;

		//게시판의 총 블럭 수
		if ((totalPage % pPerBlock) == 0)
			totalBlock = totalPage / pPerBlock;
		else
			totalBlock = totalPage / pPerBlock + 1;
		curBlock = (curPage - 1) / (pPerBlock); //현재 블럭

	}

	/**
	 * Gets the linkUrl
	 * @return Returns a String
	 */
	public String getRequestURI() {
		return linkUrl;
	}
	/**
	 * Sets the linkUrl
	 * @param linkUrl The linkUrl to set
	 */
	public void setRequestURI(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * Gets the perPage
	 * @return Returns a int
	 */
	public int getContentsPerPage() {
		return cPerPage;
	}
	/**
	 * Sets the perPage
	 * @param perPage The perPage to set
	 */
	public void setContentsPerPage(int perPage) {
		cPerPage = perPage;
	}

	/**
	 * Gets the perBlock
	 * @return Returns a int
	 */
	public int getPagesPerBlock() {
		return pPerBlock;
	}
	/**
	 * Sets the perBlock
	 * @param perBlock The perBlock to set
	 */
	public void setPagesPerBlock(int perBlock) {
		pPerBlock = perBlock;
	}

	/**
	 * Gets the pageParamName
	 * @return Returns a String
	 */
	public String getPageParamName() {
		return pageParamName;
	}
	/**
	 * Sets the pageParamName
	 * @param pageParamName The pageParamName to set
	 */
	public void setPageParamName(String pageParamName) {
		this.pageParamName = pageParamName;
	}

	public List getRecords() throws Exception {

		return null;
	}

	public int getListSize() throws Exception {
		return cPerPage;
	}

	public int getSubListSize() throws Exception {
		return (curPage - 1) * cPerPage;
	}

	public int getListNumberStart() throws Exception {
		return end;
	}
}