package hci.gnomex.controller;
import org.apache.log4j.Logger;
public class FastDataTransferUploadStartServlet extends HttpClientServletBase {
  private static Logger LOG = Logger.getLogger(FastDataTransferUploadStartServlet.class);

  @Override
  protected String getNameOfServlet() {
    return "FastDataTransferUploadStartServlet";
  }
  
  @Override
  protected GNomExCommand getCommand() {
    return new FastDataTransferUploadStart();
  }

  @Override
  protected void logError(String msg, Exception ex) {
    LOG.error(msg, ex);
  }
  
}
