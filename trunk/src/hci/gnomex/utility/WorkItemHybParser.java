package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Request;
import hci.gnomex.model.Slide;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.WorkItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class WorkItemHybParser implements Serializable {
  
  protected Document   doc;
  protected Map        hybMap = new HashMap();
  protected List       workItems = new ArrayList();
  
  public WorkItemHybParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element workItemListNode = this.doc.getRootElement();
    
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      
      String idLabeledSampleString = workItemNode.getAttributeValue("idHybridization");
      String idWorkItemString      = workItemNode.getAttributeValue("idWorkItem");
      
      Hybridization hybridization = (Hybridization)sess.load(Hybridization.class, new Integer(idLabeledSampleString));
      WorkItem workItem = (WorkItem)sess.load(WorkItem.class, new Integer(idWorkItemString));
      
      
      this.initializeHybridization(sess, workItemNode, hybridization);
      
      hybMap.put(workItem.getIdWorkItem(), hybridization);
      workItems.add(workItem);
    }
    
   
  }
  
  protected void initializeHybridization(Session sess, Element n, Hybridization hyb) throws Exception {
    if (n.getAttributeValue("hybStatus") != null && !n.getAttributeValue("hybStatus").equals("")) {
      String status = n.getAttributeValue("hybStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hyb.setHybDate(new java.sql.Date(System.currentTimeMillis()));      
        hyb.setHybFailed("N");
        hyb.setHybBypassed("N");
        
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hyb.setHybDate(null);      
        hyb.setHybFailed("Y");
        hyb.setHybBypassed("N");
        
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hyb.setHybDate(null);      
        hyb.setHybFailed("N");
        hyb.setHybBypassed("Y");
      }
    } else {
      hyb.setHybDate(null);      
      hyb.setHybFailed("N");
      hyb.setHybBypassed("N");
    }
    

    
    
    
    Integer idSlideDesign = n.getAttributeValue("idSlideDesign") == null || n.getAttributeValue("idSlideDesign").equals("") ? null : new Integer(n.getAttributeValue("idSlideDesign"));
    Integer idRequest = n.getAttributeValue("idRequest") == null || n.getAttributeValue("idRequest").equals("") ? null : new Integer(n.getAttributeValue("idRequest"));

    Slide slide = hyb.getSlide();
    sess.flush();  // We need to save what we have, because the getSlideForHyb will do a refresh on the hybs
    slide = getSlideForHyb(sess, hyb, idSlideDesign, n.getAttributeValue("slideBarcode"), idRequest);

    // If this is not an existing slide, instantiate a new one.
    if (slide == null) {
      slide = new Slide();
      
      // If we are switching out the old slide, we need to delete the old one if there are not any references to it.
      if (hyb.getSlide() != null) {
        deleteOrphanSlide(sess, hyb, idRequest); 
      }
    }
    slide.setIdSlideDesign(new Integer(n.getAttributeValue("idSlideDesign")));          
    
    // Set the barcode on the slide.
    if (n.getAttributeValue("slideBarcode") != null && !n.getAttributeValue("slideBarcode").equals("")) {
      slide.setBarcode(n.getAttributeValue("slideBarcode"));
    } else {
      slide.setBarcode(null);     
    }
    
    // If this is a new slide, save it.
    sess.save(slide);
    hyb.setIdSlide(slide.getIdSlide());

    // Set the array coordinate on the hyb
    setArrayCoordinate(sess, hyb, slide, n.getAttributeValue("arrayCoordinate"), idRequest);
    
    if (n.getAttributeValue("idHybProtocol") != null && !n.getAttributeValue("idHybProtocol").equals("")) {
      hyb.setIdHybProtocol(new Integer(n.getAttributeValue("idHybProtocol")));
    } else {
      hyb.setIdHybProtocol(null);
    }
    
    
    // Save the hyb -- we need to do this here because we look at all of the hybs
    // on the request to find "matching" slides.  If we don't flush here,
    // the other slides that may have been saved on other slides won't be flushed
    // on the request.
    sess.flush();
 
  }
  
  public static Slide getSlideForHyb(Session sess, Hybridization hyb, Integer idSlideDesign, String slideBarcode, Integer idRequest) throws InvalidValueException{
    Slide slide = hyb.getSlide();
    SlideProduct slideProduct = null;

    // We will need to check for existing slides if this we are instantiating one or
    // we are changing the barcode on an existing slide.
    boolean checkForExistingSlides = false;
    if (slide == null) {
      checkForExistingSlides = true;
    } else if (slideBarcode != null  && !slideBarcode.equals("")) {
      if (slide.getBarcode() != null && !slide.getBarcode().equals(slideBarcode)) {
        slide = null;
        checkForExistingSlides = true;
      }
    }
    
    
    if (checkForExistingSlides) {
      if (idSlideDesign != null && !idSlideDesign.equals("")) {
        List slideProducts = sess.createQuery("SELECT sp from SlideProduct sp join sp.slideDesigns sd where sd.idSlideDesign = " + idSlideDesign).list();
        if (slideProducts.size() > 0) {
          slideProduct = (SlideProduct)slideProducts.get(0);          
        }
        if (slideProduct == null) {
          throw new InvalidValueException("Unable to find slide product for slide design " + idSlideDesign);
        }
      } else {
        throw new InvalidValueException("Cannot instantiate a slide without an idSlideDesign");
      }
      

      // If this is a multi-array slide, check other hybs to see if same slide is being used.
      if (slideProduct.getArraysPerSlide() != null && slideProduct.getArraysPerSlide().intValue() > 1) {
        if (slideBarcode != null && !slideBarcode.equals("")) {
          Request request = (Request)sess.get(Request.class, idRequest);
          for(Iterator i = request.getHybridizations().iterator(); i.hasNext();) {
            Hybridization h = (Hybridization)i.next();
            // We need refresh here otherwise latest hyb slides will not be reflected.
            sess.refresh(h);
            if (h.getSlide() != null && h.getSlide().getBarcode() != null && 
                h.getSlide().getBarcode().equals(slideBarcode)) {
              slide = h.getSlide();
              break;
            }
          }    
        }
      }
    }
    return slide;
    
  }
  
  
  public static void deleteOrphanSlide(Session sess, Hybridization hyb, Integer idRequest) {
    boolean isOrphan = true;
    
    Request request = (Request)sess.get(Request.class, idRequest);
    for(Iterator i = request.getHybridizations().iterator(); i.hasNext();) {
      Hybridization h = (Hybridization)i.next();
      // We need refresh here otherwise latest hyb slides will not be reflected.
      sess.refresh(h);
      
      if (h.getIdSlide() != null &&
          !h.getIdHybridization().equals(hyb.getIdHybridization()) &&
          h.getIdSlide().equals(hyb.getIdSlide())) {
        isOrphan = false;
        break;
      }
    }   
    
    if (isOrphan) {
      sess.delete(hyb.getSlide());
    }    
  }
  
  public static void setArrayCoordinate(Session sess, Hybridization hyb, Slide slide, String arrayCoordinate, Integer idRequest)
   throws InvalidValueException {
    if (arrayCoordinate != null && !arrayCoordinate.equals("")) {
      Integer idArrayCoordinate = null;
      List validArrayCoordinates = sess.createQuery("SELECT ac from ArrayCoordinate ac where ac.idSlideDesign = " + slide.getIdSlideDesign()).list();
      for (Iterator i = validArrayCoordinates.iterator(); i.hasNext();) {
        ArrayCoordinate ac = (ArrayCoordinate)i.next();
        if (ac.getName().equals(arrayCoordinate)) {
          idArrayCoordinate = ac.getIdArrayCoordinate();
        }
      }
      if (idArrayCoordinate != null) {
        hyb.setIdArrayCoordinate(idArrayCoordinate);        
      } else {
        throw new InvalidValueException("The array coordinate " + arrayCoordinate + " is not valid for the slide used in hyb " + hyb.getNumber());
      }
      
    } else {
      hyb.setIdArrayCoordinate(null);
    }
    
  }

  public Hybridization getHyb(Integer idWorkItem) {
    return (Hybridization)hybMap.get(idWorkItem);
  }
  
  public List getWorkItems() {
    return workItems;
  }
  
  
  public void resetIsDirty() {
    Element workItemListNode = this.doc.getRootElement();
    
    for(Iterator i = workItemListNode.getChildren("WorkItem").iterator(); i.hasNext();) {
      Element workItemNode = (Element)i.next();
      workItemNode.setAttribute("isDirty", "N");
    }
  }


  


}
