package neo.util.comm;
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