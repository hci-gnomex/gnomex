package hci.gnomex.utility;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Assay;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Primer;
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
  private List<String>     wellGroupIds = new ArrayList<String>();
  private List<Integer>    groupNumSamples = new ArrayList<Integer>();
  
  private SecurityAdvisor  secAdvisor;

  public PlateReportHTMLFormatter(SecurityAdvisor secAdvisor, Element plateNode, InstrumentRun ir) {
    this.secAdvisor = secAdvisor;
    this.plateNode = plateNode;
    this.ir = ir;
  }

  // Run information above plate grid
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
    table.addContent(makeRow("Run Name", runLabel, "Run ID", runId));

    table.addContent(makeRow("Create Date", createDate, "Created By", creator));

    return table;
  }

  // Colored Plate Table
  public Element makePlateTable(  ) {

    Element table = new Element( "TABLE" );
    table.setAttribute( "CLASS", "grid" );
    table.setAttribute( "CELLPADDING", "0" );
    table.setAttribute( "CELLSPACING", "0" );
    table.setAttribute( "STYLE", "table-layout:fixed" );

    // Add column numbers
    Element rowh = new Element( "TR" );
    table.addContent( rowh );
    this.addPlateRowHeaderCell( rowh, "" );
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
        addPlateRowHeaderCell( row, String.valueOf( letter ) );

        for( int count = 0; count < 12; count ++ ) {
          if( i.hasNext() ) {
            Element well = (Element) i.next();

            String idPlateWellString = well.getAttributeValue("idPlateWellShort");
            idPlateWellString = idPlateWellString != null ? idPlateWellString.substring( idPlateWellString.length() > 6 ? idPlateWellString.length() - 5 : 0 ) : "0"; 
            String sampleName = well.getAttributeValue( "sampleName" ) != null ? well.getAttributeValue( "sampleName" ) : "";
            int endInd = Math.min( sampleName.length(), 10 );
            int midInd = Math.min( sampleName.length(), 5 );
            sampleName = sampleName.substring( 0, endInd );
            if ( well.getAttributeValue( "redoFlag" ) != null && well.getAttributeValue( "redoFlag" ).equals( "Y" ) ) {
              sampleName = "Redo-" + sampleName.substring( 0, midInd );
            }

            if ( well.getAttributeValue( "isControl" ) != null && well.getAttributeValue( "isControl" ).equals( "Y" ) ) {
              sampleName = "Control";
              addControlWellCell( row );
            
            } else {
              
              String idRequest = well.getAttributeValue( "idRequest" );
              String requestNumber = well.getAttributeValue( "requestNumber" );
              
              if ( well.getAttributeValue( "idAssay" ) != null && !well.getAttributeValue( "idAssay" ).equals("") ) {
                
                String idAssay = well.getAttributeValue( "idAssay" );
                String assayName = well.getAttributeValue( "assayName" );
                
                // Add assay number to list of group numbers
                if ( idPlateWellString != null && !idPlateWellString.equals( "0" ) ) {
                  if ( !wellGroupIds.contains( idAssay ) ) {
                    wellGroupIds.add( idAssay );
                    groupNumSamples.add( 1 );
                  } else {
                    int index = wellGroupIds.indexOf( idAssay );
                    int val = groupNumSamples.get( index );
                    int newVal = val+1;
                    groupNumSamples.set( index, newVal );
                  }
                }
                
                this.addColoredWellCell( row,
                    idPlateWellString != null && !idPlateWellString.equals( "0" ) ? idPlateWellString : "&nbsp;",
                        sampleName, assayName, requestNumber, idAssay);

              } else if ( well.getAttributeValue( "idPrimer" ) != null && !well.getAttributeValue( "idPrimer" ).equals("") ) {
                
                String idPrimer = well.getAttributeValue( "idPrimer" );
                String primerName = well.getAttributeValue( "primerName" );
                
                // Add request number to list of group numbers
                if ( idPlateWellString != null && !idPlateWellString.equals( "0" ) ) {
                  if ( !wellGroupIds.contains( idPrimer ) ) {
                    wellGroupIds.add( idPrimer );
                    groupNumSamples.add( 1 );
                  } else {
                    int index = wellGroupIds.indexOf( idPrimer );
                    int val = groupNumSamples.get( index );
                    int newVal = val+1;
                    groupNumSamples.set( index, newVal );
                  }
                }

                this.addColoredWellCell( row,
                    idPlateWellString != null && !idPlateWellString.equals( "0" ) ? idPlateWellString : "&nbsp;",
                        sampleName, primerName, requestNumber, idPrimer);

              } else {

                // Add request number to list of group numbers
                if ( idPlateWellString != null && !idPlateWellString.equals( "0" ) ) {
                  if ( !wellGroupIds.contains( idRequest ) ) {
                    wellGroupIds.add( idRequest );
                    groupNumSamples.add( 1 );
                  } else {
                    int index = wellGroupIds.indexOf( idRequest );
                    int val = groupNumSamples.get( index );
                    int newVal = val+1;
                    groupNumSamples.set( index, newVal );
                  }
                }

                this.addColoredWellCell( row,
                    idPlateWellString != null && !idPlateWellString.equals( "0" ) ? idPlateWellString : "&nbsp;",
                        sampleName, requestNumber, idRequest);
              
              }
            }
          }
        }
      }
    }
    return table;
  }

  // Request detail table
  public Element makeRequestTable(Map requestMap) {

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
      for ( Iterator i = wellGroupIds.iterator(); i.hasNext(); ) {
        String idRequestString = (String) i.next();
        Request req = 
          (Request) requestMap.get(idRequestString);
        if ( req == null ) {
          break;
        }
        int index = wellGroupIds.indexOf( idRequestString );
        Integer val = groupNumSamples.get( index );

        Element row = new Element("TR");
        table.addContent(row);
        
        // Need to add background color to this first cell.
        this.addColoredCell(row, req.getNumber() != null ? req.getNumber() : "&nbsp;", index);
        this.addCell(row, req.getName() != null ? req.getName() : "&nbsp;");
        this.addCell(row, req.getOwnerName() != null ? req.getOwnerName() : "&nbsp;");
        this.addCell(row, val.toString());
        this.addCell(row, req.getCreateDate() != null ? new SimpleDateFormat("MM/dd/yyyy").format(req.getCreateDate()) : "&nbsp;");

      }
    }
    return table;
  }
  
  // Assay detail table
  public Element makeAssayTable(Map assayMap) {

    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "5");
    table.setAttribute("CELLSPACING", "5");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Assay ID", "left");
    this.addHeaderCell(rowh, "Assay Label" );
    this.addHeaderCell(rowh, "Description"    );
    this.addHeaderCell(rowh, "Samples in Run"    );

    if ( assayMap != null ) {
      for ( Iterator i = wellGroupIds.iterator(); i.hasNext(); ) {
        String idAssayString = (String) i.next();
        Assay assay = (Assay) assayMap.get(idAssayString);
        if ( assay == null ) {
          break;
        }
        int index = wellGroupIds.indexOf( idAssayString );
        Integer val = groupNumSamples.get( index );

        Element row = new Element("TR");
        table.addContent(row);
        
        // Need to add background color to this first cell.
        this.addColoredCell(row, assay.getIdAssay() != null ? assay.getIdAssay().toString() : "&nbsp;", index);
        this.addCell(row, assay.getDisplay() != null ? assay.getDisplay() : "&nbsp;");
        this.addCell(row, assay.getDescription() != null ? assay.getDescription() : "&nbsp;");
        this.addCell(row, val.toString());
        
      }
    }
    return table;
  }
  
  // Primer detail table
  public Element makePrimerTable(Map primerMap) {

    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "5");
    table.setAttribute("CELLSPACING", "5");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Primer ID", "left");
    this.addHeaderCell(rowh, "Primer Label" );
    this.addHeaderCell(rowh, "Description" );
    this.addHeaderCell(rowh, "Sequence");
    this.addHeaderCell(rowh, "Samples in Run"    );

    if ( primerMap != null ) {
      for ( Iterator i = wellGroupIds.iterator(); i.hasNext(); ) {
        String idPrimer = (String) i.next();
        Primer primer = (Primer) primerMap.get(idPrimer);
        if ( primer == null ) {
          break;
        }
        int index = wellGroupIds.indexOf( idPrimer );
        Integer val = groupNumSamples.get( index );

        Element row = new Element("TR");
        table.addContent(row);
        
        // Need to add background color to this first cell.
        this.addColoredCell(row, primer.getIdPrimer() != null ? primer.getIdPrimer().toString() : "&nbsp;", index);
        this.addCell(row, primer.getDisplay() != null ? primer.getDisplay() : "&nbsp;");
        this.addCell(row, primer.getDescription() != null ? primer.getDescription() : "&nbsp;");
        this.addCell(row, primer.getSequence() != null ? primer.getSequence() : "&nbsp;");
        this.addCell(row, val.toString());
        
      }
    }
    return table;
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
  
  private void addColoredWellCell(Element row, String text1, String text2, String text3, String groupId) {
    int index = wellGroupIds.indexOf( groupId );

    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "plate " + "colored" + (index+1));
    Element cell2 = new Element("DIV");
    cell2.setAttribute("CLASS", "ptext " + "colored0");
    cell2.setAttribute("STYLE", "width:38px;max-width:38px;overflow:hidden");
    cell2.addContent(text1);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text2);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text3);
    cell.addContent( cell2 );
    row.addContent(cell);
  } 
  
  private void addColoredWellCell(Element row, String text1, String text2, String text3, String text4, String groupId) {
    int index = wellGroupIds.indexOf( groupId );

    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "plate " + "colored" + (index+1));
    Element cell2 = new Element("DIV");
    cell2.setAttribute("CLASS", "ptext " + "colored0");
    cell2.setAttribute("STYLE", "width:38px;max-width:38px;overflow:hidden");
    cell2.addContent(text1);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text2);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text3);
    cell2.addContent( new Element( "BR" ) );
    cell2.addContent(text4);
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

  private void addPlateRowHeaderCell(Element row, String header) {
    addPlateHeaderCell(row, header, "plategrid", 12);
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
