package neo.util.comm;

import java.util.List;

/**
 * 	@Class Name	: 	PagingHelper.java
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
public interface PagingHelper {

	/**
	 * ����¡ ó���� �Ǵ� �������� �ѱ� �Ķ���͵��� ���� �Ѵ�.
	 * @param name
	 * @param value
	 */	
	public void setParameter(String name, String value);
	/**
	 * ���� �������� ���ڵ���� ��´�.
	 * @return
	 * @throws Exception
	 */	
	public List getRecords() throws Exception;
	
	/**
	 * ���� �������� ����¡ ó���� �� html ���ڿ��� ��´�.
	 * @return
	 */
	public String getPageHtml();
	
	/**
	 * �� �������� ��Ÿ���� ���ڿ��� Ŭ���Ͽ����� ȣ��� URI
	 * ���� �� URI�� �������� ǥ���ϴ� jsp �� �� ���̴�.
	 * @param linkUrl
	 */
	public void setRequestURI(String linkUrl);
	
	/**
	 * �� �������� ǥ�õ� ���ڵ��� ����(�⺻��:10)
	 * @param perPage
	 */
	public void setContentsPerPage(int perPage);
	
	/**
	 * �� �������� ǥ�õ� ������ ��ȣ�� ����(�⺻��:10)
	 * @param perBlock
	 */	
	public void setPagesPerBlock(int perBlock);
	
	/**
	 * ǥ���� ������ ��ȣ�� �����Ҷ� ���Ǵ� �Ķ���� �̸�(�⺻��:page)
	 * @param pageParamName
	 */	
	public void setPageParamName(String pageParamName);
	
	/**
	 *  ��ü ���ڵ� ������ ���۰� �� ��ȣ ����
	 * @param record_count
	 */
	public void setRecordCount(int record_count);

	/**
	 *  ���۹�ȣ ȣ��
	 * @return
	 */
	public int getStart();
	
	/**
	 *  ����ȣ ȣ��
	 * @return
	 */
	public int getEnd();
	
	/**
	 *  �� �������� ǥ�õ� ���ڵ��� ���� ����
	 * @return
	 * @throws Exception
	 */
	public int getListSize() throws Exception;
	
	/**
	 *  MS-SQL���� ����¡ ó���� �ϱ����� ���������� ���ڵ� �� (([�ش� ������]- 1) * [����Ʈ������])
	 * @return
	 * @throws Exception
	 */
	public int getSubListSize() throws Exception;
	
	/**
	 *  jsp �������� ���� ��ȣ�� �ο��ϱ� ���� ���� ��ȣ ���� �κ�(����Ʈ�� ù��°�� ������ ��ȣ)
	 * @return
	 * @throws Exception
	 */
	public int getListNumberStart() throws Exception;

	/**
	 * ǥ���� ������ ��ȣ�� �����Ҷ� ���Ǵ� �Ķ���� �̸�
	 * @return
	 */
	public String getPageParamName();
	
	/**
	 * �� �������� ��Ÿ���� ���ڿ��� Ŭ���Ͽ����� ȣ��� URI
	 * @return
	 */
	
	public String getRequestURI();
	/**
	 * �� �������� ǥ�õ� ������ ��ȣ�� ����(�⺻��:10)
	 * @return
	 */
	public int getPagesPerBlock();
	
	/**
	 * �� �������� ǥ�õ� ���ڵ��� ����(�⺻��:10)
	 * @return
	 */
	public int getContentsPerPage();
}