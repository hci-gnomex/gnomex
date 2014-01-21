package hci.gnomex.controller;

public class SaveChromatogramFromFileServlet extends HttpClientServletBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogramFromFileServlet.class);

  protected String getNameOfServlet() {
    return "SaveChromatogramFromFileServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveChromatogramFromFile();
  }
  
  protected void logError(String msg, Exception ex) {
    log.error(msg, ex);
  }
}
