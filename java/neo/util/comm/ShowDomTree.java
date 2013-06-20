package neo.util.comm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * 	@Class Name	: 	ShowDomTree.java
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
public class ShowDomTree {

	private ShowDomTree() {
	}

	public static void saveDocAsFile(Document doc, String fileName) {
		try {
			TransformerFactory tfFac = TransformerFactory.newInstance();
			Transformer tf = tfFac.newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "euc-kr");
			tf.transform(new DOMSource(doc),	new StreamResult(new FileWriter(fileName)));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static String returnDocAsString(Document doc) {
		StringWriter sw = new StringWriter();
		try {
			TransformerFactory tfFac = TransformerFactory.newInstance();
			Transformer tf = tfFac.newTransformer();
			tf.transform(new DOMSource(doc), new StreamResult(sw));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
}