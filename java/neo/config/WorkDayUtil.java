package neo.config;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.PageContext;

import neo.exception.CommandException;
import neo.exception.ConfigException;
import neo.util.comm.BeanWorkTime;
import neo.util.comm.BeanWorkTimes;
import neo.util.comm.DateUtil;
import neo.util.comm.HolidayUtil;
import neo.util.comm.WorkDay;
import neo.util.log.Log;
import neo.util.session.SessionUtil;

/**
 * 	@Class Name	: 	WorkDayUtil.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class WorkDayUtil {
	private int miHolidayCnt; 							// ���� ����
	private String[] mstrHolidays = new String[100];	// ���� ���
	private BeanWorkTimes minsWorkTimes = null;

	public WorkDayUtil(HttpServletRequest req, boolean pbIsFromAccount) throws ConfigException {
		if (req != null) {
			minsWorkTimes = (BeanWorkTimes) SessionUtil.getValue(req, "WorkTimes");
		}

		if (minsWorkTimes == null) { // ������ ������� ���Ͽ��� Read
			readHolidayFile(req, pbIsFromAccount);

		} else { // ���ǿ� ���������ð� ���� ������
			// File�� ���泯¥�� ���Ͽ� ���������� Ȯ���Ѵ�.
			long lLastDateTime = minsWorkTimes.getFileDateTime();
			long lThisDateTime = (new File(minsWorkTimes.getFileName())).lastModified();
			if (lLastDateTime < lThisDateTime) { // ���泯¥�� Ʋ���ٸ�
				readHolidayFile(req, pbIsFromAccount);
			}
		}
	}

	public boolean isWorkDay(String pstrDate) throws CommandException {
		boolean rbIsWorkDay = false;
		try {
			// ������ ���ڰ� W = Work, H = Holiday. H�� �ƴϸ� ��������.
			if ((minsWorkTimes.get(pstrDate)).getDateType() != 'H') {
				rbIsWorkDay = true;
			}
		} catch (Exception e) {
			throw new CommandException("fail in WorkDayUtil :: " + e.getMessage(), e);
		}	
		return rbIsWorkDay;
	}

	public BeanWorkTime getWorkDay(String pstrDate, int piWorkDaysAgo) throws CommandException {
		BeanWorkTime rinsWorkTime = null;
		try {
			rinsWorkTime = minsWorkTimes.get(pstrDate, -piWorkDaysAgo);
		} catch (Exception e) {
			throw new CommandException("fail in WorkDayUtil :: " + e.getMessage(), e);
		}
		return rinsWorkTime;
	}

	private void readHolidayFile(HttpServletRequest pinsRequest,
			boolean pbIsFromAccount) throws ConfigException {
		String mstrHolidayFileLocation = Config.getInstance().getStringByServerEnv("/config/neo/holiday-file");	
		BufferedReader insBr = null;
		InputStream insIs = null;
		try {
			// �����θ� ���� file�� ���� ��������
			long lLastModified = (new File(mstrHolidayFileLocation)).lastModified();
			minsWorkTimes = new BeanWorkTimes(mstrHolidayFileLocation, lLastModified);
			// ����θ� ���� �о�� holiday.datȭ���� InputSteam
			insIs = getInputStream(mstrHolidayFileLocation);
			insBr = new BufferedReader(new InputStreamReader(insIs));

			// ���پ� ��¥�� �д´�.
			for (miHolidayCnt = 0; (mstrHolidays[miHolidayCnt] = insBr.readLine()) != null; miHolidayCnt++) {

				// ���ٿ��� �� �ð��� tokenize�Ѵ�
				StringTokenizer insToken = new StringTokenizer(mstrHolidays[miHolidayCnt], " ");
				String[] strToken = new String[5];
				int idx = 0;
				for (idx = 0; insToken.hasMoreTokens(); idx++) {
					strToken[idx] = insToken.nextToken();
				}

				// �� �ð��� bean�� �����Ѵ�
				BeanWorkTime insWorkTime = null;

				if (idx == 5) {
					insWorkTime = new BeanWorkTime(strToken[0], strToken[1], strToken[2], strToken[3], strToken[4].charAt(0));
				} else {
					throw new CommandException("RCOMWorkDayU.java : �ùٸ� holiday.dat������ �ƴմϴ�.");
				}

				minsWorkTimes.add(insWorkTime);
			}

			insBr.close();
			// ���ǿ� �����Ѵ�.
			if (pinsRequest != null) { // ����� ȣ���� �ƴ� ��츸
				SessionUtil.setValue(pinsRequest, "WorkTimes", minsWorkTimes);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	/**
	 * @title	 	Property ������ �д´�.
	 * 
	 * @param	
	 * @return 	
	 */
	public InputStream getInputStream( String pstrXPath ){
		PropertyUtil inspt = new PropertyUtil( pstrXPath );
        return inspt.getInputStream();
    }
	
	/**
	 * @title	 	������ ���������� �Ǵ��Ѵ�
	 * 
	 * @param	Ư������ ���ڿ�
	 * @return 	���������� ����
	 */
    public boolean isWorkDay(	PageContext insPageContext,
    							boolean fromJungSan ) {
    	return isWorkDay( insPageContext, DateUtil.getToday(), fromJungSan );
    }

    public boolean isWorkDay( PageContext insPageContext ) {
    	return isWorkDay( insPageContext, false );
    }
        
    /**
	 * @title	 	Ư������ ���������� �Ǵ��Ѵ�
	 * 
	 * @param	Ư������ ���ڿ�
	 * @return 	���������� ����
	 */
    public boolean isWorkDay( PageContext insPageContext,
            					String strDate,
            					boolean fromJungSan ) {
    	boolean bResult = false;
    	try {
	    	WorkDay insWorkDay = new WorkDay( insPageContext, fromJungSan );
	    	if ( insWorkDay.isWorkDay( strDate ) )
	    		bResult = true;
	    	else
	    		bResult = false;
    	} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
    	return bResult;

    }

    public boolean isWorkDay(PageContext insPageContext, String strDate ) {
    	return isWorkDay( insPageContext, strDate, false );
    }

    /**
	 * @title	 	Ư���Ϸ� ������ ���������� ���Ѵ�
	 * 
	 * @param	Ư����
	 * 					������ ������������ �ϼ�
	 * @return 	�������� ���ڿ�
	 */
    public BeanWorkTime getBefWorkDay( PageContext insPageContext
    									,String strDate
    									,int iWorkDaysAgo
    									,boolean fromJungSan ) {
    	BeanWorkTime insResult = null;
    	try {
    		WorkDay insWorkDay = new WorkDay( insPageContext, fromJungSan );    		
    		insResult = insWorkDay.getWorkDay( strDate, iWorkDaysAgo );
    	} catch ( Exception e ) {
    		Log.error(e.getMessage());
		}
    	return insResult;
    }
    
    public BeanWorkTime getBefWorkDay( PageContext insPageContext,
       									String strDate, 
       									int iWorkDaysAgo ) {
    	return getBefWorkDay( insPageContext, strDate, iWorkDaysAgo, false );
    }
    
    /**
	 * @title	 	���������� ���Ѵ�
	 * 
	 * @param	������ ������������ �ϼ�
	 * @return 	�������� ���ڿ�
	 */
    public BeanWorkTime getBefWorkDay(	PageContext insPageContext,
    									int iWorkDaysAgo,
    									boolean fromJungSan ) {
    	return getBefWorkDay(	insPageContext,
    							DateUtil.getToday(),
    							iWorkDaysAgo, fromJungSan );
    }

    public BeanWorkTime getBefWorkDay(	PageContext insPageContext,
            							int iWorkDaysAgo ) {
    	return getBefWorkDay( insPageContext, iWorkDaysAgo, false );
    }
    
    /**
	 * @title	 	Ư������ ��¥ ������ ���Ѵ�
	 * 
	 * @param	Ư������ ��¥ ������ ���Ѵ�
	 * @return 	'W' (Work day) : ������
	 * 					'S' (Saturday) : �������� �����
	 * 					'H' (Holiday)  : �Ͽ��� �� ���� ������
	 */
    public char getDateType( PageContext insPageContext
            				,String      strDate
            				,boolean     fromJungSan ) {
    	String tempStr = "X";
    	char cResult = tempStr.charAt(0);
    	try {
	    	//���� �����ð� bean���� ��¥ ������ ���Ѵ�.
	    	BeanWorkTime insWorkTime = (BeanWorkTime)(new WorkDay( insPageContext, fromJungSan )).getWorkDay( strDate, 0 ); 
	    	cResult = insWorkTime.getDateType();
	    	
	    	//�������̸� �������� ��������� �Ǵ��Ѵ�
	    	if ( cResult == 'W' ) {
	    		//Calendar�� �־������� ����Ѵ�
	    		Calendar insCalendar = Calendar.getInstance();
	    		insCalendar.set( Integer.parseInt( strDate.substring( 0, 4 ) )
	    								,Integer.parseInt( strDate.substring( 4, 6 ) ) - 1
	    								,Integer.parseInt( strDate.substring( 6, 8 ) ) );
	    		//�־��� ���� ������̸� ����
	    		if ( insCalendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY )
	    			cResult = 'S';
	    	}
    	} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
    	return cResult;
    }

    public char getDateType( PageContext insPageContext ) {
    	return getDateType( insPageContext, DateUtil.getToday(), false );
    }

    public char getDateType( PageContext insPageContext, String strDate ) {
    	return getDateType( insPageContext, strDate, false );
    }

    public boolean isWorkTime( PageContext insPageContext ) {
    	String strStartTime = getBefWorkDay( insPageContext, 0 ).getSvcStartTime(),
    														strEndTime=getBefWorkDay( insPageContext, 0 ).getSvcEndTime()
    														,strNow = DateUtil.getNow();
    	boolean bResult = false;
    	if ( 	strNow.compareTo( strStartTime ) >= 0 &&
    			strNow.compareTo( strEndTime   ) <= 0 )
    		bResult = true;
    	return bResult;
    }

    public boolean isSignTime( PageContext insPageContext ) {
    	String strStartTime = getBefWorkDay( insPageContext, 0 ).getSvcStartTime()
    														,strEndTime=getBefWorkDay( insPageContext, 0 ).getSignEndTime()
    														,strNow = DateUtil.getNow();
    	boolean bResult = false;
    	if ( 	strNow.compareTo( strStartTime ) >= 0 &&
    			strNow.compareTo( strEndTime   ) <= 0 )
    		bResult = true;

    	return bResult;
    }
    
  //���� ���� ������� üũ�Ѵ�.
	public static boolean isPayController( String pstrControllerName ) {
		boolean isPayController = false;
        try {            
            String mstrPayList = Config.getInstance().getStringByServerEnv("/config/neo/pay-request-list");
            StringTokenizer insSt = new StringTokenizer(mstrPayList, ",");
            while (insSt.hasMoreTokens()) {
                mstrPayList = insSt.nextToken();
                if (mstrPayList != null && !mstrPayList.equals("")) {
                    if (mstrPayList.equals(pstrControllerName)) {
                        isPayController = true;
                    }
                }
            }            
        } catch (Exception ex) {
        	Log.error("[ERROR] Can't find a value for the key : " + ex.getMessage());
        }
        return isPayController;
    }
    
    /**
	 * @title	 	��ȿ�� ���ӻ��������� �Ǻ��Ͽ� ���� Script�� ��ȯ�Ѵ�.
	 * 
	 * 					1. ��ȿIPüũ 2.���Ӱ��ɽð�����Ȯ�� 3.��������ȿ���� 4. �ý��ۻ��°� ��������
	 * 					5. �������ɽð����� ��..
	 * 
	 * @param	pinsRequest Request��ü
	 * 					pstrFlag �������׽ý��ۿ��� ���ӿ��ΰ�� [�������׽ý���:"noti"]
	 * 					pstrPop PopUpâ������ ���� [ null Ȥ�� ""�Ͻ� popup���� �Ǵ�]
	 * 					pstrPay �������ɽð��� �ƴ϶�� �Űܰ� ������	 * 					
	 * @return 	�ش翡��Script Ȥ�� �����ϰ�� ""
	 */
    public String isConnectionAvailable( HttpServletRequest pinsRequest , String pstrControllerName ) {
        
        String pstrFlag = ""; //�������� ���� ȣ���ߴ����� ������. "noti"�� �������׿��� ȣ��Ȱ�        
        String rstrReturnControllerName = ""; //��ȯ�� Err�޼���. ���������� ""��ȯ.
        
        boolean mbIsWorkDay; // ������ ����
        boolean mbIsWorkTime; // �����ð� ����
        boolean mbIsSignTime; // �����ð� ����
        boolean mbIsRightIP = false; //��ȿ�� IP���������� �Ǻ�����
        int mintDay = 0;
        int mintHours = 0;
        int mintMinutes = 0;
        
        String mstrRemoteIpAddress; //Request��û�� ���� IP�ּ�
        String mstrSystemStatus = null; //�ý��� ���°�. Y:����, N:�ý�������
        String mstrCheckSessionFlag = null;
        String mstrCheckHourFlag = null;
        String mstrCheckPayFlag = null;
        String mstrCheckSystemFlag = "Y";
        String mstrFromWhere = null;
        String mstrNotiFlag = "Y";
        
        String mstrErrController1 = ""; //������ ���� �������  ���ӽ� Error Message        
        String mstrErrController2 = ""; //�������� �Ǿ�����        
        String mstrErrController3 = ""; //�ý��� ���� = N �϶� ���ӺҰ� ȭ������ ��ȯ�ϴ� Script        
        String mstrErrController4 = ""; //�ý��� ���񽺽ð��� �ƴҶ�        
        String mstrErrController5 = ""; //������ ����Ǿ�����        
        String mstrErrController6 = ""; //�����ð� ��������
        
        try {
            // 1. ���Ӱ���IP���� Ȯ���Ѵ�. �ش�IP�� ���񽺽ð��̿ܿ��� �������� ����.
           mstrRemoteIpAddress = pinsRequest.getRemoteAddr();
           String mstrRightIpList = Config.getInstance().getStringByServerEnv("/config/neo/time_over_service_enable_ip_list");
           StringTokenizer insSt = new StringTokenizer(mstrRightIpList, ",");
              while (insSt.hasMoreTokens()) {
                  mstrRightIpList = insSt.nextToken();
                  if (mstrRightIpList != null && !mstrRightIpList.equals("")) {
                      if (mstrRightIpList.equals(mstrRemoteIpAddress)) {
                          mbIsRightIP = true;
                      }
                  }
              }

            if (mbIsRightIP) { //��ȿIP���
                mbIsWorkDay = true;
                mbIsWorkTime = true;
                mbIsSignTime = true;
            } else {
                // 2. ���� �ð����� Ȯ���Ѵ�.
            	HolidayUtil insHoli = new HolidayUtil();
                mbIsWorkDay = insHoli.isWorkDay();
                mbIsWorkTime = insHoli.isServiceTime();
                mbIsSignTime = insHoli.isSignTime();
            }

            //���� �ý����̶��
            if ( (pstrFlag != null) && (pstrFlag.equals("noti"))) {
            	SessionUtil.init(pinsRequest);
                mstrCheckSystemFlag = "Y";
                mstrCheckHourFlag = "Y";
                mstrCheckSessionFlag = "Y";
                mstrCheckPayFlag = "Y";                
                if (!mbIsRightIP) { //��ȿ�� IP�� �ƴ϶��
                    rstrReturnControllerName = mstrErrController1;
                    mstrNotiFlag = "N";
                } else {
                    if (SessionUtil.isNew(pinsRequest)) { //������ ����� ���
                        rstrReturnControllerName = mstrErrController2;
                        mstrCheckSessionFlag = "N";
                    }
                }
            }else {
                //�ý��� ���¼������� ��. SysStatus.dat�� Main�� Property
                //�ý��� ���� ������ �����ϰ� �ִ� class
                mstrSystemStatus = Config.getInstance().get("/config/neo/system-on-off/value");
                //frontservlet���� �������� �Ǻ��� �ý��� �������°� 'N'�̶�� ����������
                //Closeȭ������ �̵��ϰ��Ͽ����� �׷���� ���������� �����ص� ���������� ���� �ʰ�
                //�ٷ� closeȭ������ �̵��Ѵ�. ���� �ʱ�ȭ��� ��������ȭ���� �ý��ۻ��¿� ���ӵ���
                //�ʰ� �ۿ��� �� �ֵ��� �ϱ����� �� ��츦 �����Ѵ�.
                if (mstrSystemStatus.equals("N")) {
                	
                	if ( PathByModuleModel.getInstance().isFoundValue(pstrControllerName) ) {
                		//�ý����� ���� ���¶� ���� �ϴ� ȭ���� �ִٸ� ȯ��ȭ�� ��� 'path-by-module' �� ����� �� ó���Ѵ�.(key=value, �� value �� ����Ѱ�)
                        mstrCheckSystemFlag = "N";
                        rstrReturnControllerName = mstrErrController3;
                	}
                    
                }else{
                    mstrFromWhere = pinsRequest.getParameter("from");
                    mintDay = DateUtil.getCurrentDay();
                    if (mbIsWorkDay) { //�۾����� �´ٸ�
                        if (!mbIsWorkTime ) { //�۾��ð��� �ƴ϶��
                            mstrCheckHourFlag = "N";
                            rstrReturnControllerName = mstrErrController4;
                        } else {
                            mstrCheckHourFlag = "Y";
                        }

                    } else {
                        mstrCheckHourFlag = "N";
                        rstrReturnControllerName = mstrErrController4;
                    }
                    
                    if (mstrCheckHourFlag.equals("Y")) { //���� �ð��� ���
                        //��ɼ� ȭ�鿡�� ȣ��� ���
                        if ( (mstrFromWhere == null) || (mstrFromWhere.equals(""))) {
                        	SessionUtil.init(pinsRequest);
                            //���� ���ӽ� ��������� �Ǻ������� ����ȭ���� ȣ���ϴ� ��Ʈ�ѷ��� ���� ����.
                            //����� 1 �̱⿡ �ϵ��ڵ����� �Ǻ�. ?
                            if ( SessionUtil.isNew(pinsRequest) && !pstrControllerName.equals("RHOMMainC")) {
                                mstrCheckSessionFlag = "N";
                                rstrReturnControllerName = mstrErrController5;
                           } else {
                                mstrCheckSessionFlag = "Y";
                            }
                        } else {
                            mstrCheckSessionFlag = "Y";
                        }
                        
                        if (mstrCheckSessionFlag.equals("Y")) { // �������� ������ ���
                        	
                            // ����ȭ���� �ƴ� ���
                            if( !isPayController( pstrControllerName ) ){
                                mstrCheckPayFlag = "Y";
                                
                            // �������� Controller���
                            }else{
                                if (!mbIsSignTime) { //�����ð�����
                                    mstrCheckPayFlag = "N";
                                    rstrReturnControllerName = mstrErrController6;
                                } else {
                                    mstrCheckPayFlag = "Y";
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return rstrReturnControllerName;
    }
}
