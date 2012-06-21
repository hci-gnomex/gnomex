package hci.gnomex.utility;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Element;


public class PlateReportHTMLFormatter {

  private Element          plateNode;
  private InstrumentRun    ir;
  private List<String>     requestNumbers = new ArrayList<String>();
  private List<Integer>    requestSampleNo = new ArrayList<Integer>();
  
  private DictionaryHelper dictionaryHelper;
  private SecurityAdvisor  secAdvisor;

  public PlateReportHTMLFormatter(SecurityAdvisor secAdvisor, Element plateNode, InstrumentRun ir, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.plateNode = plateNode;
    this.ir = ir;
    this.dictionaryHelper = dictionaryHelper;
  }

  public Element makeIntroNote(String note) {
    Element table = new Element("TABLE");   
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "noborder");
    cell.addContent(note);
    row.addContent(cell);
    table.addContent(row);

    return table;
  }


  public Element makeRunTable() {
    
    String creator = "";
    try {
      Session sess = this.secAdvisor.getReadOnlyHibernateSession( secAdvisor.getIdAppUser().toString() );
      String create = ir.getCreator();
      if ( create != null && !create.equals( "" ) ) {
        AppUser user = (AppUser)sess.get(AppUser.class, Integer.valueOf(create));
        creator = user != null ? user.getDisplayName() : create;
      } else {
        creator = create;
      }
    } 
    catch ( Exception e ) {
      if (ir.getCreator() != null) {
        creator = ir.getCreator();
      } 
    }

    String runId = "";
    if (ir.getIdInstrumentRun() != null) {
      runId = ir.getIdInstrumentRun().toString();
    }

    String runLabel = "";
    if (ir.getLabel() != null) {
      runLabel = ir.getLabel();
    }

    String createDate = "";
    if (ir.getCreateDate() != null) {
      createDate  = new SimpleDateFormat("MM/dd/yyyy").format(ir.getCreateDate());
    }

    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "5");
    table.addContent(makeRow("Run ID",   runId,
        "Name",     runLabel));

    table.addContent(makeRow("Create Date",   createDate,
        "Created By",    creator));

    return table;
  }


  public void addPlateTable(Element parentNode) {

    addPlateTable(parentNode, null);  

  }

  private void addPlateTable( Element parentNode, String captionStyle ) {

    Element table = new Element( "TABLE" );
    table.setAttribute( "CLASS", "grid" );
    table.setAttribute( "CELLPADDING", "0" );
    table.setAttribute( "CELLSPACING", "0" );

    // Add column numbers
    Element rowh = new Element( "TR" );
    table.addContent( rowh );
    this.addPlateHeaderCell( rowh, "" );
    this.addPlateHeaderCell( rowh, "1" );
    this.addPlateHeaderCell( rowh, "2" );
    this.addPlateHeaderCell( rowh, "3" );
    this.addPlateHeaderCell( rowh, "4" );
    this.addPlateHeaderCell( rowh, "5" );
    this.addPlateHeaderCell( rowh, "6" );
    this.addPlateHeaderCell( rowh, "7" );
    this.addPlateHeaderCell( rowh, "8" );
    this.addPlateHeaderCell( rowh, "9" );
    this.addPlateHeaderCell( rowh, "10" );
    this.addPlateHeaderCell( rowh, "11" );
    this.addPlateHeaderCell( rowh, "12" );

    if( plateNode != null ) {
      Iterator i = plateNode.getChildren("PlateWell").iterator();
      for( char letter = 'A'; letter <= 'H'; letter ++ ) {

        // New row
        Element row = new Element( "TR" );
        table.addContent( row );

        // Add row letter
        addPlateHeaderCell( row, String.valueOf( letter ) );

        for( int count = 0; count < 12; count ++ ) {
          if( i.hasNext() ) {
            Element well = (Element) i.next();


            String idPlateWellString = well.getAttributeValue("idPlateWell");
            String sampleName = well.getAttributeValue( "sampleName" );
            String idRequest = well.getAttributeValue( "idRequest" );

            if ( well.getAttributeValue( "isControl" ) != null && well.getAttributeValue( "isControl" ).equals( "Y" ) ) {
              sampleName = "Control";
              addControlWellCell( row );
            
            } else {

              // Add request number to list of request numbers
              if ( idPlateWellString != null && !idPlateWellString.equals( "0" ) ) {
                if ( !requestNumbers.contains( idRequest ) ) {
                  requestNumbers.add( idRequest );
                  requestSampleNo.add( 1 );
                } else {
                  int index = requestNumbers.indexOf( idRequest );
                  int val = requestSampleNo.get( index );
                  int newVal = val+1;
                  requestSampleNo.set( index, newVal );
                }
              }
              this.addColoredWellCell( row,
                  idPlateWellString != null && !idPlateWellString.equals( "0" ) ? idPlateWellString : "&nbsp;",
                      sampleName,
                      idRequest);
            }
          }
        }
      }
    }

    parentNode.addContent( table );
  }


  public void addRequestTable(Element parentNode, Map requestMap) {

    addRequestTable(parentNode, requestMap, null);  

  }

  private void addRequestTable(Element parentNode, Map requestMap, String captionStyle) {

    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "5");
    table.setAttribute("CELLSPACING", "5");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Request ID", "left");
    this.addHeaderCell(rowh, "Request Label" );
    this.addHeaderCell(rowh, "Requestor"    );
    this.addHeaderCell(rowh, "Samples in Run"    );
    this.addHeaderCell(rowh, "Request date");

    if ( requestMap != null ) {
      for ( Iterator i = requestNumbers.iterator(); i.hasNext(); ) {
        String idRequestString = (String) i.next();
        Request req = 
          (Request) requestMap.get(idRequestString);

        int index = requestNumbers.indexOf( idRequestString );
        Integer val = requestSampleNo.get( index );

        Element row = new Element("TR");
        table.addContent(row);

        // Need to add background color to this first cell.
        this.addColoredCell(row, req.getIdRequest() != null ? req.getIdRequest().toString() : "&nbsp;", index);
        this.addCell(row, req.getName() != null ? req.getName() : "&nbsp;");
        this.addCell(row, req.getUsername() != null ? req.getUsername() : "&nbsp;");
        this.addCell(row, val.toString());
        this.addCell(row, req.getCreateDate() != null ? new SimpleDateFormat("MM/dd/yyyy").format(req.getCreateDate()) : "&nbsp;");

      }
    }

    parentNode.addContent(table);
  }




  private Element makeRow(String header1, String value1, String header2, String value2) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "RIGHT");
    cell.addContent(header1);
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(value1);
    row.addContent(cell);

    cell = new Element("TD");
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "RIGHT");
    cell.addContent(header2);
    row.addContent(cell);

    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(value2);
    row.addContent(cell);

    return row;
  }

  private void addCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "grid");      
    cell.addContent(value);
    row.addContent(cell);
  }
  
  private void addControlWellCell(Element row ) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "plate " + "coloredC");
    Element cell2 = new Element("DIV");
    cell2.setAttribute("CLASS", "ptext " + "colored0");
    cell2.addContent("Control");
    cell.addContent( cell2 );
    row.addContent(cell);
  } 
  private void addColoredWellCell(Element row, String text1, String text2, String idRequest) {
    int index = requestNumbers.indexOf( idRequest );

    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "plate " + "colored" + (index+1));
    Element cell2 = new Element("DIV");
    cell2.setAttribute("CLASS", "ptext " + "colored0");
    cell2.addContent(text1);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text2);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(idRequest);
    cell.addContent( cell2 );
    row.addContent(cell);
  } 
  private void addColoredCell(Element row, String value, int index) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "grid " + "colored" + (index+1));  
    Element cell2 = new Element("DIV");
    cell2.setAttribute("CLASS", "ptext " + "colored0");
    cell2.addContent(value);
    cell.addContent( cell2 );
    row.addContent(cell);
  } 

  private void addPlateHeaderCell(Element row, String header) {
    addPlateHeaderCell(row, header, "plategrid");
  }

  private void addPlateHeaderCell(Element row, String header, String clazzName) {
    addPlateHeaderCell(row, header, clazzName, null);
  }

  private void addPlateHeaderCell(Element row, String header, String clazzName, Integer width) {
    Element cell = new Element("TH");    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    cell.addContent(header);
    row.addContent(cell);
  }

  private void addHeaderCell(Element row, String header) {
    addHeaderCell(row, header, "normal");
  }

  private void addHeaderCell(Element row, String header, String clazzName) {
    addHeaderCell(row, header, clazzName, null);
  }

  private void addHeaderCell(Element row, String header, String clazzName, Integer width) {
    Element cell = new Element("TH");    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    cell.addContent(header);
    row.addContent(cell);
  }

  public static void makePageBreak(Element maindiv) {
    Element pb = new Element("P");
    pb.setAttribute("CLASS", "break");
    maindiv.addContent(pb);
    maindiv.addContent(new Element("BR"));
  }

}
