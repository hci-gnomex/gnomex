package hci.gnomex.utility;

import hci.gnomex.model.FlowCell;
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
  
  
  public Element makeLaneTable(Set lanes) {
    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "0");
    table.setAttribute("CELLSPACING", "0");
 
    Element caption = new Element("CAPTION");
    caption.addContent("Sequence Lanes");
    table.addContent(caption);
    
    
    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Lane #", "left");
    this.addHeaderCell(rowh, "Sample #"    );
    this.addHeaderCell(rowh, "Flow Cell Type");
    this.addHeaderCell(rowh, "# Cycles Requested");
    this.addHeaderCell(rowh, "Organism"    );
    this.addHeaderCell(rowh, "Fragment Size"    );
    this.addHeaderCell(rowh, "Conc. (ng/uL)"    );

 
    
    
    
    
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();
      
      Element row = new Element("TR");
      table.addContent(row);
      
      String fragmentSize = "";
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
      

      this.addLeftCell(row, lane.getNumber());
      this.addCell(row, lane.getSample() != null ? lane.getSample().getNumber() : "&nbsp;");
      this.addCell(row, lane.getIdFlowCellType() != null ? dictionaryHelper.getFlowCellType(lane.getIdFlowCellType()) : "&nbsp;");
      this.addCell(row, lane.getIdNumberSequencingCycles() != null  ? dictionaryHelper.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()) : "&nbsp;");
      this.addCell(row, lane.getIdOrganism() != null  ? dictionaryHelper.getOrganism(lane.getIdOrganism()) : "&nbsp;");
      this.addCell(row, fragmentSize);
      this.addCell(row, lane.getCalcConcentration() != null  ? lane.getCalcConcentration().toString() : "&nbsp;");
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
