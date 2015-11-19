package hci.gnomex.utility.parsers;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

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
