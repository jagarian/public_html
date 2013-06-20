package neo.util.renderer;

import java.util.Vector;

/**
 * 	@Class Name	: 	LookupCode.java
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
public class LookupCode implements java.io.Serializable {

	private static final long serialVersionUID = 0;

	private String 	code_ = ""; 			//�ڵ尡 ������ ���
	private int 		ncode_ = 0; 			//�ڵ尡 ������ ���
	private String 	name_ = ""; 			//��
	private int 		size_ = 0; 				//ũ��
	private boolean 	isReady_ = false; 		//���� �غ� ����
	private Vector 	cargo_ = new Vector();	//�ӽ� ����
	private String 	tempCd_ = "";
	private String 	tempCd1_ = "";
	private String 	tempCd2_ = "";
	private String 	tempCd3_ = "";
	private String 	tempCd4_ = "";
	private String 	tempCd5_ = "";

	public LookupCode() {
	}

	public LookupCode(	String code, 
						String name, 
						boolean isReady ) { //���ڿ�
		this.code_ = code;
		this.name_ = name;
		this.isReady_ = isReady;
	}

	public LookupCode(	String code,
						String name,
						Vector cargo,
						boolean isReady ) { //���ڿ� (cargo �ʵ� ����)
		this.code_ = code;
		this.name_ = name;
		this.cargo_ = cargo;
		this.isReady_ = isReady;
	}

	public LookupCode(	int ncode, 
						String name, 
						boolean isReady ) { //���ڿ�
		this.ncode_ = ncode;
		this.name_ = name;
		this.isReady_ = isReady;
	}
	
	public LookupCode(	int ncode, 
						String name, 
						Vector cargo,
						boolean isReady ) { //���ڿ� (cargo �ʵ� ����)
		this.ncode_ = ncode;
		this.name_ = name;
		this.cargo_ = cargo;
		this.isReady_ = isReady;
}

	public void setCode(String code) {
		this.code_ = code;
	}
	public void setNcode(int ncode) {
		this.ncode_ = ncode;
	}
	public void setName(String name) {
		this.name_ = name;
	}
	public void setSize(int size) {
		this.size_ = size;
	}
	public void setIsReady(boolean isReady) {
		this.isReady_ = isReady;
	}
	public void setCargo(Vector cargo) {
		this.cargo_ = cargo;
	}
	public void setTempCd(String tempCd) {
		this.tempCd_ = tempCd;
	}
	public void setTempCd1(String tempCd1) {
		this.tempCd1_ = tempCd1;
	}
	public void setTempCd2(String tempCd2) {
		this.tempCd2_ = tempCd2;
	}
	public void setTempCd3(String tempCd3) {
		this.tempCd3_ = tempCd3;
	}
	public void setTempCd4(String tempCd4) {
		this.tempCd4_ = tempCd4;
	}
	public void setTempCd5(String tempCd5) {
		this.tempCd5_ = tempCd5;
	}

	public String getCode() {
		return this.code_;
	}
	public int getNcode() {
		return this.ncode_;
	}
	public String getName() {
		return this.name_;
	}
	public int getSize() {
		return this.size_;
	}
	public boolean getIsReady() {
		return this.isReady_;
	}
	public Vector getCargo() {
		return this.cargo_;
	}
	public String getTempCd() {
		return this.tempCd_;
	}
	public String getTempCd1() {
		return this.tempCd1_;
	}
	public String getTempCd2() {
		return this.tempCd2_;
	}
	public String getTempCd3() {
		return this.tempCd3_;
	}
	public String getTempCd4() {
		return this.tempCd4_;
	}
	public String getTempCd5() {
		return this.tempCd5_;
	}
}