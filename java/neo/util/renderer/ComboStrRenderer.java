package neo.util.renderer;

import java.util.Vector;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	ComboStrRenderer.java
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
public class ComboStrRenderer implements java.io.Serializable {

	static final long serialVersionUID = 0;
	//-----------------------------------------------------------------------------
	private Vector 	keys_; 					//리스트 Element
	private String 	value_; 				//선택값
	//-----------------------------------------------------------------------------
	private boolean 	allSelectYn_; 			//전체선택 사용 여부
	private String 	allSect_ = "01"; 		//전체선택 유형(01:선택,02:Choice,03:전체,04:All)
	//-----------------------------------------------------------------------------
	private String 	allDelimiter_ = "--"; 	//전체선택 구분자
	//-----------------------------------------------------------------------------
	private String 	objName_ = ""; 			//jsp단 콤보박스 name 			
	private String 	styleStr_ = ""; 		//style
	private String 	cssClassStr_ = ""; 		//css
	private String 	jsOnChangeStr_ = "";	//js 
	//-----------------------------------------------------------------------------

	public ComboStrRenderer() {
	}

	public ComboStrRenderer(Vector keys,
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
		allSelectYn_ = allSelect;
		allSect_ = allSect;
		allDelimiter_ = allDelimiter;
		objName_ = objName;
		styleStr_ = styleStr;
		cssClassStr_ = cssClassStr;
		jsOnChangeStr_ = jsOnChangeStr;
	}

	public ComboStrRenderer(Vector keys,
							String value,
							boolean allSelect,
							String allSect) {
		keys_ = keys;
		value_ = value;
		allSelectYn_ = allSelect;
		allSect_ = allSect;
	}

	public ComboStrRenderer(Vector keys, 
							String value, 
							boolean allSelect ) {
		keys_ = keys;
		value_ = value;
		allSelectYn_ = allSelect;
	}

	public ComboStrRenderer(Vector keys, String value) {
		keys_ = keys;
		value_ = value;
		allSelectYn_ = false;
	}

	public ComboStrRenderer(Vector keys) {
		keys_ = keys;
		value_ = "";
		allSelectYn_ = false;
	}

	public void setAllSelected() {
		this.allSelectYn_ = true;
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

		String cmbHeader = "";
		String cmbTail = "";
		
		if (!objName_.equals("")) {
			cmbHeader = "<select name=\"" + objName_ + "\" ";
			if (!styleStr_.equals(""))
				cmbHeader += " style=\"" + styleStr_ + "\" ";
			if (!cssClassStr_.equals(""))
				cmbHeader += " class=\"" + cssClassStr_ + "\" ";
			if (!jsOnChangeStr_.equals(""))
				cmbHeader += " onChange=\"" + jsOnChangeStr_ + "\" ";
			cmbHeader += " >\n ";
			cmbTail += "</select>\n";
		}

		for (int i = keys_.size() - 1; i >= 0; i--) {
			lookcode = (LookupCode) keys_.elementAt(i);

			code = lookcode.getCode();
			value = lookcode.getName();

			singleRow = "<option value=\"" + code + "\" ";

			if (value_ != null && value_.equals(code))
				singleRow += " selected ";

			singleRow += ">" + value + "</option>\n";

			tagBuffer.insert(0, singleRow);
		}

		if (allSelectYn_) {
			if (allSect_.equals("01")) {
				singleRow =
						"<option value=\"\" "
						+ ((value_ != null && value_.equals("")) ? " selected " : "")
						+ " >"
						+ allDelimiter_ + " 선택 " + allDelimiter_
						+ "</option>\n";				
			} else if (allSect_.equals("02")) { //입력시
				singleRow =
					"<option value=\"\" "
						+ ((value_ != null && value_.equals("")) ? " selected " : "")
						+ " >"
						+ allDelimiter_ + " Choice " + allDelimiter_
						+ "</option>\n";
			} else if (allSect_.equals("03")) {
				singleRow =
					"<option value=\"\" "
						+ ((value_ != null && value_.equals("")) ? " selected " : "")
						+ " >"
						+ allDelimiter_ + " 전체 " + allDelimiter_
						+ "</option>\n";
			} else if (allSect_.equals("04")) { //조회시
				singleRow =
					"<option value=\"\" "
						+ ((value_ != null && value_.equals("")) ? " selected " : "")
						+ " >"
						+ allDelimiter_ + " All " + allDelimiter_
						+ "</option>\n";
			} else {
				singleRow =
					"<option value=\"\" "
						+ ((value_ != null && value_.equals("")) ? " selected " : "")
						+ " >+"
						+ allDelimiter_ + " All " + allDelimiter_
						+ "</option>\n";
			}
			tagBuffer.insert(0, singleRow);
		}

		return cmbHeader + " " + tagBuffer.toString() + " " + cmbTail;
	}
}
