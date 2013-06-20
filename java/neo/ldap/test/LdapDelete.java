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
public class LdapDelete {

	public LdapDelete() {
	}

	// LDAP server에서 인증서 일련번호(serial)을 검색조건으로 하여
	// 해당 entry의 DN을 구한다.
	public static String GetDNBySerial(String serial) {
		Hashtable env = new Hashtable(5, 0.75f);
		NamingEnumeration m_ne = null;
		SearchResult sr = null;
		String dn = null;

		// 파라미터로 넘어온 인증서 일련번호를 가지고 LDAP filter를 조립
		String filter = "(serial=" + serial + ")";
		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			// LDAP 서버의 "프로토콜://IP:포트"를 설정
			env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:7389");

			DirContext dirCtx = new InitialDirContext(env);
			SearchControls constraints = new SearchControls();

			// 검색범위를 하위디렉토까지 검색(SUBTREE)
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// 검색을 시작할 BASE DN을 설정하고, 검색
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
		// LDAP 서버의 "프로토콜://IP:포트"를 설정
		env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:7389");
		// LDAP을 insert/update/delete할 수 있는 권한을 가진 admin ID를 설정
		// 해당사항은 LDAP설정을 확인하거나 관리자에게 문의합니다.
		env.put(Context.SECURITY_PRINCIPAL, "cn=root,o=yessign,c=kr");
		// admin ID의 password를 설정
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

