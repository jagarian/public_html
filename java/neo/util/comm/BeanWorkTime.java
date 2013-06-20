package neo.util.comm;import java.io.Serializable ;import java.io.* ;import java.util.* ;import neo.util.log.Log;/** * 	@Class Name	: 	BeanWorkTime.java * 	@파일설명		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	작업일 		버젼	구분	작업자		내용 * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기) *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스) *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티) *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅) **********************************************************************************************              */
public class BeanWorkTime implements Serializable {
    private String mstrstrDate				// 해당 날짜
                  	,mstrstrSvcStartTime	// 서비스 시작 시각
                  	,mstrstrSignEndTime		// 결제 마감 시각
                  	,mstrstrSvcEndTime;		// 서비스 종료 시각
    private char mcDateType;				// 날짜 구분    
    public BeanWorkTime() {}    
    public BeanWorkTime (String strDate
                        ,String strSvcStartTime
                        ,String strSignEndTime
                        ,String strSvcEndTime
                        ,char   cDateType) {
        mstrstrDate         = strDate;
        mstrstrSvcStartTime = strSvcStartTime;
        mstrstrSignEndTime  = strSignEndTime;
        mstrstrSvcEndTime   = strSvcEndTime;
        mcDateType          = cDateType;
    }
    public String	getDate()			{ return mstrstrDate;			}
    public String	getSvcStartTime()	{ return mstrstrSvcStartTime;	}
    public String	getSignEndTime()	{ return mstrstrSignEndTime;	}
    public String	getSvcEndTime()		{ return mstrstrSvcEndTime;	}
    public char	getDateType()		{ return mcDateType;			}
}    