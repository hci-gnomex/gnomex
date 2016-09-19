package hci.gnomex.utility;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerConfiguration {
  private static FreeMarkerConfiguration _instance = new FreeMarkerConfiguration();
  
  private Configuration configuration;
  
  private FreeMarkerConfiguration() {
    configuration = new Configuration(Configuration.VERSION_2_3_22);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }
  
  public static FreeMarkerConfiguration instance() {
    return _instance;
  }
  
  public Configuration getConfiguration() {
    return configuration;
  }
}
