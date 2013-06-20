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