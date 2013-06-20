package neo.util.comm;import java.io.Serializable ;import java.io.* ;import java.util.* ;import neo.util.log.Log;/** * 	@Class Name	: 	BeanWorkTime.java * 	@���ϼ���		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	�۾��� 		����	����	�۾���		���� * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����) *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�) *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ) *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���) **********************************************************************************************              */
public class BeanWorkTime implements Serializable {
    private String mstrstrDate				// �ش� ��¥
                  	,mstrstrSvcStartTime	// ���� ���� �ð�
                  	,mstrstrSignEndTime		// ���� ���� �ð�
                  	,mstrstrSvcEndTime;		// ���� ���� �ð�
    private char mcDateType;				// ��¥ ����    
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