package neo.util.xml;

import java.io.*;
import javax.xml.parsers.*;

import neo.util.log.Log;

import org.w3c.dom.*;

/**
 * 	@Class Name	: 	XMLPrinter.java
 * 	@���ϼ���		: 	UI�ܿ��� ����Ÿ�� Page �������� �����ϱ� ���� Entity Class
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
public class XMLPrinter {
	
	private int intFlag = 0; // tab depth�� �����ϴ� ����

	/**
	 * XML ������ �Ľ��Ͽ� ��Ʈ��Ҹ� �� ��, visitChild() �Լ��� ȣ���ϴ� �޼ҵ�
	 * 
	 * @param strArg : ����� xml ����
	 * @exception Exception
	 */
	private void start(String strArg) {
		Document doc; // document ��ü
		try {
			// DOM Document�� �����ϱ� ���Ͽ� ���丮�� ������
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			// ���丮�κ��� Document�ļ��� ��
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Document DOM�ļ��� �Ͽ��� �Է¹��� ������ �Ľ��ϵ��� ��û��
			doc = builder.parse(new File(strArg));

			// �Ľ̵� ���� ��Ʈ��Ҹ� �������� �ϸ�, �� Ÿ���� ElementŸ����
			Element root = doc.getDocumentElement();

			// root element�� �����
			prtStrElement(root);
			Log.info("\n");
			// root element�� �ڽĳ�带 �湮�Ͽ� �����
			visitChild(root);
			// root element�� ����
			prtEndElement(root);
		} catch (Exception e) {
			Log.info(e.getMessage(), this);
		}
	}

	/**
	 * root node���� child node�� �湮�ϸ鼭 �ܼ�â�� ����ϴ� �޼ҵ�
	 * 
	 * @param n : root node
	 * @exception Exception
	 */
	private void visitChild(Node n) throws Exception {
		// root element�� �ڽĳ�尡 ���������� �湮�ؼ� �����
		for (Node ch = n.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
			// root element�� �ڽĳ���� element node�� ó����
			if (ch.getNodeType() == Node.ELEMENT_NODE) {
				// �����尡 ������ ����̸�
				if (isLastNode(ch)) {
					prtPlusTab();
					// �������� attribute ó��
					if (ch.hasAttributes()) {
						this.printAttr(ch);
						this.prtValElement(ch);
						this.prtEndElement(ch);
					} else {
						this.prtStrElement(ch);
						this.prtValElement(ch);
						this.prtEndElement(ch);
					}
					intFlag--;
					// �����尡 ������ ��尡 �ƴϸ�(�ڽĳ�尡 ������)
				} else {
					prtPlusTab();
					// �������� attribute ó��
					if (ch.hasAttributes()) {
						this.printAttr(ch);
					} else {
						this.prtStrElement(ch);
					}
					Log.info("\n");
					// [����Լ�] ������ ��尡 �ƴϱ� ������ �ڽĳ�带 ��� �湮��
					this.visitChild(ch);
					prtTab();
					this.prtEndElement(ch);
					intFlag--;
				}
			}
		}
	}

	/**
	 * start element�� ����ϴ� �޼ҵ�
	 * 
	 * @param n : ����� node
	 * @exception Exception
	 */
	private void prtStrElement(Node n) throws Exception {
		Log.info("<" + n.getNodeName() + ">", this);
	}

	/**
	 * element�� ���� ����ϴ� �޼ҵ�
	 * 
	 * @param n : ���� ����� node
	 * @exception Exception
	 */
	private void prtValElement(Node n) throws Exception {
		if (n.hasChildNodes()) {
			Log.info(n.getFirstChild().getNodeValue(), this);
		}
	}

	/**
	 * end element�� ����ϴ� �޼ҵ�
	 * 
	 * @param n : ����� node
	 * @exception Exception
	 */
	private void prtEndElement(Node n) throws Exception {
		Log.info("</" + n.getNodeName() + ">\n", this);
	}

	/**
	 * element�� attribute�� ���� ��� ����ϴ� �޼ҵ�
	 * 
	 * @param n : attribute�� ����� node
	 * @exception Exception
	 */
	private void printAttr(Node n) throws Exception {
		Log.info("<" + n.getNodeName());
		NamedNodeMap nm = n.getAttributes();
		for (int i = 0; i < nm.getLength(); i++) {
			Log.info(" " + nm.item(i).getNodeName() + "=\""	+ nm.item(i).getNodeValue() + "\"", this);
		}
		Log.info(">", this);
	}

	/**
	 * ������ ������� Ȯ���ϴ� �޼ҵ�
	 * 
	 * @param n : Ȯ���� ���
	 * @return ������ ����̸� true, �ƴϸ� false
	 * @exception Exception
	 */
	private boolean isLastNode(Node n) throws Exception {
		for (Node nd = n.getFirstChild(); nd != null; nd = nd.getNextSibling()) {
			if (nd.getNodeType() == Node.ELEMENT_NODE)
				return false;
		}
		return true;
	}

	/**
	 * tab depth�� �Ѵܰ� �ø��� �޼ҵ�
	 * 
	 * @exception Exception
	 */
	private void prtPlusTab() throws Exception {
		intFlag++;
		for (int i = 0; i < intFlag; i++) {
			Log.info("\t", this);
		}
	}

	/**
	 * tab depth�� �Ѵܰ� ���̴� �޼ҵ�
	 * 
	 * @exception Exception
	 */
	private void prtMinusTab() throws Exception {
		intFlag--;
		for (int i = 0; i < intFlag; i++) {
			Log.info("\t", this);
		}
	}

	/**
	 * tab�� ����ϴ� �޼ҵ�
	 * 
	 * @exception Exception
	 */
	private void prtTab() throws Exception {
		for (int i = 0; i < intFlag; i++) {
			Log.info("\t", this);
		}
	}

	/**
	 * main �Լ�
	 * 
	 * @param args : ����� xml ����
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			Log.info("\n\n[Usage] : java XMLPrinter [xml���ϸ�]");
			Log.info("          parsing�� xml���ϸ��� �Է��� �ּ���!!!\n\n");
			return;
		}
		XMLPrinter xmlPrinter = new XMLPrinter();
		xmlPrinter.start(args[0]);
	}
}
