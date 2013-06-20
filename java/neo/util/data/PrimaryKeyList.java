package neo.util.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 	@Class Name	: 	PrimaryKeyList.java
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
public class PrimaryKeyList implements Serializable {

	static final long serialVersionUID = 0;

	private ArrayList list;

	public PrimaryKeyList() {
		this.list = new ArrayList();
	}

	protected void addElement(PrimaryKey pk) {
		this.list.add(pk);
	}

	protected PrimaryKey getElement(int index) {
		return (PrimaryKey) this.list.get(index);
	}

	public void remove(int index) {
		this.list.remove(index);
	}

	public int size() {
		return this.list.size();
	}
}
