package neo.util.comm;import java.io.* ;import java.util.* ;import javax.servlet.jsp.*;import neo.config.Config;import neo.exception.CommandException;import neo.util.log.Log;/** * 	@Class Name	: 	WorkDay.java * 	@���ϼ���		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	�۾��� 		����	����	�۾���		���� * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����) *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�) *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ) *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���) **********************************************************************************************              */
public class WorkDay {
    private int miNumHollyDays;						// ���� ����
    private String[] mstrHollyDays	= new String[100];	// ���� ���
    private BeanWorkTimes minsWorkTimes = null;		// ���� �����ð� ����-�߰�(2002.5.21)
    public WorkDay(PageContext insPageContext,            
                    boolean fromJungSan ) throws Exception {    	    	System.out.println("WorkDay > ������");    	    	//���ǿ� ���� �����ð� ������ �ִ��� �Ǵ�
        if ( insPageContext != null ) // ����� ȣ��� ���� null��
            minsWorkTimes = (BeanWorkTimes)insPageContext.getAttribute("WorkTimes", insPageContext.SESSION_SCOPE);                if ( minsWorkTimes == null ) // ������ ������� ���Ͽ��� ����
        {
            //���� �д� ��ƾ�� ���� �޼ҵ�� ��ü(2002.5.21)            readFile( insPageContext, fromJungSan );
        }
        else
        {            //���ǿ� �̹� ���������ð� ���� ����
            long lLastDateTime = minsWorkTimes.getFileDateTime(),            lThisDateTime = (new File( minsWorkTimes.getFileName())).lastModified();
            //���� ������ ��¥", lLastDateTime            //���� ������ ��¥", lThisDateTime
            if ( lLastDateTime < lThisDateTime ) {
                //holiday.dat �ٲ���� -> file �ٽ� read
                readFile( insPageContext, fromJungSan );
            }
        }    }
    public boolean isWorkDay( String strDate ) throws Exception {
        //�ܼ��� ���� �����ð� bean�� ȣ���ϵ��� ����        boolean bResult = false;
        if ( ((BeanWorkTime)minsWorkTimes.get( strDate )).getDateType() != '*' )
            bResult = true;
        return bResult;
    }
    public BeanWorkTime getWorkDay( String strDate, int iWorkDaysAgo ) throws Exception {
        BeanWorkTime insResult = null;
        insResult = (BeanWorkTime)minsWorkTimes.get( strDate,
                                                     						-iWorkDaysAgo );
        return insResult;
    }        private void readFile( PageContext insPageContext, boolean fromJungSan ) throws Exception {    	        if ( fromJungSan ) {        	Config insConf = Config.getInstance();						String pLocation = insConf.getStringByServerEnv("/config/neo/holiday-file");			BufferedReader insBR = null;			try {								long lLastModified = (new File( pLocation )).lastModified();				minsWorkTimes = new BeanWorkTimes( pLocation, lLastModified );				insBR = new BufferedReader( new FileReader( pLocation ) );				//System.out.println("insBR :: "+insBR.toString());								//������ ���Ͽ��� ���پ� ���� ��¥�� �д´�.				for ( miNumHollyDays = 0;						(mstrHollyDays[miNumHollyDays]=insBR.readLine()) != null;						miNumHollyDays++ ) {										//���ٿ��� �� �ð��� tokenize�Ѵ�					StringTokenizer insToken = new StringTokenizer(mstrHollyDays[miNumHollyDays], " " );					String[] strToken = new String[5];					int iIndex = 0;					for ( iIndex = 0; insToken.hasMoreTokens(); iIndex++ )						strToken[iIndex] = insToken.nextToken();										//System.out.println("data :: "+mstrHollyDays[miNumHollyDays]);										//�� �ð��� bean�� �����Ѵ�					BeanWorkTime insWorkTime = null;										//System.out.println("���� ����/�ð�  :: "+strToken[0]+"/"+strToken[1]+"/"+strToken[2]+"/"+strToken[3]+"/"+strToken[4].charAt(0) );										if ( iIndex == 5 ) {						insWorkTime = new BeanWorkTime(	strToken[0]						                       			,strToken[1]						                       			,strToken[2]						                       			,strToken[3]						                       			,strToken[4].charAt(0) );					} else {						throw new CommandException("[WorkDay] holiday.dat ���� ���� �߸��� �����Ͱ� �����մϴ�. ");					}					minsWorkTimes.add( insWorkTime );				}				insBR.close();				if ( insPageContext != null ) 					insPageContext.setAttribute( "WorkTimes", minsWorkTimes, insPageContext.SESSION_SCOPE );			} catch ( Exception e ) {				throw new CommandException("fail in WorkDay :: " + e.getMessage(), e);			}        }    }       
}
