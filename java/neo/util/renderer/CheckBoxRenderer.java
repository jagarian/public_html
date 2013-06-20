package neo.util.renderer;

import java.util.Vector;

import neo.util.comm.StringUtil;

/**
 * 	@Class Name	: 	CheckBoxRenderer.java
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
public class CheckBoxRenderer implements java.io.Serializable {

	static final long serialVersionUID = 0;

	//-----------------------------------------------------------------------------
	private Vector 	keys_; 					//����Ʈ Element
	private String 	value_; 				//���ð�
	//-----------------------------------------------------------------------------
	private boolean 	allSelect_; 			//��ü���� ��� ����
	private String 	allSect_ = "01"; 		//��ü���� ����(01:����,02:Choice,03:��ü,04:All)
	//-----------------------------------------------------------------------------
	private String 	allDelimiter_ = "--"; 	//��ü���� ������
	//-----------------------------------------------------------------------------
	private String 	objName_ = "";			//jsp�� checkbox name 			
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

		if (allSelect_) { //��ü ���� ����� on �̶��
			if (allSect_.equals("01")) { // ��ȸ�� '����' �������� �Ѵ�. 
				singleRow =
						"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_ + " ���� " + allDelimiter_;				
			} else if (allSect_.equals("02")) { // ��ȸ�� 'Choice' �������� �Ѵ�. 
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_	+ " Choice " + allDelimiter_;
			} else if (allSect_.equals("03")) { //��ȸ�� '��ü' �������� �Ѵ�. (�ѱ۷�)
				singleRow =
					"<input type=\"checkbox\" name='"
						+ objName_
						+ "All\" value=\"\"  "
						+ ((value_ != null && value_.equals("")) ? " checked " : "")
						+ " >"
						+ allDelimiter_	+ " ��ü " + allDelimiter_;
				
			} else if (allSect_.equals("04")) { //��ȸ�� '��ü' �������� �Ѵ�. (��������)
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
