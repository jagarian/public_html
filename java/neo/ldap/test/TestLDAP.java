package neo.ldap.test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.*;
import java.util.*;

/**
 * 	@Class Name	: 	TestLDAP.java
 * 	@파일설명		: 	basic JNDI application used for testing adding/retrieving objects from the server.
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
public class TestLDAP {
	final static String ldapServerName = "localhost";
	final static String rootdn = "cn=Manager, o=jndiTest";
	final static String rootpass = "secret";
	final static String rootContext = "o=jndiTest";

	public static void main(String[] args) {
		// set up environment to access the server
		Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + ldapServerName + "/" + rootContext);
		env.put(Context.SECURITY_PRINCIPAL, rootdn);
		env.put(Context.SECURITY_CREDENTIALS, rootpass);

		try 
		{
			// obtain initial directory context using the environment
			DirContext ctx = new InitialDirContext(env);

			// create some random number to add to the directory
			Integer i = new Integer(28420);

			System.out.println("Adding " + i + " to directory...");
			ctx.bind("cn=myRandomInt", i);

			i = new Integer(98765);
			System.out.println("i is now: " + i);

			i = (Integer) ctx.lookup("cn=myRandomInt");
			System.out.println("Retrieved i from directory with value: " + i);
			
		} catch (NameAlreadyBoundException nabe) {
			System.err.println("value has already been bound!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}

// end TestLDAP.java

