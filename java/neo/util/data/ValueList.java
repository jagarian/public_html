package neo.util.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 	@Class Name	: 	ValueList.java
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
public class ValueList implements Serializable {
	static final long serialVersionUID = 0;

	private ArrayList list;

	public ValueList() {
		this.list = new ArrayList();
	}

	protected void addElement(ValueObject vo) {
		this.list.add(vo);
	}

	protected ValueObject getElement(int index) {
		return (ValueObject) this.list.get(index);
	}

	public void remove(int index) {
		this.list.remove(index);
	}

	public int size() {
		return this.list.size();
	}
}
