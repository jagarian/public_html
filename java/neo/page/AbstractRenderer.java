package neo.page;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 	@Class Name	: 	AbstractRenderer.java
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
public abstract class AbstractRenderer {

	private HashMap context = null;

	public AbstractRenderer() {
		super();
	}

	public AbstractRenderer(HashMap ctx) {
		super();
		this.context = ctx;
	}

	public HashMap getContext() {
		return context;
	}

	public String getFromContext(String name) {
		return this.getContext() == null	? "" : (String) this.getContext().get((Object) name);
	}

	public void putToContext(String name) {

		if (this.getContext() == null)
			this.setContext(new HashMap());

		this.getContext().put((Object) name, null);

		return;

	}

	public void putToContext(String name, String value) {

		if (this.getContext() == null)
			this.setContext(new HashMap());

		this.getContext().put((Object) name, value);

		return;

	}

	public void setContext(HashMap newCtx) {
		context = newCtx;
	}

	public String getContextHtml() {
		StringBuffer tagBuffer = new StringBuffer();

		Set set = this.getContext().keySet();
		Iterator e = set.iterator();

		while (e.hasNext()) {
			String key = (String) e.next();
			String value = (String) this.getContext().get(key);
			tagBuffer.append(" ").append(key);
			if (value != null)
				tagBuffer.append("=\"").append(value).append("\" ");
		}

		return tagBuffer.toString();
	}
}
