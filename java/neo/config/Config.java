package neo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Observable;


import neo.util.comm.ShowDomTree;
import neo.util.log.Log;
import neo.exception.ConfigException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 	@Class Name	: 	Config.java
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
public class Config extends Observable {

	private XmlUtils utils;
	private Document doc;
	private static Config xc;
	private static String neo_home = null;
	private static String NEO_HOME_TAG = "#home";
	private static int NEO_HOME_TAG__SIZE = NEO_HOME_TAG.length();
	private final static String default_file_name = "neo.xml";
	private static LinkedHashMap configurationMap = new LinkedHashMap();
	private static char DELIM = '/';
	private static String strEnvLayer;
		
	private Config(String name, boolean validating)
		throws ConfigException {
		try {
			validating = false;
			doc = parsing(name, validating);
			init(doc);
			
			diamond();
			System.getProperties().list(System.out);
			
		} catch (Exception e) {
			System.out.println("Config() Error on parsing : Check the neo.conf");
			throw new ConfigException("Config() Error on parsing : Check the neo.xml",e);
		}
	}
	
	public static synchronized Config getInstance()
		throws ConfigException {
		try {
			if (xc == null) {
				xc =	new Config(getConfFileLocation(default_file_name), false);
				strEnvLayer = xc.get("/config/neo/env-select/value");								
			}
			return xc;
		} catch (ConfigException e) {
			throw new ConfigException("getInstance() Error on loading Configuration Instance",e);
		}		
	}

	public static String getConfFileLocation(String filename)
		throws ConfigException {
		try {
			neo_home = System.getProperty("neo.home");			
			
			File default_file = new File(neo_home + "/conf", filename);			
			
			if  (!default_file.exists() ) {
				throw new Exception("File not Exists, Check the Config XML file");
			}
			
			return default_file.getAbsolutePath();
			
		} catch (Exception e) {
			throw new ConfigException("Check the neo.xml file location",e);
		}
	}

	public static String getNeoHome() {
		return System.getProperty("neo.home");
	}	

	private void init(Document doc) throws ConfigException {

		this.utils = XmlUtils.getImpl();
		this.doc = doc;
		
		configurationMap = null;
		configurationMap = new LinkedHashMap();
		
		Node root = doc.getDocumentElement();

		if (root == null) {
			throw new ConfigException("init() Error on initiating DOM : Check the root element of neo.xml document");

		} else {
			createConfigurationMap(root);
			NodeList children = root.getChildNodes();
			String rootNodeNm = root.getNodeName();

			for (int inx = 0; inx < children.getLength(); inx++) {
				Node childNode = children.item(inx);

				if (!childNode.getNodeName().equals("neo") && childNode.getNodeType() == Node.ELEMENT_NODE) {

					String childNodeName = childNode.getNodeName();
					String childNodeAttrValue =	childNode.getAttributes().item(0).getNodeValue();
					String fullPath =	rootNodeNm
											+ DELIM
											+ childNodeName
											+ "<"
											+ childNodeAttrValue
											+ ">"
											+ DELIM
											+ "src";
					String srcPath = get(fullPath);
					fullPath =	rootNodeNm
									+ DELIM
									+ childNodeName
									+ "<"
									+ childNodeAttrValue
									+ ">"
									+ DELIM
									+ "validate";
					String validate = get(fullPath);

					boolean validateBool = new Boolean(validate).booleanValue();
					Document subDoc = parsing(srcPath, validateBool);

					createConfigurationMap(	subDoc.getDocumentElement(),
											rootNodeNm
											+ DELIM
											+ childNodeName
											+ "<"
											+ childNodeAttrValue
											+ ">");

					NodeList nlst = subDoc.getDocumentElement().getChildNodes();
					int len = nlst.getLength();
					Node nd = null;
					for (int k = 0; k < len; k++) {
						nd = doc.importNode(nlst.item(k), true);
						children.item(inx).appendChild(nd);
					}
				}
			}
		}
	}

	private static void createConfigurationMap(Node node)
		throws ConfigException {
		createConfigurationMap(node, node.getNodeName());
	}

	private static void createConfigurationMap(Node node, String header)
		throws ConfigException {
		String tempLogStr = "";
		try {
			short NODE_TYPE = node.getNodeType();
			if (NODE_TYPE == Node.ELEMENT_NODE) {
				NodeList children = node.getChildNodes();
				if (children != null) { // 그 수 만큼 재귀호출
					for (int i = 0, size = children.getLength();
						i < size;
						i++) {
						// element인 경우 attribute에 대한 처리를 해주어야 한다.
						Node childNode = children.item(i);
						String childNodeName = childNode.getNodeName();
						short CHILD_NODE_TYPE = childNode.getNodeType();
						String childNodeValue = childNode.getNodeValue();
						NamedNodeMap attrNodeList = childNode.getAttributes();
						
						if (attrNodeList != null
							&& attrNodeList.getLength() > 0) {
							for (int attrListInx = 0,
								sizeInx = attrNodeList.getLength();
								attrListInx < sizeInx;
								attrListInx++) {								
								createConfigurationMap(	childNode,
														header
														+ DELIM
														+ childNodeName
														+ "<"
														+ attrNodeList.item(attrListInx).getNodeValue()
														+ ">");
							}
						} else {
							if (CHILD_NODE_TYPE == Node.TEXT_NODE && compareWhiteSpace(childNodeValue)) {
								createConfigurationMap(childNode, header);
							} else {
								createConfigurationMap(	childNode, header + DELIM + childNodeName);
							}
						}
					}
				}
			} else if (NODE_TYPE == Node.TEXT_NODE) {
				String mapValue = node.getNodeValue();
				if (mapValue != null && compareWhiteSpace(mapValue)) {
					mapValue = mapValue.trim();
					String mapKey = header;
					if (mapValue.startsWith(NEO_HOME_TAG)) {
						mapValue = neo_home + mapValue.substring(NEO_HOME_TAG__SIZE);
					}
					node.setNodeValue(mapValue);
					configurationMap.put(mapKey, mapValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigException("createConfigurationMap() Error on generating configurationMap",e);
		}
	}

	private static boolean compareWhiteSpace(String str) {

		char[] charStr = str.toCharArray();

		for (int i = 0, size = charStr.length; i < size; i++) {
			if (!Character.isWhitespace(charStr[i]))
				return true;
		}
		return false;

	}

	private Document parsing(String name, boolean validating)
		throws ConfigException {
		try {
			Document temp;
			File conf_file = new File(name);
			FileInputStream in = new FileInputStream(conf_file);
			if (conf_file == null || 
				!conf_file.exists() || 
				!conf_file.canRead()) {
				throw new ConfigException(new IOException("Failed open the [" + name + "] File : "));
			}
			try {
				this.utils = XmlUtils.getImpl();
				temp = XmlUtils.parse(new java.io.BufferedInputStream(in),validating);
				return temp;
			} catch (ConfigException e) {
				Log.info("parsing()" + e.getMessage());
				throw new ConfigException(e);
			} finally {
				if (in != null)
					in.close();
			}
		} catch (FileNotFoundException e) {
			throw new ConfigException("parsing", "Error on parsing ", e);
		} catch (IOException e) {
			throw new ConfigException("parsing","Error  on parsing ",e);
		}
	}

	public String get(String key) throws ConfigException {
		if (configurationMap == null) {
			throw new ConfigException("get() Error on loading configurationMap");
		}
		if (key.charAt(0) == DELIM) {
			key = key.substring(1);
		}
		String value = (String) configurationMap.get(key);
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

	public String getString(String elemPath, String defaultValue)
		throws ConfigException {
		try {
			String value = get(elemPath);
			if (value.equals("")) {
				return defaultValue;
			} else {
				return value;
			}
		} catch (Exception e) {
			throw new ConfigException("getString() Error on getter method of configurationMap",e);
		}
	}

	public boolean getBoolean(String elemPath, boolean defaultValue)
		throws ConfigException {
		try {
			String value = get(elemPath);
			String temp = value.toUpperCase();
			if ("TRUE".equals(temp) || "FALSE".equals(temp)) {
				return new Boolean(value.trim()).booleanValue();
			} else {
				return defaultValue;
			}
		} catch (Exception e) {
			throw new ConfigException("getBoolean() Error on getter method of configurationMap",e);
		}
	}

	public int getInt(String elemPath, int defaultValue)
		throws ConfigException {
		try {
			String value = get(elemPath);
			if (value.equals("")) {
				return defaultValue;
			} else {
				return new Integer(value.trim()).intValue();
			}
		} catch (Exception e) {
			throw new ConfigException("getInt() Error on getter method of configurationMap",e);
		}
	}

	public long getLong(String elemPath, long defaultValue)
		throws ConfigException {
		try {
			String value = get(elemPath);
			if (value.equals("")) {
				return defaultValue;
			} else {
				return new Long(value.trim()).longValue();
			}
		} catch (Exception e) {
			throw new ConfigException("getLong() Error on getter method of configurationMap",e);
		}
	}

	public Element getElement(String elemPath)
		throws ConfigException, IllegalArgumentException {
		try {
			NodeList list = utils.evalToNodeList(doc, elemPath);
			Node node = (list.getLength() > 0) ? list.item(0) : null;
			if (node instanceof Element)
				return (Element) node;
			else
				System.out.println("IllegalArguementException : " + elemPath + " isn't the path of an element");
			throw new ConfigException("IllegalArguementException : "	+ elemPath + " isn't the path of an element",	new IllegalArgumentException());
		} catch (Exception e) {
			throw new ConfigException("getElement() Error on getter method of DOM",e);
		}
	}

	public int getElementCount(String parentPath, String elemName)
		throws ConfigException {
		try {
			String path = parentPath + DELIM + elemName;
			NodeList list = utils.evalToNodeList(doc, path);
			return list.getLength();
		} catch (Exception e) {
			throw new ConfigException("getElementCount() Error on getter method of DOM",e);
		}
	}

	public Document getDom() {
		return doc;
	}

	public NodeList getNodeList(String elemPath)
		throws ConfigException {
		try {
			return utils.evalToNodeList(doc, elemPath);
		} catch (Exception e) {
			throw new ConfigException("getNodeList() Error on getter method of DOM",e);
		}
	}

	public Node getNode(String elemPath) throws ConfigException {
		try {
			return utils.selectSingleNode(doc, elemPath);
		} catch (Exception e) {
			throw new ConfigException("getNode() Error on getter method of DOM",e);
		}
	}

	public String getAttribute(String elemPath, String attrName)
		throws ConfigException {
		String path = elemPath + DELIM + '@' + attrName;
		try {
			return get(path);
		} catch (Exception e) {
			throw new ConfigException("getAttribute() Error on getter method of DOM",e);
		}
	}

	public String[] getArray(String arrayPath, 
										String arrName, 
										String elemName)
		throws ConfigException {
		try {
			int length = 0;
			length = getElementCount(arrayPath, arrName);
			String array[] = new String[length];
			for (int i = 0, j = 1; i < length; i++, j++) {
				String path = arrayPath
							+ DELIM
							+ arrName
							+ '['
							+ j
							+ ']'
							+ DELIM
							+ elemName;
				array[i] = get(path);
			}
			return array;
		} catch (Exception e) {
			throw new ConfigException("getArray() Error on getter method of DOM",e);
		}
	}

	public void printConfDom(String gb) {
		if ((gb.trim()).equals("file"))
			ShowDomTree.saveDocAsFile(doc, neo_home + "/conf/neo.xml");
		else {
			Log.info(ShowDomTree.returnDocAsString(doc));
		}
	}

	public String showConfDom() {
		return ShowDomTree.returnDocAsString(doc);
	}
	
	public String getSimple(String elemPath) throws ConfigException {
		try {
			Config conf = Config.getInstance();
			return conf.get(elemPath);
		} catch (Exception e) {
			throw new ConfigException("getSimple() Error", e);
		}
	}
	
	//문자
	public String getStringByServerEnv(String elemPath)
		throws ConfigException {
		try {
			Config conf = Config.getInstance();
			if (strEnvLayer != null && !strEnvLayer.equals(""))
				return conf.get(elemPath + "/" + strEnvLayer);
			return conf.get(elemPath);
		} catch (Exception e) {
			throw new ConfigException("getStringByServerEnv() Error", e);
		}
	}
	
	//정수형
	public int getIntByServerEnv(String elemPath) throws ConfigException {
		try {
			Config conf = Config.getInstance();
			if (strEnvLayer != null && !strEnvLayer.equals(""))
				return conf.getInt(elemPath + "/" + strEnvLayer, 0);
			return conf.getInt(elemPath,0);
		} catch (Exception e) {
			throw new ConfigException("getIntByServerEnv() Error", e);
		}
	}
	
	//블린형
	public boolean getBooleanByServerEnv(String elemPath) throws ConfigException {
		try {
			Config conf = Config.getInstance();
			if (strEnvLayer != null && !strEnvLayer.equals(""))
				return conf.getBoolean(elemPath + "/" + strEnvLayer, false);
			return conf.getBoolean(elemPath, false);
		} catch (Exception e) {
			throw new ConfigException("getBooleanByServerEnv() Error", e);
		}
	}
	
	public synchronized void refresh() throws ConfigException {
		try {
			doc = parsing(getConfFileLocation(default_file_name), false);
		} catch (Exception e) {
			throw new ConfigException("refresh() Error on refreshing the neo.xml DOM ",e);
		}
		init(doc);
		setChanged();
		notifyObservers();
	}
    
    public void diamond(){
    	int count=5;
		  
    	for(int i=1;i<count+1;i++){
    		for(int j=0;j<count-i;j++){
    			System.out.print(" ");
    		}		   
    		for(int j=0;j<2*i-1;j++){
    			System.out.print("*");
    		}
    		System.out.println();
		  }
		  
    	for(int i=1;i<count;i++){
    		for(int j=0;j<i;j++){
    			System.out.print(" ");
    		}		   
    		for(int j=0;j<2*(count-i)-1;j++){
    			System.out.print("*");
    		}
    		System.out.println();
    	}
    }	
}
