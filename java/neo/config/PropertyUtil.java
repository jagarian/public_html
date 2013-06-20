package neo.config;

import java.util.*;
import java.io.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	PropertyUtil.java
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
public class PropertyUtil {

    InputStream insIs = null;

    /**
     * 	생성자. Configuration.xml화일에서의 위치를 받아 해당화일을 Loading한다.
     * @param 	pstrFileName : 읽을 화일이름
     * @param 	pstrKey : 찾고자 하는 Key값
     * @return 	String : Key에 해당하는 Value
     * @throws	Exception
     */
    public PropertyUtil( String pstrXPath ){
        try{
            String mstrFileName = Config.getInstance().getStringByServerEnv(pstrXPath);
            this.insIs = new FileInputStream(mstrFileName);
        }catch( Exception ex ){
            Log.error("From PropertyUtil : File Loading중 에러발생");
        }
    }

    /**
     * 	Property 파일을 읽어 해당 Key에 해당하는 값 반환한다.
     * @param 	pstrFileName : 읽을 화일이름
     * @param 	pstrKey : 찾고자 하는 Key값
     * @return  	String : Key에 해당하는 Value
     * @throws 	Exception
     */
    public String getPropertyValue( String pstrKey ){
        Properties insPt = new Properties();
        String rstrPropertyValue = "NoValue.TryAgain";
        try{
            insPt.load(insIs);
            rstrPropertyValue = insPt.getProperty(pstrKey);
        }catch(Exception e){
        	Log.error("From PropertyUtil : getPropertyValue()수행중 에러발생");
        }
        return rstrPropertyValue;
    }
    
    public static String getPropertyValue( String mstrFileName, String pstrKey ){
        Properties insPt = new Properties();
        String rstrPropertyValue = "NoValue.TryAgain";
        try{
        	InputStream isRef = new FileInputStream(mstrFileName);
            insPt.load(isRef);
            rstrPropertyValue = insPt.getProperty(pstrKey);
        }catch(Exception e){
        	Log.error("getPropertyValue()수행중 에러발생");
        }
        return rstrPropertyValue;
    }

    /**
     * Property 파일을 읽어 해당 Stream자체를 반환한다.
     * @param 	pstrFileName : 읽을 화일이름
     * @param 	pstrKey : 찾고자 하는 Key값
     * @return  	String : Key에 해당하는 Value
     * @throws	Exception
     */
    public InputStream getInputStream(){
        return insIs;
    }

    public String getPropertyContents(){
        BufferedReader insBr = null;
        String rstrBodyContents = "";
        String mstrTmpLine = null;

        try {
            insBr = new BufferedReader(new InputStreamReader(insIs));

            for (int index = 0; (mstrTmpLine = insBr.readLine()) != null;
                 index++) {
                rstrBodyContents = rstrBodyContents + mstrTmpLine.trim() + "\n";
            }
            insBr.close();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }finally {
            try { insBr.close(); } catch (Exception ex) { ex.printStackTrace(System.out); }
        }
        return rstrBodyContents.trim();
    }

    /**
     * *.dat의 내용을 한줄씩 읽어 각 줄에 해당하는 내용이 존재하면 true를 반환
     * 유효 IP리스트를 가져오는데 사용함.
     * @param 	pstrFileName : 읽을 화일이름
     * @param 	pstrKey : 찾고자 하는 Key값
     * @return		String : Key에 해당하는 Value
     * @throws 	Exception
     */
    public boolean hasItemInContentsArray(String pstrItem) {
        boolean rbHaveItem = false; //return할결과값.
        int miIpCnt = 0; //실제 IP갯수를 저장할 변수
        String[] minsArr = new String[10];//최고갯수를 기존처럼 10개만 적음
        BufferedReader insBr = null;

        try {
            insBr = new BufferedReader(new InputStreamReader(insIs));
            for ( miIpCnt = 0; (minsArr[miIpCnt] = insBr.readLine()) != null; miIpCnt++ );
            	insBr.close();

            for ( int i = 0; i < miIpCnt; i++ ) { //같은내용이 있다면 true반환.
                rbHaveItem = rbHaveItem || ( pstrItem.equals(minsArr[i]) );
            }

       	}catch (Exception e) {
           	e.printStackTrace(System.out);
       	}finally { 
       		try { 
       			insBr.close(); 
       		} catch (Exception ex) {
       			ex.printStackTrace(System.out);
       		}
        }
        return rbHaveItem;
    }
}
