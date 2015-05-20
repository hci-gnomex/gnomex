package hci.gnomex.utility;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class MultiRequestSampleSheetFileParser extends MultiRequestSampleSheetAbstractParser {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MultiRequestSampleSheetFileParser.class);

  private String filePath = null;
  private String header = null;
  private List<String> fileRows = null;
  
  public MultiRequestSampleSheetFileParser(String filePath, SecurityAdvisor secAdvisor) {
    super(secAdvisor);
    this.filePath = filePath;
  }

  @Override
  protected void parseRows(Session sess, DictionaryHelper dh) {
    Integer rowOrdinal = 1;
    for(String row : fileRows) {
      parseRow(sess, dh, row.split("\t"), rowOrdinal);
      rowOrdinal++;
    }
  }
  
  @Override
  protected void readFile() {
    fileRows = new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      try {
        header = br.readLine();
        for(String line = br.readLine(); line != null; line = br.readLine()) {
          fileRows.add(line);
        }
      } finally {
          br.close();
      }      
    } catch(FileNotFoundException ex) {
      errors.add(new Error(Error.FATAL, "File '" + filePath + "' now found."));
    } catch(IOException iex) {
      errors.add(new Error(Error.FATAL, "File '" + filePath + "' had read error:" + iex.getMessage()));
    }
  }
  
  @Override
  protected String[] getHeaderStrings() {
    return header.split("\t");
  }
  
  @Override
  protected Integer getNumRows() {
    return fileRows.size();
  }
}
