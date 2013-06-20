package neo.util.comm;import java.io.* ;import java.util.* ;import neo.exception.CommandException;import neo.util.log.Log;/** * 	@Class Name	: 	BeanWorkTimes.java * 	@파일설명		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	작업일 		버젼	구분	작업자		내용 * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기) *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스) *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티) *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅) **********************************************************************************************              */
public class BeanWorkTimes implements Serializable {
    private Vector 	minsWorkTimes;	// 서비스 제공시각 목록
    private String		mstrFileName;	// holiday.dat 파일 위치
    private long		mlFileDateTime;	// holiday.dat 파일 수정일시    
    public BeanWorkTimes( String strFileName, long lFileDateTime ) {
        minsWorkTimes = new Vector();
        mstrFileName   = strFileName;
        mlFileDateTime = lFileDateTime;
    }    
    public void add( BeanWorkTime insWorkTime ) throws CommandException {
        minsWorkTimes.add( insWorkTime );
    }    
    //주어진 날의 전/후업무일을 구한다
    public BeanWorkTime get( String strDate, int iDays ) throws Exception {
        BeanWorkTime insResult = null;
        int i = 0;        try {		
        	//주어진 날을 찾는다
        	for ( i = 0 ; i < minsWorkTimes.size(); i++ )
        		if ( ((BeanWorkTime)minsWorkTimes.get( i )).getDate().equals( strDate ) )
        			break;        
        	if ( i == minsWorkTimes.size() ) {
        		//잘못된 holiday.dat  --> 오늘 없음        		throw new CommandException("잘못된 holiday.dat  --> 오늘 없음");
        	} else
        		insResult = (BeanWorkTime)minsWorkTimes.get( i );        
        	//찾은 날의 주어진 전/후 업무일을 구한다
        	while( iDays != 0 ) {
        		// 하루 전/후 업무일을 구한다
        		i = i + Math.abs( iDays ) / iDays;
        		insResult = (BeanWorkTime)minsWorkTimes.get( i );
        		// 구한 날이 업무일이면 iDay 절대값 감소
        		if ( insResult.getDateType() != '*' )
        			iDays -= Math.abs( iDays ) / iDays;
        	}        } catch (Exception e) {			throw new CommandException(e.getMessage(), e);		}       	return insResult;
    }    
    public BeanWorkTime get() throws Exception {
        return get( DateUtil.getToday(), 0 );
    }    
    public BeanWorkTime get( int iDays ) throws Exception {
        return get( DateUtil.getToday(), iDays );
    }    
    public BeanWorkTime get( String strDate ) throws Exception {
        return get( strDate, 0 );
    }    
    public String getFileName() {
        return mstrFileName;
    }
    public long getFileDateTime() {
        return mlFileDateTime;
    }
}    