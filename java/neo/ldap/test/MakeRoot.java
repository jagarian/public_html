package neo.ldap.test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.*;
import java.util.*;

/**
 * 	@Class Name	: 	MakeRoot.java
 * 	@파일설명		: 	basic JNDI application used for adding the "root" context to an LDAP server
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
public class MakeRoot {
	final static String ldapServerName = "localhost";
	final static String rootdn = "cn=Manager, o=jndiTest";
	final static String rootpass = "secret";
	final static String rootContext = "o=jndiTest";

	public static void main(String[] args) {
		// set up environment to access the server
		Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + ldapServerName + "/");
		env.put(Context.SECURITY_PRINCIPAL, rootdn);
		env.put(Context.SECURITY_CREDENTIALS, rootpass);

		try 
		{
			// obtain initial directory context using the environment
			DirContext ctx = new InitialDirContext(env);

			// now, create the root context, which is just a subcontext
			// of this initial directory context.
			ctx.createSubcontext(rootContext);
			
		} catch (NameAlreadyBoundException nabe) {
			System.err.println(rootContext + " has already been bound!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}

// end MakeRoot.java

