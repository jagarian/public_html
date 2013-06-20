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
 			
 			��뿹��)
 			
      while ( rtnUri.getUri().charAt(0) == '!' ) {
				System.out.println("@�ٽ� job.xml ���� command �� ���� ��Ų��.(Multipart case) :: "+rtnUri.getUri().substring(1));
				smb = servletModel.lookupCommand(rtnUri.getUri().substring(1)); 
				cmd = (AbstractAction)Class.forName(smb.getJobClass()).newInstance();
	            rtnUri = cmd.execute(req, res, smb.getJobKeyNames(), smb.getJobParameters(), rtnUri.getCargo());
	        }
			
			while ( rtnUri.getUri().charAt(0) == '@' ) {
				System.out.println("@�ٽ� job.xml ���� command �� ���� ��Ų��.(normal case) :: "+rtnUri.getUri().substring(1));
				smb = servletModel.lookupCommand(rtnUri.getUri().substring(1)); 
				cmd = (AbstractAction)Class.forName(smb.getJobClass()).newInstance();
	            rtnUri = cmd.execute(req, res, smb.getJobKeyNames(), smb.getJobParameters(), null);
	        }
	        
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
public class ServletMappingModel {
	private static ServletMappingModel minsJobIns;			//Singleton�� ���� �ڽ��� ���� �ִ´�.
	private static HashMap minsCommands = new HashMap();
	public static final String JOB 			= "job";		//�۾� ������ �������´�.
	public static final String NAME 			= "job-name";	//����ڰ� �۾��� ȣ���ϴ� �۾� ��(�۾��� �����ϴ� Class��� ���� �Ѵ�.)
	public static final String CLASS 			= "job-class";	//�ν��Ͻ��� ���� Class
	public static final String PARAMETER 		= "parameter";	//Class Constructor�� Parameter�� �����Ѵ�.
	public static final String KEY 			= "key"; 		//Class Constructor�� Parameter�� ���� �����Ѵ�(���α׷����� �����ϴ� ���� �ƴϰ� ������ ���� ���а��̴�.)
	public static final String VALUE 			= "value"; 		//Class Constructor�� Parameter�� �ȴ�.
	
	private ServletMappingModel() {	
		minsCommands = null;
		refresh();
	}
	
	/**
	 * @title	ServletMappingModel object�� �������� �ʴ� ��쿡�� ServletMappingModel instance�� �����Ͽ� return�Ѵ�.
	 * 				�׷��Ƿ� �ý��ۿ��� �� �ϳ��� instance�� �����Ͽ� Singleton���� ���۵ȴ�.
	 */
	public synchronized static ServletMappingModel getInstance() {
		if (minsJobIns == null) {
			minsJobIns = new ServletMappingModel();
		}
		return minsJobIns;
	}
	
	/**
	 * @title		DOM���� JobU �� ArrayList�� ����� �Ѱ��ش�.
	 * 
	 * @param	pRoot : Parsing�� DOM
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
	 * @title		���� DOM�� Key ���� �����ͼ� ArrayList�� �Ѱ��ش�.
	 * 
	 * @param	pNode : Node
	 * @return	java.util.ArrayList (Ű ����Ʈ)
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
	 * @title		���� DOM�� Parameter ���� �����ͼ� ArrayList�� �Ѱ��ش�.
	 * 
	 * @param	pNode : Node
	 * @return	java.util.ArrayList (value ����Ʈ)
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
	 * @title		XML�� Parsing�ϱ� ���Ͽ� ȣ���ϴ� Method.
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
