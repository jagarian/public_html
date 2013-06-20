package neo.util.xml;

import java.io.*;
import javax.xml.parsers.*;

import neo.util.log.Log;

import org.w3c.dom.*;

/**
 * 	@Class Name	: 	XMLPrinter.java
 * 	@파일설명		: 	UI단에서 데이타를 Page 형식으로 관리하기 위한 Entity Class
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
public class XMLPrinter {
	
	private int intFlag = 0; // tab depth를 저장하는 변수

	/**
	 * XML 문서를 파싱하여 루트요소를 얻어낸 후, visitChild() 함수를 호출하는 메소드
	 * 
	 * @param strArg : 출력할 xml 파일
	 * @exception Exception
	 */
	private void start(String strArg) {
		Document doc; // document 객체
		try {
			// DOM Document를 생성하기 위하여 팩토리를 생성함
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			// 팩토리로부터 Document파서를 얻어냄
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Document DOM파서로 하여금 입력받은 파일을 파싱하도록 요청함
			doc = builder.parse(new File(strArg));

			// 파싱된 후의 루트요소를 얻어내보도록 하며, 그 타입은 Element타입임
			Element root = doc.getDocumentElement();

			// root element를 출력함
			prtStrElement(root);
			Log.info("\n");
			// root element의 자식노드를 방문하여 출력함
			visitChild(root);
			// root element를 닫음
			prtEndElement(root);
		} catch (Exception e) {
			Log.info(e.getMessage(), this);
		}
	}

	/**
	 * root node밑의 child node를 방문하면서 콘솔창에 출력하는 메소드
	 * 
	 * @param n : root node
	 * @exception Exception
	 */
	private void visitChild(Node n) throws Exception {
		// root element의 자식노드가 있을때까지 방문해서 출력함
		for (Node ch = n.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
			// root element의 자식노드중 element node만 처리함
			if (ch.getNodeType() == Node.ELEMENT_NODE) {
				// 현재노드가 마지막 노드이면
				if (isLastNode(ch)) {
					prtPlusTab();
					// 현재노드의 attribute 처리
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
					// 현재노드가 마지막 노드가 아니면(자식노드가 있으면)
				} else {
					prtPlusTab();
					// 현재노드의 attribute 처리
					if (ch.hasAttributes()) {
						this.printAttr(ch);
					} else {
						this.prtStrElement(ch);
					}
					Log.info("\n");
					// [재귀함수] 마지막 노드가 아니기 때문에 자식노드를 계속 방문함
					this.visitChild(ch);
					prtTab();
					this.prtEndElement(ch);
					intFlag--;
				}
			}
		}
	}

	/**
	 * start element를 출력하는 메소드
	 * 
	 * @param n : 출력할 node
	 * @exception Exception
	 */
	private void prtStrElement(Node n) throws Exception {
		Log.info("<" + n.getNodeName() + ">", this);
	}

	/**
	 * element의 값을 출력하는 메소드
	 * 
	 * @param n : 값을 출력할 node
	 * @exception Exception
	 */
	private void prtValElement(Node n) throws Exception {
		if (n.hasChildNodes()) {
			Log.info(n.getFirstChild().getNodeValue(), this);
		}
	}

	/**
	 * end element를 출력하는 메소드
	 * 
	 * @param n : 출력할 node
	 * @exception Exception
	 */
	private void prtEndElement(Node n) throws Exception {
		Log.info("</" + n.getNodeName() + ">\n", this);
	}

	/**
	 * element에 attribute가 있을 경우 출력하는 메소드
	 * 
	 * @param n : attribute를 출력할 node
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
	 * 마지막 노드인지 확인하는 메소드
	 * 
	 * @param n : 확인할 노드
	 * @return 마지막 노드이면 true, 아니면 false
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
	 * tab depth를 한단계 늘리는 메소드
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
	 * tab depth를 한단계 줄이는 메소드
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
	 * tab을 출력하는 메소드
	 * 
	 * @exception Exception
	 */
	private void prtTab() throws Exception {
		for (int i = 0; i < intFlag; i++) {
			Log.info("\t", this);
		}
	}

	/**
	 * main 함수
	 * 
	 * @param args : 출력할 xml 파일
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			Log.info("\n\n[Usage] : java XMLPrinter [xml파일명]");
			Log.info("          parsing할 xml파일명을 입력해 주세요!!!\n\n");
			return;
		}
		XMLPrinter xmlPrinter = new XMLPrinter();
		xmlPrinter.start(args[0]);
	}
}
