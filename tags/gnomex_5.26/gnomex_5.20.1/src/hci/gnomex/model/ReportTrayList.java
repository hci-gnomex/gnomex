package hci.gnomex.model;

import hci.report.model.ReportTray;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportTrayList implements Serializable {
  
  private String                  fileName;
  private ArrayList<ReportTray>   trays = new ArrayList<ReportTray>();

  public ArrayList<ReportTray> getTrays() {
    return trays;
  }

  public void setTrays(ArrayList<ReportTray> trays) {
    this.trays = trays;
  }
  
  public void addTray(ReportTray reportTray) {
    trays.add(reportTray);
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

}
