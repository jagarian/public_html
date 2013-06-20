package neo.page;

import java.util.HashMap;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.page.AbstractRenderer;
import neo.page.Renderable;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	AbstractTableRenderer.java
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
public abstract class AbstractTableRenderer
	extends AbstractRenderer
	implements TemplateUsable, Renderable {

	public AbstractTableRenderer() {
		super();
	}

	public AbstractTableRenderer(HashMap ctx) {
		super(ctx);
	}

	protected abstract String doRender();

	public AbstractTemplate getTemplate(String templateConf) {

		String templateClassName = "";
		Object templateObject = null;

		try {
			Config conf = Config.getInstance();
			
			templateClassName = conf.get(templateConf);
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			
			templateObject = classLoader.loadClass(templateClassName).newInstance();

		} catch (ConfigException le) {
			Log.error("fail in AbstactTableRenderer : Fail to get template class name from Configuration [" + templateConf + "] : " + le.getMessage(), this);
		} catch (ClassNotFoundException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Not Found  [" + templateClassName + "] : " + e.getMessage(), this);
		} catch (InstantiationException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Could not Instantiation [" + templateClassName + "] : "	+ e.getMessage(), this);
		} catch (IllegalAccessException e) {
			Log.error("fail in AbstactTableRenderer : Template Class Illegaliy Access [" + templateClassName + "] : " + e.getMessage(), this);
		}

		return (AbstractTemplate) templateObject;
	}

	public abstract void initContext();

	public String toHtml() {
		return this.doRender();
	}

	public String toHtml(String templateConf) {
		AbstractTemplate tableTemplate = null;
		tableTemplate = this.getTemplate(templateConf);

		if (tableTemplate == null)
			return this.toHtml();

		this.initContext();

		if (this.getContext() != null)
			tableTemplate.setContext(this.getContext());

		return tableTemplate.toHtml(this.toHtml());
	}
}
