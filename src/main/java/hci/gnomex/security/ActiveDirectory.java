package hci.gnomex.security;
/**
 * The MIT License
 *
 * Copyright (c) 2010-2012 www.myjeeva.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
 *
 */


import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.log4j.Logger;



/**
 * Query Active Directory using Java
 *
 * @filename ActiveDirectory.java
 * @author <a href="mailto:jeeva@myjeeva.com">Jeevanandam Madanagopal</a>
 * @copyright &copy; 2010-2012 www.myjeeva.com
 */
public class ActiveDirectory {
    // Logger
    //private static final Logger LOG = Logger.getLogger(ActiveDirectory.class.getName());
    private static Logger LOG = Logger.getLogger(ActiveDirectory.class);

    //required private variables
    private Properties properties;
    private DirContext dirContext;
    private SearchControls searchCtls;
    private String baseFilter = "(&((&(objectCategory=Person)(objectClass=User)))";

    /**
     * constructor with parameter for initializing a LDAP context
     *
     * @param username a {@link java.lang.String} object - username to establish a LDAP connection
     * @param password a {@link java.lang.String} object - password to establish a LDAP connection
     * @param {@link java.lang.String} object - domain controller name for LDAP connection
     */
    public ActiveDirectory(String username,
                           String password,
                           String ldap_init_context_factory,
                           String ldap_provider_url,
                           String ldap_sec_protocol,
                           String ldap_sec_auth,
                           String ldap_sec_principal ) throws NamingException {

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            throw new AuthenticationException("Invalid username/password");
        }

        properties = new Properties();

        properties.put(Context.INITIAL_CONTEXT_FACTORY, ldap_init_context_factory);
        properties.put(Context.PROVIDER_URL, ldap_provider_url);
        properties.put(Context.SECURITY_PRINCIPAL, ldap_sec_principal);
        properties.put(Context.SECURITY_CREDENTIALS, password);
        properties.put(Context.SECURITY_PROTOCOL, ldap_sec_protocol);
        properties.put(Context.SECURITY_AUTHENTICATION, ldap_sec_auth);


        // initializing active directory LDAP connection
        dirContext = new InitialDirContext(properties);
    }

    /**
     * search the Active directory by username for given search base
     *
     * @param searchValue a {@link java.lang.String} object - search value used for AD search for eg. username or email
     * @param searchBase a {@link java.lang.String} object - search base value for scope tree for eg. DC=myjeeva,DC=com
     * @return search result a {@link javax.naming.NamingEnumeration} object - active directory search result
     * @throws NamingException
     */
    public NamingEnumeration<SearchResult> searchUser(String searchValue,
                                                      String searchBase,
                                                      String[] returnAttributeNames) throws NamingException {



        //initializing search controls
        searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(returnAttributeNames);

        String filter = this.baseFilter;
        filter += "(samaccountname=" + searchValue + "))";

        return this.dirContext.search(searchBase, filter, searchCtls);
    }


    /**
     * Look at the user attributes returned as search results.  Compare to the expected
     * value provided in the map.  If ANY value matches the expected value, return true.
     * @param answer  The user attributes for this user. The search results returned from ActiveDirectory.searchUser()
     * @param ldap_user_attribute_map A map of expected values for each user attribute.
     *                                The key is the attribute name, the value is the expected value.  Use
     *
     * @return
     */
    public boolean doesMatchUserAttribute(NamingEnumeration<SearchResult> answer, Map<String, String> ldap_user_attribute_map) {
        boolean matches = false;
        StringBuffer logString = new StringBuffer();
        try {
            // Iterate through the results, the user attributes.  Determine every user attribute
            // matches its expected value.  ANY matching attribute is sufficient
            if (answer.hasMore()) {
                SearchResult s = answer.next();
                logString.append(s.getName() + "---- Users current university LDAP attributes: ");
                Attributes attrs = s.getAttributes();
                for (String attributeName : ldap_user_attribute_map.keySet()) {
                    String expectedValue = ldap_user_attribute_map.get(attributeName);
                    logString.append(attrs.get(attributeName) + " ----");
                    if (attrs.get(attributeName) != null && attrs.get(attributeName).contains(expectedValue)) {
                        matches = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Unexpected exception when iterating over user attributes", e);
        }
        if(!matches){
            LOG.error("No matching LDAP attributes in active directory " + logString.toString());
        }
        return matches;
    }


    /**
     * closes the LDAP connection with Domain controller
     */
    public void closeLdapConnection(){
        try {
            if(dirContext != null)
                dirContext.close();
        }
        catch (NamingException e) {
            LOG.error(e);
        }
    }



}
