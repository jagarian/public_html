package neo.util.comm;import java.io.* ;import java.util.* ;import neo.exception.CommandException;import neo.util.log.Log;/** * 	@Class Name	: 	BeanWorkTimes.java * 	@���ϼ���		: 	 * 	@Version		: 	1.0 *	@Author		: 	hoon09 * 	@Copyright		: 	All Right Reserved ********************************************************************************************** * 	�۾��� 		����	����	�۾���		���� * -------------------------------------------------------------------------------------------- *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����) *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�) *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ) *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���) **********************************************************************************************              */
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
        int i = 0;        try {		
        	//�־��� ���� ã�´�
        	for ( i = 0 ; i < minsWorkTimes.size(); i++ )
        		if ( ((BeanWorkTime)minsWorkTimes.get( i )).getDate().equals( strDate ) )
        			break;        
        	if ( i == minsWorkTimes.size() ) {
        		//�߸��� holiday.dat  --> ���� ����        		throw new CommandException("�߸��� holiday.dat  --> ���� ����");
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