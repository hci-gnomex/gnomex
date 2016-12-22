package hci.gnomex.utility;
import hci.gnomex.model.Visibility;

import java.io.Serializable;

public abstract class MatrixLinkInfoBase implements Serializable {
  public String number;
  public String name;
  public String labLastName;
  public String labFirstName;
  public String codeVisibility;
  public String ownerFirstName;
  public String ownerLastName;
  public String description;
  
  public String getLink() {
    StringBuffer linkBuf = new StringBuffer();
    if (!isRestricted()) {
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

  public String getURL() {
    String url = "/gnomex/";
    if (isRestricted()) {
      url += "gnomexFlex";
    } else {
      url += "gnomexGuestFlex";
    }
    url += ".jsp?" + getNumberArg() + "=" + this.number;
    return url;
  }
  
  public String getLabel() {
    StringBuffer nameBuf = new StringBuffer();
    nameBuf.append(getTitle() + " " + this.number);
    if (isRestricted()) {
      nameBuf.append(" (restricted access)");
    }
    return nameBuf.toString();
  }
  
  public Boolean isRestricted() {
    return (!codeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC));
  }
  public String getName() {
    return name;
  }
  
  public String getLabName() {
    return (this.labFirstName != null ? this.labFirstName + " " : "") + (this.labLastName != null ? this.labLastName : "");
  }
  
  public String getOwnerName() {
    return (ownerFirstName != null &&  ownerFirstName.length() > 0 ? ownerFirstName + " " : "") 
            + (ownerLastName != null ? ownerLastName : "");
  }
  
  public String getDescription() {
    return description == null ? "" : description.replace("\"", "'");
  }
  
  public abstract String getNumberArg();
  
  public abstract String getTitle();
  
  public abstract Boolean isExperiment();
  
  public abstract Boolean isAnalysis();
}
