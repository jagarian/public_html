package neo.config;

import java.util.*;
import java.io.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	PropertyUtil.java
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
public class PropertyUtil {

    InputStream insIs = null;

    /**
     * 	������. Configuration.xmlȭ�Ͽ����� ��ġ�� �޾� �ش�ȭ���� Loading�Ѵ�.
     * @param 	pstrFileName : ���� ȭ���̸�
     * @param 	pstrKey : ã���� �ϴ� Key��
     * @return 	String : Key�� �ش��ϴ� Value
     * @throws	Exception
     */
    public PropertyUtil( String pstrXPath ){
        try{
            String mstrFileName = Config.getInstance().getStringByServerEnv(pstrXPath);
            this.insIs = new FileInputStream(mstrFileName);
        }catch( Exception ex ){
            Log.error("From PropertyUtil : File Loading�� �����߻�");
        }
    }

    /**
     * 	Property ������ �о� �ش� Key�� �ش��ϴ� �� ��ȯ�Ѵ�.
     * @param 	pstrFileName : ���� ȭ���̸�
     * @param 	pstrKey : ã���� �ϴ� Key��
     * @return  	String : Key�� �ش��ϴ� Value
     * @throws 	Exception
     */
    public String getPropertyValue( String pstrKey ){
        Properties insPt = new Properties();
        String rstrPropertyValue = "NoValue.TryAgain";
        try{
            insPt.load(insIs);
            rstrPropertyValue = insPt.getProperty(pstrKey);
        }catch(Exception e){
        	Log.error("From PropertyUtil : getPropertyValue()������ �����߻�");
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
        	Log.error("getPropertyValue()������ �����߻�");
        }
        return rstrPropertyValue;
    }

    /**
     * Property ������ �о� �ش� Stream��ü�� ��ȯ�Ѵ�.
     * @param 	pstrFileName : ���� ȭ���̸�
     * @param 	pstrKey : ã���� �ϴ� Key��
     * @return  	String : Key�� �ش��ϴ� Value
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
     * *.dat�� ������ ���پ� �о� �� �ٿ� �ش��ϴ� ������ �����ϸ� true�� ��ȯ
     * ��ȿ IP����Ʈ�� �������µ� �����.
     * @param 	pstrFileName : ���� ȭ���̸�
     * @param 	pstrKey : ã���� �ϴ� Key��
     * @return		String : Key�� �ش��ϴ� Value
     * @throws 	Exception
     */
    public boolean hasItemInContentsArray(String pstrItem) {
        boolean rbHaveItem = false; //return�Ұ����.
        int miIpCnt = 0; //���� IP������ ������ ����
        String[] minsArr = new String[10];//�ְ����� ����ó�� 10���� ����
        BufferedReader insBr = null;

        try {
            insBr = new BufferedReader(new InputStreamReader(insIs));
            for ( miIpCnt = 0; (minsArr[miIpCnt] = insBr.readLine()) != null; miIpCnt++ );
            	insBr.close();

            for ( int i = 0; i < miIpCnt; i++ ) { //���������� �ִٸ� true��ȯ.
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
