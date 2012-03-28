package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PlateWellParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SavePlate extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePlate.class);
  
  private int                   idInstrumentRun = 0;
  private int                   idPlate;
  private boolean               isNew = true;
  private String                plateWellXMLString;
  private Document              wellsDoc;
  private PlateWellParser       wellParser;
  
  // plate index -- this corresponds to quadrant
  private int                   plateIndex = 0;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idPlate") != null && !request.getParameter("idPlate").equals("")) {
      idPlate = Integer.parseInt(request.getParameter("idPlate"));
      isNew = false;
    } 
    if (request.getParameter("plateIndex") != null && !request.getParameter("plateIndex").equals("")) {
      plateIndex = Integer.parseInt(request.getParameter("plateIndex"));
    } 
    if (request.getParameter("idInstrumentRun") != null && !request.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.parseInt(request.getParameter("idInstrumentRun"));
    } 
    
    if (request.getParameter("plateWellXMLString") != null
        && !request.getParameter("plateWellXMLString").equals("")) {
      plateWellXMLString = request.getParameter("plateWellXMLString");
    }

    StringReader reader = new StringReader(plateWellXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      wellsDoc = sax.build(reader);
      wellParser = new PlateWellParser(wellsDoc);
    }
    catch (JDOMException je) {
      log.error("Cannot parse wellXMLString", je);
      this.addInvalidField("wellXMLString", "Invalid wellXMLString");
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      wellParser.parse(sess);
      
      Plate plate;
      
      if(isNew) {
        plate = new Plate();
        sess.save(plate);
      } else {
        plate = (Plate) sess.get(Plate.class, idPlate);
      }
      
      if ( idInstrumentRun != 0 ) {
        plate.setIdInstrumentRun(idInstrumentRun);
      }
      
      idPlate = plate.getIdPlate();
      
      
      //
      // Remove wells
      //
      TreeSet wellsToDelete = new TreeSet(new WellComparator());
      for(Iterator i = plate.getPlateWells().iterator(); i.hasNext();) {
        PlateWell existingWell = (PlateWell)i.next();
        if (!wellParser.getWellMap().containsKey(existingWell.getIdPlateWell().toString())) {
          wellsToDelete.add(existingWell);
          
        }
      }
      for (Iterator i = wellsToDelete.iterator(); i.hasNext();) {
        PlateWell wellToDelete = (PlateWell)i.next();
        plate.getPlateWells().remove(wellToDelete);
      }
      //
      // Save wells
      //
      TreeSet<PlateWell> wellsToAdd = new TreeSet<PlateWell>(new WellComparator());
      for (Iterator i = wellParser.getWellMap().keySet().iterator(); i.hasNext();) {
        String idPlateWellString = (String) i.next();
        PlateWell pw = 
          (PlateWell) wellParser.getWellMap().get(idPlateWellString);
        
        pw.setIdPlate(idPlate);
        pw.setPlate(plate);
        
        boolean exists = false;
        for(Iterator i1 = plate.getPlateWells().iterator(); i1.hasNext();) {
          PlateWell existingWell = (PlateWell)i1.next();
          if (existingWell.getIdPlateWell() == pw.getIdPlateWell() ) {
            exists = true;
          }
        }
        
        // New flow cell channel -- add it to the list
        if (!exists) {
          wellsToAdd.add(pw);
        }
      }
      plate.getPlateWells().addAll(wellsToAdd);
        
      sess.flush();
        
      Document doc = new Document(new Element("SUCCESS"));
      Element pNode = new Element("SavedPlate");
      pNode.setAttribute("idPlate",String.valueOf(idPlate));
      pNode.setAttribute("idInstrumentRun",String.valueOf(idInstrumentRun));
      pNode.setAttribute("plateIndex",String.valueOf(plateIndex));
      
      List plateWells = sess.createQuery("SELECT pw from PlateWell as pw where pw.idPlate=" + idPlate).list();

      for(Iterator i = plateWells.iterator(); i.hasNext();) {
        PlateWell plateWell = (PlateWell)i.next();
        plateWell.excludeMethodFromXML("getPlate");
        
        Element node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        
        pNode.addContent(node);
      }
      
      doc.getRootElement().addContent(pNode);
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (Exception e){
      log.error("An exception has occurred in SavePlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private class WellComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      PlateWell u1 = (PlateWell) o1;
      PlateWell u2 = (PlateWell) o2;
      
      return u1.getIdPlateWell().compareTo(u2.getIdPlateWell());
    }
  }
  
}