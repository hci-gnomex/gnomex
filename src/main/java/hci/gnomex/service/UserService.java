package hci.gnomex.service;

import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Alternative;
import javax.transaction.Transactional;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 
	  if (realmName != null) {
	    if (realmName.equals("localRealm")) {
	      appUser = getExternalAppUser(userLogin);
	    }
	    else {
	      appUser = getUniversityAppUser(userLogin);
	    }	    
	  }
	  
	  return appUser;
	}
	
	private AppUser getExternalAppUser(String userLogin) {
	  return new AppUser();
	}
	
	private AppUser getUniversityAppUser(String userLogin) {
	  return new AppUser();
	}
}