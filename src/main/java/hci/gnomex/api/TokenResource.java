/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
package hci.gnomex.api;

import hci.gnomex.model.AppUser;
import hci.ri.auth.service.UserService;
import hci.ri.auth.util.JwtGenerator;
import hci.ri.auth.util.KeystoreRSASignatureConfiguration;

import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import io.buji.pac4j.subject.Pac4jPrincipal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.util.WebUtils;
import org.joda.time.DateTime;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.jwt.JwtClaims;
import org.pac4j.jwt.profile.JwtProfile;
import org.pac4j.saml.credentials.authenticator.SAML2Authenticator;

/**
 * A resource for operating on user entities in the system.
 *
 * @author charoldsen
 * @since 1.0.0
 */
@Path("/token")
public class TokenResource {
	
  final long TOKEN_EXP_IN_MINUTES = 15;
  
  //@Inject
  //private UserService userService;    
  
  public TokenResource() {
  }

  /**
   * An endpoint to get a JWT token.
   *
   * @return a signed JWT token
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAuthenticatedUser(@Context ServletContext context) throws Exception {
	 
	//Get the keystore-based signing config out of the INI Web Environment, so we don't have to hard-code the keystore parameters or put them somewhere else
	IniWebEnvironment iwe = (IniWebEnvironment) WebUtils.getWebEnvironment(context);
	KeystoreRSASignatureConfiguration sigConfig = (KeystoreRSASignatureConfiguration) iwe.getObject("signingConfig", KeystoreRSASignatureConfiguration.class);

	PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
	
	//Create the JWT based on a JWT profile, rather than the SAML2Profile, to keep it more useful and concise.
	JwtProfile profile = new JwtProfile();
	
	//Tokens and SAML will be Pac4j principals. Direct Shiro logins will be whatever the UserService creates as a principal
	Pac4jPrincipal pjp = principals.oneByType(Pac4jPrincipal.class);
	if (pjp != null) {	
		profile.addAttribute("sub", getAttributeFromProfile(pjp.getProfile(), "uid"));	
		profile.addAttribute("name", getAttributeFromProfile(pjp.getProfile(), "displayName"));
	}
	else {
		String login = principals.oneByType(String.class);
		AppUser appUser = principals.oneByType(AppUser.class);
		
		if (login != null) {
			profile.addAttribute("sub", login);
			
		}
		
		if(appUser != null) {
			if (appUser.getIdAppUser() != -1) {
				profile.addAttribute("name", appUser.getDisplayName());
			}
			else {
				profile.addAttribute("name", "University User");
			}
		}
	}
	
	//Not using notOnOrAfter from SAML2, because it's set once for 5 minutes and is meant for the initial handshake
	//We want a new token each time one is requested
	
	//15 minutes seems to be an agreed upon length for tokens to last (minimize replay attacks)
	Date exp = new Date(System.currentTimeMillis());
	Long seconds = (exp.getTime() / 1000) + (TOKEN_EXP_IN_MINUTES * 60);
	
	profile.addAttribute(JwtClaims.EXPIRATION_TIME, seconds);

	
	//Build the token, which will be signed by RSA
	final JwtGenerator<CommonProfile> generator = new JwtGenerator<CommonProfile>(sigConfig);
    String token = generator.generate(profile);    
    String response = "{\"auth_token\":\"" + token + "\"}";
  	  
    //Return the token (JWTs are BASE64 encoded JSON)
    return Response.ok(response).build();
  }
  
  private Object getAttributeFromProfile(CommonProfile profile, String attributeName) {
	  Object attribute = profile.getAttribute(attributeName);
	  
	  if (attribute instanceof Collection<?> && ((Collection<?>) attribute).size() == 1) {
		  return ((Collection<?>) attribute).iterator().next();
	  }
	  else {
		  return attribute;
	  }	  
  }
}