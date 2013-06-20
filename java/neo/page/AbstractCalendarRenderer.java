package neo.page;

import java.util.Calendar;
import java.util.HashMap;

import neo.config.Config;
import neo.exception.ConfigException;
import neo.util.log.Log;

/**
 * 	@Class Name	: 	AbstractCalendarRenderer.java
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
public abstract class AbstractCalendarRenderer
	extends AbstractRenderer
	implements TemplateUsable, Renderable {

	protected Calendar nowDate = null;
	protected Calendar startDate = null;
	protected Calendar endDate = null;

	public AbstractCalendarRenderer() {
		super();
		this.nowDate = Calendar.getInstance();
		this.startDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();

		this.startDate.set(Calendar.DATE, 1);
		this.endDate.set(Calendar.DATE, 1);

		this.endDate.add(Calendar.MONTH, 1);
	}

	public AbstractCalendarRenderer(HashMap ctx) {
		super(ctx);
		this.nowDate = Calendar.getInstance();
		this.startDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();

		this.startDate.set(Calendar.DATE, 1);
		this.endDate.set(Calendar.DATE, 1);
		this.endDate.add(Calendar.MONTH, 1);
	}

	public AbstractCalendarRenderer(String theDate) {
		super();
		int year = 1990, month = 1, day = 1;
		this.nowDate = Calendar.getInstance();
		this.startDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();

		if (theDate.length() == 6) {
			year = Integer.parseInt(theDate.substring(0, 4));
			month = Integer.parseInt(theDate.substring(4, 6));
			day = 1;

		} else if (theDate.length() == 8) {
			year = Integer.parseInt(theDate.substring(0, 4));
			month = Integer.parseInt(theDate.substring(4, 6));
			day = Integer.parseInt(theDate.substring(6, 8));

		} else {
			Log.info("fail in AbstactCalendarRenderer : Fail to init Calendar Construct String Length is not 6 or 8 [" + this.getClass().getName() + "] : ", this);
		}

		this.startDate.set(year, month - 1, day);
		this.endDate.set(year, month - 1, day);
		this.endDate.add(Calendar.MONTH, 1);
	}

	public AbstractCalendarRenderer(Calendar theDate) {
		super();
		this.nowDate = Calendar.getInstance();
		this.startDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();

		this.startDate.set(theDate.get(Calendar.YEAR),
							theDate.get(Calendar.MONTH),
							theDate.get(Calendar.DATE));

		this.endDate.set(theDate.get(Calendar.YEAR),
						theDate.get(Calendar.MONTH),
						theDate.get(Calendar.DATE));
		this.endDate.add(Calendar.MONTH, 1);
	}

	public AbstractCalendarRenderer(Calendar startDate, Calendar endDate) {
		super();
		this.nowDate = Calendar.getInstance();
		this.startDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();

		this.startDate.set(startDate.get(Calendar.YEAR),
							startDate.get(Calendar.MONTH),
							startDate.get(Calendar.DATE));
		this.endDate.set(
			endDate.get(Calendar.YEAR),
			endDate.get(Calendar.MONTH),
			endDate.get(Calendar.DATE));
	}

	protected String doRender() {

		StringBuffer tagBuffer = new StringBuffer();

		int firstDateOfWeek = startDate.get(Calendar.DAY_OF_WEEK);
		Calendar cursorDate = Calendar.getInstance();
		cursorDate.set(this.startDate.get(Calendar.YEAR),
						this.startDate.get(Calendar.MONTH),
						this.startDate.get(Calendar.DATE));

		boolean flag = false;

		for (int idx = 0; idx < 8; idx++) {
			tagBuffer.append(renderWeek(cursorDate));
			for (int jdx = 1; jdx < 8; jdx++) {
				if (firstDateOfWeek == jdx)
					flag = true;
				if ((!flag) || (this.after(cursorDate, this.endDate)))
					tagBuffer.append(renderDay(cursorDate, false));
				else {
					tagBuffer.append(renderDay(cursorDate, true));
					cursorDate.add(Calendar.DATE, 1);
				}
			}

			tagBuffer.append("</TR>\n");
			if (this.after(cursorDate, this.endDate))
				break;
		}

		return tagBuffer.toString();

	}

	public String getDateString() {

		StringBuffer tagBuffer = new StringBuffer();

		tagBuffer
			.append(nowDate.get(Calendar.YEAR))
			.append("년 ")
			.append(nowDate.get(Calendar.MONTH) + 1)
			.append("월")
			.append(nowDate.get(Calendar.DATE))
			.append("일 ")
			.append(nowDate.get(Calendar.HOUR_OF_DAY))
			.append("시 ")
			.append(nowDate.get(Calendar.MINUTE))
			.append("분 ")
			.append(nowDate.get(Calendar.SECOND))
			.append("초 ");
		return tagBuffer.toString();

	}

	public String getDateString(Calendar cal) {

		StringBuffer tagBuffer = new StringBuffer();

		tagBuffer
			.append(cal.get(Calendar.YEAR))
			.append("년 ")
			.append(cal.get(Calendar.MONTH) + 1)
			.append("월")
			.append(cal.get(Calendar.DATE))
			.append("일 ")
			.append(cal.get(Calendar.HOUR_OF_DAY))
			.append("시 ")
			.append(cal.get(Calendar.MINUTE))
			.append("분 ")
			.append(cal.get(Calendar.SECOND))
			.append("초 ");
		return tagBuffer.toString();

	}

	protected int getLastDayOfMonth(int year, int month) {
		int daysOfMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		//leap year - mod by 4 or 400, not 100  
		if (year % 4 == 0)
			daysOfMonth[1] = 29;
		if (year % 100 == 0)
			daysOfMonth[1] = 28;
		if (year % 400 == 0)
			daysOfMonth[1] = 29;

		return daysOfMonth[month];
	}

	protected int getLastDayOfMonth(Calendar cal) {
		int daysOfMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		//leap year - mod by 4 or 400, not 100
		if (year % 4 == 0)
			daysOfMonth[1] = 29;
		if (year % 100 == 0)
			daysOfMonth[1] = 28;
		if (year % 400 == 0)
			daysOfMonth[1] = 29;

		return daysOfMonth[month];
	}

	public int getMonth() {
		return startDate.get(Calendar.MONTH) + 1;
	}

	public AbstractTemplate getTemplate(String templateConf) {

		String templateClassName = "";
		Object templateObject = null;

		try {
			Config conf = Config.getInstance();
			
			templateClassName = conf.get(templateConf);

			ClassLoader classLoader =	Thread.currentThread().getContextClassLoader();
			
			templateObject = classLoader.loadClass(templateClassName).newInstance();

		} catch (ConfigException le) {
			Log.error("fail in AbstactCalendarRenderer : Fail to get template class name from Configuration ["	+ templateConf + "] : "	+ le, this);
		} catch (ClassNotFoundException e) {
			Log.error("fail in AbstactCalendarRenderer : Template Class Not Found [" + templateClassName + "] : "	+ e, this);
		} catch (InstantiationException e) {
			Log.error("fail in AbstactCalendarRenderer : Template Class Could not Instantiation [" + templateClassName + "] : " + e, this);
		} catch (IllegalAccessException e) {
			Log.error("fail in AbstactCalendarRenderer : Template Class Illegaliy Access [" + templateClassName + "] : " + e, this);
		}
		return (AbstractTemplate) templateObject;
	}

	public int getYear() {
		return startDate.get(Calendar.YEAR);
	}

	public abstract void initContext();

	protected abstract String renderDay(Calendar cal, boolean flag);

	protected abstract String renderWeek(Calendar cal);

	public String toHtml() {
		return doRender();
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

	public boolean after(Calendar thedate, Calendar after) {
		boolean flag = true;

		int the_date = thedate.get(Calendar.YEAR) * 10000
						+ thedate.get(Calendar.MONTH) * 100
						+ thedate.get(Calendar.DATE);

		int after_date = after.get(Calendar.YEAR) * 10000
						+ after.get(Calendar.MONTH) * 100
						+ after.get(Calendar.DATE);
											
		if (the_date < after_date)
			flag = false;

		return flag;
	}
}
