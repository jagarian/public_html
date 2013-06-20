package neo.ldap.test;

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.*;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * 	@Class Name	: 	LdapSearch.java
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
public class LdapSearch {

	public LdapSearch() {
	}

	// LDAP server���� ������ �Ϸù�ȣ(serial)�� �˻��������� �Ͽ� "attrIDs"��
	// ���ǵ� LDAP attribute���� ����ϴ� ����
	public static void ldapSearchBySerial(String serial) {
		Hashtable env = new Hashtable(5, 0.75f);
		NamingEnumeration m_ne = null;

		// �˻��� ��� entry�� �Ʒ��� attribute���� ����� ����
		String[] attrIDs = { "cn", "serial", "mail", "uid" };
		// �Ķ���ͷ� �Ѿ�� ������ �Ϸù�ȣ�� ������ LDAP filter�� ����
		String filter = "(serial=" + serial + ")";

		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			// LDAP ������ "��������://IP:��Ʈ"�� ����
			// �Ʒ��� ldap ���������� ����ϴ� 127.0.0.1 ������ 7389��Ʈ�� �����ϴ� ���
			env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:7389");

			DirContext dirCtx = new InitialDirContext(env);
			SearchControls constraints = new SearchControls();

			// �˻������� ����������� �˻�(SUBTREE)
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			if (attrIDs != null)
				constraints.setReturningAttributes(attrIDs);

			// �˻��� ������ BASE DN�� �����ϰ�, �˻�
			m_ne = dirCtx.search("c=kr", filter, constraints);
			dirCtx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			SearchResult sr = null;
			while (m_ne.hasMoreElements()) {
				// System.out.println(m_ne.nextElement());
				sr = (SearchResult) m_ne.next();
				// dn��½� BASE DN�� �����ϰ� ��µȴ�.
				System.out.println(sr.getName() + "," + "c=kr");
				for (int i = 0; i < 4; i++) { // attrIDs member count
					System.out.println(i
									+ ": "
									+ attrIDs[i]
									+ " : "
									+ (String) sr.getAttributes().get(attrIDs[i]).get());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ldapSearchBySerial("000000091812");
	} // main
} // class

