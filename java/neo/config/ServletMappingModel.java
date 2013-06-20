package neo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.*;

import neo.util.comm.StringUtil;
import neo.util.file.FileUtil;
import neo.util.log.Log;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 	@Class Name	: 	ServletMappingModel.java
 			
 			사용예제)
 			
      while ( rtnUri.getUri().charAt(0) == '!' ) {
				System.out.println("@다시 job.xml 에서 command 를 가동 시킨다.(Multipart case) :: "+rtnUri.getUri().substring(1));
				smb = servletModel.lookupCommand(rtnUri.getUri().substring(1)); 
				cmd = (AbstractAction)Class.forName(smb.getJobClass()).newInstance();
	            rtnUri = cmd.execute(req, res, smb.getJobKeyNames(), smb.getJobParameters(), rtnUri.getCargo());
	        }
			
			while ( rtnUri.getUri().charAt(0) == '@' ) {
				System.out.println("@다시 job.xml 에서 command 를 가동 시킨다.(normal case) :: "+rtnUri.getUri().substring(1));
				smb = servletModel.lookupCommand(rtnUri.getUri().substring(1)); 
				cmd = (AbstractAction)Class.forName(smb.getJobClass()).newInstance();
	            rtnUri = cmd.execute(req, res, smb.getJobKeyNames(), smb.getJobParameters(), null);
	        }
	        
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
public class ServletMappingModel {
	private static ServletMappingModel minsJobIns;			//Singleton을 위해 자신을 갖고 있는다.
	private static HashMap minsCommands = new HashMap();
	public static final String JOB 			= "job";		//작업 단위를 구분짓는다.
	public static final String NAME 			= "job-name";	//사용자가 작업을 호출하는 작업 명(작업을 수행하는 Class명과 같게 한다.)
	public static final String CLASS 			= "job-class";	//인스턴스를 만들 Class
	public static final String PARAMETER 		= "parameter";	//Class Constructor의 Parameter를 지정한다.
	public static final String KEY 			= "key"; 		//Class Constructor의 Parameter의 값을 구분한다(프로그램에서 구분하는 것이 아니고 관리를 위한 구분값이다.)
	public static final String VALUE 			= "value"; 		//Class Constructor의 Parameter가 된다.
	
	private ServletMappingModel() {	
		minsCommands = null;
		refresh();
	}
	
	/**
	 * @title	ServletMappingModel object가 존재하지 않는 경우에만 ServletMappingModel instance를 생성하여 return한다.
	 * 				그러므로 시스템에는 단 하나의 instance만 존재하여 Singleton으로 동작된다.
	 */
	public synchronized static ServletMappingModel getInstance() {
		if (minsJobIns == null) {
			minsJobIns = new ServletMappingModel();
		}
		return minsJobIns;
	}
	
	/**
	 * @title		DOM에서 JobU 의 ArrayList를 만들어 넘겨준다.
	 * 
	 * @param	pRoot : Parsing된 DOM
	 * @return	java.util.ArrayList
	 */
	private ArrayList getJobs(Element pinsRoot) {
		ArrayList rinsJobs = new ArrayList();
		HashMap insHm = new HashMap();
		NodeList list = pinsRoot.getElementsByTagName(JOB);
		for (int loop = 0; loop < list.getLength(); loop++) {

			Node node = list.item(loop);
			if (node != null) {
				String JOBName = getSubTagValue(node, NAME);
				String JOBClass = getSubTagValue(node, CLASS);
				ArrayList kl = getKeys(node);
				ArrayList al = getParameters(node);

				if (!insHm.containsKey(JOBName)) {
					ServletMappingBean svlt = new ServletMappingBean(JOBName, JOBClass, kl, al);
					insHm.put(JOBName, "");
					rinsJobs.add(svlt);
				} else {
					Log.warn("getJobs() "	+ "Job Name Defined more than once : " + JOBName);
				}
			}
		}
		return rinsJobs;
	}

	/**
	 * @title		하위 DOM의 Key 값을 가져와서 ArrayList로 넘겨준다.
	 * 
	 * @param	pNode : Node
	 * @return	java.util.ArrayList (키 리스트)
	 */
	private ArrayList getKeys(Node pinsNode) {
		ArrayList rinsString = new ArrayList();
		HashMap insHm = new HashMap();
		if (pinsNode != null) {
			NodeList children = pinsNode.getChildNodes();
			for (int innerLoop = 0;
				innerLoop < children.getLength();
				innerLoop++) {
				Node child = children.item(innerLoop);
				if ((child != null)
					&& (child.getNodeName() != null)
					&& child.getNodeName().equals(PARAMETER)) {
					if (child instanceof Element) {
						Element childElement = ((Element) child);
						String key = childElement.getAttribute(KEY);
						String value = childElement.getAttribute(VALUE);
						if (value.charAt(0) == '$') {
							value = value.substring(1);
						} else if (	value.charAt(0) == '@'	|| 
										value.charAt(0) == '!'	|| 
										value.indexOf("/") > -1 || 
										value.indexOf(".") > -1) {
							//value = value;
						} else {
							value = WebUtil.makeServletPath(value);
						}
						if (!insHm.containsKey(key)) {
							rinsString.add(key);
							insHm.put(key, "");
						} else {
							Log.warn("getParameters() " + key + " is defined more than once");
						}
					}
				}
			}
		}
		return rinsString;
	}
	
	/**
	 * @title		하위 DOM의 Parameter 값을 가져와서 ArrayList로 넘겨준다.
	 * 
	 * @param	pNode : Node
	 * @return	java.util.ArrayList (value 리스트)
	 */
	private ArrayList getParameters(Node pinsNode) {
		ArrayList rinsString = new ArrayList();
		HashMap insHm = new HashMap();
		if (pinsNode != null) {
			NodeList children = pinsNode.getChildNodes();
			for (int innerLoop = 0;
				innerLoop < children.getLength();
				innerLoop++) {
				Node child = children.item(innerLoop);
				if ((child != null)
					&& (child.getNodeName() != null)
					&& child.getNodeName().equals(PARAMETER)) {
					if (child instanceof Element) {
						Element childElement = ((Element) child);
						String key = childElement.getAttribute(KEY);
						String value = childElement.getAttribute(VALUE);
						if (value.charAt(0) == '$') {
							value = value.substring(1);
						} else if (	value.charAt(0) == '@'	|| 
										value.charAt(0) == '!'	|| 
										value.indexOf("/") > -1 || 
										value.indexOf(".") > -1) {
							//value = value;
						} else {
							value = WebUtil.makeServletPath(value);
						}
						if (!insHm.containsKey(key)) {
							rinsString.add(value);
							insHm.put(key, "");
						} else {
							Log.warn("getParameters() " + key + " is defined more than once");
						}
					}
				}
			}
		}
		return rinsString;
	}

	public String getSubTagValue(Node pinsNode, String pinsSubtagname) {
		String rstrString = "";
		if (pinsNode != null) {
			NodeList children = pinsNode.getChildNodes();
			for (int innerLoop = 0;
				innerLoop < children.getLength();
				innerLoop++) {
				Node child = children.item(innerLoop);
				if ((child != null) && 
					(child.getNodeName() != null) && 
					child.getNodeName().equals(pinsSubtagname)) {
					Node grandChild = child.getFirstChild();
					if (grandChild.getNodeValue() != null) {
						return grandChild.getNodeValue();
					}
				}
			}
		}
		return rstrString;
	}

	public Element loadDocument(String pinsLocation) {
		Document insDoc = null;
		Element rinsRoot = null;
		try {		
			
			URL url = new URL(pinsLocation);
			HttpURLConnection con = (HttpURLConnection)(url.openConnection());
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			InputStream in = con.getInputStream();
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			parser.setErrorHandler(new org.xml.sax.ErrorHandler() {
		        //Ignore the fatal errors
		        public void fatalError(SAXParseException exception)throws SAXException { }
		        //Validation errors 
		        public void error(SAXParseException e)throws SAXParseException {
		          System.out.println("Error at " +e.getLineNumber() + " line.");
		          System.out.println(e.getMessage());
		        }
		        //Show warnings
		        public void warning(SAXParseException err)throws SAXParseException{
		          System.out.println(err.getMessage());
		        }
		      });
			//System.out.println(FileUtil.readFromFile(pinsLocation));
			//insDoc = parser.parse(pinsLocation);
			insDoc = parser.parse(in);			
			rinsRoot = insDoc.getDocumentElement();													
			rinsRoot.normalize(); 		
		} catch (SAXParseException ex) {
			System.err.println("+================================+");
			System.err.println("|       *SAX Parse Error*        |");
			System.err.println("+================================+");
			System.err.println(ex.toString());
			System.err.println("At line " + ex.getLineNumber());
			System.err.println("+================================+");
		} catch (SAXException ex) { 
			System.err.println("+================================+");
			System.err.println("|          *SAX Error*           |");
			System.err.println("+================================+"); 
			System.err.println(ex.toString());
			System.err.println("+================================+"); 
		} catch (ParserConfigurationException ex) {
			System.err.println("ParserConfigurationException :: "+ex.getMessage()); 
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("+================================+");
			System.err.println("|           *XML Error*          |");
			System.err.println("+================================+");
			System.err.println(e.toString());
		}
		return rinsRoot;
	}

	public ArrayList loadServletDefinitions(String pinsLocation) {
		Element insRoot = loadDocument(pinsLocation);
		return getJobs(insRoot);
	}
	
	public void servletReload(ArrayList insJobList) {		
		for ( int i=0 ; i<insJobList.size() ; i++ ) {            		
    		try {
    			minsCommands.put( ((ServletMappingBean)insJobList.get(i)).getJobName(), (ServletMappingBean) insJobList.get(i) );
    		} catch ( Exception ex ) {
    			Log.error("Class Loading Error!  Name :: " +  ( (ServletMappingBean) insJobList.get(i) ).getJobName() + " :: "+ex.getMessage());
    		}
    	}
	}
	
	public ServletMappingBean lookupCommand(String pstrCmd){
		if (minsCommands.containsKey(pstrCmd)) {
            return (ServletMappingBean)minsCommands.get(pstrCmd);
        } else {
            return (ServletMappingBean)minsCommands.get("nocmd");
        }
    }
	
	/**
	 * @title		XML을 Parsing하기 위하여 호출하는 Method.
	 * 
	 * @param	
	 * @return	void
	 */
	public void refresh() {
		try {
			System.out.println("ServletMappingModel start...");
			
			minsCommands = new HashMap(); 
			ArrayList lists = null; 
				
			Config insConf = Config.getInstance();
			
			String pLocation = insConf.getStringByServerEnv("/config/neo/mapping/job/job-file");	
			System.out.println("pLocation :: "+pLocation);
			
			String[] pLocationArr = StringUtil.split(pLocation, ",", true);
			
			for (int i=0; i<pLocationArr.length; i++) {
				System.out.println("pos["+i+"] :: "+pLocationArr[i].trim());
				lists = loadServletDefinitions(pLocationArr[i].trim());			
				servletReload(lists); 
			}		
			
			System.out.println("ServletMappingModel end...");
			
		} catch (Exception e) {
			Log.error("refresh() " + "job.xml File Loading Error! " + e.getMessage());
		}
	}
}
