package neo.util.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 	@Class Name	: 	ValueList.java
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
