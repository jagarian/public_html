package neo.page;

import java.util.HashMap;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	TemplateUtility.java
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
public class TemplateUtility {

	private AbstractTemplate template;

	private HashMap context = null;

	public TemplateUtility() {
		super();
	}

	public TemplateUtility(HashMap ctx) {
		super();
		this.context = ctx;
	}

	public HashMap getContext() {
		return context;
	}

	public HtmlTemplate getTemplate() {
		return template;
	}

	public String finishTemplate() {
		String rtnValue = "";

		if (this.template == null)
			Log.info("fail in TemplateWrapper : must call setTemplate() before postRender() call", this);
		else {
			if (this.template.getContext() == null)
				this.template.setContext(this.getContext());
			rtnValue = this.template.postRender();
		}
		return rtnValue;
	}

	public String startTemplate() {
		String rtnValue = "";

		if (this.template == null)
			Log.info("fail in TemplateWrapper : must call setTemplate() before pretRender() call", this);
		else {
			if (this.template.getContext() == null)
				this.template.setContext(this.getContext());
			rtnValue = this.template.preRender();
		}

		return rtnValue;

	}

	public void putToContext(String name, String value) {
		if (context == null)
			context = new HashMap();
		this.context.put(name, value);
	}

	public void setTemplate(String templateConf) {

		String templateClassName = "";
		Object templateObject = null;

		try {
			Config conf = Config.getInstance();
			templateClassName = conf.get(templateConf);
			ClassLoader classLoader =	Thread.currentThread().getContextClassLoader();
			templateObject = classLoader.loadClass(templateClassName).newInstance();

		} catch (ConfigException le) {
			Log.error("fail in AbstactTableRenderer : Fail to get template class name from Configuration ["+ templateConf+ "] : "+ le.getMessage(), this);
		} catch (ClassNotFoundException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Not Found  ["+ templateClassName+ "] : "+ e.getMessage(), this);
		} catch (InstantiationException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Could not Instantiation ["+ templateClassName+ "] : "+ e.getMessage(), this);
		} catch (IllegalAccessException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Illegaliy Access ["+ templateClassName+ "] : "+ e.getMessage(), this);
		}
		this.template = (AbstractTemplate) templateObject;
		return;
	}
}
