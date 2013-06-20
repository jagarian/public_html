package neo.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 	@Class Name	: 	TData.java
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
public class TData extends LinkedHashMap {

	private static final long serialVersionUID = 1L;
	protected String name = null;
	protected boolean nullToInitialize = false;

	public TData() {
		super();
	}

	public TData(int arg1) {
		super(arg1);
	}

	public TData(Map arg1) {
		super(arg1);
	}

	public TData(int arg1, float arg2) {
		super(arg1, arg2);
	}

	public TData(int arg1, float arg2, boolean arg3) {
		super(arg1, arg2, arg3);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNullToInitialize() {
		return nullToInitialize;
	}

	public void setNullToInitialize(boolean nullToInitialize) {
		this.nullToInitialize = nullToInitialize;
	}
}
