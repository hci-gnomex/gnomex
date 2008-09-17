package hci.gnomex.utility;

import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.SequenceLane;

import java.util.Iterator;
import java.util.Set;

import org.jdom.Element;


public class FlowCellHTMLFormatter {
  
  private FlowCell          flowCell;
  private DictionaryHelper dictionaryHelper;
  
 public FlowCellHTMLFormatter(FlowCell flowCell, DictionaryHelper dictionaryHelper) {
   this.flowCell = flowCell;
   this.dictionaryHelper = dictionaryHelper;
   
 }
 

 public Element makeFlowCellTable() {
    
    
    Element table = new Element("TABLE");    
    table.setAttribute("CELLPADDING", "0");
    table.addContent(makeRow("Flow Cell #:", flowCell.getNumber(), "Barcode:", (flowCell.getBarcode() != null ? flowCell.getBarcode() : "&nbsp;")));
    table.addContent(makeRow("Number of Seq Cycles:", dictionaryHelper.getNumberSequencingCycles(flowCell.getIdNumberSequencingCycles()), "&nbsp;", "&nbsp;")); 
    
    
    
    return table;
  }
  
  
  public Element makeFlowCellChannelTable(Set channels) {
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
    this.addHeaderCell(rowh, "Sequence Sample #"    );
    this.addHeaderCell(rowh, "Flow Cell Type");
    this.addHeaderCell(rowh, "Organism"    );
    this.addHeaderCell(rowh, "# Cycles");
    this.addHeaderCell(rowh, "Est. Fragment Size"    );
    this.addHeaderCell(rowh, "Lib Fragment Size"    );
    this.addHeaderCell(rowh, "Lib Conc. (ng/uL)"    );
    this.addHeaderCell(rowh, "Flow Cell Stock Lib vol (uL)"    );
    this.addHeaderCell(rowh, "Flow Cell Stock EB vol (uL)"    );

    
    for(Iterator i = channels.iterator(); i.hasNext();) {
      FlowCellChannel channel = (FlowCellChannel)i.next();
      
      
      Element row = new Element("TR");
      table.addContent(row);
      this.addLeftCell(row, channel.getNumber().toString());
      this.addCell(row, channel.getContentNumber());
      
      String fragmentSize = "";
      String libFragmentSize = "";
      if (channel.getSequenceLane() != null) {
        SequenceLane lane = channel.getSequenceLane();
        
        if (lane.getFragmentSizeFrom() != null) {
          fragmentSize += lane.getFragmentSizeFrom() + "-";
        } else {
          fragmentSize += "?-";
        }
        if (lane.getFragmentSizeTo() != null) {
          fragmentSize += lane.getFragmentSizeTo();
        } else {
          fragmentSize += "?";
        }
        
        
        if (lane.getSample().getSeqPrepQualFragmentSizeFrom() != null) {
          libFragmentSize += lane.getSample().getSeqPrepQualFragmentSizeFrom() + "-";
        } else {
          libFragmentSize += "?-";
        }
        if (lane.getSample().getSeqPrepQualFragmentSizeTo() != null) {
          libFragmentSize += lane.getSample().getSeqPrepQualFragmentSizeTo();
        } else {
          libFragmentSize += "?";
        }
        
        this.addCell(row, lane.getIdFlowCellType() != null ? dictionaryHelper.getFlowCellType(lane.getIdFlowCellType()) : "&nbsp;");
        this.addCell(row, lane.getIdOrganism() != null  ? dictionaryHelper.getOrganism(lane.getIdOrganism()) : "&nbsp;");
        this.addCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
        this.addCell(row, fragmentSize);
        this.addCell(row, libFragmentSize);
        this.addCell(row, lane.getSample().getSeqPrepLibConcentration() != null  ? lane.getSample().getSeqPrepLibConcentration().toString() : "&nbsp;");
        this.addCell(row, lane.getSample().getSeqPrepStockLibVol() != null  ? lane.getSample().getSeqPrepStockLibVol().toString() : "&nbsp;");
        this.addCell(row, lane.getSample().getSeqPrepStockEBVol() != null  ? lane.getSample().getSeqPrepStockEBVol().toString() : "&nbsp;");
        
      } else {
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
        this.addCell(row, "&nbsp;");
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

  private void addCell(Element row, String value) {
      Element cell = new Element("TD");
      cell.setAttribute("CLASS", "grid");      
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



}
