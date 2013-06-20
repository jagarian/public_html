package neo.util.comm;import java.io.* ;import java.util.* ;import javax.servlet.jsp.*;import neo.config.Config;import neo.exception.CommandException;import neo.util.log.Log;/** * 	@Class Name	: 	WorkDay.java * 	@파일설명		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	작업일 		버젼	구분	작업자		내용 * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기) *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스) *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티) *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅) **********************************************************************************************              */
public class WorkDay {
    private int miNumHollyDays;						// 휴일 갯수
    private String[] mstrHollyDays	= new String[100];	// 휴일 목록
    private BeanWorkTimes minsWorkTimes = null;		// 서비스 제공시각 정보-추가(2002.5.21)
    public WorkDay(PageContext insPageContext,            
                    boolean fromJungSan ) throws Exception {    	    	System.out.println("WorkDay > 생성자");    	    	//세션에 서비스 제공시간 정보가 있는지 판단
        if ( insPageContext != null ) // 정산시 호출된 경우는 null임
            minsWorkTimes = (BeanWorkTimes)insPageContext.getAttribute("WorkTimes", insPageContext.SESSION_SCOPE);                if ( minsWorkTimes == null ) // 없으면 기존대로 파일에서 읽음
        {
            //기존 읽는 루틴을 내부 메소드로 대체(2002.5.21)            readFile( insPageContext, fromJungSan );
        }
        else
        {            //세션에 이미 서비스제공시간 정보 있음
            long lLastDateTime = minsWorkTimes.getFileDateTime(),            lThisDateTime = (new File( minsWorkTimes.getFileName())).lastModified();
            //지난 파일의 날짜", lLastDateTime            //현재 파일의 날짜", lThisDateTime
            if ( lLastDateTime < lThisDateTime ) {
                //holiday.dat 바뀌었음 -> file 다시 read
                readFile( insPageContext, fromJungSan );
            }
        }    }
    public boolean isWorkDay( String strDate ) throws Exception {
        //단순히 서비스 제공시각 bean을 호출하도록 수정        boolean bResult = false;
        if ( ((BeanWorkTime)minsWorkTimes.get( strDate )).getDateType() != '*' )
            bResult = true;
        return bResult;
    }
    public BeanWorkTime getWorkDay( String strDate, int iWorkDaysAgo ) throws Exception {
        BeanWorkTime insResult = null;
        insResult = (BeanWorkTime)minsWorkTimes.get( strDate,
                                                     						-iWorkDaysAgo );
        return insResult;
    }        private void readFile( PageContext insPageContext, boolean fromJungSan ) throws Exception {    	        if ( fromJungSan ) {        	Config insConf = Config.getInstance();						String pLocation = insConf.getStringByServerEnv("/config/neo/holiday-file");			BufferedReader insBR = null;			try {								long lLastModified = (new File( pLocation )).lastModified();				minsWorkTimes = new BeanWorkTimes( pLocation, lLastModified );				insBR = new BufferedReader( new FileReader( pLocation ) );				//System.out.println("insBR :: "+insBR.toString());								//공휴일 파일에서 한줄씩 휴일 날짜를 읽는다.				for ( miNumHollyDays = 0;						(mstrHollyDays[miNumHollyDays]=insBR.readLine()) != null;						miNumHollyDays++ ) {										//한줄에서 각 시각을 tokenize한다					StringTokenizer insToken = new StringTokenizer(mstrHollyDays[miNumHollyDays], " " );					String[] strToken = new String[5];					int iIndex = 0;					for ( iIndex = 0; insToken.hasMoreTokens(); iIndex++ )						strToken[iIndex] = insToken.nextToken();										//System.out.println("data :: "+mstrHollyDays[miNumHollyDays]);										//각 시각을 bean에 저장한다					BeanWorkTime insWorkTime = null;										//System.out.println("서비스 일자/시간  :: "+strToken[0]+"/"+strToken[1]+"/"+strToken[2]+"/"+strToken[3]+"/"+strToken[4].charAt(0) );										if ( iIndex == 5 ) {						insWorkTime = new BeanWorkTime(	strToken[0]						                       			,strToken[1]						                       			,strToken[2]						                       			,strToken[3]						                       			,strToken[4].charAt(0) );					} else {						throw new CommandException("[WorkDay] holiday.dat 파일 내에 잘못된 데이터가 존재합니다. ");					}					minsWorkTimes.add( insWorkTime );				}				insBR.close();				if ( insPageContext != null ) 					insPageContext.setAttribute( "WorkTimes", minsWorkTimes, insPageContext.SESSION_SCOPE );			} catch ( Exception e ) {				throw new CommandException("fail in WorkDay :: " + e.getMessage(), e);			}        }    }       
}
