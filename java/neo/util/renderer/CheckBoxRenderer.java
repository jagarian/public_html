package neo.util.renderer;

import java.util.Vector;

import neo.util.comm.StringUtil;

/**
 * 	@Class Name	: 	CheckBoxRenderer.java
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
public class CheckBoxRenderer implements java.io.Serializable {

	static final long serialVersionUID = 0;

	//-----------------------------------------------------------------------------
	private Vector 	keys_; 					//리스트 Element
	private String 	value_; 				//선택값
	//-----------------------------------------------------------------------------
	private boolean 	allSelect_; 			//전체선택 사용 여부
	private String 	allSect_ = "01"; 		//전체선택 유형(01:선택,02:Choice,03:전체,04:All)
	//-----------------------------------------------------------------------------
	private String 	allDelimiter_ = "--"; 	//전체선택 구분자
	//-----------------------------------------------------------------------------
	private String 	objName_ = "";			//jsp단 checkbox name 			
	private String 	styleStr_ = ""; 		//style
	private String 	cssClassStr_ = ""; 		//css
	private String 	jsOnChangeStr_ = "";	//js 
	//-----------------------------------------------------------------------------

	public CheckBoxRenderer() {}

	public CheckBoxRenderer(Vector keys,
							String value,
							boolean allSelect,
							String allSect,
							String allDelimiter,
							String objName,
							String styleStr,
							String cssClassStr,
							String jsOnChangeStr) {
		keys_ = keys;
		value_ = value;
		allSelect_ = allSelect;
		allSect_ = allSect;
		allDelimiter_ = allDelimiter;
		objName_ = objName;
		styleStr_ = styleStr;
		cssClassStr_ = cssClassStr;
		jsOnChangeStr_ = jsOnChangeStr;
	}
	
	public CheckBoxRenderer(Vector keys,
							String value,
							boolean allSelect,
							String objName) {
		keys_ = keys;
		value_ = value;
		allSelect_ = allSelect;
		objName_ = objName;
	}	

	public CheckBoxRenderer(Vector keys, 
							String value, 
							boolean allSelect ) {
		keys_ = keys;
		value_ = value;
		allSelect_ = allSelect;
	}

	public CheckBoxRenderer(Vector keys, String value) {
		keys_ = keys;
		value_ = value;
		allSelect_ = false;
	}

	public CheckBoxRenderer(Vector keys) {
		keys_ = keys;
		value_ = "";
		allSelect_ = false;
	}

	public void setAllSelected() {
		this.allSelect_ = true;
	}

	public void setObjName(String objName) {
		this.objName_ = objName;
	}

	public void setStyle(String styleStr) {
		this.styleStr_ = styleStr;
	}

	public void setCss(String cssClassStr) {
		this.cssClassStr_ = cssClassStr;
	}

	public void setJs(String jsOnChangeStr) {
		this.jsOnChangeStr_ = jsOnChangeStr;
	}

	public String doRender() {
		StringBuffer tagBuffer = new StringBuffer();
		String singleRow = null;

		LookupCode lookcode = null;

		String code = "";
		String value = "";

		for (int i = keys_.size() - 1; i >= 0; i--) {
			lookcode = (LookupCode) keys_.elementAt(i);

			code = lookcode.getCode();
			value = lookcode.getName();

			singleRow =
					"<input type=\"checkbox\" name=\""
					+ objName_
					+ "\" value=\""
					+ code
					+ "\" ";

			if ((value_ != null && StringUtil.search(value_, code) > 0))
				singleRow += " checked ";

			singleRow += ">" + value + "\n";

			tagBuffer.insert(0, singleRow);
		}

		if (allSelect_) { //전체 선택 기능이 on 이라면
			if (allSect_.equals("01")) { // 조회시 '선택' 조건으로 한다. 
				singleRow =
						"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_ + " 선택 " + allDelimiter_;				
			} else if (allSect_.equals("02")) { // 조회시 'Choice' 조건으로 한다. 
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_	+ " Choice " + allDelimiter_;
			} else if (allSect_.equals("03")) { //조회시 '전체' 조건으로 한다. (한글로)
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_	+ " 전체 " + allDelimiter_;
				
			} else if (allSect_.equals("04")) { //조회시 '전체' 조건으로 한다. (영문으로)
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_	+ " All "	+ allDelimiter_;
			} else {
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_ + " All " + allDelimiter_;
			}
			tagBuffer.insert(0, singleRow);
		}

		return tagBuffer.toString();
	}
}
