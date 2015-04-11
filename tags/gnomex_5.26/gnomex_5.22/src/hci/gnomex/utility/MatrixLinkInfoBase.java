package hci.gnomex.utility;
import hci.gnomex.model.Visibility;

import java.io.Serializable;

public abstract class MatrixLinkInfoBase implements Serializable {
  public String number;
  public String name;
  public String labLastName;
  public String labFirstName;
  public String codeVisibility;
  
  public String getLink() {
    StringBuffer linkBuf = new StringBuffer();
    if (codeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
      linkBuf.append("<a href='/gnomex/gnomexGuestFlex.jsp?" + getNumberArg() + "=" + this.number + "'>");
      linkBuf.append(getTitle() + " " + this.number);
      linkBuf.append("  -  ");
      linkBuf.append(this.labFirstName != null ? this.labFirstName + " " : "");
      linkBuf.append(this.labLastName != null ? this.labLastName : "");
      linkBuf.append("  -  ");
      linkBuf.append(this.name);
      linkBuf.append("</a>");
    } else {
      linkBuf.append("<a href='/gnomex/gnomexFlex.jsp?" + getNumberArg() + "=" + this.number + "'>");
      linkBuf.append(getTitle() + " " + this.number);
      linkBuf.append(" (restricted access)");
      linkBuf.append("</a>");
      
    }
    return linkBuf.toString();
  }

  public abstract String getNumberArg();
  
  public abstract String getTitle();
  
  public abstract Boolean isExperiment();
  
  public abstract Boolean isAnalysis();
}
