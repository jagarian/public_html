package neo.fileupload;

import org.apache.commons.fileupload.FileItem;

/**
 * 	@Class Name	: 	FormInfo.java
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
public class FormInfo {

	private String fieldName;
	private String fieldValue;

	private FormInfo() {
	}

	public FormInfo(FileItem item) {
		this.fieldName = item.getFieldName();
		this.fieldValue = item.getString();
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}
}
