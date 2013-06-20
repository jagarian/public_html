package neo.util.renderer;

import java.util.Vector;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	ComboStrRenderer.java
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
public class ComboStrRenderer implements java.io.Serializable {

	static final long serialVersionUID = 0;
	//-----------------------------------------------------------------------------
	private Vector 	keys_; 					//����Ʈ Element
	private String 	value_; 				//���ð�
	//-----------------------------------------------------------------------------
	private boolean 	allSelectYn_; 			//��ü���� ��� ����
	private String 	allSect_ = "01"; 		//��ü���� ����(01:����,02:Choice,03:��ü,04:All)
	//-----------------------------------------------------------------------------
	private String 	allDelimiter_ = "--"; 	//��ü���� ������
	//-----------------------------------------------------------------------------
	private String 	objName_ = ""; 			//jsp�� �޺��ڽ� name 			
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
						+ allDelimiter_ + " ���� " + allDelimiter_
						+ "</option>\n";				
			} else if (allSect_.equals("02")) { //�Է½�
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
						+ allDelimiter_ + " ��ü " + allDelimiter_
						+ "</option>\n";
			} else if (allSect_.equals("04")) { //��ȸ��
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
