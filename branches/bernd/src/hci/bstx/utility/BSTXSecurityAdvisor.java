package hci.bstx.utility;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * Interface to BSTXSecurityAdvisor so that open source GNomEx can build without dependency on hciEnv
 *
 * @author Tony Di Sera
 * @created 3/11/2013
 */
public class BSTXSecurityAdvisor
{
  public static final  String SECURITY_ADVISOR_SESSION_KEY = "";
  
  public static BSTXSecurityAdvisor create(HttpServletRequest request, HttpSession httpSession, 
    Session bstxSession, String username) throws IllegalArgumentException, HibernateException {
    return null;
  };

  public Map<String, boolean[]> canReadSamples(List<String> ccNumbers, Session bstxSession){
    return null;
  };
}
