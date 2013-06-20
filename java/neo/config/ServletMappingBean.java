package neo.config;

import java.util.ArrayList;

/**
 * 	@Class Name	: 	ServletMappingBean.java
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
public class ServletMappingBean {

	private String mstrJobName; 			//ȣ��Ǿ� ���Ǵ� Job �̸�
	private String mstrJobClass; 			//�ν��Ͻ��� �� Class
	private ArrayList minsJobKeyNames; 	//Parameter���� ArrayList
	private ArrayList minsJobParameters; 	//Parameter���� ArrayList

	public ServletMappingBean(	String pstrJobname,
								String pstrJobclass,
								ArrayList pinsKeyNames,
								ArrayList pinsParameters) {
		this.mstrJobName = pstrJobname;
		this.mstrJobClass = pstrJobclass;
		this.minsJobKeyNames = pinsKeyNames;
		this.minsJobParameters = pinsParameters;
	}

	public String getJobName() {
		return this.mstrJobName;
	}

	public String getJobClass() {
		return this.mstrJobClass;
	}
	
	public ArrayList getJobKeyNames() {
		return this.minsJobKeyNames;
	}

	public ArrayList getJobParameters() {
		return this.minsJobParameters;
	}

	public String toString() {
		StringBuffer rinsString = new StringBuffer().append("[ job="
															+ mstrJobName
															+ " class="
															+ mstrJobClass
															+ " parameters=");
		for (int i = 0; i < minsJobParameters.size(); i++) {
			rinsString.append("<" + (i + 1) + ":" + (String) minsJobParameters.get(i) + "> ");
		}
		rinsString.append("]");
		return rinsString.toString();
	}
}