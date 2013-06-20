package neo.config;

import javax.xml.transform.TransformerException;

import neo.exception.ConfigException;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 	@Class Name	: 	XmlPathUtils.java
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
public class XmlPathUtils extends XmlUtils {

	protected XObject eval(Document doc, String str)
		throws TransformerException {
		Node root = doc.getDocumentElement();
		return XPathAPI.eval(root, str);
	}

	public String evalToString(Document doc, String str)
		throws ConfigException {
		try {
			return eval(doc, str).str();
		} catch (TransformerException e) {
			throw new ConfigException("evalToString() Error on Cannot get a data for " + str, e);
		}
	}

	public NodeList evalToNodeList(Document doc, String str)
		throws ConfigException {
		try {
			return (NodeList) eval(doc, str).nodelist();
		} catch (TransformerException e) {
			throw new ConfigException("evalToNodeList() Error on Cannot get a data for " + str, e);
		}
	}

	public Node selectSingleNode(Document doc, String str)
		throws ConfigException {
		try {
			return XPathAPI.selectSingleNode(doc, str);
		} catch (TransformerException e) {
			throw new ConfigException("selectSingleNode() Error on Cannot get a data for " + str, e);
		}
	}
}
