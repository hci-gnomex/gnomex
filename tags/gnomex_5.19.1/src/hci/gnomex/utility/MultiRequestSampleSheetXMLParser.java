package hci.gnomex.utility;

import hci.gnomex.security.SecurityAdvisor;

import java.util.Iterator;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class MultiRequestSampleSheetXMLParser extends MultiRequestSampleSheetAbstractParser {

  private Document headerDocument = null;
  private Document rowDocument = null;
  
  public MultiRequestSampleSheetXMLParser(Document headerDocument, Document rowDocument, SecurityAdvisor secAdvisor) {
    super(secAdvisor);
    this.headerDocument = headerDocument;
    this.rowDocument = rowDocument;
  }

  @Override
  protected void readFile() {
    // no file to read for XML
  }
  
  @Override
  protected void parseRows(Session sess, DictionaryHelper dh) {
    Integer rowOrdinal = 1;
    for (Iterator i = this.rowDocument.getRootElement().getChildren("Row").iterator(); i.hasNext(); ) {
      Element row = (Element)i.next();
      String[] values = getValues(row);
      parseRow(sess, dh, values, rowOrdinal);
      rowOrdinal++;
    }
  }

  private String[] getValues(Element row) {
    String[] values = new String[columnMap.keySet().size()];
    for(Integer ordinal : columnMap.keySet()) {
      // Note the attribute is n<ordinal> (ex. n1, n2, n10, etc.)
      String name = "n" + ordinal.toString();
      values[ordinal] = row.getAttributeValue(name);
    }
    
    return values;
  }
  
  @Override
  protected String[] getHeaderStrings() {
    // -1 to adjust for the row ordinal column.
    String headers[] = new String[this.headerDocument.getRootElement().getChildren("Header").size()-1];
    for(Iterator i = this.headerDocument.getRootElement().getChildren("Header").iterator(); i.hasNext(); ) {
      Element hdrNode = (Element)i.next();
      String name = hdrNode.getAttributeValue("name");
      String header = hdrNode.getAttributeValue("header");
      // Skip the row ordinal
      if (!name.startsWith("@n")) {
        continue;
      }
      // Note the column name is @n<ordinal> (ex. @n1, @n2, @n10, etc.)
      Integer colOrdinal = Integer.parseInt(name.substring(2));
      headers[colOrdinal] = header;
    }
    return headers;
  }

  @Override
  protected Integer getNumRows() {
    return rowDocument.getRootElement().getChildren("Row").size();
  }
}
