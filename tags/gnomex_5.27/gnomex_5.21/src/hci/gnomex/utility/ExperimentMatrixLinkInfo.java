package hci.gnomex.utility;

import hci.gnomex.model.Visibility;

import java.io.Serializable;
import java.sql.Date;

public class ExperimentMatrixLinkInfo implements Serializable {
    public String number;
    public String name;
    public Date   createDate;
    public String labLastName;
    public String labFirstName;
    public String codeVisibility;
    
    public String getLink() {
      StringBuffer linkBuf = new StringBuffer();
      if (codeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
        linkBuf.append("<a href='/gnomex/gnomexGuestFlex.jsp?requestNumber=" + this.number + "'>");
        linkBuf.append("Experiment " + this.number);
        linkBuf.append("  -  ");
        linkBuf.append(this.labFirstName != null ? this.labFirstName + " " : "");
        linkBuf.append(this.labLastName != null ? this.labLastName : "");
        linkBuf.append("  -  ");
        linkBuf.append(this.name);
        linkBuf.append("</a>");
      } else {
        linkBuf.append("<a href='/gnomex/gnomexFlex.jsp?requestNumber=" + this.number + "'>");
        linkBuf.append("Experiment " + this.number);
        linkBuf.append(" (restricted access)");
        linkBuf.append("</a>");
        
      }
      return linkBuf.toString();
    }
}
