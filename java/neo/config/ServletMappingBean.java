package neo.config;

import java.util.ArrayList;

/**
 * 	@Class Name	: 	ServletMappingBean.java
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
public class ServletMappingBean {

	private String mstrJobName; 			//호출되어 사용되는 Job 이름
	private String mstrJobClass; 			//인스턴스가 될 Class
	private ArrayList minsJobKeyNames; 	//Parameter들의 ArrayList
	private ArrayList minsJobParameters; 	//Parameter들의 ArrayList

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