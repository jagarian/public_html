package neo.page;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	AccumulationHashMap.java
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
final class AccumulationHashMap {
	
	private Map paramNameArrayCache; // paramNameArrayCache Cache
	
	private final static AccumulationHashMap singletonObj = new AccumulationHashMap();

	private AccumulationHashMap() {
		try {
			paramNameArrayCache = Collections.synchronizedMap(new HashMap());
		} catch (Exception e) {
			Log.info(e.getMessage());
		}
	}

	public static AccumulationHashMap getInstance() {
		return singletonObj;
	}

	public Map getParamNameArrayCache() {
		return paramNameArrayCache;
	}

	public void reset() {
		if (paramNameArrayCache != null && paramNameArrayCache.size() > 0)
			paramNameArrayCache.clear();
	}
}