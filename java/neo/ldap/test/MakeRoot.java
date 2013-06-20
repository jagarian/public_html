package neo.ldap.test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.*;
import java.util.*;

/**
 * 	@Class Name	: 	MakeRoot.java
 * 	@���ϼ���		: 	basic JNDI application used for adding the "root" context to an LDAP server
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

