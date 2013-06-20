package neo.util.comm;
public class WorkDay {
    private int miNumHollyDays;						// ���� ����
    private String[] mstrHollyDays	= new String[100];	// ���� ���
    private BeanWorkTimes minsWorkTimes = null;		// ���� �����ð� ����-�߰�(2002.5.21)
    public WorkDay(PageContext insPageContext,            
                    boolean fromJungSan ) throws Exception {
        if ( insPageContext != null ) // ����� ȣ��� ���� null��
            minsWorkTimes = (BeanWorkTimes)insPageContext.getAttribute("WorkTimes", insPageContext.SESSION_SCOPE);
        {
            //���� �д� ��ƾ�� ���� �޼ҵ�� ��ü(2002.5.21)
        }
        else
        {
            long lLastDateTime = minsWorkTimes.getFileDateTime(),
            //���� ������ ��¥", lLastDateTime
            if ( lLastDateTime < lThisDateTime ) {
                //holiday.dat �ٲ���� -> file �ٽ� read
                readFile( insPageContext, fromJungSan );
            }
        }
    public boolean isWorkDay( String strDate ) throws Exception {
        //�ܼ��� ���� �����ð� bean�� ȣ���ϵ��� ����
        if ( ((BeanWorkTime)minsWorkTimes.get( strDate )).getDateType() != '*' )
            bResult = true;
        return bResult;
    }
    public BeanWorkTime getWorkDay( String strDate, int iWorkDaysAgo ) throws Exception {
        BeanWorkTime insResult = null;
        insResult = (BeanWorkTime)minsWorkTimes.get( strDate,
                                                     						-iWorkDaysAgo );
        return insResult;
    }
}