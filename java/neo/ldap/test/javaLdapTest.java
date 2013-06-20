package neo.ldap.test;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * 	@Class Name	: 	javaLdapTest.java
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
public class javaLdapTest {

	public static void main(String[] args) {

		Hashtable contextSetup = new Hashtable();
		// contains the configuration for the context setup
		String uidToSearch = "(uid=c0001)";
		// The user ID we are searching for
		String baseDN = "dc=ibm, dc=com";
		// The base DN for the entire LDAP structure
		String baseUserDN = "ou=people, " + baseDN;
		// the base DN to use to search for people. This will
		// contain the full DN for the users tree. Role trees
		// are usually contained in a seperate node, which is
		// outside of the scope of this example
		// create the parameters to use during initialization of the LDAP
		// context

		contextSetup.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		contextSetup.put(Context.PROVIDER_URL, "ldap://192.168.254.128:389");
		contextSetup.put(Context.SECURITY_CREDENTIALS, "ibm4root");
		contextSetup.put(Context.SECURITY_PRINCIPAL, "cn=root,");
		contextSetup.put(Context.URL_PKG_PREFIXES, "com.sun.jndi.url");
		
		try {
			// Create the context used to communicate with the LDAP server
			// throughout
			DirContext dContext = new InitialDirContext(contextSetup);
			// SEtup the search object. With this, we will pass in a base DN,
			// and search all the child nodes
			// In the ldap
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// Execute the search, and display all the resulting attributes, and
			// display all matches
			for (NamingEnumeration results = dContext.search(baseUserDN, uidToSearch, searchControls); results.hasMore();) {
				// Get the next individual result
				SearchResult result = (SearchResult) results.next();
				// Print out the name
				System.out.println(result.getName());
				// Display all resulting attributes
				for (NamingEnumeration attributes = result.getAttributes().getAll(); attributes.hasMore();) {
					String attribute = attributes.next().toString();
					System.out.println(attribute);
				}
				// Display the email addresses that this user has
				for (NamingEnumeration mail = result.getAttributes().get("mail").getAll(); mail.hasMore();) {
					System.out.println("Email addresses: "	+ mail.next().toString());
				}
				System.out.println("---------------------------------\n\n");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}