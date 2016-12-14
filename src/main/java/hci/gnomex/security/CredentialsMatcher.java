/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
package hci.gnomex.security;

import hci.gnomex.model.AppUser;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.security.EncryptionUtility;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

/**
* Shiro credentials matcher for working with encrypted gnomex passwords stored in AppUser
*
* @author Cody Haroldsen <cody.haroldsen@hci.utah.edu>
* @since 12/14/2016
*/
public class CredentialsMatcher implements org.apache.shiro.authc.credential.CredentialsMatcher {

  @Override
  public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    boolean credentialsMatch = false;

    AppUser appUser = info.getPrincipals().oneByType(AppUser.class);
    if (appUser != null) {
      String gnomexPasswordEncrypted = appUser.getPasswordExternal();
      String salt = appUser.getSalt();
      
      //Salty
      if (salt != null) {
        EncryptionUtility passwordEncrypter = new EncryptionUtility();
        String thePasswordEncryptedNew = passwordEncrypter.createPassword(String.copyValueOf((char[]) token.getCredentials()), salt);
        credentialsMatch = thePasswordEncryptedNew.equals(gnomexPasswordEncrypted);
      }
      //Plain
      else {
        String thePasswordEncryptedOld = EncrypterService.getInstance().encrypt((String.copyValueOf((char[]) token.getCredentials())));
        credentialsMatch = thePasswordEncryptedOld.equals(gnomexPasswordEncrypted);
      }
    }
    
    return credentialsMatch;
  }

}
