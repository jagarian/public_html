package neo.util.comm;
public class BeanWorkTimes implements Serializable {
    private Vector 	minsWorkTimes;	// ���� �����ð� ���
    private String		mstrFileName;	// holiday.dat ���� ��ġ
    private long		mlFileDateTime;	// holiday.dat ���� �����Ͻ�
    public BeanWorkTimes( String strFileName, long lFileDateTime ) {
        minsWorkTimes = new Vector();
        mstrFileName   = strFileName;
        mlFileDateTime = lFileDateTime;
    }    
    public void add( BeanWorkTime insWorkTime ) throws CommandException {
        minsWorkTimes.add( insWorkTime );
    }
    //�־��� ���� ��/�ľ������� ���Ѵ�
    public BeanWorkTime get( String strDate, int iDays ) throws Exception {
        BeanWorkTime insResult = null;
        int i = 0;
        	//�־��� ���� ã�´�
        	for ( i = 0 ; i < minsWorkTimes.size(); i++ )
        		if ( ((BeanWorkTime)minsWorkTimes.get( i )).getDate().equals( strDate ) )
        			break;
        	if ( i == minsWorkTimes.size() ) {
        		//�߸��� holiday.dat  --> ���� ����
        	} else
        		insResult = (BeanWorkTime)minsWorkTimes.get( i );        
        	//ã�� ���� �־��� ��/�� �������� ���Ѵ�
        	while( iDays != 0 ) {
        		// �Ϸ� ��/�� �������� ���Ѵ�
        		i = i + Math.abs( iDays ) / iDays;
        		insResult = (BeanWorkTime)minsWorkTimes.get( i );
        		// ���� ���� �������̸� iDay ���밪 ����
        		if ( insResult.getDateType() != '*' )
        			iDays -= Math.abs( iDays ) / iDays;
        	}
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