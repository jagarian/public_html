package neo.page;

import java.util.HashMap;

/**
 * 	@Class Name	: 	AbstractTemplate.java
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
public abstract class AbstractTemplate extends AbstractRenderer implements HtmlTemplate {

	public AbstractTemplate() {
		super();
	}

	public AbstractTemplate(HashMap ctx) {
		super(ctx);
	}

	public abstract String postRender();

	public abstract String preRender();

	public String toHtml(String bodyTag) {
		return (preRender() + bodyTag + postRender());
	}
}
