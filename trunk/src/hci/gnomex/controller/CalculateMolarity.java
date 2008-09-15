package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MolarityCalculator;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.NumberFormatter;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;


public class CalculateMolarity extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CalculateMolarity.class);
  
  
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