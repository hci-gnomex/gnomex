package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.utility.MolarityCalculator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class CalculateMolarity extends GNomExCommand implements Serializable {
  
 
  
 
  
  private Integer libConcentration = null;
  private Integer averageFragmentSize = null;
 
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("libConcentration") != null && !request.getParameter("libConcentration").equals("")) {
      libConcentration = new Integer(request.getParameter("libConcentration"));
    }
    if (request.getParameter("averageFragmentSize") != null && !request.getParameter("averageFragmentSize").equals("")) {
      averageFragmentSize = new Integer(request.getParameter("averageFragmentSize"));
    }
    
    
    double molarity = MolarityCalculator.calculateConcentrationInnM(libConcentration.intValue(), averageFragmentSize.intValue());
    BigDecimal molarity1 = new BigDecimal(molarity);
    
    double soluteVol = MolarityCalculator.calculateDilutionVol(molarity, 10, 100);
    double solventVol = 100 - soluteVol;
    
    DecimalFormat decimalFormat = new DecimalFormat("#####0.0000");
    
    this.xmlResult = "<MolarityCalc concentration=\"" + decimalFormat.format(molarity) + "nM\"" + " soluteVol=\"" + decimalFormat.format(soluteVol) + "ul\"" +  " solventVol=\"" + decimalFormat.format(solventVol) + "ul\"" + "/>";
    
  }

  public Command execute() throws RollBackCommandException {
    
     
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}