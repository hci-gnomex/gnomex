package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Request;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;
import org.jdom.Element;


public class FlowCellHTMLFormatter  extends DetailObject {
  
  private FlowCell          flowCell;
  private DictionaryHelper dictionaryHelper;
  
 public FlowCellHTMLFormatter(FlowCell flowCell, DictionaryHelper dictionaryHelper) {
   this.flowCell = flowCell;
   this.dictionaryHelper = dictionaryHelper;
   
 }
 

 public Element makeFlowCellTable() {
    
    
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "0");
    table.addContent(makeRow("Barcode:", (flowCell.getBarcode() != null ? flowCell.getBarcode() : "&nbsp;")));
    table.addContent(makeRow("Cluster gen date:", (flowCell.getCreateDate() != null ? this.formatDate(flowCell.getCreateDate()) : "&nbsp;")));
    table.addContent(makeRow("Run completed:", getFlowCellCompleteDate())); 
    table.addContent(makeRow("Run folder:", getFolderNames())); 
    
    
    
    return table;
 }
 
 private String getFolderNames() {
   TreeMap folderNameMap = new TreeMap();
   for(Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext();) {
     FlowCellChannel ch = (FlowCellChannel)i.next();
     if (ch.getFileName() != null && !ch.getFileName().equals("")) {
       folderNameMap.put(ch.getFileName(), null);             
     }
   }
   StringBuffer folderName = new StringBuffer("");
   for(Iterator i = folderNameMap.keySet().iterator(); i.hasNext();) {
     String fn = (String)i.next();
     folderName.append(fn);
     if (i.hasNext()) {
       folderName.append(", ");
     }      
   }
   return folderName.toString() == "" ? "&nbsp;" : folderName.toString();
   
 }
 
 private String getFlowCellCompleteDate() {
   String buf = "&nbsp;";
   
   for(Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext();) {
     FlowCellChannel ch = (FlowCellChannel)i.next();
     if (ch.getLastCycleDate() != null) {
       buf = this.formatDate(flowCell.getCreateDate());
       break;
     }
   }
   
   return buf.toString();
 }
 
 public Element makeFlowCellPrepTable() {
   
   
   Element table = new Element("TABLE");    
   table.setAttribute("CELLPADDING", "0");
   table.addContent(makeRow("Barcode:", (flowCell.getBarcode() != null ? flowCell.getBarcode() : "&nbsp;")));
   table.addContent(makeRow("Cluster gen date:", (flowCell.getCreateDate() != null ? this.formatDate(flowCell.getCreateDate()) : "&nbsp;")));
   
   
   
   return table;
}
  
  
  public Element makeFlowCellChannelTable(Set channels) throws Exception {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Flow Cell Channels");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Channel #", "left");
    this.addHeaderCell(rowh, "Client");
    this.addHeaderCell(rowh, "Sequence Sample #"    );
    this.addHeaderCell(rowh, "Sample Barcode"    );
    this.addHeaderCell(rowh, "Seq Run Type");
    this.addHeaderCell(rowh, "Organism"    );
    this.addHeaderCell(rowh, "# Cycles (actual)");
    this.addHeaderCell(rowh, "Gel Size Range"    );
    this.addHeaderCell(rowh, "Clusters per Tile (1st cycle)"    );

    Session sess = HibernateGuestSession.currentGuestSession();
    
    for(Iterator i = channels.iterator(); i.hasNext();) {
      FlowCellChannel channel = (FlowCellChannel)i.next();

      if (channel.getSequencingControl() != null ){
        Element row = new Element("TR");
        table.addContent(row);
        this.addLeftCell(row, channel.getNumber().toString());

        this.addCell(row, channel.getSequencingControl() != null ? channel.getSequencingControl().getSequencingControl() : "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
      } else if (channel.getSequenceLanes() != null) { 
    	boolean firstLaneInChannel = true;
        for (Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext();) {
          SequenceLane lane = (SequenceLane)i1.next();

          Element row = new Element("TR");
          table.addContent(row);
          if (firstLaneInChannel) {
            this.addLeftCell(row, channel.getNumber().toString() );            
          } else {
            this.addLeftNoBorderCell(row, "&nbsp;");                        
          }
     
          String requester = "&nbsp;";
          Request request = (Request)sess.get(Request.class, lane.getIdRequest());
          if (request.getAppUser() != null) {
            requester = request.getAppUser().getDisplayName();
          } 
          
          
          
          this.addCell(row, requester);
          this.addCell(row, lane.getNumber());
          this.addCell(row, lane.getSample().getIdOligoBarcode() != null ? dictionaryHelper.getBarcodeSequence(lane.getSample().getIdOligoBarcode()) : "&nbsp;");
          
          String gelSize = "";
          if (lane.getSample().getSeqPrepGelFragmentSizeFrom() != null) {
            gelSize += lane.getSample().getSeqPrepGelFragmentSizeFrom() + "-";
          } else {
            gelSize += "?-";
          }
          if (lane.getSample().getSeqPrepGelFragmentSizeTo() != null) {
            gelSize += lane.getSample().getSeqPrepGelFragmentSizeTo();
          } else {
            gelSize += "?";
          }

          this.addCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
          this.addCell(row, lane.getIdOrganism() != null  ? dictionaryHelper.getOrganism(lane.getIdOrganism()) : "&nbsp;");
          this.addCell(row, channel.getNumberSequencingCyclesActual() != null && !channel.getNumberSequencingCyclesActual().equals("") ? channel.getNumberSequencingCyclesActual().toString() : "&nbsp;");
          this.addCell(row, gelSize);
          this.addCell(row, channel.getClustersPerTile() != null && !channel.getClustersPerTile().equals("") ? channel.getClustersPerTile().toString() : "&nbsp;");
          firstLaneInChannel = false;
        }
          
      } 
     
    }
    
    return table;
  }

  
  public Element makeFlowCellPrepChannelTable(Set channels) throws Exception {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Flow Cell Channels");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Channel #", "left");
    this.addHeaderCell(rowh, "Flow Cell Final Sample Conc. (pM)"    );
    this.addHeaderCell(rowh, "Client"    );
    this.addHeaderCell(rowh, "Sequence Sample #"    );
    this.addHeaderCell(rowh, "Sample Barcode"    );
    this.addHeaderCell(rowh, "Seq Run Type");
    this.addHeaderCell(rowh, "Organism"    );
    this.addHeaderCell(rowh, "# Cycles (requested)");
    this.addHeaderCell(rowh, "Gel Size Range"    );
    this.addHeaderCell(rowh, "Flow Cell 10nM Stock Vol (uL)"    );

    
    Session sess = HibernateGuestSession.currentGuestSession();
    
    for(Iterator i = channels.iterator(); i.hasNext();) {
      FlowCellChannel channel = (FlowCellChannel)i.next();
    
      if (channel.getSequencingControl() != null) {
        Element row = new Element("TR");
        table.addContent(row);
        this.addLeftCell(row, channel.getNumber().toString());
        
        this.addCell(row, channel.getSampleConcentrationpM() != null ? channel.getSampleConcentrationpMDisplay() : "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, channel.getSequencingControl() != null ? channel.getSequencingControl().getSequencingControl() : "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
      } else if (channel.getSequenceLanes() != null) {
    	boolean firstLaneInChannel = true;
        for(Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext();) {
          SequenceLane lane = (SequenceLane)i1.next();
          Element row = new Element("TR");
          table.addContent(row);
          if (firstLaneInChannel) {
            this.addLeftCell(row, channel.getNumber().toString() );            
            this.addCell(row, channel.getSampleConcentrationpM() != null ? channel.getSampleConcentrationpMDisplay(): "&nbsp;");
          } else {
            this.addLeftNoBorderCell(row, "&nbsp;");                        
            this.addNoBorderCell(row, "&nbsp;");
          }
          
          String requester = "&nbsp;";
          if (lane != null) {
            Request request = (Request)sess.get(Request.class, lane.getIdRequest());
            requester = request.getAppUser() != null ? request.getAppUser().getDisplayName() : "&nbsp;";        
          }

          this.addCell(row, requester);
          this.addCell(row, lane.getNumber());
          this.addCell(row, lane.getSample().getIdOligoBarcode() != null ? dictionaryHelper.getBarcodeSequence(lane.getSample().getIdOligoBarcode()) : "&nbsp;");

          
          String gelSize = "";
          if (lane.getSample().getSeqPrepGelFragmentSizeFrom() != null) {
            gelSize += lane.getSample().getSeqPrepGelFragmentSizeFrom() + "-";
          } else {
            gelSize += "?-";
          }
          if (lane.getSample().getSeqPrepGelFragmentSizeTo() != null) {
            gelSize += lane.getSample().getSeqPrepGelFragmentSizeTo();
          } else {
            gelSize += "?";
          }
          
          this.addCell(row, lane.getIdSeqRunType() != null ? dictionaryHelper.getSeqRunType(lane.getIdSeqRunType()) : "&nbsp;");
          this.addCell(row, lane.getIdOrganism() != null  ? dictionaryHelper.getOrganism(lane.getIdOrganism()) : "&nbsp;");
          this.addCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
          this.addCell(row, gelSize);
          this.addCell(row, lane.getSample().getSeqPrepStockLibVol() != null  ? lane.getSample().getSeqPrepStockLibVol().toString() : "&nbsp;");
          firstLaneInChannel = false;
        }
        
      } 
    }
    
    return table;
  }
  
  private Element makeRow(String header1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);
    

    return row;
  }

  private void addLeftCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridleft");      
    cell.addContent(value);
    row.addContent(cell);
  }
  
  private void addLeftNoBorderCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridleftnoborder");      
    cell.addContent(value);
    row.addContent(cell);
  }
 
  private void addCell(Element row, String value) {
      Element cell = new Element("TD");
      cell.setAttribute("CLASS", "grid");      
      cell.addContent(value);
      row.addContent(cell);
  }
  
  private void addNoBorderCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "gridnoborder");      
    cell.addContent(value);
    row.addContent(cell);
}

  
  private void addEmptyCell(Element row) {
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridempty");
    cell.addContent("&nbsp;");
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

  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", null);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, Integer width) {
    addHeaderCell(row, header, rowSpan, colSpan, "normal", width);
  }
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName) {
    addHeaderCell(row, header, rowSpan, colSpan, clazzName, null);
  }
  
  private void addHeaderCell(Element row, String header, Integer rowSpan, Integer colSpan, String clazzName, Integer width) {
    Element cell = new Element("TH");    
    if (clazzName != null) {
      cell.setAttribute("CLASS", clazzName);
    }
    cell.addContent(header);
    if (colSpan != null) {    
      cell.setAttribute("COLSPAN", colSpan.toString());
    }
    if (rowSpan != null) {
      cell.setAttribute("ROWSPAN", rowSpan.toString());      
    }
    if (width != null) {
      cell.setAttribute("WIDTH", width.toString());
    }
    row.addContent(cell);
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
    //cell.setAttribute("WIDTH", "80");
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

  private Element makeRow(String header1, String value1) {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "label");
    cell.setAttribute("ALIGN", "LEFT");
    cell.addContent(header1);
    row.addContent(cell);
    
    cell = new Element("TD");
    cell.setAttribute("CLASS", "value");
    cell.setAttribute("ALIGN", "LEFT");
    cell.setAttribute("WIDTH", "80%");
    cell.addContent(value1);
    row.addContent(cell);
    

    return row;
  } 

}
