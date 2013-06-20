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
 * 	@Class Name	: 	LdapDelete.java
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
public class LdapDelete {

	public LdapDelete() {
	}

	// LDAP server���� ������ �Ϸù�ȣ(serial)�� �˻��������� �Ͽ�
	// �ش� entry�� DN�� ���Ѵ�.
	public static String GetDNBySerial(String serial) {
		Hashtable env = new Hashtable(5, 0.75f);
		NamingEnumeration m_ne = null;
		SearchResult sr = null;
		String dn = null;

		// �Ķ���ͷ� �Ѿ�� ������ �Ϸù�ȣ�� ������ LDAP filter�� ����
		String filter = "(serial=" + serial + ")";
		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			// LDAP ������ "��������://IP:��Ʈ"�� ����
			env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:7389");

			DirContext dirCtx = new InitialDirContext(env);
			SearchControls constraints = new SearchControls();

			// �˻������� ����������� �˻�(SUBTREE)
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// �˻��� ������ BASE DN�� �����ϰ�, �˻�
			m_ne = dirCtx.search("c=kr", filter, constraints);

			while (m_ne.hasMoreElements()) {
				sr = (SearchResult) m_ne.next();
				dn = sr.getName() + "," + "c=kr";
			}
			dirCtx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dn;
	}

	public static boolean DeleteEntry(String dn) //
	{
		Hashtable env = new Hashtable(5, 0.75f);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		// LDAP ������ "��������://IP:��Ʈ"�� ����
		env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:7389");
		// LDAP�� insert/update/delete�� �� �ִ� ������ ���� admin ID�� ����
		// �ش������ LDAP������ Ȯ���ϰų� �����ڿ��� �����մϴ�.
		env.put(Context.SECURITY_PRINCIPAL, "cn=root,o=yessign,c=kr");
		// admin ID�� password�� ����
		env.put(Context.SECURITY_CREDENTIALS, "password is manmanse!!!");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		try {
			DirContext dirCtx = new InitialDirContext(env);
			dirCtx.destroySubcontext(dn);
			dirCtx.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String dn = null;
		dn = GetDNBySerial("000000091812");
		if (dn != null) {
			System.out.println(dn);
			boolean rc = DeleteEntry(dn);
			if (rc == true) {
				System.out.println("deleted");
			} else {
				System.out.println("failed to delete");
			}
		} else {
			System.out.println("failed to search");
		}
	} // main
} // class

