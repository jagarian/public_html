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
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class WorkDayUtil {
	private int miHolidayCnt; 							// 휴일 갯수
	private String[] mstrHolidays = new String[100];	// 휴일 목록
	private BeanWorkTimes minsWorkTimes = null;

	public WorkDayUtil(HttpServletRequest req, boolean pbIsFromAccount) throws ConfigException {
		if (req != null) {
			minsWorkTimes = (BeanWorkTimes) SessionUtil.getValue(req, "WorkTimes");
		}

		if (minsWorkTimes == null) { // 없으면 기존대로 파일에서 Read
			readHolidayFile(req, pbIsFromAccount);

		} else { // 세션에 서비스제공시간 정보 있을때
			// File의 변경날짜를 비교하여 변경유무를 확인한다.
			long lLastDateTime = minsWorkTimes.getFileDateTime();
			long lThisDateTime = (new File(minsWorkTimes.getFileName())).lastModified();
			if (lLastDateTime < lThisDateTime) { // 변경날짜가 틀리다면
				readHolidayFile(req, pbIsFromAccount);
			}
		}
	}

	public boolean isWorkDay(String pstrDate) throws CommandException {
		boolean rbIsWorkDay = false;
		try {
			// 마지막 글자가 W = Work, H = Holiday. H가 아니면 서비스일자.
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
			// 절대경로를 통한 file의 최종 변경일자
			long lLastModified = (new File(mstrHolidayFileLocation)).lastModified();
			minsWorkTimes = new BeanWorkTimes(mstrHolidayFileLocation, lLastModified);
			// 상대경로를 통해 읽어온 holiday.dat화일의 InputSteam
			insIs = getInputStream(mstrHolidayFileLocation);
			insBr = new BufferedReader(new InputStreamReader(insIs));

			// 한줄씩 날짜를 읽는다.
			for (miHolidayCnt = 0; (mstrHolidays[miHolidayCnt] = insBr.readLine()) != null; miHolidayCnt++) {

				// 한줄에서 각 시각을 tokenize한다
				StringTokenizer insToken = new StringTokenizer(mstrHolidays[miHolidayCnt], " ");
				String[] strToken = new String[5];
				int idx = 0;
				for (idx = 0; insToken.hasMoreTokens(); idx++) {
					strToken[idx] = insToken.nextToken();
				}

				// 각 시각을 bean에 저장한다
				BeanWorkTime insWorkTime = null;

				if (idx == 5) {
					insWorkTime = new BeanWorkTime(strToken[0], strToken[1], strToken[2], strToken[3], strToken[4].charAt(0));
				} else {
					throw new CommandException("RCOMWorkDayU.java : 올바른 holiday.dat형식이 아닙니다.");
				}

				minsWorkTimes.add(insWorkTime);
			}

			insBr.close();
			// 세션에 저장한다.
			if (pinsRequest != null) { // 정산시 호출이 아닌 경우만
				SessionUtil.setValue(pinsRequest, "WorkTimes", minsWorkTimes);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	/**
	 * @title	 	Property 파일을 읽는다.
	 * 
	 * @param	
	 * @return 	
	 */
	public InputStream getInputStream( String pstrXPath ){
		PropertyUtil inspt = new PropertyUtil( pstrXPath );
        return inspt.getInputStream();
    }
	
	/**
	 * @title	 	오늘이 업무일인지 판단한다
	 * 
	 * @param	특정일의 문자열
	 * @return 	업무일인지 여부
	 */
    public boolean isWorkDay(	PageContext insPageContext,
    							boolean fromJungSan ) {
    	return isWorkDay( insPageContext, DateUtil.getToday(), fromJungSan );
    }

    public boolean isWorkDay( PageContext insPageContext ) {
    	return isWorkDay( insPageContext, false );
    }
        
    /**
	 * @title	 	특정일이 업무일인지 판단한다
	 * 
	 * @param	특정일의 문자열
	 * @return 	업무일인지 여부
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
	 * @title	 	특정일로 부터의 전업무일을 구한다
	 * 
	 * @param	특정일
	 * 					몇일전 업무일인지의 일수
	 * @return 	업무일의 문자열
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
	 * @title	 	전업무일을 구한다
	 * 
	 * @param	몇일전 업무일인지의 일수
	 * @return 	업무일의 문자열
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
	 * @title	 	특정일의 날짜 종류를 구한다
	 * 
	 * @param	특정일의 날짜 종류를 구한다
	 * @return 	'W' (Work day) : 업무일
	 * 					'S' (Saturday) : 업무일인 토요일
	 * 					'H' (Holiday)  : 일요일 및 법정 공휴일
	 */
    public char getDateType( PageContext insPageContext
            				,String      strDate
            				,boolean     fromJungSan ) {
    	String tempStr = "X";
    	char cResult = tempStr.charAt(0);
    	try {
	    	//서비스 제공시각 bean에서 날짜 종류를 구한다.
	    	BeanWorkTime insWorkTime = (BeanWorkTime)(new WorkDay( insPageContext, fromJungSan )).getWorkDay( strDate, 0 ); 
	    	cResult = insWorkTime.getDateType();
	    	
	    	//업무일이면 업무일인 토요일인지 판단한다
	    	if ( cResult == 'W' ) {
	    		//Calendar에 주어진날을 등록한다
	    		Calendar insCalendar = Calendar.getInstance();
	    		insCalendar.set( Integer.parseInt( strDate.substring( 0, 4 ) )
	    								,Integer.parseInt( strDate.substring( 4, 6 ) ) - 1
	    								,Integer.parseInt( strDate.substring( 6, 8 ) ) );
	    		//주어진 날이 토요일이면 갱신
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
    
  //결재 관련 모듈인지 체크한다.
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
	 * @title	 	유효한 접속상태인지를 판별하여 관련 Script를 반환한다.
	 * 
	 * 					1. 유효IP체크 2.접속가능시간인지확인 3.세션이유효한지 4. 시스템상태가 정상인지
	 * 					5. 결제가능시각인지 등..
	 * 
	 * @param	pinsRequest Request객체
	 * 					pstrFlag 공지사항시스템에서 접속여부경우 [공지사항시스템:"noti"]
	 * 					pstrPop PopUp창인지의 여부 [ null 혹은 ""일시 popup으로 판단]
	 * 					pstrPay 결제가능시각이 아니라면 옮겨갈 페이지	 * 					
	 * @return 	해당에러Script 혹은 정상일경우 ""
	 */
    public String isConnectionAvailable( HttpServletRequest pinsRequest , String pstrControllerName ) {
        
        String pstrFlag = ""; //공지사항 에서 호출했는지의 구분자. "noti"면 공지사항에서 호출된것        
        String rstrReturnControllerName = ""; //반환될 Err메세지. 에러없을시 ""반환.
        
        boolean mbIsWorkDay; // 업무일 여부
        boolean mbIsWorkTime; // 업무시간 여부
        boolean mbIsSignTime; // 결제시간 여부
        boolean mbIsRightIP = false; //유효한 IP접속인지의 판별여부
        int mintDay = 0;
        int mintHours = 0;
        int mintMinutes = 0;
        
        String mstrRemoteIpAddress; //Request요청이 들어온 IP주소
        String mstrSystemStatus = null; //시스템 상태값. Y:정상, N:시스템정지
        String mstrCheckSessionFlag = null;
        String mstrCheckHourFlag = null;
        String mstrCheckPayFlag = null;
        String mstrCheckSystemFlag = "Y";
        String mstrFromWhere = null;
        String mstrNotiFlag = "Y";
        
        String mstrErrController1 = ""; //허용되지 않은 사용자의  접속시 Error Message        
        String mstrErrController2 = ""; //세션종료 되었을때        
        String mstrErrController3 = ""; //시스템 상태 = N 일때 접속불가 화면으로 전환하는 Script        
        String mstrErrController4 = ""; //시스템 서비스시간이 아닐때        
        String mstrErrController5 = ""; //세션이 종료되었을때        
        String mstrErrController6 = ""; //결제시간 지났을때
        
        try {
            // 1. 접속가능IP인지 확인한다. 해당IP는 서비스시간이외에도 정상접속 가능.
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

            if (mbIsRightIP) { //유효IP라면
                mbIsWorkDay = true;
                mbIsWorkTime = true;
                mbIsSignTime = true;
            } else {
                // 2. 서비스 시간인지 확인한다.
            	HolidayUtil insHoli = new HolidayUtil();
                mbIsWorkDay = insHoli.isWorkDay();
                mbIsWorkTime = insHoli.isServiceTime();
                mbIsSignTime = insHoli.isSignTime();
            }

            //관리 시스템이라면
            if ( (pstrFlag != null) && (pstrFlag.equals("noti"))) {
            	SessionUtil.init(pinsRequest);
                mstrCheckSystemFlag = "Y";
                mstrCheckHourFlag = "Y";
                mstrCheckSessionFlag = "Y";
                mstrCheckPayFlag = "Y";                
                if (!mbIsRightIP) { //유효한 IP가 아니라면
                    rstrReturnControllerName = mstrErrController1;
                    mstrNotiFlag = "N";
                } else {
                    if (SessionUtil.isNew(pinsRequest)) { //세션이 종료된 경우
                        rstrReturnControllerName = mstrErrController2;
                        mstrCheckSessionFlag = "N";
                    }
                }
            }else {
                //시스템 상태설정상태 값. SysStatus.dat의 Main의 Property
                //시스템 설정 정보를 저장하고 있는 class
                mstrSystemStatus = Config.getInstance().get("/config/neo/system-on-off/value");
                //frontservlet에서 공통으로 판별시 시스템 설정상태가 'N'이라면 강제적으로
                //Close화면으로 이동하게하였으나 그럴경우 공지사항을 띄운다해도 공지사항이 뜨지 않고
                //바로 close화면으로 이동한다. 따라서 초기화면과 공지사항화면은 시스템상태에 종속되지
                //않고 작용할 수 있도록 하기위해 두 경우를 제외한다.
                if (mstrSystemStatus.equals("N")) {
                	
                	if ( PathByModuleModel.getInstance().isFoundValue(pstrControllerName) ) {
                		//시스템이 정지 상태라도 떠야 하는 화면이 있다면 환경화일 경로 'path-by-module' 에 등록한 후 처리한다.(key=value, 단 value 는 비워둘것)
                        mstrCheckSystemFlag = "N";
                        rstrReturnControllerName = mstrErrController3;
                	}
                    
                }else{
                    mstrFromWhere = pinsRequest.getParameter("from");
                    mintDay = DateUtil.getCurrentDay();
                    if (mbIsWorkDay) { //작업일이 맞다면
                        if (!mbIsWorkTime ) { //작업시간이 아니라면
                            mstrCheckHourFlag = "N";
                            rstrReturnControllerName = mstrErrController4;
                        } else {
                            mstrCheckHourFlag = "Y";
                        }

                    } else {
                        mstrCheckHourFlag = "N";
                        rstrReturnControllerName = mstrErrController4;
                    }
                    
                    if (mstrCheckHourFlag.equals("Y")) { //서비스 시간인 경우
                        //기능성 화면에서 호출된 경우
                        if ( (mstrFromWhere == null) || (mstrFromWhere.equals(""))) {
                        	SessionUtil.init(pinsRequest);
                            //최초 접속시 세션종료로 판별함으로 최초화면을 호출하는 컨트롤러는 막지 않음.
                            //목록이 1 이기에 하드코딩으로 판별. ?
                            if ( SessionUtil.isNew(pinsRequest) && !pstrControllerName.equals("RHOMMainC")) {
                                mstrCheckSessionFlag = "N";
                                rstrReturnControllerName = mstrErrController5;
                           } else {
                                mstrCheckSessionFlag = "Y";
                            }
                        } else {
                            mstrCheckSessionFlag = "Y";
                        }
                        
                        if (mstrCheckSessionFlag.equals("Y")) { // 정상적인 세션인 경우
                        	
                            // 결제화면이 아닌 경우
                            if( !isPayController( pstrControllerName ) ){
                                mstrCheckPayFlag = "Y";
                                
                            // 결제관련 Controller라면
                            }else{
                                if (!mbIsSignTime) { //결제시간여부
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
