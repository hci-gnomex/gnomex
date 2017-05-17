/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
package hci.gnomex.service;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import javax.transaction.Transactional;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author Cody Haroldsen <cody.haroldsen@hci.utah.edu>
* @since 12/14/2016
*/
@Alternative
public class UserService implements hci.ri.auth.service.UserService, Serializable {
	
	static final long serialVersionUID = -1;
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	
	/**
	 * A method to determine whether the user should be allowed to log in (assuming they are authenticated by the realm)
	 * 
	 * In this implementation, the user is checked to determine whether the user exists in the User table
	 * and whether the user is Active and has at least one role (UserGroup or SecGroupRole).
	 * Alternately, users can also log in if there is at least one application-wide permission that does not require a user account and could apply to the user.
	 */
	public boolean isValidUser(String userLogin, String applicationName) {
	  //TODO: Implement this for GNomEx. This might be correct, since they allow users to login with LDAP credentials, but no AppUser record
	  return true;
	}
	
	/**
	 * This method will look up the User based on their userLogin and return their AuthenticationInfo
	 * 
	 * For now the principal is the User and the credentials are their locally stored (hashed) password, if they have one
	 * 
	 * @param userLogin 
	 * @param realmName The name of the realm (it is needed to add principals to SimplePrincipalCollection)
	 * 
	 * @return AuthenticationInfo - SimpleAuthenticationInfo - This just wraps up the user's principal and credentials
	 */
	public AuthenticationInfo getAuthenticationInfo(String userLogin, String realmName) {
		log.debug("getUser " + userLogin);
		
		AppUser user =  getAppUser(userLogin, realmName);
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
		SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
		principalCollection.add(userLogin, realmName);
		principalCollection.add(user, realmName);
		authenticationInfo.setPrincipals(principalCollection);
		authenticationInfo.setCredentials(user.getPasswordExternal());
		return authenticationInfo;
	}

	/**
	 * Looks up the roles in the Auth model for the specified principal (user)
	 * 
	 * Roles include both UserGroups names and SecGroupRoles.
	 * For SecGroupRoles, roles are created with a name of the form governorClass:governorId:roleName
	 * 		governorClass is the class corresponding to the SecGroup
	 * 		roleName is the name of the SecGroupRole for the given SecGroup
	 * 		governorId is the PK of the specific instance of the SecGroup, for which the user is granted the given SecGroupRole
	 */
	@Override
	@Transactional
	public Set<String> getRoles(PrincipalCollection principals, String applicationName) {
		Set<String> roles = new HashSet<String>();
		
		//TODO: Implement this for GNomEx
		roles.add("GNomExUser");
		
		return roles;
	}
	
	
	/**
	 * A method to get the idUser from the principals
	 * 
	 * Since it is up to the UserService how the principals are created in getAuthenticationInfo, the UserService should decide how to retrieve the idUser, for use in the AuthModel
	 * 
	 * @param principals
	 * @return idUser from User
	 */
	public Integer getIdUser(PrincipalCollection principals) {
	  Integer idUser = null;
	  
	  if (principals != null) {
	    AppUser user = principals.oneByType(AppUser.class);
	    idUser = (user != null)?user.getIdAppUser():null;
	  }
	  
	  return idUser;
	}
	
	private AppUser getAppUser(String userLogin, String realmName) {
	  AppUser appUser = null;
	  
	  String hql = "FROM AppUser WHERE " + (realmName.equals("localRealm")?"userNameExternal":"uNID") + " = :userLogin";
	 
	  try {
      Session sess = HibernateSession.currentReadOnlySession(userLogin);
      
      List<AppUser> appUsers = (List<AppUser>) sess.createQuery(hql).setString("userLogin", userLogin).list();
      
      //AppUser exists
      if (! appUsers.isEmpty()) {
        appUser = appUsers.get(0);
      }   
	  } catch (Exception e) {
      log.error("An exception has occurred in UserService ", e);
      HibernateSession.rollback();
    } finally {
      try {
        HibernateSession.closeSession();
      } catch (Exception e) {
        log.error("An exception has occurred in UserService ", e);
      }
    }
	  
    //AppUser not present (ok for UNID only login - no user)
	  if (appUser == null) {
      appUser = new AppUser();
      appUser.setIdAppUser(-1); 
	  }
	  
	  return appUser;
	}
}