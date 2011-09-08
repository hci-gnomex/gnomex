package hci.gnomex.controller;

/**
 *  The front controller for the test application
 *
 *@author     Bin Yu
 *@created    August 17, 2002
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckSessionStatus extends HttpServlet {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CheckSessionStatus.class);

  /**
   * Initialize global variables
   * 
   * @exception ServletException
   *              Description of the Exception
   */
  public void init() throws ServletException {
    
  }


  /**
   * Process the HTTP Get request
   * 
   * @param request
   *          Description of the Parameter
   * @param response
   *          Description of the Parameter
   * @exception ServletException
   *              Description of the Exception
   * @exception IOException
   *              Description of the Exception
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Process the HTTP Post request
   * 
   * @param request
   *          Description of the Parameter
   * @param response
   *          Description of the Parameter
   * @exception ServletException
   *              Description of the Exception
   * @exception IOException
   *              Description of the Exception
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // get the users session
    String xmlResult;
    if (!request.isRequestedSessionIdValid()) {
      xmlResult = "<data><sa exists='false' lastAccessedTime='-1' inactiveTime='-1' currentTime='-1' "
          + "sessionMaxInActiveTime='0' kiosk='true' /></data>";
      log.debug("Session is not valid anymore");
    } else {
      HttpSession session = request.getSession();
      if (session == null || session.isNew()) {
        xmlResult = "<data><sa exists='false' lastAccessedTime='-1' inactiveTime='-1' currentTime='-1' "
            + "sessionMaxInActiveTime='0' /></data>";
        log.debug("Session is new or not exist");
      } else {
        Long slac = (Long) session.getAttribute("lastGNomExAccessTime");
        long lastTime;
        if (slac == null)
          lastTime = session.getLastAccessedTime();
        else
          lastTime = slac.longValue();
        // log.debug("Session last accessed time: " + new Date(lastTime));
        xmlResult = "<data><sa exists='true' lastAccessedTime='" + lastTime + "' " + "currentTime='"
            + new Date().getTime() + "' sessionMaxInActiveTime='" + request.getSession().getMaxInactiveInterval()
            + "' " + " /></data>";
      }
      log.debug(xmlResult);
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println(xmlResult);
    out.close();
  }
}
